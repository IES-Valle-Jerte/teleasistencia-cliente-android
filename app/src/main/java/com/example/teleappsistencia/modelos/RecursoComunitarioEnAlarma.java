package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecursoComunitarioEnAlarma implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("fecha_registro")
    private String fechaRegistro;
    @SerializedName("persona")
    private String persona; //Persona que atiende la llamada
    @SerializedName("acuerdo_alcanzado")
    private String acuerdoAlcanzado;
    @SerializedName("id_alarma")
    private Object idAlarma;
    @SerializedName("id_recurso_comunitario")
    private Object idRecursoComunitairo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getAcuerdoAlcanzado() {
        return acuerdoAlcanzado;
    }

    public void setAcuerdoAlcanzado(String acuerdoAlcanzado) {
        this.acuerdoAlcanzado = acuerdoAlcanzado;
    }

    public Object getIdAlarma() {
        return idAlarma;
    }

    public void setIdAlarma(Object idAlarma) {
        this.idAlarma = idAlarma;
    }

    public Object getIdRecursoComunitairo() {
        return idRecursoComunitairo;
    }

    public void setIdRecursoComunitairo(Object idRecursoComunitairo) {
        this.idRecursoComunitairo = idRecursoComunitairo;
    }
}
