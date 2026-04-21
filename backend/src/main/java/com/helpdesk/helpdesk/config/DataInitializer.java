package com.helpdesk.helpdesk.config;

import com.helpdesk.helpdesk.entity.RoleName;
import com.helpdesk.helpdesk.entity.User;
import com.helpdesk.helpdesk.repository.RoleRepository;
import com.helpdesk.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@helpdesk.com}")
    private String adminEmail;

    @Value("${app.admin.senha:Admin@123456}")
    private String adminSenha;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initialize() {
        if (userRepository.existsByEmail(adminEmail)) return;

        var adminRole = roleRepository.findByNome(RoleName.ADMIN)
                .orElseThrow(() -> new IllegalStateException("Role ADMIN não encontrada — execute as migrações Flyway primeiro"));

        User admin = new User();
        admin.setNome("Administrador");
        admin.setEmail(adminEmail);
        admin.setSenha(passwordEncoder.encode(adminSenha));
        admin.setAtivo(true);
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}
