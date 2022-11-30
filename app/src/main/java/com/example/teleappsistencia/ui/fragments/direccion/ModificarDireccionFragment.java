package com.example.teleappsistencia.ui.fragments.direccion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
 * Use the {@link ModificarDireccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarDireccionFragment extends Fragment {

    private Direccion direccion;

    private Button btn_insertar;
    private Button btn_volver;
    private EditText editText_localidad;
    private EditText editText_provincia;
    private EditText editText_direccion;
    private EditText editText_codigoPostal;

    private TextView textView_error_localidad;
    private TextView textView_error_provincia;
    private TextView textView_error_direccion;
    private TextView textView_error_codigoPostal;

    public ModificarDireccionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param direccion
     * @return A new instance of fragment InsertarDireccionFragment.
     */
    public static ModificarDireccionFragment newInstance(Direccion direccion) {
        ModificarDireccionFragment fragment = new ModificarDireccionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.DIRECCION, direccion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            direccion = (Direccion) getArguments().getSerializable(Constantes.DIRECCION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar_direccion, container, false);

        this.btn_insertar = view.findViewById(R.id.btn_guardar_direccion);
        this.btn_volver = view.findViewById(R.id.btn_volver_direccion);
        this.editText_localidad = view.findViewById(R.id.editText_localidad_direccion);
        this.editText_provincia = view.findViewById(R.id.editText_provincia_direccion);
        this.editText_direccion = view.findViewById(R.id.editText_direccion_direccion);
        this.editText_codigoPostal = view.findViewById(R.id.editText_codigoPostal_direccion);

        this.textView_error_localidad = view.findViewById(R.id.textView_error_localidad_direccion);
        this.textView_error_provincia = view.findViewById(R.id.textView_error_provincia_direccion);
        this.textView_error_direccion = view.findViewById(R.id.textView_error_direccion_direccion);
        this.textView_error_codigoPostal =view.findViewById(R.id.textView_error_codigoPostal_direccion);

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDireccion()) {
                    modificarDireccion();
                }
            }
        });

        this.btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        rellenarDatos();

        inicializarListeners();

        return view;
    }

    private void rellenarDatos() {
        this.editText_localidad.setText(direccion.getLocalidad());
        this.editText_provincia.setText(direccion.getProvincia());
        this.editText_direccion.setText(direccion.getDireccion());
        this.editText_codigoPostal.setText(direccion.getCodigoPostal());
    }

    /**
     * Método para modificar una dirección de la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void modificarDireccion() {
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
        Call<Object> call = apiService.modifyDireccion(this.direccion.getId(), direccion, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object direccion = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_MODIFICADO_DIRECCION);
                    getActivity().onBackPressed();
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