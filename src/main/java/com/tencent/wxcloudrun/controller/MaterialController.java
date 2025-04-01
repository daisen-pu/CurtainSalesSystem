package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @GetMapping("/list")
    public Result<IPage<MaterialDTO>> getMaterialList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        
        MaterialQueryDTO query = new MaterialQueryDTO();
        query.setPage(current);
        query.setSize(size);
        query.setKeyword(keyword);
        
        return Result.success(materialService.queryMaterialList(query));
    }

    @GetMapping("/{id}")
    public Result<MaterialDTO> getMaterialDetail(@PathVariable Integer id) {
        return Result.success(materialService.getMaterialDetail(id));
    }

    @PostMapping
    public Result<MaterialDTO> createMaterial(@Validated @RequestBody MaterialDTO materialDTO) {
        return Result.success(materialService.createMaterial(materialDTO));
    }

    @PutMapping("/{id}")
    public Result<MaterialDTO> updateMaterial(
            @PathVariable Integer id,
            @Validated @RequestBody MaterialDTO materialDTO) {
        materialDTO.setId(id);
        return Result.success(materialService.updateMaterial(materialDTO));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMaterial(@PathVariable Integer id) {
        materialService.deleteMaterial(id);
        return Result.success();
    }

    @PostMapping("/{id}/stock/in")
    public Result<Void> materialIn(
            @PathVariable Integer id,
            @Validated @RequestBody MaterialStockDTO stockDTO) {
        materialService.materialIn(id, stockDTO);
        return Result.success();
    }

    @PostMapping("/{id}/stock/out")
    public Result<Void> materialOut(
            @PathVariable Integer id,
            @Validated @RequestBody MaterialStockDTO stockDTO) {
        materialService.materialOut(id, stockDTO);
        return Result.success();
    }

    @GetMapping("/{id}/stock/records")
    public Result<IPage<MaterialStockRecordDTO>> getStockRecords(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        
        MaterialStockQueryDTO query = new MaterialStockQueryDTO();
        query.setMaterialId(id);
        query.setPage(current);
        query.setSize(size);
        
        return Result.success(materialService.queryStockRecords(query));
    }

    @GetMapping("/qrcode/{id}")
    public Result<String> generateQrCode(@PathVariable Integer id) {
        return Result.success(materialService.generateQrCode(id));
    }
}
