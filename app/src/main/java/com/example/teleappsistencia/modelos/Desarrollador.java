package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Desarrollador implements Serializable {
    @SerializedName("desarrollador_tecnologias")
    private List<Object> lDesarrollador_tecnologia;
    @SerializedName("es_profesor")
    private boolean es_profesor;
    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("imagen")
    private String imagen;
    @SerializedName("nombre")
    private String nombre;

    public List<Object> getlDesarrollador_tecnologia() {
        return lDesarrollador_tecnologia;
    }

    public void setlDesarrollador_tecnologia(List<Object> lDesarrollador_tecnologia) {
        this.lDesarrollador_tecnologia = lDesarrollador_tecnologia;
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

}
