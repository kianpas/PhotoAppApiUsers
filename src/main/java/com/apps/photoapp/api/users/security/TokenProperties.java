package com.apps.photoapp.api.users.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@ConfigurationProperties("token") // "token" 접두사를 가진 프로퍼티를 매핑
@RefreshScope
public class TokenProperties {

    private final long expirationTime; // 2. private final 필드로 선언
    private final String secret;
}
