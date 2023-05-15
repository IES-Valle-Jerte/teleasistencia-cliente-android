package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Clase encargada del modelo del sub-menu Recursos.
 */
public class ClasificacionRecurso implements Serializable {

    /**
     * Atributos de la clase
     */
    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;

    /**
     * Getters y setters
     */
    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return "Clasificacion_recurso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
