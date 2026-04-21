package com.helpdesk.helpdesk.service;

import com.helpdesk.helpdesk.dto.request.ForgotPasswordRequest;
import com.helpdesk.helpdesk.dto.request.LoginRequest;
import com.helpdesk.helpdesk.dto.request.ResetPasswordRequest;
import com.helpdesk.helpdesk.dto.response.AuthMeResponse;
import com.helpdesk.helpdesk.dto.response.LoginResponse;
import com.helpdesk.helpdesk.entity.User;
import com.helpdesk.helpdesk.exception.BusinessException;
import com.helpdesk.helpdesk.repository.UserRepository;
import com.helpdesk.helpdesk.security.JwtService;
import com.helpdesk.helpdesk.security.LoginAttemptService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public AuthService(AuthenticationManager authManager,
                       JwtService jwtService,
                       UserRepository userRepository,
                       LoginAttemptService loginAttemptService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    public LoginResponse login(LoginRequest request, String clientIp) {
        if (loginAttemptService.isBlocked(clientIp)) {
            throw new BusinessException(
                    "Demasiadas tentativas de login. Tente novamente mais tarde.",
                    HttpStatus.TOO_MANY_REQUESTS);
        }

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha())
            );
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(clientIp);
            // Vague message prevents email enumeration
            throw new BusinessException("Email ou senha inválidos", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos", HttpStatus.UNAUTHORIZED));

        if (!user.isAtivo()) {
            loginAttemptService.loginFailed(clientIp);
            throw new BusinessException("Conta desativada. Contacte o administrador.", HttpStatus.FORBIDDEN);
        }

        loginAttemptService.loginSucceeded(clientIp);

        String token = jwtService.generateToken(user);
        var perfis = user.getRoles().stream()
                .map(r -> r.getNome().name())
                .collect(Collectors.toSet());

        return LoginResponse.of(token, user.getId(), user.getNome(), user.getEmail(), perfis);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        // Always return success to prevent email enumeration (Fase 3: send reset email)
        userRepository.findByEmail(request.email());
    }

    public void resetPassword(ResetPasswordRequest request) {
        // Fase 3: validar token de reset e atualizar senha com BCrypt
        throw new BusinessException("Funcionalidade disponível na Fase 3", HttpStatus.NOT_IMPLEMENTED);
    }

    public AuthMeResponse me(User user) {
        var perfis = user.getRoles().stream()
                .map(r -> r.getNome().name())
                .collect(Collectors.toSet());
        return new AuthMeResponse(user.getId(), user.getNome(), user.getEmail(), user.isAtivo(), perfis);
    }
}
