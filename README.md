<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/e00430af-2362-4be9-9d58-a4cbbd59a69a" /># 🚀 Task Manager API

API REST para gerenciamento de tarefas com Spring Boot 3 e Java 17.

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.2+
- Spring Data JPA
- PostgreSQL / H2
- Flyway
- Docker
- JUnit 5 + Mockito
- Maven

## 📋 Funcionalidades

- ✅ CRUD completo de usuários e tarefas
- ✅ Filtros e paginação
- ✅ Estatísticas por usuário
- ✅ Validação de dados
- ✅ Tratamento de erros
- ✅ Monitoramento com Actuator
- ✅ Migrations com Flyway
- ✅ Testes unitários

🎯 Endpoints:

USERS:

- ✅ POST   /users                    → Criar usuário
- ✅ GET    /users/{id}               → Buscar usuário por ID
- ✅ GET    /users/search?email=...   → Buscar usuário por email
- ✅ GET    /users                    → Listar usuários (paginado)
- ✅ PUT    /users/{id}               → Atualizar usuário
- ✅ DELETE /users/{id}               → Deletar usuário
- ✅ GET    /users/{id}/tasks         → Listar tasks do usuário (com filtro opcional)
- ✅ GET    /users/{id}/stats         → Estatísticas do usuário

TASKS:
- ✅ POST   /tasks                    → Criar task
- ✅ GET    /tasks/{id}               → Buscar task por ID
- ✅ GET    /tasks                    → Listar tasks (com filtro opcional por status)
- ✅ PUT    /tasks/{id}               → Atualizar task
- ✅ DELETE /tasks/{id}               → Deletar task

## 🚀 Como executar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Docker (opcional)

### Executar localmente (H2)
```bash
mvn spring-boot:run

