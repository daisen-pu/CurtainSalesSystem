package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.common.utils.SecurityUtils;
import com.tencent.wxcloudrun.constant.OrderConstants;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.entity.*;
import com.tencent.wxcloudrun.mapper.*;
import com.tencent.wxcloudrun.service.OrderService;
import com.tencent.wxcloudrun.service.OrderStatusLogService;
import com.tencent.wxcloudrun.service.UserService;
import com.tencent.wxcloudrun.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRoomMapper orderRoomMapper;
    private final RoomItemMapper roomItemMapper;
    private final OrderStatusLogService orderStatusLogService;
    private final UserService userService;
    private final MaterialMapper materialMapper;
    private final UserAddressMapper userAddressMapper;
    private final RoomAttachmentMapper roomAttachmentMapper;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public IPage<OrderListDTO> queryOrderList(OrderQueryDTO query) {
        try {
            IPage<Order> page = new Page<>(query.getPage(), query.getSize());
            
            // 如果不是管理员，只能查看自己的订单
            Optional.ofNullable(SecurityUtils.getCurrentUser())
                    .filter(user -> !SecurityUtils.isAdmin())
                    .ifPresent(user -> query.setUserId(user.getId()));
            
            page = orderMapper.queryOrders(page, query);
            return page.convert(this::convertToListDTO);
        } catch (Exception e) {
            logger.error("Error querying order list", e);
            throw new BusinessException("查询订单列表失败");
        }
    }

    @Override
    public OrderDTO getOrderDetail(int id) {
        try {
            Order order = Optional.ofNullable(orderMapper.selectById(id))
                                  .orElseThrow(() -> new BusinessException("订单不存在"));

            // 如果不是管理员，只能查看自己的订单
            Optional.ofNullable(SecurityUtils.getCurrentUser())
                    .filter(user -> !SecurityUtils.isAdmin() && !order.getUserId().equals(user.getId()))
                    .ifPresent(user -> { throw new BusinessException("无权查看此订单"); });

            // 查询订单的房间信息
            List<OrderRoom> rooms = orderRoomMapper.selectList(
                new LambdaQueryWrapper<OrderRoom>().eq(OrderRoom::getOrderId, id)
            );

            // 查询每个房间的附件信息和商品信息
            rooms.forEach(room -> {
                room.setAttachments(roomAttachmentMapper.selectList(
                    new LambdaQueryWrapper<RoomAttachment>().eq(RoomAttachment::getRoomId, room.getId())
                ));
                room.setItems(roomItemMapper.selectList(
                    new LambdaQueryWrapper<RoomItem>().eq(RoomItem::getRoomId, room.getId())
                ));
            });
            order.setRooms(rooms);
            
            return convertToDTO(order);
        } catch (Exception e) {
            logger.error("Error getting order detail with id: {}", id, e);
            throw new BusinessException("获取订单详情失败");
        }
    }

    @Override
    @Transactional
    public Result<OrderDTO> createOrder(OrderDTO orderDTO) {
        try {
            // 验证用户地址
            Optional.ofNullable(userAddressMapper.selectById(orderDTO.getAddressId()))
                    .orElseThrow(() -> new BusinessException("安装地址不存在"));

            if (orderDTO.getRooms() == null || orderDTO.getRooms().isEmpty()){
                return Result.error("房间信息不能为空");
            }
            
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderDTO.OrderRoomDTO roomDTO : orderDTO.getRooms()){
                for (OrderDTO.RoomItemDTO itemDTO : roomDTO.getItems()) {
                    // 验证材料是否存在
                    Material material = materialMapper.selectById(itemDTO.getMaterialId());
                    if (material == null) {
                        return Result.error("材料不存在");
                    }

                    // 验证数量
                    if (itemDTO.getQuantity() == null || itemDTO.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                        return Result.error("商品数量必须大于0");
                    }

                    // 设置单价和金额
                    itemDTO.setUnitPrice(material.getPrice());
                    itemDTO.setAmount(material.getPrice().multiply(itemDTO.getQuantity()));

                    // 累加总金额
                    totalAmount = totalAmount.add(itemDTO.getAmount());
                }
            }

            // 创建订单
            Order order = new Order();
            order.setUserId(orderDTO.getUserId());
            order.setOrderNumber(generateOrderNumber());
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderConstants.Status.CONFIRM);
            order.setPaymentStatus(OrderConstants.PaymentStatus.UNPAID);
            order.setPaidAmount(BigDecimal.ZERO);
            order.setAddressId(orderDTO.getAddressId());
            order.setRemark(orderDTO.getRemark());
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            
            orderMapper.insert(order);

            // 保存订单房间信息
            saveRooms(orderDTO, order);

            // 记录状态变更
            orderStatusLogService.logOrderStatusChange(order.getId(),null,
                    OrderConstants.Status.CONFIRM,orderDTO.getUserId(),"新订单创建");
            
            return Result.success(getOrderDetail(order.getId()));
        } catch (Exception e) {
            logger.error("Error creating order", e);
            return Result.error("创建订单失败");
        }
    }

    private void saveRooms(OrderDTO orderDTO, Order order) {
        try {
            Optional.ofNullable(orderDTO.getRooms())
                    .filter(rooms -> !rooms.isEmpty())
                    .ifPresent(rooms -> rooms.forEach(roomDTO -> {
                        OrderRoom room = new OrderRoom();
                        room.setOrderId(order.getId());
                        room.setRoomName(roomDTO.getRoomName());
                        room.setWindowWidth(roomDTO.getWindowWidth());
                        room.setWindowHeight(roomDTO.getWindowHeight());
                        room.setRemarks(roomDTO.getRemarks());
                        room.setCreatedAt(LocalDateTime.now());
                        room.setUpdatedAt(LocalDateTime.now());
                        orderRoomMapper.insert(room);
                        Optional.ofNullable(roomDTO.getAttachments())
                                .filter(attachments -> !attachments.isEmpty())
                                .ifPresent(attachments -> attachments.forEach(attachmentDTO -> {
                                    RoomAttachment attachment = new RoomAttachment();
                                    attachment.setRoomId(room.getId());
                                    attachment.setFileName(FileUtil.subFilename(attachmentDTO.getFileName()));
                                    attachment.setThumbFileName(FileUtil.subFilename(attachmentDTO.getThumbFileName()));
                                    attachment.setFileType(attachmentDTO.getFileType());
                                    attachment.setCreatedAt(LocalDateTime.now());
                                    roomAttachmentMapper.insert(attachment);
                                }));
                        Optional.ofNullable(roomDTO.getItems())
                                .filter(items -> !items.isEmpty())
                                .ifPresent(items -> items.forEach(itemDTO -> {
                                    roomItemMapper.insert(initRoomItem(room, itemDTO));
                                }));
                    }));
        } catch (Exception e) {
            logger.error("Error saving rooms for order with id: {}", order.getId(), e);
            throw new BusinessException("保存房间信息失败");
        }
    }

    private RoomItem initRoomItem(OrderRoom room, OrderDTO.RoomItemDTO itemDTO) {
        try {
            RoomItem item = new RoomItem();
            item.setRoomId(room.getId());
            item.setMaterialId(itemDTO.getMaterialId());
            item.setModel(itemDTO.getModel());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setAmount(itemDTO.getAmount());
            item.setRemark(itemDTO.getRemark());
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            return item;
        } catch (Exception e) {
            logger.error("Error initializing room item for room with id: {}", room.getId(), e);
            throw new BusinessException("初始化房间商品失败");
        }
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        try {
            // 1. 验证订单是否存在
            Order order = Optional.ofNullable(orderMapper.selectById(orderDTO.getId()))
                                  .orElseThrow(() -> new BusinessException("订单不存在"));

            // 2. 更新订单基本信息
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setRemark(orderDTO.getRemark());
            order.setAddressId(orderDTO.getAddressId());
            order.setUpdatedAt(LocalDateTime.now());
            orderMapper.updateById(order);

            // 4. 更新房间信息
            // 4.1 删除原有房间信息
            List<OrderRoom> oldRooms = orderRoomMapper.selectList(
                new LambdaQueryWrapper<OrderRoom>()
                    .eq(OrderRoom::getOrderId, order.getId())
            );
            oldRooms.forEach(room -> {
                // 删除房间附件
                roomAttachmentMapper.delete(
                    new LambdaQueryWrapper<RoomAttachment>()
                        .eq(RoomAttachment::getRoomId, room.getId())
                );
                // 删除房间商品
                roomItemMapper.delete(
                    new LambdaQueryWrapper<RoomItem>()
                        .eq(RoomItem::getRoomId, room.getId())
                );
            });
            orderRoomMapper.delete(
                new LambdaQueryWrapper<OrderRoom>()
                    .eq(OrderRoom::getOrderId, order.getId())
            );

            // 4.2 添加新房间信息
            saveRooms(orderDTO, order);

            // 5. 返回更新后的订单详情
            return getOrderDetail(order.getId());
        } catch (Exception e) {
            logger.error("Error updating order with id: {}", orderDTO.getId(), e);
            throw new BusinessException("更新订单失败");
        }
    }

    @Override
    @Transactional
    public void updateOrderStatus(int id, String status) {
        try {
            Order order = Optional.ofNullable(orderMapper.selectById(id))
                                  .orElseThrow(() -> new BusinessException("订单不存在"));
            
            // 如果不是管理员，只能更新自己的订单
            Optional.ofNullable(SecurityUtils.getCurrentUser())
                    .filter(user -> !SecurityUtils.isAdmin() && !order.getUserId().equals(user.getId()))
                    .ifPresent(user -> { throw new BusinessException("无权更新此订单"); });
            
            String oldStatus = order.getStatus();
            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            
            orderMapper.updateById(order);
            
            // 记录状态变更
            orderStatusLogService.logOrderStatusChange(
                id,
                oldStatus,
                status,
                SecurityUtils.getCurrentUser().getId(),
                "订单状态更新"
            );
        } catch (Exception e) {
            logger.error("Error updating order status for order with id: {}", id, e);
            throw new BusinessException("更新订单状态失败");
        }
    }

    @Override
    @Transactional
    public void deleteOrder(int id) {
        try {
            Order order = Optional.ofNullable(orderMapper.selectById(id))
                                  .orElseThrow(() -> new BusinessException("订单不存在"));
            
            // 如果不是管理员，只能删除自己的订单
            Optional.ofNullable(SecurityUtils.getCurrentUser())
                    .filter(user -> !SecurityUtils.isAdmin() && !order.getUserId().equals(user.getId()))
                    .ifPresent(user -> { throw new BusinessException("无权删除此订单"); });

            List<OrderRoom> oldRooms = orderRoomMapper.selectList(
                    new LambdaQueryWrapper<OrderRoom>()
                            .eq(OrderRoom::getOrderId, order.getId())
            );
            oldRooms.forEach(room -> {
                // 删除房间附件
                roomAttachmentMapper.delete(
                        new LambdaQueryWrapper<RoomAttachment>()
                                .eq(RoomAttachment::getRoomId, room.getId())
                );
                // 删除房间商品
                roomItemMapper.delete(
                        new LambdaQueryWrapper<RoomItem>()
                                .eq(RoomItem::getRoomId, room.getId())
                );
            });
            // 删除房间信息
            orderRoomMapper.delete(
                    new LambdaQueryWrapper<OrderRoom>()
                            .eq(OrderRoom::getOrderId, order.getId())
            );
            
            // 删除订单
            orderMapper.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting order with id: {}", id, e);
            throw new BusinessException("删除订单失败");
        }
    }

    @Override
    @Transactional
    public void updatePaymentStatus(int id, String paymentStatus, BigDecimal paidAmount) {
        try {
            Order order = Optional.ofNullable(orderMapper.selectById(id))
                                  .orElseThrow(() -> new BusinessException("订单不存在"));
            
            // 如果不是管理员，只能更新自己的订单
            Optional.ofNullable(SecurityUtils.getCurrentUser())
                    .filter(user -> !SecurityUtils.isAdmin() && !order.getUserId().equals(user.getId()))
                    .ifPresent(user -> { throw new BusinessException("无权更新此订单"); });
            
            // 验证支付金额
            if (paidAmount != null) {
                if (paidAmount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new BusinessException("支付金额不能为负数");
                }
                if (paidAmount.compareTo(order.getTotalAmount()) > 0) {
                    throw new BusinessException("支付金额不能超过订单总金额");
                }
            }
            
            String oldPaymentStatus = order.getPaymentStatus();
            order.setPaymentStatus(paymentStatus);
            if (paidAmount != null) {
                order.setPaidAmount(paidAmount);
            }
            order.setUpdatedAt(LocalDateTime.now());
            
            orderMapper.updateById(order);
            
            // 记录状态变更
            orderStatusLogService.logOrderStatusAndPaymentChange(
                id,
                order.getStatus(),
                order.getStatus(),
                oldPaymentStatus,
                paymentStatus,
                SecurityUtils.getCurrentUser().getId(),
                "订单支付状态更新"
            );
        } catch (Exception e) {
            logger.error("Error updating payment status for order with id: {}", id, e);
            throw new BusinessException("更新支付状态失败");
        }
    }

    private OrderDTO convertToDTO(Order order) {
        try {
            if (order == null) {
                return null;
            }
            
            OrderDTO dto = new OrderDTO();
            BeanUtils.copyProperties(order, dto);

            // 获取用户信息
            Optional.ofNullable(order.getUserId())
                    .map(userService::getUserById)
                    .ifPresent(dto::setUserInfo);
            
            // 获取地址信息
            Optional.ofNullable(order.getAddressId())
                    .map(userAddressMapper::selectById)
                    .ifPresent(address -> {
                        UserAddressDTO addressDTO = new UserAddressDTO();
                        BeanUtils.copyProperties(address, addressDTO);
                        dto.setAddress(addressDTO);
                    });
            
            // 转换房间信息
            Optional.ofNullable(order.getRooms())
                    .filter(rooms -> !rooms.isEmpty())
                    .ifPresent(rooms -> dto.setRooms(rooms.stream().map(room -> {
                        OrderDTO.OrderRoomDTO roomDTO = new OrderDTO.OrderRoomDTO();
                        BeanUtils.copyProperties(room, roomDTO);
                        
                        // 转换附件信息
                        Optional.ofNullable(room.getAttachments())
                                .filter(attachments -> !attachments.isEmpty())
                                .ifPresent(attachments -> roomDTO.setAttachments(attachments.stream().map(attachment -> {
                                    OrderDTO.RoomAttachmentDTO attachmentDTO = new OrderDTO.RoomAttachmentDTO();
                                    BeanUtils.copyProperties(attachment, attachmentDTO);
                                    return attachmentDTO;
                                }).collect(Collectors.toList())));

                        // 转换商品信息
                        Optional.ofNullable(room.getItems())
                                .filter(items -> !items.isEmpty())
                                .ifPresent(items -> roomDTO.setItems(items.stream().map(item -> {
                                    OrderDTO.RoomItemDTO itemDto = new OrderDTO.RoomItemDTO();
                                    BeanUtils.copyProperties(item, itemDto);

                                    // 获取材料信息
                                    Optional.ofNullable(materialMapper.selectById(item.getMaterialId()))
                                            .ifPresent(material -> {
                                                OrderDTO.MaterialDTO materialDTO = new OrderDTO.MaterialDTO();
                                                BeanUtils.copyProperties(material, materialDTO);
                                                itemDto.setMaterial(materialDTO);
                                            });

                                    return itemDto;
                                }).collect(Collectors.toList())));
                        
                        return roomDTO;
                    }).collect(Collectors.toList())));

            return dto;
        } catch (Exception e) {
            logger.error("Error converting order to DTO", e);
            throw new BusinessException("转换订单信息失败");
        }
    }

    private OrderListDTO convertToListDTO(Order order) {
        try {
            if (order == null) {
                return null;
            }
            
            OrderListDTO dto = new OrderListDTO();
            BeanUtils.copyProperties(order, dto);

            Optional.ofNullable(order.getAddressId())
                    .map(userAddressMapper::selectById)
                    .ifPresent(address -> dto.setAddress(address.getAddress()));

            Optional.ofNullable(order.getUserId())
                    .map(userService::getUserById)
                    .map(User::getNickname)
                    .ifPresent(dto::setNickName);
            
            return dto;
        } catch (Exception e) {
            logger.error("Error converting order to list DTO", e);
            throw new BusinessException("转换订单列表信息失败");
        }
    }

    private String generateOrderNumber() {
        try {
            return "ORD" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        } catch (Exception e) {
            logger.error("Error generating order number", e);
            throw new BusinessException("生成订单号失败");
        }
    }
}
