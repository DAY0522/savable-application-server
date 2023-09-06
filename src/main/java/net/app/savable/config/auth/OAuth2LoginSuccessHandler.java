package net.app.savable.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.config.auth.dto.OAuthAttributes;
import net.app.savable.domain.Member.MemberRepository;
import net.app.savable.domain.Member.dto.CustomOAuth2User;
import net.app.savable.security.JwtHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String TOKEN_TYPE = "Bearer ";
    private static final String OAUTH_SIGNUP_REDIRECT_URL = "http://localhost:8080/signup";
    private static final String LOGIN_REDIRECT_URL = "https://localhost:8080/main";

    private final JwtHelper helper;
    private final MemberRepository memberRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        System.out.printf("authentication : %s\n", authentication);
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        System.out.printf("oAuthAttributes : %s\n", customOAuth2User);
        log.info("토큰 발급 전@@");
        String accessToken = this.helper.generateToken(customOAuth2User.getName()); // 토큰 발급
        log.info("토큰 발급 성공!");
        response.addHeader(helper.getAccessHeader(), TOKEN_TYPE + accessToken);
        response.sendRedirect(OAUTH_SIGNUP_REDIRECT_URL);
        helper.sendAccessToken(response, accessToken);
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuthAttributes) throws IOException {
        String accessToken = this.helper.generateToken(oAuthAttributes.getName()); // 토큰 발급
        response.addHeader(helper.getAccessHeader(), "Bearer " + accessToken);
        helper.sendAccessToken(response, accessToken);
        response.sendRedirect(LOGIN_REDIRECT_URL);
    }
}
