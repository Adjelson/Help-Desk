package com.helpdesk.helpdesk.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    // Mínimo 8 chars, 1 maiúscula, 1 número, 1 especial
    private static final Pattern PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$"
    );

    @Override
    public boolean isValid(String senha, ConstraintValidatorContext ctx) {
        if (senha == null) return false;
        return PATTERN.matcher(senha).matches();
    }
}
