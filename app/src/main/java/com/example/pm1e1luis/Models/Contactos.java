package com.example.pm1e1luis.Models;

public class Contactos {

    private Integer id;
    private String nombres;
    private String pais;
    private Integer telefono;
    private String notas;

    public Contactos(Integer id, String nombres, String pais, Integer telefono, String notas) {
        this.id = id;
        this.nombres = nombres;
        this.pais = pais;
        this.telefono = telefono;
        this.notas = notas;
    }

    public Contactos(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
