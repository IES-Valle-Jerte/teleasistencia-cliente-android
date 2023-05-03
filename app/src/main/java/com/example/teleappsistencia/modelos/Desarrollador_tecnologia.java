package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Desarrollador_tecnologia implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("id_desarrollador")
    private Object id_desarrollador;

    @SerializedName("id_tecnologia")
    private Object id_tecnologia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getId_desarrollador() {
        return id_desarrollador;
    }

    public void setId_desarrollador(Object id_desarrollador) {
        this.id_desarrollador = id_desarrollador;
    }

    public Object getId_tecnologia() {
        return id_tecnologia;
    }

    public void setId_tecnologia(Object id_tecnologia) {
        this.id_tecnologia = id_tecnologia;
    }
}
