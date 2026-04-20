# Backlog Técnico — Sistema de Help Desk

## Fase 1 — Fundação (Semanas 1–2)

### Setup e infraestrutura
- [ ] Criar projeto Spring Boot com dependências: Web, Security, JPA, Validation, Lombok, MapStruct, Swagger
- [ ] Configurar `docker-compose.yml` com MySQL
- [ ] Configurar `application.yml` com perfis `dev` e `prod`
- [ ] Criar arquivo `.env.example` com todas as variáveis necessárias
- [ ] Configurar estrutura de pacotes: controller, service, repository, dto, entity, mapper, security, exception, config, audit

### Banco de dados
- [ ] Criar entidade `User` (id, nome, email, senha, ativo, dataCriacao)
- [ ] Criar entidade `Role` (id, nome: ADMIN, TECNICO, GESTOR, USUARIO)
- [ ] Criar entidade `UserRole` (relacionamento User ↔ Role)
- [ ] Configurar Flyway ou scripts SQL de migração iniciais (MySQL)

### Autenticação e segurança
- [ ] Implementar `POST /auth/login` com JWT
- [ ] Implementar `POST /auth/forgot-password`
- [ ] Implementar `POST /auth/reset-password`
- [ ] Implementar `GET /auth/me`
- [ ] Configurar filtro JWT no Spring Security
- [ ] Configurar BCrypt para hash de senhas
- [ ] Proteger endpoints por perfil com `@PreAuthorize`

### Gestão de utilizadores
- [ ] Implementar `GET /users` (paginado, filtros: nome, email, perfil, estado)
- [ ] Implementar `POST /users`
- [ ] Implementar `GET /users/{id}`
- [ ] Implementar `PUT /users/{id}`
- [ ] Implementar `PATCH /users/{id}/activate`
- [ ] Implementar `PATCH /users/{id}/deactivate`
- [ ] Criar DTOs de entrada e saída para User
- [ ] Criar mapper User ↔ DTO

### Qualidade
- [ ] Configurar tratamento global de exceções (`@ControllerAdvice`)
- [ ] Configurar Swagger/OpenAPI
- [ ] Escrever testes unitários para AuthService e UserService
- [ ] Criar seed de dados iniciais (admin, técnico, utilizador de teste)

---

## Fase 2 — Núcleo de Tickets (Semanas 3–5)

### Entidades
- [ ] Criar entidade `Category` (id, nome, ativo)
- [ ] Criar entidade `Subcategory` (id, nome, category, ativo)
- [ ] Criar entidade `Ticket` (id, código único, título, descrição, estado, prioridade, tipo, solicitante, técnico, categoria, subcategoria, datas, resumo da solução)
- [ ] Criar entidade `TicketHistory` (id, ticket, campo alterado, valor anterior, novo valor, utilizador, data)
- [ ] Criar enum `TicketStatus`: ABERTO, EM_ANALISE, EM_PROGRESSO, AGUARDANDO_UTILIZADOR, RESOLVIDO, FECHADO, CANCELADO
- [ ] Criar enum `Priority`: BAIXA, MEDIA, ALTA, CRITICA

### Endpoints de tickets
- [ ] Implementar `GET /tickets` (paginado, filtros: estado, prioridade, técnico, solicitante, período)
- [ ] Implementar `POST /tickets`
- [ ] Implementar `GET /tickets/{id}`
- [ ] Implementar `PATCH /tickets/{id}/assign`
- [ ] Implementar `PATCH /tickets/{id}/status`
- [ ] Implementar `PATCH /tickets/{id}/resolve` (exige descrição de solução)
- [ ] Implementar `PATCH /tickets/{id}/close`
- [ ] Implementar `PATCH /tickets/{id}/reopen`

### Regras de negócio
- [ ] Gerar código único ao criar ticket (ex: HD-0001)
- [ ] Estado inicial sempre `ABERTO`
- [ ] Validar que só resolve com descrição de solução preenchida
- [ ] Impedir edição de ticket fechado sem reabertura
- [ ] Registar entrada em `TicketHistory` em cada mudança relevante
- [ ] Validar permissões por perfil em cada operação

### Qualidade
- [ ] Testes unitários para TicketService (criação, transições de estado, regras de negócio)
- [ ] Testes de integração para endpoints principais
- [ ] DTOs e mappers para Ticket e TicketHistory

---

## Fase 3 — Comunicação e Anexos (Semanas 6–7)

### Entidades
- [ ] Criar entidade `Comment` (id, ticket, autor, conteúdo, notaInterna: boolean, data)
- [ ] Criar entidade `Attachment` (id, ticket, nomeOriginal, caminho, tamanho, tipo MIME, autor, data)
- [ ] Criar entidade `Notification` (id, destinatário, título, mensagem, lida, data)

### Endpoints
- [ ] Implementar `GET /tickets/{id}/comments`
- [ ] Implementar `POST /tickets/{id}/comments` (distinguir comentário público de nota interna)
- [ ] Implementar `POST /tickets/{id}/attachments` (upload com validação de tipo e tamanho)
- [ ] Implementar `GET /attachments/{id}/download`

### Regras de negócio
- [ ] Notas internas nunca visíveis ao utilizador comum
- [ ] Validar tipo de ficheiro permitido (ex: pdf, png, jpg, docx)
- [ ] Validar tamanho máximo de anexo (configurável, ex: 10MB)
- [ ] Armazenamento local no MVP (preparar interface para migração futura para MinIO/S3)

### Notificações por email
- [ ] Configurar JavaMailSender com variáveis de ambiente
- [ ] Enviar email ao solicitante quando estado mudar
- [ ] Enviar email ao técnico quando ticket for atribuído
- [ ] Enviar email ao solicitante quando ticket for resolvido ou fechado
- [ ] Criar templates de email básicos (HTML simples)

### Qualidade
- [ ] Testes unitários para CommentService e NotificationService
- [ ] Testes de integração para upload e download de anexos

---

## Fase 4 — Administração (Semanas 8–9)

### Entidades
- [ ] Criar entidade `SlaRule` (id, prioridade, prazoResposta, prazoResolucao, ativo)

### Endpoints de administração
- [ ] Implementar `GET /categories`
- [ ] Implementar `POST /categories`
- [ ] Implementar `PUT /categories/{id}`
- [ ] Implementar `PATCH /categories/{id}/activate` e `/deactivate`
- [ ] Implementar CRUD de subcategorias
- [ ] Implementar CRUD de regras de SLA

### SLA no ticket
- [ ] Calcular prazo de resposta e resolução ao criar ticket com base na prioridade
- [ ] Sinalizar tickets em atraso ou em risco (campo calculado ou job)
- [ ] Expor campo `slaStatus` nos endpoints de listagem e detalhe

### Qualidade
- [ ] Testes unitários para SlaService
- [ ] Seeds de categorias, subcategorias e regras de SLA padrão

---

## Fase 5 — Dashboard e Relatórios (Semanas 10–12)

### Endpoints
- [ ] Implementar `GET /dashboard/overview` (total por estado, por prioridade, tickets em atraso, tickets abertos por técnico)
- [ ] Implementar `GET /reports/tickets` (filtros: período, técnico, categoria, prioridade, estado; exportação CSV)

### Métricas esperadas
- [ ] Volume de tickets por período
- [ ] Tempo médio de resolução
- [ ] Taxa de cumprimento de SLA
- [ ] Produtividade por técnico
- [ ] Tickets por categoria

### Frontend (React + TypeScript)
- [ ] Setup do projeto React com TypeScript e Tailwind CSS
- [ ] Configurar React Router e estrutura de páginas
- [ ] Implementar página de login
- [ ] Implementar dashboard com cards de indicadores
- [ ] Implementar listagem de tickets com filtros e paginação
- [ ] Implementar formulário de criação de ticket
- [ ] Implementar página de detalhe do ticket (histórico, comentários, anexos)
- [ ] Implementar gestão de utilizadores (admin)
- [ ] Implementar gestão de categorias e SLA (admin)
- [ ] Configurar Axios com interceptor JWT

### Qualidade
- [ ] Testes de integração para endpoints de dashboard e relatórios

---

## Fase 6 — Hardening (Semanas 13–14)

### Auditoria
- [ ] Criar entidade `AuditLog` (id, utilizador, ação, entidade, entidadeId, detalhes, data, IP)
- [ ] Implementar `GET /audit-logs` (paginado, filtros: utilizador, ação, período)
- [ ] Registar em AuditLog: login, criação/fecho/reabertura de ticket, mudança de estado, reatribuição, gestão de utilizadores

### Testes e qualidade
- [ ] Revisar cobertura de testes — garantir regras críticas cobertas
- [ ] Testes E2E básicos nos fluxos principais (opcional: Playwright ou Cypress)
- [ ] Revisar todos os endpoints com Swagger e validar documentação

### Dockerização e deploy
- [ ] Criar `Dockerfile` para o backend
- [ ] Criar `Dockerfile` para o frontend
- [ ] Atualizar `docker-compose.yml` com backend + frontend + PostgreSQL + Nginx
- [ ] Configurar Nginx como proxy reverso
- [ ] Testar stack completa com `docker compose up`
- [ ] Documentar passos de deploy em `/docs`

### Revisão final
- [ ] Revisar todas as permissões por perfil
- [ ] Validar regras de negócio críticas end-to-end
- [ ] Preencher secção de autor no README
- [ ] Definir licença do projeto

---

## Buffer (Semanas 15–16)

- [ ] Corrigir bugs encontrados em testes finais
- [ ] Refinar UI nos pontos mais fracos
- [ ] Preparar demonstração (seed de dados realistas)
- [ ] Gravar demo ou preparar apresentação para portfólio
