package com.example.teleappsistencia.modelos;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TipoCentroSanitario implements Serializable {

    // Declaración de atributos.
    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombreTipoCentroSanitario;

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param nombreTipoCentroSanitario
     */
    public TipoCentroSanitario(String nombreTipoCentroSanitario) {
        this.nombreTipoCentroSanitario = nombreTipoCentroSanitario;
    }

    // Establecemos y obtenemos los atributos de la clase con sus getters y setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreTipoCentroSanitario() {
        return nombreTipoCentroSanitario;
    }

    public void setNombreTipoCentroSanitario(String nombreTipoCentroSanitario) {
        this.nombreTipoCentroSanitario = nombreTipoCentroSanitario;
    }

    /**
     * Se devuelve el nombre del tipo de centro sanitario para identificarlo en ListView, Spinners, etc
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return getNombreTipoCentroSanitario();
    }
}