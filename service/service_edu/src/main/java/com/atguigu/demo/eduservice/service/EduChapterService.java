package com.atguigu.demo.eduservice.service;

import com.atguigu.demo.eduservice.entity.EduChapter;
import com.atguigu.demo.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
public interface EduChapterService extends IService<EduChapter> {

    // 课程大纲列表，根据课程id进行查询
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    // 删除章节的方法
    boolean deleteChapter(String chapterId);

    // 根据课程id删章节
    void removeChapterByCourseId(String courseId);
}
