package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase encargada del modelo de una Imagen.
 */
public class Imagen implements Serializable {

    /**
     * Atributos de la clase
     */
    @SerializedName("imagen")
    private String url;

    public Imagen(String url) {
        this.url = url;
    }

    /**
     * Getters y setters
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
