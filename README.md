## **Trabalho final do curso de desenvolvimento de sistemas web.**

O objetivo desse trabalho é o desenvolvimento de um aplicação Web similar ao trello.

#### **Requisitos:**

1. [X] O sistema deve ter um mecanismo de autenticação, permitindo que usuários criem contas,
    entrem no sistema (login), saiam do sistema (logout), recuperem suas senhas e troquem de
     senha.
2. [X] Um usuário pode ter vários quadros, cada qual com um título, uma cor de fundo e uma cor
    de texto. Deve ser possível criar um quadro, alterar os dados de um quadro, remover um 
    quadro e visualizar um quadro.
3. [X] Um quadro é formado por diversas listas, representadas como colunas. Cada lista deve
    ter um título. Ao visualizar um quadro em que tenha direito de edição, um usuário pode
    adicionar uma lista, trocar a ordem das listas, editar o título de uma lista ou remover uma
    lista.
4. [X] Cada lista de um quadro pode ter vários cartões, cada qual com um conteúdo (um texto
    livre), uma data de criação e a data da última alteração. Ao visualizar um quadro, o usuário
    com direito de edição deve ser capaz de adicionar um cartão em uma lista, eliminar um cartão
    e mover um cartão para outra lista.
5. [X] Quadros podem ser compartilhados com outros usuários. Ao visualizar um quadro, o usuário 
    que o criou pode compartilhar o quadro com outro usuário. Para tanto, entra com o e-mail do
     usuário com quem deseja compartilhar e uma indicação se este usuário poderá editar o 
    quadro ou apenas visualizar o seu conteúdo.
6. [X] Deve ser possível marcar um quadro como favorito. Este quadro será apresentado com
    uma estrela na lista de quadros. É possível que eu marque um quadro que recebi via
    compartilhamento como favorito, ainda que o dono do quadro não o tenha marcado assim.
7. [ ] **EXTRA:** Deve ser possível anexar arquivos PDF a um cartão. Os usuários devem poder
    visualizar os arquivos PDF anexos a um cartão.
8. [X] **EXTRA:** quadros podem ser associados a coleções. Deve ser possível criar uma
    coleção, remover uma coleção, adicionar um quadro a uma coleção e remover um quadro de
    uma coleção.

#### **Tecnologias:**

* Spring
* MongoDB
* Docker
* auth0

#### **SMTP:**

    É preciso adicionar um usuario e senha nas configurações do smtp no arquivo application.yml.


#### **Execução:**

```shell
docker compose up -d && ./mvnw spring-boot:run
```
