package com.gy.lease.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gy.lease.model.entity.*;
import com.gy.lease.model.enums.ItemType;
import com.gy.lease.web.app.mapper.GraphInfoMapper;
import com.gy.lease.web.app.mapper.LeaseAgreementMapper;
import com.gy.lease.web.app.mapper.LeaseTermMapper;
import com.gy.lease.web.app.mapper.PaymentTypeMapper;
import com.gy.lease.web.app.service.ApartmentInfoService;
import com.gy.lease.web.app.service.GraphInfoService;
import com.gy.lease.web.app.service.LeaseAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gy.lease.web.app.service.RoomInfoService;
import com.gy.lease.web.app.vo.agreement.AgreementDetailVo;
import com.gy.lease.web.app.vo.agreement.AgreementItemVo;
import com.gy.lease.web.app.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Autowired
    private LeaseAgreementMapper mapper;

    @Autowired
    private ApartmentInfoService apartmentInfoService;
    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;
    @Override
    public List<AgreementItemVo> listByUserName(String phone) {
        return mapper.listByName(phone);
    }

    @Override
    public AgreementDetailVo getDetailById(Long id) {
        LeaseAgreement leaseAgreement = mapper.selectById(id);
        if(leaseAgreement == null){
            return null;
        }
        ApartmentInfo apartmentInfo = apartmentInfoService.getById(leaseAgreement.getApartmentId());
        RoomInfo roomInfo = roomInfoService.getById(leaseAgreement.getRoomId());

        List<GraphVo> apartmentGraph = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, leaseAgreement.getApartmentId());
        List<GraphVo> roomGraphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, leaseAgreement.getRoomId());

        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());

        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());

        AgreementDetailVo agreementDetailVo = new AgreementDetailVo();

        BeanUtils.copyProperties(leaseAgreement,agreementDetailVo);
        agreementDetailVo.setApartmentName(apartmentInfo.getName());
        agreementDetailVo.setRoomNumber(roomInfo.getRoomNumber());
        agreementDetailVo.setApartmentGraphVoList(apartmentGraph);
        agreementDetailVo.setRoomGraphVoList(roomGraphVoList);
        agreementDetailVo.setPaymentTypeName(paymentType.getName());
        agreementDetailVo.setLeaseTermMonthCount(leaseTerm.getMonthCount());
        agreementDetailVo.setLeaseTermUnit(leaseTerm.getUnit());

        return agreementDetailVo;
    }
}




