package com.atguigu.demo.eduservice.controller;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController // 类交给Spring管理，并能返回数据
@RequestMapping("/eduservice/user")
//@CrossOrigin // 解决跨域
public class EduLoginController {
    // login
    @PostMapping("login")
    public R login(){

        return R.ok().data("token","admin");
    }

    // info
    @GetMapping("info")
    public R info(){

        return R.ok().data("roles","[adimn]").
                data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
