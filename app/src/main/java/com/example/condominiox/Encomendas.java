package com.example.condominiox;

public class Encomendas {
    String tipo, data, Cep, apto;

    public Encomendas(){}
    public Encomendas(String tipo, String data, String cep, String apto) {
        this.tipo = tipo;
        this.data = data;
        Cep = cep;
        this.apto = apto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCep() {
        return Cep;
    }

    public void setCep(String cep) {
        Cep = cep;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }
}
