package com.example.teleappsistencia.ui.fragments.direccion;

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

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.modelos.Direccion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertarDireccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarDireccionFragment extends Fragment {

    private Button btn_insertar;
    private EditText editText_localidad;
    private EditText editText_provincia;
    private EditText editText_direccion;
    private EditText editText_codigoPostal;

    private TextView textView_error_localidad;
    private TextView textView_error_provincia;
    private TextView textView_error_direccion;
    private TextView textView_error_codigoPostal;

    public InsertarDireccionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarDireccionFragment.
     */
    public static InsertarDireccionFragment newInstance() {
        InsertarDireccionFragment fragment = new InsertarDireccionFragment();
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
        View view = inflater.inflate(R.layout.fragment_insertar_direccion, container, false);

        this.btn_insertar = (Button) view.findViewById(R.id.btn_guardar_direccion);
        this.editText_localidad = (EditText) view.findViewById(R.id.editText_localidad_direccion);
        this.editText_provincia = (EditText) view.findViewById(R.id.editText_provincia_direccion);
        this.editText_direccion = (EditText) view.findViewById(R.id.editText_direccion_direccion);
        this.editText_codigoPostal = (EditText) view.findViewById(R.id.editText_codigoPostal_direccion);

        this.textView_error_localidad = (TextView) view.findViewById(R.id.textView_error_localidad_direccion);
        this.textView_error_provincia = (TextView) view.findViewById(R.id.textView_error_provincia_direccion);
        this.textView_error_direccion = (TextView) view.findViewById(R.id.textView_error_direccion_direccion);
        this.textView_error_codigoPostal = (TextView) view.findViewById(R.id.textView_error_codigoPostal_direccion);

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDireccion()) {
                    insertarDireccion();
                }
            }
        });

        inicializarListeners();

        return view;
    }

    /**
     * Método para insertar una nueva dirección en la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void insertarDireccion() {
        String localidad = this.editText_localidad.getText().toString();
        String provincia = this.editText_provincia.getText().toString();
        String direc = this.editText_direccion.getText().toString();
        String codigoPostal = this.editText_codigoPostal.getText().toString();

        Direccion direccion = new Direccion();
        direccion.setLocalidad(localidad);
        direccion.setProvincia(provincia);
        direccion.setDireccion(direc);
        direccion.setCodigoPostal(codigoPostal);

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.addDireccion(direccion, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object direccion = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_CREADO_DIRECCION);
                    borrarEditTexts();
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
     * Método que borra todos los datos de los EditText y quita los mensajes de error.
     */
    private void borrarEditTexts() {
        this.editText_localidad.setText(Constantes.STRING_VACIO);
        this.editText_provincia.setText(Constantes.STRING_VACIO);
        this.editText_direccion.setText(Constantes.STRING_VACIO);
        this.editText_codigoPostal.setText(Constantes.STRING_VACIO);

        this.textView_error_localidad.setVisibility(View.GONE);
        this.textView_error_provincia.setVisibility(View.GONE);
        this.textView_error_direccion.setVisibility(View.GONE);
        this.textView_error_codigoPostal.setVisibility(View.GONE);
    }

    /**
     * Método que revisa si los datos de los EditText son válidos.
     * @return Devuelve true si es válido de lo contrario devuelve false.
     */
    private boolean validarDireccion() {
        boolean validLocalidad, validProvincia, validDireccion, validCodigoPostal;

        validLocalidad = validarLocalidad(editText_localidad.getText().toString());
        validProvincia = validarProvincia(editText_provincia.getText().toString());
        validDireccion = validarDir(editText_direccion.getText().toString());
        validCodigoPostal = validarCodigoPostal(editText_codigoPostal.getText().toString());


        if((validLocalidad) && (validProvincia) && (validDireccion) && (validCodigoPostal)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que inicializa todos los TextWachers de los EditTexts.
     * Los TextWachers se encuentran constantemente revisando si se ha añadido algo a los EditText.
     * Si se ha añadido algo, revisa si lo escrito en el EditText es válido o no.
     */
    private void inicializarListeners() {
        this.editText_localidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarLocalidad(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_provincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarProvincia(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_direccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarDir(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_codigoPostal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarCodigoPostal(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Método para validar el campo localidad.
     * @param localidad
     * @return
     */
    public boolean validarLocalidad(String localidad) {
        boolean valid = false;
        if ((localidad.isEmpty()) || (localidad.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la localidad está vacia.
            textView_error_localidad.setText(R.string.textview_localidad_obligatoria);
            textView_error_localidad.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_localidad.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo provincia.
     * @param provincia
     * @return
     */
    public boolean validarProvincia(String provincia) {
        boolean valid = false;
        if ((provincia.isEmpty()) || (provincia.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la provincia está vacia.
            textView_error_provincia.setText(R.string.textview_provincia_obligatoria);
            textView_error_provincia.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_provincia.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo direccion.
     * @param direccion
     * @return
     */
    public boolean validarDir(String direccion) {
        boolean valid = false;
        if ((direccion.isEmpty()) || (direccion.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la dirección está vacia.
            textView_error_direccion.setText(R.string.textview_direccion_obligatoria);
            textView_error_direccion.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_direccion.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo codigoPostal.
     * @param codigoPostal
     * @return
     */
    public boolean validarCodigoPostal(String codigoPostal) {
        boolean valid = false;
        if ((codigoPostal.isEmpty()) || (codigoPostal.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la dirección está vacia.
            textView_error_codigoPostal.setText(R.string.textview_codigoPostal_obligatoria);
            textView_error_codigoPostal.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_codigoPostal.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }
}