package com.gy.lease.web.admin.custom.interceptor;

import com.gy.lease.common.login.LoginUser;
import com.gy.lease.common.login.LoginUserHolder;
import com.gy.lease.common.utils.JWTutil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("access-token");
        Claims parse = JWTutil.parse(token);
        Long userId = parse.get("userId", Long.class);
        String username = parse.get("username", String.class);

        LoginUserHolder.setLoginUser(new LoginUser(userId,username));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}
