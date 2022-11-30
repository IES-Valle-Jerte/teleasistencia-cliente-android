package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase POJO "Direccion" utilizada para parsear la respuesta JSON del servidor.
 */
public class Direccion implements Serializable {

    /**
    * Atributos de la clase POJO con sus anotaciones GSON correspondientes,
    * que se utilizan para mapear las JSON keys hacia campos Java.
    */

    @SerializedName("id")
    private int id;
    @SerializedName("localidad")
    private String localidad;
    @SerializedName("provincia")
    private String provincia;
    @SerializedName("direccion")
    private String direccion;
    @SerializedName("codigo_postal")
    private String codigoPostal;

    //Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return "Direccion{" +
                "id=" + id +
                ", localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", direccion='" + direccion + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                '}';
    }
}
