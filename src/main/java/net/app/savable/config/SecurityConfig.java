package net.app.savable.config;

import lombok.RequiredArgsConstructor;
import net.app.savable.config.auth.CustomOAuth2UserService;
import net.app.savable.config.auth.OAuth2LoginFailureHandler;
import net.app.savable.config.auth.OAuth2LoginSuccessHandler;
import net.app.savable.security.JwtAuthenticationEntryPoint;
import net.app.savable.security.JwtAuthenticationFilter;
import net.app.savable.security.JwtHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint point;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final JwtHelper jwtHelper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable()) // 기본 로그인 페이지 사용 안함
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 로그인 페이지 사용 안함
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/auth/**", "/oauth2/authorization/**").permitAll()
                                .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .logout(logout -> logout.logoutSuccessUrl("/")) // 로그아웃 기능에 대한 여러 설정의 진입점
                .oauth2Login(oauth2Login -> oauth2Login // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                                .userService(customOAuth2UserService) // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
                        )
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtHelper), UsernamePasswordAuthenticationFilter.class); // 필터 등록
        return http.build();
    }
}
