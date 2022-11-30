package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Clase POJO "TipoModalidadPaciente" utilizada para parsear la respuesta JSON del servidor.
 */
public class TipoModalidadPaciente implements Serializable {

    /**
     * Atributos de la clase POJO con sus anotaciones GSON correspondientes,
     * que se utilizan para mapear las JSON keys hacia campos Java.
     */

    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
