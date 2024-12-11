package com.iszion.api.auth.controller;

import com.iszion.api.auth.dto.Response;
import com.iszion.api.auth.dto.request.UserRequestDto;
import com.iszion.api.auth.lib.Helper;
import com.iszion.api.auth.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    private final Response response;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Validated UserRequestDto.SignUp signUp, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.signUp(signUp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody UserRequestDto.Login login, Errors errors, HttpServletResponse res) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.login(login, res);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Validated @RequestBody UserRequestDto.Logout logout, @RequestHeader("Authorization") String token, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.logout(logout);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Validated @RequestBody UserRequestDto.Reissue reissue, @RequestHeader("Authorization") String token, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.reissue(reissue);
    }

}
