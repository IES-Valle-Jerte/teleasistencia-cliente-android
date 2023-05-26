package com.example.teleappsistencia.ui.fragments.gestionAlarmasFragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Contacto;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Este fragment será el que se carge como AlertDialog personalizado cuando se notifique una nueva
 * alarma.
 *
 * @author Jorge Luis Fernández Díaz
 * @version 22.05.2022
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmAlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmAlertFragment extends DialogFragment implements View.OnClickListener {

    private Context context;
    private static final String ARG_ALARMANOTIFICADA = "Alarma";
    private Alarma alarmaNotificada;

    private TextView textViewTipoAlarma;
    private TextView textViewNombrePaciente;

    private TextView textViewApellidosPaciente;
    private TextView textViewTelefono;
    private Button btnRechazarAlarma;
    private Button btnAceptarAlarma;
    private ImageButton imageButtonCerrarAlerta;
    private ConstraintLayout cabeceraAlerta;
    private int color;

    private Paciente paciente;
    private String tipoAlarma;
    private String nombrePaciente;

    private String apellidosPaciente;
    private String numeroTelefono;

    private Terminal terminal;

    private List<Contacto> lContactosPrueba;


    public AlarmAlertFragment() {
        // Required empty public constructor
    }

    /**
     * Esta es la forma por defecto de Android de crear una instancia de un fragment con parámetros.
     * Se le pasa el parámetro por el newInctance como argumento, y luego en el onCreate se recoge
     * ese argumento.
     *
     * @param alarma que se ha creado
     * @return A new instance of fragment AlarmAlertFragment.
     */
    public static AlarmAlertFragment newInstance(Alarma alarma) {
        AlarmAlertFragment fragment = new AlarmAlertFragment();
        Bundle args = new Bundle();
        /* Recibimos la alarma en este método y la ponemos como argumento */
        args.putSerializable(ARG_ALARMANOTIFICADA, alarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Si tenemos argumentos, los recogemos (comprobación por seguridad), sugerencia de Android Studio */
        if (getArguments() != null) {
            alarmaNotificada = (Alarma) getArguments().getSerializable(ARG_ALARMANOTIFICADA);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* Inflamos el layout del fragment */
        View view = inflater.inflate(R.layout.fragment_alarm_alert, container, false);

        this.context = view.getContext();

        /* Capturar los elementos del layout */
        capturarElementos(view);

        /* Asignamos el listener a los botones */
        asignarListener();

        /* Extraemos y cargamos los datos */
        if(alarmaNotificada != null){
            this.extraerDatos();
            this.cargarDatos();
        }


        return view;
    }


    /**
     * Caputramos los elementos del layout para poder usarlos.
     * @param view
     */
    private void capturarElementos(View view){
        this.textViewTipoAlarma = (TextView) view.findViewById(R.id.textViewTipoAlarmaAlerta);
        this.textViewNombrePaciente = (TextView) view.findViewById(R.id.textViewNombrePaciente);
        this.textViewApellidosPaciente = (TextView) view.findViewById(R.id.textViewApellidosPaciente);
        this.textViewTelefono = (TextView) view.findViewById(R.id.textViewTelefono);
        this.btnRechazarAlarma = (Button) view.findViewById(R.id.btnRechazarAlarma);
        this.btnAceptarAlarma = (Button) view.findViewById(R.id.btnAceptarAlarma);
        this.imageButtonCerrarAlerta = (ImageButton) view.findViewById(R.id.imageButtonCerrarAlerta);
        this.cabeceraAlerta = (ConstraintLayout) view.findViewById(R.id.cabeceraAlerta);

    }

    /**
     * Asignamos el onClick listener a los botones. La clase implementa View.OnClickListener, el método
     * onClick está implementado más abajo.
     */
    private void asignarListener(){
        this.btnRechazarAlarma.setOnClickListener(this);
        this.btnAceptarAlarma.setOnClickListener(this);
        this.imageButtonCerrarAlerta.setOnClickListener(this);
    }


    /**
     * Este método extrae los datos de la nueva alarma que ha sido notificada, ya que nos llegó por
     * parámetros al crear el fragment.
     */
    private void extraerDatos(){
        TipoAlarma tipoAlarma_;
        Persona persona;

        /* Dependiendo de cómo se haya creado la alarma, habrá que extraer los datos a partir del Paciente
            o hacerlo a partir del Terminal. Además, en cada caso también cambia el color.
         */
        if(alarmaNotificada.getId_paciente_ucr() != null){
            this.color = getResources().getColor(R.color.azul, getActivity().getTheme());
            paciente = (Paciente) Utilidad.getObjeto(alarmaNotificada.getId_paciente_ucr(), Constantes.PACIENTE);
            terminal = (Terminal) Utilidad.getObjeto(paciente.getTerminal(), Constantes.TERMINAL);
            tipoAlarma_ = (TipoAlarma) Utilidad.getObjeto(alarmaNotificada.getId_tipo_alarma(), Constantes.TIPO_ALARMA);

        }
        else{
            this.color = getResources().getColor(R.color.verde, getActivity().getTheme());
            terminal = (Terminal) Utilidad.getObjeto(alarmaNotificada.getId_terminal(), Constantes.TERMINAL);
            paciente = (Paciente) Utilidad.getObjeto(terminal.getTitular(), Constantes.PACIENTE);
            tipoAlarma_ = (TipoAlarma) Utilidad.getObjeto(alarmaNotificada.getId_tipo_alarma(), Constantes.TIPO_ALARMA);
        }
        persona = (Persona) Utilidad.getObjeto(paciente.getPersona(), Constantes.PERSONA);
        this.tipoAlarma = tipoAlarma_.getNombre();
        this.nombrePaciente = persona.getNombre();
        this.apellidosPaciente = persona.getApellidos();
        this.numeroTelefono = persona.getTelefonoMovil() + Constantes.SLASH + persona.getTelefonoFijo();

        extraerContactos();
    }

    /**
     * Cargamos los datos que extrajimos previamente en los elementos del layout.
     */
    private void cargarDatos(){
        //Color de botones y la cabecera
        ColorStateList csl = ColorStateList.valueOf(this.color);
        this.cabeceraAlerta.setBackgroundColor(this.color);
        this.btnRechazarAlarma.setBackgroundTintList(csl);
        this.btnAceptarAlarma.setBackgroundTintList(csl);

        //Datos que se muestran
        this.textViewTipoAlarma.setText(Constantes.TIPO_ALARMA_DP_SP + this.tipoAlarma);
        this.textViewNombrePaciente.setText(Constantes.NOMBRE_DP_SP + this.nombrePaciente);
        this.textViewApellidosPaciente.setText(Constantes.APELLIDOS_DP_SP + this.apellidosPaciente);
        this.textViewTelefono.setText(Constantes.TELEFONO_DP_SP + this.numeroTelefono);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAceptarAlarma:
                comprobarAlarma();
                gestionarAlarma();
                break;
            case R.id.btnRechazarAlarma:
                    this.dismiss();
                break;
            case R.id.imageButtonCerrarAlerta:
                    this.dismiss();
                break;
        }
    }

    /**
     * Este método llama a la API REST para traer la Alarma. Cuando el usuario haga click en el botón
     * ACEPTAR, se le va a asignar la alarma (cargando su ID de teleoperador en ella) y se va a persistir
     * en la base de datos. Pero previamente HAY QUE COMPROBAR QUE NIGÚN OTRO TELEOPERADOR FUE MÁS
     * RÁPIDO Y SE ASIGNÓ LA ALARMA ANTES. Aquí se hace esta comprobación.
     */
    private void comprobarAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Alarma> callAlarma = apiService.getAlarmabyId(this.alarmaNotificada.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        callAlarma.enqueue(new Callback<Alarma>() {
            @Override
            public void onResponse(Call<Alarma> callAlarma, Response<Alarma> response) {
                if(response.isSuccessful()){
                    // Traemos la alarma recibida y sacamos el teleoperador
                    Alarma alarmaRecibida = response.body();
                    Teleoperador teleoperador = (Teleoperador) Utilidad.getObjeto(alarmaRecibida.getId_teleoperador(), Constantes.TELEOPERADOR);
                    // Si el teleoperador es nulo, podemos continuar con la modificación (donde se le asignará el ID de Teleoperador)
                    if(teleoperador == null){
                        modificarAlarma(alarmaRecibida);
                    }else{
                        Toast.makeText(context, Constantes.ERROR_ALARMA_YA_ASIGNADA, Toast.LENGTH_LONG).show();
                    }

                    extraerContactos();
                } else{
                    Toast.makeText(context, Constantes.ERROR_, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Alarma> callAlarma, Throwable t) {
                Toast.makeText(context, Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Finalmente, este método realiza la petición PUT a la API REST para modificar la alarma. La modificación
     * consiste en ponerle el id_teleoperador que corresponda al usuario de la aplicación.
     * @param alarmaRecibida
     */
    private void modificarAlarma(Alarma alarmaRecibida){
        /* Siempre que hagamos un PUT tenemos que darle a la petición los datatos de la forma
           que requiere. En este caso, idTeleoperador SIEMPRE tiene que ser un intger. */
        alarmaRecibida.setId_teleoperador(Utilidad.getUserLogged().getPk());
    }


    /**
     * Este método muestra el la ventana de atender la alarma por el teleoperador
     */
    public void gestionarAlarma(){
        Bundle bundle = new Bundle();
        // se pasan los parametros
        bundle.putSerializable(Constantes.ARG_AL_CONTACTO, (Serializable) lContactosPrueba);
        bundle.putSerializable(Constantes.ARG_ALARMA, alarmaNotificada);
        bundle.putSerializable(Constantes.ARG_PACIENTE, paciente);
        bundle.putSerializable(Constantes.ARG_TERMINAL, terminal);
        bundle.putSerializable(Constantes.ARG_COLOR, color);

        GestionAlarmaFragment gestionAlarmaFragment = new GestionAlarmaFragment();
        gestionAlarmaFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, gestionAlarmaFragment)
                .addToBackStack(null)
                .commit();


        dismiss();
    }

    public void extraerContactos(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getContactosbyIdPaciente(paciente.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                List<Object> lObjectAux;
                if(response.isSuccessful()){
                    lObjectAux = response.body();
                    lContactosPrueba = (ArrayList<Contacto>) (Utilidad.getObjeto(lObjectAux, Constantes.AL_CONTACTOS));
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}