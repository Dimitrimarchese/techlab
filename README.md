# TechLab Inventory & Service Desk

Aplicação Java para gestão de ativos de TI e controle de chamados de suporte técnico, com persistência em banco de dados SQLite via JDBC.

Desenvolvida incrementalmente na disciplina de **Programação Orientada a Objetos** — Faculdade SENAC Ceará — aplicando herança, interfaces, exceções personalizadas, coleções e o padrão DAO.

---

## Estrutura do Projeto

```
Techlab/
├── Main.java                                   # Ponto de entrada — demo do CRUD completo
│
├── negocio/                                    # Camada de domínio
│   ├── AtivoTI.java                            # Classe abstrata base
│   ├── Servidor.java                           # Subclasse: ip, SO, RAM
│   ├── EstacaoTrabalho.java                    # Subclasse: departamento, usuário
│   ├── DispositivoRede.java                    # Subclasse: número de portas
│   └── ChamadoSuporte.java                     # Entidade de chamados (enum Prioridade)
│
├── interfaces/                                 # Contratos de software
│   ├── IConexaoBD.java                         # obterConexao() + inicializarBanco()
│   ├── IPersistencia.java                      # CRUD genérico <T>
│   └── INotificavel.java                       # notificar(String)
│
├── db/                                         # Camada de infraestrutura
│   └── ConexaoBanco.java                       # Implementa IConexaoBD — SQLite
│
├── dao/                                        # Padrão DAO
│   ├── AtivoDAO.java                           # CRUD de AtivoTI
│   └── ChamadoDAO.java                         # CRUD de ChamadoSuporte
│
└── excecoes/                                   # Exceções personalizadas
    ├── EquipamentoNaoEncontradoException.java
    └── FalhaConexaoBDException.java
```

> **Dependência:** `sqlite-jdbc-3.53.4.0.jar` — deve estar na raiz do projeto junto dos pacotes fonte.

---

## Como Compilar e Rodar

### Pré-requisitos

- Java JDK 11 ou superior
- Arquivo `sqlite-jdbc-3.53.4.0.jar` na raiz do projeto

### Compilar (Windows CMD)

```bat
mkdir out
javac -cp sqlite-jdbc-3.53.4.0.jar -d out Main.java negocio\*.java interfaces\*.java db\*.java excecoes\*.java dao\*.java
```

### Rodar

```bat
java -cp "out;sqlite-jdbc-3.53.4.0.jar" Main
```

> Linux/macOS: substitua `;` por `:` e `\` por `/`.

### O que acontece ao rodar

1. O banco `techlab.db` é criado automaticamente na pasta raiz
2. As tabelas `ativos` e `chamados` são inicializadas via DDL
3. 3 ativos são inseridos (Servidor, EstacaoTrabalho, DispositivoRede)
4. CRUD completo é demonstrado: listar, buscar por ID, atualizar, deletar
5. 2 chamados são criados e vinculados aos ativos
6. Filtro por prioridade é executado
7. Delete com CASCADE é testado

---

## Modelagem de Domínio

### Hierarquia de Classes

`AtivoTI` é a classe abstrata base. Todas as subclasses implementam `calcularCustoManutencaoMensal()`.

| Classe | Atributos específicos | Custo mensal |
| --- | --- | --- |
| `Servidor` | `ipEstatico`, `sistemaOperacional`, `capacidadeRAM` (GB) | R$ 50 × RAM |
| `EstacaoTrabalho` | `departamento`, `usuarioAlocado` | R$ 150 fixo |
| `DispositivoRede` | `numeroPortas` | R$ 200 × portas |

### ChamadoSuporte

| Campo | Tipo |
| --- | --- |
| `id` | int |
| `titulo` | String |
| `descricao` | String |
| `prioridade` | enum `Prioridade` (BAIXA, MEDIA, ALTA) |
| `dataAbertura` | LocalDateTime |
| `ativoRelacionado` | AtivoTI |

### Interfaces

| Interface | Métodos | Implementada por |
| --- | --- | --- |
| `IConexaoBD` | `obterConexao()`, `inicializarBanco()` | `ConexaoBanco` |
| `IPersistencia<T>` | `salvar`, `buscarPorId`, `listarTodos`, `atualizar`, `deletar` | `AtivoDAO`, `ChamadoDAO` |
| `INotificavel` | `notificar(String)` | disponível para implementação futura |

---

## Banco de Dados

Arquivo `techlab.db` criado automaticamente na primeira execução.

### Tabela `ativos`

| Coluna | Tipo | Descrição |
| --- | --- | --- |
| `id` | INTEGER PK AUTOINCREMENT | Identificador único |
| `patrimonio` | TEXT NOT NULL UNIQUE | Código de patrimônio |
| `nome` | TEXT NOT NULL | Modelo do equipamento |
| `categoria` | TEXT NOT NULL | Subclasse Java (`Servidor`, `EstacaoTrabalho`, `DispositivoRede`) |
| `detalhe_especifico` | TEXT | Campo polimórfico: IP / departamento / nº portas |

### Tabela `chamados`

| Coluna | Tipo | Descrição |
| --- | --- | --- |
| `id` | INTEGER PK AUTOINCREMENT | Identificador único |
| `titulo` | TEXT NOT NULL | Título do chamado |
| `descricao` | TEXT NOT NULL | Descrição do problema |
| `prioridade` | TEXT NOT NULL | `BAIXA`, `MEDIA` ou `ALTA` |
| `data_abertura` | TEXT NOT NULL | ISO-8601 (LocalDateTime.toString()) |
| `ativo_id` | INTEGER FK | Referência a `ativos.id` com `ON DELETE CASCADE` |

---

## Calendário de Entregas

| Semana | Período | Conteúdo | Status |
| --- | --- | --- | --- |
| 01 | 10–14/Ago | Setup & Git | ✅ |
| 02 | 17–21/Ago | Classes e Encapsulamento | ✅ |
| 03 | 24–28/Ago | Ciclo de Vida & Memória | ✅ |
| 04 | 31/Ago–04/Set | Sobrecarga de Construtores | ✅ |
| 05 | 07–11/Set | Herança & Especialização | ✅ |
| 06 | 14–18/Set | Abstração & Interfaces | ✅ |
| 07 | 21–25/Set | Tratamento de Exceções | ✅ |
| 08 | 28/Set–02/Out | Revisão Geral | ✅ |
| 09 | 05–09/Out | **Avaliação 1** — `v0.5-alpha` | ✅ |
| 10 | 12–16/Out | Recesso | — |
| 11 | 19–23/Out | Mapeamento & DTOs | ✅ |
| 12 | 26–30/Out | Coleções & Genéricos | ✅ |
| 13 | 02–06/Nov | Persistência em Arquivo | ✅ |
| 14 | 09–13/Nov | Persistência em BD — DAO | ✅ |
| 15 | 16–20/Nov | Integração BD + Interface | 🔄 |
| 16 | 23–27/Nov | Polimento e Testes | ⬜ |
| 17 | 30/Nov–04/Dez | **Avaliação 2** — `v1.0-final` | ⬜ |

---

*Disciplina: Programação Orientada a Objetos — Faculdade SENAC Ceará — 2026.2*
