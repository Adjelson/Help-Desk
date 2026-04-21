package com.helpdesk.helpdesk.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RoleName nome;

    public Role() {}

    public Role(RoleName nome) {
        this.nome = nome;
    }

    public Long getId()       { return id; }
    public RoleName getNome() { return nome; }
    public void setNome(RoleName nome) { this.nome = nome; }
}
