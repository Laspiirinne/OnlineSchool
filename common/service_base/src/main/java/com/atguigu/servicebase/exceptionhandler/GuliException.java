package com.atguigu.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class GuliException extends RuntimeException{
    // 异常一般要有状态码和描述信息
    private Integer code; // 状态码
    private String msg; // 异常信息

    public GuliException() {
    }

    public GuliException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
