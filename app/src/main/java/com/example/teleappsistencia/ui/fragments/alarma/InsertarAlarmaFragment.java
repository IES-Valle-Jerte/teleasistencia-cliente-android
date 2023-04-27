package com.example.teleappsistencia.ui.fragments.alarma;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertarAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarAlarmaFragment extends Fragment implements View.OnClickListener{

    private Alarma alarma;
    private List<TipoAlarma> lTipoAlarma;
    private List<Terminal> lTerminales;
    private List<Paciente> lPacientes;
    private List<Paciente> lUsuarios;
    private Spinner spinnerTipoAlarma;
    private Spinner spinnerIdTerminalOPaciente;
    private Spinner spinnerUsuario;
    private RadioGroup radioGroupAlarma;
    private TextView textViewIdTerminalPacienteAlarmaCrear;

    private Switch switchProgramarAlarma;
    private Button buttonGuardarAlarma;
    private Button buttonVolverAlarma;

    private EditText buttonTimePicker;
    private EditText etPlannedDate;
    private ArrayAdapter adapterTiposAlarma;
    private ArrayAdapter adapterTerminales;
    private ArrayAdapter adapterPacientes;
    private ArrayAdapter adapterUsuarios;

    private TextView textViewProgramarAlarma;

    public InsertarAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarAlarmaFragment.
     */
    public static InsertarAlarmaFragment newInstance() {
        InsertarAlarmaFragment fragment = new InsertarAlarmaFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insertar_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos desde la API REST en los spinners
        cargarDatosSpinners();

        //Oculta los campos de programar alarmas
        //ocultarCamposProgramarAlarma();

        // Próxima entrega quitar comentarios de la siguiente línea

        /*etPlannedDate.setVisibility(View.INVISIBLE);
                    etPlannedDate.setEnabled(false);
                    buttonTimePicker.setVisibility(View.INVISIBLE);
                    buttonTimePicker.setEnabled(false);*/


        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view){
        this.spinnerTipoAlarma = (Spinner) view.findViewById(R.id.spinnerTipoAlarma);
        this.spinnerUsuario = (Spinner) view.findViewById(R.id.spinner_usuario);
        this.buttonGuardarAlarma = (Button) view.findViewById(R.id.buttonGuardarAlarma);
        this.buttonVolverAlarma = (Button) view.findViewById(R.id.buttonVolverAlarma);
        this.etPlannedDate = (EditText) view.findViewById(R.id.etPlannedDate);
        this.buttonTimePicker = (EditText) view.findViewById(R.id.btn_timePickerBtn);
        this.switchProgramarAlarma = (Switch) view.findViewById(R.id.switchProgramarAlarma);
        this.textViewProgramarAlarma = (TextView) view.findViewById(R.id.textViewProgramarAlarma);

    }

    /**
     * En este método se asignan los lístner a los botones y al Radio grup
     */
    private void asignarListener(){
        this.buttonGuardarAlarma.setOnClickListener(this);
        this.buttonVolverAlarma.setOnClickListener(this);
        this.etPlannedDate.setOnClickListener(this);
        this.buttonTimePicker.setOnClickListener(this);
        this.switchProgramarAlarma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switchProgramarAlarma.isChecked()){
                    etPlannedDate.setVisibility(View.VISIBLE);
                    etPlannedDate.setEnabled(true);
                    buttonTimePicker.setVisibility(View.VISIBLE);
                    buttonTimePicker.setEnabled(true);
                }else{
                    etPlannedDate.setVisibility(View.INVISIBLE);
                    etPlannedDate.setEnabled(false);
                    buttonTimePicker.setVisibility(View.INVISIBLE);
                    buttonTimePicker.setEnabled(false);
                }
            }
        });
    }

    /**
     * En este método se cargan los datos de los spinners haciendo llamadas a la API REST
     */
    private void cargarDatosSpinners(){
        cargarDatosTiposAlarmas();
        cargarDatosUsuarios();
    }

    /**
     * Extraemos los datos del layout y los guardamos en un objeto del tipo Alarma.
     */
    private void guardarDatos(){
        TipoAlarma tipoAlarma = (TipoAlarma) this.spinnerTipoAlarma.getSelectedItem();
        Paciente paciente = (Paciente) this.spinnerUsuario.getSelectedItem();

        this.alarma = new Alarma();
        this.alarma.setId_tipo_alarma(tipoAlarma.getId());
        this.alarma.setId_paciente_ucr(paciente.getId());
    }

    /**
     * Este método carga los Tipos de Alarma en su adapter desde la API REST y se lo añade al Spinner
     */
    private void cargarDatosTiposAlarmas(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getTiposAlarma(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lTipoAlarma = (ArrayList<TipoAlarma>) Utilidad.getObjeto(lObjetos, Constantes.AL_TIPO_ALARMA);
                    adapterTiposAlarma = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lTipoAlarma);
                    spinnerTipoAlarma.setAdapter(adapterTiposAlarma);
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método carga los datos de los Terminales desde la API REST y los carga en su adapter
     */
    private void cargarDatosTerminales(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getTerminales(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    radioGroupAlarma.check(R.id.radioButtonTerminal);
                    List<Object> lObjetos = response.body();
                    lTerminales = (ArrayList<Terminal>) Utilidad.getObjeto(lObjetos, Constantes.AL_TERMINAL);
                    adapterTerminales = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lTerminales);
                    spinnerIdTerminalOPaciente.setAdapter(adapterTerminales); // se cargan este adapter primero
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método carga los datos de los Terminales desde la API REST y los carga en su adapter
     */
    private void cargarDatosPacientes(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacientes(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lPacientes = (ArrayList<Paciente>) Utilidad.getObjeto(lObjetos, Constantes.AL_PACIENTE);
                    adapterPacientes = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lPacientes);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método carga el spinner usuarios con los nombres de los usuarios del servicio (pacientes)
     */
    public void cargarDatosUsuarios(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacientes(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lUsuarios = (ArrayList<Paciente>) Utilidad.getObjeto(lObjetos, Constantes.AL_PACIENTE);
                    adapterUsuarios = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lUsuarios);
                    spinnerUsuario.setAdapter(adapterUsuarios);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método lanza la petición POST a la API REST para guardar la alarma
     */
    private void persistirAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Alarma> call = apiService.addAlarma(this.alarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Alarma>() {
            @Override
            public void onResponse(Call<Alarma> call, Response<Alarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.ALARMA_GUARDADA,  Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Alarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, new ListarAlarmasOrdenadasFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Este método oculta el campo fecha, hora y programar de las alarmas
     */

    public void ocultarCamposProgramarAlarma(){
        this.etPlannedDate.setVisibility(View.INVISIBLE);
        this.etPlannedDate.setEnabled(false);
        this.buttonTimePicker.setVisibility(View.INVISIBLE);
        this.buttonTimePicker.setEnabled(false);
        this.switchProgramarAlarma.setVisibility(View.INVISIBLE);
        this.switchProgramarAlarma.setEnabled(false);
        this.textViewProgramarAlarma.setVisibility(View.INVISIBLE);
        this.textViewProgramarAlarma.setEnabled(false);
    }

    private void showDatePickerDialog() {
        int day, month, year;
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        day = newFragment.getDay();
        month = newFragment.getMonth();
        year = newFragment.getYear();

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                SpannableString texto = new SpannableString(day + Constantes.SLASH + month + Constantes.SLASH + year);
                builder.append(texto);
                Bitmap imagen = BitmapFactory.decodeResource(getResources(), R.drawable.ic_calendar);
                ImageSpan span = new ImageSpan(getActivity(), imagen, ImageSpan.ALIGN_BOTTOM);
                builder.setSpan(span, texto.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                etPlannedDate.setText(builder);
            }

        };

        newFragment.setListener(listener);
    }

    public void showTimePrickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Aquí puedes obtener la hora seleccionada por el usuario
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        SpannableString texto = new SpannableString(hourOfDay + Constantes.PUNTOS_DOBLES + minute);
                        builder.append(texto);
                        Bitmap imagen = BitmapFactory.decodeResource(getResources(), R.drawable.ic_reloj);
                        ImageSpan span = new ImageSpan(getActivity(), imagen, ImageSpan.ALIGN_BOTTOM);
                        builder.setSpan(span, texto.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        buttonTimePicker.setText(builder);
                    }
                }, hora, minuto, false);

        // Muestra el diálogo
        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonGuardarAlarma:
                guardarDatos();
                persistirAlarma();
                break;
            case R.id.buttonVolverAlarma:
                volver();
                break;
            case R.id.etPlannedDate:
                showDatePickerDialog();
                break;
            case R.id.btn_timePickerBtn:
                showTimePrickerDialog();
                break;
        }
    }
}