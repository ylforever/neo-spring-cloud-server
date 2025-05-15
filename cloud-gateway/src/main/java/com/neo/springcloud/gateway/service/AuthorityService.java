package com.neo.springcloud.gateway.service;

import com.neo.springcloud.gateway.util.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

/**
 * 权限认证服务
 *
 * @author neo
 * @since 2025/5/12
 * @version 1.0
 */
@Component
public class AuthorityService implements WebFilter {
    private static final Logger LOGGER = LogManager.getLogger(AuthorityService.class);

    private static PathPattern loginPattern = PathPatternParser.defaultInstance.parse("/spring-cloud-one/v1/login/**");

    @Value("${gateway.jwt.secret:}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        /**
         * 登录接口不做鉴权
         */
        if (loginPattern.matches(exchange.getRequest().getPath())) {
            return chain.filter(exchange);
        }

        // 提取请求头中的jwt信息
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String jwtToken = headers.getFirst("jwt-token");

        try {
            // 在此处对JWT的合法性校验
            JWTUtil.instance().verifyJWT(jwtToken, jwtSecret);
        } catch (Exception e){
            LOGGER.error("Verify token fail.", e);
            ServerHttpResponse response  = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }

        /**
         * 下面还可以补充对用户的其它权限校验。可以解析出用户的姓名，账号等非敏感信息放到header中，下游接口可以直接获取使用
         */

        return chain.filter(exchange);
    }
}
