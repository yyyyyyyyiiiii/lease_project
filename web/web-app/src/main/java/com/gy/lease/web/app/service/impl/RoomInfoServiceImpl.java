package com.gy.lease.web.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.lease.common.constant.RedisConstant;
import com.gy.lease.common.login.LoginUserHolder;
import com.gy.lease.model.entity.*;
import com.gy.lease.model.enums.ItemType;
import com.gy.lease.web.app.mapper.*;
import com.gy.lease.web.app.service.ApartmentInfoService;
import com.gy.lease.web.app.service.BrowsingHistoryService;
import com.gy.lease.web.app.service.RoomInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gy.lease.web.app.vo.apartment.ApartmentItemVo;
import com.gy.lease.web.app.vo.attr.AttrValueVo;
import com.gy.lease.web.app.vo.fee.FeeValueVo;
import com.gy.lease.web.app.vo.graph.GraphVo;
import com.gy.lease.web.app.vo.room.RoomDetailVo;
import com.gy.lease.web.app.vo.room.RoomItemVo;
import com.gy.lease.web.app.vo.room.RoomQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
@Slf4j
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private AttrValueMapper attrValueMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private ApartmentInfoService apartmentInfoService;

    @Autowired
    private BrowsingHistoryService browsingHistoryService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public IPage<RoomItemVo> pageRoom(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo) {
        return roomInfoMapper.pageRoom(roomItemVoPage,queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        String key = RedisConstant.APP_ROOM_PREFIX+id;
        RoomDetailVo roomDetailVo = (RoomDetailVo) redisTemplate.opsForValue().get(key);
        if(roomDetailVo == null){
            RoomInfo roomInfo = roomInfoMapper.selectById(id);
            if (roomInfo == null) {
                return null;
            }
            List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);
            List<AttrValueVo> attrValueVoList = attrValueMapper.selectVoList(id);
            List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectByRoomId(id);
            List<LabelInfo> labelInfos = labelInfoMapper.selectByRoomId(id);
            List<PaymentType> paymentTypes = paymentTypeMapper.selectByRoomId(id);
            List<FeeValueVo> feeValueVos = feeValueMapper.selecetVoByRoomId(roomInfo.getApartmentId());
            List<LeaseTerm> leaseTermList = leaseTermMapper.selectByRoomId(id);

            ApartmentItemVo apartmentItemVo = apartmentInfoService.selectVoByRoomId(roomInfo.getApartmentId());

            roomDetailVo = new RoomDetailVo();
            BeanUtils.copyProperties(roomInfo, roomDetailVo);

            roomDetailVo.setApartmentItemVo(apartmentItemVo);
            roomDetailVo.setGraphVoList(graphVoList);
            roomDetailVo.setAttrValueVoList(attrValueVoList);
            roomDetailVo.setFacilityInfoList(facilityInfoList);
            roomDetailVo.setLabelInfoList(labelInfos);
            roomDetailVo.setPaymentTypeList(paymentTypes);
            roomDetailVo.setFeeValueVoList(feeValueVos);
            roomDetailVo.setLeaseTermList(leaseTermList);

            redisTemplate.opsForValue().set(key,roomDetailVo);
        }



        //保存浏览历史
        browsingHistoryService.saveHistroy(LoginUserHolder.getLoginUser().getUserId(),id);

        return roomDetailVo;


    }

    @Override
    public IPage<RoomItemVo> pageByApartmentId(Page<RoomItemVo> page, Long id) {
        return roomInfoMapper.pageByApartmentId(page,id);
    }
}




