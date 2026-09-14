# bingusdb

Sistema Acadêmico — aplicação desktop em **Java Swing** para gerenciamento de alunos, notas e boletins.

## Funcionalidades

- **Cadastro de alunos**: registro com RA, nome, data de nascimento e RG, com validação dos dados e verificação de duplicidade.
- **Lançamento de notas**: entrada de nota por aluno, disciplina e tipo de prova, impedindo registros duplicados.
- **Consulta de nota**: pesquisa de uma nota específica de um aluno em uma disciplina e tipo de prova.
- **Boletim**: geração do boletim completo de um aluno, com notas organizadas por disciplina e tipo de prova.

## Requisitos

- **Java JDK 21 ou superior** (o `pom.xml` define compilação com `source`/`target` = 26; JDKs 21+ com suporte a recursos recentes da linguagem, como `records`, funcionam).
- **Maven** para compilação e gerenciamento de dependências.
- **MariaDB 11** (ou servidor MariaDB compatível) — o repositório inclui um `docker-compose.yaml` para subir o banco via Docker.
- **Docker / Docker Compose** (opcional, apenas se for usar o banco via container).

## Dependências

| Dependência | Versão | Finalidade |
|---|---|---|
| `org.mariadb.jdbc:mariadb-java-client` | 3.5.10 | Driver JDBC para conexão com o MariaDB |
| `io.github.cdimascio:dotenv-java` | 3.2.0 | Leitura das variáveis de ambiente do arquivo `.env` |

## Configuração

### 1. Subir o banco de dados

Com Docker:

```bash
docker compose up -d
```

Isso inicia um contêiner **MariaDB 11** com as seguintes configurações:

- Banco de dados: `bingus_db`
- Usuário: `root`
- Senha: `root2`
- Porta: `3306`
- Volume nomeado `mariadb_data` para persistência dos dados

> Alternativamente, aponte para uma instância MariaDB já existente, desde que acessível via JDBC.

### 2. Configurar as variáveis de ambiente

Renomeie o arquivo `.env.example` para `.env` e preencha as variáveis (o arquivo `.env` é ignorado pelo Git):

```bash
DATABASE_HOST=localhost
DATABASE_PORT=3306
DATABASE_NAME=bingus_db
DATABASE_USER=root
DATABASE_PASSWORD=root2
```

### 3. Criar o esquema do banco

Use as instruções contidas na pasta 'sql' na raiz do projeto para criar e povoar o banco de dados.

```

## Compilar e executar

Compile o projeto:

```bash
mvn compile
```

### Pelo IDE

Abra o projeto no IntelliJ IDEA (ou outro IDE com suporte a Maven) e execute a classe `Application`.

### Pela linha de comando

Copie as dependências e execute com o classpath montado:

```bash
mvn dependency:copy-dependencies -DoutputDirectory=target/lib
java -cp "target/classes;target/lib/*" Application
```

> Em ambiente Unix/macOS substitua os separadores do classpath: `target/classes:target/lib/*`. O arquivo `.env` deve estar no diretório de trabalho atual ao executar.

## Executar sem Maven (Eclipse / IntelliJ / linha de comando)

Se você não pode (ou não quer) usar Maven, o projeto funciona como um projeto Java puro: você só precisa compilar o código-fonte e ter os JARs de dependência no classpath.

### 1. Baixar os JARs das dependências manualmente

As dependências (2 JARs) podem ser baixadas diretamente do Maven Central:

| Biblioteca | URL para download |
|---|---|
| `mariadb-java-client-3.5.10.jar` | `https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.5.10/mariadb-java-client-3.5.10.jar` |
| `dotenv-java-3.2.0.jar` | `https://repo1.maven.org/maven2/io/github/cdimascio/dotenv-java/3.2.0/dotenv-java-3.2.0.jar` |

Exemplo com `curl` (salvando em uma pasta `lib`):

```bash
mkdir -p lib
curl -o lib/mariadb-java-client-3.5.10.jar https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.5.10/mariadb-java-client-3.5.10.jar
curl -o lib/dotenv-java-3.2.0.jar https://repo1.maven.org/maven2/io/github/cdimascio/dotenv-java/3.2.0/dotenv-java-3.2.0.jar
```

Em Windows PowerShell:

```powershell
New-Item -ItemType Directory -Path lib -Force
Invoke-WebRequest -OutFile lib\mariadb-java-client-3.5.10.jar https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.5.10/mariadb-java-client-3.5.10.jar
Invoke-WebRequest -OutFile lib\dotenv-java-3.2.0.jar https://repo1.maven.org/maven2/io/github/cdimascio/dotenv-java/3.2.0/dotenv-java-3.2.0.jar
```

> **Transitivas**: o driver MariaDB pode usar **SLF4J** se estiver presente, mas ele não é obrigatório para rodar a aplicação; o `dotenv-java` 3.x não tem dependências obrigatórias. Em geral, apenas esses 2 arquivos são necessários.

### 2. Adicionar os JARs no Eclipse

1. Abra o Eclipse (IDE for Java Developers) com **JDK 21+** configurado.
2. Crie um **projeto Java comum** (File → New → Java Project) e indique o diretório `src/main/java` do projeto como *source folder* (ou arraste os arquivos `.java` para dentro do novo projeto).
3. Clique com o botão direito no projeto → **Build Path → Configure Build Path → Libraries → Add External JARs...** e selecione os 2 JARs baixados na pasta `lib`.
4. Coloque o arquivo `.env` na raiz do projeto/aba do classpath de execução.
5. Execute a classe `Application` com **Run As → Java Application**.

### 3. Adicionar os JARs no IntelliJ IDEA

1. Abra o IntelliJ IDEA e faça **File → New → Project from Existing Sources...**, selecionando a raiz do repositório e criando um projeto Java puro (sem Maven/Gradle).
2. Vá em **File → Project Structure → Modules → Dependencies → `+` → JARs or directories...** e adicione os 2 JARs da pasta `lib`.
3. Em **Project Structure → Project**, confirme que o SDK é **JDK 21+** e defina o *language level* compatível.
4. Garanta que o arquivo `.env` esteja no diretório de trabalho da configuração de execução (Run Configuration → Working directory).
5. Execute a classe `Application`.

### 4. Compilar e executar pela linha de comando, sem Maven

Compile todos os fontes com `javac` apontando para a pasta `lib`:

```bash
# Windows (PowerShell)
javac -encoding UTF-8 -cp "lib/*" -d out (Get-ChildItem -Recurse src\main\java -Filter *.java).FullName

# Unix/macOS (bash)
mkdir -p out && javac -encoding UTF-8 -cp "lib/*" -d out $(find src/main/java -name "*.java")
```

Depois rode a aplicação:

```bash
java -cp "out;lib/*" Application   # Windows
java -cp "out:lib/*" Application   # Unix/macOS
```

> O arquivo `.env` precisa estar no diretório de trabalho atual ao executar o `java`.

## Estrutura do projeto

```
bingusdb/
├── docker-compose.yaml          # Configuração do MariaDB via Docker
├── pom.xml                      # Build Maven e dependências
├── .env.example                 # Modelo de configuração (.env é ignorado)
└── src/main/java/
    ├── Application.java         # Ponto de entrada da aplicação
    ├── database/
    │   └── DatabaseConnection.java  # Conexão JDBC via variáveis do .env
    ├── entities/                # Modelos: Aluno, Notas, Disciplina, TipoProva
    ├── repository/              # Camada de acesso a dados (JDBC)
    └── ui/                      # Interface Swing: MainFrame, AlunoPanel,
                                 # NotaPanel, ConsultaPanel, BoletimFrame
```

## Como funciona

- **`Application`** inicializa a interface na *Event Dispatch Thread* (EDT) e abre a janela principal `MainFrame`, que organiza as funcionalidades em abas.
- **`DatabaseConnection`** lê as variáveis `DATABASE_*` do arquivo `.env` e monta a URL `jdbc:mariadb://host:porta/banco`.
- Os **repositórios** encapsulam as instruções SQL (INSERT, consultas com JOIN etc.) e retornam DTOs (`ConsultaNotaResultado`, `RegistroBoletim`) quando necessário.
- A camada de **UI** valida a entrada (ex.: RA no formato `A12345-6`, data de nascimento com `dd/MM/uuuu` em modo estrito, nota entre 0 e 10) e exibe mensagens de erro amigáveis via diálogos.