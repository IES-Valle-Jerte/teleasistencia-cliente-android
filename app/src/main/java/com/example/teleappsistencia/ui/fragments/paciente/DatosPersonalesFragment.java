package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.direccion.DireccionAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosPersonalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosPersonalesFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button buttonGuardar;
    private Button buttonVolver;
    private EditText nExpediente;
    private EditText nombre;
    private EditText apellidos;
    private EditText dni;
    private EditText fechaNac;
    private Spinner sexo;
    private EditText telfFijo;
    private EditText telfMovil;
    private EditText localidad;
    private EditText provincia;
    private EditText direccion;
    private EditText cp;
    private Spinner tipoUsuario;
    private EditText otrosServicios;

    private List<TipoModalidadPaciente> listadoTipoModalidadPaciente=null;
    private List<Direccion> listaDirecciones;
    private int idpersona=0;
    private int idTerminal=0;
    public DatosPersonalesFragment() {
        // Required empty public constructor
    }

    public int getIdpersona() {
        return idpersona;
    }

    public int getIdTerminal() {
        return idTerminal;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosPersonalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosPersonalesFragment newInstance(String param1, String param2) {
        DatosPersonalesFragment fragment = new DatosPersonalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void volver() {
        ListarPacienteFragment listarPacienteFragment = new ListarPacienteFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarPacienteFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_datos_personales, container, false);
        buttonGuardar=v.findViewById(R.id.buttonGuardarDatosPersonales);
        buttonGuardar.setOnClickListener(this);
        buttonVolver=v.findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(this);
        nExpediente=v.findViewById(R.id.editTextNumeroExpedientePaciente);
        nombre=v.findViewById(R.id.editText_nombre_paciente);
        apellidos=v.findViewById(R.id.editText_apellidos_paciente);
        dni=v.findViewById(R.id.editText_dni_paciente);
        fechaNac=v.findViewById(R.id.editText_fechaNacimiento_paciente);
        sexo =v.findViewById(R.id.spinner_sexo_paciente);
        telfFijo=v.findViewById(R.id.editText_telefonoFijo_paciente);
        telfMovil=v.findViewById(R.id.editText_telefonoMovil_paciente);
        localidad=v.findViewById(R.id.editText_localidad_direccionPaciente);
        provincia=v.findViewById(R.id.editText_provincia_direccionPaciente);
        direccion=v.findViewById(R.id.editText_direccion_direccionPaciente);
        cp=v.findViewById(R.id.editText_codigoPostal_direccionPaciente);
        tipoUsuario=v.findViewById(R.id.spinner_Tipo_Usuario);
        otrosServicios=v.findViewById(R.id.editText_OtrosServiciosSociales);
        //Rellenar datos del spinner de sexo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{Constantes.SEXO_MASCULINO, Constantes.SEXO_FEMENINO});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.sexo.setAdapter(adapter);
        //Rellenar datos del spinner de tipo de usuario
        inicializarSpinnertipoUsuario();
        return v;
    }
    private List<String> convertirListaTipoModalidadPaciente(List<TipoModalidadPaciente> listadoTipoModalidadPaciente) {
        List<String> listadoString = new ArrayList<>();
        for (TipoModalidadPaciente tipoModalidadPaciente : listadoTipoModalidadPaciente) {
            listadoString.add(tipoModalidadPaciente.getId() + "-" + tipoModalidadPaciente.getNombre());
        }
        return listadoString;
    }
    private void inicializarSpinnertipoUsuario() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<TipoModalidadPaciente>> call = apiService.getListadoTipoModalidadPaciente(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoModalidadPaciente>>() {
            @Override
            public void onResponse(Call<List<TipoModalidadPaciente>> call, Response<List<TipoModalidadPaciente>> response) {
                if (response.code() == 200) {
                    listadoTipoModalidadPaciente = response.body();
                    if (listadoTipoModalidadPaciente != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaTipoModalidadPaciente(listadoTipoModalidadPaciente));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        tipoUsuario.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TipoModalidadPaciente>> call, Throwable t) {

            }
        });
    }
    private void insertarPersona() {
        //Recoger los valores introducidos por el usuario
        String snombre = this.nombre.getText().toString();
        String sapellidos = this.apellidos.getText().toString();
        String sdni = dni.getText().toString();
        String sfechaNacimiento = fechaNac.getText().toString();
        String ssexo = sexo.getSelectedItem().toString();
        String stelefonoFijo = this.telfFijo.getText().toString();
        String stelefonoMovil = this.telfMovil.getText().toString();

        String slocalidad = this.localidad.getText().toString();
        String sprovincia = this.provincia.getText().toString();
        String sdirec = this.direccion.getText().toString();
        String scodigoPostal = this.cp.getText().toString();
        //Crear objeto direccion(atributo de persona)
        Direccion direccion = new Direccion();
        direccion.setLocalidad(slocalidad);
        direccion.setProvincia(sprovincia);
        direccion.setDireccion(sdirec);
        direccion.setCodigoPostal(scodigoPostal);
        //Crear objeto persona (atributo del paciente)
        Persona persona = new Persona();
        persona.setNombre(snombre);
        persona.setApellidos(sapellidos);
        persona.setDni(sdni);
        persona.setFechaNacimiento(sfechaNacimiento);
        persona.setSexo(ssexo);
        persona.setTelefonoFijo(stelefonoFijo);
        persona.setTelefonoMovil(stelefonoMovil);
        persona.setDireccion(direccion);

        //Insertar persona
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.addPersona(persona, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object persona = response.body();
                    Persona p= (Persona) Utilidad.getObjeto(persona,Constantes.PERSONA);
                    idpersona=p.getId();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_CREADO_PERSONA);
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
    private void borrarEditTexts() {
        //Borra los datos introducidos por el usuario de los campos de texto
        this.nombre.setText(Constantes.STRING_VACIO);
        this.apellidos.setText(Constantes.STRING_VACIO);
        this.dni.setText(Constantes.STRING_VACIO);
        this.fechaNac.setText(Constantes.STRING_VACIO);
        this.telfFijo.setText(Constantes.STRING_VACIO);
        this.telfMovil.setText(Constantes.STRING_VACIO);
        this.localidad.setText(Constantes.STRING_VACIO);
        this.provincia.setText(Constantes.STRING_VACIO);
        this.direccion.setText(Constantes.STRING_VACIO);
        this.cp.setText(Constantes.STRING_VACIO);
        this.nExpediente.setText(Constantes.STRING_VACIO);
    }
    public void pasarPacienteAlSiguienteFragmento(Paciente paciente,Terminal terminal) {
        DatosSanitariosFragment segundoFragmento = new DatosSanitariosFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("paciente", paciente); //Se carga el objeto en el bundle
        bundle.putSerializable("terminal",terminal);
        segundoFragmento.setDatosPersonalesFragment(this);
        segundoFragmento.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, segundoFragmento)
                .addToBackStack(null)
                .commit();
    }
    private void insertarTerminal(Terminal terminal) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Terminal> call = apiService.addTerminal(terminal, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Terminal>() {
            @Override
            public void onResponse(Call<Terminal> call, Response<Terminal> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.TERMINAL_INSERTADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                    Object terminalAux = response.body();
                    Terminal t= (Terminal) Utilidad.getObjeto(terminalAux,Constantes.TERMINAL);
                    idTerminal=t.getId();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_INSERTANDO_TERMINAL, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Terminal> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_INSERTANDO_TERMINAL, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
    public boolean comprobarCamposRellenos(){
        boolean todosRellenos=false;
        int contadorCorrectos=0;
        ArrayList<String> cadenas=new ArrayList<>();
        String numExp=this.nExpediente.getText().toString();
        cadenas.add(numExp);

        String snombre = this.nombre.getText().toString();
        cadenas.add(snombre);
        String sapellidos = this.apellidos.getText().toString();
        cadenas.add(sapellidos);
        String sdni = dni.getText().toString();
        cadenas.add(sdni);
        String sfechaNacimiento = fechaNac.getText().toString();
        cadenas.add(sfechaNacimiento);
        String ssexo = sexo.getSelectedItem().toString();
        cadenas.add(ssexo);
        String stelefonoFijo = this.telfFijo.getText().toString();
        cadenas.add(stelefonoFijo);
        String stelefonoMovil = this.telfMovil.getText().toString();
        cadenas.add(stelefonoMovil);

        String slocalidad = this.localidad.getText().toString();
        cadenas.add(slocalidad);
        String sprovincia = this.provincia.getText().toString();
        cadenas.add(sprovincia);
        String sdirec = this.direccion.getText().toString();
        cadenas.add(sdirec);
        String scodigoPostal = this.cp.getText().toString();
        cadenas.add(scodigoPostal);

        for (int i = 0; i < cadenas.size(); i++) {
            if (!cadenas.get(i).equals("")){
                contadorCorrectos++;
            }
        }
        if (contadorCorrectos==cadenas.size()){
            todosRellenos=true;
        }
        return todosRellenos;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGuardarDatosPersonales:

                if (comprobarCamposRellenos()){
                    //Crear objeto paciente y pasarlo al sigueinte fragment para seguir rellenandolo
                    Paciente paciente=new Paciente();
                    Terminal terminal=new Terminal();
                    terminal.setNumeroTerminal("");
                    terminal.setModoAccesoVivienda("");
                    terminal.setBarrerasArquitectonicas("");
                    // Guardar los datos personales
                    insertarPersona();
                    insertarTerminal(terminal);
                    paciente.setNumeroExpediente(this.nExpediente.getText().toString());
                    paciente.setPrestacionOtrosServiciosSociales(this.otrosServicios.getText().toString());

                    //Obtemos el id del tipo de modalidad de paciente
                    String tipoModalidadPacienteSeleccionado = tipoUsuario.getSelectedItem().toString();
                    String[] tipoModalidadPacienteSplit = tipoModalidadPacienteSeleccionado.split("-");
                    tipoModalidadPacienteSeleccionado = tipoModalidadPacienteSplit[0].replaceAll("\\s+", "");
                    paciente.setTipoModalidadPaciente(tipoModalidadPacienteSeleccionado);
                    pasarPacienteAlSiguienteFragmento(paciente,terminal);
                }else{
                    Toast.makeText(getContext(), R.string.rellenar_campos, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.buttonVolver:
                volver();
                break;
        }
    }


}