package com.example.projeto;

import java.io.Serializable;
import java.util.List;

public class Evento implements Serializable {
    private int id;
    private String nome;
    private String descricao;
    private String tipoEvento; // Novo campo
    private int idOrganizador;
    private String dataInicial;
    private String dataFinal;
    private String horaInicial;
    private String horaFinal;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    private String estado;
    private String cidade;
    private double latitude;
    private double longitude;
    private List<String> imagens;

    // Construtor atualizado
    public Evento(int id, String nome, String descricao, String tipoEvento, int idOrganizador,
                  String dataInicial, String dataFinal, String horaInicial, String horaFinal,
                  String rua, String numero, String complemento, String bairro, String cep, String estado, String cidade,
                  double latitude, double longitude, List<String> imagens) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tipoEvento = tipoEvento; // Atribuir novo campo
        this.idOrganizador = idOrganizador;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagens = imagens;
    }

    public String getEnderecoCompleto() {
        String endereco = rua + ", " + numero;
        if (complemento != null && !complemento.isEmpty()) {
            endereco += " - " + complemento;
        }
        endereco += ", " + "Bairro: " + bairro +  ", Cidade: " + cidade + ", Estado:" + estado + ", CEP: " + cep;
        return endereco;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipoEvento() { return tipoEvento; } // Getter para tipoEvento
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; } // Setter para tipoEvento

    public int getIdOrganizador() { return idOrganizador; }
    public void setIdOrganizador(int idOrganizador) { this.idOrganizador = idOrganizador; }

    public String getDataInicial() { return dataInicial; }
    public void setDataInicial(String dataInicial) { this.dataInicial = dataInicial; }

    public String getDataFinal() { return dataFinal; }
    public void setDataFinal(String dataFinal) { this.dataFinal = dataFinal; }

    public String getHoraInicial() { return horaInicial; }
    public void setHoraInicial(String horaInicial) { this.horaInicial = horaInicial; }

    public String getHoraFinal() { return horaFinal; }
    public void setHoraFinal(String horaFinal) { this.horaFinal = horaFinal; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public List<String> getImagens() { return imagens; }
    public void setImagens(List<String> imagens) { this.imagens = imagens; }
}
