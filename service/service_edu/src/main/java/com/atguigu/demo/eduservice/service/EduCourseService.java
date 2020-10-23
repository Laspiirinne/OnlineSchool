package com.atguigu.demo.eduservice.service;

import com.atguigu.demo.eduservice.entity.EduCourse;
import com.atguigu.demo.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.demo.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.demo.eduservice.entity.vo.frontvo.CourseFrontVo;
import com.atguigu.demo.eduservice.entity.vo.frontvo.CourseWebVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    // 根据课程ID查询课程的基本信息
    CourseInfoVo getCourseInfo(String courseId);

    // 修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    // 删除课程
    void removeCourse(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
