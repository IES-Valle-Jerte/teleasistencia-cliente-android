package com.example.teleappsistencia.ui.fragments.relacion_usuario_centro;

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

import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.RelacionUsuarioCentro;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.ui.fragments.relacion_paciente_persona.ListarRelacionPacientePersonaFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Terminal;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertarRelacionUsuarioCentroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarRelacionUsuarioCentroFragment extends Fragment implements View.OnClickListener {

    private Paciente paciente;

    private Spinner spinnerPacienteInsertar;
    private Spinner spinnerCentroSanitarioInsertar;
    private EditText editTextPersonaContactoInsertar;
    private EditText editTextDistanciaInsertar;
    private EditText editTextTiempoInsertar;
    private EditText editTextObservacionesInsertar;
    private Button btnInsertarRelacionUsuarioCentro;
    private Button btnVolverRelacionUsuarioCentroInsertar;

    public InsertarRelacionUsuarioCentroFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InsertarRelacionUsuarioCentroFragment newInstance() {
        InsertarRelacionUsuarioCentroFragment fragment = new InsertarRelacionUsuarioCentroFragment();
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
        View root = inflater.inflate(R.layout.fragment_insertar_relacion_usuario_centro, container, false);

        obtenerComponentes(root);
        inicializarSpinnerPaciente();
        inicializarSpinnerCentroSanitario();
        // Inflate the layout for this fragment
        return root;
    }


    private void obtenerComponentes(View root) {
        spinnerCentroSanitarioInsertar = root.findViewById(R.id.spinnerCentroSanitarioInsertar);
        spinnerPacienteInsertar = root.findViewById(R.id.spinnerPacienteInsertar);
        editTextPersonaContactoInsertar = root.findViewById(R.id.editTextPersonaContactoInsertar);
        editTextDistanciaInsertar = root.findViewById(R.id.editTextDistanciaInsertar);
        editTextTiempoInsertar = root.findViewById(R.id.editTextTiempoInsertar);
        editTextObservacionesInsertar = root.findViewById(R.id.editTextObservacionesInsertar);
        btnInsertarRelacionUsuarioCentro = root.findViewById(R.id.btnInsertarRelacionUsuarioCentro);
        btnVolverRelacionUsuarioCentroInsertar = root.findViewById(R.id.btnVolverRelacionUsuarioCentroInsertar);
        btnInsertarRelacionUsuarioCentro.setOnClickListener(this);
        btnVolverRelacionUsuarioCentroInsertar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInsertarRelacionUsuarioCentro:
                accionBotonGuardar();
                break;
            case R.id.btnVolverRelacionUsuarioCentroInsertar:
                volver();
                break;
        }
    }

    private void accionBotonGuardar() {
        if (validarCampos()) {
            obtenerDatos();
        }
    }

    private void obtenerDatos() {
        RelacionUsuarioCentro relacionUsuarioCentroInsertar = new RelacionUsuarioCentro();
        //Obtenemos el id del paciente
        String pacienteSeleccionado = spinnerPacienteInsertar.getSelectedItem().toString();
        String[] pacienteSeleccionadoSplit = pacienteSeleccionado.split("-");
        int idPaciente = Integer.parseInt(pacienteSeleccionadoSplit[0]);
        relacionUsuarioCentroInsertar.setIdPaciente(idPaciente);
        //Obtenemos el id del centro sanitario
        String centroSanitarioSeleccionado = spinnerCentroSanitarioInsertar.getSelectedItem().toString();
        String[] centroSanitarioSeleccionadoSplit = centroSanitarioSeleccionado.split("-");
        int idCentroSanitario = Integer.parseInt(centroSanitarioSeleccionadoSplit[0]);
        relacionUsuarioCentroInsertar.setIdCentroSanitario(idCentroSanitario);
        //Obtenemos el nombre de la persona de contacto
        relacionUsuarioCentroInsertar.setPersonaContacto(editTextPersonaContactoInsertar.getText().toString());
        //Obtenemos la distancia
        relacionUsuarioCentroInsertar.setDistancia(Integer.parseInt(editTextDistanciaInsertar.getText().toString()));
        //Obtenemos el tiempo
        relacionUsuarioCentroInsertar.setTiempo(Integer.parseInt(editTextTiempoInsertar.getText().toString()));
        //Obtenemos las observaciones
        relacionUsuarioCentroInsertar.setObservaciones(editTextObservacionesInsertar.getText().toString());
        //Insertamos la relacion en la base de datos
        insertarRelacionUsuarioCentro(relacionUsuarioCentroInsertar);
    }

    private void insertarRelacionUsuarioCentro(RelacionUsuarioCentro relacionUsuarioCentroInsertar) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<RelacionUsuarioCentro> call = apiService.addRelacionUsuarioCentro(relacionUsuarioCentroInsertar,"Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<RelacionUsuarioCentro>() {
            @Override
            public void onResponse(Call<RelacionUsuarioCentro> call, Response<RelacionUsuarioCentro> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.RELACION_DE_USUARIO_CENTRO_INSERTADA_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_INSERTAR_LA_RELACION_DE_USUARIO_CENTRO, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RelacionUsuarioCentro> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.RELACION_DE_USUARIO_CENTRO_INSERTADA_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private boolean validarCampos() {
        if (editTextDistanciaInsertar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextTiempoInsertar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextObservacionesInsertar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.CAMPO_VACIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void inicializarSpinnerPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacientes(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    List<Object> lPacientes = response.body();
                    List<Paciente> listadoPacientes = (ArrayList<Paciente>) Utilidad.getObjeto(lPacientes, Constantes.AL_PACIENTE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaPacientes(listadoPacientes));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPacienteInsertar.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_LOS_DATOS, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private List<String> convertirListaPacientes(List<Paciente> listadoPacientes) {
        List<String> listadoPacientesString = new ArrayList<>();
        for (Paciente paciente : listadoPacientes) {
            listadoPacientesString.add(paciente.getId() + "-" +"Nº expediente: " + paciente.getNumeroExpediente());
        }
        return listadoPacientesString;
    }

    private void inicializarSpinnerCentroSanitario() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<CentroSanitario>> call = apiService.getListadoCentroSanitario(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<CentroSanitario>>() {
            @Override
            public void onResponse(Call<List<CentroSanitario>> call, Response<List<CentroSanitario>> response) {
                if (response.isSuccessful()) {
                    List<CentroSanitario> lCentrosSanitarios = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaCentrosSanitarios(lCentrosSanitarios));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCentroSanitarioInsertar.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CentroSanitario>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_LOS_DATOS, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private List<String> convertirListaCentrosSanitarios(List<CentroSanitario> lCentrosSanitarios) {
        List<String> listadoCentrosSanitariosString = new ArrayList<>();
        for (CentroSanitario centroSanitario : lCentrosSanitarios) {
            listadoCentrosSanitariosString.add(centroSanitario.getId() + Constantes.REGEX_SEPARADOR_GUION + centroSanitario.getNombre());
        }
        return listadoCentrosSanitariosString;
    }

    private void volver() {
        ListarRelacionUsuarioCentroFragment listarRelacionUsuarioCentroFragment = new ListarRelacionUsuarioCentroFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarRelacionUsuarioCentroFragment)
                .addToBackStack(null)
                .commit();
    }

    public void limpiarCampos(){
        editTextDistanciaInsertar.setText("");
        editTextObservacionesInsertar.setText("");
        editTextPersonaContactoInsertar.setText("");
        editTextTiempoInsertar.setText("");
    }


}