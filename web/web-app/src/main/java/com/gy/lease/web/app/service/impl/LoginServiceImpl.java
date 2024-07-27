package com.gy.lease.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gy.lease.common.constant.RedisConstant;
import com.gy.lease.common.exception.LeaseException;
import com.gy.lease.common.result.ResultCodeEnum;
import com.gy.lease.common.utils.CodeUtil;
import com.gy.lease.common.utils.JWTutil;
import com.gy.lease.model.entity.UserInfo;
import com.gy.lease.model.enums.BaseStatus;
import com.gy.lease.web.app.mapper.UserInfoMapper;
import com.gy.lease.web.app.service.LoginService;
import com.gy.lease.web.app.service.SmsService;
import com.gy.lease.web.app.service.UserInfoService;
import com.gy.lease.web.app.vo.user.LoginVo;
import com.gy.lease.web.app.vo.user.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SmsService smsService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void getCode(String phone) {
        String randomCode = CodeUtil.getRandomCode(6);


        String key = RedisConstant.APP_LOGIN_PREFIX+phone;
        Boolean b = stringRedisTemplate.hasKey(key);
        if(b){
            Long expire = stringRedisTemplate.getExpire(key);
            if(RedisConstant.APP_LOGIN_CODE_TTL_SEC - expire < 60){
                throw new LeaseException(ResultCodeEnum.APP_SEND_SMS_TOO_OFTEN);
            }
        }
        stringRedisTemplate.opsForValue().set(key,randomCode,RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);

        smsService.Sms(phone,randomCode);
    }

    @Override
    public String login(LoginVo loginVo) {
        //判断手机号
        if(loginVo.getPhone()== null || loginVo.getPhone().isEmpty()){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }
        if(loginVo.getCode() == null || loginVo.getCode().isEmpty()){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }
        String code = stringRedisTemplate.opsForValue().get(RedisConstant.APP_LOGIN_PREFIX+loginVo.getPhone());
        if(!code.equals(loginVo.getCode())){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
        }

        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getPhone,loginVo.getPhone());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);


        if(userInfo == null){
            userInfo = new UserInfo();
            userInfo.setPhone(loginVo.getPhone());
            userInfo.setNickname("用户-"+loginVo.getPhone().substring(7));
            userInfo.setStatus(BaseStatus.ENABLE);
            userInfoMapper.insert(userInfo);
        }
        if(userInfo.getStatus() == BaseStatus.DISABLE){
            throw new LeaseException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
        }

        return JWTutil.createToken(userInfo.getId(), userInfo.getPhone());
    }

    @Override
    public UserInfoVo getByToken(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setNickname(userInfo.getNickname());
        userInfoVo.setAvatarUrl(userInfo.getAvatarUrl());
        return userInfoVo;
    }
}
