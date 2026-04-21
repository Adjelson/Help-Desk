package com.helpdesk.helpdesk.controller;

import com.helpdesk.helpdesk.dto.request.ForgotPasswordRequest;
import com.helpdesk.helpdesk.dto.request.LoginRequest;
import com.helpdesk.helpdesk.dto.request.ResetPasswordRequest;
import com.helpdesk.helpdesk.dto.response.AuthMeResponse;
import com.helpdesk.helpdesk.dto.response.LoginResponse;
import com.helpdesk.helpdesk.entity.User;
import com.helpdesk.helpdesk.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                        HttpServletRequest httpRequest) {
        return ResponseEntity.ok(authService.login(request, extractClientIp(httpRequest)));
    }

    @PostMapping("/forgot-password")
    ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    ResponseEntity<AuthMeResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.me(user));
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
