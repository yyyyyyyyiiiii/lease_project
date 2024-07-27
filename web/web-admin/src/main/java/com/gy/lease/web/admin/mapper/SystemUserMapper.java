package com.gy.lease.web.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.lease.model.entity.SystemUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gy.lease.web.admin.vo.system.user.SystemUserInfoVo;
import com.gy.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.gy.lease.web.admin.vo.system.user.SystemUserQueryVo;

/**
* @author liubo
* @description 针对表【system_user(员工信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.gy.lease.model.SystemUser
*/
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    Page<SystemUserItemVo> pageVo(Page<SystemUserItemVo> page, SystemUserQueryVo queryVo);

    SystemUser selectOnebyUserName(String username);

    SystemUserInfoVo getInfoByToken(Long userId);
}




