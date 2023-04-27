package com.example.teleappsistencia.modelos;

import java.util.List;

public class Alumno {

    private String nombre;
    private List<String> tecnologias;
    private String fotoPerfil;
    private String decripción;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getTecnologias() {
        return tecnologias;
    }

    public void setTecnologias(List<String> tecnologias) {
        this.tecnologias = tecnologias;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getDecripción() {
        return decripción;
    }

    public void setDecripción(String decripción) {
        this.decripción = decripción;
    }
}
