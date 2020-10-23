package com.atguigu.demo.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
// 出错后调用实现类中的方法
public class VodFileDegradeFeignClient implements VodClient{
    // 出错之后会执行
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错");
    }

    @Override
    public R deleteBatch(List<String> videoList) {
        return R.error().message("删除多个视频出错");
    }
}
