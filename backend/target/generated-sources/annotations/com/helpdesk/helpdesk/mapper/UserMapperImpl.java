package com.helpdesk.helpdesk.mapper;

import com.helpdesk.helpdesk.dto.response.UserResponse;
import com.helpdesk.helpdesk.entity.User;
import java.time.LocalDateTime;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-20T22:04:38+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Set<String> perfis = null;
        Long id = null;
        String nome = null;
        String email = null;
        boolean ativo = false;
        LocalDateTime createdAt = null;

        perfis = mapRoles( user.getRoles() );
        id = user.getId();
        nome = user.getNome();
        email = user.getEmail();
        ativo = user.isAtivo();
        createdAt = user.getCreatedAt();

        UserResponse userResponse = new UserResponse( id, nome, email, ativo, perfis, createdAt );

        return userResponse;
    }
}
