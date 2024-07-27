package com.gy.lease.web.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gy.lease.model.entity.RoomInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gy.lease.web.admin.vo.room.RoomItemVo;
import com.gy.lease.web.admin.vo.room.RoomQueryVo;
import com.gy.lease.web.admin.vo.room.RoomSubmitVo;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.gy.lease.model.RoomInfo
*/
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {


    IPage<RoomItemVo> page(IPage<RoomItemVo> page, RoomQueryVo queryVo);
}




