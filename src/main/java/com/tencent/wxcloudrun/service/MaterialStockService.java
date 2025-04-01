package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.dto.MaterialStockRecordDTO;

import java.math.BigDecimal;

public interface MaterialStockService {
    /**
     * 更新库存
     * @param materialId 物料ID
     * @param change 变更数量（正数表示入库，负数表示出库）
     * @param remark 备注
     */
    void updateStock(Integer materialId, BigDecimal change, String remark);

    /**
     * 获取库存记录
     * @param materialId 物料ID
     * @param current 当前页
     * @param size 每页大小
     * @return 库存记录分页数据
     */
    IPage<MaterialStockRecordDTO> getStockRecords(Integer materialId, Integer current, Integer size);
}
