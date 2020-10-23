package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // 标识全局异常类
@Slf4j // 表示要用到logback日志
public class GlobalExceptionHandler {

    // 指定出现什么异常执行这个方法,所有异常都执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody // 为了能够返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    // 特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody // 为了能够返回数据
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理");
    }

    // 自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody // 为了能够返回数据
    public R error(GuliException e){
        // 把异常信息写到日志文件中
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
