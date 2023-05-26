package com.example.teleappsistencia.ui.fragments.alarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Teleoperador;
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
 * Use the {@link ModificarAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarAlarmaFragment extends Fragment implements View.OnClickListener{

    private Alarma alarma;
    private Spinner spinnerEstadoAlarma;
    private EditText editTextObservacionesAlarmaModificar;
    private EditText editTextResumenAlarmaModificar;
    private EditText editTextNumberIdTeleoperadorModificar;
    private Button buttonGuardarAlarmaModificada;
    private Button buttonVolverAlarma;


    public ModificarAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param alarma que se va a modificar
     * @return A new instance of fragment ModificarAlarmaFragment.
     */

    public static ModificarAlarmaFragment newInstance(Alarma alarma) {
        ModificarAlarmaFragment fragment = new ModificarAlarmaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_ALARMA, alarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobamos que la instancia se ha creado con argumentos y si es así las recogemos.
        if (getArguments() != null) {
            this.alarma = (Alarma) getArguments().getSerializable(Constantes.ARG_ALARMA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_modificar_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos
        if(this.alarma != null){
            cargarDatos();
        }

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view){
        this.spinnerEstadoAlarma = (Spinner) view.findViewById(R.id.spinnerEstadoAlarma);
        this.editTextObservacionesAlarmaModificar = (EditText) view.findViewById(R.id.editTextObservacionesAlarmaModificar);
        this.editTextResumenAlarmaModificar = (EditText) view.findViewById(R.id.editTextResumenAlarmaModificar);
        this.editTextNumberIdTeleoperadorModificar = (EditText) view.findViewById(R.id.editTextNumberIdTeleoperadorModificar);
        this.buttonGuardarAlarmaModificada = (Button) view.findViewById(R.id.buttonGuardarAlarmaModificada);
        this.buttonVolverAlarma = (Button) view.findViewById(R.id.buttonVolverAlarma);
    }

    /**
     * Asignamos la propia clase como OnClickListener, ya que abajo tenemos el método implementado
     */
    private void asignarListener(){
        this.buttonGuardarAlarmaModificada.setOnClickListener(this);
        this.buttonVolverAlarma.setOnClickListener(this);
    }

    /**
     * Este método carga los datos de la alarma en el layout.
     */
    private void cargarDatos() {
        int idTeleoperador;
        // Le cargamos de forma estática Array desde un Recurso xml con los dos estados, Abierta y Cerrada.
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.estados_alarmas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerEstadoAlarma.setAdapter(adapter);
        if(this.alarma.getEstado_alarma().equals(Constantes.ABIERTA)){
            this.spinnerEstadoAlarma.setSelection(0);
        }
        else{
            this.spinnerEstadoAlarma.setSelection(1);
        }
        this.editTextObservacionesAlarmaModificar.setText(this.alarma.getObservaciones());
        this.editTextResumenAlarmaModificar.setText(this.alarma.getResumen());
        if(this.alarma.getId_teleoperador() != null){
            idTeleoperador = ((Teleoperador) Utilidad.getObjeto(this.alarma.getId_teleoperador(), Constantes.TELEOPERADOR)).getId();
            this.editTextNumberIdTeleoperadorModificar.setText(String.valueOf(idTeleoperador));
        }
    }


    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(editTextNumberIdTeleoperadorModificar.getText().toString().isEmpty()){
            Toast.makeText(getContext(), Constantes.OBLIGATORIO_INDICAR_TELEOPERADOR, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Modificamos los datos del objeto Alarma que nos llegó al crear el fragment
     */
    private void modificarDatos(){
        alarma.setObservaciones(this.editTextObservacionesAlarmaModificar.getText().toString());
        alarma.setResumen(this.editTextResumenAlarmaModificar.getText().toString());
        alarma.setEstado_alarma((String)this.spinnerEstadoAlarma.getSelectedItem());
        alarma.setId_teleoperador(Integer.parseInt(this.editTextNumberIdTeleoperadorModificar.getText().toString()));
    }

    /**
     * Este método lanza la petición PUT a la API REST para modificar la Alarma
     */
    private void persistirAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarAlarma(alarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), alarma);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.ALARMA_MODIFICADA, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_MODIFICACION + Constantes.PISTA_TELEOPERADOR_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ +t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarAlarmasFragment listarAlarmasFragment = new ListarAlarmasFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, new ListarAlarmasOrdenadasFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonGuardarAlarmaModificada:
                if(comprobaciones()){
                    modificarDatos();
                    persistirAlarma();
                }
                break;
            case R.id.buttonVolverAlarma:
                volver();
                break;
        }
    }
}