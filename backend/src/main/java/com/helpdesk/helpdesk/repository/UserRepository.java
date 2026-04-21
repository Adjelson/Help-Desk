package com.helpdesk.helpdesk.repository;

import com.helpdesk.helpdesk.entity.RoleName;
import com.helpdesk.helpdesk.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM User u JOIN u.roles r
        WHERE (:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
          AND (:role IS NULL OR r.nome = :role)
          AND (:ativo IS NULL OR u.ativo = :ativo)
    """)
    Page<User> findWithFilters(
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("role") RoleName role,
            @Param("ativo") Boolean ativo,
            Pageable pageable
    );
}
