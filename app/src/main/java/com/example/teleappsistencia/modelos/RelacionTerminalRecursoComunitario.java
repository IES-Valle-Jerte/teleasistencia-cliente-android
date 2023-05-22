package com.example.teleappsistencia.modelos;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

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
    @SerializedName("tiempo_estimado")
    private int tiempoEstimado;
    private static String cadena;
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

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    @Override
    public String toString() {
            /*APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<List<RecursoComunitario>> call = apiService.getListadoRecursoComunitario(Constantes.BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new retrofit2.Callback<List<RecursoComunitario>>() {
                @Override
                public void onResponse(Call<List<RecursoComunitario>> call, Response<List<RecursoComunitario>> response) {
                    if (response.isSuccessful()) {
                        List<RecursoComunitario> listadoRecursoComunitario = response.body();
                        for (RecursoComunitario recurso: listadoRecursoComunitario) {
                            if (recurso.getId()==Integer.parseInt(idRecursoComunitario.toString())){
                                TipoRecursoComunitario t= (TipoRecursoComunitario) Utilidad.getObjeto(recurso.getTipoRecursoComunitario(),Constantes.TIPO_RECURSO_COMUNITARIO);
                                cadena = "["+recurso.getNombre()+"]["+tiempoEstimado+"]["+t.getNombreTipoRecursoComunitario()+"]";
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<RecursoComunitario>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        return cadena;*/
        return "idRecursoComunitario=" + idRecursoComunitario +
                " - tiempoEstimado=" + tiempoEstimado;
    }

    /* @Override
    public String toString() {
        RecursoComunitario recursoComunitario = (RecursoComunitario) Utilidad.getObjeto(getIdRecursoComunitario(), Constantes.RECURSO_COMUNITARIO);
        return recursoComunitario.getNombre();
    }*/
}
