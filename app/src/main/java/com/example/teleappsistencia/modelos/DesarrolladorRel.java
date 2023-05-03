package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DesarrolladorRel implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("descipcion")
    private String descripcion;
    @SerializedName("imagen")
    private String imagen;
    @SerializedName("es_profesor")
    private boolean es_profesor;
    @SerializedName("id_convocatoria_proyecto")
    private int id_convocatoria_proyecto;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isEs_profesor() {
        return es_profesor;
    }

    public void setEs_profesor(boolean es_profesor) {
        this.es_profesor = es_profesor;
    }

    public int getId_convocatoria_proyecto() {
        return id_convocatoria_proyecto;
    }

    public void setId_convocatoria_proyecto(int id_convocatoria_proyecto) {
        this.id_convocatoria_proyecto = id_convocatoria_proyecto;
    }
}
