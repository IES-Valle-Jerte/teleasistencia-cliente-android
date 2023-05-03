package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConvocatoriaDesarrollador implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("convocatoria")
    private String convocatoria_fecha;
    @SerializedName("fecha")
    private String fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConvocatoria_fecha() {
        return convocatoria_fecha;
    }

    public void setConvocatoria_fecha(String convocatoria_fecha) {
        this.convocatoria_fecha = convocatoria_fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
