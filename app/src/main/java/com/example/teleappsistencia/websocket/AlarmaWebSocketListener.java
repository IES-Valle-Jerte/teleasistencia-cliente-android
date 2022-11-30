package com.example.teleappsistencia.websocket;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.ui.fragments.gestionAlarmasFragments.AlarmAlertFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * @author Jorge Luis Fernández Díaz.
 * @version 21.05.2022
 * Clase que extiende de WebSocketListener. Contiene los métodos que se ejecutarán cuando la conexión
 * realice acciones (conectarse y recibir mensajes). Además se incluyen aquí varios métodos relacionados
 * con las notificaciones.
 */
public class AlarmaWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private MainActivity activity;

    /**
     * Constructor del WebSocketListener
     * @param activity para acceder al UiThread y al getSupportFragmentManager()
     */
    public AlarmaWebSocketListener(Activity activity){
        this.activity = (MainActivity) activity;
        createNotificationChannel(); // Hay que crear el canal de las notificaciones al iniciar (consultar API de Android)
    }

    /**
     * Método que se ejecuta cuando se establece la conexión con el servidor.
     * TODO: posible mejora. Poner un icono o hacer algo para indicar que se está conectado.
     * @param webSocket
     * @param response
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        System.out.println(Constantes.CONEXION_ESTABLECIDA_WEBSOCKET);
    }

    /**
     * Método que se ejecuta cuando se recibe un mensaje dese el servidor. El String txt_data será un
     * Json con dos objetos: la acción que se ha hecho, y la alamra que se está notificando.
     * La acción, a día de hoy, puede ser de dos tipos: nueva alarma, o asignación de alarma. Nueva
     * alarma indicará que la alarma notificada ha sido creada, y por lo tanto hay que crear
     * una notificación emergente y el diálogo correspondiente.
     * Asignación de alarma indicará que esa alarma ya ha sido asignada a un teleoperador, y por lo
     * tanto hay que borrar la notificación emergente correspondiente a esa alarma.
     * @param webSocket
     * @param text_data
     */
    @Override
    public void onMessage(WebSocket webSocket, String text_data) {
        try {
            JSONObject object = new JSONObject(text_data);
            String action = object.getString(Constantes.ACTION);
            JSONObject alarma_object = object.getJSONObject(Constantes.ALARMA_MIN);
            Gson gson= new Gson();
            Alarma alarma = gson.fromJson(alarma_object.toString(), Alarma.class);
            // Evaluamos la acción a realizar
            switch (action){
                case Constantes.NEW_ALARM:
                    crearNotificacion(alarma);
                    break;
                case Constantes.ALARM_ASSIGNMENT:
                    borrarNotificacion(alarma);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Este método se ejecuta cuando falla la conexión. Muestra un TOAST con la información.
     * TODO: posiblemente sea mejor opción crear un alert dialog y/o alguna otra alternativa como poner un icono en algún sitio
     * @param webSocket
     * @param t
     * @param response
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        System.out.println(Constantes.ERROR_ + t.getMessage());
        this.activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
                Toast.makeText(activity.getApplicationContext(), Constantes.MENSAJE_FALLO_CONEXION_WS, Toast.LENGTH_LONG).show();
            }
        });
        t.printStackTrace();
    }


    /**
     * Este método crea una notificación con los datos de la nueva alarma que se ha creado. De momento,
     * solo muestra una notificación simple y crea el alert dialog en la pantalla principal de la aplicación.
     * @param alarma
     */
    public void crearNotificacion(Alarma alarma){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity.getApplicationContext(), Constantes.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(Constantes.NUEVA_ALARMA)
                .setContentText(Constantes.ALARMA_CREADA_CON_ID + String.valueOf(alarma.getId()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.alarmicon));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
        notificationManager.notify(alarma.getId(), builder.build()); // El id de la notificación será el id de la alarma, así podremos borrarla en el futuro (IMPORTANTE)
        this.crearAlertDialog(alarma);
    }

    /**
     * Este método borra la notificación emergente perteneciente a esa alarma
     * @param alarma
     */
    public void borrarNotificacion(Alarma alarma){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
        notificationManager.cancel(alarma.getId());
    }

    /**
     * Este método crea el canal de notificaciones la primera vez que se ejecute la app.
     * A partir de Android 8.0 (nivel de API 26), todas las notificaciones deben asignarse a un canal; si no, no aparecerán.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH; // Importance high para que aparezca en
            NotificationChannel channel = new NotificationChannel(Constantes.NOTIFICATION_CHANNEL_ID, Constantes.NOTIFICATION_CHANNEL_NAME, importance);
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Este método crea una notificación con el id de la alarma que recibe por parámetros.
     * TODO: posibles mejoras: poner icono pequeño, hacer la notificación expandible para mostrar más datos, y/o poner acciones (ACEPTAR, RECHAZAR...)
     * @param alarmaNotificada será la alarma que nos envía el servidor
     */
    public void crearAlertDialog(Alarma alarmaNotificada){
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlarmAlertFragment aAF = AlarmAlertFragment.newInstance(alarmaNotificada);
                aAF.setCancelable(false); // Esta opción bloquea la pantalla hasta que Rechazamos o Aceptamos la alarma
                aAF.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_MyDialogTheme);
                aAF.show(activity.getSupportFragmentManager(), null);
            }
        });
    }


}
