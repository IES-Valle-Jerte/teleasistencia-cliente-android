package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase encargada del modelo de un DispositivoAuxiliar.
 */
public class DispositivoAuxiliar implements Serializable {

    /**
     * Atributos de la clase
     */
    @SerializedName("id")
    private int id;
    @SerializedName("id_terminal")
    private Object terminal;
    @SerializedName("id_tipo_alarma")
    private Object tipoAlarma;

    /**
     * Getters y setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getTerminal() {
        return terminal;
    }

    public void setTerminal(Object terminal) {
        this.terminal = terminal;
    }

    public Object getTipoAlarma() {
        return tipoAlarma;
    }

    public void setTipoAlarma(Object tipoAlarma) {
        this.tipoAlarma = tipoAlarma;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return "DispositivoAuxiliar{" +
                "id=" + id +
                ", terminal=" + terminal +
                ", tipoAlarma=" + tipoAlarma +
                '}';
    }
}
