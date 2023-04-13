package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosPersonalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosPersonalesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button buttonGuardar;
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

    public DatosPersonalesFragment() {
        // Required empty public constructor
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_datos_personales, container, false);
        buttonGuardar=v.findViewById(R.id.buttonGuardarDatosPersonales);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar los datos personales
                insertarPersona();
                // Cambiar al siguiente fragment
                ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager_pacientes);
                viewPager.setCurrentItem(1);
            }
        });
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
        //Rellenar datos del spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{Constantes.SEXO_MASCULINO, Constantes.SEXO_FEMENINO});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.sexo.setAdapter(adapter);
        return v;
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
        //Insertar objetos en la API
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.addPersona(persona, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object persona = response.body();
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
}