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
    private Object id;
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
    @SerializedName("id_paciente")
    private Object idPaciente;
    @SerializedName("id_persona")
    private Object idPersona;

    //Getters y setters
    
    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
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

    public Object getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Object idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Object getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Object idPersona) {
        this.idPersona = idPersona;
    }

}
