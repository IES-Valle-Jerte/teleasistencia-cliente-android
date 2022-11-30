package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.annotation.NonNull;

import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Clase POJO "RelacionTerminalRecursoComunitario" utilizada para parsear la respuesta JSON del servidor.
 */
public class RelacionTerminalRecursoComunitario implements Serializable {
        /**
     * Atributos de la clase POJO con sus anotaciones GSON correspondientes,
     * que se utilizan para mapear las JSON keys hacia campos Java.
     */
    @SerializedName("id")
    private int id;
    @SerializedName("id_terminal")
    private Object idTerminal;
    @SerializedName("id_recurso_comunitario")
    private Object idRecursoComunitario;

    // Getters y Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(Object idTerminal) {
        this.idTerminal = idTerminal;
    }

    public Object getIdRecursoComunitario() {
        return idRecursoComunitario;
    }

    public void setIdRecursoComunitario(Object idRecursoComunitario) {
        this.idRecursoComunitario = idRecursoComunitario;
    }

    @Override
    public String toString() {
        RecursoComunitario recursoComunitario = (RecursoComunitario) Utilidad.getObjeto(getIdRecursoComunitario(), Constantes.RECURSO_COMUNITARIO);
        return recursoComunitario.getNombre();
    }
}
