package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.Vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.sl.draw.geom.Guide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Li Jiale
 * @since 2020-10-06
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 登录的方法
    @Override
    public String login(UcenterMember member) {
        // 获取登陆手机号 和 密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        // 手机号 和 密码 非空判断
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password))
            throw new GuliException(20001,"登陆失败");

        // 判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);

        // 判断查询对象是否为空
        if(mobileMember==null)
            throw new GuliException(20001,"手机号不存在");

        // 判断密码(库中密码是加密后的)
        // 加密方式MD5，只能加密不能解密
        if(!MD5.encrypt(password).equals(mobileMember.getPassword()))
            throw new GuliException(20001,"密码错误");

        // 判断用户是否禁用
        if(mobileMember.getIsDeleted())
            throw new GuliException(20001,"你被禁了！");

        // 登陆成功，按规则生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    // 注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        // 获取注册的数据
        String code = registerVo.getCode(); // 验证码
        String mobile = registerVo.getMobile(); // 手机号
        String nickname = registerVo.getNickname(); // 昵称
        String password = registerVo.getPassword(); // 密码

        // 非空判断
        if(StringUtils.isEmpty(code)||StringUtils.isEmpty(mobile)||
                StringUtils.isEmpty(nickname)||StringUtils.isEmpty(password))
            throw new GuliException(20001,"没填全！");

        // 判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode))
            throw new GuliException(20001,"验证码错误");

        // 判断手机号是否重复，重复的不添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        int count = baseMapper.selectCount(wrapper);
        if(count>0)
            throw new GuliException(20001,"注册过了");

        // 数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDeleted(false);
        member.setAvatar("https://fkedu.oss-cn-beijing.aliyuncs.com/2020/10/05ea8e581000fd4ea7b20c82da384a384afile.png");
        baseMapper.insert(member);
    }

    // 根据openid进行查询
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    // 查询某一天注册人数
    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
