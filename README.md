# Restaurante API

[![CI](https://github.com/teeagohenrique/Restaurante-Projeto-Next/actions/workflows/ci.yml/badge.svg)](https://github.com/teeagohenrique/Restaurante-Projeto-Next/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?logo=apachemaven&logoColor=white)
![H2](https://img.shields.io/badge/H2-em%20mem%C3%B3ria-1B4F72)
![Testes](https://img.shields.io/badge/testes-8%20passando-brightgreen)

API REST para o cardápio de um restaurante: CRUD de produtos, busca por nome,
validação de entrada e banco em memória para rodar sem nenhuma configuração.

> Projeto acadêmico de Spring Boot que foi **reestruturado**: o código original
> não subia (sem datasource), o controller não injetava o repositório e a
> entidade não serializava. Hoje sobe, responde e tem testes de integração.

---

## O que a API faz

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/produtos` | Lista o cardápio. Aceita `?nome=` para busca parcial, sem diferenciar maiúsculas. |
| `GET` | `/produtos/{id}` | Busca um produto. Retorna `404` se não existir. |
| `POST` | `/produtos` | Cadastra um produto. Retorna `201` com o recurso criado. |
| `PUT` | `/produtos/{id}` | Atualiza nome, descrição e valor. |
| `DELETE` | `/produtos/{id}` | Remove o produto. Retorna `204`. |

### Exemplos

```bash
# listar
curl http://localhost:8080/produtos

# buscar por nome
curl "http://localhost:8080/produtos?nome=moqueca"

# cadastrar
curl -X POST http://localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Tapioca de carne de sol","descricao":"Com queijo coalho","valor":24.90}'

# atualizar
curl -X PUT http://localhost:8080/produtos/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Baião de dois","descricao":"Porção individual","valor":44.00}'

# remover
curl -X DELETE http://localhost:8080/produtos/1
```

### Validação

Entradas inválidas retornam `400` com o campo e o motivo:

```json
{
  "erro": "dados inválidos",
  "campos": {
    "nome": "nome é obrigatório",
    "valor": "valor não pode ser negativo"
  }
}
```

Regras: `nome` obrigatório (até 120 caracteres), `descricao` até 500 caracteres,
`valor` obrigatório e não negativo (`BigDecimal`, precisão de centavos).

---

## Stack

- **Java 11** e **Spring Boot 2.7.18**
- **Spring Web** (REST) e **Bean Validation** (`javax.validation`)
- **Spring Data JPA** / Hibernate
- **H2** em memória (padrão) e **MySQL** (perfil opcional)
- **JUnit 5 + MockMvc** para testes de integração
- **Maven Wrapper** (não exige Maven instalado)

---

## Como rodar

Pré-requisito: **JDK 11+**.

```bash
# sobe com H2 em memória, já com 4 itens de exemplo
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API fica em `http://localhost:8080/produtos`.

Console do H2: `http://localhost:8080/h2-console`
JDBC URL `jdbc:h2:mem:restaurante`, usuário `sa`, senha vazia.

### MySQL (opcional)

```bash
DB_HOST=localhost DB_NAME=restaurante DB_USER=root DB_PASSWORD=segredo \
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

O perfil `mysql` usa `ddl-auto=update` e não executa o `data.sql`.

---

## Testes

```bash
./mvnw test
```

**8 testes** cobrindo contexto da aplicação, listagem, criação, validação,
busca por nome, atualização, remoção e `404`. Rodam contra H2 em memória, sem
dependência de banco externo.

---

## Estrutura

```text
src/main/java/com/example/restaurante/
├── RestauranteApplication.java
├── controller/
│   ├── ProdutoController.java      # rotas REST
│   └── ApiExceptionHandler.java    # erros de validação em JSON
├── model/
│   └── Produto.java                # entidade + validações
└── repository/
    └── ProdutoRepository.java      # Spring Data JPA + busca por nome

src/main/resources/
├── application.properties          # H2 (padrão)
├── application-mysql.properties    # perfil MySQL
└── data.sql                        # cardápio de exemplo

src/test/java/com/example/restaurante/
├── RestauranteApplicationTests.java
└── ProdutoControllerTest.java
```

---

## Decisões de projeto

| Decisão | Motivo |
| :--- | :--- |
| H2 em memória como padrão | Permite clonar e rodar em um comando, sem instalar banco. |
| Perfil `mysql` separado | Mostra configuração por ambiente sem acoplar o demo a um servidor. |
| `BigDecimal` para valor | Evita erro de arredondamento de `double` em dinheiro. |
| Injeção por construtor | Dependência explícita, testável e sem campo nulo. |
| `404` com mensagem | Diferencia recurso inexistente de erro de servidor. |
| Testes com `MockMvc` | Cobrem contrato HTTP real (status, JSON, validação), não só métodos. |
| `data.sql` com cardápio | Demo já apresenta dados coerentes com o domínio. |

---

## Autor

[Thiago Henrique](https://github.com/teeagohenrique)
