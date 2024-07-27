package com.gy.lease.web.admin.service;

import com.gy.lease.common.login.LoginUserHolder;
import com.gy.lease.web.admin.vo.login.CaptchaVo;
import com.gy.lease.web.admin.vo.login.LoginVo;
import com.gy.lease.web.admin.vo.system.user.SystemUserInfoVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);

    SystemUserInfoVo getInfoByToken(Long userId);
}
