package com.example.teleappsistencia.utilidades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.CentroSanitarioEnAlarma;
import com.example.teleappsistencia.modelos.ClasificacionAlarma;
import com.example.teleappsistencia.modelos.Contacto;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.PersonaContactoEnAlarma;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.RecursoComunitarioEnAlarma;
import com.example.teleappsistencia.modelos.RelacionPacientePersona;
import com.example.teleappsistencia.modelos.RelacionTerminalRecursoComunitario;
import com.example.teleappsistencia.modelos.RelacionUsuarioCentro;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.modelos.TipoSituacion;
import com.example.teleappsistencia.modelos.TipoVivienda;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.websocket.AlarmaWebSocketListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class Utilidad {

    /**
     * Token para poder realizar las peticiones a la API.
     */
    private static Token token;

    /**
     * Usuario logueado en el sistema.
     */
    private static Usuario userLogged;

    /**
     * Si isAdmin está en true se podrá acceder a todas las opciones del menu.
     * Si es false algunas opciones se ocultarán.
     */
    private static boolean isAdmin;

    /**
     * Método que recibe un número y si se encuentra entre 1 y 9 le añade un 0 delante.
     * Este método es utilizado para los meses y días de las fechas.
     *
     * @param number Número al que hay que añadirle el 0 si cumple con los requisitos.
     * @return Devuelve el número como un String y con el 0 delante si cumple con los requisitos.
     */
    public static String twoDigitsDate(int number) {
        return (number <= 9) ? ("0" + number) : String.valueOf(number);
    }

    /**
     * Método usado para establecer la conexión con el WebSocket del servidor.
     *
     * @param activity le llega la activity porque el WSListener lo va a necesitar
     */
    public static void iniciarEscuchaAlarmas(Activity activity) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Constantes.DIRECCION_WEBSOCKET).build();
        AlarmaWebSocketListener aWSListener = new AlarmaWebSocketListener(activity);
        WebSocket ws = client.newWebSocket(request, aWSListener);
        client.dispatcher().executorService().shutdown();
    }

    /**
     * Este método convierte un Objeto LinkedTreeMap en un Objeto de otra clase, dependiendo de nuestras
     * necesidades. Se usa cuando
     *
     * @param lTM
     * @param tipo
     * @return
     */
    public static Object getObjeto(Object lTM, String tipo) {
        Gson gson = new Gson();
        Type type = null;
        Object objeto = null;
        switch (tipo) {
            case Constantes.GRUPO:
                type = new TypeToken<Grupo>() {
                }.getType();
                break;
            case Constantes.TELEOPERADOR:
                type = new TypeToken<Teleoperador>() {
                }.getType();
                break;
            case Constantes.TIPO_SITUACION:
                type = new TypeToken<TipoSituacion>() {
                }.getType();
                break;
            case Constantes.TIPOALARMA:
                type = new TypeToken<TipoAlarma>() {
                }.getType();
                break;
            case Constantes.CLASIFICACION_ALARMA:
                type = new TypeToken<ClasificacionAlarma>() {
                }.getType();
                break;
            case Constantes.CONTACTO:
                type = new TypeToken<Contacto>() {
                }.getType();
                break;
            case Constantes.AL_CONTACTOS:
                type = new TypeToken<ArrayList<Contacto>>() {
                }.getType();
                break;
            case Constantes.AL_RECURSOS_COMUNITARIOS:
                type = new TypeToken<ArrayList<RecursoComunitario>>() {
                }.getType();
                break;
            case Constantes.RELACION_USUARIO_CENTRO:
                type = new TypeToken<ArrayList<RelacionUsuarioCentro>>() {
                }.getType();
                break;
            case Constantes.AL_RELACION_TERMINAL_RECURSO_COMUNITARIO:
                type = new TypeToken<ArrayList<RelacionTerminalRecursoComunitario>>() {
                }.getType();
                break;
            case Constantes.AL_ALARMA:
                type = new TypeToken<ArrayList<Alarma>>() {
                }.getType();
                break;
            case Constantes.AL_CENTRO_SANITARIO_ALARMA:
                type = new TypeToken<ArrayList<CentroSanitarioEnAlarma>>() {
                }.getType();
                break;
            case Constantes.AL_TIPO_ALARMA:
                type = new TypeToken<ArrayList<TipoAlarma>>() {
                }.getType();
                break;
            case Constantes.AL_TERMINAL:
                type = new TypeToken<ArrayList<Terminal>>() {
                }.getType();
                break;
            case Constantes.AL_PACIENTE:
                type = new TypeToken<ArrayList<Paciente>>() {
                }.getType();
                break;
            case Constantes.ALARMA:
                type = new TypeToken<Alarma>() {
                }.getType();
                break;
            case Constantes.AL_CLASIFICACION_ALARMA:
                type = new TypeToken<ArrayList<ClasificacionAlarma>>() {
                }.getType();
                break;
            case Constantes.AL_PERSONAS_CONTACTO_EN_ALARMA:
                type = new TypeToken<ArrayList<PersonaContactoEnAlarma>>() {
                }.getType();
                break;
            case Constantes.AL_RECURSOS_COMUNITARIOS_EN_ALARMA:
                type = new TypeToken<ArrayList<RecursoComunitarioEnAlarma>>() {
                }.getType();
                break;
            case "Paciente":
                type = new TypeToken<Paciente>() {
                }.getType();
                break;
            case "RelacionPacientePersonaViewholder":
                type = new TypeToken<RelacionPacientePersona>() {
                }.getType();
                break;
            case "CentroSanitario":
                type = new TypeToken<CentroSanitario>() {
                }.getType();
                break;
            case "TipoCentroSanitario":
                type = new TypeToken<TipoCentroSanitario>() {
                }.getType();
                break;
            case "TipoRecursoComunitario":
                type = new TypeToken<TipoRecursoComunitario>() {
                }.getType();
                break;
            case "Direccion":
                type = new TypeToken<Direccion>() {
                }.getType();
                break;
            case "Persona":
                type = new TypeToken<Persona>() {
                }.getType();
                break;
            case "RelacionTerminalRecursoComunitario":
                type = new TypeToken<RelacionTerminalRecursoComunitario>() {
                }.getType();
                break;
            case "Terminal":
                type = new TypeToken<Terminal>() {
                }.getType();
                break;
            case "Usuario":
                type = new TypeToken<Usuario>() {
                }.getType();
                break;
            case "TipoModalidadPaciente":
                type = new TypeToken<TipoModalidadPaciente>() {
                }.getType();
                break;
            case "RecursoComunitario":
                type = new TypeToken<RecursoComunitario>() {
                }.getType();
                break;
            case "ArrayList<TipoCentroSanitario>":
                type = new TypeToken<ArrayList<TipoCentroSanitario>>() {
                }.getType();
                break;
            case "ArrayList<TipoRecursoComunitario>":
                type = new TypeToken<ArrayList<TipoRecursoComunitario>>() {
                }.getType();
                break;
            case "RelacionUsuarioCentro":
                type = new TypeToken<RelacionUsuarioCentro>() {
                }.getType();
                break;
            case "TipoVivienda":
                type = new TypeToken<TipoVivienda>() {
                }.getType();
                break;
        }
        if (type != null) {
            objeto = gson.fromJson(gson.toJson(lTM), type);
        }
        return objeto;
    }

    /*
     * Método para mostrar una capa de espera mientras se obtienen los datos de la API.
     *
     * @param view Vista
     */
    public static void generarCapaEspera(View view, ConstraintLayout dataConstraintLayout) {
        // Creamos una capa de espera
        ShimmerFrameLayout shimmerFrameLayout =
                (ShimmerFrameLayout) view.findViewById(R.id.listviewPlaceholder);
        // Obtenemos el layout con nuestros datos
        //ConstraintLayout dataConstraintLayout = (ConstraintLayout) view.findViewById(R.id.listViewDataPacientes);

        // Hacemos que la capa de datos se oculte para mostrarla cuando ya se hayan obtenido los datos.
        dataConstraintLayout.setVisibility(View.INVISIBLE);
        // Inicializamos la animación de la capa de espera
        shimmerFrameLayout.startShimmer();

        // Creando un nuevo objeto Handler para manejar la carga de datos.
        Handler handler = new Handler();

        // Un controlador que retrasa la ejecución del código dentro del método de ejecución durante
        // 2500 milisegundos.
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // Hacemos que la capa de datos se muestre
                dataConstraintLayout.setVisibility(View.VISIBLE);
                // Detenemos la animación de espera
                shimmerFrameLayout.stopShimmer();
                // Eliminamos la capa de espera
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }, 2500);
    }

    /*
     * Este método devuelve un String con Sí cuando le pasamos un true, y No si es false
     * @param condicion
     * @return
     */
    public static String trueSifalseNo(boolean condicion) {
        if (condicion) {
            return Constantes.SI;
        }
        return Constantes.NO;
    }

    /**
     * Este método de vuelve la fecha del sistema con el formato año-mes-día
     *
     * @return
     */
    public static String getStringFechaNowYYYYMMDD() {
        Date fecha;
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        fecha = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return sdf.format(fecha);
    }

    /* Este método crea un Dialogo de selección de fecha. Le asigna el valor a una variable y además
       escribe la fecha en el EditText. */
    public static void dialogoFecha(Context context, EditText editText) {
        DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String mes = String.valueOf(month + 1);
                ;
                String dia = String.valueOf(day);
                if (month < 9) {
                    mes = Constantes.CERO + String.valueOf(month + 1);
                }
                if (day < 10) {
                    dia = Constantes.CERO + String.valueOf(day);
                }

                editText.setText(year + Constantes.GUION + mes + Constantes.GUION + dia);
            }
        }, LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());
        dpd.show();
    }


    /**
     * Getters y Setters
     */

    public static Token getToken() {
        return token;
    }

    public static void setToken(Token token) {
        Utilidad.token = token;
    }

    public static Usuario getUserLogged() {
        return userLogged;
    }

    public static void setUserLogged(Usuario userLogged) {
        Utilidad.userLogged = userLogged;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean isAdmin) {
        Utilidad.isAdmin = isAdmin;
    }
}
