package com.example.teleappsistencia.ui.fragments.gestionAlarmasFragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
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

    private static final String ARG_ALARMANOTIFICADA = "Alarma";
    private Alarma alarmaNotificada;
    private TextView textViewIdTerminal;
    private TextView textViewNombrePaciente;
    private TextView textViewTelefono;
    private Button btnRechazarAlarma;
    private Button btnAceptarAlarma;
    private ImageButton imageButtonCerrarAlerta;
    private ConstraintLayout cabeceraAlerta;
    private int color;
    private int numTerminal;
    private String nombrePaciente;
    private String numeroTelefono;


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
        this.textViewIdTerminal = (TextView) view.findViewById(R.id.textViewIdTerminal);
        this.textViewNombrePaciente = (TextView) view.findViewById(R.id.textViewNombrePaciente);
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
        Terminal terminal;
        Paciente paciente;
        Persona persona;

        /* Dependiendo de cómo se haya creado la alarma, habrá que extraer los datos a partir del Paciente
            o hacerlo a partir del Terminal. Además, en cada caso también cambia el color.
         */
        if(alarmaNotificada.getId_paciente_ucr() != null){
            this.color = getResources().getColor(R.color.azul, getActivity().getTheme());
            paciente = (Paciente) Utilidad.getObjeto(alarmaNotificada.getId_paciente_ucr(), Constantes.PACIENTE);
            terminal = (Terminal) Utilidad.getObjeto(paciente.getTerminal(), Constantes.TERMINAL);
        }
        else{
            this.color = getResources().getColor(R.color.verde, getActivity().getTheme());
            terminal = (Terminal) Utilidad.getObjeto(alarmaNotificada.getId_terminal(), Constantes.TERMINAL);
            paciente = (Paciente) Utilidad.getObjeto(terminal.getTitular(), Constantes.PACIENTE);
        }
        persona = (Persona) Utilidad.getObjeto(paciente.getPersona(), Constantes.PERSONA);
        this.numTerminal = terminal.getId();
        this.nombrePaciente = persona.getNombre()+Constantes.ESPACIO+persona.getApellidos();
        this.numeroTelefono = persona.getTelefonoMovil() + Constantes.SLASH + persona.getTelefonoFijo();
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
        this.textViewIdTerminal.setText(Constantes.ID_TERMINAL_DP_SP + String.valueOf(this.numTerminal));
        this.textViewNombrePaciente.setText(Constantes.PACIENTE_DP_SP + this.nombrePaciente);
        this.textViewTelefono.setText(Constantes.TELEFONO_DP_SP + this.numeroTelefono);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAceptarAlarma:
                comprobarAlarma();
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
                        Toast.makeText(getContext(), Constantes.ERROR_ALARMA_YA_ASIGNADA, Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Alarma> callAlarma, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
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
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarAlarma(alarmaRecibida.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), alarmaRecibida);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    InfoGestionAlarmaFragment iGAF = InfoGestionAlarmaFragment.newInstance(alarmaRecibida);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, iGAF)
                            .addToBackStack(null)
                            .commit();
                    dismiss();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_CARGAR_DATOS, Toast.LENGTH_LONG).show();
            }
        });
    }
}