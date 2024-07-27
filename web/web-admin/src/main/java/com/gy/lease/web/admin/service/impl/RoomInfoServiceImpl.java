package com.gy.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gy.lease.common.constant.RedisConstant;
import com.gy.lease.model.entity.*;
import com.gy.lease.model.enums.ItemType;
import com.gy.lease.web.admin.mapper.*;
import com.gy.lease.web.admin.service.RoomInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gy.lease.web.admin.vo.attr.AttrValueVo;
import com.gy.lease.web.admin.vo.graph.GraphVo;
import com.gy.lease.web.admin.vo.room.RoomDetailVo;
import com.gy.lease.web.admin.vo.room.RoomItemVo;
import com.gy.lease.web.admin.vo.room.RoomQueryVo;
import com.gy.lease.web.admin.vo.room.RoomSubmitVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private RoomLabelMapper roomLabelMapper;

    @Autowired
    private RoomFacilityMapper roomFacilityMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;
    @Autowired
    private RoomAttrValueMapper roomAttrValueMapper;
    @Autowired
    private LabelInfoMapper labelInfoMapper;
    @Autowired
    private RoomPaymentTypeMapper roomPaymentTypeMapper;
    @Autowired
    private RoomLeaseTermMapper roomLeaseTermMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public void saveDetail(RoomSubmitVo roomSubmitVo) {

        boolean isUpdate = roomSubmitVo.getId() !=null;
        super.saveOrUpdate(roomSubmitVo);

        if(isUpdate){
            //是更新

            //删除原来的图片
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemId,roomSubmitVo.getId());
            graphInfoMapper.delete(graphInfoLambdaQueryWrapper);

            //删除原来的标签
            LambdaQueryWrapper<RoomLabel> roomLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLabelLambdaQueryWrapper.eq(RoomLabel::getRoomId,roomSubmitVo.getId());
            roomLabelMapper.delete(roomLabelLambdaQueryWrapper);

            //删除原来的设施facility
            LambdaQueryWrapper<RoomFacility> roomFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomFacilityLambdaQueryWrapper.eq(RoomFacility::getRoomId,roomSubmitVo.getId());
            roomFacilityMapper.delete(roomFacilityLambdaQueryWrapper);

            //删除原来的属性
            LambdaQueryWrapper<RoomAttrValue> attrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            attrValueLambdaQueryWrapper.eq(RoomAttrValue::getRoomId,roomSubmitVo.getId());
            roomAttrValueMapper.delete(attrValueLambdaQueryWrapper);

            //删除原来的支付方式
            LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomPaymentTypeLambdaQueryWrapper.eq(RoomPaymentType::getRoomId,roomSubmitVo.getId());
            roomPaymentTypeMapper.delete(roomPaymentTypeLambdaQueryWrapper);

            //删除原来的租期
            LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLeaseTermLambdaQueryWrapper.eq(RoomLeaseTerm::getRoomId,roomSubmitVo.getId());
            roomLeaseTermMapper.delete(roomLeaseTermLambdaQueryWrapper);

            //删除缓存
            redisTemplate.delete(RedisConstant.APP_ROOM_PREFIX+roomSubmitVo.getId());
        }

        //再重新插入

        //插入图片
        if(roomSubmitVo.getGraphVoList()!=null){
            roomSubmitVo.getGraphVoList().forEach(graphVo -> {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemId(roomSubmitVo.getId());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfo.setName(graphVo.getName());
                graphInfo.setItemType(ItemType.ROOM);

                graphInfoMapper.insert(graphInfo);
            });
        }

        //插入标签
        if(roomSubmitVo.getLabelInfoIds()!=null){
            roomSubmitVo.getLabelInfoIds().forEach(labelId ->{
                RoomLabel roomLabel = new RoomLabel();
                roomLabel.setLabelId(labelId);
                roomLabel.setRoomId(roomSubmitVo.getId());
                roomLabelMapper.insert(roomLabel);
            });
        }

        //插入配套
        if(roomSubmitVo.getFacilityInfoIds()!=null){
            roomSubmitVo.getFacilityInfoIds().forEach(facilityId ->{
                RoomFacility roomFacility = new RoomFacility();
                roomFacility.setFacilityId(facilityId);
                roomFacility.setRoomId(roomSubmitVo.getId());
                roomFacilityMapper.insert(roomFacility);
            });
        }

        //插入属性
        if(roomSubmitVo.getAttrValueIds()!=null){
            roomSubmitVo.getAttrValueIds().forEach(attrValueId -> {
                RoomAttrValue roomAttrValue = new RoomAttrValue();
                roomAttrValue.setAttrValueId(attrValueId);
                roomAttrValue.setRoomId(roomSubmitVo.getId());
                roomAttrValueMapper.insert(roomAttrValue);
            });
        }

        //插入支付方式
        if(roomSubmitVo.getPaymentTypeIds()!=null){
            roomSubmitVo.getPaymentTypeIds().forEach(payMentTypeid ->{
                RoomPaymentType roomPaymentType = new RoomPaymentType();
                roomPaymentType.setPaymentTypeId(payMentTypeid);
                roomPaymentType.setRoomId(roomSubmitVo.getId());
                roomPaymentTypeMapper.insert(roomPaymentType);
            });
        }


        //插入租期
        if(roomSubmitVo.getLeaseTermIds()!=null){
           roomSubmitVo.getLeaseTermIds().forEach(leaseTermId -> {
               RoomLeaseTerm roomLeaseTerm = new RoomLeaseTerm();
               roomLeaseTerm.setLeaseTermId(leaseTermId);
               roomLeaseTerm.setRoomId(roomSubmitVo.getId());
               roomLeaseTermMapper.insert(roomLeaseTerm);
           });
        }

    }

    @Override
    public IPage<RoomItemVo> pageDetail(IPage<RoomItemVo> page, RoomQueryVo queryVo) {


        return roomInfoMapper.page(page,queryVo);
    }

    @Override
    public RoomDetailVo selectById(Long id) {
        //查询房间的信息
        RoomInfo roomInfo = roomInfoMapper.selectById(id);
        //查询图片
        List<GraphVo> graphVoList = graphInfoMapper.selectGraphVotList(ItemType.ROOM,id);

        //查询配套
        List<FacilityInfo> facilityInfos = facilityInfoMapper.selectFacilityInfo2(id);
        //查询标签
        List<LabelInfo> labelInfos = labelInfoMapper.selectLabelInfo2(id);
        //查询属性
        List<AttrValueVo> attrValueVos = attrValueMapper.selectAttrValueVo(id);
        //查询所属公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(roomInfo.getApartmentId());
        //查询支付方式
        List<PaymentType> paymentTypes = paymentTypeMapper.selectPaymentInfo(id);
        //查询可选周期
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectInfo(id);


        //组装
        RoomDetailVo roomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo,roomDetailVo);
        roomDetailVo.setApartmentInfo(apartmentInfo);
        roomDetailVo.setFacilityInfoList(facilityInfos);
        roomDetailVo.setLabelInfoList(labelInfos);
        roomDetailVo.setGraphVoList(graphVoList);
        roomDetailVo.setAttrValueVoList(attrValueVos);
        roomDetailVo.setLeaseTermList(leaseTermList);
        roomDetailVo.setPaymentTypeList(paymentTypes);

        return roomDetailVo;
    }

    @Override
    public void removeVo(Long id) {
        //删除相应的图片
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType,ItemType.ROOM).eq(GraphInfo::getItemId,id);
        graphInfoMapper.delete(graphInfoLambdaQueryWrapper);

        //删除相应的属性
        LambdaQueryWrapper<RoomAttrValue> roomAttrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomAttrValueLambdaQueryWrapper.eq(RoomAttrValue::getRoomId,id);
        roomAttrValueMapper.delete(roomAttrValueLambdaQueryWrapper);

        //删除相应的标签
        LambdaQueryWrapper<RoomLabel> roomLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLabelLambdaQueryWrapper.eq(RoomLabel::getRoomId,id);
        roomLabelMapper.delete(roomLabelLambdaQueryWrapper);

        //删除相应的支付方式
        LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomPaymentTypeLambdaQueryWrapper.eq(RoomPaymentType::getRoomId,id);
        roomPaymentTypeMapper.delete(roomPaymentTypeLambdaQueryWrapper);

        //删除相应的租期
        LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLeaseTermLambdaQueryWrapper.eq(RoomLeaseTerm::getRoomId,id);
        roomLeaseTermMapper.delete(roomLeaseTermLambdaQueryWrapper);


        //删除相应的配套
        LambdaQueryWrapper<RoomFacility> roomFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomFacilityLambdaQueryWrapper.eq(RoomFacility::getRoomId,id);
        roomFacilityMapper.delete(roomFacilityLambdaQueryWrapper);

        //删除对应的房间信息
        roomInfoMapper.deleteById(id);

        //删除缓存
        redisTemplate.delete(RedisConstant.APP_ROOM_PREFIX + id);
    }
}




