# Sistema de Biblioteca

Trabalho final da disciplina de Engenharia de Software (UEL) — Sistema de
Biblioteca desenvolvido com praticas ageis (Scrum/Kanban) e DevOps (CI/CD via
GitHub Actions).

## Equipe

| Nome | RA | Responsabilidade |
|---|---|---|
| Luiz G. B. Girotto | 202401220351 | Arquitetura DAO e Banco |
| Antonio J. Santos | 202401220077 | Cadastro e Interface Gráfica |
| Eduardo P. Barbosa | 202401220124 | Empréstimo e Devolução |
| Heitor Oliveira Nunes | 202401220226 | Teste, DevOps e Scrum |

## Arquitetura

O sistema segue uma **arquitetura em camadas (Layered Architecture)** combinada
com o padrao **MVC** na camada de apresentacao e o padrao **DAO** na camada de
persistencia:

```
Controller (Spring MVC + Thymeleaf)
        |
     Service (regras de negocio)
        |
       DAO (interface + implementacao JDBC)
        |
     SQLite (arquivo biblioteca.db)
```

Veja a documentacao completa da arquitetura, os diagramas de classe e de
sequencia em `docs/arquitetura.md` (ou no arquivo `.docx` entregue junto ao
trabalho).

## Tecnologias

- Java 17
- Spring Boot 3 (Web + Thymeleaf)
- SQLite (JDBC puro, padrao DAO implementado manualmente)
- JUnit 5 + Mockito (testes unitarios)
- GitHub Actions (CI)

## Como rodar o projeto

Pre-requisitos: JDK 17 e Maven instalados (ou use o wrapper `./mvnw`, se
adicionado ao repositorio).

```bash
# Compilar e rodar os testes
mvn test

# Subir a aplicacao
mvn spring-boot:run
```

A aplicacao sobe em `http://localhost:8080`. O banco SQLite (`biblioteca.db`)
e criado automaticamente na primeira execucao, na raiz do projeto.

## Funcionalidades

- Cadastro de Livros (Titulos e Exemplares) — `/livros`
- Cadastro de Alunos — `/alunos`
- Emprestar Livro — `/emprestar`
- Devolver Livro — `/devolver`

## Estrutura do projeto

```
src/main/java/com/biblioteca/
  model/        # Entidades de dominio (Aluno, Titulo, Exemplar, Emprestimo, Item)
  dao/          # Interfaces e implementacoes DAO (persistencia JDBC/SQLite)
  service/      # Regras de negocio (EmprestimoService, DevolucaoService, ...)
  controller/   # Controllers Spring MVC (GUI web)
  config/       # Configuracao de banco de dados
src/main/resources/templates/  # Views Thymeleaf (GUI)
src/test/java/com/biblioteca/  # Testes unitarios (JUnit + Mockito)
.github/workflows/ci.yml       # Pipeline de Integracao Continua
```

## Fluxo de trabalho (Scrum + Kanban + GitHub)

- **Product Backlog**: cadastrar livro/aluno, emprestar livro, devolver livro
  (ver Issues do repositorio).
- **Kanban**: quadro no GitHub Projects com colunas `To Do`, `In Progress`,
  `Review`, `Done`.
- **Branches**: uma branch por funcionalidade, ex. `feature/cadastro-livro`,
  `feature/emprestar-livro`, `feature/devolver-livro`.
- **Pull Requests**: toda alteracao e revisada via PR antes do merge em `main`.
  A pipeline de CI (`.github/workflows/ci.yml`) roda automaticamente a cada PR,
  compilando o projeto e executando os testes unitarios.
- **Release**: ao final, uma release e publicada no GitHub descrevendo o que
  foi entregue.
