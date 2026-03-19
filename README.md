# Sales API

REST API desenvolvida com **Java 17 + Spring Boot** para gerenciamento de usuários, clientes e assinaturas.

O projeto implementa **autenticação JWT**, **arquitetura em camadas**, **testes unitários com Mockito** e **containerização com Docker**, além de **deploy em cloud**.

---

# 🌍 Live API

API disponível publicamente:

🌍 **Live API:** [Clique aqui](https://personal-project-production-748e.up.railway.app)  
📚 **Swagger Docs:** [Clique aqui](https://personal-project-production-748e.up.railway.app/swagger-ui/index.html)

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

**Tech Stack:**
- Java 17 & Spring Boot → API REST com arquitetura limpa
- Spring Security + JWT → Autenticação e autorização seguras
- Spring Data JPA + MySQL → Persistência robusta
- Docker & Docker Compose → Containerização e facilidade de deploy
- JUnit 5 & Mockito → Testes unitários da camada de serviço
- Railway → Deploy em cloud gratuito e rápido

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

# 📌 Principais Features

- CRUD completo de Customers e Subscriptions com relacionamento
- Autenticação segura com JWT e roles (ADMIN / USER)
- Validação e tratamento global de erros
- Containerização com Docker & Docker Compose
- Testes unitários com Mockito

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

SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/sales_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=senha
JWT_SECRET=supersecretkey

---

# 📈 Future Improvements

- Logging estruturado
- CI/CD pipeline com GitHub Actions
- Deploy em ambiente AWS
- Monitoramento da aplicação

---

# 👨‍💻 Author

**João Victor Fuzeto Nascibem**

Backend Developer | Java | Spring Boot

<div>
  <a href= "https://www.linkedin.com/in/joao-fuzeto" target= "_blank"> <img src= "https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a>
</div>

