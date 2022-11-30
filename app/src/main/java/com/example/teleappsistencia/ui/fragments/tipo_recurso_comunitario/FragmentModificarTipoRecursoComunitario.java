package com.example.teleappsistencia.ui.fragments.tipo_recurso_comunitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
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
 * Use the {@link FragmentModificarTipoRecursoComunitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentModificarTipoRecursoComunitario extends Fragment implements View.OnClickListener {

    // Declaración de atributos.
    private TextView textViewErrorPedirNombre;
    private EditText editTextPedirNombre;
    private Button buttonGuardar;
    private Button buttonVolver;
    private TipoRecursoComunitario tipoRecursoComunitario;

    public FragmentModificarTipoRecursoComunitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentModificarTipoRecursoComunitario.
     * @param tipoRecursoComunitario: Recibe el objeto a modificar.
     */
    public static FragmentModificarTipoRecursoComunitario newInstance(TipoRecursoComunitario tipoRecursoComunitario) {
        FragmentModificarTipoRecursoComunitario fragment = new FragmentModificarTipoRecursoComunitario();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.TIPO_RECURSO_COMUNITARIO_OBJETO, tipoRecursoComunitario);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Método que inicializa el objeto a modificar.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tipoRecursoComunitario = (TipoRecursoComunitario) getArguments().getSerializable(Constantes.TIPO_RECURSO_COMUNITARIO_OBJETO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_modificar_tipo_recurso_comunitario, container, false);

        // Se inicializan las variables.
        this.textViewErrorPedirNombre = (TextView) root.findViewById(R.id.textViewErrorPedirNombre);
        this.editTextPedirNombre = (EditText) root.findViewById(R.id.editTextPedirNombre);
        this.buttonGuardar = (Button) root.findViewById(R.id.buttonGuardar);
        this.buttonVolver = (Button) root.findViewById(R.id.buttonVolver);

        // Se establece la acción de pulsar los botones.
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);

        // Si el objeto no es nulo, se muestran sus atributos en el layout.
        if (this.tipoRecursoComunitario != null) {
            this.editTextPedirNombre.setText(this.tipoRecursoComunitario.getNombreTipoRecursoComunitario());
        }

        // Se inicializan los listeners.
        inicializarListeners();
        // Inflate the layout for this fragment
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
                accionBotonGuardar();
                break;
            case R.id.buttonVolver:
                accionBotonVolver();
                break;
        }
    }

    /**
     * Método para el botón guardar.
     */
    private void accionBotonGuardar() {
        if (validarTipoRecursoComunitario()) {
            modificarTipoRecursoComunitario();
        }
    }

    /**
     * Método para el botón volver.
     */
    private void accionBotonVolver() {
        getActivity().onBackPressed();
    }

    /**
     * Método que modifica el tipo de recurso comunitario.
     */
    private void modificarTipoRecursoComunitario() {
        String nombre = this.editTextPedirNombre.getText().toString();

        this.tipoRecursoComunitario.setNombreTipoRecursoComunitario(nombre);

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.putTipoRecursoComunitario(tipoRecursoComunitario.getId(), tipoRecursoComunitario, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.MENSAJE_MODIFICAR_TIPO_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
                    accionBotonVolver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_MENSAJE_MODIFICAR_TIPO_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Método que comprueba si el tipo de recurso comunitario es válido.
     *
     * @return: Retorna si el tipo de recurso comunitario es válido o no.
     */
    private boolean validarTipoRecursoComunitario() {
        boolean validNombre;

        validNombre = validarNombre(editTextPedirNombre.getText().toString());

        if (validNombre){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que inicializa los listeners con las acciones deseadas.
     */
    private void inicializarListeners() {
        this.editTextPedirNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarNombre(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Método que valida si el nombre está vacío.
     *
     * @param nombre: Recibe por parámetros el nombre.
     * @return: Retorna si el nombre es válido o no.
     */
    public boolean validarNombre(String nombre) {
        boolean valid = false;
        if ((nombre.isEmpty()) || (nombre.trim().equals(Constantes.VACIO))) {
            textViewErrorPedirNombre.setText(Constantes.ERROR_NOMBRE_VACIO);
            textViewErrorPedirNombre.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            textViewErrorPedirNombre.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }
}