package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.entity.MaterialAttachment;
import com.tencent.wxcloudrun.mapper.MaterialAttachmentMapper;
import com.tencent.wxcloudrun.service.MaterialAttachmentService;
import com.tencent.wxcloudrun.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialAttachmentServiceImpl implements MaterialAttachmentService {
    private final MaterialAttachmentMapper materialAttachmentMapper;

    @Override
    public List<MaterialAttachment> getAttachmentsByMaterialId(Integer materialId) {
        return materialAttachmentMapper.selectList(
            new LambdaQueryWrapper<MaterialAttachment>()
                .eq(MaterialAttachment::getMaterialId, materialId)
        );
    }

    @Override
    public void addAttachment(MaterialAttachment attachment) {
        attachment.setCreatedAt(LocalDateTime.now());
        attachment.setUpdatedAt(LocalDateTime.now());
        attachment.setFileName(FileUtil.subFilename(attachment.getFileName()));
        attachment.setThumbFileName(FileUtil.subFilename(attachment.getThumbFileName()));
        materialAttachmentMapper.insert(attachment);
    }

    @Override
    public void deleteAttachment(Integer id) {
        materialAttachmentMapper.deleteById(id);
    }

    @Override
    public void deleteAttachmentsByMaterialId(Integer materialId) {
        materialAttachmentMapper.delete(
            new LambdaQueryWrapper<MaterialAttachment>()
                .eq(MaterialAttachment::getMaterialId, materialId)
        );
    }
}
