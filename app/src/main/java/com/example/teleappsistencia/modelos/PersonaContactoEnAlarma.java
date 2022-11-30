package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class PersonaContactoEnAlarma implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("fecha_registro")
    private String fechaRegistro;
    @SerializedName("acuerdo_alcanzado")
    private String acuerdoAlcanzado;
    @SerializedName("id_alarma")
    private Object idAlarma;
    @SerializedName("id_persona_contacto")
    private Object idPersonaContacto;

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

    public Object getIdPersonaContacto() {
        return idPersonaContacto;
    }

    public void setIdPersonaContacto(Object idPersonaContacto) {
        this.idPersonaContacto = idPersonaContacto;
    }
}
