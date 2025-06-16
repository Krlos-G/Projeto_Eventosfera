# Projeto Eventosfera

Bem-vindo ao **Projeto Eventosfera**! Este repositório é dedicado ao desenvolvimento de uma aplicação para gestão de eventos, proporcionando aos usuários uma experiência completa desde o cadastro até o acompanhamento das atividades relacionadas a diferentes tipos de eventos.

## 📚 Sumário

- [Descrição](#descrição)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Como Rodar o Projeto](#como-rodar-o-projeto)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Contribuição](#contribuição)
- [Contato](#contato)

---

## Descrição

O **Eventosfera** é uma plataforma desenvolvida com o objetivo de facilitar a organização, divulgação e gerenciamento de eventos. Com ela, organizadores podem criar eventos, controlar inscrições, gerenciar participantes e disponibilizar informações relevantes de maneira intuitiva.

## Funcionalidades

- Cadastro e autenticação de usuários (organizadores e participantes)
- Criação, edição e exclusão de eventos
- Listagem e detalhamento de eventos disponíveis
- Controle de participantes por evento
- Painel administrativo para organizadores
- Notificações e atualizações sobre eventos

## Tecnologias Utilizadas

- **Backend:** Spring Boot, Hibernate, Java
- **Frontend:** Android Studio (Java), XML, Retrofit, Google Maps SDK
- **Banco de Dados:** MySQL
- **Outras:** Docker pra facil conexão com banco.

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Git](https://git-scm.com/)
- [Node.js](https://nodejs.org/) e NPM (caso utilize Node)
- [Docker](https://www.docker.com/) (opcional)
- Outras dependências específicas do projeto

## Como Rodar o Projeto

```bash
# Clone este repositório
git clone https://github.com/Krlos-G/Projeto_Eventosfera.git

# Acesse a pasta do projeto
cd Projeto_Eventosfera

# Instale as dependências
npm install

# Configure as variáveis de ambiente
cp .env.example .env

# Inicie a aplicação
npm start
```

## Estrutura do Projeto

```
Projeto_Eventosfera/
│
├── backend/             # Código fonte do backend
├── frontend/            # Código fonte do frontend
├── docs/                # Documentação
├── scripts/             # Scripts auxiliares
├── .env.example         # Exemplo de variáveis de ambiente
├── package.json         # Dependências e scripts (Node)
└── README.md            # Documentação principal
```

## Contribuição

Contribuições são sempre bem-vindas! Para contribuir, siga os passos abaixo:

1. Faça um fork do projeto.
2. Crie uma branch para sua feature ou correção (`git checkout -b minha-feature`).
3. Commit suas alterações (`git commit -m 'feat: minha nova feature'`).
4. Faça push para a branch (`git push origin minha-feature`).
5. Abra um Pull Request.

## Contato

Desenvolvido por [Krlos-G](https://github.com/Krlos-G)  
Dúvidas, sugestões ou feedback? Abra uma issue ou entre em contato!