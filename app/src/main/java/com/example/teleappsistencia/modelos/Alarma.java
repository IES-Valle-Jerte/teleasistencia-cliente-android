package com.example.teleappsistencia.modelos;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

public class Alarma implements Serializable, Comparable<Alarma>{

    @SerializedName("id")
    private int id;
    @SerializedName("estado_alarma")
    private String estado_alarma;
    @SerializedName("fecha_registro")
    private Date fecha_registro;
    @SerializedName("observaciones")
    private String observaciones;
    @SerializedName("resumen")
    private String resumen;
    @SerializedName("id_tipo_alarma")
    private Object id_tipo_alarma;
    @SerializedName("id_teleoperador")
    private Object id_teleoperador;
    @SerializedName("id_paciente_ucr")
    private Object id_paciente_ucr;
    @SerializedName("id_terminal")
    private Object id_terminal;

    //Getters y Setters a continucación

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado_alarma() {
        return estado_alarma;
    }

    public void setEstado_alarma(String estado_alarma) {
        this.estado_alarma = estado_alarma;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Object getId_tipo_alarma() {
        return id_tipo_alarma;
    }

    public void setId_tipo_alarma(Object id_tipo_alarma) {
        this.id_tipo_alarma = id_tipo_alarma;
    }

    public Object getId_teleoperador() {
        return id_teleoperador;
    }

    public void setId_teleoperador(Object id_teleoperador) {
        this.id_teleoperador = id_teleoperador;
    }

    public Object getId_paciente_ucr() {
        return id_paciente_ucr;
    }

    public void setId_paciente_ucr(Object id_paciente_ucr) {
        this.id_paciente_ucr = id_paciente_ucr;
    }

    public Object getId_terminal() {
        return id_terminal;
    }

    public void setId_terminal(Object id_terminal) {
        this.id_terminal = id_terminal;
    }

    /**
     * Este método compara la alarma con otra alarma por su estado y en caso de que el estado sea el mismo lo
     * compara por su fecha y hora de registro
     * @param otraAlarma
     * @return
     */
    @Override
    public int compareTo(Alarma otraAlarma) {
        int resultado = this.getEstado_alarma().compareTo(otraAlarma.getEstado_alarma());
        if (resultado == 0) {
            resultado = this.getFecha_registro().compareTo(otraAlarma.getFecha_registro());
        }
        return resultado;
    }
}
