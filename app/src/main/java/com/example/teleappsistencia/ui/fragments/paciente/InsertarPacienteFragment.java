package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
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
 * Una clase {@link Fragment} para recoger los datos para insertar un Paciente nuevo.
 * <p> Esta clase es una subclase de {@link Fragment} y hereda de ella todos sus métodos y atributos.
 */
public class InsertarPacienteFragment extends Fragment implements View.OnClickListener {

    /**
     * El paciente que se va a consultar.
     */
    private Paciente paciente;

    // Atributos de la interfaz de usuario (UI) del fragment.
    private Spinner spinnerTerminal;
    private Spinner spinnerPersona;
    private Spinner spinnerTipoModalidadPaciente;
    private Spinner spinnerTipoModalidadPacienteInsertar;
    private EditText editTextTieneUCR;
    private EditText editTextNumeroExpediente;
    private EditText editTextNumeroSeguridadSocial;
    private EditText editTextPrestacionOtrosServicios;
    private EditText editTextObservacionesMedicas;
    private EditText editTextInteresesActividades;
    private Button btnInsertarPaciente;
    private Button btnVolverPacienteInsertar;

    //Atributos de la clase.
    private Paciente pacienteInsertar;

    // Constructor por defecto.
    public InsertarPacienteFragment() {
    }

    /**
     * Método que crea una instancia de la clase.
     */
    public static InsertarPacienteFragment newInstance() {
        InsertarPacienteFragment fragment = new InsertarPacienteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     *
     * @param savedInstanceState Si la actividad se reinicializa después de cerrarse previamente, este
     *                           paquete contiene los datos que suministró más recientemente en onSaveInstanceState(Bundle).
     *                           Nota: De lo contrario es nulo.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * La función onCreateView() se llama cuando se crea el fragmento
     *
     * @param inflater           El objeto LayoutInflater que se puede usar para inflar cualquier vista en el
     *                           fragmento,
     * @param container          La vista principal a la que se debe adjuntar la interfaz de usuario del
     *                           fragmento.
     * @param savedInstanceState Un objeto Bundle que contiene el estado guardado previamente de la
     *                           actividad. Si la actividad nunca ha existido antes, el valor del objeto Bundle es nulo.
     * @return La vista del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_insertar_paciente, container, false);
        pacienteInsertar = new Paciente();
        // Inicialización de los atributos de la interfaz de usuario (UI).
        obtenerComponentes(root);
        // Inicialización de los Spinners del formulario
        inicializarSpinnerTipoModalidadPaciente();
        inicializarSpinnerTerminal();
        inicializarSpinnerPersona();
        return root;
    }

    private void inicializarSpinnerTipoModalidadPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<TipoModalidadPaciente>> call = apiService.getListadoTipoModalidadPaciente("Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoModalidadPaciente>>() {
            @Override
            public void onResponse(Call<List<TipoModalidadPaciente>> call, Response<List<TipoModalidadPaciente>> response) {
                if (response.code() == 200) {
                    List<TipoModalidadPaciente> listadoTipoModalidadPaciente = response.body();
                    if (listadoTipoModalidadPaciente != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaTipoModalidadPaciente(listadoTipoModalidadPaciente));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTipoModalidadPaciente.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TipoModalidadPaciente>> call, Throwable t) {

            }
        });
    }

    private List<String> convertirListaTipoModalidadPaciente(List<TipoModalidadPaciente> listadoTipoModalidadPaciente) {
        List<String> listadoString = new ArrayList<>();
        for (TipoModalidadPaciente tipoModalidadPaciente : listadoTipoModalidadPaciente) {
            listadoString.add(tipoModalidadPaciente.getId() + "-" + tipoModalidadPaciente.getNombre());
        }
        return listadoString;
    }

    /**
     * Método que obtiene los componentes de la interfaz de usuario (UI) del fragment.
     *
     * @param root La vista del fragmento.
     */
    private void obtenerComponentes(View root) {
        spinnerTerminal = root.findViewById(R.id.spinnerTerminalInsertar);
        spinnerPersona = root.findViewById(R.id.spinnerPersonaModificar);
        spinnerTipoModalidadPacienteInsertar = root.findViewById(R.id.spinnerTipoModalidadPacienteInsertar);
        spinnerTipoModalidadPaciente = root.findViewById(R.id.spinnerTipoModalidadPacienteInsertar);
        editTextTieneUCR = root.findViewById(R.id.editTextTieneUCRInsertar);
        editTextNumeroExpediente = root.findViewById(R.id.editTextNumeroExpedienteInsertar);
        editTextNumeroSeguridadSocial = root.findViewById(R.id.editTextNumeroSeguridadSocialInsertar);
        editTextPrestacionOtrosServicios = root.findViewById(R.id.editTextPrestacionOtrosServiciosInsertar);
        editTextObservacionesMedicas = root.findViewById(R.id.editTextObservacionesMedicasInsertar);
        editTextInteresesActividades = root.findViewById(R.id.editTextInteresesActividadesInsertar);
        btnInsertarPaciente = root.findViewById(R.id.btnInsertarPaciente);
        btnVolverPacienteInsertar = root.findViewById(R.id.btnVolverPacienteInsertar);
        btnInsertarPaciente.setOnClickListener(this);
        btnVolverPacienteInsertar.setOnClickListener(this);
    }

    public void limpiarCampos(){
        editTextNumeroExpediente.setText("");
        editTextNumeroSeguridadSocial.setText("");
        editTextPrestacionOtrosServicios.setText("");
        editTextObservacionesMedicas.setText("");
        editTextInteresesActividades.setText("");
    }

    /**
     * Una función que se llama cuando se presiona un botón.
     *
     * @param view La vista en la que se hizo clic.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInsertarPaciente:
                accionBotonGuardar();
                break;
            case R.id.btnVolverPacienteInsertar:
                volver();
                break;
        }
    }

    /**
     * Método que se ejecuta cuando se presiona el botón de Guardar.
     */
    private void accionBotonGuardar() {
        if (validarCampos()) {
            obtenerDatosFormulario();
        }
    }

    private boolean validarCampos() {
        if (editTextNumeroExpediente.getText().toString().length() < 7) {
            Toast.makeText(getContext(), Constantes.NUMERO_CARACTERES + "7", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextNumeroSeguridadSocial.getText().toString().length() < 12) {
            Toast.makeText(getContext(), Constantes.NUMERO_CARACTERES + "12", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextNumeroExpediente.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextNumeroSeguridadSocial.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void obtenerDatosFormulario() {
        String terminalSeleccionado = spinnerTerminal.getSelectedItem().toString();
        String[] terminalSplit = terminalSeleccionado.split(":");
        //Obtenemos el numero de terminal seleccionado y recogemos sus datos
        terminalSeleccionado = terminalSplit[1].replaceAll("\\s+", "");
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Terminal>> call = apiService.getTerminalByNumeroTerminal(terminalSeleccionado, "Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Terminal>>() {
            @Override
            public void onResponse(Call<List<Terminal>> call, Response<List<Terminal>> response) {
                if (response.isSuccessful()) {
                    List<Terminal> terminales = response.body();
                    //Obtenemos el id del terminal seleccionado
                    pacienteInsertar.setTerminal(terminales.get(0).getId());
                    //Obtemos el id de la persona seleccionada desde el Spinner esta vez, ya que incluye el ID
                    String personaSeleccionada = spinnerPersona.getSelectedItem().toString();
                    String[] personaSplit = personaSeleccionada.split("-");
                    personaSeleccionada = personaSplit[0].replaceAll("\\s+", "");
                    //Obtenemos el resto de los datos del paciente a insertar
                    pacienteInsertar.setPersona(personaSeleccionada);
                    pacienteInsertar.setNumeroExpediente(editTextNumeroExpediente.getText().toString());
                    pacienteInsertar.setNumeroSeguridadSocial(editTextNumeroSeguridadSocial.getText().toString());
                    pacienteInsertar.setPrestacionOtrosServiciosSociales(editTextPrestacionOtrosServicios.getText().toString());
                    pacienteInsertar.setObservacionesMedicas(editTextObservacionesMedicas.getText().toString());
                    pacienteInsertar.setInteresesYActividades(editTextInteresesActividades.getText().toString());
                    if (editTextTieneUCR.getText().toString().equals("Si") || editTextTieneUCR.getText().toString().equals("Sí")) {
                        pacienteInsertar.setTieneUcr(true);
                    } else {
                        pacienteInsertar.setTieneUcr(false);
                    }
                    //Obtemos el id del tipo de modalidad de paciente
                    String tipoModalidadPacienteSeleccionado = spinnerTipoModalidadPaciente.getSelectedItem().toString();
                    String[] tipoModalidadPacienteSplit = tipoModalidadPacienteSeleccionado.split("-");
                    tipoModalidadPacienteSeleccionado = tipoModalidadPacienteSplit[0].replaceAll("\\s+", "");
                    pacienteInsertar.setTipoModalidadPaciente(tipoModalidadPacienteSeleccionado);
                    insertarPacienteBD(pacienteInsertar);
                    personaSeleccionada = personaSplit[0].replaceAll("\\s+", "");
                } else {
                    Toast.makeText(getContext(), "Error al obtener datos de la terminal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Terminal>> call, Throwable t) {

            }
        });
    }

    private void volver() {
        ListarPacienteFragment listarPacienteFragment = new ListarPacienteFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarPacienteFragment)
                .addToBackStack(null)
                .commit();
    }

    private void insertarPacienteBD(Paciente pacienteInsertar) {
        //Insertamos el paciente en la BD
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Paciente> call = apiService.addPaciente(pacienteInsertar, Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.PACIENTE_INSERTADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_INSERTAR_PACIENTE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable t) {

            }
        });
    }

    /**
     * Método que inicializa el Spinner de Terminal.
     */
    private void inicializarSpinnerTerminal() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Terminal>> call = apiService.getListadoTerminal(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<Terminal>>() {
            @Override
            public void onResponse(Call<List<Terminal>> call, Response<List<Terminal>> response) {
                if (response.isSuccessful()) {
                    List<Terminal> listadoTerminales = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaTerminales(listadoTerminales));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTerminal.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Terminal>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_LOS_DATOS, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    /**
     * Toma una lista de objetos Terminal y devuelve una lista de String para mostrar en el Spinner.
     *
     * @param listadoTerminales Lista de objetos Terminal
     * @return Una lista de String.
     */
    private List<String> convertirListaTerminales(List<Terminal> listadoTerminales) {
        List<String> listadoTerminalesString = new ArrayList<>();
        for (Terminal terminal : listadoTerminales) {
            listadoTerminalesString.add("Nº de terminal: " + terminal.getNumeroTerminal());
        }
        return listadoTerminalesString;
    }

    /**
     * Método que inicializa el Spinner de Persona.
     */
    private void inicializarSpinnerPersona() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Persona>> call = apiService.getListadoPersona("Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if (response.isSuccessful()) {
                    List<Persona> listadoPersona = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaPersonas(listadoPersona));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPersona.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_LOS_DATOS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {

            }
        });
    }

    /**
     * Toma una lista de objetos Persona y devuelve una lista de cadenas que contienen el nombre y
     * apellido de cada persona para mostrar en el Spinner.
     *
     * @param listadoPersona Lista de objetos de tipo Persona
     * @return Una lista de String.
     */
    private List<String> convertirListaPersonas(List<Persona> listadoPersona) {
        List<String> listadoPersonaString = new ArrayList<>();
        for (Persona persona : listadoPersona) {
            listadoPersonaString.add(persona.getId() + Constantes.REGEX_SEPARADOR_GUION + persona.getNombre() + " " + persona.getApellidos());
        }
        return listadoPersonaString;
    }
}