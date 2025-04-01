package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("material_attachments")
public class MaterialAttachment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer materialId;
    private String fileName;
    private String fileType;
    private String thumbFileName;  // 新增缩略图字段
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
