# Sistema de Help Desk / Ticketing / Atendimento

README do projeto orientado ao levantamento de requisitos funcional e técnico.

## Visão geral

O **Sistema de Help Desk / Ticketing / Atendimento** é uma plataforma web concebida para registar, organizar, acompanhar e auditar solicitações, incidentes, dúvidas e requisições submetidas por utilizadores a uma equipa de suporte ou atendimento.

O objetivo principal é substituir fluxos informais baseados em chamadas, mensagens, emails ou pedidos verbais por um processo rastreável, com responsáveis definidos, estados claros, histórico completo e indicadores de desempenho.

O mesmo núcleo funcional pode ser aplicado em diferentes contextos:

- suporte interno de TI
- atendimento ao cliente
- atendimento ao cidadão
- gestão de pedidos de serviço
- ambientes académicos, clínicos, empresariais e públicos

---

## Objetivo do projeto

Desenvolver uma plataforma web robusta para gestão de tickets com:

- autenticação e controlo de acesso por perfil
- workflow completo de tratamento de tickets
- comentários públicos e notas internas
- anexos e histórico de alterações
- auditoria de ações críticas
- dashboards e relatórios operacionais

---

## Problemas que o sistema resolve

Este sistema foi desenhado para resolver problemas recorrentes em processos de atendimento e suporte:

- pedidos perdem-se em canais informais
- falta histórico centralizado
- ausência de rastreabilidade sobre quem fez o quê e quando
- baixa visibilidade do estado dos pedidos
- dificuldade em priorizar atendimentos
- ausência de métricas de SLA, produtividade e qualidade

---

## Escopo funcional

### Incluído no escopo

- autenticação e gestão de perfis
- criação, consulta, atribuição e tratamento de tickets
- categorias, subcategorias, prioridades e tipos de ticket
- comentários, notas internas e anexos
- histórico de alterações
- dashboards e relatórios básicos
- notificações por email
- auditoria de ações críticas

### Fora do escopo inicial

- chat em tempo real
- integração com WhatsApp
- integração com central telefónica
- aplicação mobile nativa
- IA para triagem automática
- integrações corporativas avançadas, como Active Directory

---

## Perfis de utilizador

### Utilizador comum
Permissões principais:

- abrir ticket
- consultar tickets próprios
- comentar
- anexar ficheiros

### Técnico
Permissões principais:

- assumir tickets
- mudar estado
- responder ao solicitante
- registar solução
- utilizar notas internas

### Administrador
Permissões principais:

- gerir utilizadores
- gerir categorias e SLA
- controlar permissões
- consultar auditoria
- administrar relatórios

### Gestor
Permissões principais:

- consultar dashboards
- acompanhar SLA
- analisar produtividade e desempenho da equipa

---

## Requisitos funcionais principais

### Autenticação e segurança

- login por email e senha
- logout seguro
- recuperação e redefinição de senha
- controlo de acesso por perfil
- proteção contra acesso não autorizado

### Gestão de utilizadores

- criação, edição, ativação e desativação de utilizadores
- associação de papéis por utilizador
- pesquisa por nome, email, perfil e estado

### Gestão de tickets

- abertura de ticket com título, descrição, categoria e prioridade
- geração de código único por ticket
- estado inicial `Aberto`
- atribuição e reatribuição de tickets
- alteração de estado
- registo de solução
- fecho e reabertura segundo regras definidas
- consulta de histórico pelo utilizador

### Comentários e anexos

- comentários visíveis às partes autorizadas
- notas internas visíveis apenas à equipa
- upload e download de anexos com validação de tipo e tamanho

### Workflow, SLA e notificações

- estados padronizados do ciclo de vida
- cálculo de prazo por prioridade e/ou SLA
- sinalização de atraso ou risco
- envio de notificações por email em eventos críticos

### Administração e reporting

- gestão de categorias, subcategorias, prioridades e regras de SLA
- dashboards operacionais
- exportação de relatórios
- logs de auditoria

---

## Regras de negócio essenciais

- todo ticket deve possuir identificador único e solicitante definido
- o estado inicial de um ticket é sempre `Aberto`
- um ticket só pode ser marcado como `Resolvido` se houver descrição de solução
- apenas utilizadores autorizados podem visualizar tickets de terceiros
- notas internas nunca podem ser visíveis ao utilizador comum
- tickets fechados não podem ser editados sem reabertura
- a prioridade influencia os prazos de resposta e resolução
- reatribuição, mudança de estado e reabertura devem ser auditadas

---

## Estados e prioridades

### Estados do ticket

- Aberto
- Em análise
- Em progresso
- Aguardando utilizador
- Resolvido
- Fechado
- Cancelado

### Prioridades

- Baixa
- Média
- Alta
- Crítica

### Exemplo de SLA

| Prioridade | Resposta | Resolução |
|---|---:|---:|
| Baixa | 24h | 72h |
| Média | 8h | 24h |
| Alta | 4h | 8h |
| Crítica | 1h | 4h |

---

## Casos de uso principais

- **UC-01 Abrir ticket**  
  O utilizador autentica-se, preenche os dados e submete o pedido.

- **UC-02 Assumir ticket**  
  O técnico consulta a fila e assume a responsabilidade pelo atendimento.

- **UC-03 Atualizar ticket**  
  O técnico adiciona comentários, muda estado e regista progresso.

- **UC-04 Fechar ticket**  
  O ticket é encerrado após resolução, mantendo histórico completo.

- **UC-05 Reabrir ticket**  
  O ticket pode ser reaberto quando o problema persiste.

- **UC-06 Gerir utilizadores**  
  O administrador controla contas, perfis e acessos.

- **UC-07 Consultar dashboard**  
  O gestor acompanha volume, SLA e desempenho da equipa.

---

## Arquitetura técnica recomendada

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Bean Validation
- Lombok
- MapStruct
- Swagger / OpenAPI
- JUnit 5
- Mockito

### Base de dados e infraestrutura

- PostgreSQL
- Docker
- Docker Compose
- Nginx
- armazenamento local no MVP
- evolução futura para MinIO/S3

### Frontend

- React com TypeScript
- Tailwind CSS

> Alternativamente, pode ser utilizada uma primeira versão com Thymeleaf caso a estratégia priorize um backend monolítico com interface simples.

---

## Estrutura lógica sugerida do backend

```text
src/main/java/com/seu-dominio/helpdesk
├── controller
├── service
├── repository
├── dto
├── entity
├── mapper
├── security
├── exception
├── config
└── audit
```

### Responsabilidades por camada

- **controller**: recebe requisições HTTP e devolve respostas
- **service**: concentra regras de negócio e orquestração
- **repository**: acesso a dados via JPA
- **dto**: objetos de entrada e saída
- **entity**: modelos persistidos
- **mapper**: conversão entre entidades e DTOs
- **security**: autenticação, autorização, JWT e filtros
- **exception**: tratamento global de erros
- **config**: configurações gerais
- **audit**: rastreabilidade e logs críticos

---

## Modelo de dados inicial

Entidades nucleares sugeridas:

- **User**
- **Role**
- **UserRole**
- **Ticket**
- **Category**
- **Subcategory**
- **Comment**
- **Attachment**
- **TicketHistory**
- **SlaRule**
- **Notification**
- **AuditLog**

### Observações de modelagem

A entidade `Ticket` deve contemplar, no mínimo:

- identificador interno
- código único do ticket
- título
- descrição
- estado
- prioridade
- tipo
- solicitante
- técnico responsável
- categoria
- subcategoria
- datas relevantes
- resumo da solução

---

## Endpoints principais da API

### Autenticação

```http
POST /auth/login
POST /auth/forgot-password
POST /auth/reset-password
GET  /auth/me
```

### Utilizadores

```http
GET   /users
POST  /users
GET   /users/{id}
PUT   /users/{id}
PATCH /users/{id}/activate
PATCH /users/{id}/deactivate
```

### Tickets

```http
GET   /tickets
POST  /tickets
GET   /tickets/{id}
PATCH /tickets/{id}/assign
PATCH /tickets/{id}/status
PATCH /tickets/{id}/resolve
PATCH /tickets/{id}/close
PATCH /tickets/{id}/reopen
```

### Comentários e anexos

```http
GET  /tickets/{id}/comments
POST /tickets/{id}/comments
POST /tickets/{id}/attachments
GET  /attachments/{id}/download
```

### Administração e reporting

```http
GET  /categories
POST /categories
GET  /dashboard/overview
GET  /reports/tickets
GET  /audit-logs
```

---

## Requisitos não funcionais

### Segurança

- autenticação segura com Spring Security
- palavras-passe com hash forte, como BCrypt
- proteção de endpoints
- validação de permissões por perfil
- validação de entrada de dados

### Desempenho e disponibilidade

- listagens paginadas
- tempo de resposta aceitável nas operações críticas
- política de backup e recuperação
- disponibilidade em ambiente web estável

### Usabilidade e manutenibilidade

- interface clara e responsiva
- arquitetura em camadas
- separação entre controller, service e repository
- documentação técnica
- testes automatizados para regras críticas

### Evolução

- abertura para novas categorias e módulos
- possibilidade de evolução para multiempresa / SaaS

---

## Estratégia de desenvolvimento por fases

### Fase 1 — Fundação
- setup do projeto
- configuração da base de dados
- autenticação
- perfis
- CRUD inicial de utilizadores

### Fase 2 — Núcleo de tickets
- criação de tickets
- listagem
- detalhe
- atribuição
- mudança de estado
- histórico básico

### Fase 3 — Comunicação e anexos
- comentários
- notas internas
- anexos
- notificações por email

### Fase 4 — Administração
- categorias
- prioridades
- subcategorias
- regras de SLA
- gestão administrativa

### Fase 5 — Dashboard e relatórios
- indicadores principais
- filtros
- exportação
- análise por período

### Fase 6 — Hardening
- auditoria
- testes adicionais
- documentação final
- dockerização
- deploy

---

## MVP recomendado

A primeira versão recomendada deve incluir:

- autenticação
- perfis de acesso
- criação e consulta de tickets
- atribuição a técnico
- mudança de estado
- comentários
- categorias
- histórico
- documentação da API

Este conjunto já demonstra valor técnico e funcional suficiente para portfólio, prova de conceito ou início de operação controlada.

---

## Estrutura sugerida do repositório

```text
helpdesk-system/
├── backend/
├── frontend/
├── docs/
│   ├── requisitos/
│   ├── diagramas/
│   └── api/
├── docker/
├── scripts/
├── .env.example
├── docker-compose.yml
└── README.md
```

---

## Como executar localmente

> **Nota:** esta secção assume a estrutura recomendada do projeto. Ajusta os comandos conforme a implementação real do repositório.

### Pré-requisitos

- Java 21
- Node.js 20+
- Docker
- Docker Compose
- PostgreSQL (caso não seja executado via container)

### Passos gerais

```bash
# 1. Clonar o repositório
git clone <url-do-repositorio>
cd helpdesk-system

# 2. Subir dependências
docker compose up -d

# 3. Iniciar backend
cd backend
./mvnw spring-boot:run

# 4. Iniciar frontend
cd ../frontend
npm install
npm run dev
```

### Variáveis de ambiente esperadas

Exemplo:

```env
APP_NAME=helpdesk-system
SERVER_PORT=8080

DB_HOST=localhost
DB_PORT=5432
DB_NAME=helpdesk
DB_USER=postgres
DB_PASSWORD=postgres

JWT_SECRET=change-me
JWT_EXPIRATION=86400000

MAIL_HOST=smtp.example.com
MAIL_PORT=587
MAIL_USERNAME=user
MAIL_PASSWORD=password
MAIL_FROM=no-reply@example.com
```

---

## Qualidade e desenvolvimento

### Boas práticas esperadas

- uso de Git com branches por funcionalidade
- critérios de aceite por feature
- documentação OpenAPI ou coleção Postman
- testes unitários para regras críticas
- testes de integração para endpoints principais
- seeds para demonstração local

### Testes

Exemplo de comandos esperados:

```bash
# backend
./mvnw test

# frontend
npm test
```

---

## Critérios de aceite

O sistema será considerado aderente ao requisito quando permitir que:

- o utilizador se autentique com segurança
- o utilizador abra ticket e obtenha código único
- o técnico assuma, trate e encerre tickets
- cada alteração relevante fique registada em histórico
- as permissões sejam respeitadas por perfil
- o administrador consiga gerir utilizadores, categorias e parâmetros
- relatórios básicos e dashboards estejam disponíveis
- a aplicação possa ser executada localmente com instruções claras

---

## Riscos principais

| Risco | Impacto | Mitigação |
|---|---|---|
| Escopo excessivo | atraso e abandono | definir MVP e trabalhar por fases |
| Segurança fraca | exposição de dados | aplicar Spring Security corretamente e testar permissões |
| Modelagem inadequada do ticket | inconsistência e baixa evolutividade | definir estados, histórico e regras desde o início |
| Excesso de foco no frontend | backend fraco e incompleto | priorizar núcleo backend e API |
| Baixa cobertura de testes | regressões e menor confiança | testar regras críticas desde cedo |

---

## Funcionalidades futuras

- base de conhecimento e FAQ
- integração com email de entrada
- avaliação de satisfação
- chatbot ou triagem automática
- modo multiempresa / SaaS
- relatórios avançados
- observabilidade

---

## Estado do projeto

**Fase atual:** levantamento e estruturação técnica.  
Este README foi preparado como base para implementação, documentação do repositório e apresentação do projeto.

---

## Valor para portfólio

Este projeto demonstra competências em:

- Java e Spring Boot
- autenticação e autorização
- modelagem relacional
- APIs REST
- workflow empresarial
- arquitetura em camadas
- desenho de sistemas internos com valor real

---

## Próximos passos recomendados

1. Converter requisitos em backlog técnico.
2. Desenhar o diagrama entidade-relacionamento.
3. Criar o projeto base em Spring Boot.
4. Definir contrato inicial da API.
5. Implementar o MVP por fases.
6. Documentar decisões arquiteturais em `/docs`.

---

## Licença

Definir conforme a estratégia do projeto:

- uso académico
- portfólio pessoal
- uso interno privado
- licença open source

---

## Autor

Preencher com o teu nome, contacto e links relevantes:

- LinkedIn
- GitHub
- Email profissional
