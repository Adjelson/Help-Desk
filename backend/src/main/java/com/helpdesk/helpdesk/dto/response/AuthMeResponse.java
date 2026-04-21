package com.helpdesk.helpdesk.dto.response;

import java.util.Set;

public record AuthMeResponse(
        Long id,
        String nome,
        String email,
        boolean ativo,
        Set<String> perfis
) {}
