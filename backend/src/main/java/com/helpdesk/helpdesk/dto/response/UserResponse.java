package com.helpdesk.helpdesk.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String nome,
        String email,
        boolean ativo,
        Set<String> perfis,
        LocalDateTime createdAt
) {}
