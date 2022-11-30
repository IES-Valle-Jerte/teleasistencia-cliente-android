package com.example.teleappsistencia.ui.fragments.terminal;

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
import com.example.teleappsistencia.modelos.TipoVivienda;
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
 * Use the {@link InsertarTerminalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarTerminalFragment extends Fragment implements View.OnClickListener {

    private Terminal terminal;

    private Spinner spinnerTitularInsertar;
    private Spinner spinnerTipoViviendaInsertar;
    private EditText editTextNumeroTerminalInsertar;
    private EditText editTextModoAccesoViviendaInsertar;
    private EditText editTextBarrerasArquitectonicasInsertar;

    private Button btnInsertarTerminal;
    private Button btnVolverTerminalInsertar;

    public InsertarTerminalFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InsertarTerminalFragment newInstance() {
        InsertarTerminalFragment fragment = new InsertarTerminalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_insertar_terminal, container, false);

        obtenerComponentes(root);
        inicializarSpinnerTitular();
        inicializarSpinnerTipoVivienda();
        // Inflate the layout for this fragment
        return root;
    }

    private void obtenerComponentes(View root) {
        spinnerTitularInsertar = root.findViewById(R.id.spinnerTitularInsertar);
        spinnerTipoViviendaInsertar = root.findViewById(R.id.spinnerTipoViviendaInsertar);
        editTextNumeroTerminalInsertar = root.findViewById(R.id.editTextNumeroTerminalInsertar);
        editTextModoAccesoViviendaInsertar = root.findViewById(R.id.editTextModoAccesoViviendaInsertar);
        editTextBarrerasArquitectonicasInsertar = root.findViewById(R.id.editTextBarrerasArquitectonicasInsertar);
        btnInsertarTerminal = root.findViewById(R.id.btnInsertarTerminal);
        btnVolverTerminalInsertar = root.findViewById(R.id.btnVolverTerminalInsertar);
        btnInsertarTerminal.setOnClickListener(this);
        btnVolverTerminalInsertar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInsertarTerminal:
                accionBotonGuardar();
                break;
            case R.id.btnVolverTerminalInsertar:
                volver();
                break;
        }
    }

    private void accionBotonGuardar() {
        if (validarCampos()) {
            obtenerDatos();
        }
    }

    private boolean validarCampos() {
        if (editTextBarrerasArquitectonicasInsertar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBE_INGRESAR_LAS_BARRERAS_ARQUITECTÓNICAS, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextModoAccesoViviendaInsertar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBE_INGRESAR_EL_MODO_DE_ACCESO_A_LA_VIVIENDA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextNumeroTerminalInsertar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBE_INGRESAR_EL_NUMERO_DE_TERMINAL, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void obtenerDatos() {
        Terminal terminalInsertar = new Terminal();
        String titularInsertado = spinnerTitularInsertar.getSelectedItem().toString();
        String[] titularInsertadoSplit = titularInsertado.split("-");
        titularInsertado = titularInsertadoSplit[0];
        terminalInsertar.setTitular(titularInsertado);
        String tipoViviendaInsertado = spinnerTipoViviendaInsertar.getSelectedItem().toString();
        String[] tipoViviendaInsertadoSplit = tipoViviendaInsertado.split("-");
        tipoViviendaInsertado = tipoViviendaInsertadoSplit[0];
        terminalInsertar.setTipoVivienda(tipoViviendaInsertado);
        terminalInsertar.setNumeroTerminal(editTextNumeroTerminalInsertar.getText().toString());
        terminalInsertar.setModoAccesoVivienda(editTextModoAccesoViviendaInsertar.getText().toString());
        terminalInsertar.setBarrerasArquitectonicas(editTextBarrerasArquitectonicasInsertar.getText().toString());
        insertarTerminal(terminalInsertar);
    }

    private void insertarTerminal(Terminal terminalInsertar) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Terminal> call = apiService.addTerminal(terminalInsertar, "Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Terminal>() {
            @Override
            public void onResponse(Call<Terminal> call, Response<Terminal> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.TERMINAL_INSERTADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_INSERTANDO_TERMINAL, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Terminal> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_INSERTANDO_TERMINAL, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void inicializarSpinnerTitular() {
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
                    spinnerTitularInsertar.setAdapter(adapter);
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
        List<String> listadoTerminalesString = new ArrayList<>();
        for (Paciente paciente : listadoPacientes) {
            listadoTerminalesString.add(paciente.getId() + "-" + "Exp: " + paciente.getNumeroExpediente());
        }
        return listadoTerminalesString;
    }


    private void inicializarSpinnerTipoVivienda() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<TipoVivienda>> call = apiService.getListadoTipoVivienda("Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<TipoVivienda>>() {
            @Override
            public void onResponse(Call<List<TipoVivienda>> call, Response<List<TipoVivienda>> response) {
                if (response.isSuccessful()) {
                    List<TipoVivienda> listadoTipoVivienda = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaTipoVivienda(listadoTipoVivienda));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipoViviendaInsertar.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TipoVivienda>> call, Throwable t) {

            }
        });
    }

    private List<String> convertirListaTipoVivienda(List<TipoVivienda> listadoTipoVivienda) {
        List<String> listadoTipoViviendaString = new ArrayList<>();
        for (TipoVivienda tipoVivienda : listadoTipoVivienda) {
            listadoTipoViviendaString.add(tipoVivienda.getId() + "-" + tipoVivienda.getNombre());
        }
        return listadoTipoViviendaString;
    }

    private List<String> convertirListaPersonas(List<Persona> listadoPersona) {
        List<String> listadoPersonaString = new ArrayList<>();
        for (Persona persona : listadoPersona) {
            listadoPersonaString.add(persona.getId() + "-" + persona.getNombre() + " " + persona.getApellidos());
        }
        return listadoPersonaString;
    }

    private void volver() {
        ListarTerminalFragment listarTerminalFragment = new ListarTerminalFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarTerminalFragment)
                .addToBackStack(null)
                .commit();
    }

    public void limpiarCampos(){
        editTextBarrerasArquitectonicasInsertar.setText("");
        editTextNumeroTerminalInsertar.setText("");
        editTextModoAccesoViviendaInsertar.setText("");
    }
}