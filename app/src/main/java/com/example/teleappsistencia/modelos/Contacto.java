package com.example.teleappsistencia.modelos;

import androidx.annotation.NonNull;

import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contacto implements Serializable {

    @SerializedName("id")
    private int idContacto;
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
    @SerializedName("id_paciente")
    private Object id_paciente;
    @SerializedName("id_persona")
    private Object personaEnContacto;

    public int getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(int idContacto) {
        this.idContacto = idContacto;
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

    public Object getPersonaEnContacto() {
        return personaEnContacto;
    }

    public void setPersonaEnContacto(Object personaEnContacto) {
        this.personaEnContacto = personaEnContacto;
    }

    /**
     * Se retorna el nombre de la persona de contacto para identificarla en los ListView, Spinners, etc
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        Persona persona = (Persona) Utilidad.getObjeto(this.getPersonaEnContacto(), Constantes.PERSONA);
        String nombreContacto = persona.getNombre() + Constantes.ESPACIO +persona.getApellidos();
        return nombreContacto;
    }
}
