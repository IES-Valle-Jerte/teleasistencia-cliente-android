package com.example.teleappsistencia.ui.fragments.recursosComunitariosEnAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.RecursoComunitarioEnAlarma;
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
 * Use the {@link InsertarRecursosComunitariosEnAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarRecursosComunitariosEnAlarmaFragment extends Fragment implements View.OnClickListener{

    private RecursoComunitarioEnAlarma recursoComunitarioEnAlarma;
    private EditText editTextNumberIdAlarmaRCEACrear;
    private EditText editTextNumberIdRecursoComunitarioRCEACrear;
    private EditText editTextFechaRegistroRCEACrear;
    private EditText editTextPersonaRCEACrear;
    private EditText editTextAcuerdoRCEACrear;
    private Button buttonGuardarRCEAModificado;
    private Button buttonVolverRCEA;

    public InsertarRecursosComunitariosEnAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarRecursosComunitariosEnAlarmaFragment.
     */
    
    public static InsertarRecursosComunitariosEnAlarmaFragment newInstance() {
        InsertarRecursosComunitariosEnAlarmaFragment fragment = new InsertarRecursosComunitariosEnAlarmaFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insertar_recursos_comunitarios_en_alarma, container, false);

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
        this.editTextNumberIdAlarmaRCEACrear = (EditText) view.findViewById(R.id.editTextNumberIdAlarmaRCEACrear);
        this.editTextNumberIdRecursoComunitarioRCEACrear = (EditText) view.findViewById(R.id.editTextNumberIdRecursoComunitarioRCEACrear);
        this.editTextFechaRegistroRCEACrear = (EditText) view.findViewById(R.id.editTextFechaRegistroRCEACrear);
        this.editTextPersonaRCEACrear = (EditText) view.findViewById(R.id.editTextPersonaRCEACrear);
        this.editTextAcuerdoRCEACrear = (EditText) view.findViewById(R.id.editTextAcuerdoRCEACrear);
        this.buttonGuardarRCEAModificado = (Button) view.findViewById(R.id.buttonGuardarRCEAModificado);
        this.buttonVolverRCEA = (Button) view.findViewById(R.id.buttonVolverRCEA);
    }

    /**
     * En este método se asignan los lístner a los botones y al Edit Text de la fecha
     */
    private void asignarListener() {
        this.buttonGuardarRCEAModificado.setOnClickListener(this);
        this.buttonVolverRCEA.setOnClickListener(this);
        this.editTextFechaRegistroRCEACrear.setOnClickListener(this);
    }


    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextFechaRegistroRCEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_FECHA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdAlarmaRCEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_ALARMA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdRecursoComunitarioRCEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Extraemos los datos del layout y los guardamos en un objeto del tipo RecursoComunitarioEnAlarma.
     */
    private void guardarDatos(){
        this.recursoComunitarioEnAlarma = new RecursoComunitarioEnAlarma();
        this.recursoComunitarioEnAlarma.setIdAlarma(Integer.parseInt(this.editTextNumberIdAlarmaRCEACrear.getText().toString()));
        this.recursoComunitarioEnAlarma.setIdRecursoComunitairo(Integer.parseInt(this.editTextNumberIdRecursoComunitarioRCEACrear.getText().toString()));
        this.recursoComunitarioEnAlarma.setFechaRegistro(this.editTextFechaRegistroRCEACrear.getText().toString());
        this.recursoComunitarioEnAlarma.setPersona(this.editTextPersonaRCEACrear.getText().toString());
        this.recursoComunitarioEnAlarma.setAcuerdoAlcanzado(this.editTextAcuerdoRCEACrear.getText().toString());
    }

    /**
     * Este método lanza la petición POST a la API REST para guardar los datos del Recurso Comunitario en Alarma
     */
    private void persistirRecursoComunitarioAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<RecursoComunitarioEnAlarma> call = apiService.addRecursoComunitarioEnAlarma(this.recursoComunitarioEnAlarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<RecursoComunitarioEnAlarma>() {
            @Override
            public void onResponse(Call<RecursoComunitarioEnAlarma> call, Response<RecursoComunitarioEnAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.GUARDADO_CON_EXITO, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + Constantes.PISTA_ALARMA_RECURSOCOMUNITARIO_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RecursoComunitarioEnAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarRecursosComunitariosEnAlarmaFragment lRCEA = new ListarRecursosComunitariosEnAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, lRCEA)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Este método limpia los campos.
     */
    private void limpiarCampos(){
        this.editTextNumberIdAlarmaRCEACrear.setText(Constantes.VACIO);
        this.editTextNumberIdRecursoComunitarioRCEACrear.setText(Constantes.VACIO);
        this.editTextFechaRegistroRCEACrear.setText(Constantes.VACIO);
        this.editTextPersonaRCEACrear.setText(Constantes.VACIO);
        this.editTextAcuerdoRCEACrear.setText(Constantes.VACIO);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editTextFechaRegistroRCEACrear:
                Utilidad.dialogoFecha(getContext(), editTextFechaRegistroRCEACrear);
                break;
            case R.id.buttonGuardarRCEAModificado:
                if(comprobaciones()){
                    guardarDatos();
                    persistirRecursoComunitarioAlarma();
                }
                break;
            case R.id.buttonVolverRCEA:
                volver();
                break;
        }
    }
}