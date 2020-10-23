package com.atguigu.demo.eduservice.service.impl;

import com.atguigu.demo.eduservice.entity.EduCourse;
import com.atguigu.demo.eduservice.entity.EduCourseDescription;
import com.atguigu.demo.eduservice.entity.EduTeacher;
import com.atguigu.demo.eduservice.entity.EduVideo;
import com.atguigu.demo.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.demo.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.demo.eduservice.entity.vo.frontvo.CourseFrontVo;
import com.atguigu.demo.eduservice.entity.vo.frontvo.CourseWebVo;
import com.atguigu.demo.eduservice.mapper.EduCourseMapper;
import com.atguigu.demo.eduservice.service.EduChapterService;
import com.atguigu.demo.eduservice.service.EduCourseDescriptionService;
import com.atguigu.demo.eduservice.service.EduCourseService;
import com.atguigu.demo.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    // 注入课程描述
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节的service
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService eduChapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        // 向课程表添加课程基本信息
        // CourseInfoVo对象转换为eduCourse对象
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);

        // 返回影响行数（添加行数）
        int insert=baseMapper.insert(eduCourse);
        if(insert<=0){
            // 添加失败
            throw new GuliException(20001,"添加课程信息失败");
        }

        // 获取添加之后课程id
        String cid=eduCourse.getId();

        // 向课程简介表添加课程简介;edu_course_description
        EduCourseDescription courseDescription=new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());

        // 设置描述id就是课程id
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);

        return cid;
    }

    // 根据课程ID查询课程的基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        // 1 查课程表
        EduCourse eduCourse=baseMapper.selectById(courseId);

        CourseInfoVo courseInfoVo=new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        // 2 查描述表
        EduCourseDescription courseDescription=courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    // 修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        // 1 先修改课程表
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        // 返回影响的行数
        int update=baseMapper.updateById(eduCourse);
        if(update==0)
            throw new GuliException(20001,"修改课程失败");

        // 2 修改描述表
        EduCourseDescription description=new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        // 调用mapper
        CoursePublishVo coursePublishVo=baseMapper.getPublishCourseInfo(id);
        return coursePublishVo;
    }

    // 删除课程
    @Override
    public void removeCourse(String courseId) {
        // 1 根据课程id删小节
        eduVideoService.removeVideoByCourseId(courseId);

        // 2 根据课程id删章节
        eduChapterService.removeChapterByCourseId(courseId);

        // 3 根据课程id删描述
        courseDescriptionService.removeById(courseId);

        // 4 根据课程id删课程本身
        int result=baseMapper.deleteById(courseId);
        if(result==0){ // 失败返回
            throw new GuliException(20001,"删除失败");
        }
    }

    // 条件查询带分页查询课程
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        // 判断条件是否为空，不为空进行条件拼接

        // 判断一级分类是否为空
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId()))
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());

        // 判断二级分类是否为空
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId()))
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());

        // 是否根据销量进行排序
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort()))
            wrapper.orderByDesc("buy_count");

        // 是否根据时间(最新)进行排序
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort()))
            wrapper.orderByDesc("gmt_create");

        // 是否根据价格进行排序
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort()))
            wrapper.orderByDesc("price");

        baseMapper.selectPage(pageCourse,wrapper);

        // 分页数据获取出来，放到map集合
        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        // 是否有上页。下页
        boolean hasNext = pageCourse.hasNext();
        boolean hasPrevious = pageCourse.hasPrevious();

        Map<String,Object> map = new HashedMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    // 根据课程id，编写sql语句查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
