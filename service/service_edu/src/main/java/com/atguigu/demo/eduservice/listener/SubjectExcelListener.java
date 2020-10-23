package com.atguigu.demo.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.demo.eduservice.entity.EduSubject;
import com.atguigu.demo.eduservice.entity.excel.SubjectData;
import com.atguigu.demo.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    // 因为SubjectExcelListener不能交给Spring进行管理，需要在EduSubjectServiceImpl
    // 中手动new，所以不能注入其他对象（比如mapper），所以不能通过service-mapper对数据库
    // 进行操作
    public EduSubjectService subjectService;

    public SubjectExcelListener() {}

    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // 读取excel中内容，按行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData==null){
            throw new GuliException(20001,"文件数据为空");
        }

        // 按行读取，每次读取都是两个值（一级分类、二级分类）
        EduSubject existOneSubject=this.existOneSubject(subjectService,subjectData.getOneSubjectName());
        if(existOneSubject==null){ // 没有相同一级分类，进行添加
            existOneSubject=new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName()); // 一级分类名称
            subjectService.save(existOneSubject);
        }

        String pid=existOneSubject.getId();
        // 添加二级分类，判断二级分类是否重复
        EduSubject existTwoSubject=this.existTwoSubject(subjectService,subjectData.getTwoSubjectName(),pid);
        if(existTwoSubject==null){ // 没有相同二级分类，进行添加
            existTwoSubject=new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName()); // 二级分类名称
            subjectService.save(existTwoSubject);
        }
    }

    // 判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject oneSubject=subjectService.getOne(wrapper);
        return oneSubject;
    }

    // 判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject=subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
}
