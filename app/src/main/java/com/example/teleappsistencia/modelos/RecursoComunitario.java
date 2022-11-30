package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Clase POJO "RecursoComunitario" utilizada para parsear la respuesta JSON del servidor.
 */
public class RecursoComunitario implements Serializable {
        /**
     * Atributos de la clase POJO con sus anotaciones GSON correspondientes,
     * que se utilizan para mapear las JSON keys hacia campos Java.
     */
    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("id_tipos_recurso_comunitario")
    private Object tipoRecursoComunitario;
    @SerializedName("id_direccion")
    private Object direccion;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Object getTipoRecursoComunitario() {
        return tipoRecursoComunitario;
    }

    public void setTipoRecursoComunitario(Object tipoRecursoComunitario) {
        this.tipoRecursoComunitario = tipoRecursoComunitario;
    }

    public Object getDireccion() {
        return direccion;
    }

    public void setDireccion(Object direccion) {
        this.direccion = direccion;
    }

    /**
     * Se devuelve el nombre del Recurso Comunitario para identificarlo en ListViews, Spinners etc.
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return this.nombre;
    }
}
