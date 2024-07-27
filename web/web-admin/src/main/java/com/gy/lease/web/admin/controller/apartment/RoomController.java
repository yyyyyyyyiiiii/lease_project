package com.gy.lease.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.lease.common.result.Result;
import com.gy.lease.model.entity.RoomInfo;
import com.gy.lease.model.enums.ReleaseStatus;
import com.gy.lease.web.admin.service.RoomInfoService;
import com.gy.lease.web.admin.vo.room.RoomDetailVo;
import com.gy.lease.web.admin.vo.room.RoomItemVo;
import com.gy.lease.web.admin.vo.room.RoomQueryVo;
import com.gy.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间信息管理")
@RestController
@RequestMapping("/admin/room")
public class RoomController {

    @Autowired
    private RoomInfoService roomInfoService;

    @Operation(summary = "保存或更新房间信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody RoomSubmitVo roomSubmitVo) {
        roomInfoService.saveDetail(roomSubmitVo);//完成
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询房间列表")
    @GetMapping("pageItem")
    public Result<IPage<RoomItemVo>> pageItem(@RequestParam long current, @RequestParam long size, RoomQueryVo queryVo) {
        IPage<RoomItemVo> page = new Page<>(current,size);
        IPage<RoomItemVo> list = roomInfoService.pageDetail(page,queryVo);
        return Result.ok(list);//已完成
    }

    @Operation(summary = "根据id获取房间详细信息")
    @GetMapping("getDetailById")
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        RoomDetailVo roomDetailVo = roomInfoService.selectById(id);
        return Result.ok(roomDetailVo);//完成
    }

    @Operation(summary = "根据id删除房间信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        roomInfoService.removeVo(id);
        return Result.ok();//完成
    }

    @Operation(summary = "根据id修改房间发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(Long id, ReleaseStatus status) {
        LambdaUpdateWrapper<RoomInfo> roomInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        roomInfoLambdaUpdateWrapper.eq(RoomInfo::getId,id);
        roomInfoLambdaUpdateWrapper.set(RoomInfo::getIsRelease,status);
        roomInfoService.update(roomInfoLambdaUpdateWrapper);
        return Result.ok();//完成
    }

    @GetMapping("listBasicByApartmentId")
    @Operation(summary = "根据公寓id查询房间列表")
    public Result<List<RoomInfo>> listBasicByApartmentId(Long id) {
        LambdaQueryWrapper<RoomInfo> roomInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomInfoLambdaQueryWrapper.eq(RoomInfo::getApartmentId,id).eq(RoomInfo::getIsRelease,ReleaseStatus.RELEASED);
        List<RoomInfo> list = roomInfoService.list(roomInfoLambdaQueryWrapper);
        return Result.ok(list);//完成

    }

}


















