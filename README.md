# Cadastro de Clientes 

Este é um projeto de uma aplicação de desktop para o cadastro, leitura, atualização e remoção (CRUD) de clientes. A aplicação foi desenvolvida em Java utilizando a biblioteca gráfica Swing, com persistência de dados num banco de dados PostgreSQL.

## Tecnologias Utilizadas

* **Java 21:** Versão da linguagem de programação.
* **Java Swing:** Biblioteca para a criação da interface gráfica (GUI).
* **PostgreSQL:** Sistema de gerenciamento de banco de dados relacional.
* **Maven:** Ferramenta para automação de *build* e gerenciamento de dependências.
* **JDBC:** API para a conexão entre a aplicação Java e o banco de dados.

---

## Tutorial: Como Configurar e Rodar o Projeto

Siga estes passos para configurar o ambiente e executar a aplicação.

### 1. Pré-requisitos

Certifique-se de que tem os seguintes programas instalados no seu sistema:

* **JDK 21 (Java Development Kit):** Essencial para compilar e executar o código Java.
* **Apache Maven:** Para gerir as dependências e o processo de *build*.
* **PostgreSQL:** O banco de dados onde as informações serão guardadas.
* **Git:** Para clonar o repositório (opcional, se o código já estiver na sua máquina).

### 2. Configuração do Banco de Dados

A aplicação precisa de uma base de dados, um utilizador e uma tabela para funcionar. Os comandos abaixo são para um ambiente Linux (Ubuntu/Debian).

#### Passo 2.1: Criar o Utilizador do Banco

Primeiro, vamos criar o utilizador que a aplicação usará para se conectar. O código está configurado para usar o utilizador com a senha que escolher.

1.  Abra um terminal e conecte-se ao `psql` como superutilizador:
    ```bash
    sudo -u postgres psql
    ```

2.  Dentro do `psql`, execute o comando para criar o novo utilizador:
    ```sql
    CREATE USER username WITH PASSWORD 'password';
    ```

#### Passo 2.2: Criar a Base de Dados

1.  Ainda no `psql`, crie a base de dados chamada `customers`:
    ```sql
    CREATE DATABASE customers;
    ```

#### Passo 2.3: Criar a Tabela `clientes`

1.  Conecte-se à base de dados que acabou de criar:
    ```sql
    \c customers
    ```

2.  Execute o seguinte comando para criar a tabela `clientes`:
    ```sql
    CREATE TABLE clientes (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(255) NOT NULL,
        telefone VARCHAR(20),
        email VARCHAR(255) UNIQUE NOT NULL,
        cpf VARCHAR(14) UNIQUE NOT NULL,
        senha VARCHAR(255) NOT NULL
    );
    ```

#### Passo 2.4: Conceder Permissões

Finalmente, dê ao utilizador com o `username` escolhido as permissões necessárias para aceder e modificar a base de dados e a tabela.

1.  Execute os seguintes comandos no `psql` (ainda conectado à base de dados `customers`):
    ```sql
    GRANT CONNECT ON DATABASE customers TO username;
    GRANT USAGE ON SCHEMA public TO username;
    GRANT ALL PRIVILEGES ON TABLE clientes TO username;
    GRANT USAGE, SELECT ON SEQUENCE clientes_id_seq TO username;
    ```

2.  Saia do `psql` com o comando `\q`. O seu banco de dados está pronto!

### 3. Executar a Aplicação

Com o banco de dados configurado, agora pode compilar e rodar o projeto.

1.  Abra um terminal na pasta raiz do projeto (onde o ficheiro `pom.xml` está localizado).

2.  Use o Maven para compilar o projeto e empacotar tudo num único ficheiro `.jar`.
    ```bash
    mvn clean package
    ```
    Aguarde até ver a mensagem `BUILD SUCCESS`.

3.  Execute a aplicação a partir do ficheiro `.jar` que foi criado na pasta `target/`:
    ```bash
    java -jar target/cadastro-clientes-1.0.0-jar-with-dependencies.jar
    ```

A janela da aplicação deverá abrir, conectada ao banco de dados e pronta para ser usada.



**OBS.: Para funcionar o código, troque no SQLConnection.java o nome do usuário e a senha escolhidas**

---

## Arquitetura e Decisões de Projeto

O código foi estruturado utilizando padrões de projeto bem conhecidos para garantir que seja organizado, fácil de manter e escalável.

### 1. Padrão MVC (Model-View-Controller)

O projeto segue a arquitetura MVC para separar as responsabilidades:

* **Model:** Representa os dados e a lógica de negócio. É a "cabeça" da aplicação.
    * `Cliente.java`: Uma classe "POJO" (Plain Old Java Object) que representa a entidade cliente, com os seus atributos (ID, nome, CPF, etc.).
    * `SQLConnection.java`: Responsável por uma única tarefa: estabelecer a conexão com o banco de dados. Centralizar a conexão aqui facilita a sua manutenção.
    * `VerificaCPF.java`: Uma classe de utilidade com a lógica de validação de CPF, mantendo esta responsabilidade fora do modelo `Cliente`.

* **View:** É a camada de apresentação, a interface gráfica com a qual o utilizador interage.
    * `MainWindow.java`: Responsável por construir e exibir todos os componentes visuais (janela, botões, tabela). É intencionalmente "burra", não contendo nenhuma lógica de negócio. Ela apenas mostra informações e captura as ações do utilizador.

* **Controller:** Atua como o intermediário, conectando o Model e a View.
    * `ClienteController.java`: Ouve as ações do utilizador na `View` (ex: clique num botão), processa essas ações utilizando o `Model` e o `DAO`, e depois atualiza a `View` com os novos dados.

### 2. Padrão DAO (Data Access Object)

Para separar a lógica de negócio da lógica de acesso aos dados, foi utilizado o padrão DAO.

* **`ClienteDAO.java` (Interface):** Define um "contrato" com todos os métodos que são necessários para manipular os dados dos clientes (criar, ler, atualizar, remover, etc.). Usar uma interface desacopla o resto da aplicação da implementação específica do banco de dados.

* **`ClientePostgresDAO.java` (Implementação Concreta):** Esta classe implementa a interface `ClienteDAO` e contém o código SQL específico para o PostgreSQL. Se no futuro quiséssemos migrar para outro banco de dados (como MySQL ou SQL Server), apenas precisaríamos de criar uma nova classe que implemente `ClienteDAO`, sem precisar de alterar o Controller ou a View.

### 3. Gerenciamento de Dependências com Maven

* **`pom.xml`:** O projeto utiliza o Maven para gerir as suas dependências. A única dependência externa é o driver JDBC do PostgreSQL. O Maven descarrega-o automaticamente, evitando a necessidade de adicionar ficheiros `.jar` manualmente ao projeto. O `maven-assembly-plugin` também é usado para empacotar a aplicação e todas as suas dependências num único ficheiro `.jar` executável, o que simplifica muito a distribuição.

