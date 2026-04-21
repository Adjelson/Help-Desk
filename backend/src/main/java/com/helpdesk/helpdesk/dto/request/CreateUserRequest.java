package com.helpdesk.helpdesk.dto.request;

import com.helpdesk.helpdesk.entity.RoleName;
import com.helpdesk.helpdesk.security.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateUserRequest(
        @NotBlank @Size(min = 2, max = 120) String nome,
        @NotBlank @Email String email,
        @NotBlank @ValidPassword String senha,
        Set<RoleName> roles
) {}
