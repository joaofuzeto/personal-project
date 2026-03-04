# Sales API

REST API desenvolvida com Java 17 e Spring Boot para gerenciamento de usuários, clientes e assinaturas.

O projeto implementa autenticação JWT, arquitetura em camadas, testes unitários com Mockito e containerização com Docker.

---

## 🚀 Technologies

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- MySQL
- Docker & Docker Compose
- JUnit 5
- Mockito

---

## 🏗 Architecture

O projeto segue arquitetura em camadas, separando responsabilidades de forma clara:

- Entity → Camada representativa da entidade do Database
- Controller → Camada de entrada (HTTP)
- Service → Regras de negócio
- Repository → Persistência com JPA
- DTOs → Comunicação entre camadas
- Global Exception Handler → Tratamento centralizado de erros
- Infra → Responsável pela configuração Spring Security

Essa estrutura facilita manutenção, testes e escalabilidade.

---

## 📌 Features

- CRUD completo de Customers
- CRUD completo de Subscriptions
- Relacionamento entre Customer e Subscription
- Autenticação e autorização com JWT
- Validação de dados com Bean Validation
- Tratamento global de exceções
- Persistência com MySQL
- Containerização da aplicação
- Testes unitários na camada de serviço

---

## 🔐 Authentication

A API utiliza autenticação baseada em JWT.

Após realizar login (http://localhost:8080/auth/login), o token deve ser enviado no header:

Authorization: Bearer <token>

---

## 🧪 Tests

O projeto possui testes unitários utilizando:

- JUnit 5
- Mockito

Os testes cobrem a camada de serviço, garantindo regras de negócio e comportamento esperado.

Para rodar os testes:

mvn test

---

## 💻 Running Locally

### Clone the repository

git clone https://github.com/joaofuzeto/personal-project
cd sales-api

### Run with Docker

docker compose up --build

A aplicação estará disponível em:
http://localhost:8080

---

## 🗄 Database

O projeto utiliza MySQL como banco de dados.

A configuração é feita via variáveis de ambiente:

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- JWT_SECRET

---

## 🌍 Public Deployment

(Em breve)

---

## 📈 Future Improvements

- Paginação com Pageable
- Testcontainers para testes de integração
- Logging estruturado
- CI/CD pipeline
- Deploy em ambiente cloud

---

## 👨‍💻 Author

João Victor Fuzeto Nascibem