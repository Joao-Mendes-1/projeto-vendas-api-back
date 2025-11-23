# Vendas API

API REST para gerenciamento de vendas e vendedores, construída com **Java 17** e **Spring Boot 3.5.7**, utilizando **Spring Data JPA**, **Bean Validation** e banco de dados **H2** em memória.

---

## Índice

- [Descrição](#descrição)  
- [Tecnologias](#tecnologias)  
- [Estrutura do Projeto](#estrutura-do-projeto)  
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)  
- [Endpoints](#endpoints)  
- [Testes](#testes)  
- [Como Rodar](#como-rodar)  

---

## Descrição

A API permite:  

- Criar, atualizar e consultar **vendas**;  
- Criar, atualizar e consultar **vendedores**;  
- Gerar relatórios de média de vendas por período;  
- Tratamento padronizado de exceções com respostas JSON.

---

## Tecnologias

- **Java 17**  
- **Spring Boot 3.5.7**  
  - Spring Web  
  - Spring Data JPA  
  - Spring Boot Starter Validation  
- **H2 Database** (em memória)  
- **Maven** para gerenciamento de dependências  
- **Lombok** para reduzir boilerplate  
- **JUnit 5** e Spring Boot Test para testes automatizados  

---

## Estrutura do Projeto

├── data
│   ├── vendas-api.mv.db
│   └── vendas-api.trace.db
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── joaoMendes
│   │   │           └── vendas_api
│   │   │               ├── controller
│   │   │               │   ├── VendaController.java
│   │   │               │   └── VendedorController.java
│   │   │               ├── domain
│   │   │               │   ├── entities
│   │   │               │   │   ├── Venda.java
│   │   │               │   │   └── Vendedor.java
│   │   │               │   ├── exception
│   │   │               │   │   ├── PeriodoInvalidoException.java
│   │   │               │   │   ├── VendaNotFoundException.java
│   │   │               │   │   └── VendedorNotFoundException.java
│   │   │               │   ├── repository
│   │   │               │   │   ├── VendaRepository.java
│   │   │               │   │   └── VendedorRepository.java
│   │   │               │   └── service
│   │   │               │       ├── VendaService.java
│   │   │               │       └── VendedorService.java
│   │   │               ├── dto
│   │   │               │   ├── request
│   │   │               │   │   ├── MediaPorPeriodoRequest.java
│   │   │               │   │   ├── VendaRequest.java
│   │   │               │   │   └── VendedorRequest.java
│   │   │               │   └── response
│   │   │               │       ├── MediaPorPeriodoResponse.java
│   │   │               │       ├── VendaResponse.java
│   │   │               │       └── VendedorResponse.java
│   │   │               ├── exceptionhandler
│   │   │               │   ├── ApiErrorResponse.java
│   │   │               │   └── ApiExceptionHandler.java
│   │   │               ├── mapper
│   │   │               │   ├── VendaMapper.java
│   │   │               │   └── VendedorMapper.java
│   │   │               ├── utils
│   │   │               │   └── StringUtils.java
│   │   │               └── VendasApiApplication.java
│   │   └── resources
│   │       ├── application.properties
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── joaoMendes
│                   └── vendas_api
│                       ├── controller
│                       │   ├── VendaControllerTest.java
│                       │   └── VendedorControllerTest.java
│                       ├── domain
│                       │   └── service
│                       │       ├── VendaServiceTest.java
│                       │       └── VendedorServiceTest.java
│                       └── VendasApiApplicationTests.java
└── target
    ├── classes
    │   ├── application.properties
    │   └── com
    │       └── joaoMendes
    │           └── vendas_api
    │               ├── controller
    │               │   ├── VendaController.class
    │               │   └── VendedorController.class
    │               ├── domain
    │               │   ├── entities
    │               │   │   ├── Venda.class
    │               │   │   └── Vendedor.class
    │               │   ├── exception
    │               │   │   ├── PeriodoInvalidoException.class
    │               │   │   ├── VendaNotFoundException.class
    │               │   │   └── VendedorNotFoundException.class
    │               │   ├── repository
    │               │   │   ├── VendaRepository.class
    │               │   │   └── VendedorRepository.class
    │               │   └── service
    │               │       ├── VendaService.class
    │               │       └── VendedorService.class
    │               ├── dto
    │               │   ├── request
    │               │   │   ├── MediaPorPeriodoRequest.class
    │               │   │   ├── VendaRequest.class
    │               │   │   └── VendedorRequest.class
    │               │   └── response
    │               │       ├── MediaPorPeriodoResponse.class
    │               │       ├── VendaResponse.class
    │               │       └── VendedorResponse.class
    │               ├── exceptionhandler
    │               │   ├── ApiErrorResponse.class
    │               │   └── ApiExceptionHandler.class
    │               ├── mapper
    │               │   ├── VendaMapper.class
    │               │   └── VendedorMapper.class
    │               ├── utils
    │               │   └── StringUtils.class
    │               └── VendasApiApplication.class
    ├── generated-sources
    │   └── annotations
    ├── generated-test-sources
    │   └── test-annotations
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
    │       │       ├── createdFiles.lst
    │       │       └── inputFiles.lst
    │       └── testCompile
    │           └── default-testCompile
    │               ├── createdFiles.lst
    │               └── inputFiles.lst
    ├── surefire-reports
    │   ├── com.joaoMendes.vendas_api.VendasApiApplicationTests.txt
    │   └── TEST-com.joaoMendes.vendas_api.VendasApiApplicationTests.xml
    └── test-classes
        └── com
            └── joaoMendes
                └── vendas_api
                    ├── controller
                    │   ├── VendaControllerTest.class
                    │   └── VendedorControllerTest.class
                    ├── domain
                    │   └── service
                    │       ├── VendaServiceTest.class
                    │       └── VendedorServiceTest.class
                    └── VendasApiApplicationTests.class
                    

## Configuração do Banco de Dados

O projeto utiliza **H2 Database** em memória, configurado no `application.properties`:

```properties
spring.application.name=vendas-api
spring.datasource.url=jdbc:h2:file:./data/vendas-api
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=jm
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true




## Endpoints Principais
Vendedor
POST /vendedores — Criar vendedor (VendedorRequest → VendedorResponse 201 Created)

GET /vendedores — Listar todos os vendedores (List<VendedorResponse>)

GET /vendedores/{id} — Buscar vendedor por ID (VendedorResponse)

PUT /vendedores/{id} — Atualizar vendedor (VendedorRequest → VendedorResponse)

DELETE /vendedores/{id} — Remover vendedor (204 No Content)

Venda
POST /vendas — Criar venda (VendaRequest → VendaResponse 201 Created)

GET /vendas — Listar todas as vendas (List<VendaResponse>)

GET /vendas/{id} — Buscar venda por ID (VendaResponse)

GET /vendas/vendedor/{id} — Listar vendas de um vendedor pelo ID (List<VendaResponse>)

PUT /vendas/{id} — Atualizar venda (VendaRequest → VendaResponse)

DELETE /vendas/{id} — Remover venda (204 No Content)

GET /vendas/{idVendedor}/estatistica?dataInicio=dd/MM/yyyy&dataFim=dd/MM/yyyy — Calcular média de vendas de um vendedor em um período (MediaPorPeriodoResponse)
