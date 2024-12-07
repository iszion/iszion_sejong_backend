package com.iszion.api.auth.service;

import com.iszion.api.auth.dto.request.UserRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


public interface AuthService {

    ResponseEntity<?> signUp(UserRequestDto.SignUp signUp);
    ResponseEntity<?> login(UserRequestDto.Login login, HttpServletResponse res);
}
