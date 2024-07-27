package com.gy.lease.web.admin.service.impl;

import com.gy.lease.model.entity.LeaseTerm;
import com.gy.lease.web.admin.mapper.LeaseTermMapper;
import com.gy.lease.web.admin.service.LeaseTermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author liubo
* @description 针对表【lease_term(租期)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class LeaseTermServiceImpl extends ServiceImpl<LeaseTermMapper, LeaseTerm>
    implements LeaseTermService{

}




