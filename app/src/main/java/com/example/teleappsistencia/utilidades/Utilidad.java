package com.example.teleappsistencia.utilidades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.websocket.AlarmaWebSocketListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
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
     * Si isSuperUser es true, se ignorará el valor de isAdmin.
     * Si isAdmin está en true significa que el usuario es Profesor (group) y tendrá acceso a algunos menús de administración adicional.
     * Si es false algunas opciones de administración se ocultarán, y se ocultarán todas si el usuario no es ni Profesor (Admin) ni Administrador (SuperUser).
     *
     * @see #isAdmin()
     * @see #isSuperUser()
     */
    private static boolean isAdmin;

    /**
     * Si isSuperUser está en true significa que el usuario es Administrador (group) y podrá acceder a todas las opciones.
     * Si es false algunas opciones de administración se ocultarán si isAdmin == true, y se ocultarán todas si el usuario no
     * es ni Profesor (Admin) ni Administrador (SuperUser).
     *
     * @see #isAdmin()
     * @see #isSuperUser()
     */
    private static boolean isSuperUser;

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
    public static Object getObjeto(Object lTM, String tipo){
        Gson gson = new Gson();
        Type type = null;
        Object objeto = null;

        boolean isLista=false; // Indica si el tipo pasado como objeto es una lista o no

        // Este bloque de código extrae la clase del String tipo en caso de que este sea un ArrayList
        Pattern pattern = Pattern.compile("<(\\w+)>"); // Buscar la subcadena entre '<' y '>'
        Matcher matcher = pattern.matcher(tipo); // Comprobar que existe la cadena a buscar
        String subcadena=tipo; // Cadena resultante con la clase
        if (matcher.find()) { // Si se a encontrado que tipo cumple con ArrayList<clase>
            isLista=true; // tipo es una lista
            subcadena= matcher.group(1); // Obtener la subcadena con la clase
        }
        // En caso de que que tipo solo contenga el nombre de la calse y no un ArrayList subacadena sera igual a tipo

        Class<?> clazz = null;
        String ruta=Constantes.RUTA_MODELOS+subcadena;
        try {
            clazz = Class.forName(ruta); // Creamos una clase con la ruta indicada
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clazz != null) {
            // Si la clase existe se comprueba si es una lista o un objeto
            if(isLista){
                // En caso de lista
                type = TypeToken.getParameterized(ArrayList.class, clazz).getType();
            }else{
                // En caso de objeto
                type = TypeToken.getParameterized(clazz).getType();
            }

        }
        if (type != null) {
            // Si type tiene algun valor se obtiene el objeto buscado
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

    // ! ================================== IMAGENES CON PICASSO ===================================
    // Mediante urlImagen
    /**
     * @see Utilidad#cargarImagen(String, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(String urlImagen, ImageView imageView) {
        return cargarImagen(urlImagen, imageView, null, null);
    }

    /**
     * @see Utilidad#cargarImagen(String, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(String urlImagen, ImageView imageView, Integer radiousDP) {
        return cargarImagen(urlImagen, imageView, radiousDP, null);
    }

    /**
     * Implementación del método cargarImagen para cargar imágenes con Picasso mediante un ResourceID.
     *
     * @param {@link Integer} representando el Resource ID del drawable.
     * @param imageView ImageView en el que insertará la imagen.
     * @param radiousDP [Opcional] Radio del borde para bordes redondeados, no afecta si 0 o null.
     * @param marginDP [Opcional] wMargen respecto al borde, no afecta si 0 o null.
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(String urlImagen, ImageView imageView, Integer radiousDP, Integer marginDP) {
        return cargarImagen((Object) urlImagen, imageView, radiousDP, marginDP);
    }

    // Mediante Resource ID de la imagen
    /**
     * @see Utilidad#cargarImagen(Integer, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(Integer resourceIdImagen, ImageView imageView) {
        return cargarImagen(resourceIdImagen, imageView, null, null);
    }
    /**
     * @see Utilidad#cargarImagen(Integer, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(Integer resourceIdImagen, ImageView imageView, Integer radiousDP) {
        return cargarImagen(resourceIdImagen, imageView, radiousDP, null);
    }
    /**
     * Implementación del método cargarImagen para cargar imágenes con Picasso mediante un ResourceID.
     *
     * @param {@link Integer} representando el Resource ID del drawable.
     * @param imageView ImageView en el que insertará la imagen.
     * @param radiousDP [Opcional] Radio del borde para bordes redondeados, no afecta si 0 o null.
     * @param marginDP [Opcional] wMargen respecto al borde, no afecta si 0 o null.
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(Integer resourceIdImagen, ImageView imageView, Integer radiousDP, Integer marginDP) {
        return cargarImagen((Object) resourceIdImagen, imageView, radiousDP, marginDP);
    }

    // Mediante File de la imagen
    /**
     * @see Utilidad#cargarImagen(File, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(File fileImagen, ImageView imageView) {
        return cargarImagen(fileImagen, imageView, null, null);
    }

    /**
     * @see Utilidad#cargarImagen(File, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(File fileImagen, ImageView imageView, Integer radiousDP) {
        return cargarImagen(fileImagen, imageView, radiousDP, null);
    }

    /**
     * Implementación del método cargarImagen para cargar imágenes con Picasso mediante un ResourceID.
     *
     * @param fileImagen {@link File} File de una imagen.
     * @param imageView ImageView en el que insertará la imagen.
     * @param radiousDP [Opcional] Radio del borde para bordes redondeados, no afecta si 0 o null.
     * @param marginDP [Opcional] wMargen respecto al borde, no afecta si 0 o null.
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(File fileImagen, ImageView imageView, Integer radiousDP, Integer marginDP) {
        return cargarImagen((Object) fileImagen, imageView, radiousDP, marginDP);
    }

    // Mediante Uri de la imagen
    /**
     * @see Utilidad#cargarImagen(Uri, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(Uri uriImagen, ImageView imageView) {
        return cargarImagen(uriImagen, imageView, null, null);
    }

    /**
     * @see Utilidad#cargarImagen(Uri, ImageView, Integer, Integer)
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(Uri uriImagen, ImageView imageView, Integer radiousDP) {
        return cargarImagen(uriImagen, imageView, radiousDP, null);
    }

    /**
     * Implementación del método cargarImagen para cargar imágenes con Picasso mediante un ResourceID.
     *
     * @param uriImagen {@link Uri} representando la URL de la imagen para descargarla.
     * @param imageView ImageView en el que insertará la imagen.
     * @param radiousDP [Opcional] Radio del borde para bordes redondeados, no afecta si 0 o null.
     * @param marginDP [Opcional] wMargen respecto al borde, no afecta si 0 o null.
     * @return Devuelve true o false dependiendo de si ha habido un problema al cargar la imagen.
     */
    public static boolean cargarImagen(Uri uriImagen, ImageView imageView, Integer radiousDP, Integer marginDP) {
        return cargarImagen((Object) uriImagen, imageView, radiousDP, marginDP);
    }

    // Implementación
    /**
     * Implementación del método cargarImagen para reutilizar código y poder cargar imágenes de todas
     * las formas que Picasso puede cargarlas.
     *
     * @param imagen Uno de los sigieintes:
     *      {@link Integer} representando el Resource ID del drawable,
     *      {@link String}/{@link Uri} representando la URL del fichero para descargarla,
     *      o un {@link File} referenciando al fichero de la imagen.
     *
     * @param imageView ImageView en el que insertará la imagen
     * @param radiousDP [Opcional] Radio del borde para bordes redondeados, no afecta si 0 o null
     * @param marginDP [Opcional] wMargen respecto al borde, no afecta si 0 o null
     */
    private static boolean cargarImagen(Object imagen, ImageView imageView, Integer radiousDP, Integer marginDP) {
        if (imagen != null && imageView != null) {
            OkHttpClient client; Picasso picasso;

            // Inicializar picaso para acceso web
            if (imagen instanceof String || imagen instanceof Uri) {
                client = new OkHttpClient();
                picasso = new Picasso.Builder(imageView.getContext())
                        .downloader(new OkHttp3Downloader(client)).build();
            // Inicializar picaso para acceso a recurso local
            } else if (imagen instanceof Integer || imagen instanceof File) {
                picasso = new Picasso.Builder(imageView.getContext()).build();

            // Si la imagen es de cualquier otro tipo no soportado, nos detenemos
            } else return false;

            // Comprobaciones para los campos opcionales (y hacer paso de DP a Pixeles)
            int radious = pasarDPaPX(radiousDP, imageView);
            int margin = pasarDPaPX(marginDP, imageView);

            // Cargar imagen en el ImageView (metodo para simplificar los imputs)
            picassoLoad(picasso, imagen)
                .fit().centerCrop()
                // Redondear si es necesario
                .transform(new RoundedCornersTransformation(radious, margin))
                // Placeholder mientras carga e Imagen si no carga bien
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.drawable.ic_menu_gallery)
                // View donde cargar la imagen final
                .into(imageView);

            return true;
        // Si la imagen o el imageView es nulo, no podemos cargar ninguna imagen.
        } else return false;
    }

    private static RequestCreator picassoLoad(Picasso picasso, Object imagen) {
        if (imagen instanceof String) {
            return picasso.load((String) imagen);
        } else if (imagen instanceof Integer) {
            return picasso.load((Integer) imagen);
        } else if (imagen instanceof Uri) {
            return picasso.load((Uri) imagen);
        } else if (imagen instanceof File) {
            return picasso.load((File) imagen);
        }

        return null;
    }

    /**
     * Función que hace el paso de unidades en Density Independent Pixels (DP) a Pixels (PX).
     * Util para hacer que ciertos valores como los redondeos de las imágenes se vean igual en todas las pantallas.
     *
     * @param densityPixels La cantidad en DP.
     * @param vistaDeReferencia Una View cualquiera proveniente del layout en el que vaya a ser usado el valor calculado.
     * @return La cantidad de DP pasada a PX.
     */
    public static int pasarDPaPX(Integer densityPixels, View vistaDeReferencia) {
        if (null == densityPixels) return 0;
        else return (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, densityPixels,
            // Sacar dimensiones de pantalla
            vistaDeReferencia.getContext().getResources().getDisplayMetrics()
        );
    }

    /**
     * Verifica si el contenido de un EditText es una dirección de correo y es válida.
     * @param editText EditText a comprobar
     * @return Devolverá true solo si es una dirección de correo válida. O el campo está vacío.
     */
    public static boolean validarFormatoEmail(EditText editText) {
        // Sacamos el texto sin espacios sobrantes
        CharSequence input = editText.getText().toString().trim();
        // Si está vacio o cumple el regex, el email es valido
        return TextUtils.isEmpty(input) || Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    /**
     * Verifica si el contenido de un EditText es una contraseña válida.
     *
     * <ul><b>Condiciones de validación</b>
     *     <li>[SERVIDOR] No puede ser demasiado similar a otra información personal.</li>
     *     <li>[SERVIDOR] No puede ser una contraseña comúnmente utilizadaTener al menos 8 caracteres.</li>
     *     <li>Tener al menos 8 caracteres.</li>
     *     <li>No puede ser completamente numérica.</li>
     *     <li>Los campos no pueden estar vacios.</li>
     * </ul>
     *
     * <b>NOTA</b>: Ciertos cambios en la política de validación del servidor pueden hacer
     * que la contraseña se considere invalida igualmente.
     *
     * @param editText EditText a comprobar
     * @return Devolverá true solo si es una contraseña valida.
     */
    public static boolean validatePassword(EditText editText) {
        final Pattern pattern = Pattern.compile(Constantes.TOAST_MODPERFIL_CAMBIOPASS_REGEX);
        String password = editText.getText().toString().trim();

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    /**
     * Crea un fichero temporal a partir de un recurso local.
     * Este recurso local será obtenido normalmente mediante un file picker Intent o similar.
     *
     * NOTA: El fichero temporal se almacenará en la Caché de la aplicación y se eliminará cuando
     * se cierre la aplicación, ya que está marcado con y está marcado con {@link File#deleteOnExit()}.
     *
     * @param uriRecursoLocal {@link Uri} al recurso local. Vease un Fichero convencional o una Imagen.
     * @return {@link File} temporal con el contenido del recurso local. Valido hasta que se cierre la Aplicación.
     *
     * @throws SecurityException EACCES (Permission denied) en caso de que no se tengan los permisos necesarios.
     * @see File#deleteOnExit()
     */
    public static File extraerFicheroTemporal(Context context, Uri uriRecursoLocal) throws SecurityException {
        File tempFile = null;
        InputStream is = null; FileOutputStream fos = null;

        // No hacer nada si los parametros son nulos
        if (null == context || null == uriRecursoLocal) return null;

        try {
            // Abrir el recurso y hacer dump a un fichero temporal (almacenado en caché)
            is = context.getContentResolver().openInputStream(uriRecursoLocal);
            tempFile = File.createTempFile("temp", null, context.getCacheDir());
            fos = new FileOutputStream(tempFile);

            // Buffer para leer los datos
            int length; byte[] buffer = new byte[1024]; // 1MB

            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();

        // Devolver la excepción de falta de permisos
        } catch (SecurityException eacces) {
            throw eacces;

        // Si hay problema de lectura/escritura de otro tipo se devuelve un fichero nulo.
        } catch (IOException ioe) {
            tempFile = null;

        // Cerrar los Streams y marcar el fichero para que sea borrado.
        } finally {
            try {
                if (fos != null) fos.close();
                if (is != null)  is.close();
                // Marcar el fichero temporal para que se borre cuando la app se cierre
                if (tempFile != null)  tempFile.deleteOnExit();
            } catch (IOException ex) {}
        }

        return tempFile;
    }

    /**
     * Alterna la visibilidad de una View entre {@link View#VISIBLE} y {@link View#GONE}
     * @param v
     */
    public static void alternarVista(View v) {
        if (View.VISIBLE == v.getVisibility()) v.setVisibility(View.GONE);
        else v.setVisibility(View.VISIBLE);
    }

    /**
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

    /**
     * Este método de vuelve un datetime devuelta por la API al formato: dd/MM/yyyy @ HH:mm:ss
     *
     * @return null si no es posible dar formato al datetime
     */
    public static String getStringDatetime(String datetimeApi) {
        // Tratar casos erroneos
        if (null == datetimeApi || datetimeApi.trim().isEmpty()) return null;

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault());
            return getStringDatetime(inFormat.parse(datetimeApi));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método de vuelve un datetime devuelta por la API al formato: dd/MM/yyyy @ HH:mm:ss
     *
     * @return null si no es posible dar formato al datetime
     */
    public static String getStringDatetime(Date datetimeApi) {
        // Tratar casos erroneos
        if (null == datetimeApi) return null;

        SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss", Locale.getDefault());
        return outFormat.format(datetimeApi);
    }

    /**
     * Este método de vuelve un datetime devuelta por la API al formato: dd/MM/yyyy
     *
     * @return null si no es posible dar formato al datetime
     */
    public static String getStringDate(Date datetimeApi) {
        // Tratar casos erroneos
        if (null == datetimeApi) return null;

        SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return outFormat.format(datetimeApi);
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

    public static String getAuthorization() {
        return Constantes.TOKEN_BEARER + token.getAccess();
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

    /**
     * Los Profesores y Administradores (usuarios de los grupos) son ambos admin.
     * @return si el usuario es administrador o superusuario.
     */
    public static boolean isAdmin() {
        return isAdmin || isSuperUser;
    }

    public static boolean isSuperUser() {
        return isSuperUser;
    }

    public static Grupo getGrupoUser(Usuario user) {
        // Sacamos la lista de grupos y como solo debe contener 1, lo devolvemos como grupo
        List<Grupo> gruposList = (ArrayList) user.getGroups();
        return null == gruposList || gruposList.isEmpty() ? null
                : (Grupo) Utilidad.getObjeto(gruposList.get(0), Constantes.GRUPO);
    }

    public static boolean isAdmin(Usuario user) {
        Grupo grupo = getGrupoUser(user);
        return null != grupo
            && (grupo.getName().equalsIgnoreCase(Constantes.PROFESOR)
            || grupo.getName().equalsIgnoreCase(Constantes.ADMINISTRADOR));
    }

    public static boolean isSuperUser(Usuario user) {
        Grupo grupo = getGrupoUser(user);
        return null != grupo
            && grupo.getName().equalsIgnoreCase(Constantes.ADMINISTRADOR);
    }

    public static void setIsAdmin(boolean isAdmin) {
        Utilidad.isAdmin = isAdmin;
    }

    public static void setIsSuperUser(boolean isSuperUser) {
        Utilidad.isSuperUser = isSuperUser;
    }
}
