package com.gy.lease.web.app.service.impl;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.gy.lease.common.constant.RedisConstant;
import com.gy.lease.common.exception.LeaseException;
import com.gy.lease.common.result.ResultCodeEnum;
import com.gy.lease.common.utils.CodeUtil;
import com.gy.lease.web.app.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {


    @Autowired
    private Client client;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void Sms(String phone, String code) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phone);
        sendSmsRequest.setSignName("阿里云短信测试");
        sendSmsRequest.setTemplateCode("SMS_154950909");
        sendSmsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");

        try {
            client.sendSms(sendSmsRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
