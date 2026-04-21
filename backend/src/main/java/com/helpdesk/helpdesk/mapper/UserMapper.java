package com.helpdesk.helpdesk.mapper;

import com.helpdesk.helpdesk.dto.response.UserResponse;
import com.helpdesk.helpdesk.entity.Role;
import com.helpdesk.helpdesk.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "perfis", source = "roles")
    UserResponse toResponse(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(r -> r.getNome().name())
                .collect(Collectors.toSet());
    }
}
