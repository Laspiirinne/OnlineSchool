package com.atguigu.demo.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.demo.eduservice.entity.EduChapter;
import com.atguigu.demo.eduservice.entity.chapter.ChapterVo;
import com.atguigu.demo.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduservice/chapter")
//@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    // 课程大纲列表,根据课程id进行查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable("courseId") String courseId){
        List<ChapterVo> list=chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo",list);
    }

    // 添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    // 根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable("chapterId")String chapterId){
        EduChapter eduChapter=chapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }

    // 修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    // 删除方法
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable("chapterId")String chapterId){
        boolean flag=chapterService.deleteChapter(chapterId);
        if(flag)
            return R.ok();
        else
            return R.error();
    }
}
