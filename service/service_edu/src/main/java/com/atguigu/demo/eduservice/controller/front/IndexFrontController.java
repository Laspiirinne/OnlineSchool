package com.atguigu.demo.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.demo.eduservice.entity.EduCourse;
import com.atguigu.demo.eduservice.entity.EduTeacher;
import com.atguigu.demo.eduservice.service.EduCourseService;
import com.atguigu.demo.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    // 查询前8条热门课程，前4条名师
    @GetMapping("index")
    public R index(){

        // 查询前8条热门课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduCourseList = courseService.list(wrapper);

        // 前4条名师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> eduTeacherList = teacherService.list(wrapperTeacher);

        return R.ok().data("eduList",eduCourseList).data("teacherList",eduTeacherList);
    }
}
