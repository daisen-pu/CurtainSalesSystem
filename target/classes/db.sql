CREATE TABLE `Counters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count` int(11) NOT NULL DEFAULT '1',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : 127.0.0.1:3306
 Source Schema         : curtain_sales_system

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 01/04/2025 08:36:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for suppliers
-- ----------------------------
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers`  (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '供应商名称',
                              `contact` varchar(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '联系人',
                              `phone` varchar(20) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '联系电话',
                              `address` text CHARACTER SET utf8mb4 NULL COMMENT '地址',
                              `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `idx_name`(`name`) USING BTREE,
                              INDEX `idx_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COMMENT = '供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of suppliers
-- ----------------------------
INSERT INTO `suppliers` VALUES (1, '北京布艺有限公司', '张经理asdfasdf', '13800138001', '北京市朝阳区建国路88号', '2025-02-14 11:56:13', '2025-02-25 13:59:31');
INSERT INTO `suppliers` VALUES (2, '上海窗帘制造厂', '李总', '13900139002', '上海市浦东新区陆家嘴1号', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `suppliers` VALUES (3, '广州布料批发中心', '王老板', '13700137003', '广州市天河区天河路123号', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `suppliers` VALUES (4, '杭州纺织有限公司', '陈经理', '13600136004', '杭州市西湖区文三路456号', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `suppliers` VALUES (5, '深圳装饰材料公司', '刘总', '13500135005', '深圳市南山区科技园789号', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `suppliers` VALUES (6, 'asdfasdf1111', 'asdffasdf', '13400000000', 'asdfasdfasdf', '2025-02-25 13:45:17', '2025-02-25 13:56:46');

-- ----------------------------
-- Table structure for user_addresses
-- ----------------------------
DROP TABLE IF EXISTS `user_addresses`;
CREATE TABLE `user_addresses`  (
                                   `id` int NOT NULL AUTO_INCREMENT COMMENT '地址ID',
                                   `user_id` bigint NOT NULL COMMENT '用户ID',
                                   `address` text CHARACTER SET utf8mb4 NOT NULL COMMENT '详细地址',
                                   `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
                                   `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COMMENT = '用户地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_addresses
-- ----------------------------
INSERT INTO `user_addresses` VALUES (3, 2, '深圳市罗湖区东门步行街茂业百货旁边金光华广场2301', 1, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (4, 2, '深圳市宝安区前海路333号中洲控股中心B座1606', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (5, 3, '深圳市龙岗区横岗街道六约社区园山街10号', 1, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (6, 2, '北京市朝阳区xx路xx号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (7, 2, '北京市朝阳区yy路yy号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (8, 2, '北京市朝阳区zz路zz号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (9, 3, '上海市浦东新区aa路aa号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (10, 3, '上海市浦东新区bb路bb号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (11, 4, '广州市天河区cc路cc号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (12, 4, '广州市天河区dd路dd号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (13, 5, '深圳市南山区ee路ee号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (14, 5, '深圳市南山区ff路ff号', 0, '2025-02-15 18:48:38', '2025-02-15 18:48:38');
INSERT INTO `user_addresses` VALUES (17, 6, 'asdfasd', 0, '2025-02-16 20:58:17', '2025-02-16 21:46:54');
INSERT INTO `user_addresses` VALUES (23, 6, 'asdf', 0, '2025-02-16 21:46:34', '2025-02-16 21:46:34');
INSERT INTO `user_addresses` VALUES (24, 6, 'dddddd', 0, '2025-02-16 21:46:54', '2025-02-25 13:10:48');
INSERT INTO `user_addresses` VALUES (27, 9, 'test', 0, '2025-02-21 09:57:32', '2025-02-21 09:57:32');
INSERT INTO `user_addresses` VALUES (28, 11, 'test1111', 0, '2025-02-22 13:48:09', '2025-02-22 13:48:09');
INSERT INTO `user_addresses` VALUES (30, 11, 'fasdfadsf', 0, '2025-02-25 09:20:10', '2025-02-25 09:26:18');
INSERT INTO `user_addresses` VALUES (31, 11, 'aaaa', 0, '2025-02-25 09:26:06', '2025-02-25 09:26:06');
INSERT INTO `user_addresses` VALUES (32, 11, 'bbbb', 1, '2025-02-25 09:26:18', '2025-02-25 09:26:18');
INSERT INTO `user_addresses` VALUES (33, 6, 'aaaaa', 1, '2025-02-25 13:10:48', '2025-02-25 13:19:41');
INSERT INTO `user_addresses` VALUES (35, 12, '展源阁', 1, '2025-03-12 10:12:32', '2025-03-12 10:12:32');
INSERT INTO `user_addresses` VALUES (36, 12, '回家路途', 0, '2025-03-21 09:29:42', '2025-03-21 09:29:42');
INSERT INTO `user_addresses` VALUES (37, 16, '深圳龙华阳光365家具广场二楼', 0, '2025-03-26 22:05:21', '2025-03-26 22:05:21');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `username` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '用户名',
                          `password` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '密码',
                          `openid` varchar(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '微信openid',
                          `phone` varchar(20) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '手机号',
                          `nickname` varchar(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '昵称',
                          `avatar_url` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '头像URL',
                          `role` varchar(20) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'user' COMMENT '角色：admin/user',
                          `status` varchar(20) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'active' COMMENT '状态：active/inactive',
                          `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `username`(`username`) USING BTREE,
                          UNIQUE INDEX `phone`(`phone`) USING BTREE,
                          INDEX `idx_phone`(`phone`) USING BTREE,
                          INDEX `idx_openid`(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '$2a$10$X/hX5qrs.NPqnz1RBJFgNOPn3Jh.VxgBJL.RPGQl8YBAWg.uvjXqG', NULL, '13800138000', '系统管理员', '8adc1b4f-2f18-467e-9f39-ca859919380b.jpg', 'ADMIN', 'active', '2025-02-13 14:48:28', '2025-03-04 21:53:26');
INSERT INTO `users` VALUES (2, 'zhangsan', '$2a$10$X/hX5qrs.NPqnz1RBJFgNOPn3Jh.VxgBJL.RPGQl8YBAWg.uvjXqG', NULL, '13800138001', '张三', '8adc1b4f-2f18-467e-9f39-ca859919380b.jpg', 'USER', 'active', '2025-02-13 14:48:28', '2025-03-07 20:54:42');
INSERT INTO `users` VALUES (3, 'lisi', '$2a$10$X/hX5qrs.NPqnz1RBJFgNOPn3Jh.VxgBJL.RPGQl8YBAWg.uvjXqG', NULL, '13800138002', '李四', '8adc1b4f-2f18-467e-9f39-ca859919380b.jpg', 'USER', 'active', '2025-02-13 14:48:28', '2025-03-04 21:53:26');
INSERT INTO `users` VALUES (4, 'wangwu', '$2a$10$X/hX5qrs.NPqnz1RBJFgNOPn3Jh.VxgBJL.RPGQl8YBAWg.uvjXqG', NULL, '13800138003', '王五', 'cc42fbd6-c53a-4105-8ba3-868fa6e6cecd.png', 'USER', 'active', '2025-02-13 14:48:28', '2025-03-04 21:54:15');
INSERT INTO `users` VALUES (5, 'zhaoliu', '$2a$10$X/hX5qrs.NPqnz1RBJFgNOPn3Jh.VxgBJL.RPGQl8YBAWg.uvjXqG', NULL, '13800138004', '赵六', 'cc42fbd6-c53a-4105-8ba3-868fa6e6cecd.png', 'USER', 'active', '2025-02-13 14:48:28', '2025-03-12 09:04:01');
INSERT INTO `users` VALUES (6, '13428742678', '$2a$10$eLYK8uDmQX8Podws0UuVKOOwLEg0HQoQp/AeAl03mdoF8/bUeg8bW', 'ociQC7IcECjnNu5TVl0pOOzjLKgs', '13428742678', '微信用户03', '29b66e7d-1b96-4428-b007-b8e1b4ab7676.jpg', 'ADMIN', 'active', '2025-02-13 14:54:43', '2025-03-17 08:42:44');
INSERT INTO `users` VALUES (8, '123', '$2a$10$iS8dRAyNAzjfvoIhZkxBberjS6tXVF25eJ0GcjVoh/ohUdLlHpj8u', NULL, '123', 'test', '92d49738-d371-409a-a13c-b51ea415fb4c.jpg', 'USER', 'active', '2025-02-20 15:45:45', '2025-03-20 12:15:39');
INSERT INTO `users` VALUES (9, '13410000001', '$2a$10$T2LAUwUh7IGGTI0KYdydKOzQHVvmXBqJkkODUunhBLW.ZLF0G9Lw2', NULL, '13410000001', 'test', '92d49738-d371-409a-a13c-b51ea415fb4c.jpg', 'USER', 'active', '2025-02-20 16:32:03', '2025-03-20 12:15:39');
INSERT INTO `users` VALUES (11, '18038193827', '$2a$10$5p6vUwhJaPnpIm0Pq9a1J.EkJa/Bz6/k9gDediat11w8tt15rJ3wi', 'ociQC7B4aceSeSEQQaE-WOht4Xus', '18038193827', 'test1', '92d49738-d371-409a-a13c-b51ea415fb4c.jpg', 'USER', 'active', '2025-02-20 22:08:28', '2025-03-20 12:15:39');
INSERT INTO `users` VALUES (12, '18038131513', '$2a$10$.hhn1aMYaUN1RwsWX2pxl.H75fg2nHIn/17Q0zhA.7QhXuhkw3IZG', 'ociQC7Exd79vvfcQuE0qYjT2VdyQ', '18038131513', 'daisen', NULL, 'USER', 'active', '2025-03-12 10:12:07', '2025-03-20 12:15:39');
INSERT INTO `users` VALUES (13, '13428741000', '$2a$10$BHDhdUUPkAnHWyAlxpwoWemD6kbmoOOResGF/Lz/ANW1QjJAYINTS', NULL, '13428741000', 'asdf', NULL, 'USER', 'active', '2025-03-20 12:04:34', '2025-03-20 12:15:39');
INSERT INTO `users` VALUES (14, '13400000000', '$2a$10$MG6JqY/pfaVVIgcEia5GiOrKUQ2U9qW0GrY983T4sKKs/ylwdxs.6', NULL, '13400000000', 'fasdfad', NULL, 'MASTER', 'active', '2025-03-20 12:07:21', '2025-03-20 12:07:21');
INSERT INTO `users` VALUES (15, '13420000000', '$2a$10$PYRnJB0jsiwERi.L3blMGOaHZ5Z9o7LHpSm8SDYdlyQ2fcqhlIyQq', NULL, '13420000000', 'adsf', NULL, 'USER', 'active', '2025-03-20 12:08:33', '2025-03-20 12:08:33');
INSERT INTO `users` VALUES (16, '18098263805', '$2a$10$9VIR15DgBY4FU3M1aVKL0ub3KKbuRKsnJMLaCogqvO.aNM26LWo8.', NULL, '18098263805', '黎小姐', NULL, 'USER', 'active', '2025-03-26 22:01:41', '2025-03-26 22:01:41');

-- ----------------------------
-- Table structure for materials
-- ----------------------------
DROP TABLE IF EXISTS `materials`;
CREATE TABLE `materials`  (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `code` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '材料编号',
                              `name` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '材料名称',
                              `supplier_id` int NOT NULL COMMENT '供应商ID',
                              `price` decimal(10, 2) NOT NULL COMMENT '单价',
                              `unit` varchar(10) CHARACTER SET utf8mb4 NOT NULL COMMENT '单位（米/个/件）',
                              `stock` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '当前库存',
                              `min_stock` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '最低库存警戒线',
                              `description` text CHARACTER SET utf8mb4 NULL COMMENT '描述',
                              `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE INDEX `uk_code`(`code`) USING BTREE,
                              INDEX `idx_name`(`name`) USING BTREE,
                              INDEX `idx_supplier`(`supplier_id`) USING BTREE,
                              CONSTRAINT `materials_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COMMENT = '材料表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of materials
-- ----------------------------
INSERT INTO `materials` VALUES (1, 'FAB0011', '纯棉印花窗帘布1', 6, 681.00, '米', 5001.00, 1001.00, '100%纯棉材质，多种印花图案可选1', '2025-02-14 11:56:13', '2025-03-07 19:24:01');
INSERT INTO `materials` VALUES (2, 'FAB002', '亚麻纯色窗帘布', 1, 88.00, '米', 300.00, 80.00, '高品质亚麻面料，自然质感', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (3, 'FAB003', '遮光布', 2, 75.00, '米', 400.00, 100.00, '三层遮光面料，遮光率99%', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (4, 'FAB004', '雪尼尔窗帘布', 2, 98.00, '米', 200.00, 50.00, '高档雪尼尔面料，手感柔软', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (5, 'FAB005', '提花窗帘布', 3, 108.00, '米', 250.00, 60.00, '欧式提花工艺，奢华大气', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (6, 'ACC001', '金属窗帘杆', 4, 45.00, '个', 100.00, 20.00, '不锈钢材质，多种尺寸可选', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (7, 'ACC002', '罗马杆配件套装', 4, 128.00, '套', 80.00, 15.00, '包含滑轮、挂钩等配件', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (8, 'ACC003', '窗帘挂钩', 5, 0.50, '个', 2000.00, 500.00, '加厚塑料材质，耐用防锈', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (9, 'ACC004', '窗帘带扣', 5, 15.00, '个', 300.00, 50.00, '欧式风格，多色可选', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (10, 'ACC005', '窗帘轨道', 4, 35.00, '米', 150.00, 30.00, '铝合金材质，静音滑轨', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (11, 'DEC001', '流苏装饰带', 3, 18.00, '米', 200.00, 40.00, '高档流苏，多色可选', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (12, 'DEC002', '珠帘配件', 5, 25.00, '串', 150.00, 30.00, '水晶珠帘，透明材质', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (13, 'DEC003', '绣花边条', 1, 12.00, '米', 300.00, 60.00, '精美刺绣花边', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (14, 'DEC004', '窗帘夹', 5, 22.00, '对', 100.00, 20.00, '欧式复古风格', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (15, 'DEC005', '装饰绳', 3, 8.00, '米', 400.00, 80.00, '编织装饰绳，多色可选', '2025-02-14 11:56:13', '2025-02-14 11:56:13');
INSERT INTO `materials` VALUES (17, 'aaa', 'test', 2, 123.00, '米', 0.00, 50.00, 'test', '2025-03-01 15:41:08', '2025-03-01 15:41:08');
-- ----------------------------
-- Table structure for material_attachments
-- ----------------------------
DROP TABLE IF EXISTS `material_attachments`;
CREATE TABLE `material_attachments`  (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `material_id` int NOT NULL,
                                         `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `thumb_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '缩略图文件名',
                                         `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `created_at` datetime NOT NULL,
                                         `updated_at` datetime NOT NULL,
                                         PRIMARY KEY (`id`) USING BTREE,
                                         INDEX `material_id`(`material_id`) USING BTREE,
                                         CONSTRAINT `material_attachments_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of material_attachments
-- ----------------------------
INSERT INTO `material_attachments` VALUES (20, 1, '38de0e17-1818-43a3-a397-301a88794f8d.mp4', '8adc1b4f-2f18-467e-9f39-ca859919380b.jpg', 'video', '2025-02-26 19:45:04', '2025-02-26 19:45:04');
INSERT INTO `material_attachments` VALUES (21, 1, '61fc5c13-f0d4-4ad4-ab14-6849ddf4697a.jpg', NULL, 'image', '2025-02-26 19:45:04', '2025-02-26 19:45:04');
INSERT INTO `material_attachments` VALUES (22, 1, 'a139dacb-355f-4f7c-bb95-a6bc52f2173e.jpg', NULL, 'image', '2025-02-26 19:45:04', '2025-02-26 19:45:04');
INSERT INTO `material_attachments` VALUES (23, 17, '63af5180-9094-4075-8687-fd3ee2c1ea30.jpg', NULL, 'image', '2025-03-01 15:41:08', '2025-03-01 15:41:08');
INSERT INTO `material_attachments` VALUES (24, 17, '60af4c35-b63e-42a5-a381-1219dcd14b72.jpg', NULL, 'image', '2025-03-01 15:41:08', '2025-03-01 15:41:08');
INSERT INTO `material_attachments` VALUES (25, 17, '2d199398-c94c-4464-b4a9-8c4b3dd84b4e.mp4', '8adc1b4f-2f18-467e-9f39-ca859919380b.jpg', 'video', '2025-03-01 15:41:08', '2025-03-01 15:41:08');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
                           `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                           `user_id` bigint NOT NULL COMMENT '用户ID',
                           `order_number` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '订单编号',
                           `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
                           `status` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '订单状态：pending-待处理，processing-处理中，completed-已完成，cancelled-已取消',
                           `payment_status` varchar(20) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'unpaid' COMMENT '支付状态：unpaid-未支付，paid-已支付',
                           `paid_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '已支付金额',
                           `address_id` int NULL DEFAULT NULL COMMENT '安装地址ID',
                           `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `remark` text CHARACTER SET utf8mb4 NULL COMMENT '备注信息',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE INDEX `order_number`(`order_number`) USING BTREE,
                           INDEX `user_id`(`user_id`) USING BTREE,
                           INDEX `address_id`(`address_id`) USING BTREE,
                           CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (48, 6, 'ORD20250307205040', 2414.00, 'pending', 'unpaid', 0.00, 2, '2025-03-07 20:50:41', '2025-03-07 20:50:41', 'aaaaaaaaaaaaaaaaaaaaaaaa');
INSERT INTO `orders` VALUES (49, 6, 'ORD20250307205329', 825.00, 'pending', 'unpaid', 0.00, 33, '2025-03-07 20:53:30', '2025-03-07 20:53:30', '1111');
INSERT INTO `orders` VALUES (50, 2, 'ORD20250307205442', 13653.00, 'cancelled', 'unpaid', 0.00, 3, '2025-03-07 20:54:42', '2025-03-12 09:59:57', 'asdfadsfadf');
INSERT INTO `orders` VALUES (51, 5, 'ORD20250312090401', 246.00, 'pending', 'unpaid', 0.00, 13, '2025-03-12 09:04:01', '2025-03-12 09:41:18', 'fasdf');
INSERT INTO `orders` VALUES (52, 12, 'ORD20250312101708', 14301.00, 'confirm', 'unpaid', 0.00, 35, '2025-03-12 10:17:09', '2025-03-12 10:17:09', 'fasdf');

-- ----------------------------
-- Table structure for order_room
-- ----------------------------
DROP TABLE IF EXISTS `order_room`;
CREATE TABLE `order_room`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `order_id` int NOT NULL,
                               `room_name` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '房间名称',
                               `window_width` decimal(10, 2) NOT NULL COMMENT '窗户宽度(米)',
                               `window_height` decimal(10, 2) NOT NULL COMMENT '窗户高度(米)',
                               `remarks` varchar(500) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '备注',
                               `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `idx_order_id`(`order_id`) USING BTREE,
                               CONSTRAINT `order_room_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COMMENT = '订单房间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_room
-- ----------------------------
INSERT INTO `order_room` VALUES (56, 48, '1', 1.00, 1.00, '111111', '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `order_room` VALUES (57, 48, '2', 2.00, 2.00, '2222', '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `order_room` VALUES (58, 48, '3', 3.00, 3.00, '33333', '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `order_room` VALUES (59, 49, '12', 1.00, 1.00, '112', '2025-03-07 20:53:30', '2025-03-07 20:53:30');
INSERT INTO `order_room` VALUES (60, 50, 'asdf', 1.00, 1.00, '1', '2025-03-07 20:54:42', '2025-03-07 20:54:42');
INSERT INTO `order_room` VALUES (61, 51, 'ateawet', 123.00, 2.00, '234', '2025-03-12 09:04:01', '2025-03-12 09:04:01');
INSERT INTO `order_room` VALUES (62, 52, 'fasdf', 23.00, 23.00, '123412asdf', '2025-03-12 10:17:09', '2025-03-12 10:17:09');

-- ----------------------------
-- Table structure for order_status_logs
-- ----------------------------
DROP TABLE IF EXISTS `order_status_logs`;
CREATE TABLE `order_status_logs`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `order_id` int NOT NULL COMMENT '订单ID',
                                      `old_status` varchar(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '原状态',
                                      `new_status` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '新状态',
                                      `operator_id` bigint NOT NULL COMMENT '操作人ID',
                                      `remark` text CHARACTER SET utf8mb4 NULL COMMENT '备注',
                                      `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_order`(`order_id`) USING BTREE,
                                      INDEX `idx_operator`(`operator_id`) USING BTREE,
                                      INDEX `idx_created`(`created_at`) USING BTREE,
                                      CONSTRAINT `order_status_logs_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                                      CONSTRAINT `order_status_logs_ibfk_2` FOREIGN KEY (`operator_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COMMENT = '订单状态变更记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_status_logs
-- ----------------------------
INSERT INTO `order_status_logs` VALUES (35, 48, NULL, 'pending', 6, '新订单创建', '2025-03-07 20:50:41');
INSERT INTO `order_status_logs` VALUES (36, 49, NULL, 'pending', 6, '新订单创建', '2025-03-07 20:53:30');
INSERT INTO `order_status_logs` VALUES (37, 50, NULL, 'pending', 2, '新订单创建', '2025-03-07 20:54:42');
INSERT INTO `order_status_logs` VALUES (38, 51, NULL, 'confirm', 5, '新订单创建', '2025-03-12 09:04:01');
INSERT INTO `order_status_logs` VALUES (39, 51, 'confirm', 'pending', 6, '订单状态更新', '2025-03-12 09:41:18');
INSERT INTO `order_status_logs` VALUES (40, 50, 'pending', 'cancelled', 6, '订单状态更新', '2025-03-12 09:59:57');
INSERT INTO `order_status_logs` VALUES (41, 52, NULL, 'confirm', 12, '新订单创建', '2025-03-12 10:17:09');


-- ----------------------------
-- Table structure for payment_config
-- ----------------------------
DROP TABLE IF EXISTS `payment_config`;
CREATE TABLE `payment_config`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `config_type` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '配置类型：WXPAY-微信支付，ALIPAY-支付宝支付',
                                   `config_key` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '配置键',
                                   `config_value` varchar(2000) CHARACTER SET utf8mb4 NOT NULL COMMENT '配置值',
                                   `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-启用，1-禁用',
                                   `remark` varchar(200) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '备注',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE INDEX `uk_type_key`(`config_type`, `config_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COMMENT = '支付配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_config
-- ----------------------------
INSERT INTO `payment_config` VALUES (1, 'WXPAY', 'appId', 'your_app_id', 0, '微信小程序ID', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (2, 'WXPAY', 'mchId', 'your_mch_id', 0, '微信商户号', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (3, 'WXPAY', 'key', 'your_key', 0, '微信支付密钥', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (4, 'WXPAY', 'certPath', '/path/to/cert/apiclient_cert.p12', 0, '证书路径', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (5, 'WXPAY', 'notifyUrl', 'https://your-domain.com/api/payment/notify/wxpay', 0, '支付回调地址', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (6, 'ALIPAY', 'appId', 'your_app_id', 0, '支付宝应用ID', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (7, 'ALIPAY', 'privateKey', 'your_private_key', 0, '支付宝私钥', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (8, 'ALIPAY', 'publicKey', 'your_public_key', 0, '支付宝公钥', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (9, 'ALIPAY', 'notifyUrl', 'https://your-domain.com/api/payment/notify/alipay', 0, '支付回调地址', '2025-02-14 22:13:40', NULL);
INSERT INTO `payment_config` VALUES (10, 'ALIPAY', 'sandbox', 'false', 0, '是否沙箱环境', '2025-02-14 22:13:40', NULL);

-- ----------------------------
-- Table structure for payment_records
-- ----------------------------
DROP TABLE IF EXISTS `payment_records`;
CREATE TABLE `payment_records`  (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `order_id` int NOT NULL COMMENT '订单ID',
                                    `amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
                                    `amount_sign` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '支付金额验证签名',
                                    `payment_method` enum('cash','wechat','alipay','bank_transfer') CHARACTER SET utf8mb4 NOT NULL COMMENT '支付方式',
                                    `type` varchar(10) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '订单支付类型：deposit定金/final尾款',
                                    `transaction_no` varchar(100) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '交易流水号',
                                    `operator_id` bigint NOT NULL COMMENT '操作人ID',
                                    `status` enum('success','failed','pending') CHARACTER SET utf8mb4 NOT NULL DEFAULT 'pending' COMMENT '支付状态',
                                    `remark` text CHARACTER SET utf8mb4 NULL COMMENT '备注',
                                    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `expired_at` timestamp NULL DEFAULT NULL COMMENT '支付过期时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_order`(`order_id`) USING BTREE,
                                    INDEX `idx_transaction`(`transaction_no`) USING BTREE,
                                    INDEX `idx_operator`(`operator_id`) USING BTREE,
                                    INDEX `idx_created`(`created_at`) USING BTREE,
                                    INDEX `idx_payment_records_expired_at`(`expired_at`) USING BTREE,
                                    CONSTRAINT `payment_records_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                    CONSTRAINT `payment_records_ibfk_2` FOREIGN KEY (`operator_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '支付记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_records
-- ----------------------------

-- ----------------------------
-- Table structure for room_attachment
-- ----------------------------
DROP TABLE IF EXISTS `room_attachment`;
CREATE TABLE `room_attachment`  (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `room_id` int NOT NULL,
                                    `file_name` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '文件名',
                                    `thumb_file_name` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '缩略图文件名',
                                    `file_type` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '文件类型(image/video)',
                                    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_room_id`(`room_id`) USING BTREE,
                                    CONSTRAINT `room_attachment_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `order_room` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8mb4 COMMENT = '房间附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_attachment
-- ----------------------------
INSERT INTO `room_attachment` VALUES (76, 56, 'ee0cd336-2ba2-4f50-b581-cf4e6b356268.jpg', NULL, 'image', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (77, 56, '1a89a1ee-1504-405c-b9de-de0f056e3f41.jpg', NULL, 'image', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (78, 57, 'f6b7d5b0-4aa8-47cb-bb7d-7903fac88da6.mp4', '171f94f1-6ad4-41d8-8e4a-f69ce07de5aa.jpg', 'video', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (79, 58, 'eaac8af2-4e85-4740-99d7-dbb9848fd006.jpg', NULL, 'image', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (80, 58, '2e37353f-860a-4b83-8c43-7d48a0c1ea5a.jpg', NULL, 'image', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (81, 58, '3319a32e-ad1b-4413-abc5-2df3fb037a9b.jpg', NULL, 'image', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (82, 58, '569710e4-f419-4940-8e74-f364400518a7.jpg', NULL, 'image', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (83, 58, 'cfb78dc1-6bd8-483e-84a0-30ec86e983c4.jpg', NULL, 'image', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (84, 58, 'b3c37851-8c98-4137-a214-894094c7786b.mp4', '4b26e1bd-c4a9-46a3-a5d9-40b2e1140ca4.jpg', 'video', '2025-03-07 20:50:41');
INSERT INTO `room_attachment` VALUES (85, 60, '16c90c3a-3694-448b-930b-58e612bdbe09.mp4', 'bb5619be-c5eb-4f0e-abd9-89218d1c1d81.jpg', 'video', '2025-03-07 20:54:42');
INSERT INTO `room_attachment` VALUES (86, 61, 'a6214f17-a88e-4543-93bb-59801397cc4f.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (87, 61, '027a81aa-a18e-4dda-bfb2-7b7db6dd027d.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (88, 61, '1b10baa2-50c6-47e8-ae58-15cea86d2b0d.mp4', 'd92a75ab-faf0-4cfa-913c-94263de62a2a.jpg', 'video', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (89, 61, '09da541a-dcd5-4762-9a5c-e84a8e16cbc9.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (90, 61, '4d425269-5e5e-476f-8edc-5e837f3f4122.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (91, 61, 'c2d55760-828d-496f-991c-7873da2a119a.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (92, 61, '2a49c0c9-5f0c-4713-86d3-9b1c5e0ebe45.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (93, 61, '1145adb9-e30d-4960-b85b-cc95a1fac1c0.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (94, 61, '47f92731-58cf-4b12-9a9e-63e51f2affab.jpg', NULL, 'image', '2025-03-12 09:04:01');
INSERT INTO `room_attachment` VALUES (95, 62, 'c3c2bcdd-ba28-4055-85dc-930e37f4ecd3.jpg', NULL, 'image', '2025-03-12 10:17:09');
INSERT INTO `room_attachment` VALUES (96, 62, '41f6b48e-f039-44b5-a5b5-c1f597e58120.jpg', NULL, 'image', '2025-03-12 10:17:09');

-- ----------------------------
-- Table structure for room_items
-- ----------------------------
DROP TABLE IF EXISTS `room_items`;
CREATE TABLE `room_items`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `room_id` int NOT NULL COMMENT '房间ID',
                               `material_id` int NOT NULL COMMENT '材料ID',
                               `model` varchar(100) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '型号',
                               `quantity` decimal(10, 2) NOT NULL COMMENT '数量',
                               `unit_price` decimal(10, 2) NOT NULL COMMENT '单价',
                               `amount` decimal(10, 2) NOT NULL COMMENT '金额',
                               `remark` text CHARACTER SET utf8mb4 NULL COMMENT '备注',
                               `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `idx_order`(`room_id`) USING BTREE,
                               INDEX `idx_material`(`material_id`) USING BTREE,
                               CONSTRAINT `room_items_ibfk_2` FOREIGN KEY (`material_id`) REFERENCES `materials` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_items
-- ----------------------------
INSERT INTO `room_items` VALUES (84, 56, 17, '1', 1.00, 123.00, 123.00, NULL, '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `room_items` VALUES (85, 57, 17, '2', 2.00, 123.00, 246.00, NULL, '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `room_items` VALUES (86, 57, 2, '2', 2.00, 88.00, 176.00, NULL, '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `room_items` VALUES (87, 58, 3, '3', 3.00, 75.00, 225.00, NULL, '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `room_items` VALUES (88, 58, 4, '33', 3.00, 98.00, 294.00, NULL, '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `room_items` VALUES (89, 58, 6, '333', 30.00, 45.00, 1350.00, NULL, '2025-03-07 20:50:41', '2025-03-07 20:50:41');
INSERT INTO `room_items` VALUES (90, 59, 3, '1', 11.00, 75.00, 825.00, NULL, '2025-03-07 20:53:30', '2025-03-07 20:53:30');
INSERT INTO `room_items` VALUES (91, 60, 17, '1', 111.00, 123.00, 13653.00, NULL, '2025-03-07 20:54:42', '2025-03-07 20:54:42');
INSERT INTO `room_items` VALUES (92, 61, 17, 'adsfa', 2.00, 123.00, 246.00, NULL, '2025-03-12 09:04:01', '2025-03-12 09:04:01');
INSERT INTO `room_items` VALUES (93, 62, 1, 'asd', 21.00, 681.00, 14301.00, NULL, '2025-03-12 10:17:09', '2025-03-12 10:17:09');

-- ----------------------------
-- Table structure for stock_records
-- ----------------------------
DROP TABLE IF EXISTS `stock_records`;
CREATE TABLE `stock_records`  (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `material_id` int NOT NULL COMMENT '材料ID',
                                  `type` enum('in','out') CHARACTER SET utf8mb4 NOT NULL COMMENT '变动类型：入库/出库',
                                  `quantity` decimal(10, 2) NOT NULL COMMENT '变动数量',
                                  `before_stock` decimal(10, 2) NOT NULL COMMENT '变动前库存',
                                  `after_stock` decimal(10, 2) NOT NULL COMMENT '变动后库存',
                                  `operator_id` bigint NOT NULL COMMENT '操作人ID',
                                  `order_id` int NULL DEFAULT NULL COMMENT '关联订单ID（如果变动是由订单引起的）',
                                  `remark` text CHARACTER SET utf8mb4 NULL COMMENT '备注',
                                  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_material`(`material_id`) USING BTREE,
                                  INDEX `idx_operator`(`operator_id`) USING BTREE,
                                  INDEX `idx_order`(`order_id`) USING BTREE,
                                  INDEX `idx_created`(`created_at`) USING BTREE,
                                  CONSTRAINT `stock_records_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                  CONSTRAINT `stock_records_ibfk_2` FOREIGN KEY (`operator_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                  CONSTRAINT `stock_records_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '库存变动记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock_records
-- ----------------------------


-- ----------------------------
-- Triggers structure for table materials
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_material_stock_update`;
delimiter ;;
CREATE TRIGGER `tr_material_stock_update` BEFORE UPDATE ON `materials` FOR EACH ROW BEGIN
    IF NEW.stock != OLD.stock THEN
        SET NEW.updated_at = CURRENT_TIMESTAMP;
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
