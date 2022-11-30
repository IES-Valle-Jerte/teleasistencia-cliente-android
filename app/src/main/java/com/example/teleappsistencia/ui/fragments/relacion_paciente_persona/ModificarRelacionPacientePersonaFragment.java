package com.example.teleappsistencia.ui.fragments.relacion_paciente_persona;

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
import com.example.teleappsistencia.modelos.RelacionPacientePersona;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarRelacionPacientePersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarRelacionPacientePersonaFragment extends Fragment implements View.OnClickListener {

    private RelacionPacientePersona relacionPacientePersona;
    private Spinner spinnerPersonaModificar;
    private Spinner spinnerPacienteModificarRelacionPacientePersona;
    private EditText editTextTipoRelacionModificar;
    private EditText editTextTieneLlaveViviendaModificar;
    private EditText editTextDisponibilidadModificar;
    private EditText editTextObservacionesModificar;
    private EditText editTextPrioridadModificar;

    private Button btnModificarRelacionPacientePersona;
    private Button btnVolverRelacionPacientePersonaModificar;

    //Atributos de la clase
    private RelacionPacientePersona relacionPacientePersonaModificar;

    public ModificarRelacionPacientePersonaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ModificarRelacionPacientePersonaFragment newInstance(RelacionPacientePersona relacionPacientePersona) {
        ModificarRelacionPacientePersonaFragment fragment = new ModificarRelacionPacientePersonaFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoRelacionPacientePersona", relacionPacientePersona);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            relacionPacientePersona = (RelacionPacientePersona) getArguments().getSerializable("objetoRelacionPacientePersona");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modificar_relacion_paciente_persona, container, false);
        relacionPacientePersonaModificar = new RelacionPacientePersona();
        obtenerComponentes(root);
        rellenarCampos();
        // Inflate the layout for this fragment
        return root;
    }

    private void obtenerComponentes(View root) {
        spinnerPersonaModificar = root.findViewById(R.id.spinnerPersonaModificar);
        spinnerPacienteModificarRelacionPacientePersona = root.findViewById(R.id.spinnerPacienteModificarRelacionPacientePersona);
        editTextTipoRelacionModificar = root.findViewById(R.id.editTextTipoRelacionModificar);
        editTextTieneLlaveViviendaModificar = root.findViewById(R.id.editTextTieneLlaveViviendaModificar);
        editTextDisponibilidadModificar = root.findViewById(R.id.editTextDisponibilidadModificar);
        editTextObservacionesModificar = root.findViewById(R.id.editTextObservacionesModificar);
        editTextPrioridadModificar = root.findViewById(R.id.editTextPrioridadModificarRelacionPacientePersona);

        btnModificarRelacionPacientePersona = root.findViewById(R.id.btnModificarRelacionPacientePersona);
        btnVolverRelacionPacientePersonaModificar = root.findViewById(R.id.btnVolverRelacionPacientePersonaModificar);

        btnModificarRelacionPacientePersona.setOnClickListener(this);
        btnVolverRelacionPacientePersonaModificar.setOnClickListener(this);
    }

    private void rellenarCampos() {
        inicializarSpinnerPersona();
        inicializarSpinnerPaciente();
        fijarSiTieneLlaves();
        editTextTipoRelacionModificar.setText(relacionPacientePersona.getTipoRelacion());
        editTextDisponibilidadModificar.setText(relacionPacientePersona.getDisponibilidad());
        editTextObservacionesModificar.setText(relacionPacientePersona.getObservaciones());
        editTextPrioridadModificar.setText(String.valueOf(relacionPacientePersona.getPrioridad()));
    }

    private void fijarSiTieneLlaves() {
        if (relacionPacientePersona.isTieneLlavesVivienda()) {
            this.editTextTieneLlaveViviendaModificar.setText("Si");
        } else {
            this.editTextTieneLlaveViviendaModificar.setText("No");
        }
    }

    private void inicializarSpinnerPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacientes("Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    List<Object> lPacientes = response.body();
                    List<Paciente> listadoPacientes = (ArrayList<Paciente>) Utilidad.getObjeto(lPacientes, Constantes.AL_PACIENTE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaPacientes(listadoPacientes));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Paciente paciente = (Paciente) Utilidad.getObjeto(relacionPacientePersona.getIdPaciente(), "Paciente");
                    spinnerPacienteModificarRelacionPacientePersona.setAdapter(adapter);
                    if (relacionPacientePersona.getIdPaciente() != null) {
                        spinnerPacienteModificarRelacionPacientePersona.setSelection(buscarPosicionSpinnerPaciente(listadoPacientes, paciente.getId()));
                    } else {
                        spinnerPacienteModificarRelacionPacientePersona.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private List<String> convertirListaPacientes(List<Paciente> listadoPacientes) {
        List<String> listadoPacientesString = new ArrayList<>();
        for (Paciente paciente : listadoPacientes) {
            listadoPacientesString.add(paciente.getId() + "-" + "Nº expediente: " + paciente.getNumeroExpediente());
        }
        return listadoPacientesString;
    }

    private int buscarPosicionSpinnerPaciente(List<Paciente> listadoPacientes, int id) {
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            if (listadoPacientes.get(i).getId() == id) {
                encontrado = true;
            }
            i++;
        }
        return i - 1;
    }

    private void obtenerDatosFormulario() {
        //Obtemos el id del paciente seleccionado desde el Spinner esta vez, ya que incluye el ID
        String pacienteSeleccionado = spinnerPacienteModificarRelacionPacientePersona.getSelectedItem().toString();
        String[] pacienteSplit = pacienteSeleccionado.split("-");
        pacienteSeleccionado = pacienteSplit[0].replaceAll("\\s+", "");
        relacionPacientePersonaModificar.setIdPaciente(Integer.parseInt(pacienteSeleccionado));
        //Obtemos el id de la persona seleccionada desde el Spinner esta vez, ya que incluye el ID
        String personaSeleccionada = spinnerPersonaModificar.getSelectedItem().toString();
        String[] personaSplit = personaSeleccionada.split("-");
        personaSeleccionada = personaSplit[0].replaceAll("\\s+", "");
        relacionPacientePersonaModificar.setIdPersona(personaSeleccionada);
        //Obtenemos el resto de atributos de los editText
        if (editTextTieneLlaveViviendaModificar.getText().toString().equals("Sí") || editTextTieneLlaveViviendaModificar.getText().toString().equals("Si")) {
            relacionPacientePersonaModificar.setTieneLlavesVivienda(true);
        } else {
            relacionPacientePersonaModificar.setTieneLlavesVivienda(false);
        }
        relacionPacientePersonaModificar.setTipoRelacion(editTextTipoRelacionModificar.getText().toString());
        relacionPacientePersonaModificar.setDisponibilidad(editTextDisponibilidadModificar.getText().toString());
        relacionPacientePersonaModificar.setObservaciones(editTextObservacionesModificar.getText().toString());
        relacionPacientePersonaModificar.setPrioridad(Integer.parseInt(editTextPrioridadModificar.getText().toString()));
        relacionPacientePersonaModificar.setId((int) ((Double) relacionPacientePersona.getId()).intValue());
        modificarRelacionPacientePersona(relacionPacientePersonaModificar);
    }

    private void modificarRelacionPacientePersona(RelacionPacientePersona relacionPacientePersona) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<RelacionPacientePersona> call = apiService.updateRelacionPacientePersona((int) relacionPacientePersona.getId(), relacionPacientePersona, "Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<RelacionPacientePersona>() {
            @Override
            public void onResponse(Call<RelacionPacientePersona> call, Response<RelacionPacientePersona> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.RELACIÓN_PACIENTE_PERSONA_MODIFICADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    //Cargamos de nuevo la lista de pacientes y cerramos el fragment
                    volver();
                }
            }

            @Override
            public void onFailure(Call<RelacionPacientePersona> call, Throwable t) {

            }
        });
    }

    public void limpiarCampos() {
        editTextTieneLlaveViviendaModificar.setText("");
        editTextTipoRelacionModificar.setText("");
        editTextDisponibilidadModificar.setText("");
        editTextObservacionesModificar.setText("");
        editTextPrioridadModificar.setText("");
    }

    private void volver() {
        ListarRelacionPacientePersonaFragment listarRelacionPacientePersonaFragment = new ListarRelacionPacientePersonaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarRelacionPacientePersonaFragment)
                .addToBackStack(null)
                .commit();
    }

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
                    Persona persona = (Persona) Utilidad.getObjeto(relacionPacientePersona.getIdPersona(), "Persona");
                    spinnerPersonaModificar.setAdapter(adapter);
                    if (persona != null) {
                        spinnerPersonaModificar.setSelection(buscarPosicionSpinnerPersona(listadoPersona, persona.getId()));
                    } else {
                        spinnerPersonaModificar.setSelection(0);
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

    private List<String> convertirListaPersonas(List<Persona> listadoPersona) {
        List<String> listadoPersonaString = new ArrayList<>();
        for (Persona persona : listadoPersona) {
            listadoPersonaString.add(persona.getId() + Constantes.REGEX_SEPARADOR_GUION + persona.getNombre() + " " + persona.getApellidos());
        }
        return listadoPersonaString;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnModificarRelacionPacientePersona:
                accionBotonGuardar();
                break;
            case R.id.btnVolverRelacionPacientePersonaModificar:
                volver();
                break;
        }
    }

    private void accionBotonGuardar() {
        if (validarCampos()) {
            obtenerDatosFormulario();
        } else {
            Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (String.valueOf(editTextPrioridadModificar.getText().toString()).isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextTieneLlaveViviendaModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextTipoRelacionModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}