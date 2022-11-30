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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarClasificacionAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarClasificacionAlarmaFragment extends Fragment implements View.OnClickListener{

    private ClasificacionAlarma clasificacionAlarma;
    private EditText editTextNombreClasificacionAlarmaModificar;
    private EditText editTextCodigoClasificacionAlarmaModificar;
    private Button buttonGuardarClasificacionAlarma;
    private Button buttonVolverClasificacionAlarma;

    public ModificarClasificacionAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clasificacionAlarma se recibe por parámetros el objeto Clasificación Alarma que se va a modificar
     * @return A new instance of fragment ModificarClasificacionAlarmaFragment.
     */
    public static ModificarClasificacionAlarmaFragment newInstance(ClasificacionAlarma clasificacionAlarma) {
        ModificarClasificacionAlarmaFragment fragment = new ModificarClasificacionAlarmaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_CLASIFICACIONALARMA, clasificacionAlarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobamos que la instancia se ha creado con argumentos y si es así las recogemos.
        if (getArguments() != null) {
            this.clasificacionAlarma = (ClasificacionAlarma) getArguments().getSerializable(Constantes.ARG_CLASIFICACIONALARMA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modificar_clasificacion_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos
        if(this.clasificacionAlarma != null){
            cargarDatos();
        }

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view) {
        this.editTextNombreClasificacionAlarmaModificar = (EditText) view.findViewById(R.id.editTextNombreClasificacionAlarmaModificar);
        this.editTextCodigoClasificacionAlarmaModificar = (EditText) view.findViewById(R.id.editTextCodigoClasificacionAlarmaModificar);
        this.buttonGuardarClasificacionAlarma = (Button) view.findViewById(R.id.buttonGuardarClasificacionAlarma);
        this.buttonVolverClasificacionAlarma = (Button) view.findViewById(R.id.buttonVolverClasificacionAlarma);

        // Añadimos al campo código un filtro de mayúsculas y longitud máxima
        this.editTextCodigoClasificacionAlarmaModificar.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});
    }

    /**
     * Asignamos la propia clase como OnClickListener, ya que abajo tenemos el método implementado
     */
    private void asignarListener(){
        this.buttonGuardarClasificacionAlarma.setOnClickListener(this);
        this.buttonVolverClasificacionAlarma.setOnClickListener(this);
    }

    /**
     * Este método carga los datos de la Clasificación de Alarma  en el layout.
     */
    private void cargarDatos(){
        this.editTextNombreClasificacionAlarmaModificar.setText(this.clasificacionAlarma.getNombre());
        this.editTextCodigoClasificacionAlarmaModificar.setText(this.clasificacionAlarma.getCodigo());
    }

    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextNombreClasificacionAlarmaModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_NOMBRE, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextCodigoClasificacionAlarmaModificar.getText().toString().length() < 2) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_CODIGO_2_3_LETRAS, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Modificamos los datos del objeto Clasificación Alarma que nos llegó al crear el fragment
     */
    private void modificarDatos(){
        this.clasificacionAlarma.setNombre(this.editTextNombreClasificacionAlarmaModificar.getText().toString());
        this.clasificacionAlarma.setCodigo(this.editTextCodigoClasificacionAlarmaModificar.getText().toString());
    }

    /**
     * Este método lanza la petición PUT a la API REST para modificar la Clasificación de alarma
     */
    private void persistirClasificacionAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarClasificacionAlarma(this.clasificacionAlarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), this.clasificacionAlarma);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.errorBody() == null){
                    Toast.makeText(getContext(), Constantes.MODIFICADO_CON_EXITO, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_MODIFICACION + response.message() , Toast.LENGTH_LONG).show();
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
        ListarClasificacionAlarmaFragment listarClasificacionAlarmaFragment = new ListarClasificacionAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarClasificacionAlarmaFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonGuardarClasificacionAlarma:
                if(comprobaciones()){
                    modificarDatos();
                    persistirClasificacionAlarma();
                }
                break;
            case R.id.buttonVolverClasificacionAlarma:
                volver();
                break;
        }
    }
}