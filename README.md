# Sales API

REST API desenvolvida com **Java 17 + Spring Boot** para gerenciamento de usuários, clientes e assinaturas.

O projeto implementa **autenticação JWT**, **arquitetura em camadas**, **testes unitários com Mockito** e **containerização com Docker**, além de **deploy em cloud utilizando Railway**.

---

# 🌍 Live API

API disponível publicamente:

https://personal-project-production-748e.up.railway.app

---

# 📚 API Documentation (Swagger)

A documentação interativa da API pode ser acessada em:

https://personal-project-production-748e.up.railway.app/swagger-ui/index.html

Através do Swagger é possível:

- visualizar todos os endpoints
- testar requisições
- autenticar utilizando JWT

---

# 🚀 Technologies

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- MySQL
- Docker & Docker Compose
- JUnit 5
- Mockito
- Railway (Cloud Deployment)

---

# 🏗 Architecture

O projeto segue **arquitetura em camadas**, separando responsabilidades:

Controller → Camada de entrada HTTP
Service → Regras de negócio
Repository → Persistência com JPA
Entity → Representação das tabelas do banco
DTO → Transferência de dados entre camadas
Security → Autenticação e autorização JWT
GlobalExceptionHandler → Tratamento centralizado de erros


Essa estrutura melhora:

- manutenção
- testabilidade
- escalabilidade

---

# 📌 Features

- CRUD completo de **Customers**
- CRUD completo de **Subscriptions**
- Relacionamento entre **Customer e Subscription**
- Autenticação segura com **JWT**
- **Role-based authorization** (ADMIN / USER)
- Validação de dados com **Bean Validation**
- Tratamento global de exceções
- Persistência com **MySQL**
- Containerização com **Docker** e **Docker Compose**
- Deploy em **cloud (Railway)**
- Testes unitários na camada de serviço

---

# 🔐 Authentication

A API utiliza autenticação baseada em **JWT**.

### Login

POST /auth/login


Após autenticação, a API retorna um **token JWT**.

Esse token deve ser enviado no header das requisições protegidas:

Authorization: Bearer <token>

O Swagger permite autenticação através do botão **Authorize**.

Endpoints que não necessitam de autenticação e não exigem autorização:

POST /auth/login
POST /v1/users

Certifique-se de definir o campo `"role" : "ADMIN"` na criação de um usuário para acesso total aos endpoints.
---

# 📡 Main Endpoints


POST /auth/login

GET /v1/users
GET /v1/users/{id}
POST /v1/users
PUT /v1/users/{id}
DELETE /v1/users/{id}

GET /v1/customers
GET /v1/customers/{id}
POST /v1/customers
PUT /v1/customers/{id}
DELETE /v1/customers/{id}

GET /v1/subscriptions
GET /v1/subscriptions/{id}
POST /v1/subscriptions
PUT /v1/subscriptions/{id}
DELETE /v1/subscriptions/{id}

---

# 🧪 Tests

O projeto possui **testes unitários na camada de serviço** utilizando:

- JUnit 5
- Mockito

Para rodar os testes:

mvn test

---

# 💻 Running Locally

Clone o repositório:

git clone https://github.com/joaofuzeto/personal-project

cd sales-api


Rodar com Docker:

docker compose up --build


A aplicação estará disponível em:

http://localhost:8080

---

# 🗄 Database

O projeto utiliza **MySQL** como banco de dados.

A configuração é feita através de variáveis de ambiente:

SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET

---

# 📈 Future Improvements

- Paginação com Pageable
- Testcontainers para testes de integração
- Logging estruturado
- CI/CD pipeline com GitHub Actions
- Monitoramento da aplicação

---

# 👨‍💻 Author

**João Victor Fuzeto Nascibem**

Backend Developer | Java | Spring Boot



