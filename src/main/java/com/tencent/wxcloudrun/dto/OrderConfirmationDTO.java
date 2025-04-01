package com.tencent.wxcloudrun.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderConfirmationDTO {
    private String orderNumber;
    private String customerName;
    private String customerPhone;
    private String orderDate;
    private String installAddress;
    private List<OrderRoomDTO> rooms;
    private String customerNotes;
    private List<OrderMaterialDTO> materials;
    
    @Data
    public static class OrderRoomDTO {
        private String roomName;
        private String roomSize;
        private String notes;
    }
    
    @Data
    public static class OrderMaterialDTO {
        private String materialName;
        private String spec;
        private Double quantity;
        private String unit;
        private Double unitPrice;
    }
}
