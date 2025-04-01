package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Integer id;
    private Long userId;
    private String nickname;
    private String phone;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private BigDecimal paidAmount;
    private Integer addressId;
    private UserAddressDTO address;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderRoomDTO> rooms;
    private User userInfo;
    
    @Data
    public static class RoomItemDTO {
        private Integer id;
        private Integer roomId;
        private String model;
        private Integer materialId;
        private String materialName;
        private MaterialDTO material;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private String remark;
    }
    
    @Data
    public static class MaterialDTO {
        private Integer id;
        private String name;
        private String code;
        private String specification;
        private String unit;
        private BigDecimal price;
    }
    
    @Data
    public static class OrderRoomDTO {
        private Integer id;
        private Integer orderId;
        private String roomName;
        private BigDecimal windowWidth;
        private BigDecimal windowHeight;
        private String remarks;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<RoomAttachmentDTO> attachments;
        private List<RoomItemDTO> items;
    }

    @Data
    public static class RoomAttachmentDTO {
        private Long id;
        private String fileName;
        private String fileType;
        private String thumbFileName;
    }
}
