package com.atguigu.demo.eduservice.mapper;

import com.atguigu.demo.eduservice.entity.EduCourse;
import com.atguigu.demo.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.demo.eduservice.entity.vo.frontvo.CourseWebVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getPublishCourseInfo(String courseId);

    CourseWebVo getBaseCourseInfo(String courseId);
}
