package com.atguigu.demo.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.demo.eduservice.entity.EduSubject;
import com.atguigu.demo.eduservice.entity.excel.SubjectData;
import com.atguigu.demo.eduservice.entity.subject.OneSubject;
import com.atguigu.demo.eduservice.entity.subject.TwoSubject;
import com.atguigu.demo.eduservice.listener.SubjectExcelListener;
import com.atguigu.demo.eduservice.mapper.EduSubjectMapper;
import com.atguigu.demo.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try{
            // 文件输入流
            InputStream in=file.getInputStream();
            // 调用方法进行读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // 课程分列表（树形）
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 1 查出所有的1级分类:parent_id=0
        QueryWrapper<EduSubject> wrapperOne=new QueryWrapper<>();
        wrapperOne.eq("parent_id",0);

        // 等价于：this.list(wrapperOne)
        List<EduSubject> oneSubjectList=baseMapper.selectList(wrapperOne);

        // 2 查出所有的2级分类:parent_id!=0
        QueryWrapper<EduSubject> wrapperTwo=new QueryWrapper<>();
        wrapperOne.ne("parent_id",0); // ne表示不等于
        List<EduSubject> twoSubjectList=baseMapper.selectList(wrapperTwo);

        // 创建list集合，用于存储最终封装数据
        List<OneSubject> finalSubjectList=new ArrayList<>();

        // 3 封装一级分类
        // 遍历查出的所有的一级分类list集合，获取每个一级分类对象值，
        // 封装到要求的list集合中：List<OneSubject> finalSubjectList
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject=oneSubjectList.get(i);
            OneSubject oneSubject=new OneSubject();

            // oneSubject.setId(eduSubject.getId());
            // oneSubject.setTitle(eduSubject.getTitle());
            // 等价于下面这个（对应属性名进行赋值）
            BeanUtils.copyProperties(eduSubject,oneSubject);
            finalSubjectList.add(oneSubject);

            // 在一级分类中循环遍历查询所有的二级分类
            // 创建list集合封装每个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList=new ArrayList<>();
            for (int m = 0; m < twoSubjectList.size(); m++) {
                // 获取每个二级分类
                EduSubject twoSubject=twoSubjectList.get(m);
                // 判断二级分类的parent_id是否和一级分类的id相同
                if(twoSubject.getParentId().equals(oneSubject.getId())){
                    TwoSubject twoSubject1=new TwoSubject();
                    BeanUtils.copyProperties(twoSubject,twoSubject1);
                    twoFinalSubjectList.add(twoSubject1);
                }
            }
            oneSubject.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
