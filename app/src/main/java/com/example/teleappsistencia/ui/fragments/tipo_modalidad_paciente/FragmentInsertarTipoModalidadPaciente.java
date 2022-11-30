package com.example.teleappsistencia.ui.fragments.tipo_modalidad_paciente;

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
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
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
 * Use the {@link FragmentInsertarTipoModalidadPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInsertarTipoModalidadPaciente extends Fragment implements View.OnClickListener {

    // Declaración de atributos.
    private TextView textViewErrorPedirNombre;
    private EditText editTextPedirNombre;
    private Button buttonGuardar;
    private Button buttonVolver;

    public FragmentInsertarTipoModalidadPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentInsertarTipoModalidadPaciente.
     */
    public static FragmentInsertarTipoModalidadPaciente newInstance() {
        FragmentInsertarTipoModalidadPaciente fragment = new FragmentInsertarTipoModalidadPaciente();
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
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_insertar_tipo_modalidad_paciente, container, false);

        // Se inicializan las variables.
        this.textViewErrorPedirNombre = (TextView) root.findViewById(R.id.textViewErrorPedirNombre);
        this.editTextPedirNombre = (EditText) root.findViewById(R.id.editTextPedirNombre);
        this.buttonGuardar = (Button) root.findViewById(R.id.buttonGuardar);
        this.buttonVolver = (Button) root.findViewById(R.id.buttonVolver);

        // Se establece la acción de pulsar los botones.
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);

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
        if (validarTipoModalidadPaciente()) {
            insertarTipoModalidadPaciente();
        }
    }

    /**
     * Método para el botón volver.
     */
    private void accionBotonVolver() {
        getActivity().onBackPressed();
    }

    /**
     * Método que inserta el tipo de modalidad de paciente.
     */
    private void insertarTipoModalidadPaciente() {
        String nombre = this.editTextPedirNombre.getText().toString();

        TipoModalidadPaciente tipoModalidadPaciente = new TipoModalidadPaciente();
        tipoModalidadPaciente.setNombre(nombre);

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.postTipoModalidadPaciente(tipoModalidadPaciente, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object tipoModalidadPaciente = response.body();
                    System.out.println(tipoModalidadPaciente);
                    Toast.makeText(getContext(), Constantes.MENSAJE_INSERTAR_TIPO_MODALIDAD_PACIENTE, Toast.LENGTH_SHORT).show();
                    borrarEditTexts();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_MENSAJE_INSERTAR_TIPO_MODALIDAD_PACIENTE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Método que borra todos los datos de los editText y quita los mensajes de error.
     */
    private void borrarEditTexts() {
        this.editTextPedirNombre.setText(Constantes.VACIO);
        this.textViewErrorPedirNombre.setVisibility(View.GONE);
    }

    /**
     * Método que comprueba si el tipo de modalidad de paciente es válido.
     *
     * @return: Retorna si el tipo de modalidad de paciente es válido o no.
     */
    private boolean validarTipoModalidadPaciente() {
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