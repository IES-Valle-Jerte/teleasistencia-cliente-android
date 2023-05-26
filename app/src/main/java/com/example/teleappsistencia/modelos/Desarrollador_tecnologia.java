package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Desarrollador_tecnologia implements Serializable {

    @SerializedName("id_tecnologia")
    private Object id_tecnologia;

    public Object getId_tecnologia() {
        return id_tecnologia;
    }

    public void setId_tecnologia(Object id_tecnologia) {
        this.id_tecnologia = id_tecnologia;
    }
}
