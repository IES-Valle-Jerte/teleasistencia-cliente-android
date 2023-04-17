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
 * Una clase {@link Fragment} para recoger los datos a modificar de un Paciente particular.
 * <p> Esta clase es una subclase de {@link Fragment} y hereda de ella todos sus métodos y atributos.
 */
public class ModificarPacienteFragment extends Fragment implements View.OnClickListener {

    /**
     * El paciente que se quiere modificar.
     */
    private Paciente paciente;

    // Atributos de la interfaz de usuario (UI) del fragment.
    private Spinner spinnerTerminal;
    private Spinner spinnerPersona;
    private Spinner spinnerTipoModalidadPacienteModificar;
    private EditText editTextTieneUCR;
    private EditText editTextNumeroExpediente;
    private EditText editTextNumeroSeguridadSocial;
    private EditText editTextPrestacionOtrosServicios;
    private EditText editTextObservacionesMedicas;
    private EditText editTextInteresesActividades;
    private Button btnModificarPaciente;
    private Button btnVolverPacienteModificar;

    //Atributos de la clase
    private Paciente pacienteModificar;

    // Constructor por defecto.
    public ModificarPacienteFragment() {
    }


    /**
     * Esta función crea una nueva instancia del fragmento y pasa el objeto al fragmento.
     *
     * @param paciente es el objeto que quiero pasar al fragmento
     * @return Una nueva instancia del fragmento.
     */
    public static ModificarPacienteFragment newInstance(Paciente paciente) {
        ModificarPacienteFragment fragment = new ModificarPacienteFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoPaciente", paciente);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * La función se llama cuando se crea el fragmento.
     *
     * @param savedInstanceState El estado guardado del fragmento, o nulo si se trata de un fragmento
     *                           recién creado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paciente = (Paciente) getArguments().getSerializable("objetoPaciente");
        }
    }

    /**
     * La función onCreateView() se llama cuando se crea el fragmento
     *
     * @param inflater           El objeto LayoutInflater que se puede usar para inflar cualquier vista en el
     *                           fragmento,
     * @param container          La vista principal a la que se debe adjuntar la interfaz de usuario del
     *                           fragmento.
     * @param savedInstanceState Si el fragmento se vuelve a crear a partir de un estado guardado
     *                           anterior, este es el estado.
     * @return La vista del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modificar_paciente, container, false);
        pacienteModificar = new Paciente();
        // Inicializamos los atributos de la interfaz de usuario (UI).
        obtenerComponentes(root);
        // Rellenamos los campos con los datos del paciente que se quiere modificar.
        rellenarCampos();
        // Añadimos los eventos de los botones.
        this.btnVolverPacienteModificar.setOnClickListener(this);
        this.btnModificarPaciente.setOnClickListener(this);
        return root;
    }

    private void rellenarCampos() {
        // Inicializamos los spinners.
        inicializarSpinnerTerminal();
        inicializarSpinnerPersona();
        inicializarSpinnerTipoModalidadPaciente();
        // Fijamos si el paciente tiene UCR evitando que se muestren booleanos y sea algo más legible.
        fijarSiTieneUCR();
        // Rellenamos los campos con los datos del paciente que se quiere modificar.
        editTextNumeroExpediente.setText(paciente.getNumeroExpediente());
        editTextNumeroSeguridadSocial.setText(paciente.getNumeroSeguridadSocial());
        editTextPrestacionOtrosServicios.setText(paciente.getPrestacionOtrosServiciosSociales());
        editTextObservacionesMedicas.setText(paciente.getObservacionesMedicas());
        editTextInteresesActividades.setText(paciente.getInteresesYActividades());
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

    private void inicializarSpinnerTipoModalidadPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<TipoModalidadPaciente>> call = apiService.getListadoTipoModalidadPaciente(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoModalidadPaciente>>() {
            @Override
            public void onResponse(Call<List<TipoModalidadPaciente>> call, Response<List<TipoModalidadPaciente>> response) {
                if (response.code() == 200) {
                    List<TipoModalidadPaciente> listadoTipoModalidadPaciente = response.body();
                    if (listadoTipoModalidadPaciente != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaTipoModalidadPaciente(listadoTipoModalidadPaciente));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        TipoModalidadPaciente tipoModalidadPaciente = (TipoModalidadPaciente) Utilidad.getObjeto(paciente.getTipoModalidadPaciente(), "TipoModalidadPaciente");
                        spinnerTipoModalidadPacienteModificar.setAdapter(adapter);
                        //comprobar que no sea null ya que en la api estan subidos con null
                        if(tipoModalidadPaciente!=null){
                            spinnerTipoModalidadPacienteModificar.setSelection(buscarPosicionSpinnerTipoModalidadPaciente(listadoTipoModalidadPaciente, tipoModalidadPaciente.getId()));
                        }

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
            listadoString.add(tipoModalidadPaciente.getId() + Constantes.REGEX_SEPARADOR_GUION + tipoModalidadPaciente.getNombre());
        }
        return listadoString;
    }

    private int buscarPosicionSpinnerTipoModalidadPaciente(List<TipoModalidadPaciente> listadoTipoModalidadPaciente, int id) {
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            if (listadoTipoModalidadPaciente.get(i).getId() == id) {
                encontrado = true;
            }
            i++;
        }
        return i - 1;
    }

    private void obtenerDatosFormulario() {
        String terminalSeleccionado = spinnerTerminal.getSelectedItem().toString();
        String[] terminalSplit = terminalSeleccionado.split(":");
        //Obtenemos el numero de terminal seleccionado y recogemos sus datos
        terminalSeleccionado = terminalSplit[1].replaceAll("\\s+", "");
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Terminal>> call = apiService.getTerminalByNumeroTerminal(terminalSeleccionado, Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Terminal>>() {
            @Override
            public void onResponse(Call<List<Terminal>> call, Response<List<Terminal>> response) {
                if (response.isSuccessful()) {
                    List<Terminal> terminales = response.body();
                    //Obtenemos el id del terminal seleccionado
                    pacienteModificar.setTerminal(terminales.get(0).getId());
                    //Obtemos el id de la persona seleccionada desde el Spinner esta vez, ya que incluye el ID
                    String personaSeleccionada = spinnerPersona.getSelectedItem().toString();
                    String[] personaSplit = personaSeleccionada.split(Constantes.REGEX_SEPARADOR_GUION);
                    personaSeleccionada = personaSplit[0].replaceAll("\\s+", "");
                    //Obtenemos el resto de los datos del paciente a insertar
                    pacienteModificar.setPersona(personaSeleccionada);
                    pacienteModificar.setNumeroExpediente(editTextNumeroExpediente.getText().toString());
                    pacienteModificar.setNumeroSeguridadSocial(editTextNumeroSeguridadSocial.getText().toString());
                    pacienteModificar.setPrestacionOtrosServiciosSociales(editTextPrestacionOtrosServicios.getText().toString());
                    pacienteModificar.setObservacionesMedicas(editTextObservacionesMedicas.getText().toString());
                    pacienteModificar.setInteresesYActividades(editTextInteresesActividades.getText().toString());
                    if (editTextTieneUCR.getText().toString().equals("Si") || editTextTieneUCR.getText().toString().equals("Sí")) {
                        pacienteModificar.setTieneUcr(true);
                    } else {
                        pacienteModificar.setTieneUcr(false);
                    }
                    //Obtemos el id del tipo de modalidad de paciente
                    String tipoModalidadPacienteSeleccionado = spinnerTipoModalidadPacienteModificar.getSelectedItem().toString();
                    String[] tipoModalidadPacienteSplit = tipoModalidadPacienteSeleccionado.split(Constantes.REGEX_SEPARADOR_GUION);
                    tipoModalidadPacienteSeleccionado = tipoModalidadPacienteSplit[0].replaceAll("\\s+", "");
                    pacienteModificar.setTipoModalidadPaciente(tipoModalidadPacienteSeleccionado);
                    modificarPacienteBD(paciente.getId(), pacienteModificar);
                    personaSeleccionada = personaSplit[0].replaceAll("\\s+", "");
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_LOS_DATOS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Terminal>> call, Throwable t) {

            }
        });
    }

    private void modificarPacienteBD(int id, Paciente pacienteModificar) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Paciente> call = apiService.updatePaciente(id, pacienteModificar, Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.PACIENTE_MODIFICADO, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_MODIFICAR_EL_PACIENTE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable t) {

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

    /**
     * Si el paciente tiene una UCR, el campo de texto mostrará "Si", de lo contrario, mostrará "No".
     */
    private void fijarSiTieneUCR() {
        if (paciente.isTieneUcr()) {
            this.editTextTieneUCR.setText("Si");
        } else {
            this.editTextTieneUCR.setText("No");
        }
    }

    /**
     * Método que inicializa el Spinner de Personas en la interfaz de usuario.
     */
    private void inicializarSpinnerPersona() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Persona>> call = apiService.getListadoPersona(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if (response.isSuccessful()) {
                    List<Persona> listadoPersona = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaPersonas(listadoPersona));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Persona persona = (Persona) Utilidad.getObjeto(paciente.getPersona(), "Persona");
                    spinnerPersona.setAdapter(adapter);
                    if (persona != null) {
                        spinnerPersona.setSelection(buscarPosicionSpinnerPersona(listadoPersona, persona.getId()));
                    } else {
                        spinnerPersona.setSelection(0);
                    }
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
                    Terminal terminal = (Terminal) Utilidad.getObjeto(paciente.getTerminal(), "Terminal");
                    spinnerTerminal.setAdapter(adapter);
                    if (terminal != null) {
                        spinnerTerminal.setSelection(buscarPosicionSpinnerTerminal(listadoTerminales, terminal.getId()), true);
                    } else {
                        spinnerTerminal.setSelection(0, true);
                    }
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


    private int buscarPosicionSpinnerTerminal(List<Terminal> listadoTerminales, int id) {
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            if (listadoTerminales.get(i).getId() == id) {
                encontrado = true;
            }
            i++;
        }
        return i - 1;
    }

    private int buscarPosicionSpinnerPersona(List<Persona> listadoPersonas, int id) {
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            if (listadoPersonas.get(i).getId() == id) {
                encontrado = true;
            }
            i++;
        }
        return i - 1;
    }

    /**
     * Método que obtiene los componentes de la interfaz de usuario (UI) del fragment.
     *
     * @param root La vista del fragmento.
     */
    private void obtenerComponentes(View root) {
        this.btnVolverPacienteModificar = (Button) root.findViewById(R.id.btnVolverPacienteModificar);
        this.btnModificarPaciente = (Button) root.findViewById(R.id.btnModificarPaciente);
        this.spinnerTerminal = (Spinner) root.findViewById(R.id.spinnerTerminal);
        this.spinnerPersona = (Spinner) root.findViewById(R.id.spinnerPersona);
        this.spinnerTipoModalidadPacienteModificar = (Spinner) root.findViewById(R.id.spinnerTipoModalidadPacienteModificar);
        this.editTextTieneUCR = (EditText) root.findViewById(R.id.editTextTieneUCR);
        this.editTextNumeroExpediente = (EditText) root.findViewById(R.id.editTextNumeroExpediente);
        this.editTextNumeroSeguridadSocial = (EditText) root.findViewById(R.id.editTextNumeroSeguridadSocial);
        this.editTextPrestacionOtrosServicios = (EditText) root.findViewById(R.id.editTextPrestacionOtrosServicios);
        this.editTextObservacionesMedicas = (EditText) root.findViewById(R.id.editTextObservacionesMedicas);
        this.editTextInteresesActividades = (EditText) root.findViewById(R.id.editTextInteresesActividades);
    }


    /**
     * Una función que se llama cuando se hace clic en un botón.
     *
     * @param view La vista en la que se hizo clic.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnModificarPaciente:
                accionBotonGuardar();
                break;
            case R.id.btnVolverPacienteModificar:
                volver();
                break;
        }
    }

    /**
     * Método que se ejecuta cuando se hace clic en el botón de guardar.
     * Si los campos son válidos, guarde los datos. De lo contrario, mostrar un mensaje de error.
     */
    private void accionBotonGuardar() {
        if (validarCampos()) {
            obtenerDatosFormulario();
        } else {
            Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    public void limpiarCampos() {
        this.editTextTieneUCR.setText("");
        this.editTextNumeroExpediente.setText("");
        this.editTextNumeroSeguridadSocial.setText("");
        this.editTextPrestacionOtrosServicios.setText("");
        this.editTextObservacionesMedicas.setText("");
        this.editTextInteresesActividades.setText("");
    }
}