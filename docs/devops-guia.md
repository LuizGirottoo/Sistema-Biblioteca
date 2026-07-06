# Guia de Execucao — DevOps, Scrum e Kanban no GitHub

Este guia e um passo a passo pratico para a equipe cumprir os requisitos de
DevOps/Agil do trabalho (nao e codigo, e o que voces fazem no GitHub).

## 1. Configuracao inicial

1. Criar um repositorio **publico** no GitHub (ex: `biblioteca-sistema`).
2. Fazer upload de todo o conteudo desta pasta (`biblioteca-sistema/`) para a
   raiz do repositorio.
3. Adicionar todos os membros da equipe como colaboradores
   (Settings → Collaborators).
4. Confirmar que o `README.md` esta na raiz e preencher a tabela de equipe.

## 2. Product Backlog (Scrum)

O backlog do produto ja esta definido pelo enunciado:

1. Cadastrar Livros e Alunos
2. Emprestar Livro
3. Devolver Livro
4. Testes (unitarios + automatizados)
5. DevOps (Kanban, Issues, branches/PRs, GitHub Actions, Release)

Sugestao de Sprints (ajuste as datas ao prazo real da disciplina):

| Sprint | Escopo |
|---|---|
| Sprint 1 | Arquitetura, cadastro de livros e alunos |
| Sprint 2 | Caso de Uso Emprestar Livro (diagramas + implementacao) |
| Sprint 3 | Caso de Uso Devolver Livro (diagramas + implementacao) |
| Sprint 4 | Testes, GitHub Actions, Release final |

## 3. Kanban (GitHub Projects)

1. No repositorio, abrir a aba **Projects** → **New project** → template
   **Board**.
2. Criar as colunas: `To Do`, `In Progress`, `Review`, `Done`.
3. Vincular esse Project ao repositorio (ja acontece automaticamente se
   criado a partir do repo).

## 4. Issues

Para cada item do backlog, criar uma Issue. Sugestao de Issues iniciais:

- `feature: cadastro de titulos e exemplares`
- `feature: cadastro de alunos`
- `feature: diagrama de classe - emprestar livro`
- `feature: diagrama de sequencia - emprestar livro`
- `feature: implementacao - emprestar livro`
- `feature: diagrama de caso de uso - devolver livro`
- `feature: diagrama de classe - devolver livro`
- `feature: diagrama de sequencia - devolver livro`
- `feature: implementacao - devolver livro`
- `test: casos de teste emprestar/devolver`
- `test: testes unitarios automatizados`
- `devops: configurar github actions (CI)`
- `devops: criar release final`

Para cada Issue:
- Atribuir uma **label** (`feature`, `bug`, `test`, `review`, `devops`).
- Atribuir um **responsavel** (membro da equipe).
- Vincular a Issue ao **Project** (Kanban) criado no passo 3.
- Mover o cartao entre as colunas conforme o trabalho avanca.
- Usar os **comentarios da Issue** para registrar decisoes tomadas pela
  equipe (ex: "decidimos usar SQLite pois...").

## 5. Branches e Commits

- Nunca commitar direto na `main`. Criar uma branch por funcionalidade:
  - `feature/cadastro-livro`
  - `feature/cadastro-aluno`
  - `feature/emprestar-livro`
  - `feature/devolver-livro`
  - `feature/testes`
  - `devops/github-actions`
- Commits pequenos e descritivos, no padrao:
  `feat: implementa cadastro de titulo`, `fix: corrige calculo de prazo`,
  `test: adiciona teste de emprestimo em debito`.

## 6. Pull Requests e Code Review

1. Ao terminar uma funcionalidade na branch, abrir um **Pull Request** para
   a `main`.
2. Pelo menos **2 revisões de codigo (Code Review)** devem ser feitas entre
   os membros da equipe ao longo do projeto — use a aba "Files changed" do
   PR no GitHub e o botao "Review changes" (Comment / Request changes /
   Approve).
3. So fazer o merge apos aprovacao E apos o workflow de CI passar (ver
   secao 7).

## 7. GitHub Actions (CI)

O arquivo `.github/workflows/ci.yml` (ja incluido neste projeto) roda
automaticamente a cada Pull Request e a cada push na `main`:
- Compila o projeto com Maven.
- Executa os testes unitarios.
- Publica o relatorio de testes como artefato do workflow.

Nao e necessario nenhum passo extra alem de dar `push` do arquivo para o
repositorio — o GitHub detecta o workflow automaticamente. Confirme, na aba
**Actions** do repositorio, que o workflow rodou com sucesso apos abrir um
Pull Request de teste.

## 8. Release

Ao final do projeto:

1. Ir em **Releases** → **Draft a new release**.
2. Criar uma tag (ex: `v1.0.0`).
3. Descrever no corpo da release o que foi entregue: cadastro de
   livros/alunos, emprestimo, devolucao, testes, CI/CD.
4. Confirmar que todas as Issues relacionadas estao com status `Done` no
   Kanban antes de publicar a release.
