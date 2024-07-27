package com.gy.lease.web.app.service;

import com.gy.lease.web.app.vo.user.LoginVo;
import com.gy.lease.web.app.vo.user.UserInfoVo;

public interface LoginService {
    void getCode(String phone);

    String login(LoginVo loginVo);

    UserInfoVo getByToken(Long userId);
}
