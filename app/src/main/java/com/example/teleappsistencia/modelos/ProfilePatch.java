package com.example.teleappsistencia.modelos;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Clase "ProfilePatch" utilizada para mandar los cambios sobre un usuario del sistema al servidor.
 *
 * El servidor solo tendrá en cuenta aquellos campos que no sean nulos, por lo que para prevenir errores,
 * esta clase 
 */
public class ProfilePatch implements Serializable {
    @SerializedName("is_active")
    private Boolean isActive;
    @SerializedName("username")
    private String nuevoUsername;
    @SerializedName("groups")
    private Integer nuevoGrupo;
    @SerializedName("first_name")
    private String nuevoNombre;
    @SerializedName("last_name")
    private String nuevosApellidos;
    @SerializedName("email")
    private String nuevoEmail;
    @SerializedName("password")
    private String nuevaPassword;

    private transient Usuario usuarioAsociado;
    private transient Uri nuevaFotoPerfil;

    // Inicializamos a nulo para asegurarnos de que el servidor lo interpreta bien
    public ProfilePatch(Usuario usuarioAsociado) {
        this.usuarioAsociado = usuarioAsociado;
        nuevoEmail = null; nuevaPassword = null;
        nuevaFotoPerfil = null;
    }

    // Getters
    public Boolean getIsActive() { return isActive; }
    public String getNuevoUsername() { return nuevoUsername; }
    public Integer getNuevoGrupo() { return nuevoGrupo; }
    public String getNuevoNombre() { return nuevoNombre; }
    public String getNuevosApellidos() { return nuevosApellidos; }
    public String getNuevoEmail() { return nuevoEmail; }
    public String getNuevaPassword() { return nuevaPassword; }
    public Uri getNuevaFotoPerfil() { return nuevaFotoPerfil; }

    // Setters con prevencion de errores
    public void setActive(boolean isActive) {
        if (null == usuarioAsociado.getActive() || usuarioAsociado.getActive() != isActive)
            this.isActive = isActive;
        else
            this.isActive = null;
    }
    public void setNuevoUsername(String nuevoUsername) {
        if (null != nuevoUsername && !nuevoUsername.trim().isEmpty() && !nuevoUsername.equals(usuarioAsociado.getUsername()))
            this.nuevoUsername = nuevoUsername;
        else
            this.nuevoUsername = null;
    }
    public void setNuevoGrupo(Integer nuevoGrupo) {
        if (null == Utilidad.getGrupoUser(usuarioAsociado) || Utilidad.getGrupoUser(usuarioAsociado).getId() != nuevoGrupo)
            this.nuevoGrupo = nuevoGrupo;
        else
            this.nuevoGrupo = null;
    }
    public void setNuevoNombre(String nuevoNombre) {
        if (null != nuevoNombre && !nuevoNombre.trim().isEmpty() && !nuevoNombre.equals(usuarioAsociado.getFirstName()))
            this.nuevoNombre = nuevoNombre;
        else
            this.nuevoNombre = null;
    }
    public void setNuevosApellidos(String nuevosApellidos) {
        if (null != nuevosApellidos && !nuevosApellidos.trim().isEmpty() && !nuevosApellidos.equals(usuarioAsociado.getLastName()))
            this.nuevosApellidos = nuevosApellidos;
        else
            this.nuevosApellidos = null;
    }
    public void setNuevoEmail(String nuevoEmail) {
        if (null != nuevoEmail && !nuevoEmail.equals(usuarioAsociado.getEmail()))
            this.nuevoEmail = nuevoEmail;
        else
            this.nuevoEmail = null;
    }
    public void setNuevaPassword(String nuevaPassword) {
        if (null != nuevaPassword && !nuevaPassword.isEmpty())
            this.nuevaPassword = nuevaPassword;
        else
            this.nuevaPassword = null;
    }
    public void setNuevaFotoPerfil(Uri nuevaFotoPerfil) {
        this.nuevaFotoPerfil = nuevaFotoPerfil;
    }

    // Utilidad para facilitar usar el Cliente Retrofit
    public boolean hasPatches() {
        return (!this.asPartMap().isEmpty()) || null != nuevaFotoPerfil;
    };

    /**
     * Pasa los campos a un mapa de Parts para poder ser enviado junto a la imagen.
     * @return
     */
    @NonNull
    private Map<String, RequestBody> asPartMap() {
        Map<String, RequestBody> campos = new HashMap<>();
        if (null != isActive) campos.put(
            "is_active", RequestBody.create(isActive.toString(), MediaType.parse("application/json"))
        );
        if (null != nuevoUsername) campos.put(
            "username", RequestBody.create(nuevoUsername, MediaType.parse("text/plain"))
        );
        if (null != nuevoGrupo) campos.put(
            "groups", RequestBody.create(nuevoGrupo.toString(), MediaType.parse("application/json"))
        );
        if (null != nuevoNombre) campos.put(
            "first_name", RequestBody.create(nuevoNombre, MediaType.parse("text/plain"))
        );
        if (null != nuevosApellidos) campos.put(
            "last_name", RequestBody.create(nuevosApellidos, MediaType.parse("text/plain"))
        );
        if (null != nuevoEmail) campos.put(
            "email", RequestBody.create(nuevoEmail, MediaType.parse("text/plain"))
        );
        if (null != nuevaPassword) campos.put(
            "password", RequestBody.create(nuevaPassword, MediaType.parse("text/plain"))
        );

        return campos;
    }

    /**
     * Genera la call a la API rest para facilitar aplicar cambios a un modelo.
     *
     * @param context {@link Context} del fragment/activity que llama al método.
     * @param isForSelf Indica si estamos modificando nuestro perfil u otro usuario.
     *                 <ul>
     *                   <li>Si true se usará el endpoint {@link APIService#patchPerfilMultipart(int, Map, MultipartBody.Part, String)}</li>
     *                   <li>Si false se usa endpoint {@link APIService#patchUsuarioMultipart(int, Map, MultipartBody.Part, String)}</li>
     *                 </ul>
     * @return {@link Call} que podemos invocar directamente con {@link Call#enqueue(Callback)}
     */
    public Call<Usuario> createMultipartPatchAPICall(Context context, boolean isForSelf) {
        APIService servicio = ClienteRetrofit.getInstance().getAPIService();
        Call<Usuario> call;

        // Crear fichero temporal para poder mandar la imagen
        File tempFile = (null == nuevaFotoPerfil) ? null
            : Utilidad.extraerFicheroTemporal(context, nuevaFotoPerfil);

        // Crear el wrapper Multipart.Part para la imagen
        MultipartBody.Part imagenPart = null;
        if (null != tempFile) {
            imagenPart = MultipartBody.Part.createFormData(
                "imagen", tempFile.getName(),
                RequestBody.create(tempFile, MediaType.parse("image/*"))
            );
        }

        // Crear la call
        if (isForSelf) {
            // Para modificar nuestro perfil
            call = servicio.patchPerfilMultipart(
                usuarioAsociado.getPk(),
                this.asPartMap(), imagenPart,
                Utilidad.getAuthorization()
            );

        } else {
            // Para modificar otro usuario
            call = servicio.patchUsuarioMultipart(
                usuarioAsociado.getPk(),
                this.asPartMap(), imagenPart,
                Utilidad.getAuthorization()
            );
        }

        // Devolver la petición
        return call;
    }

    /**
     * Genera la call a la API rest para facilitar aplicar dar de alta usuarios.
     *
     * @param context {@link Context} del fragment/activity que llama al método.
     * @return {@link Call} que podemos invocar directamente con {@link Call#enqueue(Callback)}
     */
    public Call<Usuario> createMultipartPostAPICall(Context context) {
        APIService servicio = ClienteRetrofit.getInstance().getAPIService();
        Call<Usuario> call;

        // Crear fichero temporal para poder mandar la imagen
        File tempFile = (null == nuevaFotoPerfil) ? null
            : Utilidad.extraerFicheroTemporal(context, nuevaFotoPerfil);

        // Crear el wrapper Multipart.Part para la imagen
        MultipartBody.Part imagenPart = null;
        if (null != tempFile) {
            imagenPart = MultipartBody.Part.createFormData(
                "imagen", tempFile.getName(),
                RequestBody.create(tempFile, MediaType.parse("image/*"))
            );
        }

        // Crear la callaser
        return servicio.postUsuarioMultipart(
            this.asPartMap(), imagenPart,
            Utilidad.getAuthorization()
        );
    }
}
