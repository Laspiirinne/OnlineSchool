package com.atguigu.demo.eduservice.service;

import com.atguigu.demo.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-16
 */
public interface EduVideoService extends IService<EduVideo> {

    // 根据课程id删小节
    void removeVideoByCourseId(String courseId);
}
