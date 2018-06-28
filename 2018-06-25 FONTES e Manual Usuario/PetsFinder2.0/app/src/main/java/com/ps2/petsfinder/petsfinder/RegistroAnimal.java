package com.ps2.petsfinder.petsfinder;

import java.io.Serializable;

public class RegistroAnimal implements Serializable {

    private String id;
    private String animal;
    private String nome;
    private String raca;
    private CorAnimal cor;
    private String coleira;
    private String dono;
    private String telefone;
    private String latitude;
    private String longitude;
    private String usuario;
    private String isYou;
    private String telaMostraRegistro;

    public RegistroAnimal() {
    }

    public String getTelaMostraRegistro() {
        return telaMostraRegistro;
    }

    public void setTelaMostraRegistro(String telaMostraRegistro) {
        this.telaMostraRegistro = telaMostraRegistro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsYou() {
        return isYou;
    }

    public void setIsYou(String isYou) {
        this.isYou = isYou;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public CorAnimal getCor() {
        return cor;
    }

    public void setCor(CorAnimal cor) {
        this.cor = cor;
    }

    public String getColeira() {
        return coleira;
    }

    public void setColeira(String coleira) {
        this.coleira = coleira;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
