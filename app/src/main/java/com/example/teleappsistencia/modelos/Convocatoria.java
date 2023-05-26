package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Convocatoria implements Serializable {

    @SerializedName("id")
    private int id_convocatoria;

    @SerializedName("desarrolladores")
    private List<Object> lDesarrolladores;

    @SerializedName("convocatoria")
    private String convocatoria;

    @SerializedName("fecha")
    private String fecha;

    public int getId_convocatoria() {
        return id_convocatoria;
    }

    public void setId_convocatoria(int id_convocatoria) {
        this.id_convocatoria = id_convocatoria;
    }

    public String getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(String convocatoria) {
        this.convocatoria = convocatoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Object> getlDesarrolladores() {
        return lDesarrolladores;
    }

    public void setlDesarrolladores(List<Object> lDesarrolladores) {
        this.lDesarrolladores = lDesarrolladores;
    }
}
