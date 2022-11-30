package com.example.teleappsistencia.ui.fragments.alarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Paciente;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertarAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarAlarmaFragment extends Fragment implements View.OnClickListener{

    private Alarma alarma;
    private List<TipoAlarma> lTipoAlarma;
    private List<Terminal> lTerminales;
    private List<Paciente> lPacientes;
    private Spinner spinnerTipoAlarma;
    private Spinner spinnerIdTerminalOPaciente;
    private RadioGroup radioGroupAlarma;
    private TextView textViewIdTerminalPacienteAlarmaCrear;
    private Button buttonGuardarAlarma;
    private Button buttonVolverAlarma;
    private ArrayAdapter adapterTiposAlarma;
    private ArrayAdapter adapterTerminales;
    private ArrayAdapter adapterPacientes;

    public InsertarAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarAlarmaFragment.
     */
    public static InsertarAlarmaFragment newInstance() {
        InsertarAlarmaFragment fragment = new InsertarAlarmaFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insertar_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos desde la API REST en los spinners
        cargarDatosSpinners();

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view){
        this.spinnerTipoAlarma = (Spinner) view.findViewById(R.id.spinnerTipoAlarma);
        this.spinnerIdTerminalOPaciente = (Spinner) view.findViewById(R.id.spinnerIdTerminalOPaciente);
        this.radioGroupAlarma = (RadioGroup) view.findViewById(R.id.radioGroupAlarma);
        this.textViewIdTerminalPacienteAlarmaCrear = (TextView) view.findViewById(R.id.textViewIdTerminalPacienteAlarmaCrear);
        this.buttonGuardarAlarma = (Button) view.findViewById(R.id.buttonGuardarAlarma);
        this.buttonVolverAlarma = (Button) view.findViewById(R.id.buttonVolverAlarma);
    }

    /**
     * En este método se asignan los lístner a los botones y al Radio grup
     */
    private void asignarListener(){
        this.buttonGuardarAlarma.setOnClickListener(this);
        this.buttonVolverAlarma.setOnClickListener(this);

        /* Cuando seleccionemos un radioButton, cargaremos el adapter correspondiente con sus datos */
        this.radioGroupAlarma.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.radioButtonTerminal:
                        spinnerIdTerminalOPaciente.setAdapter(adapterTerminales);
                        textViewIdTerminalPacienteAlarmaCrear.setText(Constantes.ID_TERMINAL);
                        break;
                    case R.id.radioButtonPaciente:
                        spinnerIdTerminalOPaciente.setAdapter(adapterPacientes);
                        textViewIdTerminalPacienteAlarmaCrear.setText(Constantes.ID_PACIENTE);
                        break;
                }
            }
        });
    }

    /**
     * En este método se cargan los datos de los spinners haciendo llamadas a la API REST
     */
    private void cargarDatosSpinners(){
        cargarDatosTiposAlarmas();
        cargarDatosTerminales();
        cargarDatosPacientes();
    }

    /**
     * Extraemos los datos del layout y los guardamos en un objeto del tipo Alarma.
     */
    private void guardarDatos(){
        Paciente paciente;
        Terminal terminal;

        TipoAlarma tipoAlarma = (TipoAlarma) this.spinnerTipoAlarma.getSelectedItem();

        this.alarma = new Alarma();
        this.alarma.setId_tipo_alarma(tipoAlarma.getId());

        /* La alarma se puede crear de dos formas: por Terminal (automática) o por Paciente (voluntaria)
           Settearemos el item correspondiente dependidendo del radioButton seleccionado.
         */
        switch (this.radioGroupAlarma.getCheckedRadioButtonId()){
            case R.id.radioButtonTerminal:
                terminal = (Terminal) this.spinnerIdTerminalOPaciente.getSelectedItem();
                this.alarma.setId_terminal(terminal.getId());
                break;
            case R.id.radioButtonPaciente:
                paciente = (Paciente) this.spinnerIdTerminalOPaciente.getSelectedItem();
                this.alarma.setId_paciente_ucr(paciente.getId());
                break;
        }
    }

    /**
     * Este método carga los Tipos de Alarma en su adapter desde la API REST y se lo añade al Spinner
     */
    private void cargarDatosTiposAlarmas(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getTiposAlarma(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lTipoAlarma = (ArrayList<TipoAlarma>) Utilidad.getObjeto(lObjetos, Constantes.AL_TIPO_ALARMA);
                    adapterTiposAlarma = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lTipoAlarma);
                    spinnerTipoAlarma.setAdapter(adapterTiposAlarma);
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


    /**
     * Este método carga los datos de los Terminales desde la API REST y los carga en su adapter
     */
    private void cargarDatosTerminales(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getTerminales(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    radioGroupAlarma.check(R.id.radioButtonTerminal);
                    List<Object> lObjetos = response.body();
                    lTerminales = (ArrayList<Terminal>) Utilidad.getObjeto(lObjetos, Constantes.AL_TERMINAL);
                    adapterTerminales = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lTerminales);
                    spinnerIdTerminalOPaciente.setAdapter(adapterTerminales); // se cargan este adapter primero
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


    /**
     * Este método carga los datos de los Terminales desde la API REST y los carga en su adapter
     */
    private void cargarDatosPacientes(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacientes(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lPacientes = (ArrayList<Paciente>) Utilidad.getObjeto(lObjetos, Constantes.AL_PACIENTE);
                    adapterPacientes = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lPacientes);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método lanza la petición POST a la API REST para guardar la alarma
     */
    private void persistirAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Alarma> call = apiService.addAlarma(this.alarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Alarma>() {
            @Override
            public void onResponse(Call<Alarma> call, Response<Alarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.ALARMA_GUARDADA,  Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Alarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarAlarmasFragment listarAlarmasFragment = new ListarAlarmasFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarAlarmasFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonGuardarAlarma:
                guardarDatos();
                persistirAlarma();
                break;
            case R.id.buttonVolverAlarma:
                volver();
                break;
        }
    }
}