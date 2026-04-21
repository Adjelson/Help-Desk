package com.helpdesk.helpdesk.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Senha deve ter mínimo 8 caracteres, uma letra maiúscula, um número e um caractere especial";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
