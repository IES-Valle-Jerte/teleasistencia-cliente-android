package com.example.teleappsistencia.ui.fragments.recurso_comunitario;

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
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentModificarRecursoComunitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentModificarRecursoComunitario extends Fragment implements View.OnClickListener {

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
    private Spinner spinnerTipoRecursoComunitario;
    private Button buttonGuardar;
    private Button buttonVolver;
    private RecursoComunitario recursoComunitario;
    private List<TipoRecursoComunitario> lTipoRecursoComunitario;

    public FragmentModificarRecursoComunitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentModificarRecursoComunitario.
     * @param recursoComunitario: Recibe el objeto a modificar.
     */
    public static FragmentModificarRecursoComunitario newInstance(RecursoComunitario recursoComunitario) {
        FragmentModificarRecursoComunitario fragment = new FragmentModificarRecursoComunitario();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.RECURSO_COMUNITARIO_OBJETO, recursoComunitario);
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
            this.recursoComunitario = (RecursoComunitario) getArguments().getSerializable(Constantes.RECURSO_COMUNITARIO_OBJETO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_modificar_recurso_comunitario, container, false);

        // Se inicializan las variables.
        this.textViewErrorPedirNombre = (TextView) root.findViewById(R.id.textViewErrorPedirNombre);
        this.textViewErrorPedirTelefono = (TextView) root.findViewById(R.id.textViewErrorPedirTelefono);
        this.textViewErrorPedirLocalidad = (TextView) root.findViewById(R.id.textViewErrorPedirLocalidad);
        this.textViewErrorPedirProvincia = (TextView) root.findViewById(R.id.textViewErrorPedirProvincia);
        this.textViewErrorPedirDireccion = (TextView) root.findViewById(R.id.textViewErrorPedirDireccion);
        this.textViewErrorPedirCodigoPostal = (TextView) root.findViewById(R.id.textViewErrorPedirCodigoPostal);
        this.editTextPedirNombre = (EditText) root.findViewById(R.id.editTextPedirNombre);
        this.editTextPhonePedirTelefono = (EditText) root.findViewById(R.id.editTextPhonePedirTelefono);
        this.editTextPedirLocalidad = (EditText) root.findViewById(R.id.editTextPedirLocalidad);
        this.editTextPedirProvincia = (EditText) root.findViewById(R.id.editTextPedirProvincia);
        this.editTextPedirDireccion = (EditText) root.findViewById(R.id.editTextPedirDireccion);
        this.editTextTextPostalAddressPedirCodigoPostal = (EditText) root.findViewById(R.id.editTextTextPostalAddressPedirCodigoPostal);
        this.spinnerTipoRecursoComunitario = (Spinner) root.findViewById(R.id.spinnerTipoRecursoComunitario);
        this.buttonGuardar = (Button) root.findViewById(R.id.buttonGuardar);
        this.buttonVolver = (Button) root.findViewById(R.id.buttonVolver);

        // Se establece la acción de pulsar los botones.
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);

        // Si el objeto no es nulo, se muestran sus atributos en el layout.

        if (this.recursoComunitario != null) {
            Direccion direccion = (Direccion) Utilidad.getObjeto(this.recursoComunitario.getDireccion(), Constantes.DIRECCION);
            this.editTextPedirNombre.setText(this.recursoComunitario.getNombre());
            this.editTextPhonePedirTelefono.setText(this.recursoComunitario.getTelefono());
            this.editTextPedirLocalidad.setText(direccion.getLocalidad());
            this.editTextPedirProvincia.setText(direccion.getProvincia());
            this.editTextPedirDireccion.setText(direccion.getDireccion());
            this.editTextTextPostalAddressPedirCodigoPostal.setText(direccion.getCodigoPostal());

            // Llamamos al método que cargará el spinner con los tipos de recursos comunitarios.
            cargarSpinner();
        }

        // Se inicializan los listeners.
        inicializarListeners();
        // Se inicializan los listeners.
        inicializarListeners();
        // Inflate the layout for this fragment
        return root;
    }

    /**
     * Este método hace una petición a la API REST de la lista con todos los tipos de recursos comunitarios.
     * Las carga en un spinner.
     */
    private void cargarSpinner() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<TipoRecursoComunitario>> call = apiService.getTipoRecursoComunitario(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoRecursoComunitario>>() {
            @Override
            public void onResponse(Call<List<TipoRecursoComunitario>> call, Response<List<TipoRecursoComunitario>> response) {
                if(response.isSuccessful()) {
                    List<TipoRecursoComunitario> lObjetos = response.body();
                    lTipoRecursoComunitario = (ArrayList<TipoRecursoComunitario>) Utilidad.getObjeto(lObjetos, Constantes.ARRAYLIST_TIPO_RECURSO_COMUNITARIO);
                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, lTipoRecursoComunitario);
                    spinnerTipoRecursoComunitario.setAdapter(adapter);
                    seleccionarItem();
                } else {
                    Toast.makeText(getContext(), Constantes.MENSAJE_MODIFICAR_RECURSO_COMUNITARIO, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoRecursoComunitario>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_MENSAJE_MODIFICAR_RECURSO_COMUNITARIO, Toast.LENGTH_LONG).show();
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
        if (validarRecursoComunitario()) {
            modificarRecursoComunitario();
        }
    }

    /**
     * Método para el botón volver.
     */
    private void accionBotonVolver() {
        getActivity().onBackPressed();
    }

    /**
     * Este método busca la posición del objeto Tipo Recurso Comunitario en la lista y selecciona el item
     * del spinner que corresponde a esa posición.
     */
    private void seleccionarItem() {
        boolean encontrado = false;
        TipoRecursoComunitario tipoRecursoComunitario = (TipoRecursoComunitario) Utilidad.getObjeto(recursoComunitario.getTipoRecursoComunitario(), Constantes.TIPO_RECURSO_COMUNITARIO);
        TipoRecursoComunitario itemSpinner;
        int i = 0;
        while(!encontrado && i < this.lTipoRecursoComunitario.size()) {
            itemSpinner = this.lTipoRecursoComunitario.get(i);
            if(itemSpinner.getId() == tipoRecursoComunitario.getId()) {
                this.spinnerTipoRecursoComunitario.setSelection(i);
                encontrado = true;
            }
            i++;
        }
    }

    /**
     * Método que modifica el recurso comunitario.
     */
    private void modificarRecursoComunitario() {
        TipoRecursoComunitario tipoRecursoComunitario = (TipoRecursoComunitario) spinnerTipoRecursoComunitario.getSelectedItem();
        Direccion direccionObjeto = (Direccion) Utilidad.getObjeto(this.recursoComunitario.getDireccion(), Constantes.DIRECCION);
        String nombre = this.editTextPedirNombre.getText().toString();
        String telefono = this.editTextPhonePedirTelefono.getText().toString();
        String localidad = this.editTextPedirLocalidad.getText().toString();
        String provincia = this.editTextPedirProvincia.getText().toString();
        String direccion = this.editTextPedirDireccion.getText().toString();
        String codigoPostal = this.editTextTextPostalAddressPedirCodigoPostal.getText().toString();

        direccionObjeto.setLocalidad(localidad);
        direccionObjeto.setProvincia(provincia);
        direccionObjeto.setDireccion(direccion);
        direccionObjeto.setCodigoPostal(codigoPostal);

        this.recursoComunitario.setNombre(nombre);
        this.recursoComunitario.setTelefono(telefono);
        this.recursoComunitario.setDireccion(direccionObjeto);
        this.recursoComunitario.setTipoRecursoComunitario(tipoRecursoComunitario.getId());

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.putRecursoComunitario(recursoComunitario.getId(), recursoComunitario, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.MENSAJE_MODIFICAR_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
                    accionBotonVolver();
                } else {
                    Toast.makeText(getContext(), Constantes.MENSAJE_MODIFICAR_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Método que comprueba si el recurso comunitario es válido.
     *
     * @return: Retorna si el recurso comunitario es válido o no.
     */
    private boolean validarRecursoComunitario() {
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

        this.editTextPhonePedirTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarTelefono(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editTextPedirLocalidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarLocalidad(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editTextPedirProvincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarProvincia(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editTextPedirDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarDireccion(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editTextTextPostalAddressPedirCodigoPostal.addTextChangedListener(new TextWatcher() {
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