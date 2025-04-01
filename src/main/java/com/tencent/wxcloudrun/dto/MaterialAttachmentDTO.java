package com.tencent.wxcloudrun.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class MaterialAttachmentDTO {
    private Integer id;
    private Integer materialId;
    
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    private String thumbFileName;
    
    @NotBlank(message = "文件类型不能为空")
    private String fileType;
}
