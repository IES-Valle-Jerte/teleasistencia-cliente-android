package com.example.teleappsistencia.ui.fragments.personaContactoEnAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.PersonaContactoEnAlarma;
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
 * Use the {@link InsertarPersonaContactoEnAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarPersonaContactoEnAlarmaFragment extends Fragment implements View.OnClickListener{

    private PersonaContactoEnAlarma personaContactoEnAlarma;
    private EditText editTextNumberIdAlarmaPCEACrear;
    private EditText editTextNumberIdPersonaPCEACrear;
    private EditText editTextFechaRegistroPCEACrear;
    private EditText editTextAcuerdoPCEACrear;
    private Button buttonGuardarPCEACrear;
    private Button buttonVolverPCEA;

    public InsertarPersonaContactoEnAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarPersonaContactoEnAlarmaFragment.
     */

    public static InsertarPersonaContactoEnAlarmaFragment newInstance() {
        InsertarPersonaContactoEnAlarmaFragment fragment = new InsertarPersonaContactoEnAlarmaFragment();
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
        View view = inflater.inflate(R.layout.fragment_insertar_persona_contacto_en_alarma, container, false);

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
        this.editTextNumberIdAlarmaPCEACrear = (EditText) view.findViewById(R.id.editTextNumberIdAlarmaPCEACrear);
        this.editTextNumberIdPersonaPCEACrear = (EditText) view.findViewById(R.id.editTextNumberIdPersonaPCEACrear);
        this.editTextFechaRegistroPCEACrear = (EditText) view.findViewById(R.id.editTextFechaRegistroPCEACrear);
        this.editTextAcuerdoPCEACrear = (EditText) view.findViewById(R.id.editTextAcuerdoPCEACrear);
        this.buttonGuardarPCEACrear = (Button) view.findViewById(R.id.buttonGuardarPCEACrear);
        this.buttonVolverPCEA = (Button) view.findViewById(R.id.buttonVolverPCEA);
    }

    /**
     * En este método se asignan los lístner a los botones y al Edit Text de la fecha
     */
    private void asignarListener() {
        this.buttonGuardarPCEACrear.setOnClickListener(this);
        this.buttonVolverPCEA.setOnClickListener(this);
        this.editTextFechaRegistroPCEACrear.setOnClickListener(this);
    }

    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextFechaRegistroPCEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_FECHA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdAlarmaPCEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_ALARMA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdPersonaPCEACrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_PERSONA, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Extraemos los datos del layout y los guardamos en un objeto del tipo PersonaContactoEnAlarma.
     */
    private void guardarDatos(){
        this.personaContactoEnAlarma = new PersonaContactoEnAlarma();
        this.personaContactoEnAlarma.setIdAlarma(Integer.parseInt(this.editTextNumberIdAlarmaPCEACrear.getText().toString()));
        this.personaContactoEnAlarma.setIdPersonaContacto(Integer.parseInt(this.editTextNumberIdPersonaPCEACrear.getText().toString()));
        this.personaContactoEnAlarma.setFechaRegistro(this.editTextFechaRegistroPCEACrear.getText().toString());
        this.personaContactoEnAlarma.setAcuerdoAlcanzado(this.editTextAcuerdoPCEACrear.getText().toString());
    }

    /**
     * Este método lanza la petición POST a la API REST para guardar los datos de la Persona de contacto en alarma
     */
    private void persistirPersonaContactoEnAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<PersonaContactoEnAlarma> call = apiService.addPersonaContactoEnAlarma(this.personaContactoEnAlarma, Constantes.BEARER_ESPACIO+ Utilidad.getToken().getAccess());
        call.enqueue(new Callback<PersonaContactoEnAlarma>() {
            @Override
            public void onResponse(Call<PersonaContactoEnAlarma> call, Response<PersonaContactoEnAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.GUARDADO_CON_EXITO, Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + Constantes.PISTA_ALARMA_PERSONA_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<PersonaContactoEnAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarPersonasContactoEnAlarmaFragment lPCEA = new ListarPersonasContactoEnAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, lPCEA)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Este método limpia los campos.
     */
    private void limpiarCampos(){
        this.editTextNumberIdAlarmaPCEACrear.setText(Constantes.VACIO);
        this.editTextNumberIdPersonaPCEACrear.setText(Constantes.VACIO);
        this.editTextFechaRegistroPCEACrear.setText(Constantes.VACIO);
        this.editTextAcuerdoPCEACrear.setText(Constantes.VACIO);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editTextFechaRegistroPCEACrear:
                Utilidad.dialogoFecha(getContext(), editTextFechaRegistroPCEACrear);
                break;
            case R.id.buttonGuardarPCEACrear:
                if(comprobaciones()){
                    guardarDatos();
                    persistirPersonaContactoEnAlarma();
                }
                break;
            case R.id.buttonVolverPCEA:
                volver();
                break;
        }
    }
}