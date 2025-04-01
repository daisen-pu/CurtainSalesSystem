package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.entity.Material;
import com.tencent.wxcloudrun.entity.MaterialAttachment;
import com.tencent.wxcloudrun.entity.MaterialStockRecord;
import com.tencent.wxcloudrun.entity.User;
import com.tencent.wxcloudrun.mapper.MaterialMapper;
import com.tencent.wxcloudrun.mapper.MaterialStockRecordMapper;
import com.tencent.wxcloudrun.service.MaterialAttachmentService;
import com.tencent.wxcloudrun.service.MaterialService;
import com.tencent.wxcloudrun.service.SupplierService;
import com.tencent.wxcloudrun.service.UserService;
import com.tencent.wxcloudrun.utils.FileUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialMapper materialMapper;
    private final MaterialStockRecordMapper stockRecordMapper;
    private final MaterialAttachmentService materialAttachmentService;
    private final UserService userService;
    private final SupplierService supplierService;

    @Value("${wechat.miniapp.appid}")
    private String appId;

    @Override
    public IPage<MaterialDTO> queryMaterialList(MaterialQueryDTO queryDTO) {
        Page<Material> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.like(Material::getName, queryDTO.getKeyword())
                  .or()
                  .like(Material::getCode, queryDTO.getKeyword());
        }
        wrapper.orderByDesc(Material::getUpdatedAt);
        
        IPage<Material> materialPage = materialMapper.selectPage(page, wrapper);
        return materialPage.convert(this::convertToDTO);
    }

    @Override
    public MaterialDTO getMaterialDetail(Integer id) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException("材料不存在");
        }
        MaterialDTO dto = convertToDTO(material);
        
        // 获取附件列表
        List<MaterialAttachment> attachments = materialAttachmentService.getAttachmentsByMaterialId(id);
        dto.setAttachments(attachments.stream().map(this::convertToAttachmentDTO).collect(Collectors.toList()));
        
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaterialDTO createMaterial(MaterialDTO materialDTO) {
        // 检查编码是否重复
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Material::getCode, materialDTO.getCode());
        if (materialMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("材料编码已存在");
        }
        
        Material material = new Material();
        BeanUtils.copyProperties(materialDTO, material);
        material.setStock(BigDecimal.ZERO);
        material.setCreatedAt(LocalDateTime.now());
        material.setUpdatedAt(LocalDateTime.now());
        
        materialMapper.insert(material);

        // 保存附件
        if (materialDTO.getAttachments() != null && !materialDTO.getAttachments().isEmpty()) {
            for (MaterialAttachmentDTO attachmentDTO : materialDTO.getAttachments()) {
                MaterialAttachment attachment = new MaterialAttachment();
                BeanUtils.copyProperties(attachmentDTO, attachment);
                attachment.setMaterialId(material.getId());
                materialAttachmentService.addAttachment(attachment);
            }
        }

        return convertToDTO(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaterialDTO updateMaterial(MaterialDTO materialDTO) {
        Material material = materialMapper.selectById(materialDTO.getId());
        if (material == null) {
            throw new BusinessException("材料不存在");
        }
        
        // 检查编码是否重复
        if (!material.getCode().equals(materialDTO.getCode())) {
            LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Material::getCode, materialDTO.getCode());
            if (materialMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("材料编码已存在");
            }
        }
        
        BeanUtils.copyProperties(materialDTO, material);
        material.setUpdatedAt(LocalDateTime.now());
        
        materialMapper.updateById(material);

        // 更新附件
        materialAttachmentService.deleteAttachmentsByMaterialId(material.getId());
        if (materialDTO.getAttachments() != null && !materialDTO.getAttachments().isEmpty()) {
            for (MaterialAttachmentDTO attachmentDTO : materialDTO.getAttachments()) {
                MaterialAttachment attachment = new MaterialAttachment();
                BeanUtils.copyProperties(attachmentDTO, attachment);
                attachment.setMaterialId(material.getId());
                materialAttachmentService.addAttachment(attachment);
            }
        }

        return convertToDTO(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaterial(Integer id) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException("材料不存在");
        }
        
        // 检查是否有库存
        if (material.getStock().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("材料还有库存，不能删除");
        }
        
        materialMapper.deleteById(id);
        materialAttachmentService.deleteAttachmentsByMaterialId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialIn(Integer id, MaterialStockDTO stockDTO) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException("材料不存在");
        }
        
        BigDecimal beforeStock = material.getStock();
        BigDecimal afterStock = beforeStock.add(stockDTO.getQuantity());
        
        // 更新库存
        material.setStock(afterStock);
        material.setUpdatedAt(LocalDateTime.now());
        materialMapper.updateById(material);
        
        // 记录库存变动
        MaterialStockRecord record = new MaterialStockRecord();
        record.setMaterialId(id);
        record.setType("in");
        record.setQuantity(stockDTO.getQuantity());
        record.setBeforeStock(beforeStock);
        record.setAfterStock(afterStock);
        record.setRemark(stockDTO.getRemark());
        record.setOperatorId(userService.getCurrentUser().getId());
        record.setCreatedAt(LocalDateTime.now());
        
        stockRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialOut(Integer id, MaterialStockDTO stockDTO) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException("材料不存在");
        }
        
        BigDecimal beforeStock = material.getStock();
        if (beforeStock.compareTo(stockDTO.getQuantity()) < 0) {
            throw new BusinessException("库存不足");
        }
        
        BigDecimal afterStock = beforeStock.subtract(stockDTO.getQuantity());
        
        // 更新库存
        material.setStock(afterStock);
        material.setUpdatedAt(LocalDateTime.now());
        materialMapper.updateById(material);
        
        // 记录库存变动
        MaterialStockRecord record = new MaterialStockRecord();
        record.setMaterialId(id);
        record.setType("out");
        record.setQuantity(stockDTO.getQuantity());
        record.setBeforeStock(beforeStock);
        record.setAfterStock(afterStock);
        record.setRemark(stockDTO.getRemark());
        record.setOperatorId(userService.getCurrentUserId());
        record.setCreatedAt(LocalDateTime.now());
        
        stockRecordMapper.insert(record);
    }

    @Override
    public IPage<MaterialStockRecordDTO> queryStockRecords(MaterialStockQueryDTO queryDTO) {
        Page<MaterialStockRecord> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        LambdaQueryWrapper<MaterialStockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockRecord::getMaterialId, queryDTO.getMaterialId());
        if (StringUtils.hasText(queryDTO.getType())) {
            wrapper.eq(MaterialStockRecord::getType, queryDTO.getType());
        }
        wrapper.orderByDesc(MaterialStockRecord::getCreatedAt);
        
        IPage<MaterialStockRecord> recordPage = stockRecordMapper.selectPage(page, wrapper);
        return recordPage.convert(this::convertToStockRecordDTO);
    }

    @Override
    public String generateQrCode(Integer id) {
        try {
            // 使用微信小程序 URL Scheme
            String path = String.format("pages/material/view/index?id=%d", id);
            String content = String.format("weixin://dl/business/?t=DJI1eXFxVUx&appid=%s&path=%s", 
                appId, path);
            
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new BusinessException("生成二维码失败: " + e.getMessage());
        }
    }

    private MaterialDTO convertToDTO(Material material) {
        if (material == null) {
            return null;
        }
        
        MaterialDTO dto = new MaterialDTO();
        BeanUtils.copyProperties(material, dto);
        
        if (material.getSupplierId() != null) {
            dto.setSupplier(supplierService.getById(material.getSupplierId()));
        }
        // 获取附件列表
        List<MaterialAttachment> attachments = materialAttachmentService.getAttachmentsByMaterialId(dto.getId());
        dto.setAttachments(attachments.stream().map(this::convertToAttachmentDTO).collect(Collectors.toList()));

        return dto;
    }

    private MaterialAttachmentDTO convertToAttachmentDTO(MaterialAttachment attachment) {
        if (attachment == null) {
            return null;
        }
        MaterialAttachmentDTO dto = new MaterialAttachmentDTO();
        BeanUtils.copyProperties(attachment, dto);
        return dto;
    }

    private MaterialStockRecordDTO convertToStockRecordDTO(MaterialStockRecord record) {
        if (record == null) {
            return null;
        }
        MaterialStockRecordDTO dto = new MaterialStockRecordDTO();
        BeanUtils.copyProperties(record, dto);
        
        // 获取材料名称
        Material material = materialMapper.selectById(record.getMaterialId());
        if (material != null) {
            dto.setMaterialName(material.getName());
        }
        
        // 获取操作人姓名
        User operator = userService.getUserOperatorById(record.getOperatorId());
        if (operator != null) {
            dto.setOperatorName(operator.getNickname());
        }
        
        return dto;
    }
}
