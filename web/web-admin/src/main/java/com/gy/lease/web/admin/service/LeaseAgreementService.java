package com.gy.lease.web.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.lease.model.entity.LeaseAgreement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gy.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.gy.lease.web.admin.vo.agreement.AgreementVo;

/**
* @author liubo
* @description 针对表【lease_agreement(租约信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface LeaseAgreementService extends IService<LeaseAgreement> {

    Page<AgreementVo> pageVo(Page<AgreementVo> page, AgreementQueryVo queryVo);

    AgreementVo selectAgreemengVo(Long id);
}
