package com.example.teleappsistencia.modelos;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Clase POJO "ProfilePatch" utilizada para mandar los cambios sobre un usuario del sistema al servidor.
 * El servidor solo tendrá en cuenta aquellos campos que no sean nulos
 */
public class ProfilePatch implements Serializable {
    @SerializedName("email")
    private String nuevoEmail;
    @SerializedName("password")
    private String nuevaContrasenia;

    private transient Usuario usuarioAsociado;
    private transient Uri nuevaFotoPerfil;

    // Inicializamos a nulo para asegurarnos de que el servidor lo interpreta bien
    public ProfilePatch(Usuario usuarioAsociado) {
        this.usuarioAsociado = usuarioAsociado;
        nuevoEmail = null; nuevaContrasenia = null;
        nuevaFotoPerfil = null;
    }

    // Getters
    public String getNuevoEmail() { return nuevoEmail; }
    public String getNuevaContrasenia() { return nuevaContrasenia; }
    public Uri getNuevaFotoPerfil() { return nuevaFotoPerfil; }

    // Setters
    public void setNuevoEmail(String nuevoEmail) {
        if (!usuarioAsociado.getEmail().equals(nuevoEmail))
            this.nuevoEmail = nuevoEmail;
    }
    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }
    public void setNuevaFotoPerfil(Uri nuevaFotoPerfil) {
        this.nuevaFotoPerfil = nuevaFotoPerfil;
    }

    // Utilidad para facilitar usar el Cliente Retrofit
    public boolean hasPatches() {
        return (nuevoEmail != null && !nuevoEmail.isEmpty())
            || (nuevaContrasenia != null && !nuevaContrasenia.isEmpty())
            || nuevaFotoPerfil != null;
    };

    /**
     * Pasa los campos a un mapa de Parts para poder ser enviado junto a la imagen
     * @return
     */
    private Map<String, RequestBody> asPartMap() {
        Map<String, RequestBody> campos = new HashMap<>();
        if (nuevoEmail != null) campos.put(
            "email", RequestBody.create(nuevoEmail, MediaType.parse("text/plain"))
        );
        if (nuevaContrasenia != null) campos.put(
            "password", RequestBody.create(nuevaContrasenia, MediaType.parse("text/plain"))
        );

        return campos;
    }

    public Call<Usuario> createAPIServiceCall(File imageFile) {
        MultipartBody.Part imagenPart = null;

        APIService servicio = ClienteRetrofit.getInstance().getAPIService();
        Call<Usuario> call;

        // Sacamos el fichero de la nueva foto de perfil y creamos multipart-part para pasarlo al servidor
        if (null != nuevaFotoPerfil && null != imageFile) {
            imagenPart = MultipartBody.Part.createFormData(
                    "imagen", imageFile.getName(),
                    RequestBody.create(imageFile, MediaType.parse("image/*"))
            );
        }

        // Cargar los cambios en la petición
        call = servicio.patchImagenPerfil(
                usuarioAsociado.getPk(), this.asPartMap(), imagenPart,
                Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());

        // Devolver la petición
        return call;
    }
}
