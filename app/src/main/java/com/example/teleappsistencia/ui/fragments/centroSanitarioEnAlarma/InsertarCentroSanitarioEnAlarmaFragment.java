package com.example.teleappsistencia.ui.fragments.centroSanitarioEnAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.CentroSanitarioEnAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertarCentroSanitarioEnAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarCentroSanitarioEnAlarmaFragment extends Fragment implements View.OnClickListener{

    private CentroSanitarioEnAlarma centroSanitarioEnAlarma;
    private EditText editTextNumberIdAlarmaCSEACrear;
    private EditText editTextNumberIdCentroSanitarioCSEACrear;
    private EditText editTextFechaRegistroCSEACrear;
    private EditText editTextPersonaCSEACrear;
    private EditText editTextAcuerdoCSEACrear;
    private Button buttonGuardarCSEA;
    private Button buttonVolverCSEA;


    public InsertarCentroSanitarioEnAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarCentroSanitarioEnAlarmaFragment.
     */
    public static InsertarCentroSanitarioEnAlarmaFragment newInstance() {
        InsertarCentroSanitarioEnAlarmaFragment fragment = new InsertarCentroSanitarioEnAlarmaFragment();
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
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_insertar_centro_sanitario_en_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view) {
        this.editTextNumberIdAlarmaCSEACrear = (EditText) view.findViewById(R.id.editTextNumberIdAlarmaCSEACrear);
        this.editTextNumberIdCentroSanitarioCSEACrear = (EditText) view.findViewById(R.id.editTextNumberIdCentroSanitarioCSEACrear);
        this.editTextFechaRegistroCSEACrear = (EditText) view.findViewById(R.id.editTextFechaRegistroCSEACrear);
        this.editTextPersonaCSEACrear = (EditText) view.findViewById(R.id.editTextPersonaCSEACrear);
        this.editTextAcuerdoCSEACrear = (EditText) view.findViewById(R.id.editTextAcuerdoCSEACrear);
        this.buttonGuardarCSEA = (Button) view.findViewById(R.id.buttonGuardarCSEA);
        this.buttonVolverCSEA = (Button) view.findViewById(R.id.buttonVolverCSEA);
    }

    /**
     * En este método se asignan los lístner a los botones y al Edit Text de la fecha
     */
    private void asignarListener() {
        this.buttonGuardarCSEA.setOnClickListener(this);
        this.buttonVolverCSEA.setOnClickListener(this);
        this.editTextFechaRegistroCSEACrear.setOnClickListener(this);
    }


    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextFechaRegistroCSEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_FECHA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdAlarmaCSEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_ALARMA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdCentroSanitarioCSEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_CENTRO, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Extraemos los datos del layout y los guardamos en un objeto del tipo CentroSanitarioEnAlarma.
     */
    private void guardarDatos() {
        this.centroSanitarioEnAlarma = new CentroSanitarioEnAlarma();
        this.centroSanitarioEnAlarma.setIdAlarma(Integer.parseInt(this.editTextNumberIdAlarmaCSEACrear.getText().toString()));
        this.centroSanitarioEnAlarma.setIdCentroSanitario(Integer.parseInt(this.editTextNumberIdCentroSanitarioCSEACrear.getText().toString()));
        this.centroSanitarioEnAlarma.setFechaRegistro(Utilidad.getStringFechaNowYYYYMMDD());
        this.centroSanitarioEnAlarma.setPersona(this.editTextPersonaCSEACrear.getText().toString());
        this.centroSanitarioEnAlarma.setAcuerdoAlcanzado(this.editTextAcuerdoCSEACrear.getText().toString());
    }

    /**
     * Este método lanza la petición POST a la API REST para guardar los datos del CentroSantiarioEnAlarma
     */
    private void persistirCentroSanitarioEnAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<CentroSanitarioEnAlarma> call = apiService.addCentroSanitarioEnAlarma(this.centroSanitarioEnAlarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<CentroSanitarioEnAlarma>() {
            @Override
            public void onResponse(Call<CentroSanitarioEnAlarma> call, Response<CentroSanitarioEnAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.GUARDADO_CON_EXITO, Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + Constantes.PISTA_ALARMA_CENTRO_ID, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CentroSanitarioEnAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarCentrosSanitariosEnAlarmaFragment lCSEA = new ListarCentrosSanitariosEnAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, lCSEA)
                .addToBackStack(null)
                .commit();
    }


    /**
     * Este método limpia los campos.
     */
    private void limpiarCampos(){
        this.editTextNumberIdAlarmaCSEACrear.setText(Constantes.VACIO);
        this.editTextNumberIdCentroSanitarioCSEACrear.setText(Constantes.VACIO);
        this.editTextFechaRegistroCSEACrear.setText(Constantes.VACIO);
        this.editTextPersonaCSEACrear.setText(Constantes.VACIO);
        this.editTextAcuerdoCSEACrear.setText(Constantes.VACIO);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editTextFechaRegistroCSEACrear:
                Utilidad.dialogoFecha(getContext(), editTextFechaRegistroCSEACrear);
                break;
            case R.id.buttonGuardarCSEA:
                if(comprobaciones()){
                    guardarDatos();
                    persistirCentroSanitarioEnAlarma();
                }
                break;
            case R.id.buttonVolverCSEA:
                volver();
                break;
        }
    }
}