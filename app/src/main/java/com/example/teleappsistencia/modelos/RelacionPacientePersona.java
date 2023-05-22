package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Clase POJO "RelacionPacientePersona" utilizada para parsear la respuesta JSON del servidor.
 */
public class RelacionPacientePersona implements Serializable {

    /**
     * Atributos de la clase POJO con sus anotaciones GSON correspondientes,
     * que se utilizan para mapear las JSON keys hacia campos Java.
     */
    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellidos")
    private String apellidos;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("tipo_relacion")
    private String tipoRelacion;
    @SerializedName("tiene_llaves_vivienda")
    private boolean tieneLlavesVivienda;
    @SerializedName("disponibilidad")
    private String disponibilidad;
    @SerializedName("observaciones")
    private String observaciones;
    @SerializedName("prioridad")
    private int prioridad;
    @SerializedName("es_conviviente")
    private boolean esConviviente;
    @SerializedName("tiempo_domicilio")
    private int tiempoDomicilio;
    @SerializedName("id_paciente")
    private int idPaciente;


    //Getters y setters


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

    public String getTipoRelacion() {
        return tipoRelacion;
    }

    public void setTipoRelacion(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    public boolean isTieneLlavesVivienda() {
        return tieneLlavesVivienda;
    }

    public void setTieneLlavesVivienda(boolean tieneLlavesVivienda) {
        this.tieneLlavesVivienda = tieneLlavesVivienda;
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

    public boolean isEsConviviente() {
        return esConviviente;
    }

    public void setEsConviviente(boolean esConviviente) {
        this.esConviviente = esConviviente;
    }

    public int getTiempoDomicilio() {
        return tiempoDomicilio;
    }

    public void setTiempoDomicilio(int tiempoDomicilio) {
        this.tiempoDomicilio = tiempoDomicilio;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
}
