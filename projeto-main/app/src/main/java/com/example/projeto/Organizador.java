package com.example.projeto;

public class Organizador {
    private int id;
    private String nome;
    private String cpfOuCnpj;
    private String email;
    private String senha;
    private String telefone;

    public Organizador(int id, String nome, String cpfOuCnpj, String email, String senha, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpfOuCnpj = cpfOuCnpj;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

