package com.example.teleappsistencia.ui.fragments.historico_tipo_situacion;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.modelos.HistoricoTipoSituacion;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoSituacion;
import com.example.teleappsistencia.utilidades.dialogs.DatePickerFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertarHistoricoTipoSituacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarHistoricoTipoSituacionFragment extends Fragment {

    private Button btn_insertar;
    private EditText editText_fecha;
    private Spinner spinner_terminal;
    private Spinner spinner_tipoSituacion;

    private TextView textView_error_fecha;

    public InsertarHistoricoTipoSituacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarHistoricoTipoSituacionFragment.
     */
    public static InsertarHistoricoTipoSituacionFragment newInstance() {
        InsertarHistoricoTipoSituacionFragment fragment = new InsertarHistoricoTipoSituacionFragment();
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
        View view = inflater.inflate(R.layout.fragment_insertar_historico_tipo_situacion, container, false);

        this.btn_insertar = (Button) view.findViewById(R.id.btn_guardar_historicoTipoSituacion);
        this.spinner_tipoSituacion = (Spinner) view.findViewById(R.id.spinner_tipoSituacion_historicoTipoSituacion);
        this.spinner_terminal = (Spinner) view.findViewById(R.id.spinner_terminal_historicoTipoSituacion);
        this.editText_fecha = (EditText) view.findViewById(R.id.editText_fecha_historicoTipoSituacion);
        this.textView_error_fecha = (TextView) view.findViewById(R.id.textView_error_fecha_historicoTipoSituacion);

        inicializarSpinnerTerminal();
        inicializarSpinnerTipoSituacion();

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarHistoricoTipoSituacion()){
                    insertarHistoricoTipoSituacion();
                }
            }
        });

        /* Creo un dialog datePicker para recoger la fecha */
        this.editText_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatePickerDialog();
            }
        });

        inicializarListeners();

        return view;
    }

    /**
     * Método encargado de inicializar un DatePickerFragment para poder recoger una fecha.
     */
    private void mostrarDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String selectedDate = year + "-" + Utilidad.twoDigitsDate(month+1) + "-" + Utilidad.twoDigitsDate(day); // Formateo la fecha.
                editText_fecha.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), Constantes.TAG_DATEPICKER);
    }

    /**
     * Método encargado de realizar una petición a la API y inicializar el spinnerTerminal
     * con los terminales recibidos por la petición.
     */
    private void inicializarSpinnerTerminal(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<Object>> call = apiService.getTerminales(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    List<Object> objectList = response.body(); // Recogo la lista de Terminales.
                    // Creo el adapter y le asigno la lista.
                    List<Terminal> terminalList = (ArrayList<Terminal>) Utilidad.getObjeto(objectList, Constantes.AL_TERMINAL);
                    ArrayAdapter<Terminal> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, terminalList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_terminal.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método encargado de realizar una petición a la API y inicializar el spinnerTipoSituacion
     * con los tipoSituaciones recibidos por la petición.
     */
    private void inicializarSpinnerTipoSituacion(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<TipoSituacion>> call = apiService.getTipoSituacion(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoSituacion>>() {
            @Override
            public void onResponse(Call<List<TipoSituacion>> call, Response<List<TipoSituacion>> response) {
                if (response.isSuccessful()) {
                    List<TipoSituacion> tipoSituacionList = response.body();
                    ArrayAdapter<TipoSituacion> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipoSituacionList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tipoSituacion.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TipoSituacion>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método para insertar un nuevo histórico tipo de situación en la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void insertarHistoricoTipoSituacion() {
        String fecha = this.editText_fecha.getText().toString();
        TipoSituacion tipoSituacion = (TipoSituacion) this.spinner_tipoSituacion.getSelectedItem();
        Terminal terminal = (Terminal) this.spinner_terminal.getSelectedItem();

        HistoricoTipoSituacion historicoTipoSituacion = new HistoricoTipoSituacion();
        historicoTipoSituacion.setFecha(fecha);
        historicoTipoSituacion.setIdTipoSituacion(tipoSituacion.getId());
        historicoTipoSituacion.setTerminal(terminal.getId());

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.addHistoricoTipoSituacion(historicoTipoSituacion, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object historicoTipoSituacion = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_CREADO_HISTORICO_TIPO_SITUACION);
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
        this.editText_fecha.setText(Constantes.STRING_VACIO);
        this.textView_error_fecha.setVisibility(View.GONE);
    }

    /**
     * Método que revisa si los datos de los EditText son válidos.
     * @return Devuelve true si es válido de lo contrario devuelve false.
     */
    private boolean validarHistoricoTipoSituacion() {
        boolean validFecha, validTerminal, validTipoSituacion;

        validFecha = validarFecha(editText_fecha.getText().toString());
        validTerminal = validarTerminal();
        validTipoSituacion = validarTipoSituacion();

        if((validFecha) && (validTerminal) && (validTipoSituacion)){
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
        this.editText_fecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarFecha(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Método para validar el campo nombre.
     * @param fecha
     * @return
     */
    public boolean validarFecha(String fecha) {
        boolean valid = false;
        if ((fecha.isEmpty()) || (fecha.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la fecha está vacia.
            textView_error_fecha.setText(R.string.textview_fecha_obligatoria);
            textView_error_fecha.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_fecha.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo terminal.
     * @return
     */
    private boolean validarTerminal() {
        if(spinner_terminal.getSelectedItem() != null) {
            return true;
        } else{
            return false;
        }
    }

    /**
     * Método para validar el campo tipoSituacion.
     * @return
     */
    private boolean validarTipoSituacion() {
        if(spinner_tipoSituacion.getSelectedItem() != null) {
            return true;
        } else{
            return false;
        }
    }
}