package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.dto.MaterialStockRecordDTO;
import com.tencent.wxcloudrun.entity.MaterialStockRecord;
import com.tencent.wxcloudrun.service.MaterialStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/materials/{materialId}/stock")
@RequiredArgsConstructor
public class MaterialStockController {
    private final MaterialStockService materialStockService;

    @PostMapping
    public Result<Void> updateStock(
            @PathVariable Integer materialId,
            @RequestParam BigDecimal change,
            @RequestParam(required = false) String remark
    ) {
        materialStockService.updateStock(materialId, change, remark);
        return Result.success();
    }

    @GetMapping("/records")
    public Result<IPage<MaterialStockRecordDTO>> getStockRecords(
            @PathVariable Integer materialId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        IPage<MaterialStockRecordDTO> page = materialStockService.getStockRecords(materialId, current, size);
        return Result.success(page);
    }
}
