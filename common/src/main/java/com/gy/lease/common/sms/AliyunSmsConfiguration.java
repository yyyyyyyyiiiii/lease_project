package com.gy.lease.common.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliyunSMSProperties.class)
@ConditionalOnProperty(name = "aliyun.sms.endpoint")
public class AliyunSmsConfiguration {
    @Autowired
    private AliyunSMSProperties aliyunSMSProperties;

    @Bean
    public Client createClient(){

        Config config = new Config();
        config.setAccessKeyId(aliyunSMSProperties.getAccessKeyId());
        config.setAccessKeySecret((aliyunSMSProperties.getAccessKeySecret()));
        config.setEndpoint(aliyunSMSProperties.getEndpoint());

        try {
            return new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
