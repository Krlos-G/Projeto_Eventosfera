package com.example.projeto;

public class Imagem {
    private int id;
    private int idEvento;
    private String url;

    public Imagem(int id, int idEvento, String url) {
        this.id = id;
        this.idEvento = idEvento;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

