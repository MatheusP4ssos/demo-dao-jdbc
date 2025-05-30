# Sistema de Gerenciamento de Vendedores e Departamentos (JDBC)

## 📝 Descrição
Sistema desenvolvido em Java para gerenciamento de vendedores e departamentos, utilizando JDBC para persistência de dados. O projeto implementa um CRUD (Create, Read, Update, Delete) completo para ambas as entidades, com tratamento de integridade referencial e validações de dados.

## 🛠 Tecnologias Utilizadas

* Java (JDK 20)
* MySQL
* JDBC (Java Database Connectivity)
* Padrão DAO (Data Access Object)
* Git para controle de versão

## 🏗 Arquitetura do Projeto

### Padrões de Projeto Utilizados
* DAO (Data Access Object)
* Factory Method
* Dependency Injection

### Diagrama de Classes
```
# Sistema de Gerenciamento de Vendedores e Departamentos (JDBC)

![diagrama-de-classe.png](assets/diagrama-de-classe.png)

## 🔧 Funcionalidades

### Departamentos
- [x] Criar novo departamento
- [x] Buscar departamento por ID
- [x] Listar todos os departamentos
- [x] Atualizar departamento
- [x] Deletar departamento
- [x] Validação de departamentos duplicados

### Vendedores
- [x] Criar novo vendedor
- [x] Buscar vendedor por ID
- [x] Buscar vendedor por email
- [x] Listar todos os vendedores
- [x] Listar vendedores por departamento
- [x] Atualizar vendedor
- [x] Deletar vendedor
- [x] Validação de emails duplicados

## 📦 Estrutura do Banco de Dados

```sql
CREATE TABLE department (
    Id int(11) NOT NULL AUTO_INCREMENT,
    Name varchar(60) DEFAULT NULL,
    PRIMARY KEY (Id)
);

CREATE TABLE seller (
    Id int(11) NOT NULL AUTO_INCREMENT,
    Name varchar(60) NOT NULL,
    Email varchar(100) NOT NULL,
    BirthDate datetime NOT NULL,
    BaseSalary double NOT NULL,
    DepartmentId int(11) NOT NULL,
    PRIMARY KEY (Id),
    FOREIGN KEY (DepartmentId) REFERENCES department (Id)
);
```
```
## 🚀 Como Executar
1. Clone o repositório
``` bash
git clone [url-do-repositorio]
```
1. Configure o banco de dados MySQL

- Crie um banco de dados
- Execute os scripts SQL fornecidos
- Configure o arquivo com suas credenciais: `db.properties`
``` properties
user=seu_usuario
password=sua_senha
dburl=jdbc:mysql://localhost:3306/nome_do_banco
useSSL=false
```
1. Importe o projeto em sua IDE
2. Execute a classe ou para testar as funcionalidades `Program.java``Program2.java`

## 🧪 Testes Disponíveis
### Testes para Departamentos () `Program2.java`
- Listar todos os departamentos
- Buscar departamento por ID
- Inserir novo departamento
- Atualizar departamento
- Deletar departamento

### Testes para Vendedores () `Program.java`
- Buscar vendedor por ID
- Buscar vendedores por departamento
- Listar todos os vendedores
- Inserir novo vendedor
- Atualizar salário
- Deletar vendedor

## 🔒 Tratamento de Exceções
- : Exceções gerais de banco de dados `DbException`
- : Violações de integridade referencial `DbIntegrityException`
- Validações de dados duplicados
- Tratamento de recursos (try-with-resources)

## 👥 Contribuições
Contribuições são bem-vindas! Sinta-se à vontade para sugerir melhorias ou reportar problemas.