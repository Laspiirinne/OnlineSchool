package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Li Jiale
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 1 生成订单的方法
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
        // 创建订单，返回订单号
        String orderNo = orderService.createOrders(courseId,JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId",orderNo);
    }

    // 2 根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order one = orderService.getOne(wrapper);
        return R.ok().data("item",one);
    }

    // 3 根据课程id和用户id查询订单表中订单的状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1); // 1代表已支付
        int count = orderService.count(wrapper);
        if(count>0)
            return true;
        return false;
    }
}
