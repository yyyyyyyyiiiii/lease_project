package com.gy.lease.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gy.lease.model.entity.RoomInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gy.lease.web.admin.vo.room.RoomDetailVo;
import com.gy.lease.web.admin.vo.room.RoomItemVo;
import com.gy.lease.web.admin.vo.room.RoomQueryVo;
import com.gy.lease.web.admin.vo.room.RoomSubmitVo;

import java.util.List;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface RoomInfoService extends IService<RoomInfo> {

    void saveDetail(RoomSubmitVo roomSubmitVo);

    IPage<RoomItemVo> pageDetail(IPage<RoomItemVo> page, RoomQueryVo queryVo);

    RoomDetailVo selectById(Long id);

    void removeVo(Long id);
}
