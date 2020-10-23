package com.atguigu.staservice.service;

import com.atguigu.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-10-19
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    // 统计某一天的注册人数,生成统计数据
    void registerCount(String day);

    Map<String, Object> getShowData(String type, String begin, String end);
}
