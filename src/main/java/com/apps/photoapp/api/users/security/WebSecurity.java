package com.apps.photoapp.api.users.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final Environment environment;

    /**
     * HTTP 요청에 대한 보안 설정을 정의하는 SecurityFilterChain 빈을 등록합니다.
     * Spring Security 6.x (Spring Boot 3.x) 스타일의 컴포넌트 기반 설정입니다.
     *
     * @param http HttpSecurity 객체. 웹 기반 보안을 구성하는 데 사용됩니다.
     * @return SecurityFilterChain 인스턴스
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // 1. CSRF(Cross-Site Request Forgery) 보호 설정 비활성화
        // REST API 서버와 같이 세션을 상태 없이(stateless) 운영하고, 토큰 기반 인증(예: JWT)을 사용하는 경우
        // CSRF 공격에 상대적으로 안전하며, CSRF 토큰 검증 로직이 불필요할 수 있습니다.
        // 만약 일반적인 웹 애플리케이션(세션 사용)이라면 CSRF 보호를 활성화하는 것이 기본입니다.
        http.csrf((csrf) -> csrf.disable());
        // 2. HTTP 요청에 대한 인가(Authorization) 규칙 설정FD
        http.authorizeHttpRequests((authz) -> authz
                // '/users' 경로 및 하위 경로에 대한 모든 요청을 인증 없이 허용합니다. (예: 회원 가입, 사용자 정보 조회(공개))
                // Spring Security 6부터 requestMatchers는 기본적으로 AntPathMatcher와 유사하게 동작합니다.
                //ip주소 제한
//                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/users/**").access(
                        new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')"))
                // '/h2-console/**' 경로에 대한 모든 요청을 인증 없이 허용합니다. (개발 및 테스트용 H2 데이터베이스 콘솔 접근)
                .requestMatchers("/h2-console/**").permitAll() // 기존: new AntPathRequestMatcher("/h2-console/**")
                // 위에서 명시적으로 허용한 경로 외의 모든 요청은 반드시 인증된 사용자만 접근 가능하도록 설정합니다.
                // 이 설정을 통해 허가되지 않은 접근을 기본적으로 차단하여 보안성을 높입니다.
                .anyRequest().authenticated() // 이 부분을 추가하여 명시적으로 나머지 요청에 대한 인증 요구를 선언
        );

        // 3. 세션 관리(Session Management) 설정
        // 세션 생성 정책을 STATELESS로 설정합니다.
        // 이는 서버가 클라이언트의 세션 상태를 저장하지 않음을 의미하며, 주로 REST API에서 토큰 기반 인증(예: JWT)을 사용할 때 적용합니다.
        // 이 설정을 사용하면 HttpSession이 생성되지 않으며, SecurityContext가 요청 간에 공유되지 않습니다.
        // 따라서 각 요청마다 인증 정보(예: Authorization 헤더의 토큰)를 전달받아 처리해야 합니다.
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 4. HTTP 헤더 설정
        http.headers((headers) -> headers
                // X-Frame-Options 헤더 설정을 통해 클릭재킹(Clickjacking) 공격을 방지합니다.
                // 'sameOrigin()'은 현재 페이지와 동일한 출처(origin)의 프레임에서만 페이지를 표시하도록 허용합니다.
                // H2 데이터베이스 콘솔은 <frame> 또는 <iframe>을 사용하므로, 이 설정이 필요합니다.
                // 이 설정을 하지 않으면 H2 콘솔 화면이 제대로 표시되지 않을 수 있습니다.
                .frameOptions((frameOptions) -> frameOptions.sameOrigin())
        );
        // 5. (선택 사항) 폼 로그인 및 HTTP Basic 인증 비활성화
        // SessionCreationPolicy.STATELESS를 사용하고, JWT 등의 토큰 기반 인증을 사용한다면
        // 전통적인 폼 로그인이나 HTTP Basic 인증은 필요 없을 수 있습니다.
        // Spring Security 6는 특정 인증 메커니즘을 구성하지 않으면 기본적으로 비활성화되는 경향이 있지만, 명시적으로 비활성화할 수도 있습니다.
        // http.formLogin((formLogin) -> formLogin.disable()); // 폼 로그인 비활성화
        // http.httpBasic((httpBasic) -> httpBasic.disable()); // HTTP Basic 인증 비활성화

        // 구성된 HttpSecurity 객체를 기반으로 SecurityFilterChain을 빌드하여 반환합니다.
        // 이 SecurityFilterChain이 실제 보안 필터들의 체인으로 동작하게 됩니다.
        return http.build();
    }

}
