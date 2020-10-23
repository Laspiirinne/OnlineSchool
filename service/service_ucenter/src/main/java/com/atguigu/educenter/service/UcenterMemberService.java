package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.Vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-10-06
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    // 登录的方法
    String login(UcenterMember member);

    // 注册的方法
    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countRegisterDay(String day);
}
