# 🚀 Todo List API — Spring Boot + MySQL + Docker

CRUD simples para praticar Java + Spring Boot + JPA + MySQL rodando no Docker.

README.md gerado por IA para documentação prática do aprendizado.

---

## 🧱 Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL 8.4 (Docker)
- Maven

---

## 📦 Entidade

Task {
Long id
String description
boolean finished
}

Tabela: tasks

---

# 🐳 Subindo o MySQL no Docker

Arquivo docker-compose.yml:

Subir banco:

docker compose up -d

---

# ▶️ Rodando a aplicação

./mvnw spring-boot:run

ou

mvn spring-boot:run

Servidor sobe em:

http://localhost:8080

---

# 📌 Endpoints

Base URL:

/tasks

---

## ✅ Criar Task

curl -X POST http://localhost:8080/tasks \
-H "Content-Type: application/json" \
-d '{"description": "Create endpoint GET /issues/:id", "finished": false}'

---

## 📄 Listar todas

curl -X GET http://localhost:8080/tasks -v | python3 -m json.tool

---

## 🔎 Buscar por ID

curl -X GET http://localhost:8080/tasks/1 -v | python3 -m json.tool

---

## 🔄 Atualizar (PATCH)

curl -X PATCH http://localhost:8080/tasks/1 \
-H "Content-Type: application/json" \
-d '{"description": "Endpoint PATCH /tasks/:id", "finished": true}' \
-v | python3 -m json.tool

---

## ❌ Deletar

curl -X DELETE http://localhost:8080/tasks/2 \
-v | python3 -m json.tool

---

# 🗂 Estrutura

entity/
service/
controller/
repository/

Arquitetura:

Controller → Service → Repository → MySQL

---

# 🎯 Objetivo do Projeto

Praticar:

- JPA
- Tipos primitivos do Java
- Spring e Springboot
- Injeção de dependência
- Docker com banco relacional
- CRUD REST

---