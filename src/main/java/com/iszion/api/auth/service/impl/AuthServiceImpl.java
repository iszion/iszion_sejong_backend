package com.iszion.api.auth.service.impl;

import com.iszion.api.auth.dto.CustomUserDetails;
import com.iszion.api.auth.dto.Response;
import com.iszion.api.auth.dto.request.UserRequestDto;
import com.iszion.api.auth.dto.response.UserResponseDto;
import com.iszion.api.auth.mapper.AuthMapper;
import com.iszion.api.auth.service.AuthService;
import com.iszion.api.aux.controller.AuxController;
import com.iszion.api.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuxController.class);

    public final static long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 5; // 5시간

    @Override
    public ResponseEntity<?> signUp(UserRequestDto.SignUp signUp) {

        try {
            int valid = authMapper.existsUserId(signUp.getId());
            if (valid == 1) {
                return response.fail("이미 회원가입된 아이디입니다.", HttpStatus.BAD_REQUEST);
            }

            CustomUserDetails user = CustomUserDetails.builder()
                    .user_id(signUp.getId())
                    .passwd(passwordEncoder.encode(signUp.getPassword()))
                    .userName(signUp.getUserName())
                    .role("ROLE_USER")
                    .build();

            authMapper.insert(user);

            return response.success("회원가입에 성공 했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return response.success("회원가입에 실패 했습니다.");
        }


    }

    @Override
    public ResponseEntity<?> login(UserRequestDto.Login login, HttpServletResponse res) {

        try {
            int valid = authMapper.existsUserId(login.getId());
            if (valid == 0) {
                return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            }

            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
            UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
        } catch (AuthenticationException e) {
            System.out.println("인증 실패 : " + e.getMessage());
            return response.fail("로그인에 실패했습니다..", HttpStatus.BAD_REQUEST);

        }


    }

    public ResponseEntity<?> logout(UserRequestDto.Logout logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
        // 1. Refresh Token 검증
        String accessToken = reissue.getAccessToken().replace("\"", "");
        String refreshToken = reissue.getRefreshToken().replace("\"", "");
        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
            }


            // 2. Access Token 에서 User email 을 가져옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

            // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
            String oldRefreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());
            // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
            if (ObjectUtils.isEmpty(oldRefreshToken)) {
                return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
            }
            if (!oldRefreshToken.equals(refreshToken)) {
                return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
            }

            // 4. 새로운 토큰 생성
            UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            // 5. RefreshToken Redis 업데이트
            redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return response.fail("새로운 토큰 발행 실패", HttpStatus.BAD_REQUEST);
        }

    }
}