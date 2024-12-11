package com.iszion.api.config.jwt;

import com.iszion.api.auth.mapper.AuthMapper;
import com.iszion.api.sys.controller.SysController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    //private final RedisTemplate redisTemplate;

    private final AuthMapper authMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(SysController.class);

    private final String[] NO_FILTER_URL = {
            "/api/auth/login",
            "/api/auth/reissue",
            "/api/auth/logout"
    };

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isNoFilterURL = Arrays.asList(NO_FILTER_URL).contains(request.getRequestURI());
        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        // 2. validateToken 으로 토큰 유효성 검사
        if (isNoFilterURL || StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            try {
                // (추가) Redis 에 해당 accessToken logout 여부 확인
                //String isLogout = (String) redisTemplate.opsForValue().get(token);
                HashMap<String, Object> getToken = authMapper.getAccessToken(token);

                if (getToken != null) {
                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                LOGGER.info("Token Access Error");
            }
        } else {
            // 유효하지 않은 토큰 처리를 여기서 수행
            // 클라이언트에게 401 Unauthorized 응답을 보냄
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("유효하지 않거나 만료된 토큰");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
