package com.example.teleappsistencia.modelos;

import com.example.teleappsistencia.utilidades.Constantes;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClasificacionAlarma implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("codigo")
    private String codigo;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Este método retorna un String que lo identifique. Normalmente usado para Spinners.
     * @return
     */
    @Override
    public String toString() {
        return getId() + Constantes.ESPACIO_GUION_ESPACIO + getNombre() + Constantes.ESPACIO_PARENTESIS_AP + getCodigo() + Constantes.PARENTESIS_CIERRE;
    }
}
