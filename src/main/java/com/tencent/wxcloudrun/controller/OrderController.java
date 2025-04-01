package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.common.utils.DateUtils;
import com.tencent.wxcloudrun.dto.OrderDTO;
import com.tencent.wxcloudrun.dto.OrderListDTO;
import com.tencent.wxcloudrun.dto.OrderQueryDTO;
import com.tencent.wxcloudrun.entity.User;
import com.tencent.wxcloudrun.service.OrderService;
import com.tencent.wxcloudrun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/list")
    public Result<IPage<OrderListDTO>> getOrderList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        OrderQueryDTO query = new OrderQueryDTO();
        query.setPage(current);
        query.setSize(size);
        query.setStatus(status);

        // 转换时间格式
        if (startTime != null && !startTime.isEmpty()) {
            query.setStartTime(DateUtils.parseDateTime(startTime));
        }
        if (endTime != null && !endTime.isEmpty()) {
            query.setEndTime(DateUtils.parseDateTime(endTime));
        }

        return Result.success(orderService.queryOrderList(query));
    }

    @GetMapping("/{id}")
    public Result<OrderDTO> getOrderDetail(@PathVariable int id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @PostMapping
    public Result<OrderDTO> createOrder(@Validated @RequestBody OrderDTO orderDTO) {
        if (orderDTO.getUserId() == null || orderDTO.getUserId() == 0) {
            return Result.error("系统异常：用户ID为空");
        }

        // 1. 先根据用户ID获取用户属性
        User searchUser = userService.getUserById(orderDTO.getUserId());
        if (searchUser == null) {
            return Result.error("系统异常：用户不存在");
        }
        // 2. 如果入参的电话和查出来的用户电话一致，则不对电话查重；否则需要根据电话查重
        if (!orderDTO.getPhone().equals(searchUser.getPhone())) {
            if (userService.getUserByPhone(orderDTO.getPhone()) != null) {
                return Result.error("手机号已存在，请核对手机号");
            }
        }
        // 3. 更新用户信息
        User user = new User();
        user.setId(Long.parseLong(orderDTO.getUserId().toString()));
        user.setPhone(orderDTO.getPhone());
        user.setNickname(orderDTO.getNickname());
        userService.updateUser(user);

        return orderService.createOrder(orderDTO);
    }

    @PutMapping("/{id}")
    public Result<OrderDTO> updateOrder(@PathVariable int id, @RequestBody @Validated OrderDTO orderDTO) {
        if (orderDTO.getUserId() == null || orderDTO.getUserId() == 0) {
            return Result.error("系统异常：用户ID为空");
        }

        // 1. 先根据用户ID获取用户属性
        User searchUser = userService.getUserById(orderDTO.getUserId());
        if (searchUser == null) {
            return Result.error("系统异常：用户不存在");
        }
        orderDTO.setId(id);
        return Result.success(orderService.updateOrder(orderDTO));
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateOrderStatus(
            @PathVariable int id,
            @RequestParam String status
    ) {
        orderService.updateOrderStatus(id, status);
        return Result.success(true);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return Result.success();
    }

    @GetMapping("/getUserOrders/{userId}")
    public Result<IPage<OrderListDTO>> getOrderListByUserId(@PathVariable Long userId) {
        OrderQueryDTO query = new OrderQueryDTO();
        query.setPage(1);
        query.setSize(100);
        query.setUserId(userId);
        return Result.success(orderService.queryOrderList(query));
    }
}
