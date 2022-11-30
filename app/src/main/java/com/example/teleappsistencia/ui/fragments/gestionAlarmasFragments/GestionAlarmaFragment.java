package com.example.teleappsistencia.ui.fragments.gestionAlarmasFragments;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.CentroSanitarioEnAlarma;
import com.example.teleappsistencia.modelos.Contacto;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.PersonaContactoEnAlarma;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.RecursoComunitarioEnAlarma;
import com.example.teleappsistencia.modelos.RelacionTerminalRecursoComunitario;
import com.example.teleappsistencia.modelos.RelacionUsuarioCentro;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.ui.fragments.alarma.ListarAlarmasFragment;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.android.material.textfield.TextInputEditText;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Este fragment será donde el Teleoperador realice las gestiones para tratar una Alarma.
 *
 * @author Jorge Luis Fernández Díaz
 * @version 22.05.2022
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link GestionAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestionAlarmaFragment extends Fragment implements View.OnClickListener{

    //Parámetros de inicio
    private Alarma alarma;
    private int color;
    private String nombrePaciente;
    private String numeroTelefono;
    private List<Object> lContactos;
    private Terminal terminal;
    private Paciente paciente;

    // Elementos del layout
    private TextView textViewNombre;
    private TextView textViewTelefono;
    private TextInputEditText editTextObservaciones;
    private Button btnRegistrarLlamadaPaciente;
    private Spinner spinnerContactos;
    private TextInputEditText editTextAcuerdoContacto;
    private Button btnRegistrarLlamadaContacto;
    private Spinner spinnerCentrosSanitarios;
    private TextInputEditText editTextPersonaLlamadaCentroSanitario;
    private TextInputEditText editTextAcuerdoCentro;
    private Button btnRegistrarLlamadaCentroSanitario;
    private Spinner spinnerRecursosComunitarios;
    private TextInputEditText editTextPersonaLlamadaRecursoComunitario;
    private TextInputEditText editTextAcuerdoRecursoComunitario;
    private Button btnRegistrarRecursosComunitarios;
    private Button buttonCrearAgenda;
    private ImageButton imageButtonInfoPaciente;
    private ImageButton imageButtonInfoContacto;
    private ImageButton imageButtonInfoCentroSanitario;
    private ImageButton imageButtonInfoRecursoComunitario;
    private TextInputEditText editTextResumen;
    private Button buttonFinalizar;
    private Button buttonCancelar;

    // Elementos Auxiliares
    private List<Contacto> lContactosParseada;
    private List<RelacionUsuarioCentro> lCentrosSanitarios;
    private List<RelacionTerminalRecursoComunitario> lRecursosComunitarios;


    public GestionAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Esta es la forma por defecto de Android de crear una instancia de un fragment con parámetros.
     * Se le pasa el parámetro por el newInctance como argumento, y luego en el onCreate se recoge
     * ese argumento.
     *
     * @param alarma alarma que se va a gestionar
     * @param nombrePaciente nombre del Paciente
     * @param numeroTelefono número de teléfono del paciente
     * @param lContactos lista de contactos del paciente
     * @param paciente objeto Paciente
     * @param terminal objeto Terminal
     * @param color color representativo de la alrma (Verde automática, Azul voluntaria)
     *
     * @return A new instance of fragment GestionAlarmaFragment.
     */
    public static GestionAlarmaFragment newInstance(Alarma alarma, String nombrePaciente, String numeroTelefono, ArrayList<Object> lContactos, Paciente paciente, Terminal terminal, int color) {
        GestionAlarmaFragment fragment = new GestionAlarmaFragment();
        Bundle args = new Bundle();
        /* Recibimos los datos por parámetros en este método y los ponemos como argumentos */
        args.putSerializable(Constantes.ARG_ALARMA, alarma);
        args.putString(Constantes.ARG_NOMBREPACIENTE, nombrePaciente);
        args.putString(Constantes.ARG_NUMEROTELEFONO, numeroTelefono);
        args.putSerializable(Constantes.ARG_LCONTACTOS, lContactos);
        args.putSerializable(Constantes.ARG_PACIENTE, paciente);
        args.putSerializable(Constantes.ARG_TERMINAL, terminal);
        args.putInt(Constantes.ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Si tenemos argumentos, los recogemos (comprobación por seguridad), sugerencia de Android Studio */
        if (getArguments() != null) {
            alarma = (Alarma) getArguments().getSerializable(Constantes.ARG_ALARMA);
            nombrePaciente = getArguments().getString(Constantes.ARG_NOMBREPACIENTE);
            numeroTelefono = getArguments().getString(Constantes.ARG_NUMEROTELEFONO);
            lContactos = (ArrayList<Object>) getArguments().getSerializable(Constantes.ARG_LCONTACTOS);
            paciente = (Paciente) getArguments().getSerializable(Constantes.ARG_PACIENTE);
            terminal = (Terminal) getArguments().getSerializable(Constantes.ARG_TERMINAL);
            color = getArguments().getInt(Constantes.ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* Inflamos el layout del fragment */
        View view = inflater.inflate(R.layout.fragment_gestion_alarma, container, false);

        /* Capturar los elementos del layout */
        capturarElementos(view);

        /* Asignamos el listener a los botones */
        asignarListener();

        /* Extraemos y cargamos los datos */
        if(this.alarma != null){
            extraerDatos();
            cargarDatos();
        }

        return view;
    }


      /**************************/
     /* PREPARACIÓN DEL LAYOUT */
    /**************************/
    /**
     * Caputramos los elementos del layout para poder usarlos.
     * @param view
     */
    private void capturarElementos(View view){
        //TextView
        this.textViewNombre = (TextView) view.findViewById(R.id.textViewNombrePacienteGestion);
        this.textViewTelefono = (TextView) view.findViewById(R.id.textViewTelefonoPacienteGestion);

        //TextInputEditText
        this.editTextObservaciones = (TextInputEditText) view.findViewById(R.id.textInputEditTextObservaciones);
        this.editTextAcuerdoContacto = (TextInputEditText) view.findViewById(R.id.textInputEditTextAcuerdoContacto);
        this.editTextPersonaLlamadaCentroSanitario = (TextInputEditText) view.findViewById(R.id.textInputEditTextPersonaLlamadaCentroSanitario);
        this.editTextAcuerdoCentro = (TextInputEditText) view.findViewById(R.id.textInputEditTextAcuerdoCentroSanitario);
        this.editTextPersonaLlamadaRecursoComunitario = (TextInputEditText) view.findViewById(R.id.textInputEditTextPersonaLlamadaRecursoComunitario);
        this.editTextAcuerdoRecursoComunitario = (TextInputEditText) view.findViewById(R.id.textInputEditTextAcuerdoRecursoComunitario);
        this.editTextResumen = (TextInputEditText) view.findViewById(R.id.textInputEditTextResumen);

        //Spinners
        this.spinnerContactos = (Spinner) view.findViewById(R.id.spinnerContactos);
        this.spinnerCentrosSanitarios = (Spinner) view.findViewById(R.id.spinnerCentrosSanitarios);
        this.spinnerRecursosComunitarios = (Spinner) view.findViewById(R.id.spinnerRecursosComunitarios);

        //Botones
        this.btnRegistrarLlamadaPaciente = (Button) view.findViewById(R.id.buttonRegistrarLlamadaPaciente);
        this.btnRegistrarLlamadaContacto = (Button) view.findViewById(R.id.buttonRegistrarLlamadaContacto);
        this.btnRegistrarLlamadaCentroSanitario = (Button) view.findViewById(R.id.buttonRegistrarLlamadaCentroSanitario);
        this.btnRegistrarRecursosComunitarios = (Button) view.findViewById(R.id.buttonRegistrarRecursosComunitarios);
        this.buttonCrearAgenda = (Button) view.findViewById(R.id.buttonCrearAgenda);
        this.buttonFinalizar = (Button) view.findViewById(R.id.buttonFinalizar);
        this.buttonCancelar = (Button) view.findViewById(R.id.buttonCancelar);

        //ImageButton (Info)
        this.imageButtonInfoPaciente = (ImageButton) view.findViewById(R.id.imageButtonInfoPaciente);
        this.imageButtonInfoContacto = (ImageButton) view.findViewById(R.id.imageButtonInfoContacto);
        this.imageButtonInfoCentroSanitario = (ImageButton) view.findViewById(R.id.imageButtonInfoCentroSanitario);
        this.imageButtonInfoRecursoComunitario = (ImageButton) view.findViewById(R.id.imageButtonInfoRecursoComunitario);
    }


    /**
     * Asignamos el onClick listener a los botones. La clase implementa View.OnClickListener, el método
     * onClick está implementado más abajo.
     */
    private void asignarListener(){
        this.btnRegistrarLlamadaPaciente.setOnClickListener(this);
        this.btnRegistrarLlamadaContacto.setOnClickListener(this);
        this.btnRegistrarLlamadaCentroSanitario.setOnClickListener(this);
        this.btnRegistrarRecursosComunitarios.setOnClickListener(this);
        this.buttonCrearAgenda.setOnClickListener(this);
        this.imageButtonInfoPaciente.setOnClickListener(this);
        this.imageButtonInfoContacto.setOnClickListener(this);
        this.imageButtonInfoCentroSanitario.setOnClickListener(this);
        this.imageButtonInfoRecursoComunitario.setOnClickListener(this);
        this.buttonFinalizar.setOnClickListener(this);
        this.buttonCancelar.setOnClickListener(this);
    }



      /************************/
     /* PREPARACIÓN DE DATOS */
    /************************/

    /**
     * Este método extrae los datos de la lista de contactos. Se hace un parseo ya que nos llega como
     * una lista de Object y queremos pasarla a un ArrayList de Contactos.
     */
    private void extraerDatos(){
        this.lContactosParseada = (ArrayList<Contacto>) Utilidad.getObjeto(lContactos, Constantes.AL_CONTACTOS);

        /* Estos métodos además cargarán los datos de sus Spinner correspondiente, ya que las operaciones
         *  de extracción y carga deben ir anidadas para hacerlo de forma síncrona y no falle. */
        cargarSpinnerContactos();
        recuperarListaCentrosSanitarios();
        recuperarListaRecursosComunitarios();
    }


    /**
     * Este método carga los datos del paciente
     */
    private void cargarDatos(){
        this.textViewNombre.setText(this.nombrePaciente);
        this.textViewTelefono.setText(this.numeroTelefono);
        cambiarColorbotones();
    }


    /**
     * Est método únicamente cambia el color a los botones que el usuario siga teniendo en cuenta qué
     * tipo de alarma se está creando.
     */
    private void cambiarColorbotones(){
        ColorStateList csl = ColorStateList.valueOf(this.color);
        this.btnRegistrarLlamadaPaciente.setBackgroundTintList(csl);
        this.btnRegistrarLlamadaContacto.setBackgroundTintList(csl);
        this.btnRegistrarLlamadaCentroSanitario.setBackgroundTintList(csl);
        this.btnRegistrarRecursosComunitarios.setBackgroundTintList(csl);
        this.buttonCrearAgenda.setBackgroundTintList(csl);
        this.imageButtonInfoPaciente.setColorFilter(this.color);
        this.imageButtonInfoContacto.setColorFilter(this.color);
        this.imageButtonInfoCentroSanitario.setColorFilter(this.color);
        this.imageButtonInfoRecursoComunitario.setColorFilter(this.color);
    }


    /**
     * En este método se carga la lista de Contactos del Paciente en el Spinner.
     */
    private void cargarSpinnerContactos(){
        /* Si la lista de contactos está vacía, inhabilitamos el botón de registrar llamada al Contacto
         * y  escondemos el botón de ver información del Contacto */
        if(lContactosParseada.isEmpty()){
            this.imageButtonInfoContacto.setVisibility(View.INVISIBLE);
            this.btnRegistrarLlamadaContacto.setEnabled(false);
        }
        else{
            ArrayAdapter<Contacto> adapter = new ArrayAdapter<Contacto>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lContactosParseada);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinnerContactos.setAdapter(adapter);
        }

    }


    /**
     * Este métoddo hace una petición a la API REST para traerse los datos de los Centros Sanitarios
     * relacionados con el Paciente.
     */
    private void recuperarListaCentrosSanitarios() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getCentrosbyIdPaciente(this.paciente.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lista = response.body();
                    lCentrosSanitarios = (ArrayList<RelacionUsuarioCentro>) Utilidad.getObjeto(lista, Constantes.AL_RELACION_USUARIO_CENTRO);
                    if(!lCentrosSanitarios.isEmpty()){
                        cargarSpinnerCentrosSanitarios(); //Si la lista tiene valores, los cargamos en el Spinner
                    }
                    else{ //Si la lista no tiene datos, ocultamos el botón de información y deshabilitamos el de registrar la llamada
                        imageButtonInfoCentroSanitario.setVisibility(View.INVISIBLE);
                        btnRegistrarLlamadaCentroSanitario.setEnabled(false);
                    }
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Cargamos la lista de Centros Sanitarios relacionados con el Paciente en su Spinner
     */
    private void cargarSpinnerCentrosSanitarios(){
        ArrayAdapter<RelacionUsuarioCentro> adapter = new ArrayAdapter<RelacionUsuarioCentro>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lCentrosSanitarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerCentrosSanitarios.setAdapter(adapter);
    }


    /**
     * Este métoddo hace una petición a la API REST para traerse los datos de los Recursos Comunitarios
     * relacionados con el Paciente.
     */
    private void recuperarListaRecursosComunitarios() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getRecursosComunitariosbyIdTerminal(this.terminal.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lista = response.body();
                    lRecursosComunitarios = (ArrayList<RelacionTerminalRecursoComunitario>) Utilidad.getObjeto(lista, Constantes.AL_RELACION_TERMINAL_RECURSO_COMUNITARIO);
                    if(!lRecursosComunitarios.isEmpty()) {
                        cargarSpinnerRecursosComunitarios(); //Si la lista tiene valores, los cargamos en el Spinner
                    }
                    else{  //Si la lista no tiene datos, ocultamos el botón de información y deshabilitamos el de registrar la llamada
                        imageButtonInfoRecursoComunitario.setVisibility(View.INVISIBLE);
                        btnRegistrarRecursosComunitarios.setEnabled(false);
                    }
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Cargamos la lista de Recursos Comunitarios relacionados con el Terminal en su Spinner
     */
    private void cargarSpinnerRecursosComunitarios(){
        ArrayAdapter<RelacionTerminalRecursoComunitario> adapter = new ArrayAdapter<RelacionTerminalRecursoComunitario>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lRecursosComunitarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerRecursosComunitarios.setAdapter(adapter);
    }



      /***********************************/
     /* MÉTODOS PARA REGISTRAR LLAMADAS */
    /***********************************/

    /**
     * Este método registrará que se ha llamado al paciente guardando el mensaje que aparezca en el
     * campo de texto de Observaciones. No se persiste nada en base de datos todavía.
     */
    private void registrarLlamadaPaciente(){
        LocalDate fecha;
        LocalTime hora;
        String observaciones = this.editTextObservaciones.getText().toString();
        if(!observaciones.isEmpty() && observaciones.length() >= 10) {// Borre la llave para saber por donde voy
            fecha  = LocalDate.now();
            hora = LocalTime.now();
            this.alarma.setObservaciones(observaciones+ Constantes.LLAMADA_REGISTRADA_A + fecha + Constantes.A_LAS + hora + Constantes.PARENTESIS_CIERRE);
            this.btnRegistrarLlamadaPaciente.setText(Constantes.EDITAR);
            Toast.makeText(getContext(), Constantes.LLAMADA_REGISTRADA_EXITO, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getContext(), Constantes.TEXTO_MINIMO_10, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Este método prepara los datos de la llamadda al Contacto para poder registrarlo en la Base de Datos.
     */
    private void registrarLlamadaContacto(){
        Contacto contacto;
        Persona personaEnContacto;
        PersonaContactoEnAlarma personaContactoEnAlarma;
        String acuerdoAlcanzado = editTextAcuerdoContacto.getText().toString();

        if(!acuerdoAlcanzado.isEmpty() && acuerdoAlcanzado.length() >= 10){ //TODO: estas comprobaciones pueden mejorarse
            contacto = (Contacto) this.spinnerContactos.getSelectedItem();
            personaEnContacto = (Persona) Utilidad.getObjeto(contacto.getPersonaEnContacto(), Constantes.PERSONA);

            personaContactoEnAlarma = new PersonaContactoEnAlarma();
            personaContactoEnAlarma.setFechaRegistro(Utilidad.getStringFechaNowYYYYMMDD());
            personaContactoEnAlarma.setIdAlarma(this.alarma.getId());
            personaContactoEnAlarma.setIdPersonaContacto(personaEnContacto.getId());
            personaContactoEnAlarma.setAcuerdoAlcanzado(acuerdoAlcanzado);

            // LLamamos al método que hace la petición POST
            registrarPersonaContactoEnAlarma(personaContactoEnAlarma);
        }
        else{
            Toast.makeText(getContext(), Constantes.ACUERDO_CORTO_TEXTO_MINIMO_10, Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Este método hace una petición POST a la API REST para persistir en Base de Datos datos de una
     *  Persona Contacto En Alarma
     * @param personaContactoEnAlarma
     */
    private void registrarPersonaContactoEnAlarma(PersonaContactoEnAlarma personaContactoEnAlarma){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<PersonaContactoEnAlarma> call = apiService.addPersonaContactoEnAlarma(personaContactoEnAlarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<PersonaContactoEnAlarma>() {
            @Override
            public void onResponse(Call<PersonaContactoEnAlarma> call, Response<PersonaContactoEnAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.LLAMADA_REGISTRADA_EXITO, Toast.LENGTH_LONG).show();
                    editTextAcuerdoContacto.setText(Constantes.VACIO);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_REGISTRAR_LLAMADA, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<PersonaContactoEnAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método prepara los datos de la lamadda al Centro Sanitario para poder registrarlo en la Base de Datos.
     */
    private void registrarLlamadaCentroSanitario(){
        RelacionUsuarioCentro relacionUsuarioCentro;
        CentroSanitario centroSanitario;
        CentroSanitarioEnAlarma centroSanitarioEnAlarma;
        String personaLlamada = this.editTextPersonaLlamadaCentroSanitario.getText().toString();
        String acuerdoAlcanzado = this.editTextAcuerdoCentro.getText().toString();

        if(!personaLlamada.isEmpty() && personaLlamada.length() >= 3){ //TODO: estas comprobaciones pueden mejorarse
            if(!acuerdoAlcanzado.isEmpty() && acuerdoAlcanzado.length() >= 10) {
                // Recuperamos el Centro Sanitario del Spinner
                relacionUsuarioCentro = (RelacionUsuarioCentro) this.spinnerCentrosSanitarios.getSelectedItem();
                centroSanitario = (CentroSanitario) Utilidad.getObjeto(relacionUsuarioCentro.getIdCentroSanitario(), Constantes.CENTRO_SANITARIO);

                // Creamos el Centro Sanitario en alarma con sus datos
                centroSanitarioEnAlarma = new CentroSanitarioEnAlarma();
                centroSanitarioEnAlarma.setFechaRegistro(Utilidad.getStringFechaNowYYYYMMDD()); //Fecha de sistema
                centroSanitarioEnAlarma.setPersona(personaLlamada);
                centroSanitarioEnAlarma.setAcuerdoAlcanzado(acuerdoAlcanzado);
                centroSanitarioEnAlarma.setIdAlarma(this.alarma.getId());
                centroSanitarioEnAlarma.setIdCentroSanitario(centroSanitario.getId());

                // LLamamos al método que hace la petición POST
                registrarCentroSanitarioEnAlarma(centroSanitarioEnAlarma);
            }
            else{
                Toast.makeText(getContext(), Constantes.ACUERDO_CORTO_TEXTO_MINIMO_10, Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getContext(), Constantes.INTRODUCIR_NOMBRE_PERSONA_LLAMADA, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Este método hace la petición POST a la API REST para persistir en Base de Datos la entidad
     * Centro Sanitario En Alarma
     * @param centroSanitarioEnAlarma
     */
    private void registrarCentroSanitarioEnAlarma(CentroSanitarioEnAlarma centroSanitarioEnAlarma) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<CentroSanitarioEnAlarma> call = apiService.addCentroSanitarioEnAlarma(centroSanitarioEnAlarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<CentroSanitarioEnAlarma>() {
            @Override
            public void onResponse(Call<CentroSanitarioEnAlarma> call, Response<CentroSanitarioEnAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.LLAMADA_REGISTRADA_EXITO, Toast.LENGTH_LONG).show();
                    editTextPersonaLlamadaCentroSanitario.setText(Constantes.VACIO);
                    editTextAcuerdoCentro.setText(Constantes.VACIO);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_REGISTRAR_LLAMADA, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<CentroSanitarioEnAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método prepara los datos de la lamadda al Recurso Comunitario para poder registrarlo en la Base de Datos.
     */
    private void registrarLlamadaRecursoComunitario(){
        RelacionTerminalRecursoComunitario relacionTerminalRecursoComunitario;
        RecursoComunitario recursoComunitario;
        RecursoComunitarioEnAlarma recursoComunitarioEnAlarma;
        String personaLlamada = this.editTextPersonaLlamadaRecursoComunitario.getText().toString();
        String acuerdoAlcanzado = this.editTextAcuerdoRecursoComunitario.getText().toString();

        if(!personaLlamada.isEmpty() && personaLlamada.length() >= 3){ //TODO: estas comprobaciones pueden mejorarse
            if(!acuerdoAlcanzado.isEmpty() && acuerdoAlcanzado.length() >= 10) {
                // Recuperamos el Recurso Comunitario del Spinner
                relacionTerminalRecursoComunitario = (RelacionTerminalRecursoComunitario) this.spinnerRecursosComunitarios.getSelectedItem();
                recursoComunitario = (RecursoComunitario) Utilidad.getObjeto(relacionTerminalRecursoComunitario.getIdRecursoComunitario(), Constantes.RECURSO_COMUNITARIO);

                // Creamos el Recurso Comunitario en Alarma con sus datos
                recursoComunitarioEnAlarma = new RecursoComunitarioEnAlarma();
                recursoComunitarioEnAlarma.setFechaRegistro(Utilidad.getStringFechaNowYYYYMMDD());
                recursoComunitarioEnAlarma.setPersona(personaLlamada);
                recursoComunitarioEnAlarma.setAcuerdoAlcanzado(acuerdoAlcanzado);
                recursoComunitarioEnAlarma.setIdAlarma(this.alarma.getId());
                recursoComunitarioEnAlarma.setIdRecursoComunitairo(recursoComunitario.getId());

                // LLamamos al método que hace la petición POST
                registrarRecursoComunitarioEnAlarma(recursoComunitarioEnAlarma);
            }
            else{
                Toast.makeText(getContext(), Constantes.ACUERDO_CORTO_TEXTO_MINIMO_10, Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getContext(), Constantes.INTRODUCIR_NOMBRE_PERSONA_LLAMADA, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Este método hace la petición POST a la API REST para persistir en Base de Datos los datos de un
     * Recurso Comuniatrio En Alarma
     * @param recursoComunitarioEnAlarma
     */
    private void registrarRecursoComunitarioEnAlarma(RecursoComunitarioEnAlarma recursoComunitarioEnAlarma) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<RecursoComunitarioEnAlarma> call = apiService.addRecursoComunitarioEnAlarma(recursoComunitarioEnAlarma, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<RecursoComunitarioEnAlarma>() {
            @Override
            public void onResponse(Call<RecursoComunitarioEnAlarma> call, Response<RecursoComunitarioEnAlarma> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.LLAMADA_REGISTRADA_EXITO, Toast.LENGTH_LONG).show();
                    editTextPersonaLlamadaRecursoComunitario.setText(Constantes.VACIO);
                    editTextAcuerdoRecursoComunitario.setText(Constantes.VACIO);
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_REGISTRAR_LLAMADA, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RecursoComunitarioEnAlarma> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



      /***************************/
     /* DIÁLOGOS DE INFORMACIÓN */
    /***************************/
    //TODO: estos diálogos de información ahora son genéricos. Se podrían mejorar.
    /**
     * Este método mostrará la información del Paciente del cual se está gestionando su alarma
     */
    private void dialogoInfoPaciente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(Constantes.INFORMACION_DEL_PACIENTE);
        builder.setMessage(Constantes.NUMERO_EXPEDIENTE_DP_SP+this.paciente.getNumeroExpediente()+ Constantes.SALTO_LINEA +
                           Constantes.NUMERO_DE_LA_SS_DP_SP+this.paciente.getNumeroSeguridadSocial() + Constantes.SALTO_LINEA +
                           Constantes.OBSERVACIONES_MEDDICAS_DP_SP+this.paciente.getObservacionesMedicas());
        builder.setNeutralButton(Constantes.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    /**
     * Este método mostrará la información del Contacto que esté seleccionado en su Spinner correspondiente
     */
    private void dialogoInfoContacto(){
        Contacto contacto = (Contacto) this.spinnerContactos.getSelectedItem();
        Persona personaEnContacto = (Persona) Utilidad.getObjeto(contacto.getPersonaEnContacto(), Constantes.PERSONA);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(Constantes.INFORMACION_CONTACTO);
        builder.setMessage(Constantes.NOMBRE_DP_SP + personaEnContacto.getNombre() + Constantes.ESPACIO + personaEnContacto.getApellidos() + Constantes.SALTO_LINEA +
                           Constantes.TELEFONO_MOVIL_DP_SP + personaEnContacto.getTelefonoMovil() + Constantes.SALTO_LINEA +
                           Constantes.TELEFONO_FIJO_DP_SP + personaEnContacto.getTelefonoFijo() + Constantes.SALTO_LINEA +
                           Constantes.RELACION_CON_PACIENTE_DP_SP + contacto.getTipo_relacion() + Constantes.SALTO_LINEA +
                           Constantes.DISPONIBILIDAD_DP_SP + contacto.getDisponibilidad() + Constantes.SALTO_LINEA +
                           Constantes.OBSERVACIONES_DP_SP + contacto.getObservaciones() + Constantes.SALTO_LINEA +
                           Constantes.INTERR_TIENE_LLAVES_DP_SP + Utilidad.trueSifalseNo(contacto.isTiene_llaves_vivienda()));
        builder.setNeutralButton(Constantes.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    /**
     * Este método mostrará la información del Centro Sanitario que esté seleccionado en su Spinner correspondiente
     */
    private void dialogoInfoCentroSanitario(){
        RelacionUsuarioCentro relacionUsuarioCentro = (RelacionUsuarioCentro) this.spinnerCentrosSanitarios.getSelectedItem();
        CentroSanitario centroSanitario = (CentroSanitario) Utilidad.getObjeto(relacionUsuarioCentro.getIdCentroSanitario(), Constantes.CENTRO_SANITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(centroSanitario.getDireccion(), Constantes.DIRECCION);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(Constantes.INFORMACION_CENTRO_SANITARIO);
        builder.setMessage(Constantes.NOMBRE_DP_SP + centroSanitario.getNombre() + Constantes.SALTO_LINEA +
                Constantes.TELEFONO_DP_SP + centroSanitario.getTelefono() + Constantes.SALTO_LINEA +
                Constantes.LOCALIDAD_DP_SP + direccion.getLocalidad()+Constantes.ESPACIO_PARENTESIS_AP + direccion.getProvincia() + Constantes.PARENTESIS_CIERRE + Constantes.SALTO_LINEA +
                Constantes.DIRECCION_DP_SP + direccion.getDireccion());
        builder.setNeutralButton(Constantes.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    /**
     * Este método mostrará la información del Recurso Sanitario que esté seleccionado en su Spinner correspondiente
     */
    private void dialogoInfoRecursoComunitario(){
        RelacionTerminalRecursoComunitario rTRC = (RelacionTerminalRecursoComunitario) this.spinnerRecursosComunitarios.getSelectedItem();
        RecursoComunitario recursoComunitario = (RecursoComunitario) Utilidad.getObjeto(rTRC.getIdRecursoComunitario(), Constantes.RECURSO_COMUNITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(recursoComunitario.getDireccion(), Constantes.DIRECCION);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(Constantes.INFORMACION_RECURSO_COMUNITARIO);
        builder.setMessage(Constantes.NOMBRE_DP_SP + recursoComunitario.getNombre() + Constantes.SALTO_LINEA +
                           Constantes.TELEFONO_DP_SP + recursoComunitario.getTelefono() + Constantes.SALTO_LINEA +
                           Constantes.LOCALIDAD_DP_SP + direccion.getLocalidad() + Constantes.ESPACIO_PARENTESIS_AP + direccion.getProvincia() + Constantes.PARENTESIS_CIERRE + Constantes.SALTO_LINEA +
                           Constantes.DIRECCION_DP_SP + direccion.getDireccion());
        builder.setNeutralButton(Constantes.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Este diálogo se mostrará cuando se pulse el botón de Crear Agenda, ya que todavía no se ha implementado.
     */
    private void dialogoEnConstruccion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(Constantes.ATENCION);
        builder.setMessage(Constantes.EN_CONSTRUCCION);
        builder.setNeutralButton(Constantes.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }



      /*******************/
     /* MÉTODOS FINALES */
    /*******************/

    /**
     * Para finalizar la gestión, comprobaremos que se ha llamado al Paciente obligatoriamente
     * (consultando si hay observaciones registradas) y que el Teleoperador ha escrito un resumen.
     */
    private void finalizarGestion(){
        String resumen = this.editTextResumen.getText().toString();
        if(!this.alarma.getObservaciones().isEmpty()){ //TODO: estas comprobaciones pueden merjorarse
            if(resumen.length() > 10){
                /* Siempre que hagamos un PUT tenemos que darle a la petición los datatos de la forma
                 que requiere. En este caso, idTeleoperador SIEMPRE tiene que ser un intger. */
                this.alarma.setId_teleoperador(Utilidad.getUserLogged().getPk());
                this.alarma.setResumen(resumen);
                this.alarma.setEstado_alarma(Constantes.CERRADA);
                guardarAlarma();
            }
            else{
                Toast.makeText(getContext(), Constantes.RESUMEN_MINIMO_10, Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getContext(), Constantes.NO_REGISTRO_LLAMADA_PACIENTE, Toast.LENGTH_LONG).show();
        }
    }


    /**
     *  Finalmente al finalizar la gestión, se hace la petición PUT a la API REST para modificar los
     *  datos de la alarma, quedándola cerrada.
     *  TODO: habría que investigar si al usuario le gustaría poder elegir finalizar la gestión y guardar datos, pero dejar la alarma como "Abierta"
     */
    private void guardarAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarAlarma(this.alarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), this.alarma);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.ALARMA_GESTIONADA_CORRECTAMENTE, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CERRAR_ALARMA, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Este método carga el fragment del listado de alarmas
     */
    private void volver(){
        ListarAlarmasFragment listarAlarmasFragment = new ListarAlarmasFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarAlarmasFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonRegistrarLlamadaPaciente:
                registrarLlamadaPaciente();
                break;
            case R.id.buttonRegistrarLlamadaContacto:
                registrarLlamadaContacto();
                break;
            case R.id.buttonRegistrarLlamadaCentroSanitario:
                registrarLlamadaCentroSanitario();
                break;
            case R.id.buttonRegistrarRecursosComunitarios:
                registrarLlamadaRecursoComunitario();
                break;
            case R.id.buttonCrearAgenda:
                dialogoEnConstruccion(); // TODO: implementar la opción de crear Agenda
                break;
            case R.id.imageButtonInfoPaciente:
                dialogoInfoPaciente();
                break;
            case R.id.imageButtonInfoContacto:
                dialogoInfoContacto();
                break;
            case R.id.imageButtonInfoCentroSanitario:
                dialogoInfoCentroSanitario();
                break;
            case R.id.imageButtonInfoRecursoComunitario:
                dialogoInfoRecursoComunitario();
                break;
            case R.id.buttonFinalizar:
                finalizarGestion();
                break;
            case R.id.buttonCancelar:
                volver();
                break;
        }
    }
}