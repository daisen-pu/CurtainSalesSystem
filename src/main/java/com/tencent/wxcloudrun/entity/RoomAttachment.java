package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("room_attachment")
public class RoomAttachment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer roomId;
    private String fileName;
    private String fileType;
    private String thumbFileName;  
    private LocalDateTime createdAt;
}
