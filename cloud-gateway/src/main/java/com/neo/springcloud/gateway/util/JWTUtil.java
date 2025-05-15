package com.neo.springcloud.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT生成、解析、校验工具类
 *
 * @author neo
 * @since 2025-02-27
 */
public class JWTUtil {
    private JWTUtil(){

    }

    public static JWTUtil instance(){
        return JWTUtilBuilder.instance;
    }

    /**
     * 创建Token
     *
     * @param parameterMap 参数表
     * @param secret 密钥
     * @return 新建的Token
     */
    public String createToken(Map<String, String> parameterMap, String secret){
        Calendar calendar = Calendar.getInstance();

        // 默认三天后过期
        calendar.add(Calendar.DATE, 3);

        JWTCreator.Builder builder = JWT.create();

        // 设置header
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");
        builder.withHeader(headerMap);

        // 填充负载
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            builder.withClaim(entry.getKey(), entry.getValue());
        }

        // 创建token
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(secret));

        return token;
    }

    /**
     * 校验Token是否有效
     *
     * @param token 令牌
     * @param secret 密钥
     * @return 校验结果
     */
    public DecodedJWT verifyJWT(String token, String secret){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token);
    }

    /**
     * 解析Token获取负载中的参数
     *
     * @param token 令牌
     * @param secret 密钥
     * @return 参数
     */
    public Map<String, String> decodeToken(String token, String secret){
        DecodedJWT decodedJWT = verifyJWT(token, secret);

        // 提取参数
        Map<String, String> parameterMap = new HashMap<>();
        Map<String, Claim> claimMap = decodedJWT.getClaims();
        claimMap.forEach((k, v)->{
            parameterMap.put(k, v.asString());
        });

        return parameterMap;
    }

    private static final class JWTUtilBuilder {
        private static JWTUtil instance = new JWTUtil();
    }
}
