package com.example.teleappsistencia.servicios;
import com.example.teleappsistencia.utilidades.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Es una clase singleton que construye una instancia de Retrofit y la proporciona al resto de la
 * aplicación. Esta clase es la encargada de configurar la conexión con el servidor. 
 * También es la que se encarga de la serialización de los objetos apoyado en la librería GSON, que se integra
 * gracias al convertidor que proporciona la librería Retrofit.
 */
public class ClienteRetrofit {

    // Instancia de la clase
    private static ClienteRetrofit clienteRetrofit = null;

    // Instancia de la interfaz
    private APIService apiService;

    
    /**
     * Método para obtener la instancia de la clase.   
     * Si el objeto clienteRetrofit es nulo, cree uno nuevo. De lo contrario, devolver el existente
     * 
     * @return La instancia de la clase.
     */
    public static ClienteRetrofit getInstance() {
        if (clienteRetrofit == null) {
            clienteRetrofit = new ClienteRetrofit();
        }
        return clienteRetrofit;
    }


    // Un constructor privado para evitar que se pueda crear una instancia de la clase desde fuera.
    private ClienteRetrofit() {
        buildRetrofit(Constantes.API_BASE_URL);
    }


    /**
     * Esta función crea un objeto Retrofit que se usará para realizar llamadas a la API.
     * 
     * @param urlAPI La URL de la API.
     */
    private void buildRetrofit(String urlAPI) {
        
        // Configuración de la conexión con el servidor a través de OkHttp, utilizado por Retrofit como capa de conexión.
        OkHttpClient clienteHttp =
                new OkHttpClient.Builder()
                        // Se configura el tiempo de espera de la conexión.
                        .connectTimeout(30, TimeUnit.SECONDS)
                        // Se configura el tiempo de espera de la respuesta.
                        .readTimeout(30, TimeUnit.SECONDS)
                        // Si la conexión del servidor es lenta, no intenta de nuevo y evita una nueva petición (OKHTTP si la conexión es lenta, intenta de nuevo)
                        .retryOnConnectionFailure(Boolean.FALSE)
                        .build();

        // Configuración del formato de fechas.
        Gson gson = new GsonBuilder()
                .setDateFormat(Constantes.FORMATEADOR_API).create();

        /*
         * Se crea un objeto Retrofit, que se encarga de la conexión con la API.
         * Se le pasa como parámetro la URL de la API, el cliente HTTP y el convertidor de objetos.
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .client(clienteHttp)
                // Conversor para que los booleanos y números se manden como boleanos y no texto plano
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        /*
         * Se crea un objeto de la interfaz APIService, que es la clase que implementa la interfaz de la API.
         * Se le pasa como parámetro el objeto Retrofit.
         */
        this.apiService = retrofit.create(APIService.class);

    }

    /**
     * Esta función devuelve una instancia de la clase APIService.
     * 
     * @return El objeto apiService.
     */
    public APIService getAPIService() {
        return this.apiService;
    }

}
