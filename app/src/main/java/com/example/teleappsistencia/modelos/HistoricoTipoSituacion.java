package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase encargada del modelo de un HistoricoTipoSituacion.
 */
public class HistoricoTipoSituacion implements Serializable {

    /**
     * Atributos de la clase
     */
    @SerializedName("id")
    private int id;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("id_tipo_situacion")
    private Object idTipoSituacion;
    @SerializedName("id_terminal")
    private Object terminal;

    /**
     * Getters y setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Object getIdTipoSituacion() {
        return idTipoSituacion;
    }

    public void setIdTipoSituacion(Object idTipoSituacion) {
        this.idTipoSituacion = idTipoSituacion;
    }

    public Object getTerminal() {
        return terminal;
    }

    public void setTerminal(Object terminal) {
        this.terminal = terminal;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return "HistoricoTipoSituacion{" +
                "id=" + id +
                ", fecha='" + fecha + '\'' +
                ", idTipoSituacion=" + idTipoSituacion +
                ", terminal=" + terminal +
                '}';
    }
}
