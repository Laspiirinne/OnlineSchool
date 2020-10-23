package com.atguigu.demo.eduservice.service.impl;

import com.atguigu.demo.eduservice.entity.EduChapter;
import com.atguigu.demo.eduservice.entity.EduVideo;
import com.atguigu.demo.eduservice.entity.chapter.ChapterVo;
import com.atguigu.demo.eduservice.entity.chapter.VideoVo;
import com.atguigu.demo.eduservice.mapper.EduChapterMapper;
import com.atguigu.demo.eduservice.service.EduChapterService;
import com.atguigu.demo.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    // 注入小结的service，否则没法用Chapter的service查小结
    private EduVideoService videoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        // 1 根据课程id查出课程中所有的章节
        QueryWrapper<EduChapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterList=baseMapper.selectList(wrapper);

        // 2 根据课程id查出课程中所有的小结
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduVideo> eduVideoList=videoService.list(wrapperVideo);

        // 创建list集合，用于最终封装数据
        List<ChapterVo> finalList=new ArrayList<>();

        // 3 遍历查询出的章节list集合进行封装
        // 遍历查询出的章节的list集合
        for (int i = 0; i < eduChapterList.size(); i++) {
            // 取出每个章节
            EduChapter eduChapter=eduChapterList.get(i);
            // eduChapter对象中的值复制到ChapterVo中
            ChapterVo chapterVo=new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            // 创建集合用于封装章节的小结
            List<VideoVo> videoList=new ArrayList<>();
            // 4 遍历查询出的小结list集合，进行封装
            for (int m= 0; m < eduVideoList.size(); m++) {
                // 得到每个小节
                EduVideo eduVideo=eduVideoList.get(m);
                // 判断：小结的chapterid和章节的id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    // 进行封装
                    VideoVo videoVo=new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    // 放到小结的集合中
                    videoList.add(videoVo);
                }
            }

            // 把封装后的小结list放到章节对象中
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }

    // 删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        // 根据chapterid查询小结表，如果能查到数据就不进行删除
        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count=videoService.count(wrapper);
        // 判断
        // 能查出小结
        if(count>0)
            throw new GuliException(20001,"不能删!");
        else{ // 没查到小结，可以删
            int res=baseMapper.deleteById(chapterId);
            return res>0;
        }

    }

    // 根据课程id删章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
