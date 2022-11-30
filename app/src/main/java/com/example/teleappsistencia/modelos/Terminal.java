package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Clase POJO "Terminal" utilizada para parsear la respuesta JSON del servidor.
 */
public class Terminal implements Serializable {

    /**
     * Atributos de la clase POJO con sus anotaciones GSON correspondientes,
     * que se utilizan para mapear las JSON keys hacia campos Java.
     */
    @SerializedName("id")
    private int id;
    @SerializedName("numero_terminal")
    private String numeroTerminal;
    @SerializedName("modo_acceso_vivienda")
    private String modoAccesoVivienda;
    @SerializedName("barreras_arquitectonicas")
    private String barrerasArquitectonicas;
    @SerializedName("id_titular")
    private Object titular;
    @SerializedName("id_tipo_vivienda")
    private Object tipoVivienda;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroTerminal() {
        return numeroTerminal;
    }

    public void setNumeroTerminal(String numeroTerminal) {
        this.numeroTerminal = numeroTerminal;
    }

    public String getModoAccesoVivienda() {
        return modoAccesoVivienda;
    }

    public void setModoAccesoVivienda(String modoAccesoVivienda) {
        this.modoAccesoVivienda = modoAccesoVivienda;
    }

    public String getBarrerasArquitectonicas() {
        return barrerasArquitectonicas;
    }

    public void setBarrerasArquitectonicas(String barrerasArquitectonicas) {
        this.barrerasArquitectonicas = barrerasArquitectonicas;
    }

    public Object getTitular() {
        return titular;
    }

    public void setTitular(Object titular) {
        this.titular = titular;
    }

    public Object getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(Object tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return getNumeroTerminal();
    }
}
