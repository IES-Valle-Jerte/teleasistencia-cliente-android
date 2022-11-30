package com.example.teleappsistencia.ui.fragments.tipoAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.ClasificacionAlarma;
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
 * Use the {@link InsertarTipoAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarTipoAlarmaFragment extends Fragment implements View.OnClickListener{

    private TipoAlarma tipoAlarma;
    private List<ClasificacionAlarma> lClasificacionAlarma;
    private EditText editTextCodigoTipoAlarmaCrear;
    private EditText editTextNombreTipoAlarmaCrear;
    private RadioButton radioButtonSi;
    private RadioButton radioButtonNo;
    private Spinner spinnerClasificacionTipoAlarma;
    private Button buttonGuardarTipoAlarma;
    private Button buttonVolverTipoAlarma;

    public InsertarTipoAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarTipoAlarmaFragment.
     */

    public static InsertarTipoAlarmaFragment newInstance() {
        InsertarTipoAlarmaFragment fragment = new InsertarTipoAlarmaFragment();
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
        View view = inflater.inflate(R.layout.fragment_insertar_tipo_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        // Cargamos los datos del spinner
        cargarSpinner();

        //Asignamos el listener a los botones
        asignarListener();

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view){
        this.editTextCodigoTipoAlarmaCrear = (EditText) view.findViewById(R.id.editTextCodigoTipoAlarmaCrear);
        this.editTextNombreTipoAlarmaCrear= (EditText) view.findViewById(R.id.editTextNombreTipoAlarmaCrear);
        this.spinnerClasificacionTipoAlarma = (Spinner) view.findViewById(R.id.spinnerClasificacionTipoAlarma);
        this.radioButtonSi = (RadioButton) view.findViewById(R.id.radioButtonSi);
        this.radioButtonNo = (RadioButton) view.findViewById(R.id.radioButtonNo);
        this.buttonGuardarTipoAlarma= (Button) view.findViewById(R.id.buttonGuardarTipoAlarma);
        this.buttonVolverTipoAlarma = (Button) view.findViewById(R.id.buttonVolverTipoAlarma);
    }

    /**
     * En este método se asignan los lístner a los botones
     */
    private void asignarListener(){
        this.buttonGuardarTipoAlarma.setOnClickListener(this);
        this.buttonVolverTipoAlarma.setOnClickListener(this);
    }

    /**
     * Este método hace la petición a la API REST para obtener las Clasificaciones de Alarma que hay
     * y las carga en el adapter que se le asignará al Spinneer
     */
    private void cargarSpinner(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getListaClasificacionAlarma(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lClasificacionAlarma = (ArrayList<ClasificacionAlarma>) Utilidad.getObjeto(lObjetos, Constantes.AL_CLASIFICACION_ALARMA);
                    ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lClasificacionAlarma);
                    spinnerClasificacionTipoAlarma.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextCodigoTipoAlarmaCrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_CODIGO_TIPO_ALARMA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNombreTipoAlarmaCrear.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_NOMBRE, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Extraemos los datos del layout y los guardamos en un objeto del tipo TipoAlarma.
     */
    private void guardarDatos(){
        this.tipoAlarma = new TipoAlarma();
        ClasificacionAlarma clasificacionAlarma = (ClasificacionAlarma) spinnerClasificacionTipoAlarma.getSelectedItem();
        this.tipoAlarma.setNombre(this.editTextNombreTipoAlarmaCrear.getText().toString());
        this.tipoAlarma.setCodigo(this.editTextCodigoTipoAlarmaCrear.getText().toString());
        this.tipoAlarma.setEsDispositivo(radioButtonSi.isChecked());
        this.tipoAlarma.setClasificacionAlarma(clasificacionAlarma.getId());
    }

    /**
     * Este método lanza la petición POST a la API REST para guardar los datos del Tipo de Alarma
     */
    private void persistirTipoAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<TipoAlarma> call = apiService.addTipoAlarma(this.tipoAlarma, Constantes.BEARER_ESPACIO+ Utilidad.getToken().getAccess());
        call.enqueue(new Callback<TipoAlarma>() {
            @Override
            public void onResponse(Call<TipoAlarma> call, Response<TipoAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.GUARDADO_CON_EXITO, Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + response.message() , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<TipoAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarTipoAlarmaFragment listarTipoAlarmaFragment = new ListarTipoAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarTipoAlarmaFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Este método limpia los campos.
     */
    private void limpiarCampos(){
        this.editTextCodigoTipoAlarmaCrear.setText(Constantes.VACIO);
        this.editTextNombreTipoAlarmaCrear.setText(Constantes.VACIO);
        this.radioButtonSi.setChecked(true);
        this.spinnerClasificacionTipoAlarma.setSelection(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonGuardarTipoAlarma:
                if(comprobaciones()){
                    guardarDatos();
                    persistirTipoAlarma();
                }
                break;
            case R.id.buttonVolverTipoAlarma:
                volver();
                break;
        }
    }
}