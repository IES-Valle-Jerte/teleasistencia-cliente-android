package com.example.teleappsistencia.servicios;

import android.content.Context;

import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.Database;
import com.example.teleappsistencia.modelos.ProfilePatch;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.DispositivoAuxiliar;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.HistoricoTipoSituacion;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.TipoSituacion;
import com.example.teleappsistencia.modelos.TipoVivienda;
import com.example.teleappsistencia.modelos.Usuario;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.RelacionPacientePersona;
import com.example.teleappsistencia.modelos.RelacionTerminalRecursoComunitario;
import com.example.teleappsistencia.modelos.RelacionUsuarioCentro;
import com.google.gson.internal.LinkedTreeMap;

import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.CentroSanitarioEnAlarma;
import com.example.teleappsistencia.modelos.ClasificacionAlarma;
import com.example.teleappsistencia.modelos.PersonaContactoEnAlarma;
import com.example.teleappsistencia.modelos.RecursoComunitarioEnAlarma;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interfaz para realizar llamada a los distintas "entidades" del servidor.
 * Cada entidad tiene una URL correspondiente, y una lista de métodos para realizar las llamadas.
 * Cada método devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
 */
public interface APIService {

    // Peticiones del Token

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/token/")
    public Call<Token> getToken(@Field("username") String username, @Field("password") String password);

    //Peticiones de Tipo Centro Sanitario.

    @GET("/api-rest/tipo_centro_sanitario")
    public Call<List<TipoCentroSanitario>> getTipoCentroSanitario(@Header("Authorization") String token);

    @POST("/api-rest/tipo_centro_sanitario")
    public Call<Object> postTipoCentroSanitario(@Body TipoCentroSanitario tipoCentroSanitario, @Header("Authorization") String token);

    @PUT("/api-rest/tipo_centro_sanitario/{id_dato}")
    public Call<Object> putTipoCentroSanitario(@Path("id_dato") int id_dato, @Body TipoCentroSanitario tipoCentroSanitario, @Header("Authorization") String token);

    @DELETE("/api-rest/tipo_centro_sanitario/{id_dato}")
    public Call<Response<String>> deleteTipoCentroSanitario(@Path ("id_dato") int id_dato, @Header("Authorization") String token);

    //Peticiones de Centro Sanitario.

    @GET("/api-rest/centro_sanitario")
    public Call<List<CentroSanitario>> getCentroSanitario(@Header("Authorization") String token);

    @POST("/api-rest/centro_sanitario")
    public Call<Object> postCentroSanitario(@Body CentroSanitario centroSanitario, @Header("Authorization") String token);

    @PUT("/api-rest/centro_sanitario/{id_dato}")
    public Call<Object> putCentroSanitario(@Path("id_dato") int id_dato, @Body CentroSanitario centroSanitario, @Header("Authorization") String token);

    @DELETE("/api-rest/centro_sanitario/{id_dato}")
    public Call<Response<String>> deleteCentroSanitario(@Path ("id_dato") int id_dato, @Header("Authorization") String token);

    //Peticiones de Recurso Comunitario.

    @GET("/api-rest/recurso_comunitario")
    public Call<List<RecursoComunitario>> getRecursoComunitario(@Header("Authorization") String token);

    @POST("/api-rest/recurso_comunitario")
    public Call<Object> postRecursoComunitario(@Body RecursoComunitario recursoComunitario, @Header("Authorization") String token);

    @PUT("/api-rest/recurso_comunitario/{id_dato}")
    public Call<Object> putRecursoComunitario(@Path("id_dato") int id_dato, @Body RecursoComunitario recursoComunitario, @Header("Authorization") String token);

    @DELETE("/api-rest/recurso_comunitario/{id_dato}")
    public Call<Response<String>> deleteRecursoComunitario(@Path ("id_dato") int id_dato, @Header("Authorization") String token);

    //Peticiones de Tipo Modalidad Paciente.

    @GET("/api-rest/tipo_modalidad_paciente")
    public Call<List<TipoModalidadPaciente>> getTipoModalidadPaciente(@Header("Authorization") String token);

    @POST("/api-rest/tipo_modalidad_paciente")
    public Call<Object> postTipoModalidadPaciente(@Body TipoModalidadPaciente tipoModalidadPaciente, @Header("Authorization") String token);

    @PUT("/api-rest/tipo_modalidad_paciente/{id_dato}")
    public Call<Object> putTipoModalidadPaciente(@Path("id_dato") int id_dato, @Body TipoModalidadPaciente tipoModalidadPaciente, @Header("Authorization") String token);

    @DELETE("/api-rest/tipo_modalidad_paciente/{id_dato}")
    public Call<Response<String>> deleteTipoModalidadPaciente(@Path ("id_dato") int id_dato, @Header("Authorization") String token);

    //Peticiones de Tipo Recurso Comunitario.

    @GET("/api-rest/tipo_recurso_comunitario")
    public Call<List<TipoRecursoComunitario>> getTipoRecursoComunitario(@Header("Authorization") String token);

    @GET("api-rest/tipo_recurso_comunitario")
    public Call<List<TipoRecursoComunitario>> getTiposRecursosComunitariosPorId(@Query("id_clasificacion_recurso_comunitario") int idClasificacionRecursoComunitario, @Header("Authorization") String token);

    @POST("/api-rest/tipo_recurso_comunitario")
    public Call<Object> postTipoRecursoComunitario(@Body TipoRecursoComunitario tipoRecursoComunitario, @Header("Authorization") String token);

    @PUT("/api-rest/tipo_recurso_comunitario/{id_dato}")
    public Call<Object> putTipoRecursoComunitario(@Path("id_dato") int id_dato, @Body TipoRecursoComunitario tipoRecursoComunitario, @Header("Authorization") String token);

    @DELETE("/api-rest/tipo_recurso_comunitario/{id_dato}")
    public Call<Response<String>> deleteTipoRecursoComunitario(@Path ("id_dato") int id_dato, @Header("Authorization") String token);

    // Peticiones de Direccion

    @GET("api-rest/direccion")
    public Call<List<Direccion>> getDirecciones(@Header("Authorization") String token);

    @GET("api-rest/direccion")
    public Call<Direccion> getDireccionById(@Query("id") String id, @Header("Authorization") String token);

    @POST("api-rest/direccion")
    public Call<Object> addDireccion(@Body Direccion direccion, @Header("Authorization") String token);

    @PUT("api-rest/direccion/{id}")
    public Call<Object> modifyDireccion(@Path("id") int id, @Body Direccion direccion, @Header("Authorization") String token);

    @DELETE("api-rest/direccion/{id}")
    Call<Response<String>> deleteDireccion(@Path("id") int id, @Header("Authorization") String token);


    // Peticiones de Dispositivos Auxiliares

    @GET("api-rest/dispositivos_auxiliares_en_terminal")
    public Call<List<DispositivoAuxiliar>> getDispositivosAuxiliares(@Header("Authorization") String token);

    @GET("api-rest/dispositivos_auxiliares_en_terminal")
    public Call<DispositivoAuxiliar> getDispositivoAuxiliarById(@Query("id") String id, @Header("Authorization") String token);

    @POST("api-rest/dispositivos_auxiliares_en_terminal")
    public Call<Object> addDispositivoAuxiliar(@Body DispositivoAuxiliar dispositivoAuxiliar, @Header("Authorization") String token);

    @PUT("api-rest/dispositivos_auxiliares_en_terminal/{id}")
    public Call<Object> modifyDispositivoAuxiliar(@Path("id") int id, @Body DispositivoAuxiliar dispositivoAuxiliar, @Header("Authorization") String token);

    @DELETE("api-rest/dispositivos_auxiliares_en_terminal/{id}")
    Call<Response<String>> deleteDispositivosAuxiliar(@Path("id") int id, @Header("Authorization") String token);

    // Peticiones de Batabases
    @GET("/api-rest/databases")
    Call<List<Database>> getDatabases(@Header("Authorization") String token);

    // Peticiones de Grupo

    @GET("api-rest/groups")
    public Call<List<Grupo>> getGrupos(@Header("Authorization") String token);

    @GET("api-rest/groups")
    public Call<Grupo> getGrupoByPk(@Query("pk") String pk, @Header("Authorization") String token);

    @POST("api-rest/groups")
    public Call<Object> addGrupo(@Body Grupo grupo, @Header("Authorization") String token);

    @PUT("api-rest/groups/{pk}")
    public Call<Object> modifyGrupo(@Path("pk") int pk, @Body Grupo grupo, @Header("Authorization") String token);

    @DELETE("api-rest/groups/{pk}")
    Call<Response<String>> deleteGrupo(@Path("pk") int pk, @Header("Authorization") String token);


    // Peticiones de Historico Tipo Situacion

    @GET("api-rest/historico_tipo_situacion")
    public Call<List<HistoricoTipoSituacion>> getHistoricoTipoSituacion(@Header("Authorization") String token);

    @GET("api-rest/historico_tipo_situacion")
    public Call<HistoricoTipoSituacion> getHistoricoTipoSituacionById(@Query("id") String id, @Header("Authorization") String token);

    @POST("api-rest/historico_tipo_situacion")
    public Call<Object> addHistoricoTipoSituacion(@Body HistoricoTipoSituacion historicoTipoSituacion, @Header("Authorization") String token);

    @PUT("api-rest/historico_tipo_situacion/{id}")
    public Call<Object> modifyHistoricoTipoSituacion(@Path("id") int id, @Body HistoricoTipoSituacion historicoTipoSituacion, @Header("Authorization") String token);

    @DELETE("api-rest/historico_tipo_situacion/{id}")
    Call<Response<String>> deleteHistoricoTipoSituacion(@Path("id") int id, @Header("Authorization") String token);


    // Peticiones de Persona

    @GET("api-rest/persona")
    public Call<List<Persona>> getPersonas(@Header("Authorization") String token);

    @GET("api-rest/persona")
    public Call<Persona> getPersonaById(@Query("id") String id, @Header("Authorization") String token);

    @POST("api-rest/persona")
    public Call<Object> addPersona(@Body Persona persona, @Header("Authorization") String token);

    @PUT("api-rest/persona/{id}")
    public Call<Object> modifyPersona(@Path("id") int id, @Body Persona persona, @Header("Authorization") String token);

    @DELETE("api-rest/persona/{id}")
    Call<Response<String>> deletePersona(@Path("id") int id, @Header("Authorization") String token);


    // Peticiones de Tipo Situacion

    @GET("api-rest/tipo_situacion")
    public Call<List<TipoSituacion>> getTipoSituacion(@Header("Authorization") String token);

    @GET("api-rest/tipo_situacion")
    public Call<TipoSituacion> getTipoSituacionById(@Query("id") String id, @Header("Authorization") String token);

    @POST("api-rest/tipo_situacion")
    public Call<Object> addTipoSituacion(@Body TipoSituacion tipoSituacion, @Header("Authorization") String token);

    @PUT("api-rest/tipo_situacion/{id}")
    public Call<Object> modifyTipoSituacion(@Path("id") int id, @Body TipoSituacion tipoSituacion, @Header("Authorization") String token);

    @DELETE("api-rest/tipo_situacion/{id}")
    Call<Response<String>> deleteTipoSituacion(@Path("id") int id, @Header("Authorization") String token);


    // Peticiones de Tipo Vivienda

    @GET("api-rest/tipo_vivienda")
    public Call<List<TipoVivienda>> getTipoVivienda(@Header("Authorization") String token);

    @GET("api-rest/tipo_vivienda")
    public Call<TipoVivienda> getTipoViviendaById(@Query("id") String id, @Header("Authorization") String token);

    @POST("api-rest/tipo_vivienda")
    public Call<Object> addTipoVivienda(@Body TipoVivienda tipoVivienda, @Header("Authorization") String token);

    @PUT("api-rest/tipo_vivienda/{id}")
    public Call<Object> modifyTipoVivienda(@Path("id") int id, @Body TipoVivienda tipoVivienda, @Header("Authorization") String token);

    @DELETE("api-rest/tipo_vivienda/{id}")
    Call<Response<String>> deleteTipoVivienda(@Path("id") int id, @Header("Authorization") String token);


    // Peticiones de Tipo Alarma

    @GET("api-rest/tipo_alarma")
    Call<List<TipoAlarma>> getTiposAlarmas(@Header("Authorization") String token);
    //Peticiones de Paciente

    
    /**
     * Es una solicitud GET a la URL "api-rest/paciente" con un encabezado "Autorización" y un cuerpo
     * de tipo List<LinkedTreeMap>
     * 
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor
     */
    @GET("api-rest/paciente")
    Call<List<Object>> getPacientes(@Header("Authorization") String token);

    
    /**
     * Esta función enviará una solicitud POST a la URL "api-rest/paciente" siendo el cuerpo de la
     * solicitud el objeto Paciente pasado como parámetro. La función también enviará el token pasado
     * como parámetro en el encabezado de la solicitud.
     * 
     * @param paciente El objeto que se enviará al servidor.
     * @param token El token que obtiene de la solicitud de inicio de sesión.
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor
     */
    @POST("api-rest/paciente")
    Call<Paciente> addPaciente(@Body Paciente paciente, @Header("Authorization") String token);


    
    /**
     * Esta función actualizará al paciente con la identificación especificada en la ruta, con el
     * objeto paciente pasado en el cuerpo, y utilizará el token pasado en el encabezado para
     * autenticar la solicitud.
     * 
     * @param id El id del paciente que desea actualizar.
     * @param paciente El objeto que se enviará al servidor.
     * @param token El token que obtiene de la solicitud de inicio de sesión.
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor
     */
    @PUT("api-rest/paciente/{id}")
    Call<Paciente> updatePaciente(@Path("id") int id, @Body Paciente paciente, @Header("Authorization") String token);

    /**
     * Esta función eliminará al paciente con el id pasado como parámetro en la ruta, y utilizará el
        * token pasado en el encabezado para autenticar la solicitud.
     * 
     * @param id El id del paciente a ser borrado
     * @param token El token que obtiene de la solicitud de inicio de sesión.
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor
     */
    @DELETE("api-rest/paciente/{id}")
    Call<ResponseBody> deletePaciente(@Path("id") String id, @Header("Authorization") String token);

    //Peticiones de Relacion Paciente Persona

    /**
     * Es una petición GET al endpoint "api-rest/relacion_paciente_persona" con cabecera "Autorización". 
     * 
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor
     */
    @GET("api-rest/relacion_paciente_persona")
    Call<List<LinkedTreeMap>> getListadoPacientesPersona(@Header("Authorization") String token);

    
    /**
     * Esta función enviará una solicitud POST al servidor siendo el cuerpo de la solicitud el objeto
     * relacionPacientePersona y el encabezado de la solicitud el token
     *
     * @param relacionPacientePersona es el objeto que quiero enviar al servidor.
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @POST("api-rest/relacion_paciente_persona")
    Call<RelacionPacientePersona> addRelacionPacientePersonaSeleccionadoPaciente(@Body RelacionPacientePersona relacionPacientePersona, @Header("Authorization") String token);

    /**
     * Esta función elimina un objeto RelaciónPacientePersona del servidor
     * 
     * @param id El id del objeto a eliminar.
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @DELETE("api-rest/relacion_paciente_persona/{id}")
    Call<ResponseBody> deleteRelacionPacientePersona(@Path("id") int id, @Header("Authorization") String token);


    /**
     * Esta función actualizará el objeto RelaciónPacientePersona con el id especificado en la URL, con
     * los datos proporcionados en el cuerpo de la solicitud, y devolverá el objeto actualizado
     * 
     * @param id El id del objeto a actualizar.
     * @param relacionPacientePersona es el objeto que quiero actualizar
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @PUT("api-rest/relacion_paciente_persona/{id}")
    Call<RelacionPacientePersona> updateRelacionPacientePersona(@Path("id") int id, @Body RelacionPacientePersona relacionPacientePersona, @Header("Authorization") String token);

    //Peticiones de Listado Relacion Terminal Recurso Comunitario

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     * 
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/relacion_terminal_recurso_comunitario")
    Call<List<LinkedTreeMap>> getListadoRelacionTerminalRecursoComunitario(@Header("Authorization") String token);

    /**
     * Esta función enviará una solicitud POST al servidor siendo el cuerpo de la solicitud el objeto
     * relacionTerminalRecursoComunitario y el encabezado de la solicitud el token
     * 
     * @param relacionTerminalRecursoComunitario es el objeto que quiero enviar al servidor.
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @POST("api-rest/relacion_terminal_recurso_comunitario")
    Call<RelacionTerminalRecursoComunitario> addRelacionTerminalRecursoComunitario(@Body RelacionTerminalRecursoComunitario relacionTerminalRecursoComunitario, @Header("Authorization") String token);

    /**
     * Esta función elimina un objeto RelaciónTerminalRecursoComunitario del servidor
     * 
     * @param id El id del objeto a eliminar.
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @DELETE("api-rest/relacion_terminal_recurso_comunitario/{id}")
    Call<ResponseBody> deleteRelacionTerminalRecursoComunitario(@Path("id") String id, @Header("Authorization") String token);

    /**
     * Esta función actualizará el objeto RelaciónTerminalRecursoComunitario con el id especificado en la URL, con
     * los datos proporcionados en el cuerpo de la solicitud, y devolverá el objeto actualizado
     * 
     * @param id El id del objeto a actualizar.
     * @param relacionTerminalRecursoComunitario es el objeto que quiero actualizar
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @PUT("api-rest/relacion_terminal_recurso_comunitario/{id}")
    Call<RelacionTerminalRecursoComunitario> updateRelacionTerminalRecursoComunitario(@Path("id") int id, @Body RelacionTerminalRecursoComunitario relacionTerminalRecursoComunitario, @Header("Authorization") String token);

    //Peticiones de Listado Relacion Usuario Centro

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     * 
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/relacion_usuario_centro")
    Call<List<LinkedTreeMap>> getListadoRelacionUsuarioCentro(@Header("Authorization") String token);

    /**
     * Esta función enviará una solicitud POST al servidor siendo el cuerpo de la solicitud el objeto
     * relacionUsuarioCentro y el encabezado de la solicitud el token
     * 
     * @param relacionUsuarioCentro es el objeto que quiero enviar al servidor.
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @POST("api-rest/relacion_usuario_centro")
    Call<RelacionUsuarioCentro> addRelacionUsuarioCentro(@Body RelacionUsuarioCentro relacionUsuarioCentro, @Header("Authorization") String token);

    /**
     * Esta función elimina un objeto RelaciónUsuarioCentro del servidor
     * 
     * @param id El id del objeto a eliminar.
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @DELETE("api-rest/relacion_usuario_centro/{id}")
    Call<ResponseBody> deleteRelacionUsuarioCentro(@Path("id") String id, @Header("Authorization") String token);

    /**
     * Esta función actualizará el objeto RelaciónUsuarioCentro con el id especificado en la URL, con los datos
     * proporcionados en el cuerpo de la solicitud, y devolverá el objeto actualizado
     * 
     * @param id El id del objeto a actualizar.
     * @param relacionUsuarioCentro es el objeto que quiero actualizar
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @PUT("api-rest/relacion_usuario_centro/{id}")
    Call<RelacionUsuarioCentro> updateRelacionUsuarioCentro(@Path("id") int id, @Body RelacionUsuarioCentro relacionUsuarioCentro, @Header("Authorization") String token);

    //Peticiones de Terminal

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     * 
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/terminal")
    Call<List<Terminal>> getListadoTerminal(@Header("Authorization") String token);

    @GET("api-rest/terminal")
    Call<List<Terminal>> getTerminalByNumeroTerminal(@Query("numero_terminal") String numero_terminal, @Header("Authorization") String token);

    /**
     * Esta función enviará una solicitud POST al servidor siendo el cuerpo de la solicitud el objeto
     * terminal y el encabezado de la solicitud el token
     * 
     * @param terminal es el objeto que quiero enviar al servidor.
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @POST("api-rest/terminal")
    Call<Terminal> addTerminal(@Body Terminal terminal, @Header("Authorization") String token);

    /**
     * Esta función elimina un objeto Terminal del servidor
     * 
     * @param id El id del objeto a eliminar.
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @DELETE("api-rest/terminal/{id}")
    Call<ResponseBody> deleteTerminal(@Path("id") String id, @Header("Authorization") String token);

    /**
     * Esta función actualizará el objeto Terminal con el id especificado en la URL, con los datos proporcionados
     * en el cuerpo de la solicitud, y devolverá el objeto actualizado
     * 
     * @param id El id del objeto a actualizar.
     * @param terminal es el objeto que quiero actualizar
     * @param token El token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @PUT("api-rest/terminal/{id}")
    Call<Terminal> updateTerminal(@Path("id") int id, @Body Terminal terminal, @Header("Authorization") String token);

    // Peticiones de Usuarios
    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     * 
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/users")
    Call<List<Usuario>> getUsuarios(@Header("Authorization") String token);


    
    /**
     * Esta función realizará una solicitud GET a la URL "api-rest/usuarios" con el parámetro de
     * consulta "nombre de usuario" y el encabezado "Autorización"
     * 
     * @param username El nombre de usuario del usuario que desea recuperar.
     * @param token el token que obtienes del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor
     */
    @GET("api-rest/users")
    Call<List<Usuario>> getUserByUsername(@Query("username") String username, @Header("Authorization") String token);


   /**
    * Esta función enviará una solicitud POST a la URL "api-rest/users" con el cuerpo de la solicitud
    * siendo el objeto Usuario pasado como parámetro. La función también enviará el token pasado como
    * parámetro en el encabezado de la solicitud.
    *
    * @deprecated Usar {@link #postUsuarioMultipart(Map, MultipartBody.Part, String)}
    *
    * @param usuario El objeto que se enviará al servidor.
    * @param token El token que obtienes del inicio de sesión
    * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
    */
    @POST("api-rest/users")
    Call<Object> addUsuario(@Body Usuario usuario, @Header("Authorization") String token);


    /**
     * @deprecated Usar {@link #patchUsuarioMultipart(int, Map, MultipartBody.Part, String)}
     *
     * @param id
     * @param usuario
     * @param token
     * @return
     */
    @PUT("/api-rest/users/{id}")
    Call<Object> modifyUsuario(@Path("id") int id, @Body Usuario usuario, @Header("Authorization") String token);

    @DELETE("/api-rest/users/{id}")
    Call<String> deleteUser(@Path("id") int id, @Header("Authorization") String token);

    /**
     * Se utiliza para modificar los datos de otro usuario.
     * @param modificaciones POJO "parcial" (los campos nulos se ignoran) convertido a un mapa de {@link RequestBody}
     * @param image [Opcional] Nueva imagen de perfil del usuario.
     * @see ProfilePatch#createMultipartPatchAPICall(Context, boolean)
     */
    @PATCH("/api-rest/users/{id_usuario}")
    @Multipart
    Call<Usuario> patchUsuarioMultipart(@Path("id_usuario") int id_usuario, @PartMap Map<String, RequestBody> modificaciones, @Part MultipartBody.Part image, @Header("Authorization") String token);

    /**
     * Se utiliza para dar de alta a un nuevo Usuario.
     * @param modificaciones POJO "parcial" (los campos nulos se ignoran) convertido a un mapa de {@link RequestBody}
     * @param image [Opcional] Nueva imagen de perfil del usuario.
     * @see ProfilePatch#createMultipartPostAPICall(Context)
     */
    @POST("/api-rest/users")
    @Multipart
    Call<Usuario> postUsuarioMultipart(@PartMap Map<String, RequestBody> modificaciones, @Part MultipartBody.Part image, @Header("Authorization") String token);
    // PROFILE
    @GET("/api-rest/profile")
    Call<List<Usuario>> getUsuarioLogueado(@Header("Authorization") String token);

    /**
     * Se utiliza para modificar el perfil de un usuario (deber ser el mismo que el usuario loggeado)
     * @param modificaciones POJO "parcial" (los campos nulos se ignoran) convertido a un mapa de {@link RequestBody}
     * @param image [Opcional] fichero de imagen a
     * @see ProfilePatch#createMultipartPatchAPICall(Context, boolean)
     */
    @PATCH("/api-rest/profile/{id_usuario}")
    @Multipart
    Call<Usuario> patchPerfilMultipart(@Path("id_usuario") int id_usuario, @PartMap Map<String, RequestBody> modificaciones, @Part MultipartBody.Part image, @Header("Authorization") String token);

    /**
     * Permite a los administradores cambiar de BBDD. Verificación en lado de servidor.
     * @return
     */
    @PATCH("/api-rest/profile/{id_usuario}")
    @Multipart
    Call<Usuario> patchPerfilCambioBatabase(@Path("id_usuario") int id_usuario, @Part("id_database") Integer id_database, @Header("Authorization") String token);
    //Peticiones de Personas

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     * 
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/persona")
    Call<List<Persona>> getListadoPersona(@Header("Authorization") String token);

    //Listado de peticiones de Recurso Comunitario

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     *
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/recurso_comunitario")
    Call<List<RecursoComunitario>> getListadoRecursoComunitario(@Header("Authorization") String token);

    //Listado de peticiones de Centros Sanitarios

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     *
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/centro_sanitario")
    Call<List<CentroSanitario>> getListadoCentroSanitario(@Header("Authorization") String token);

    //Peticiones de TipoVivienda

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     *
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/tipo_vivienda")
    Call<List<TipoVivienda>> getListadoTipoVivienda(@Header("Authorization") String token);

    //Peticiones de TipoModalidadPaciente

    /**
     * Esta función devolverá una llamada al servidor y pasará el token especificado en el encabezado de
     * Autorización.
     *
     * @param token el token que obtengo del inicio de sesión
     * @return Devuelve una llamada a Retrofit, que se utiliza para realizar la llamada al servidor.
     */
    @GET("api-rest/tipo_modalidad_paciente")
    Call<List<TipoModalidadPaciente>> getListadoTipoModalidadPaciente(@Header("Authorization") String token);
    /* Peticiones de Alarma */
    @GET("api-rest/alarma")
    public Call<List<Object>> getAlarmas(@Header("Authorization") String token);

    @GET("api-rest/alarma/{id}")
    public Call<Alarma> getAlarmabyId(@Path("id") int id, @Header("Authorization") String token);

    @GET("/api-rest/alarma")
    public Call<List<Object>> getAlarmasbyIdTeleoperador(@Query("id_teleoperador") int id, @Header("Authorization") String token);

    @GET("/api-rest/alarma?id_teleoperador__isnull=true")
    public Call<List<Object>> getAlarmasSinAsignar(@Header("Authorization") String token);

    @GET("/api-rest/alarma")
    public Call<List<Object>> getAlarmasDelDia(@Query("fecha_registro__gt") String fecha, @Header("Authorization") String token);

    @POST("api-rest/alarma")
    public Call<Alarma> addAlarma(@Body Alarma alarma, @Header("Authorization") String token);

    @PUT("api-rest/alarma/{id}")
    public Call<ResponseBody> actualizarAlarma(@Path("id") int id, @Header("Authorization") String token, @Body Alarma alarma);

    @DELETE("api-rest/alarma/{id}")
    public Call<ResponseBody> deleteAlarmabyId(@Path("id") int id, @Header("Authorization") String token);


    /* Peticiones Clasificacion Alarma */
    @GET("/api-rest/clasificacion_alarma")
    public Call<List<Object>> getListaClasificacionAlarma(@Header("Authorization") String token);

    @POST("api-rest/clasificacion_alarma")
    public Call<ClasificacionAlarma> addClasificacionAlarma(@Body ClasificacionAlarma clasificacionAlarma, @Header("Authorization") String token);

    @PUT("api-rest/clasificacion_alarma/{id}")
    public Call<ResponseBody> actualizarClasificacionAlarma(@Path("id") int id, @Header("Authorization") String token, @Body ClasificacionAlarma clasificacionAlarma);

    @DELETE("api-rest/clasificacion_alarma/{id}")
    public Call<ResponseBody> deleteClasificacionAlarmabyId(@Path("id") int id, @Header("Authorization") String token);


    /* Peticiones Tipo Alarma */
    @GET("/api-rest/tipo_alarma")
    public Call<List<Object>> getTiposAlarma(@Header("Authorization") String token);

    @POST("api-rest/tipo_alarma")
    public Call<TipoAlarma> addTipoAlarma(@Body TipoAlarma tipoAlarma, @Header("Authorization") String token);

    @PUT("api-rest/tipo_alarma/{id}")
    public Call<ResponseBody> actualizarTipoAlarma(@Path("id") int id, @Header("Authorization") String token, @Body TipoAlarma tipoAlarma);

    @DELETE("api-rest/tipo_alarma/{id}")
    public Call<ResponseBody> deleteTipoAlarmabyId(@Path("id") int id, @Header("Authorization") String token);


    /* Peticiones Centro_sanitario_en_alarma */
    @GET("/api-rest/centro_sanitario_en_alarma")
    public Call<List<Object>> getCentrosSanitariosEnAlarma(@Header("Authorization") String token);

    @POST("/api-rest/centro_sanitario_en_alarma")
    public Call<CentroSanitarioEnAlarma> addCentroSanitarioEnAlarma(@Body CentroSanitarioEnAlarma centroSanitarioEnAlarma, @Header("Authorization") String token);

    @PUT("/api-rest/centro_sanitario_en_alarma/{id}")
    public Call<ResponseBody> actualizarCentroSanitarioEnAlarma(@Path("id") int id, @Header("Authorization") String token, @Body CentroSanitarioEnAlarma centroSanitarioEnAlarma);

    @DELETE("/api-rest/centro_sanitario_en_alarma/{id}")
    public Call<ResponseBody> deleteCentroSanitarioEnAlarmabyId(@Path("id") int id, @Header("Authorization") String token);


    /* Peticiones Persona_contacto_en_alarma */
    @GET("/api-rest/persona_contacto_en_alarma")
    public Call<List<Object>> getPersonasContactoEnAlarma(@Header("Authorization") String token);

    @POST("/api-rest/persona_contacto_en_alarma")
    public Call<PersonaContactoEnAlarma> addPersonaContactoEnAlarma(@Body PersonaContactoEnAlarma personaContactoEnAlarma,  @Header("Authorization") String token);

    @PUT("/api-rest/persona_contacto_en_alarma/{id}")
    public Call<ResponseBody> actualizarPersonaContactoEnAlarma(@Path("id") int id, @Header("Authorization") String token, @Body PersonaContactoEnAlarma personaContactoEnAlarma);

    @DELETE("/api-rest/persona_contacto_en_alarma/{id}")
    public Call<ResponseBody> deletePersonaContactoEnAlarmabyId(@Path("id") int id, @Header("Authorization") String token);


    /* Peticiones Recurso_comunitario_en_alarma*/
    @GET("/api-rest/recursos_comunitarios_en_alarma")
    public Call<List<Object>> getRecursosComunitariosEnAlarma(@Header("Authorization") String token);

    @POST("/api-rest/recursos_comunitarios_en_alarma")
    public Call<RecursoComunitarioEnAlarma> addRecursoComunitarioEnAlarma(@Body RecursoComunitarioEnAlarma recursoComunitarioEnAlarma, @Header("Authorization") String token);

    @PUT("/api-rest/recursos_comunitarios_en_alarma/{id}")
    public Call<ResponseBody> actualizarRecursoComunitarioEnAlarma(@Path("id") int id, @Header("Authorization") String token, @Body RecursoComunitarioEnAlarma recursoComunitarioEnAlarma);

    @DELETE("/api-rest/recursos_comunitarios_en_alarma/{id}")
    public Call<ResponseBody> deleteRecursoComunitarioEnAlarmabyId(@Path("id") int id, @Header("Authorization") String token);

    /* Peticiones Terminal */
    @GET("/api-rest/terminal")
    public Call<List<Object>> getTerminales(@Header("Authorization") String token);

    // Peticiones Relacion Paciente - Persona
    @GET("/api-rest/relacion_paciente_persona")
    public Call<List<Object>> getContactosbyIdPaciente(@Query("id_paciente") int id, @Header("Authorization") String token);

    // Peticiones Relacion Usuario - Centro
    @GET("/api-rest/relacion_usuario_centro")
    public Call<List<Object>> getCentrosbyIdPaciente(@Query("id_paciente") int id, @Header("Authorization") String token);

    // Peticiones Relacion Terminal - Recurso Comunitario
    @GET("/api-rest/relacion_terminal_recurso_comunitario")
    public Call<List<Object>> getRecursosComunitariosbyIdTerminal(@Query("id_terminal") int id, @Header("Authorization") String token);

    // mod-GAG
    /**
     * Peticiones Recursos
     */
    // Peticion Clasificación_recurso_comunitario
    @GET("/api-rest/clasificacion_recurso_comunitario")
    public Call<List<Object>> getClasificacionRecursoComunitario(@Header("Authorization") String token);
}
