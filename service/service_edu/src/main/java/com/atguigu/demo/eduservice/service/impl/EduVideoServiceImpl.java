package com.atguigu.demo.eduservice.service.impl;

import com.atguigu.demo.eduservice.client.VodClient;
import com.atguigu.demo.eduservice.entity.EduVideo;
import com.atguigu.demo.eduservice.mapper.EduVideoMapper;
import com.atguigu.demo.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    // 注入vodClient
    @Autowired
    private VodClient vodClient;

    // 根据课程id删小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        // 1 根据视频ID查出所有视频ID
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

        // 把List<EduVideo>变成List<String>
        List<String> videoIds=new ArrayList<>();
        for(int i=0;i<eduVideoList.size();++i){
            EduVideo eduVideo=eduVideoList.get(i);
            String videoSourceId=eduVideo.getVideoSourceId();///
            if(!StringUtils.isEmpty(videoSourceId))
                videoIds.add(videoSourceId);
        }
        // 根据多个视频id删除多个视频
        if(videoIds.size()>0)
            vodClient.deleteBatch(videoIds);

        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
