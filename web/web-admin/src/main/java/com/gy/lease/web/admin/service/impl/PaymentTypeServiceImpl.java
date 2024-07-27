package com.gy.lease.web.admin.service.impl;

import com.gy.lease.model.entity.PaymentType;
import com.gy.lease.web.admin.mapper.PaymentTypeMapper;
import com.gy.lease.web.admin.service.PaymentTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author liubo
* @description 针对表【payment_type(支付方式表)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class PaymentTypeServiceImpl extends ServiceImpl<PaymentTypeMapper, PaymentType>
    implements PaymentTypeService{

}




