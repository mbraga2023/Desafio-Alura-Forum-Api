# API do Fórum - README

## Visão Geral
Este aplicativo Java Spring 3 simula um fórum de mensagens onde os usuários podem criar tópicos, responder a tópicos e interagir entre si. Ele utiliza um banco de dados MySQL para armazenar informações do usuário, detalhes do curso, tópicos e respostas.

## Funcionalidades
- **Gerenciamento de Usuários**: Os usuários podem se registrar, fazer login e atualizar seus perfis.
- **Criação de Tópicos**: Os usuários podem criar novos tópicos em cursos específicos.
- **Postagem de Respostas**: Os usuários podem responder a tópicos existentes.
- **Segurança**: Utiliza o Spring Security para autenticação e autorização baseada em token JWT.

## Documentação
- **Swagger UI**: A documentação da API está disponível em [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
- **Coleção do Postman**: Você pode encontrar a coleção do Postman para testar a API no arquivo [Forum API.postman_collection.json](Forum%20API.postman_collection.json).

## Primeiros Passos
Siga estas etapas para configurar e executar o aplicativo localmente:

1. **Clonar o Repositório**: Clone este repositório para a sua máquina local.
   ```
   git clone <url_do_repositório>
   ```

2. **Configuração do Banco de Dados**: Configure um banco de dados MySQL e defina os detalhes de conexão nas propriedades do aplicativo.

3. **Construir e Executar**: Use o Maven para construir o aplicativo e executá-lo localmente.
   ```
   mvn spring-boot:run
   ```

4. **Acessar a API**: Uma vez que o aplicativo estiver em execução, você pode acessar a documentação da API através do Swagger UI em [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

## Dependências
- Java Spring Boot 3
- MySQL
- Spring Security
- Autenticação de Token JWT

## Contribuidores
- Michel Diener Braga
- [michel.diener@gmail.com]

Sinta-se à vontade para contribuir para este projeto enviando solicitações de pull ou relatando problemas.

## Licença
[Licença Apache2](LICENSE)
