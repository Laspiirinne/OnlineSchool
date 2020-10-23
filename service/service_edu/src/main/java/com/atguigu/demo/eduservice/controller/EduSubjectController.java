package com.atguigu.demo.eduservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.demo.eduservice.entity.EduSubject;
import com.atguigu.demo.eduservice.entity.subject.OneSubject;
import com.atguigu.demo.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {
    @Autowired // controller中要注入service
    private EduSubjectService subjectService;

    // 添加课程分类
    // 获取上传的文件，读取文件内容
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        // 通过上传得到的excel文件

        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }

    // 课程分类列表（树形）
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list=subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}

