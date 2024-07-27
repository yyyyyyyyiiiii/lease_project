package com.gy.lease.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gy.lease.common.result.Result;
import com.gy.lease.model.entity.FacilityInfo;
import com.gy.lease.model.entity.LabelInfo;
import com.gy.lease.model.enums.ItemType;
import com.gy.lease.web.admin.service.FacilityInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "配套管理")
@RestController
@RequestMapping("/admin/facility")
public class FacilityController {
    @Autowired
    private FacilityInfoService facilityInfoServicel;

    @Operation(summary = "[根据类型]查询配套信息列表")
    @GetMapping("list")
    public Result<List<FacilityInfo>> listFacility(@RequestParam(required = false) ItemType type) {

        LambdaQueryWrapper<FacilityInfo> facilityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        facilityInfoLambdaQueryWrapper.eq(type!=null,FacilityInfo::getType,type);

        QueryWrapper<FacilityInfo> facilityInfoQueryWrapper = new QueryWrapper<FacilityInfo>();
        facilityInfoQueryWrapper.eq(type!=null,"type",type);



        List<FacilityInfo> list = facilityInfoServicel.list(facilityInfoQueryWrapper);
        return Result.ok(list);
    }

    @Operation(summary = "新增或修改配套信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody FacilityInfo facilityInfo) {
        facilityInfoServicel.saveOrUpdate(facilityInfo);
        return Result.ok();
    }

    @Operation(summary = "根据id删除配套信息")
    @DeleteMapping("deleteById")
    public Result removeFacilityById(@RequestParam Long id) {
        facilityInfoServicel.removeById(id);
        return Result.ok();
    }

}
