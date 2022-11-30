package com.example.teleappsistencia.ui.fragments.gestionAlarmasFragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.ClasificacionAlarma;
import com.example.teleappsistencia.modelos.Contacto;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Este fragment mostrará la información de la alarma que se quiere Gestionar.
 *
 * @author Jorge Luis Fernández Díaz
 * @version 22.05.2022
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoGestionAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoGestionAlarmaFragment extends Fragment {

    private Alarma alarma;
    private int color;
    private TextView textViewIdAlarma;
    private TextView textViewTipoAlarma;
    private TextView textViewClasificacionAlarma;
    private TextView textViewNumTerminal;
    private TextView textViewNombreyApellidos;
    private TextView textViewNumeroTelefono;
    private ListView listViewContactos;
    private Button btnGestionar;
    private int idAlarma;
    private String tipoAlarma;
    private String clasificacionAlarma;
    private int numTerminal;
    private String nombrePaciente;
    private String numeroTelefono;
    private ArrayList<Object> lContactos;
    private Terminal terminal;
    private Paciente paciente;


    public InfoGestionAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Esta es la forma por defecto de Android de crear una instancia de un fragment con parámetros.
     * Se le pasa el parámetro por el newInctance como argumento, y luego en el onCreate se recoge
     * ese argumento.
     *
     * @param alarma Alarma de la cual se va a mostrar la información
     * @return A new instance of fragment InfoGestionAlarmaFragment.
     */
    public static InfoGestionAlarmaFragment newInstance(Alarma alarma) {
        InfoGestionAlarmaFragment fragment = new InfoGestionAlarmaFragment();
        Bundle args = new Bundle();
        /* Recibimos la alarma en este método y la ponemos como argumentos */
        args.putSerializable(Constantes.ARG_ALARMA, alarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Si tenemos argumentos, los recogemos (comprobación por seguridad), sugerencia de Android Studio */
        if (getArguments() != null) {
            this.alarma = (Alarma) getArguments().getSerializable(Constantes.ARG_ALARMA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* Inflamos el layout del fragment */
        View view = inflater.inflate(R.layout.fragment_info_gestion_alarma, container, false);

        /* Capturar los elementos del layout */
        capturarElementos(view);

        /* Asignamos el listener a los botones */
        asignarListener();

        /* Extraemos y cargamos los datos */
        if(this.alarma != null){
            extraerDatos();
            cargarDatos();
        }

        return view;
    }


    /**
     * Caputramos los elementos del layout para poder usarlos.
     * @param view
     */
    private void capturarElementos(View view) {
        this.textViewIdAlarma = (TextView) view.findViewById(R.id.textViewIdAlarma);
        this.textViewTipoAlarma = (TextView) view.findViewById(R.id.textViewTipoAlarma);
        this.textViewClasificacionAlarma = (TextView) view.findViewById(R.id.textViewClasificacionAlarma);
        this.textViewNumTerminal = (TextView) view.findViewById(R.id.textViewNumTerminal);
        this.textViewNombreyApellidos = (TextView) view.findViewById(R.id.textViewNombreyApellidos);
        this.textViewNumeroTelefono = (TextView) view.findViewById(R.id.textViewNumeroTelefono);
        this.btnGestionar = (Button) view.findViewById(R.id.btnGestionar);
        this.listViewContactos = (ListView) view.findViewById(R.id.listViewContactos);

    }


    /**
     * Asignamos el onClick listener a los botones. En este caso al haber sólo un botón no considero
     * necesario implementar en la clase el OnClick Listener
     */
    private void asignarListener(){
        this.btnGestionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GestionAlarmaFragment gAF = GestionAlarmaFragment.newInstance(alarma, nombrePaciente, numeroTelefono, lContactos, paciente, terminal, color);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, gAF)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    /**
     * Este método extrae los datos de la  ya que nos llegó por parámetros al crear el fragment.
     */
    private void extraerDatos(){
        Persona persona;
        TipoAlarma tipoAlarma;
        ClasificacionAlarma clasificacionAlarma;

        /* Dependiendo de cómo se haya creado la alarma, habrá que extraer los datos a partir del Paciente
            o hacerlo a partir del Terminal. Además, en cada caso también cambia el color.
         */
        if(this.alarma.getId_paciente_ucr() != null){
            this.color = getResources().getColor(R.color.azul, getActivity().getTheme());
            this.paciente = (Paciente) Utilidad.getObjeto(this.alarma.getId_paciente_ucr(), Constantes.PACIENTE);
            this.terminal = (Terminal) Utilidad.getObjeto(paciente.getTerminal(), Constantes.TERMINAL);
        }
        else{
            this.color = getResources().getColor(R.color.verde, getActivity().getTheme());
            this.terminal = (Terminal) Utilidad.getObjeto(this.alarma.getId_terminal(), Constantes.TERMINAL);
            this.paciente = (Paciente) Utilidad.getObjeto(terminal.getTitular(), Constantes.PACIENTE);
        }
        persona = (Persona) Utilidad.getObjeto(paciente.getPersona(), Constantes.PERSONA);
        tipoAlarma = (TipoAlarma) Utilidad.getObjeto(this.alarma.getId_tipo_alarma(), Constantes.TIPOALARMA);
        clasificacionAlarma = (ClasificacionAlarma) Utilidad.getObjeto(tipoAlarma.getClasificacionAlarma(), Constantes.CLASIFICACION_ALARMA);

        this.idAlarma = this.alarma.getId();
        this.tipoAlarma = tipoAlarma.getNombre() + Constantes.ESPACIO_PARENTESIS_AP + tipoAlarma.getCodigo() + Constantes.PARENTESIS_CIERRE;
        this.clasificacionAlarma = clasificacionAlarma.getNombre() + Constantes.ESPACIO_PARENTESIS_AP + clasificacionAlarma.getCodigo() + Constantes.PARENTESIS_CIERRE;
        this.numTerminal = terminal.getId();
        this.nombrePaciente = persona.getNombre() + Constantes.ESPACIO + persona.getApellidos();
        this.numeroTelefono = persona.getTelefonoMovil() + Constantes.SLASH + persona.getTelefonoFijo();
        cargarDatos();
        extraerContactos(paciente.getId());
    }


    /**
     * Cargamos los datos que extrajimos previamente en los elementos del layout.
     */
    private void cargarDatos(){
        ColorStateList csl = ColorStateList.valueOf(this.color);
        this.btnGestionar.setBackgroundTintList(csl);

        this.textViewIdAlarma.setText(Constantes.ID_ALARMA_DP_SP + this.idAlarma);
        this.textViewTipoAlarma.setText(Constantes.TIPO_DP_SP + this.tipoAlarma);
        this.textViewClasificacionAlarma.setText(Constantes.CLASIFICACION_DP_SP + this.clasificacionAlarma);
        this.textViewNumTerminal.setText(Constantes.TERMINAL_DP_SP + this.numTerminal);
        this.textViewNombreyApellidos.setText(Constantes.PACIENTE_DP_SP + this.nombrePaciente);
        this.textViewNumeroTelefono.setText(Constantes.TELEFONO_DP_SP + this.numeroTelefono);
    }


    /**
     * Este método hace una petición a la API REST para recuperar los contactos relacionados con el
     * paciente. Los carga en una lista.
     * @param idPaciente
     */
    private void extraerContactos(int idPaciente) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getContactosbyIdPaciente(idPaciente, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    lContactos = (ArrayList<Object>) response.body();
                    cargarListViewContactos(lContactos);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_NO_CONTACTOS, Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método carga en un List View los datos de los contactos del paciente
     * @param lContactos
     */
    private void cargarListViewContactos(List<Object> lContactos){
        List<String> lDatosContactos = new ArrayList<>();
        Contacto contacto;
        Persona persona;
        String datoContacto;

        /* Si la lista está vacía, cargamos un único elemento que indicará que no hay contactos */
        if(lContactos.isEmpty()){
            lDatosContactos.add(Constantes.NO_HAY_CONTACTOS);
        }

        /* Si la lista contiene contactos, ponemos sus datos */
        else{
            for(Object object : lContactos){
                contacto = (Contacto) Utilidad.getObjeto(object, Constantes.CONTACTO);
                persona = (Persona) Utilidad.getObjeto(contacto.getPersonaEnContacto(), Constantes.PERSONA);
                datoContacto = persona.getNombre() + Constantes.ESPACIO + persona.getApellidos() + Constantes.ESPACIO_PARENTESIS_AP + contacto.getTipo_relacion()+ Constantes.PARENTESIS_CIERRE;
                lDatosContactos.add(datoContacto);
            }
        }

        /* Ponemos el adpater a la lista */
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,  lDatosContactos);
        this.listViewContactos.setAdapter(adapter);
    }
}