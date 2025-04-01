package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif"
    ));

    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
            "mp4", "mov", "avi"
    ));

    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("开始上传图片：{}", file.getOriginalFilename());
        return uploadFile(file, ALLOWED_IMAGE_EXTENSIONS);
    }

    @PostMapping("/video")
    public Result<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            // 先上传视频文件
            Result<String> videoResult = uploadFile(file, ALLOWED_VIDEO_EXTENSIONS);
            if (StringUtils.isEmpty(videoResult.getData())) {
                return Result.error(videoResult.getMessage());
            }

            return Result.success(videoResult.getData());
        } catch (Exception e) {
            log.error("上传视频失败", e);
            return Result.error("上传视频失败");
        }
    }

    @PostMapping("/video/thumb")
    public Result<String> uploadVideoThumb(@RequestParam("file") MultipartFile file) {
        try {
            Result<String> thumbResult = uploadFile(file, ALLOWED_IMAGE_EXTENSIONS);
            if (StringUtils.isEmpty(thumbResult.getData())) {
                return Result.error(thumbResult.getMessage());
            }
            return Result.success(thumbResult.getData());
        } catch (Exception e) {
            log.error("上传视频缩略图失败", e);
            return Result.error("上传视频缩略图失败");
        }
    }

    private Result<String> uploadFile(MultipartFile file, Set<String> allowedExtensions) {
        try {
            if (file == null) {
                log.error("文件为空");
                return Result.error("请选择文件");
            }

            if (file.isEmpty()) {
                log.error("文件内容为空");
                return Result.error("请选择文件");
            }

            String originalFilename = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
            log.info("处理文件：{}, 大小：{}", originalFilename, file.getSize());

            if (originalFilename.contains("..")) {
                log.error("文件名不合法：{}", originalFilename);
                return Result.error("文件名不合法");
            }

            String fileExtension = org.springframework.util.StringUtils.getFilenameExtension(originalFilename);
            if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
                log.error("不支持的文件类型：{}", fileExtension);
                return Result.error("不支持的文件类型");
            }

            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            log.info("上传目录：{}", uploadPath);
            
            if (!Files.exists(uploadPath)) {
                log.info("创建上传目录");
                Files.createDirectories(uploadPath);
            }

            // 生成文件名
            String newFilename = UUID.randomUUID().toString() + "." + fileExtension;
            Path targetLocation = uploadPath.resolve(newFilename);
            log.info("目标文件路径：{}", targetLocation);

            // 保存文件
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                log.info("文件保存成功：{}", newFilename);
            } catch (IOException e) {
                log.error("保存文件时发生IO错误", e);
                throw e;
            }

            // 返回文件名
            log.info("返回文件名：{}", newFilename);
            return Result.success(newFilename);
        } catch (IOException ex) {
            log.error("文件上传失败", ex);
            return Result.error("文件上传失败：" + ex.getMessage());
        } catch (Exception ex) {
            log.error("未知错误", ex);
            return Result.error("服务器错误：" + ex.getMessage());
        }
    }

    // 新增：删除已上传的文件
    private void deleteUploadedFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("删除文件失败：" + fileName, e);
        }
    }

    // 新增：视频上传结果类
    @Data
    @AllArgsConstructor
    public static class VideoUploadResult {
        private String fileName;
        private String thumbFileName;
    }
}
