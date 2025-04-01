package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.dto.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public interface MaterialService {
    IPage<MaterialDTO> queryMaterialList(MaterialQueryDTO queryDTO);
    
    MaterialDTO getMaterialDetail(Integer id);
    
    MaterialDTO createMaterial(MaterialDTO materialDTO);
    
    MaterialDTO updateMaterial(MaterialDTO materialDTO);
    
    void deleteMaterial(Integer id);
    
    void materialIn(Integer id, MaterialStockDTO stockDTO);
    
    void materialOut(Integer id, MaterialStockDTO stockDTO);
    
    IPage<MaterialStockRecordDTO> queryStockRecords(MaterialStockQueryDTO queryDTO);
    
    String generateQrCode(Integer id);
}
