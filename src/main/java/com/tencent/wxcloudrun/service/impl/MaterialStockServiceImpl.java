package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.dto.MaterialStockRecordDTO;
import com.tencent.wxcloudrun.entity.Material;
import com.tencent.wxcloudrun.entity.MaterialStockRecord;
import com.tencent.wxcloudrun.mapper.MaterialMapper;
import com.tencent.wxcloudrun.mapper.MaterialStockRecordMapper;
import com.tencent.wxcloudrun.service.MaterialStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialStockServiceImpl implements MaterialStockService {
    private final MaterialMapper materialMapper;
    private final MaterialStockRecordMapper stockRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Integer materialId, BigDecimal change, String remark) {
        // 获取物料信息
        Material material = materialMapper.selectById(materialId);
        if (material == null) {
            throw new BusinessException("物料不存在");
        }

        // 如果是出库，检查库存是否足够
        if (change.compareTo(BigDecimal.ZERO) < 0 && material.getStock().add(change).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("库存不足");
        }

        // 更新库存
        material.setStock(material.getStock().add(change));
        material.setUpdatedAt(LocalDateTime.now());
        materialMapper.updateById(material);

        // 记录库存变更
        MaterialStockRecord record = new MaterialStockRecord();
        record.setMaterialId(materialId);
        record.setQuantity(change.abs());
        record.setType(change.compareTo(BigDecimal.ZERO) > 0 ? "in" : "out");
        record.setRemark(remark);
        record.setCreatedAt(LocalDateTime.now());
        stockRecordMapper.insert(record);
    }

    @Override
    public IPage<MaterialStockRecordDTO> getStockRecords(Integer materialId, Integer current, Integer size) {
        Page<MaterialStockRecord> page = new Page<>(current, size);
        
        LambdaQueryWrapper<MaterialStockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockRecord::getMaterialId, materialId)
              .orderByDesc(MaterialStockRecord::getCreatedAt);
        
        IPage<MaterialStockRecord> recordPage = stockRecordMapper.selectPage(page, wrapper);
        
        return recordPage.convert(record -> {
            MaterialStockRecordDTO dto = new MaterialStockRecordDTO();
            BeanUtils.copyProperties(record, dto);
            return dto;
        });
    }
}
