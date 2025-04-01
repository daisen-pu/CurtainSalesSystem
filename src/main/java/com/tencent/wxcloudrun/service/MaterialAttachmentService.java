package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.entity.MaterialAttachment;
import java.util.List;

public interface MaterialAttachmentService {
    List<MaterialAttachment> getAttachmentsByMaterialId(Integer materialId);
    void addAttachment(MaterialAttachment attachment);
    void deleteAttachment(Integer id);
    void deleteAttachmentsByMaterialId(Integer materialId);
}
