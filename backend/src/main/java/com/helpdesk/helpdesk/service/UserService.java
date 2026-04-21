package com.helpdesk.helpdesk.service;

import com.helpdesk.helpdesk.dto.request.CreateUserRequest;
import com.helpdesk.helpdesk.dto.request.UpdateUserRequest;
import com.helpdesk.helpdesk.dto.response.PageResponse;
import com.helpdesk.helpdesk.dto.response.UserResponse;
import com.helpdesk.helpdesk.entity.Role;
import com.helpdesk.helpdesk.entity.RoleName;
import com.helpdesk.helpdesk.entity.User;
import com.helpdesk.helpdesk.exception.BusinessException;
import com.helpdesk.helpdesk.mapper.UserMapper;
import com.helpdesk.helpdesk.repository.RoleRepository;
import com.helpdesk.helpdesk.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public PageResponse<UserResponse> findAll(String nome, String email, RoleName role, Boolean ativo, Pageable pageable) {
        return PageResponse.of(
                userRepository.findWithFilters(nome, email, role, ativo, pageable)
                        .map(userMapper::toResponse)
        );
    }

    public UserResponse findById(Long id) {
        return userMapper.toResponse(findUserOrThrow(id));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já está em uso", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setNome(request.nome());
        user.setEmail(request.email().toLowerCase().trim());
        user.setSenha(passwordEncoder.encode(request.senha()));
        user.setAtivo(true);
        user.setRoles(resolveRoles(request.roles()));

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = findUserOrThrow(id);

        if (!user.getEmail().equals(request.email().toLowerCase().trim())
                && userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já está em uso", HttpStatus.CONFLICT);
        }

        user.setNome(request.nome());
        user.setEmail(request.email().toLowerCase().trim());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void activate(Long id) {
        User user = findUserOrThrow(id);
        if (user.isAtivo()) {
            throw new BusinessException("Utilizador já está ativo", HttpStatus.CONFLICT);
        }
        user.setAtivo(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivate(Long id) {
        User user = findUserOrThrow(id);
        if (!user.isAtivo()) {
            throw new BusinessException("Utilizador já está desativado", HttpStatus.CONFLICT);
        }
        user.setAtivo(false);
        userRepository.save(user);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Utilizador não encontrado", HttpStatus.NOT_FOUND));
    }

    private Set<Role> resolveRoles(Set<RoleName> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Set.of(roleRepository.findByNome(RoleName.USUARIO)
                    .orElseThrow(() -> new BusinessException("Perfil USUARIO não encontrado", HttpStatus.INTERNAL_SERVER_ERROR)));
        }
        Set<Role> roles = new HashSet<>();
        for (RoleName name : roleNames) {
            roles.add(roleRepository.findByNome(name)
                    .orElseThrow(() -> new BusinessException("Perfil não encontrado: " + name, HttpStatus.BAD_REQUEST)));
        }
        return roles;
    }
}
