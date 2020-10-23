package com.atguigu.demo.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.demo.eduservice.entity.EduCourse;
import com.atguigu.demo.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.demo.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.demo.eduservice.entity.vo.CourseQuery;
import com.atguigu.demo.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    // 课程列表（条件查询+分页）
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) CourseQuery courseQuery){
        /**
         * @RequestBody使用json传递数据，把json数据封装到courseQuery中
         * 但是需要使用post方式提交
         * required = false,表示这个可以没有
         */

        // 创建page对象
        Page<EduCourse> pageCourse=new Page<>(current,limit);

        // 构建条件
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();

        // 多条件组合查询
        // 法一：mybatis动态sql
        // 此处使用法二：判断条件值是否为空，如果不为空拼接条件
        String title=courseQuery.getTitle();
        String status=courseQuery.getStatus();
        if(!StringUtils.isEmpty(title)){
            // 构建条件:模糊查询
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }

        // 排序：按创建日期降序
        wrapper.orderByDesc("gmt_create");

        // 调用方法实现条件查询分页;底层封装，把分页的所有数据封装到pageTeacher对象中
        courseService.page(pageCourse,wrapper);
        long total=pageCourse.getTotal();
        List<EduCourse> records=pageCourse.getRecords(); // 数据list
        return R.ok().data("total",total).data("rows",records);
    }

    // 查所有课程信息
    @GetMapping
    public R getCourseList(){
        List<EduCourse> list=courseService.list(null);
        return R.ok().data("list",list);
    }

    // 添加课程基本信息
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        // 返回添加课程的id，为了后面添加大纲使用

        //System.out.println(courseInfoVo);

        String id=courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    // 根据课程ID查询课程的基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId") String courseId){
        CourseInfoVo courseInfoVo=courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    // 修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }


    // 根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable("id") String id){
        CoursePublishVo coursePublishVo=courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    // 课程最终发布（修改课程状态）
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable("id") String id){
        EduCourse eduCourse=new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    // 删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable("courseId") String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }
}

