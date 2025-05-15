package com.neo.springcloud.gateway.service;

import com.neo.springcloud.gateway.util.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截rest接口请求，做用户合法性认证.
 *
 * @author neo
 * @since 2025-02-28
 */
public class JWTInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(JWTInterceptor.class);

    /**
     * JWT加密密钥
     */
    private String secret;

    public JWTInterceptor(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 提起请求头中的jwt信息
        String jwtToken = request.getHeader("jwt_token");

        try {
            // 在此处对JWT的合法性校验
            JWTUtil.instance().verifyJWT(jwtToken, secret);
            return true;
        } catch (Exception e){
            LOGGER.error("Verify token fail.", e);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("Token is valid");
            return false;
        }

        /**
         * 后面还可以补充对用户的其它权限校验。可以解析出用户的姓名，账号等非敏感信息放到header中，下游接口可以直接获取使用
         */
    }
}
