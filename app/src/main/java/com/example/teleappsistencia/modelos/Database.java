package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Database implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("nameDescritive")
    private String nombre;

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
