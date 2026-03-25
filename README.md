# 📦 Order Microservice

Este projeto é um microserviço responsável por processar e gerenciar pedidos através de mensageria (RabbitMQ) e persistir os dados em MongoDB. A API fornece endpoints para consultar pedidos por cliente e calcular o gasto total.

Desenvolvido como desafio técnico para o BTG Pactual.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.4**
- **Spring Data MongoDB**
- **Spring AMQP (RabbitMQ)**
- **MongoDB**
- **RabbitMQ**
- **Maven**

## 📋 Pré-requisitos

- Java 21+
- Docker e Docker Compose
- Maven 3.8+

## ▶️ Como Executar

### 1. Clone o repositório:
```bash
git clone https://github.com/perondi09/desafio-btgpactual.git
cd desafio-btgpactual/orderms
```

### 2. Inicie os containers (MongoDB e RabbitMQ):
```bash
cd infra
docker-compose up -d
```

### 3. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## 📍 Endpoints da Aplicação

### Listar Pedidos por Cliente

```http
GET /cliente/{codigoCliente}/pedidos
```

**Parâmetros de Path:**

| Parâmetro       | Tipo    | Descrição                          |
| :-------------- | :------ | :--------------------------------- |
| `codigoCliente` | `Long`  | **Obrigatório.** ID do cliente     |

**Parâmetros de Query:**

| Parâmetro | Tipo      | Descrição                                  |
| :-------- | :-------- | :----------------------------------------- |
| `pagina`  | `Integer` | **Não obrigatório.** Página (padrão: 0)   |
| `tamanho` | `Integer` | **Não obrigatório.** Itens por página (padrão: 10) |

**Exemplo de Requisição:**
```bash
curl -X GET "http://localhost:8080/cliente/1/pedidos?pagina=0&tamanho=10"
```

**Exemplo de Resposta (200):**
```json
{
  "soma": {
    "totalGasto": 210.00
  },
  "data": [
    {
      "codigoPedido": 1001,
      "codigoCliente": 1,
      "valorPedido": 210.00
    },
    {
      "codigoPedido": 1002,
      "codigoCliente": 1,
      "valorPedido": 350.50
    }
  ],
  "paginacaoResponse": {
    "pagina": 0,
    "tamanho": 10,
    "elementosTotais": 2,
    "paginasTotais": 1
  }
}
```

---

## 🔌 Listener RabbitMQ

### Receber Pedidos

O serviço consome mensagens da fila `btgpactual-pedidos-feitos` com o seguinte formato:

**Exemplo de Mensagem JSON:**
```json
{
  "codigoPedido": 1001,
  "codigoClient": 1,
  "itens": [
    {
      "produto": "Notebook",
      "quantidade": 1,
      "precoProduto": 3000.00
    },
    {
      "produto": "Mouse",
      "quantidade": 2,
      "preco": 50.00
    }
  ]
}
```

**Nota:** O campo `precoProduto` aceita também o alias `preco` para compatibilidade.

---

## 🗄️ Banco de Dados

### MongoDB

O MongoDB é usado para persistir os pedidos. A estrutura da coleção:

```javascript
{
  "_id": 1001,
  "codigoCliente": 1,
  "valorPedido": 3100.00,
  "itens": [
    {
      "produto": "Notebook",
      "quantidade": 1,
      "precoProduto": 3000.00
    },
    {
      "produto": "Mouse",
      "quantidade": 2,
      "precoProduto": 50.00
    }
  ]
}
```

**Credenciais Padrão:**
- Usuário: `admin`
- Senha: `adm123`

---

## 🐇 Fila de Mensagens

### RabbitMQ

O RabbitMQ é utilizado para consumir eventos de pedidos.

**Interface de Gerenciamento:**
- URL: `http://localhost:15672`
- Usuário: `guest`
- Senha: `guest`

**Fila:** `btgpactual-pedidos-feitos`

---

## 🏗️ Arquitetura do Projeto

```
orderms/
├── src/
│   ├── main/
│   │   ├── java/perondi/orderms/
│   │   │   ├── config/          # Configurações (RabbitMQ, Jackson)
│   │   │   ├── controller/      # Endpoints REST
│   │   │   ├── dto/             # Objetos de transferência de dados
│   │   │   ├── listener/        # Listeners de fila RabbitMQ
│   │   │   ├── models/          # Modelos de dados (MongoDB)
│   │   │   ├── repository/      # Interfaces de acesso a dados
│   │   │   ├── service/         # Lógica de negócio
│   │   │   └── OrdermsApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── infra/
│   ├── docker-compose.yml       # Configuração de containers
│   └── data/                    # Dados do MongoDB (gitignored)
├── pom.xml                      # Dependências Maven
└── README.md
```

---

## 📦 Dependências Principais

- **Spring Boot Starter AMQP:** Para integração com RabbitMQ
- **Spring Data MongoDB:** Para acesso ao MongoDB
- **Lombok:** Para reduzir boilerplate de código
- **Jackson:** Para serialização/desserialização JSON

---

## 🔧 Variáveis de Ambiente

Você pode configurar as seguintes variáveis no `application.properties`:

```properties
spring.data.mongodb.uri=mongodb://admin:adm123@localhost:27017
spring.data.mongodb.database=orderms
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

## 📝 Logs

A aplicação gera logs ao processar pedidos:

```
INFO: Recebendo pedido: PedidoEvent[codigoPedido=1001, codigoCliente=1, itens=[...]]
INFO: Pedido salvo com sucesso: 1001
```

---

## 🚨 Tratamento de Erros

### Conversão de Mensagem Falha

Se a desserialização do JSON falhar, a mensagem é rejeitada:

```
WARN: Failed to convert Message content
java.lang.NullPointerException: Cannot invoke "java.math.BigDecimal.multiply()" because precoProduto is null
```

**Solução:** Verifique se o JSON contém os campos obrigatórios e com os tipos corretos.

---

## 📊 Estatísticas

### Total Gasto por Cliente

O endpoint retorna uma agregação do MongoDB que calcula o total gasto por cliente:

```json
{
  "soma": {
    "totalGasto": 560.50
  }
}
```

---

## 👨‍💻 Autor

Desenvolvido por **Guilherme Perondi** — [LinkedIn](https://www.linkedin.com/in/guilherme-perondi/)