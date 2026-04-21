package com.helpdesk.helpdesk.repository;

import com.helpdesk.helpdesk.entity.Role;
import com.helpdesk.helpdesk.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNome(RoleName nome);
}
