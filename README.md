<div align="center">

# 🍳 Receitas AI API

API RESTful inteligente que une o poder da IA generativa do **Gemini** com o catálogo global do **TheMealDB** para buscar, traduzir e adaptar receitas culinárias automaticamente para o português brasileiro.

<p>
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Cloud_Feign-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring_AI_Gemini-purple?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring_Data_JPA-59666C?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white" />
  <img src="https://img.shields.io/badge/Mockito-78A641?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" />
</p>

</div>

---

<table>
<tr>

<td width="280" align="center">

<img src="https://github.com/wilsonneto-plx.png" width="250" alt="Wilson de Andrade Veloso Neto">

</td>

<td>

## 👨‍💻 Desenvolvedor Backend Java

**Wilson de Andrade Veloso Neto**

🎓 Bacharelando em Ciência da Computação — UESPI

📚 Aprimorando conhecimentos em Java, Spring Boot e Backend pela Alura

☕ Java | Spring Boot | APIs REST | JUnit 5 | Mockito | PostgreSQL | Spring AI

<p>

<a href="https://github.com/wilsonneto-plx">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
</a>

<a href="https://www.linkedin.com/in/wilson-neto-5b1207398/">
<img src="https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white">
</a>

</p>

</td>

</tr>
</table>

---

## 📌 Sobre o Projeto

A **Receitas AI API** resolve a barreira do idioma na busca por receitas internacionais. O usuário não precisa saber inglês nem fazer conversões manuais de medidas (como onças ou libras). A aplicação orquestra um fluxo inteligente e automatizado:

1. O usuário busca por uma receita em **Português (PT-BR)**.
2. A aplicação utiliza o **Gemini AI** para traduzir o termo de busca para o Inglês.
3. Com o termo em inglês, é feita uma consulta à API pública **TheMealDB**.
4. A resposta da receita (ingredientes, categoria e instruções) é enviada novamente ao Gemini, que atua como um "Chef de Cozinha", traduzindo para o português, convertendo medidas imperiais para o sistema métrico e adaptando termos culinários.
5. A receita traduzida é salva no banco de dados local. Se o mesmo prato for buscado no futuro, a API retorna diretamente do banco, garantindo alta performance e economia de chamadas externas (Cache).

### ✨ Principais Funcionalidades

- **Busca e Tradução Inteligente:** Integração bidirecional com IA utilizando prompts contextualizados.
- **Persistência com PostgreSQL (Cache):** Salvamento automático das receitas no banco de dados para evitar chamadas redundantes a APIs externas e garantir alta performance.
- **Tratamento Estruturado de Dados:** Formatação inteligente de ingredientes (separados por `|`) e instruções.
- **CRUD Completo:** Listagem paginada, busca por ID, atualização parcial e exclusão de receitas salvas.
- **Tratamento Global de Exceções:** Retornos padronizados via `@RestControllerAdvice` para erros 400 (validação), 404 (não encontrado) e 500 (erro interno).
- **Segurança e Configuração:** Utilização de variáveis de ambiente para proteção de chaves de API (Gemini) e credenciais do banco de dados.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17+
- **Framework e Módulos:** Spring Boot 3+ (Spring Web, Spring Boot DevTools)
- **Integração de APIs:** Spring Cloud OpenFeign
- **Inteligência Artificial:** Spring AI (Google Gemini)
- **Banco de Dados:** PostgreSQL
- **Persistência:** Spring Data JPA / Hibernate
- **Validação:** Spring Validation (Jakarta Bean Validation)
- **Testes Automatizados:** JUnit 5, Mockito, MockMvc
- **Boilerplate:** Lombok
- **Documentação e Testes de API:** Swagger (Springdoc OpenAPI) e Postman

---

## 🏗️ Arquitetura do Projeto

O projeto foi desenvolvido seguindo uma **arquitetura em camadas**, buscando uma melhor organização das responsabilidades, separação das regras de negócio e facilidade de manutenção.

A aplicação está estruturada nos seguintes pacotes:

```text
📦 src/main/java/com.wilson.api_receitas
│
├── 📂 client
│   ├── 📂 impl
│   │   └── Implementações concretas dos clientes (ex: orquestração e Prompt Engineering com Gemini via Spring AI)
│   └── Contratos e interfaces de integração externa (Spring Cloud OpenFeign para o TheMealDB)
│
├── 📂 config
│   └── Configurações da documentação Swagger/OpenAPI
│
├── 📂 controller
│   └── Responsável pelos endpoints REST e gerenciamento das requisições HTTP
│
├── 📂 dto
│   └── Objetos de transferência de dados entre as camadas da aplicação
│
├── 📂 exception
│   └── Gerenciamento e tratamento global das exceções da aplicação
│
├── 📂 model
│   └── Contém a entidade JPA que representa os dados persistidos
│
├── 📂 repository
│   └── Responsável pela comunicação com o banco de dados através do Spring Data JPA
│
└── 📂 service
    └── Contém as regras de negócio, orquestração de IA e lógica da aplicação

```

Os testes automatizados estão organizados separadamente em:

```text

📦 src/test/java/com.wilson.api_receitas
│
├── 📂 controller
│   └── Testes dos endpoints REST utilizando MockMvc e validação de rotas HTTP
│
└── 📂 service
    └── Testes unitários das regras de negócio utilizando JUnit 5 e Mockito
```
---
## 🔄 Fluxo da Aplicação

A aplicação utiliza uma arquitetura baseada em camadas, onde o serviço principal
atua como orquestrador entre a inteligência artificial, API externa e banco de dados.

```mermaid
flowchart TD

    A[👤 Usuário] --> B[ReceitaController]

    B --> C[ReceitaService]

    C --> D{Receita existe no banco?}

    D -->|Sim| E[Retorna receita do PostgreSQL]

    D -->|Não| F[Gemini AI]

    F --> G[Tradução PT-BR → Inglês]

    G --> H[TheMealDB API]

    H --> I[Dados da receita]

    I --> J[Gemini AI]

    J --> K[Tradução + Conversão de medidas + Adaptação culinária]

    K --> L[Salvar receita no PostgreSQL]

    L --> M[Retornar resposta]

 ```
---

## 🌐 Endpoints da API

O prefixo base da API é `/api/receitas`.

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/buscar?nome={nome}` | Busca, traduz, persiste e retorna uma receita pelo nome em português. |
| `GET` | `/` | Lista todas as receitas salvas no banco (suporta paginação `?page=0&size=10`).  |
| `GET` | `/{id}` | Retorna os detalhes de uma receita salva específica pelo seu ID. |
| `PUT` | `/{id}` | Atualiza o nome traduzido e/ou a categoria de uma receita existente. |
| `DELETE`| `/{id}` | Exclui uma receita do banco de dados pelo seu ID. |

## 🧪 Testes Automatizados

O projeto possui uma suíte de testes robusta que garante a confiabilidade das regras de negócio e das rotas HTTP, sem depender de conexões reais com o banco ou consumo de cotas de APIs externas.

*   **Camada Web (Controllers):** Utiliza `@WebMvcTest` e `MockMvc` para validar os códigos de status HTTP (200, 204, 400, 404), serialização JSON e validações do Bean Validation (`@Valid`).
*   **Camada de Negócio (Services):** Utiliza JUnit 5 e Mockito (`@ExtendWith(MockitoExtension.class)`) para validar a orquestração complexa de dados. Cobre cenários de sucesso, comportamento de cache em banco e tratamento de indisponibilidade de APIs externas.

Para rodar a suíte de testes localmente, execute o comando abaixo no terminal, na raiz do projeto:

```bash
./mvnw test
```
Nota para usuários Windows: Se estiver utilizando o Prompt de Comando (CMD) ou PowerShell, utilize mvnw test ou .\mvnw test.

## 🚀 Como Executar o Projeto

### Pré-requisitos
Antes de começar, você precisará ter instalado na sua máquina:
- Java 17 ou superior.
- **PostgreSQL** (rodando localmente ou em nuvem).
- Chave de API válida do **Google Gemini**.

### Configuração de Ambiente
Por questões de segurança, o projeto utiliza variáveis de ambiente para as credenciais do banco de dados e da inteligência artificial. Antes de rodar a aplicação, configure as seguintes variáveis no seu sistema ou na sua IDE:

- `DB_HOSTPOST`: Host e porta do banco (ex: `localhost:5432`)
- `DB_NAMEPOST`: Nome do banco de dados (ex: `receitas_db`)
- `DB_USERPOST`: Usuário do PostgreSQL (ex: `postgres`)
- `DB_PASSWORDPOST`: Senha do PostgreSQL
- `GEMINI_API_KEY`: Sua chave de API do Gemini

> **Nota:** A aplicação já está configurada nativamente para utilizar a base URL do Gemini (`gemini-3.5-flash`) através do adaptador OpenAI do Spring AI. A URL do TheMealDB também já está configurada por padrão.

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/wilsonneto-plx/api-receitas.git
   ```
2. **Acesse a pasta do projeto:**
   ```bash
   cd api-receitas
   ```
3. **Inicie a aplicação utilizando o Maven Wrapper:**
   ```bash
   ./mvnw spring-boot:run
   ```
4. **A API iniciará na porta 8080 e estará disponível para receber requisições em: http://localhost:8080/api/receitas**   

---

## 📄 Documentação da API (Swagger)

O projeto conta com a documentação automatizada e interativa fornecida pelo **Swagger UI**. 

Após iniciar a aplicação, você pode visualizar todos os endpoints, os schemas de requisição/resposta (DTOs) e testar a API diretamente pelo navegador acessando:

🔗 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

<div align="center">

Desenvolvido por **Wilson de Andrade Veloso Neto**

⭐ Se este projeto foi útil para você, considere deixar uma estrela no repositório!

</div>

