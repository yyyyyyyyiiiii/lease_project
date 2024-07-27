package com.gy.lease.web.app.controller.agreement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gy.lease.common.login.LoginUserHolder;
import com.gy.lease.common.result.Result;
import com.gy.lease.model.entity.LeaseAgreement;
import com.gy.lease.model.enums.LeaseStatus;
import com.gy.lease.web.app.service.LeaseAgreementService;
import com.gy.lease.web.app.vo.agreement.AgreementDetailVo;
import com.gy.lease.web.app.vo.agreement.AgreementItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/agreement")
@Tag(name = "租约信息")
public class LeaseAgreementController {

    @Autowired
    private LeaseAgreementService leaseAgreementService;

    @Operation(summary = "获取个人租约基本信息列表")
    @GetMapping("listItem")
    public Result<List<AgreementItemVo>> listItem() {
        String phone = LoginUserHolder.getLoginUser().getUsername();
        List<AgreementItemVo> agreementItemVos = leaseAgreementService.listByUserName(phone);
        return Result.ok(agreementItemVos);
    }

    @Operation(summary = "根据id获取租约详细信息")
    @GetMapping("getDetailById")
    public Result<AgreementDetailVo> getDetailById(@RequestParam Long id) {
        AgreementDetailVo agreementDetailVo = leaseAgreementService.getDetailById(id);
        return Result.ok(agreementDetailVo);
    }

    @Operation(summary = "根据id更新租约状态", description = "用于确认租约和提前退租")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus leaseStatus) {
        LambdaUpdateWrapper<LeaseAgreement> leaseAgreementLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        leaseAgreementLambdaQueryWrapper.eq(LeaseAgreement::getId,id).set(LeaseAgreement::getStatus,leaseStatus);
        leaseAgreementService.update(leaseAgreementLambdaQueryWrapper);
        return Result.ok();
    }

    @Operation(summary = "保存或更新租约", description = "用于续约")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        leaseAgreementService.saveOrUpdate(leaseAgreement);
        return Result.ok();
    }

}
