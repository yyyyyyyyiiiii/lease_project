package com.gy.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gy.lease.common.exception.LeaseException;
import com.gy.lease.common.result.ResultCodeEnum;
import com.gy.lease.model.entity.*;
import com.gy.lease.model.enums.ItemType;
import com.gy.lease.web.admin.mapper.*;
import com.gy.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gy.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.gy.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.gy.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.gy.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.gy.lease.web.admin.vo.fee.FeeValueVo;
import com.gy.lease.web.admin.vo.graph.GraphVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private ApartmentLabelService apartmentLabelService;

    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    @Autowired
    private ApartmentFacilityService apartmentFacilityService;
    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        boolean isUpdate = apartmentSubmitVo.getId() != null;
        super.saveOrUpdate(apartmentSubmitVo);//传入的是子类，将父类的东西更新或者保存，多的东西自动舍弃了

        if(isUpdate){
            //是修改操作,把多余的值全删除，再添加

            //删除图片
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getId,apartmentSubmitVo.getId());
            graphInfoService.remove(graphInfoLambdaQueryWrapper);

            //删除标签
            LambdaQueryWrapper<ApartmentLabel> apartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getApartmentId,apartmentSubmitVo.getId());
            apartmentLabelService.remove(apartmentLabelLambdaQueryWrapper);


            //删除杂费
            LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFeeValueLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId,apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(apartmentFeeValueLambdaQueryWrapper);

            //删除配套
            LambdaQueryWrapper<ApartmentFacility> apartmentFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFacilityLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId,apartmentSubmitVo.getId());
            apartmentFacilityService.remove(apartmentFacilityLambdaQueryWrapper);

        }


        //插入图片
        List<GraphInfo> graphInfos = new ArrayList<>();
        if(apartmentSubmitVo.getGraphVoList()!=null) {
            apartmentSubmitVo.getGraphVoList().forEach(graphVo -> {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setUrl(graphVo.getUrl());
                graphInfo.setName(graphVo.getName());
                graphInfo.setItemId(apartmentSubmitVo.getId());

                graphInfos.add(graphInfo);
            });
        }
        graphInfoService.saveBatch(graphInfos);


        //插入标签
        List<ApartmentLabel> apartmentLabels = new ArrayList<>();
        if(apartmentSubmitVo.getLabelIds()!=null) {
            apartmentSubmitVo.getLabelIds().forEach(LabelId -> {
                ApartmentLabel apartmentLabel = new ApartmentLabel();

                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(LabelId);

                apartmentLabels.add(apartmentLabel);
            });
        }
        apartmentLabelService.saveBatch(apartmentLabels);


        //插入杂费
        List<ApartmentFeeValue> apartmentFeeValues = new ArrayList<>();
        if(apartmentSubmitVo.getFeeValueIds()!=null) {
            apartmentSubmitVo.getFeeValueIds().forEach(feeId -> {
                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();

                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeId);

                apartmentFeeValues.add(apartmentFeeValue);

            });
        }
        apartmentFeeValueService.saveBatch(apartmentFeeValues);






        //插入配套
        List<ApartmentFacility> apartmentFacilities = new ArrayList<>();
        if(apartmentSubmitVo.getFacilityInfoIds()!=null) {
            apartmentSubmitVo.getFacilityInfoIds().forEach(facilityId -> {
                ApartmentFacility apartmentFacility = new ApartmentFacility();

                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(facilityId);

                apartmentFacilities.add(apartmentFacility);
            });
        }
        apartmentFacilityService.saveBatch(apartmentFacilities);

    }

    @Override
    public IPage<ApartmentItemVo> pageItem(IPage<ApartmentItemVo> page, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.page(page,queryVo);
    }

    @Override
    public ApartmentDetailVo selectDetailById(Long id) {
        //1查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        //查询图片信息
        List<GraphVo> graphVoList =graphInfoMapper.selectGraphVotList(ItemType.APARTMENT,id);

        //查询标签列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectLabelInfo(id);

        //查询配套列表
        List<FacilityInfo> facilityInfos = facilityInfoMapper.selectFacilityInfo(id);

        //查询杂费列表
        List<FeeValueVo> feeValueVos = feeValueMapper.selectFeeValueVo(id);

        //组装结果
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);

        apartmentDetailVo.setFacilityInfoList(facilityInfos);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVos);
        return apartmentDetailVo;
    }

    @Override
    public void removeApartment(Long id) {

        LambdaQueryWrapper<RoomInfo> roomInfoLambdaQueryWrapper = new LambdaQueryWrapper<RoomInfo>();
        roomInfoLambdaQueryWrapper.eq(RoomInfo::getApartmentId,id);
        Long count = roomInfoMapper.selectCount(roomInfoLambdaQueryWrapper);
        if(count>0){
            //终止删除，并返回错误信息
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }


        super.removeById(id);
        //删除图片
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getId,id);
        graphInfoService.remove(graphInfoLambdaQueryWrapper);

        //删除标签
        LambdaQueryWrapper<ApartmentLabel> apartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getApartmentId,id);
        apartmentLabelService.remove(apartmentLabelLambdaQueryWrapper);


        //删除杂费
        LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFeeValueLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId,id);
        apartmentFeeValueService.remove(apartmentFeeValueLambdaQueryWrapper);

        //删除配套
        LambdaQueryWrapper<ApartmentFacility> apartmentFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFacilityLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId,id);
        apartmentFacilityService.remove(apartmentFacilityLambdaQueryWrapper);
    }
}




