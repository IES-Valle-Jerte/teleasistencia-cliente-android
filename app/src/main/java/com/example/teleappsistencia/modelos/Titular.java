package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

/**
 * Clase encargada del modelo de un Titular.
 */
public class Titular {

    /**
     * Atributos de la clase
     */
    @SerializedName("id")
    private int id;
    @SerializedName("tiene_ucr")
    private boolean tieneUcr;
    @SerializedName("numero_expediente")
    private String numeroExpediente;
    @SerializedName("numero_seguridad_social")
    private String numeroSeguridadSocial;
    @SerializedName("prestacion_otros_servicios_sociales")
    private String prestacionOtrosServiciosSociales;
    @SerializedName("observaciones_medicas")
    private String observacionesMedicas;
    @SerializedName("intereses_y_actividades")
    private String interesesYActividades;
    @SerializedName("id_terminal")
    private Object terminal;
    @SerializedName("id_persona")
    private Object persona;
    @SerializedName("id_tipo_modalidad_paciente")
    private Object tipoModalidadPaciente;

    /**
     * Getters y setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTieneUcr() {
        return tieneUcr;
    }

    public void setTieneUcr(boolean tieneUcr) {
        this.tieneUcr = tieneUcr;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getNumeroSeguridadSocial() {
        return numeroSeguridadSocial;
    }

    public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
        this.numeroSeguridadSocial = numeroSeguridadSocial;
    }

    public String getPrestacionOtrosServiciosSociales() {
        return prestacionOtrosServiciosSociales;
    }

    public void setPrestacionOtrosServiciosSociales(String prestacionOtrosServiciosSociales) {
        this.prestacionOtrosServiciosSociales = prestacionOtrosServiciosSociales;
    }

    public String getObservacionesMedicas() {
        return observacionesMedicas;
    }

    public void setObservacionesMedicas(String observacionesMedicas) {
        this.observacionesMedicas = observacionesMedicas;
    }

    public String getInteresesYActividades() {
        return interesesYActividades;
    }

    public void setInteresesYActividades(String interesesYActividades) {
        this.interesesYActividades = interesesYActividades;
    }

    public Object getTerminal() {
        return terminal;
    }

    public void setTerminal(Object terminal) {
        this.terminal = terminal;
    }

    public Object getPersona() {
        return persona;
    }

    public void setPersona(Object persona) {
        this.persona = persona;
    }

    public Object getTipoModalidadPaciente() {
        return tipoModalidadPaciente;
    }

    public void setTipoModalidadPaciente(Object tipoModalidadPaciente) {
        this.tipoModalidadPaciente = tipoModalidadPaciente;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return "Titular{" +
                "id=" + id +
                ", tieneUcr=" + tieneUcr +
                ", numeroExpediente='" + numeroExpediente + '\'' +
                ", numeroSeguridadSocial='" + numeroSeguridadSocial + '\'' +
                ", prestacionOtrosServiciosSociales='" + prestacionOtrosServiciosSociales + '\'' +
                ", observacionesMedicas='" + observacionesMedicas + '\'' +
                ", interesesYActividades='" + interesesYActividades + '\'' +
                ", terminal=" + terminal +
                ", persona=" + persona +
                ", tipoModalidadPaciente=" + tipoModalidadPaciente +
                '}';
    }
}
