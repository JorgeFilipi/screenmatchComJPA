# 🎬 ScreenMatch com JPA

![Programação-Formação Java](https://github.com/iasminaraujoc/3355-java-screenmatch-com-jpa/assets/84939115/3c51e000-962d-4dc9-97fc-1d384e2511a2)

Projeto desenvolvido durante o curso de "Java: persistência de dados e consultas com Spring Data JPA" da formação Avançando com Java da Alura.

## 📋 Sobre o Projeto

O ScreenMatch é um sistema de gerenciamento de séries de TV que permite armazenar e gerenciar informações detalhadas sobre séries, incluindo seus episódios, categorias e avaliações. O projeto foi desenvolvido utilizando Spring Boot e JPA para persistência de dados.

## 🎯 Objetivos do Projeto

- Evoluir no projeto Screenmatch, iniciado no primeiro curso da formação, criando um menu com várias opções
- Modelar as abstrações da aplicação através de classes, enums, atributos e métodos
- Consumir a API do ChatGPT
- Utilizar o Spring Data JPA para persistir dados no banco
- Conhecer vários tipos de banco de dados e utilizar o PostgreSQL
- Trabalhar com vários tipos de consultas ao banco de dados
- Aprofundar na interface JPARepository

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- ChatGPT API

## 📦 Estrutura do Projeto

```
src/main/java/br/com/alura/screenmatch/
├── model/
│   ├── Serie.java
│   ├── Episodio.java
│   └── Categoria.java
├── repository/
├── service/
└── controller/
```

## 🚀 Como Executar

1. Clone o repositório:
```bash
git clone https://github.com/JorgeFilipi/screenmatchComJPA.git
```

2. Configure o banco de dados PostgreSQL:
   - Crie um banco de dados
   - Configure as credenciais no arquivo `application.properties`

3. Execute o projeto:
```bash
mvn spring-boot:run
```

## 📝 Funcionalidades

- Cadastro e gerenciamento de séries
- Gerenciamento de episódios por série
- Categorização de séries
- Sistema de avaliação para séries e episódios
- Tradução automática de sinopses
- Consultas personalizadas ao banco de dados

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

## 📚 Aprendizados

Este projeto foi fundamental para o aprendizado de:
- Persistência de dados com JPA
- Consultas complexas ao banco de dados
- Integração com APIs externas
- Boas práticas de programação em Java
- Arquitetura de software com Spring Boot

---
⌨️ com ❤️ por [JorgeFilipi](https://github.com/JorgeFilipi) 😊
