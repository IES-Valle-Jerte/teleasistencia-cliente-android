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
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.CentroSanitarioEnAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarCentroSanitarioEnAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarCentroSanitarioEnAlarmaFragment extends Fragment implements View.OnClickListener{

    private CentroSanitarioEnAlarma centroSanitarioEnAlarma;
    private EditText editTextNumberIdAlarmaCSEAModificar;
    private EditText editTextNumberIdCentroSanitarioCSEAModificar;
    private EditText editTextFechaRegistroCSEAModificar;
    private EditText editTextPersonaCSEAModificar;
    private EditText editTextAcuerdoCSEAModificar;
    private Button buttonGuardarCSEAModificado;
    private Button buttonVolverCSEA;

    public ModificarCentroSanitarioEnAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param centroSanitarioEnAlarma objeto que se va a modificar
     * @return A new instance of fragment ModificarCentroSanitarioEnAlarmaFragment.
     */
    public static ModificarCentroSanitarioEnAlarmaFragment newInstance(CentroSanitarioEnAlarma centroSanitarioEnAlarma) {
        ModificarCentroSanitarioEnAlarmaFragment fragment = new ModificarCentroSanitarioEnAlarmaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_CENTROSANITARIOEA, centroSanitarioEnAlarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobamos que la instancia se ha creado con argumentos y si es así las recogemos.
        if (getArguments() != null) {
            this.centroSanitarioEnAlarma = (CentroSanitarioEnAlarma) getArguments().getSerializable(Constantes.ARG_CENTROSANITARIOEA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_modificar_centro_sanitario_en_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos
        if(this.centroSanitarioEnAlarma != null){
            cargarDatos();
        }

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view) {
        this.editTextNumberIdAlarmaCSEAModificar = (EditText) view.findViewById(R.id.editTextNumberIdAlarmaCSEAModificar);
        this.editTextNumberIdCentroSanitarioCSEAModificar = (EditText) view.findViewById(R.id.editTextNumberIdCentroSanitarioCSEAModificar);
        this.editTextFechaRegistroCSEAModificar = (EditText) view.findViewById(R.id.editTextFechaRegistroCSEAModificar);
        this.editTextPersonaCSEAModificar = (EditText) view.findViewById(R.id.editTextPersonaCSEAModificar);
        this.editTextAcuerdoCSEAModificar = (EditText) view.findViewById(R.id.editTextAcuerdoCSEAModificar);
        this.buttonGuardarCSEAModificado = (Button) view.findViewById(R.id.buttonGuardarCSEAModificado);
        this.buttonVolverCSEA = (Button) view.findViewById(R.id.buttonVolverCSEA);
    }

    /**
     * Asignamos la propia clase como OnClickListener, ya que abajo tenemos el método implementado
     */
    private void asignarListener() {
        this.buttonGuardarCSEAModificado.setOnClickListener(this);
        this.buttonVolverCSEA.setOnClickListener(this);
        this.editTextFechaRegistroCSEAModificar.setOnClickListener(this);
    }

    /**
     * Este método carga los datos del Centro Sanitario en Alarma en el layout.
     */
    private void cargarDatos() {
        Alarma alarma = (Alarma) Utilidad.getObjeto(centroSanitarioEnAlarma.getIdAlarma(), Constantes.ALARMA);
        CentroSanitario centroSanitario = (CentroSanitario) Utilidad.getObjeto(centroSanitarioEnAlarma.getIdCentroSanitario(), Constantes.CENTRO_SANITARIO);

        this.editTextNumberIdAlarmaCSEAModificar.setText(String.valueOf(alarma.getId()));
        this.editTextNumberIdCentroSanitarioCSEAModificar.setText(String.valueOf(centroSanitario.getId()));
        this.editTextFechaRegistroCSEAModificar.setText(this.centroSanitarioEnAlarma.getFechaRegistro());
        this.editTextPersonaCSEAModificar.setText(this.centroSanitarioEnAlarma.getPersona());
        this.editTextAcuerdoCSEAModificar.setText(this.centroSanitarioEnAlarma.getAcuerdoAlcanzado());
    }

    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextFechaRegistroCSEAModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_FECHA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdAlarmaCSEAModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_ALARMA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdCentroSanitarioCSEAModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_CENTRO, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Modificamos los datos del objeto Centro Sanitario en Alarma que nos llegó al crear el fragment
     */
    private void modificarDatos(){
        this.centroSanitarioEnAlarma.setIdAlarma(Integer.parseInt(this.editTextNumberIdAlarmaCSEAModificar.getText().toString()));
        this.centroSanitarioEnAlarma.setIdCentroSanitario(Integer.parseInt(this.editTextNumberIdCentroSanitarioCSEAModificar.getText().toString()));
        this.centroSanitarioEnAlarma.setFechaRegistro(this.editTextFechaRegistroCSEAModificar.getText().toString());
        this.centroSanitarioEnAlarma.setPersona(this.editTextPersonaCSEAModificar.getText().toString());
        this.centroSanitarioEnAlarma.setAcuerdoAlcanzado(this.editTextAcuerdoCSEAModificar.getText().toString());
    }


    /**
     * Este método lanza la petición PUT a la API REST para modificar el Centro Sanitario en alarma
     */
    private void persistirCentroSanitarioEnAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarCentroSanitarioEnAlarma(centroSanitarioEnAlarma.getId(), Constantes.BEARER_ESPACIO+ Utilidad.getToken().getAccess(), this.centroSanitarioEnAlarma);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.errorBody() == null){
                    Toast.makeText(getContext(), Constantes.MODIFICADO_CON_EXITO, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_MODIFICACION + Constantes.PISTA_ALARMA_CENTRO_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editTextFechaRegistroCSEAModificar:
                Utilidad.dialogoFecha(getContext(), editTextFechaRegistroCSEAModificar);
                break;
            case R.id.buttonGuardarCSEAModificado:
                if(comprobaciones()){
                    modificarDatos();
                    persistirCentroSanitarioEnAlarma();
                }
                break;
            case R.id.buttonVolverCSEA:
                volver();
                break;
        }
    }
}