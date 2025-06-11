# 🎬 ScreenMatch - Backend API


## 📋 Sobre o Projeto

ScreenMatch é uma API REST completa desenvolvida como backend para um sistema de gerenciamento de séries de TV. Este projeto faz parte da formação Avançando com Java da Alura, onde desenvolvemos uma aplicação completa com frontend e backend.

### 🔗 Repositórios Relacionados
- **Backend (Este repositório)**: [ScreenMatch Backend](https://github.com/JorgeFilipi/screenmatchComJPA)
- **Frontend**: [ScreenMatch Frontend](https://github.com/JorgeFilipi/ScreenMatchFrontEndWeb)

### 🎯 Objetivos do Projeto

- Desenvolver uma API REST completa para gerenciamento de séries
- Implementar uma arquitetura em camadas (Controller, Service, Repository)
- Criar endpoints RESTful para todas as operações CRUD
- Integrar com banco de dados PostgreSQL usando JPA
- Implementar validações e tratamento de erros
- Consumir a API do ChatGPT para enriquecimento de dados
- Implementar consultas complexas e otimizadas ao banco de dados

## 🏗️ Arquitetura do Projeto

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/br/com/alura/screenmatch/
├── controller/     # Endpoints REST da API
├── service/        # Lógica de negócio
├── repository/     # Acesso ao banco de dados
├── model/          # Entidades JPA
│   ├── Serie.java
│   ├── Episodio.java
│   └── Categoria.java
└── dto/            # Objetos de transferência de dados
```

### 🔄 Fluxo de Dados
1. Requisições HTTP chegam aos Controllers
2. Controllers delegam a lógica para os Services
3. Services utilizam Repositories para persistência
4. Dados são mapeados entre DTOs e Entidades

## 🛠️ Stack Tecnológica

- **Backend Framework**: Spring Boot 3.x
- **Linguagem**: Java 17
- **Persistência**: 
  - Spring Data JPA
  - PostgreSQL
- **Gerenciamento de Dependências**: Maven
- **Ferramentas de Desenvolvimento**:
  - Lombok
  - Spring DevTools
- **APIs Externas**:
  - ChatGPT API
  - TMDB API (para dados de séries)

## 📦 Principais Funcionalidades

### 1. Gestão de Séries
- CRUD completo de séries
- Busca por título, categoria ou avaliação
- Paginação e ordenação de resultados
- Filtros avançados

### 2. Gestão de Episódios
- Cadastro de episódios por temporada
- Avaliação individual de episódios
- Busca por temporada e número do episódio

### 3. Sistema de Avaliação
- Avaliação de séries (0-10)
- Avaliação de episódios
- Cálculo de média de avaliações

### 4. Recursos Avançados
- Tradução automática de sinopses via ChatGPT
- Categorização automática de séries
- Recomendações baseadas em avaliações
- Relatórios e estatísticas

## 🚀 Como Executar

1. **Pré-requisitos**:
   - Java 17 ou superior
   - PostgreSQL
   - Maven
   - IDE (recomendado: IntelliJ IDEA ou Eclipse)

2. **Configuração do Banco de Dados**:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/screenmatch
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

3. **Configuração da API do ChatGPT**:
   - Adicione sua chave API no `application.properties`:
   ```properties
   chatgpt.api.key=sua_chave_api
   ```

4. **Executando o Projeto**:
   ```bash
   # Clone o repositório
   git clone https://github.com/JorgeFilipi/screenmatchComJPA.git
   
   # Entre no diretório
   cd screenmatchComJPA
   
   # Execute o projeto
   mvn spring-boot:run
   ```

## 📚 Documentação da API

A API está documentada usando Swagger/OpenAPI. Após iniciar o projeto, acesse:
```
http://localhost:8080/swagger-ui.html
```

Para uma experiência completa, execute também o frontend da aplicação seguindo as instruções no repositório [ScreenMatch Frontend](https://github.com/JorgeFilipi/ScreenMatchFrontEndWeb).

## 🧪 Testes

O projeto inclui testes unitários e de integração:
```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn verify
```

## 📈 Próximos Passos

- [ ] Implementar autenticação JWT
- [ ] Adicionar cache com Redis
- [ ] Implementar rate limiting
- [ ] Adicionar documentação mais detalhada
- [ ] Implementar testes de carga

## 🤝 Contribuindo

1. Faça um Fork do projeto
2. Crie uma Branch para sua Feature (`git checkout -b feature/AmazingFeature`)
3. Faça o Commit das suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Faça o Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## ✒️ Autor

* **Jorge Filipi** - *Desenvolvimento* - [JorgeFilipi](https://github.com/JorgeFilipi)

## 📚 Formação

Este projeto foi desenvolvido como parte da formação "Avançando com Java" da Alura, onde aprendemos:
- Desenvolvimento de APIs REST com Spring Boot
- Persistência de dados com JPA
- Integração entre frontend e backend
- Boas práticas de desenvolvimento
- Arquitetura de software em camadas

---
⌨️ com ❤️ por [JorgeFilipi](https://github.com/JorgeFilipi) 😊
