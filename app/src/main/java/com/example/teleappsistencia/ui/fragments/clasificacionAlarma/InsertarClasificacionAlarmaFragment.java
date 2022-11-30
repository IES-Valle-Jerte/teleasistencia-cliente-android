package com.example.teleappsistencia.ui.fragments.clasificacionAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.ClasificacionAlarma;
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
 * Use the {@link InsertarClasificacionAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarClasificacionAlarmaFragment extends Fragment implements View.OnClickListener{

    private ClasificacionAlarma clasificacionAlarma;
    private EditText editTextNombreClasificacionAlarmaCrear;
    private EditText editTextCodigoClasificacionAlarmaCrear;
    private Button buttonGuardarClasificacionAlarma;
    private Button buttonVolverClasificacionAlarma;

    public InsertarClasificacionAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarClasificacionAlarmaFragment.
     */
    public static InsertarClasificacionAlarmaFragment newInstance() {
        InsertarClasificacionAlarmaFragment fragment = new InsertarClasificacionAlarmaFragment();
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
        View view = inflater.inflate(R.layout.fragment_insertar_clasificacion_alarma, container, false);

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
        this.editTextNombreClasificacionAlarmaCrear = (EditText) view.findViewById(R.id.editTextNombreClasificacionAlarmaCrear);
        this.editTextCodigoClasificacionAlarmaCrear = (EditText) view.findViewById(R.id.editTextCodigoClasificacionAlarmaCrear);
        this.buttonGuardarClasificacionAlarma = (Button) view.findViewById(R.id.buttonGuardarClasificacionAlarma);
        this.buttonVolverClasificacionAlarma = (Button) view.findViewById(R.id.buttonVolverClasificacionAlarma);

        // Añadimos al campo código un filtro de mayúsculas y longitud máxima
        this.editTextCodigoClasificacionAlarmaCrear.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});
    }

    /**
     * En este método se asignan los lístner a los botones
     */
    private void asignarListener(){
        this.buttonGuardarClasificacionAlarma.setOnClickListener(this);
        this.buttonVolverClasificacionAlarma.setOnClickListener(this);
    }

    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextNombreClasificacionAlarmaCrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_NOMBRE, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextCodigoClasificacionAlarmaCrear.getText().toString().length() < 2) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_CODIGO_2_3_LETRAS, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Extraemos los datos del layout y los guardamos en un objeto del tipo ClasificacionAlarma.
     */
    private void guardarDatos(){
        this.clasificacionAlarma = new ClasificacionAlarma();
        this.clasificacionAlarma.setNombre(this.editTextNombreClasificacionAlarmaCrear.getText().toString());
        this.clasificacionAlarma.setCodigo(this.editTextCodigoClasificacionAlarmaCrear.getText().toString());
    }

    /**
     * Este método lanza la petición POST a la API REST para guardar los datos de la nueva Clasificación de Alarma
     */
    private void persistirClasificacionAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ClasificacionAlarma> call = apiService.addClasificacionAlarma(this.clasificacionAlarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<ClasificacionAlarma>() {
            @Override
            public void onResponse(Call<ClasificacionAlarma> call, Response<ClasificacionAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.GUARDADO_CON_EXITO,  Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClasificacionAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarClasificacionAlarmaFragment listarClasificacionAlarmaFragment = new ListarClasificacionAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarClasificacionAlarmaFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Este método limpia los campos.
     */
    private void limpiarCampos(){
        this.editTextCodigoClasificacionAlarmaCrear.setText(Constantes.VACIO);
        this.editTextNombreClasificacionAlarmaCrear.setText(Constantes.VACIO);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonGuardarClasificacionAlarma:
                if(comprobaciones()){
                    guardarDatos();
                    persistirClasificacionAlarma();
                }
                break;
            case R.id.buttonVolverClasificacionAlarma:
                volver();
                break;
        }
    }
}