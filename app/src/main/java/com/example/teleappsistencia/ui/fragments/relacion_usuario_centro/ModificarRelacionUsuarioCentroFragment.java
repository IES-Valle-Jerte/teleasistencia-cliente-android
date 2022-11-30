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
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.Paciente;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarRelacionUsuarioCentroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarRelacionUsuarioCentroFragment extends Fragment implements View.OnClickListener {

    private RelacionUsuarioCentro relacionUsuarioCentro;

    private Spinner spinnerPacienteModificar;
    private Spinner spinnerCentroSanitarioModificar;
    private EditText editTextPersonaContactoModificar;
    private EditText editTextDistanciaModificar;
    private EditText editTextTiempoModificar;
    private EditText editTextObservacionesModificarRelacionUsuarioCentro;
    private Button btnModificarRelacionUsuarioCentro;
    private Button btnVolverRelacionUsuarioCentroModificar;

    public ModificarRelacionUsuarioCentroFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ModificarRelacionUsuarioCentroFragment newInstance(RelacionUsuarioCentro relacionUsuarioCentro) {
        ModificarRelacionUsuarioCentroFragment fragment = new ModificarRelacionUsuarioCentroFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoRelacionUsuarioCentro", relacionUsuarioCentro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            relacionUsuarioCentro = (RelacionUsuarioCentro) getArguments().getSerializable("objetoRelacionUsuarioCentro");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modificar_relacion_usuario_centro, container, false);

        obtenerComponentes(root);
        inicializarSpinnerPaciente();
        inicializarSpinnerCentroSanitario();
        editTextDistanciaModificar.setText(String.valueOf(relacionUsuarioCentro.getDistancia()));
        editTextTiempoModificar.setText(String.valueOf(relacionUsuarioCentro.getTiempo()));
        editTextObservacionesModificarRelacionUsuarioCentro.setText(relacionUsuarioCentro.getObservaciones());
        editTextPersonaContactoModificar.setText(relacionUsuarioCentro.getPersonaContacto());
        this.btnModificarRelacionUsuarioCentro.setOnClickListener(this);
        this.btnVolverRelacionUsuarioCentroModificar.setOnClickListener(this);

        // Inflate the layout for this fragment
        return root;
    }

    private void obtenerComponentes(View root) {
        this.spinnerPacienteModificar = root.findViewById(R.id.spinnerPacienteModificar);
        this.spinnerCentroSanitarioModificar = root.findViewById(R.id.spinnerCentroSanitarioModificar);
        this.editTextPersonaContactoModificar = root.findViewById(R.id.editTextPersonaContactoModificar);
        this.editTextDistanciaModificar = root.findViewById(R.id.editTextDistanciaModificar);
        this.editTextTiempoModificar = root.findViewById(R.id.editTextTiempoModificar);
        this.editTextObservacionesModificarRelacionUsuarioCentro = root.findViewById(R.id.editTextObservacionesModificarRelacionUsuarioCentro);
        this.btnModificarRelacionUsuarioCentro = root.findViewById(R.id.btnModificarRelacionUsuarioCentro);
        this.btnVolverRelacionUsuarioCentroModificar = root.findViewById(R.id.btnVolverRelacionUsuarioCentroModificar);
        btnModificarRelacionUsuarioCentro.setOnClickListener(this);
        btnVolverRelacionUsuarioCentroModificar.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnModificarRelacionUsuarioCentro:
                accionBotonGuardar();
                break;
            case R.id.btnVolverRelacionUsuarioCentroModificar:
                volver();
                break;
        }
    }

    private void accionBotonGuardar() {
        RelacionUsuarioCentro relacionUsuarioCentroModificar = new RelacionUsuarioCentro();
        ///Obtenemos el id del paciente
        String pacienteSeleccionado = spinnerPacienteModificar.getSelectedItem().toString();
        String[] pacienteSeleccionadoSplit = pacienteSeleccionado.split("-");
        int idPaciente = Integer.parseInt(pacienteSeleccionadoSplit[0]);
        relacionUsuarioCentroModificar.setIdPaciente(idPaciente);
        //Obtenemos el id del centro sanitario
        String centroSanitarioSeleccionado = spinnerCentroSanitarioModificar.getSelectedItem().toString();
        String[] centroSanitarioSeleccionadoSplit = centroSanitarioSeleccionado.split("-");
        int idCentroSanitario = Integer.parseInt(centroSanitarioSeleccionadoSplit[0]);
        relacionUsuarioCentroModificar.setIdCentroSanitario(idCentroSanitario);
        //Obtenemos el nombre de la persona de contacto
        relacionUsuarioCentroModificar.setPersonaContacto(editTextPersonaContactoModificar.getText().toString());
        //Obtenemos la distancia
        relacionUsuarioCentroModificar.setDistancia(Integer.parseInt(editTextDistanciaModificar.getText().toString()));
        //Obtenemos el tiempo
        relacionUsuarioCentroModificar.setTiempo(Integer.parseInt(editTextTiempoModificar.getText().toString()));
        //Obtenemos las observaciones
        relacionUsuarioCentroModificar.setObservaciones(editTextObservacionesModificarRelacionUsuarioCentro.getText().toString());
        //Modificamos la relacion en la base de datos
        modificarRelacionUsuarioCentro(relacionUsuarioCentro.getId(),relacionUsuarioCentroModificar);
    }

    private void modificarRelacionUsuarioCentro(int idRelacionUsuarioPacienteModificar, RelacionUsuarioCentro relacionUsuarioCentroModificar) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<RelacionUsuarioCentro> call = apiService.updateRelacionUsuarioCentro(
                idRelacionUsuarioPacienteModificar,
                relacionUsuarioCentroModificar,
                "Bearer "+ Utilidad.getToken().getAccess()
                );
        call.enqueue(new Callback<RelacionUsuarioCentro>() {
            @Override
            public void onResponse(Call<RelacionUsuarioCentro> call, Response<RelacionUsuarioCentro> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.RELACION_MODIFICADA_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_MODIFICAR_LA_RELACION, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RelacionUsuarioCentro> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_AL_MODIFICAR_LA_RELACION, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    private boolean validarCampos() {
        return false;
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
                    CentroSanitario centroSanitario = (CentroSanitario) Utilidad.getObjeto(relacionUsuarioCentro.getIdCentroSanitario(), "CentroSanitario");
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCentroSanitarioModificar.setAdapter(adapter);
                    spinnerCentroSanitarioModificar.setSelection(buscarPosicionSpinnerCentroSanitario(lCentrosSanitarios, centroSanitario.getId()));
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
            listadoCentrosSanitariosString.add(centroSanitario.getId() + "-" + centroSanitario.getNombre());
        }
        return listadoCentrosSanitariosString;
    }

    private int buscarPosicionSpinnerCentroSanitario(List<CentroSanitario> lCentrosSanitarios, int id) {
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            if (lCentrosSanitarios.get(i).getId() == id) {
                encontrado = true;
            }
            i++;
        }
        return i-1;
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
                    Paciente paciente = (Paciente) Utilidad.getObjeto(relacionUsuarioCentro.getIdPaciente(), "Paciente");
                    spinnerPacienteModificar.setAdapter(adapter);
                    if(relacionUsuarioCentro.getIdPaciente() != null) {
                        spinnerPacienteModificar.setSelection(buscarPosicionSpinnerPaciente(listadoPacientes, paciente.getId()));
                    } else {
                        spinnerPacienteModificar.setAdapter(adapter);
                    }
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
            listadoPacientesString.add(paciente.getId() + Constantes.REGEX_SEPARADOR_GUION +"Nº expediente: " + paciente.getNumeroExpediente());
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
        return i-1;
    }

    private void volver() {
        ListarRelacionUsuarioCentroFragment listarRelacionUsuarioCentroFragment = new ListarRelacionUsuarioCentroFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarRelacionUsuarioCentroFragment)
                .addToBackStack(null)
                .commit();
    }

    public void limpiarCampos(){
        editTextObservacionesModificarRelacionUsuarioCentro.setText("");
        editTextTiempoModificar.setText("");
        editTextDistanciaModificar.setText("");
        editTextPersonaContactoModificar.setText("");
    }
}