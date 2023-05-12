package com.example.teleappsistencia.ui.fragments.recursos;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecursosOpcionesFragment extends Fragment implements View.OnClickListener {

    // Declaración de atributos.
    private TextView textViewErrorPedirNombre;
    private TextView textViewErrorPedirTelefono;
    private TextView textViewErrorPedirLocalidad;
    private TextView textViewErrorPedirProvincia;
    private TextView textViewErrorPedirDireccion;
    private TextView textViewErrorPedirCodigoPostal;
    private EditText nombreRecursoComunitario;
    private EditText telefonoRecursoComunitario;
    private EditText editTextTipoRecursoComunitario;
    private EditText localidadRecursoComunitario;
    private EditText provinciaRecursoComunitario;
    private EditText direccionRecursoComunitario;
    private EditText codigoPostalRecursoComunitario;
    private Button buttonGuardar;
    private Button buttonVolver;
    private RecursoComunitario recursoComunitario;
    private String opcion;
    private int idClasificacionRecurso;
    private Spinner spinnerTipoRecursoComunitario;

    public RecursosOpcionesFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recursoComunitario: Recibe el recurso comunitario.
     * @param opcion:             Recibe el id del boton presionado.
     * @param idClasificacion:    Recibe el id de la clasificación de recursos.
     * @return A new instance of fragment ConsultarRecursoComunitario.
     */
    public static RecursosOpcionesFragment newInstance(RecursoComunitario recursoComunitario, String opcion, int idClasificacion) {
        RecursosOpcionesFragment fragment = new RecursosOpcionesFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.KEY_RECURSO_COMUNITARIO, recursoComunitario);
        args.putSerializable(Constantes.KEY_OPCION_SELECCIONADA, opcion);
        args.putSerializable(Constantes.KEY_ID_CLASIFICACION_RECURSOS, idClasificacion);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Crea una nueva instancia con dos atributos, opción y idClasificacion.
     * Se utiliza para la opción de NUEVO. Ya que el parametro recursoComunitario no
     * es necesario en esta opción.
     *
     * @param opcion:             Recibe el id del boton presionado.
     * @param idClasificacion:    Recibe el id de la clasificación de recursos.
     * @return A new instance of fragment ConsultarRecursoComunitario.
     */
    public static RecursosOpcionesFragment newInstance(String opcion, int idClasificacion) {
        RecursosOpcionesFragment fragment = new RecursosOpcionesFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.KEY_OPCION_SELECCIONADA, opcion);
        args.putSerializable(Constantes.KEY_ID_CLASIFICACION_RECURSOS, idClasificacion);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Método que inicializa el objeto a consultar.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.opcion = (String) getArguments().getSerializable(Constantes.KEY_OPCION_SELECCIONADA);
            this.idClasificacionRecurso = (int) getArguments().getSerializable(Constantes.KEY_ID_CLASIFICACION_RECURSOS);
            // Controla la excepción que salta si se crea un nuevo recurso comunitario ya que el atributo recursoComunitario tiene un valor null.
            try{
                this.recursoComunitario = (RecursoComunitario) getArguments().getSerializable(Constantes.KEY_RECURSO_COMUNITARIO);
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TipoRecursoComunitario tipoRecursoComunitario = new TipoRecursoComunitario(null);
        Direccion direccion = new Direccion();

        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_opciones_recursos, container, false);

        // Método que muestra los valores del recurso comunitario.
        if(this.recursoComunitario != null){
            tipoRecursoComunitario = (TipoRecursoComunitario) Utilidad.getObjeto(recursoComunitario.getTipoRecursoComunitario(), Constantes.TIPO_RECURSO_COMUNITARIO);
            direccion = (Direccion) Utilidad.getObjeto(this.recursoComunitario.getDireccion(), Constantes.DIRECCION);
        }


        // Se inicializan las variables.
        this.textViewErrorPedirNombre = (TextView) root.findViewById(R.id.textViewErrorNombre);
        this.nombreRecursoComunitario = (EditText) root.findViewById(R.id.editTextNombre);
        this.textViewErrorPedirTelefono = (TextView) root.findViewById(R.id.textViewErrorTelefono);
        this.telefonoRecursoComunitario = (EditText) root.findViewById(R.id.editTextPhoneTelefono);
        this.textViewErrorPedirLocalidad = (TextView) root.findViewById(R.id.textViewErrorPedirLocalidad);
        this.localidadRecursoComunitario = (EditText) root.findViewById(R.id.editTextLocalidad);
        this.textViewErrorPedirProvincia = (TextView) root.findViewById(R.id.textViewErrorProvincia);
        this.provinciaRecursoComunitario = (EditText) root.findViewById(R.id.editTextProvincia);
        this.textViewErrorPedirDireccion = (TextView) root.findViewById(R.id.textViewErrorDireccion);
        this.direccionRecursoComunitario = (EditText) root.findViewById(R.id.editTextDireccion);
        this.textViewErrorPedirCodigoPostal = (TextView) root.findViewById(R.id.textViewErrorCodigoPostal);
        this.codigoPostalRecursoComunitario = (EditText) root.findViewById(R.id.editTextTextPostalAddressCodigoPostal);

        this.editTextTipoRecursoComunitario = (EditText) root.findViewById(R.id.editTextTipoRecursoComunitario);
        this.spinnerTipoRecursoComunitario = (Spinner) root.findViewById(R.id.spinnerTipoRecursoComunitario);

        this.buttonGuardar = (Button) root.findViewById(R.id.buttonGuardar);
        this.buttonVolver = (Button) root.findViewById(R.id.buttonVolver);

        switch (this.opcion) {

            // Consultar.
            case Constantes.CONSULTAR:
                this.nombreRecursoComunitario.setKeyListener(null);
                this.telefonoRecursoComunitario.setKeyListener(null);
                this.localidadRecursoComunitario.setKeyListener(null);
                this.provinciaRecursoComunitario.setKeyListener(null);
                this.direccionRecursoComunitario.setKeyListener(null);
                this.codigoPostalRecursoComunitario.setKeyListener(null);
                this.editTextTipoRecursoComunitario.setVisibility(View.VISIBLE);
                this.editTextTipoRecursoComunitario.setKeyListener(null);
                this.spinnerTipoRecursoComunitario.setVisibility(View.INVISIBLE);
                this.buttonGuardar.setVisibility(View.GONE);
                this.buttonVolver.setOnClickListener(this);

                this.nombreRecursoComunitario.setText(this.recursoComunitario.getNombre());
                this.telefonoRecursoComunitario.setText(this.recursoComunitario.getTelefono());
                this.editTextTipoRecursoComunitario.setText(tipoRecursoComunitario.getNombreTipoRecursoComunitario());
                this.localidadRecursoComunitario.setText(direccion.getLocalidad());
                this.provinciaRecursoComunitario.setText(direccion.getProvincia());
                this.direccionRecursoComunitario.setText(direccion.getDireccion());
                this.codigoPostalRecursoComunitario.setText(direccion.getCodigoPostal());
                break;

            // Editar.
            case Constantes.MODIFICAR:
                this.buttonGuardar.setOnClickListener(this);
                this.buttonVolver.setOnClickListener(this);
                this.editTextTipoRecursoComunitario.setVisibility(View.INVISIBLE);

                cargarSpinner();

                this.nombreRecursoComunitario.setText(this.recursoComunitario.getNombre());
                this.telefonoRecursoComunitario.setText(this.recursoComunitario.getTelefono());
                this.localidadRecursoComunitario.setText(direccion.getLocalidad());
                this.provinciaRecursoComunitario.setText(direccion.getProvincia());
                this.direccionRecursoComunitario.setText(direccion.getDireccion());
                this.codigoPostalRecursoComunitario.setText(direccion.getCodigoPostal());
                break;

            // Nuevo.
            case Constantes.NUEVO:
                this.buttonGuardar.setOnClickListener(this);
                this.buttonVolver.setOnClickListener(this);
                this.editTextTipoRecursoComunitario.setVisibility(View.INVISIBLE);

                cargarSpinner();
                break;
        }

        return root;
    }

    /**
     * Método para establecer las acciones de los botones, según sea un botón u otro.
     *
     * @param view: Recibe por parámetros la vista.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGuardar:
                switch (this.opcion) {
                    case Constantes.NUEVO:
                        accionBotonGuardarNuevo();
                        break;
                    case Constantes.MODIFICAR:
                        accionBotonGuardarModificado();
                        break;
                }
                break;
            case R.id.buttonVolver:
                accionBotonVolver();
                break;
        }
    }

    /**
     * Método para el botón volver.
     */
    private void accionBotonVolver() {
        getActivity().onBackPressed();
    }

    /**
     * Método para el botón guardar cuando se modifica un recurso.
     */
    private void accionBotonGuardarModificado() {
        RecursoComunitario recursoComunitarioModificado = generarRecurso();
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.putRecursoComunitario(this.recursoComunitario.getId(), recursoComunitarioModificado, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object recurso = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_MODIFICADO_RECURSO);
                    // Necesita un tiempo para ejecutarse, de no ser así, salta excepción.
                    new CountDownTimer(500, 500) {
                        public void onTick(long millisUntilFinished) {
                            // No se necesita hacer nada en este caso
                        }

                        public void onFinish() {
                            // Código para el segundo comando después de esperar medio segundo
                            getActivity().onBackPressed();
                        }
                    }.start();
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método para el botón guardar cuando se crea un nuevo recurso.
     */
    private void accionBotonGuardarNuevo() {
        RecursoComunitario recursoComunitarioModificado = generarRecurso();
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.postRecursoComunitario(recursoComunitarioModificado, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object recurso = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_CREADO_RECURSO);
                    // Necesita tiempo para ejecutarse.
                    new CountDownTimer(500, 500) {
                        public void onTick(long millisUntilFinished) {
                            // No se necesita hacer nada en este caso
                        }

                        public void onFinish() {
                            // Código para el segundo comando después de esperar medio segundo
                            getActivity().onBackPressed();
                        }
                    }.start();
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método para cargar el Spinner.
     */
    private void cargarSpinner() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<TipoRecursoComunitario>> call = apiService.getTiposRecursosComunitariosPorId(this.idClasificacionRecurso, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoRecursoComunitario>>() {
            @Override
            public void onResponse(Call<List<TipoRecursoComunitario>> call, Response<List<TipoRecursoComunitario>> response) {
                if (response.isSuccessful()) {
                    List<TipoRecursoComunitario> lTiposRecursos = response.body();
                    // Este método sobre escribe el getView y el getDropDownView de la clase ArrayAdapter para que guarde un objeto y muestre su nombre con un tamaño determinado en dimens.xml.
                    ArrayAdapter<TipoRecursoComunitario> lTipoRecursosSpinner = new ArrayAdapter<TipoRecursoComunitario>(getContext(), android.R.layout.simple_spinner_item, lTiposRecursos) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView textView = (TextView) super.getView(position, convertView, parent);
                            TipoRecursoComunitario objeto = getItem(position);
                            textView.setText(objeto.getNombreTipoRecursoComunitario());
                            float tamañoTexto = getResources().getDimension(R.dimen.texto_tamaño);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamañoTexto);
                            return textView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            TextView text = (TextView) super.getDropDownView(position, convertView, parent);
                            float tamañoTexto = getResources().getDimension(R.dimen.texto_tamaño);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamañoTexto);
                            text.setPadding(10, 5, 10, 5);
                            return text;
                        }
                    };
                    spinnerTipoRecursoComunitario.setAdapter(lTipoRecursosSpinner);
                }
            }

            @Override
            public void onFailure(Call<List<TipoRecursoComunitario>> call, Throwable t) {

            }
        });
    }

    /**
     * Metodo que genera el Recurso Modificado.
     * @return
     */
    private RecursoComunitario generarRecurso(){
        RecursoComunitario recursoComunitarioModificado = new RecursoComunitario();
        Direccion direccionModificada = new Direccion();

        recursoComunitarioModificado.setNombre(this.nombreRecursoComunitario.getText().toString());
        recursoComunitarioModificado.setTelefono(this.telefonoRecursoComunitario.getText().toString());

        // Para hacer el PUT, es necesario que el objeto recursoComunitario tenga el atributo
        // tipoRecursoComunitario como int, ya que en PostMan recibe el id, no el objeto.
        TipoRecursoComunitario tipoRecursoComunitario = (TipoRecursoComunitario) spinnerTipoRecursoComunitario.getSelectedItem();
        int id_tipo_comunitario = tipoRecursoComunitario.getId();
        recursoComunitarioModificado.setTipoRecursoComunitario(id_tipo_comunitario);

        direccionModificada.setLocalidad(this.localidadRecursoComunitario.getText().toString());
        direccionModificada.setProvincia(this.provinciaRecursoComunitario.getText().toString());
        direccionModificada.setDireccion(this.direccionRecursoComunitario.getText().toString());
        direccionModificada.setCodigoPostal(this.codigoPostalRecursoComunitario.getText().toString());

        recursoComunitarioModificado.setDireccion(direccionModificada);

        return recursoComunitarioModificado;
    }
}

