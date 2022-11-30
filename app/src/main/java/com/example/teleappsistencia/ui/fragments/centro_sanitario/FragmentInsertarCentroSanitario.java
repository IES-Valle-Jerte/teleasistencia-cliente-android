package com.example.teleappsistencia.ui.fragments.centro_sanitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInsertarCentroSanitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInsertarCentroSanitario extends Fragment implements View.OnClickListener {

    // Declaración de atributos.
    private TextView textViewErrorPedirNombre;
    private TextView textViewErrorPedirTelefono;
    private TextView textViewErrorPedirLocalidad;
    private TextView textViewErrorPedirProvincia;
    private TextView textViewErrorPedirDireccion;
    private TextView textViewErrorPedirCodigoPostal;
    private EditText editTextPedirNombre;
    private EditText editTextPhonePedirTelefono;
    private EditText editTextPedirLocalidad;
    private EditText editTextPedirProvincia;
    private EditText editTextPedirDireccion;
    private EditText editTextTextPostalAddressPedirCodigoPostal;
    private Button buttonGuardar;
    private Button buttonVolver;
    private CentroSanitario centroSanitario;
    private Spinner spinnerTipoCentroSanitario;

    public FragmentInsertarCentroSanitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters..
     * @return A new instance of fragment FragmentInsertarCentroSanitario.
     */
    public static FragmentInsertarCentroSanitario newInstance() {
        FragmentInsertarCentroSanitario fragment = new FragmentInsertarCentroSanitario();
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
        View root = inflater.inflate(R.layout.fragment_insertar_centro_sanitario, container, false);

        // Se inicializan las variables.
        this.textViewErrorPedirNombre = (TextView) root.findViewById(R.id.textViewErrorPedirNombre);
        this.editTextPedirNombre = (EditText) root.findViewById(R.id.editTextPedirNombre);
        this.textViewErrorPedirTelefono = (TextView) root.findViewById(R.id.textViewErrorPedirTelefono);
        this.editTextPhonePedirTelefono = (EditText) root.findViewById(R.id.editTextPhonePedirTelefono);
        this.textViewErrorPedirLocalidad = (TextView) root.findViewById(R.id.textViewErrorPedirLocalidad);
        this.editTextPedirLocalidad = (EditText) root.findViewById(R.id.editTextPedirLocalidad);
        this.textViewErrorPedirProvincia = (TextView) root.findViewById(R.id.textViewErrorPedirProvincia);
        this.editTextPedirProvincia = (EditText) root.findViewById(R.id.editTextPedirProvincia);
        this.textViewErrorPedirDireccion = (TextView) root.findViewById(R.id.textViewErrorPedirDireccion);
        this.editTextPedirDireccion = (EditText) root.findViewById(R.id.editTextPedirDireccion);
        this.textViewErrorPedirCodigoPostal = (TextView) root.findViewById(R.id.textViewErrorPedirCodigoPostal);
        this.editTextTextPostalAddressPedirCodigoPostal = (EditText) root.findViewById(R.id.editTextTextPostalAddressPedirCodigoPostal);
        this.spinnerTipoCentroSanitario = (Spinner) root.findViewById(R.id.spinnerTipoCentroSanitario);

        this.buttonGuardar = (Button) root.findViewById(R.id.buttonGuardar);
        this.buttonVolver = (Button) root.findViewById(R.id.buttonVolver);

        // Se establece la acción de pulsar los botones.
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);

        inicializarSpinnerTipoCentroSanitario();

        // Se inicializan los listeners.
        inicializarListeners();
        // Inflate the layout for this fragment
        return root;
    }

    /**
     * Método que se encarga de cargar los tipos de centros sanitarios en el spinner.
     */
    public void inicializarSpinnerTipoCentroSanitario() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<TipoCentroSanitario>> call = apiService.getTipoCentroSanitario(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoCentroSanitario>>() {
            @Override
            public void onResponse(Call<List<TipoCentroSanitario>> call, Response<List<TipoCentroSanitario>> response) {
                if (response.isSuccessful()) {
                    List<TipoCentroSanitario> listadoTipoCentroSanitario = response.body();
                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listadoTipoCentroSanitario);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipoCentroSanitario.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_DATOS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoCentroSanitario>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_DATOS, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
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
        if (validarCentroSanitario()) {
            insertarCentroSanitario();
        }
    }

    /**
     * Método para el botón volver.
     */
    private void accionBotonVolver() {
        getActivity().onBackPressed();
    }

    /**
     * Método que inserta el centro sanitario.
     */
    private void insertarCentroSanitario() {
        String nombre = this.editTextPedirNombre.getText().toString();
        String telefono = this.editTextPhonePedirTelefono.getText().toString();
        TipoCentroSanitario tipoCentroSanitario = (TipoCentroSanitario) this.spinnerTipoCentroSanitario.getSelectedItem();
        Direccion direccion = new Direccion();
        direccion.setLocalidad(this.editTextPedirLocalidad.getText().toString());
        direccion.setProvincia(this.editTextPedirProvincia.getText().toString());
        direccion.setDireccion(this.editTextPedirDireccion.getText().toString());
        direccion.setCodigoPostal(this.editTextTextPostalAddressPedirCodigoPostal.getText().toString());

        this.centroSanitario = new CentroSanitario();
        centroSanitario.setNombre(nombre);
        centroSanitario.setTelefono(telefono);
        centroSanitario.setTipoCentroSanitario(tipoCentroSanitario.getId());
        centroSanitario.setDireccion(direccion);

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.postCentroSanitario(centroSanitario, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.MENSAJE_INSERTAR_CENTRO_SANITARIO, Toast.LENGTH_SHORT).show();
                    borrarEditTexts();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_MENSAJE_INSERTAR_CENTRO_SANITARIO, Toast.LENGTH_SHORT).show();
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
        this.editTextPhonePedirTelefono.setText(Constantes.VACIO);
        this.textViewErrorPedirTelefono.setVisibility(View.GONE);
        this.editTextPedirLocalidad.setText(Constantes.VACIO);
        this.textViewErrorPedirLocalidad.setVisibility(View.GONE);
        this.editTextPedirProvincia.setText(Constantes.VACIO);
        this.textViewErrorPedirProvincia.setVisibility(View.GONE);
        this.editTextPedirDireccion.setText(Constantes.VACIO);
        this.textViewErrorPedirDireccion.setVisibility(View.GONE);
        this.editTextTextPostalAddressPedirCodigoPostal.setText(Constantes.VACIO);
        this.textViewErrorPedirCodigoPostal.setVisibility(View.GONE);
    }

    /**
     * Método que comprueba si el centro sanitario es válido.
     *
     * @return: Retorna si el centro sanitario es válido o no.
     */
    private boolean validarCentroSanitario() {
        boolean validNombre;
        boolean validTelefono;
        boolean validLocalidad;
        boolean validProvincia;
        boolean validDireccion;
        boolean validCodigoPostal;

        validNombre = validarNombre(editTextPedirNombre.getText().toString());
        validTelefono = validarTelefono(editTextPhonePedirTelefono.getText().toString());
        validLocalidad = validarLocalidad(editTextPedirLocalidad.getText().toString());
        validProvincia = validarProvincia(editTextPedirProvincia.getText().toString());
        validDireccion = validarDireccion(editTextPedirDireccion.getText().toString());
        validCodigoPostal = validarCodigoPostal(editTextTextPostalAddressPedirCodigoPostal.getText().toString());

        if (validNombre && validTelefono && validLocalidad && validProvincia && validDireccion && validCodigoPostal) {
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

    /**
     * Método que valida si el teléfono está vacío.
     *
     * @param telefono: Recibe por parámetros el teléfono.
     * @return: Retorna si el teléfono es válido o no.
     */
    public boolean validarTelefono(String telefono) {
        boolean valid = false;
        if ((telefono.isEmpty()) || (telefono.trim().equals(Constantes.VACIO))) {
            textViewErrorPedirTelefono.setText(Constantes.ERROR_TELEFONO_VACIO);
            textViewErrorPedirTelefono.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            textViewErrorPedirTelefono.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método que valida si la localidad está vacía.
     *
     * @param localidad: Recibe por parámetros la localidad.
     * @return: Retorna si la localidad es válida o no.
     */
    public boolean validarLocalidad(String localidad) {
        boolean valid = false;
        if ((localidad.isEmpty()) || (localidad.trim().equals(Constantes.VACIO))) {
            textViewErrorPedirLocalidad.setText(Constantes.ERROR_LOCALIDAD_VACIO);
            textViewErrorPedirLocalidad.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            textViewErrorPedirLocalidad.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método que valida si la provincia está vacía.
     *
     * @param provincia: Recibe por parámetros la provincia.
     * @return: Retorna si la provincia es válida o no.
     */
    public boolean validarProvincia(String provincia) {
        boolean valid = false;
        if ((provincia.isEmpty()) || (provincia.trim().equals(Constantes.VACIO))) {
            textViewErrorPedirProvincia.setText(Constantes.ERROR_PROVINCIA_VACIO);
            textViewErrorPedirProvincia.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            textViewErrorPedirProvincia.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método que valida si la dirección está vacía.
     *
     * @param direccion: Recibe por parámetros la dirección.
     * @return: Retorna si la dirección es válida o no.
     */
    public boolean validarDireccion(String direccion) {
        boolean valid = false;
        if ((direccion.isEmpty()) || (direccion.trim().equals(Constantes.VACIO))) {
            textViewErrorPedirDireccion.setText(Constantes.ERROR_DIRECCION_VACIO);
            textViewErrorPedirDireccion.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            textViewErrorPedirDireccion.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método que valida si el código postal está vacío.
     *
     * @param codigoPostal: Recibe por parámetros el código postal.
     * @return: Retorna si el código postal es válido o no.
     */
    public boolean validarCodigoPostal(String codigoPostal) {
        boolean valid = false;
        if ((codigoPostal.isEmpty()) || (codigoPostal.trim().equals(Constantes.VACIO))) {
            textViewErrorPedirCodigoPostal.setText(Constantes.ERROR_CODIGO_POSTAL_VACIO);
            textViewErrorPedirCodigoPostal.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            textViewErrorPedirCodigoPostal.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }
}