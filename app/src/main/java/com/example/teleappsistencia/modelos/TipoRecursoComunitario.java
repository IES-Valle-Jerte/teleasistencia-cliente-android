package com.example.teleappsistencia.modelos;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TipoRecursoComunitario implements Serializable {

    // Declaración de atributos.
    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombreTipoRecursoComunitario;

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param nombreTipoRecursoComunitario
     */
    public TipoRecursoComunitario(String nombreTipoRecursoComunitario) {
        this.nombreTipoRecursoComunitario = nombreTipoRecursoComunitario;
    }

    // Establecemos y obtenemos los atributos de la clase con sus getters y setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreTipoRecursoComunitario() {
        return nombreTipoRecursoComunitario;
    }

    public void setNombreTipoRecursoComunitario(String nombreTipoRecursoComunitario) {
        this.nombreTipoRecursoComunitario = nombreTipoRecursoComunitario;
    }

    /**
     * Se devuelve el nombre del tipo de recurso comunitario para identificarlo en ListView, Spinners, etc
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return getNombreTipoRecursoComunitario();
    }
}