package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase encargada del modelo de un Grupo.
 */
public class Grupo implements Serializable {

    /**
     * Atributos de la clase
     */
    @SerializedName("pk")
    private int pk;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    /**
     * Getters y setters
     */
    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
}