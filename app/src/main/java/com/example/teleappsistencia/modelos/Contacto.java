package com.example.teleappsistencia.modelos;

import androidx.annotation.NonNull;

import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contacto implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellidos")
    private String apellidos;

    @SerializedName("telefono")
    private String telefono;
    @SerializedName("tipo_relacion")
    private String tipo_relacion;
    @SerializedName("tiene_llaves_vivienda")
    private boolean tiene_llaves_vivienda;
    @SerializedName("disponibilidad")
    private String disponibilidad;
    @SerializedName("observaciones")
    private String observaciones;
    @SerializedName("prioridad")
    private int prioridad;

    @SerializedName("es_conviviente")
    private boolean es_conviviente;

    @SerializedName("tiempo_domicilio")
    private int tiempo_domicilio;
    @SerializedName("id_paciente")
    private Object id_paciente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo_relacion() {
        return tipo_relacion;
    }

    public void setTipo_relacion(String tipo_relacion) {
        this.tipo_relacion = tipo_relacion;
    }

    public boolean isTiene_llaves_vivienda() {
        return tiene_llaves_vivienda;
    }

    public void setTiene_llaves_vivienda(boolean tiene_llaves_vivienda) {
        this.tiene_llaves_vivienda = tiene_llaves_vivienda;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public Object getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(Object id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEs_conviviente() {
        return es_conviviente;
    }

    public void setEs_conviviente(boolean es_conviviente) {
        this.es_conviviente = es_conviviente;
    }

    public int getTiempo_domicilio() {
        return tiempo_domicilio;
    }

    public void setTiempo_domicilio(int tiempo_domicilio) {
        this.tiempo_domicilio = tiempo_domicilio;
    }

    /**
     * Se retorna el nombre de la persona de contacto para identificarla en los ListView, Spinners, etc
     * @return
     */
}
