package com.example.teleappsistencia.ui.fragments.tipo_situacion;

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
import com.example.teleappsistencia.modelos.TipoSituacion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarTipoSituacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarTipoSituacionFragment extends Fragment {

    private TipoSituacion tipoSituacion;

    private Button btn_insertar;
    private Button btn_volver;
    private EditText editText_nombre_tipo_situacion;
    private TextView textView_error_nombre;

    public ModificarTipoSituacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tipoSituacion
     * @return A new instance of fragment InsertarTipoSituacionFragment.
     */
    public static ModificarTipoSituacionFragment newInstance(TipoSituacion tipoSituacion) {
        ModificarTipoSituacionFragment fragment = new ModificarTipoSituacionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.TIPO_SITUACION, tipoSituacion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            tipoSituacion = (TipoSituacion) getArguments().getSerializable(Constantes.TIPO_SITUACION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar_tipo_situacion, container, false);

        this.editText_nombre_tipo_situacion = view.findViewById(R.id.editText_nombre_tipoSituacion);
        this.btn_insertar = view.findViewById(R.id.btn_guardar_tipoSituacion);
        this.btn_volver = view.findViewById(R.id.btn_volver_tipoSituacion);
        this.textView_error_nombre = view.findViewById(R.id.textView_error_nombre_tipoSituacion);

        this.editText_nombre_tipo_situacion.setText(this.tipoSituacion.getNombre());

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarTipoSituacion()) {
                    modificarTipoSituacion();
                }
            }
        });

        this.btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        inicializarListeners();

        return view;
    }

    /**
     * Método para modificar un tipo de situación de la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void modificarTipoSituacion() {
        String nombre = this.editText_nombre_tipo_situacion.getText().toString();

        TipoSituacion tipoSituacion = new TipoSituacion();
        tipoSituacion.setNombre(nombre);

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.modifyTipoSituacion(this.tipoSituacion.getId(), tipoSituacion, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object tipoSituacion = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_MODIFICADO_TIPO_SITUACION);
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
    private boolean validarTipoSituacion() {
        boolean validNombre;

        validNombre = validarNombre(editText_nombre_tipo_situacion.getText().toString());

        if(validNombre){
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
        this.editText_nombre_tipo_situacion.addTextChangedListener(new TextWatcher() {
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
     * Método para validar el campo nombre.
     * @param nombre
     * @return
     */
    public boolean validarNombre(String nombre) {
        boolean valid = false;
        if ((nombre.isEmpty()) || (nombre.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si el nombre está vacio.
            textView_error_nombre.setText(R.string.textview_nombre_obligatorio);
            textView_error_nombre.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatorio y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_nombre.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }
}