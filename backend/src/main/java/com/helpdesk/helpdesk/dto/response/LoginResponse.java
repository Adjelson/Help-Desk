package com.helpdesk.helpdesk.dto.response;

import java.util.Set;

public record LoginResponse(
        String token,
        String tipo,
        Long id,
        String nome,
        String email,
        Set<String> perfis
) {
    public static LoginResponse of(String token, Long id, String nome, String email, Set<String> perfis) {
        return new LoginResponse(token, "Bearer", id, nome, email, perfis);
    }
}
