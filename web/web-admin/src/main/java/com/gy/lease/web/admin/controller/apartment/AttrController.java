package com.gy.lease.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gy.lease.common.result.Result;
import com.gy.lease.model.entity.AttrKey;
import com.gy.lease.model.entity.AttrValue;
import com.gy.lease.web.admin.service.AttrKeyService;
import com.gy.lease.web.admin.service.AttrValueService;
import com.gy.lease.web.admin.vo.attr.AttrKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "房间属性管理")
@RestController
@RequestMapping("/admin/attr")
public class AttrController {

    @Autowired
    private AttrKeyService attrKeyService;

    @Autowired
    private AttrValueService attrValueService;


    @Operation(summary = "新增或更新属性名称")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateAttrKey(@RequestBody AttrKey attrKey) {
        attrKeyService.saveOrUpdate(attrKey);
        return Result.ok();
    }

    @Operation(summary = "新增或更新属性值")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateAttrValue(@RequestBody AttrValue attrValue) {
        attrValueService.saveOrUpdate(attrValue);
        return Result.ok();
    }


    @Operation(summary = "查询全部属性名称和属性值列表")
    @GetMapping("list")
    public Result<List<AttrKeyVo>> listAttrInfo() {

        /*List<AttrKeyVo> attrKeyVos = new ArrayList<>();

        List<AttrKey> attrKeys = attrKeyService.list();
        attrKeys.forEach(attrKey -> {
            LambdaQueryWrapper<AttrValue> attrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            attrValueLambdaQueryWrapper.eq(AttrValue::getAttrKeyId,attrKey.getId());

            List<AttrValue> attrValues = attrValueService.list(attrValueLambdaQueryWrapper);
            AttrKeyVo attrKeyVo = new AttrKeyVo();
            attrKeyVo.setAttrValueList(attrValues);
            attrKeyVo.setId(attrKey.getId());
            attrKeyVo.setName(attrKey.getName());
            attrKeyVo.setIsDeleted(attrKey.getIsDeleted());
            attrKeyVo.setUpdateTime(attrKey.getUpdateTime());
            attrKeyVo.setCreateTime(attrKey.getCreateTime());

            attrKeyVos.add(attrKeyVo);
        });*/
        List<AttrKeyVo> attrKeyVos = attrKeyService.attKeyVos();
        return Result.ok(attrKeyVos);
    }

    @Transactional
    @Operation(summary = "根据id删除属性名称")
    @DeleteMapping("key/deleteById")
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        //删除属性名
        attrKeyService.removeById(attrKeyId);

        //删除这个属性下面的所有值
        LambdaQueryWrapper<AttrValue> attrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        attrValueLambdaQueryWrapper.eq(AttrValue::getAttrKeyId,attrKeyId);
        attrValueService.remove(attrValueLambdaQueryWrapper);

        return Result.ok();
    }

    @Operation(summary = "根据id删除属性值")
    @DeleteMapping("value/deleteById")
    public Result removeAttrValueById(@RequestParam Long id) {
        attrValueService.removeById(id);
        return Result.ok();
    }

}
