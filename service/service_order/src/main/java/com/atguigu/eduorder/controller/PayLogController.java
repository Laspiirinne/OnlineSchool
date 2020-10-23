package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.Cacheable;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author Li Jiale
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    // 生成微信支付二维码接口,参数是订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        // 返回信息：二维码地址和其他需要的信息
        Map map = payLogService.createNative(orderNo);
        System.out.println("!!!!!返回二维码的map集合"+map);
        return R.ok().data(map);
    }

    // 查询订单支付状态,参数是订单号
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("!!!!!查询查询订单状态的map集合"+map);
        if(map==null)
            return R.error().message("支付出错");
        // 如果map不为空，通过map获取订单状态
        if(map.get("trade_state").equals("SUCCESS")){
            // 向支付表中加记录，并更新订单表中订单的状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }
}

