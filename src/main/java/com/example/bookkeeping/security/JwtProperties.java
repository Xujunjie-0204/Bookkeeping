package com.example.bookkeeping.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "bookkeeping.jwt")
public class JwtProperties {

    /**
     * JWT 签名密钥，生产环境必须通过环境变量覆盖。
     */
    private String secret;

    /**
     * Token 有效期，单位秒。
     */
    private long expiresIn;
}
