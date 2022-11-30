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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarTipoAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarTipoAlarmaFragment extends Fragment implements View.OnClickListener{

    private TipoAlarma tipoAlarma;
    private List<ClasificacionAlarma> lClasificacionAlarma;
    private EditText editTextCodigoTipoAlarmaModificar;
    private EditText editTextNombreTipoAlarmaModificar;
    private RadioButton radioButtonSi;
    private RadioButton radioButtonNo;
    private Spinner spinnerClasificacionTipoAlarma;
    private Button buttonGuardarTipoAlarmaModificado;
    private Button buttonVolverTipoAlarma;

    public ModificarTipoAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModificarTipoAlarmaFragment.
     */

    public static ModificarTipoAlarmaFragment newInstance(TipoAlarma tipoAlarma) {
        ModificarTipoAlarmaFragment fragment = new ModificarTipoAlarmaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_TIPOALARMA, tipoAlarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tipoAlarma = (TipoAlarma) getArguments().getSerializable(Constantes.ARG_TIPOALARMA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modificar_tipo_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos
        if(this.tipoAlarma != null){
            cargarDatos();
        }


        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view){
        this.editTextCodigoTipoAlarmaModificar = (EditText) view.findViewById(R.id.editTextCodigoTipoAlarmaModificar);
        this.editTextNombreTipoAlarmaModificar = (EditText) view.findViewById(R.id.editTextNombreTipoAlarmaModificar);
        this.spinnerClasificacionTipoAlarma = (Spinner) view.findViewById(R.id.spinnerClasificacionTipoAlarma);
        this.radioButtonSi = (RadioButton) view.findViewById(R.id.radioButtonSi);
        this.radioButtonNo = (RadioButton) view.findViewById(R.id.radioButtonNo);
        this.buttonGuardarTipoAlarmaModificado = (Button) view.findViewById(R.id.buttonGuardarTipoAlarmaModificado);
        this.buttonVolverTipoAlarma = (Button) view.findViewById(R.id.buttonVolverTipoAlarma);
    }

    /**
     * Asignamos la propia clase como OnClickListener, ya que abajo tenemos el método implementado
     */
    private void asignarListener(){
        this.buttonGuardarTipoAlarmaModificado.setOnClickListener(this);
        this.buttonVolverTipoAlarma.setOnClickListener(this);
    }

    /**
     * Este método carga los datos del Tipo de Alarma en el layout.
     */
    private void cargarDatos(){
        this.editTextCodigoTipoAlarmaModificar.setText(this.tipoAlarma.getCodigo());
        this.editTextNombreTipoAlarmaModificar.setText(this.tipoAlarma.getNombre());
        if(this.tipoAlarma.isEsDispositivo()){
            this.radioButtonSi.setChecked(true);
        }
        else{
            this.radioButtonNo.setChecked(true);
        }
        cargarSpinner();
    }

    /**
     * Este método hace una petición a la API REST de la lista con todas las Clasificaciones de Alarma.
     * Las carga en un spinner.
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
                    seleccionarItem();
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
     * Este método busca la posición del objeto Clasificación Alarma en la lista y selecciona el item
     * del spinner que corresponde a esa posición. Eso hace que el spinner carge de inicio con el la Clasificación
     * de Alarma que trae el objeto a modificar.
     */
    private void seleccionarItem(){
        boolean encontrado = false;
        ClasificacionAlarma clasificacionAlarma = (ClasificacionAlarma) Utilidad.getObjeto(this.tipoAlarma.getClasificacionAlarma(), "ClasificacionAlarma");
        ClasificacionAlarma itemSpinner;
        int i = 0;
        while(!encontrado && i < this.lClasificacionAlarma.size()){
            itemSpinner = this.lClasificacionAlarma.get(i);
            if(itemSpinner.getId() == clasificacionAlarma.getId()){
                this.spinnerClasificacionTipoAlarma.setSelection(i);
                encontrado = true;
            }
            i++;
        }
    }

    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextNombreTipoAlarmaModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_NOMBRE, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextCodigoTipoAlarmaModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_CODIGO_TIPO_ALARMA, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Modificamos los datos del objeto alarma que nos llegó al crear el fragment
     */
    private void modificarDatosTipoAlarma(){
        ClasificacionAlarma clasificacionAlarma = (ClasificacionAlarma) spinnerClasificacionTipoAlarma.getSelectedItem();
        this.tipoAlarma.setNombre(this.editTextNombreTipoAlarmaModificar.getText().toString());
        this.tipoAlarma.setCodigo(this.editTextCodigoTipoAlarmaModificar.getText().toString());
        this.tipoAlarma.setEsDispositivo(radioButtonSi.isChecked());
        this.tipoAlarma.setClasificacionAlarma(clasificacionAlarma.getId());
    }

    /**
     * Este método lanza la petición PUT a la API REST para modificar el Tipo de Alarma
     */
    private void persistirTipoAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarTipoAlarma(this.tipoAlarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), this.tipoAlarma);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.MODIFICADO_CON_EXITO, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
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
        ListarTipoAlarmaFragment listarTipoAlarmaFragment = new ListarTipoAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarTipoAlarmaFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonGuardarTipoAlarmaModificado:
                if(comprobaciones()){
                    modificarDatosTipoAlarma();
                    persistirTipoAlarma();
                }
                break;
            case R.id.buttonVolverTipoAlarma:
                volver();
                break;
        }
    }
}