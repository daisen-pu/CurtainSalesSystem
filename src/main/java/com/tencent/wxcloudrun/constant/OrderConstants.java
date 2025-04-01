package com.tencent.wxcloudrun.constant;

/**
 * 订单相关常量类
 * 包含订单统计、订单状态、支付状态等相关常量定义
 *
 * @author Curtain
 * @since 1.0
 */
public class OrderConstants {
    /**
     * 订单统计数据键名常量
     * 用于统一管理订单统计相关的Map键名，避免硬编码
     */
    public static class StatisticsKey {
        /** 今日订单统计数据 */
        public static final String TODAY = "today";
        /** 本月订单统计数据 */
        public static final String MONTH = "month";
        /** 总体订单统计数据 */
        public static final String TOTAL = "total";
        /** 用户订单统计数据 */
        public static final String USER_STATS = "userStats";
        
        /** 订单总数 */
        public static final String TOTAL_ORDERS = "totalOrders";
        /** 待处理订单数 */
        public static final String PENDING_ORDERS = "pendingOrders";
        /** 处理中订单数 */
        public static final String PROCESSING_ORDERS = "processingOrders";
        /** 已完成订单数 */
        public static final String COMPLETED_ORDERS = "completedOrders";
        /** 已取消订单数 */
        public static final String CANCELLED_ORDERS = "cancelledOrders";
        /** 未支付订单数 */
        public static final String UNPAID_ORDERS = "unpaidOrders";
        /** 部分支付订单数 */
        public static final String PARTIAL_PAID_ORDERS = "partialPaidOrders";
        /** 已支付订单数 */
        public static final String PAID_ORDERS = "paidOrders";
        /** 订单总金额 */
        public static final String TOTAL_AMOUNT = "totalAmount";
        /** 已支付总金额 */
        public static final String TOTAL_PAID_AMOUNT = "totalPaidAmount";

        private StatisticsKey() {
            throw new IllegalStateException("Utility class");
        }
    }

    /**
     * 订单状态常量
     * 用于定义订单的生命周期状态
     */
    public static class Status {
        /** 待处理：订单刚创建，待确认 */
        public static final String CONFIRM = "confirm";
        /** 待处理：订单确认之后，等待处理 */
        public static final String PENDING = "pending";
        /** 处理中：订单正在处理过程中 */
        public static final String PROCESSING = "processing";
        /** 已完成：订单已经完成所有处理 */
        public static final String COMPLETED = "completed";
        /** 已取消：订单被取消 */
        public static final String CANCELLED = "cancelled";

        private Status() {
            throw new IllegalStateException("Utility class");
        }
    }

    /**
     * 支付状态常量
     * 用于定义订单的支付状态
     */
    public static class PaymentStatus {
        /** 未支付：订单还未收到任何支付 */
        public static final String UNPAID = "unpaid";
        /** 部分支付：订单收到部分支付，但未付清 */
        public static final String PARTIAL = "partial";
        /** 已支付：订单已经付清 */
        public static final String PAID = "paid";

        private PaymentStatus() {
            throw new IllegalStateException("Utility class");
        }
    }

    private OrderConstants() {
        throw new IllegalStateException("Utility class");
    }
}
