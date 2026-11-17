TechLab Inventory & Service Desk
17 de nov. de 2026 · @chamaa
Visão Geral
O TechLab Inventory & Service Desk é uma aplicação Java para gestão de ativos de TI (servidores, estações de trabalho e dispositivos de rede) e controle de chamados de suporte técnico, com persistência em banco de dados SQLite via JDBC.
O projeto foi desenvolvido incrementalmente ao longo do semestre na disciplina de Programação Orientada a Objetos — Faculdade SENAC Ceará — aplicando herança, interfaces, exceções personalizadas, coleções e o padrão DAO.
Arquitetura e Estrutura de Pacotes
O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:
Techlab/
├── Main.java                          # Ponto de entrada — demo do CRUD completo
│
├── negocio/                           # Camada de domínio
│   ├── AtivoTI.java                   # Classe abstrata base
│   ├── Servidor.java                  # Subclasse: ip, SO, RAM
│   ├── EstacaoTrabalho.java           # Subclasse: departamento, usuário
│   ├── DispositivoRede.java           # Subclasse: número de portas
│   └── ChamadoSuporte.java            # Entidade de chamados (enum Prioridade)
│
├── interfaces/                        # Contratos de software
│   ├── IConexaoBD.java                # obterConexao() + inicializarBanco()
│   ├── IPersistencia.java             # CRUD genérico <T>
│   └── INotificavel.java              # notificar(String)
│
├── db/                                # Camada de infraestrutura
│   └── ConexaoBanco.java              # Implementa IConexaoBD — SQLite
│
├── dao/                               # Camada de acesso a dados (padrão DAO)
│   ├── AtivoDAO.java                  # CRUD de AtivoTI
│   └── ChamadoDAO.java                # CRUD de ChamadoSuporte
│
└── excecoes/                          # Exceções personalizadas
    ├── EquipamentoNaoEncontradoException.java
    └── FalhaConexaoBDException.java
Dependência externa: sqlite-jdbc-3.53.4.0.jar — driver JDBC para SQLite. Deve estar na raiz do projeto junto dos pacotes fonte.
Como Compilar e Rodar
Pré-requisitos
• Java JDK 11 ou superior instalado
• Arquivo sqlite-jdbc-3.53.4.0.jar na raiz do projeto
• Todos os arquivos .java extraídos mantendo a estrutura de pastas
Compilar (Windows CMD)
Abra o terminal na pasta raiz do projeto (onde está o .jar) e execute:
mkdir out
javac -cp sqlite-jdbc-3.53.4.0.jar -d out Main.java negocio\*.java interfaces\*.java db\*.java excecoes\*.java dao\*.java
Rodar
java -cp "out;sqlite-jdbc-3.53.4.0.jar" Main
Nota: No Linux/macOS, substitua ; por : no classpath e \ por / nos caminhos.
O que acontece ao rodar
1.  O banco techlab.db é criado automaticamente na pasta raiz
2.  As tabelas ativos e chamados são inicializadas via DDL
3.  3 ativos são inseridos (Servidor, EstacaoTrabalho, DispositivoRede)
4.  CRUD completo é demonstrado: listar, buscar por ID, atualizar, deletar
5.  2 chamados de suporte são criados e vinculados aos ativos
6.  Filtro por prioridade é executado
7.  Delete com CASCADE é testado (remoção do ativo apaga o chamado vinculado)
Modelagem de Domínio
Hierarquia de Classes
AtivoTI é a classe abstrata base. Todas as subclasses implementam calcularCustoManutencaoMensal().
Classe
Atributos específicos
Custo mensal
Servidor
ipEstatico, sistemaOperacional, capacidadeRAM (GB)
R$ 50 × RAM
EstacaoTrabalho
departamento, usuarioAlocado
R$ 150 fixo
DispositivoRede
numeroPortas
R$ 200 × portas
ChamadoSuporte
Entidade de suporte vinculada a um AtivoTI. Prioridade definida por enum:
Campo
Tipo
id
int
titulo
String
descricao
String
prioridade
enum Prioridade (BAIXA, MEDIA, ALTA)
dataAbertura
LocalDateTime
ativoRelacionado
AtivoTI
Interfaces
Interface
Métodos
Implementada por
IConexaoBD
obterConexao(), inicializarBanco()
ConexaoBanco
IPersistencia<T>
salvar, buscarPorId, listarTodos, atualizar, deletar
AtivoDAO, ChamadoDAO
INotificavel
notificar(String)
— (disponível para implementação futura)
Exceções Personalizadas
• EquipamentoNaoEncontradoException — lançada quando buscarPorId() não encontra o ativo no banco
• FalhaConexaoBDException — RuntimeException lançada em falhas de conexão ou inicialização do banco
Banco de Dados
O banco techlab.db é um arquivo SQLite criado automaticamente na raiz do projeto na primeira execução.
Tabela ativos
Coluna
Tipo
Descrição
id
INTEGER PK AUTOINCREMENT
Identificador único
patrimonio
TEXT NOT NULL UNIQUE
Código de patrimônio
nome
TEXT NOT NULL
Modelo do equipamento
categoria
TEXT NOT NULL
Nome da subclasse Java (Servidor, EstacaoTrabalho, DispositivoRede)
detalhe_especifico
TEXT
Campo polimórfico: IP (Servidor), departamento (Estação), nº portas (Rede)
Tabela chamados
Coluna
Tipo
Descrição
id
INTEGER PK AUTOINCREMENT
Identificador único
titulo
TEXT NOT NULL
Título do chamado
descricao
TEXT NOT NULL
Descrição do problema
prioridade
TEXT NOT NULL
Valor do enum: BAIXA, MEDIA ou ALTA
data_abertura
TEXT NOT NULL
ISO-8601 (LocalDateTime.toString())
ativo_id
INTEGER FK
Referência a ativos.id com ON DELETE CASCADE
A constraint ON DELETE CASCADE garante que ao remover um ativo, todos os chamados vinculados a ele são removidos automaticamente.
Calendário de Entregas
Semana
Período
Conteúdo
Entregável
Status
01
10–14/Ago
Setup & Git
README + repositório
✅
02
17–21/Ago
Classes e Encapsulamento
AtivoTI, ChamadoSuporte
✅
03
24–28/Ago
Ciclo de Vida & Memória
Vinculação de objetos em RAM
✅
04
31/Ago–04/Set
Sobrecarga
Construtores flexíveis
✅
05
07–11/Set
Herança
Servidor, EstacaoTrabalho
✅
06
14–18/Set
Interfaces
IConexaoBD, IPersistencia, INotificavel
✅
07
21–25/Set
Exceções
EquipamentoNaoEncontradoException, FalhaConexaoBDException
✅
08
28/Set–02/Out
Revisão
Bug fixes + push consolidado
✅
09
05–09/Out
Avaliação 1
Release v0.5-alpha
✅
10
12–16/Out
Recesso
—
—
11
19–23/Out
Mapeamento & DTOs
Mapeadores de objetos
✅
12
26–30/Out
Coleções & Genéricos
Listas e filtros em memória
✅
13
02–06/Nov
Persistência em Arquivo
Leitura/gravação local
✅
14
09–13/Nov
Persistência em BD
AtivoDAO + ChamadoDAO funcionais
✅
15
16–20/Nov
Integração BD
CRUD completo e testado
🔄 Em andamento
16
23–27/Nov
Polimento e Testes
Código congelado
⬜
17
30/Nov–04/Dez
Avaliação 2
Release v1.0-final
⬜
