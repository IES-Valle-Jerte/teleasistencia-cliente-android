package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.RelacionPacientePersona;
import com.example.teleappsistencia.modelos.Terminal;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactosPacienteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactosPacienteFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatosViviendaFragment datosViviendaFragment;
    private Paciente paciente;
    private Terminal terminal;

    private Button buttonGuardar;
    private Button buttonVolver;
    //Contacto 1
    private TableLayout tableLayout1;
    private TextView contacto1;
    private EditText nombre1;
    private EditText apelldios1;
    private EditText telefono1;
    private EditText relacion1;
    private EditText disponibilidad1;
    private EditText tiempoADomicilio1;
    private ToggleButton llaves1;
    private ToggleButton conviviente1;
    private boolean desplegado1=true;
    private Switch activarContacto1;
    private boolean guardarContacto=false;
    //Contacto 2
    private TableLayout tableLayout2;
    private TextView contacto2;
    private EditText nombre2;
    private EditText apelldios2;
    private EditText telefono2;
    private EditText relacion2;
    private EditText disponibilidad2;
    private EditText tiempoADomicilio2;
    private ToggleButton llaves2;
    private ToggleButton conviviente2;
    private boolean desplegado2=false;
    private Switch activarContacto2;
    private boolean guardarContacto2=false;
    //Contacto 3
    private TableLayout tableLayout3;
    private TextView contacto3;
    private EditText nombre3;
    private EditText apelldios3;
    private EditText telefono3;
    private EditText relacion3;
    private EditText disponibilidad3;
    private EditText tiempoADomicilio3;
    private ToggleButton llaves3;
    private ToggleButton conviviente3;
    private boolean desplegado3=false;
    private Switch activarContacto3;
    private boolean guardarContacto3=false;
    //Contacto 4
    private TableLayout tableLayout4;
    private TextView contacto4;
    private EditText nombre4;
    private EditText apelldios4;
    private EditText telefono4;
    private EditText relacion4;
    private EditText disponibilidad4;
    private EditText tiempoADomicilio4;
    private ToggleButton llaves4;
    private ToggleButton conviviente4;
    private boolean desplegado4=false;
    private Switch activarContacto4;
    private boolean guardarContacto4=false;
    //Contacto 5
    private TableLayout tableLayout5;
    private TextView contacto5;
    private EditText nombre5;
    private EditText apelldios5;
    private EditText telefono5;
    private EditText relacion5;
    private EditText disponibilidad5;
    private EditText tiempoADomicilio5;
    private ToggleButton llaves5;
    private ToggleButton conviviente5;
    private boolean desplegado5=false;
    private Switch activarContacto5;
    private boolean guardarContacto5=false;

    public ContactosPacienteFragment() {
        // Required empty public constructor
    }

    public void setDatosViviendaFragment(DatosViviendaFragment datosViviendaFragment) {
        this.datosViviendaFragment = datosViviendaFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactosPacienteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactosPacienteFragment newInstance(String param1, String param2) {
        ContactosPacienteFragment fragment = new ContactosPacienteFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            Paciente paciente = (Paciente) bundle.getSerializable("paciente"); // Reemplaza "objeto" con la clave que hayas utilizado en el primer fragmento
            // Haz lo que desees con el objeto en este fragmento
            this.paciente=paciente;
            Terminal terminal= (Terminal) bundle.get("terminal");
            this.terminal=terminal;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_contactos_paciente, container, false);
        buttonGuardar=v.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(this);
        buttonVolver=v.findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(this);
        //Capturar datos de contactos
        //Contacto 1
        tableLayout1=v.findViewById(R.id.tableLayoutContacto);
        contacto1=v.findViewById(R.id.textViewContacto);
        contacto1.setText(getString(R.string.contacto)+"1 ▼");
        contacto1.setOnClickListener(this);
        nombre1=v.findViewById(R.id.editTextNombreContacto);
        apelldios1=v.findViewById(R.id.editTextApellidoContacto);
        telefono1=v.findViewById(R.id.editTextTelefonoContacto);
        relacion1=v.findViewById(R.id.editTextRelacionContacto);
        disponibilidad1=v.findViewById(R.id.editTextDisponibilidad);
        tiempoADomicilio1=v.findViewById(R.id.editTextTiempoADomicilio);
        llaves1=v.findViewById(R.id.toggleButtonLlaves);
        conviviente1=v.findViewById(R.id.toggleButtonConviviente);
        activarContacto1=v.findViewById(R.id.switchActivarContacto);
        activarContacto1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Verifica si el Switch está activado o desactivado
                if (isChecked) {
                    // Si está activado, establece el texto "Guardar contacto"
                    activarContacto1.setText(getString(R.string.contacto_activado));
                    guardarContacto=true;
                } else {
                    // Si está desactivado, establece el texto "Contacto vacío"
                    activarContacto1.setText(getString(R.string.contacto_desactivado));
                    guardarContacto=false;
                }
            }
        });
        //Contacto 2
        tableLayout2=v.findViewById(R.id.tableLayoutContacto2);
        contacto2=v.findViewById(R.id.textViewContacto2);
        contacto2.setText(getString(R.string.contacto)+"2 ▼");
        contacto2.setOnClickListener(this);
        nombre2=v.findViewById(R.id.editTextNombreContacto2);
        apelldios2=v.findViewById(R.id.editTextApellidoContacto2);
        telefono2=v.findViewById(R.id.editTextTelefonoContacto2);
        relacion2=v.findViewById(R.id.editTextRelacionContacto2);
        disponibilidad2=v.findViewById(R.id.editTextDisponibilidad2);
        tiempoADomicilio2=v.findViewById(R.id.editTextTiempoADomicilio2);
        llaves2=v.findViewById(R.id.toggleButtonLlaves2);
        conviviente2=v.findViewById(R.id.toggleButtonConviviente2);
        activarContacto2=v.findViewById(R.id.switchActivarContacto2);
        activarContacto2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Verifica si el Switch está activado o desactivado
                if (isChecked) {
                    // Si está activado, establece el texto "Guardar contacto"
                    activarContacto2.setText(getString(R.string.contacto_activado));
                    guardarContacto2=true;
                } else {
                    // Si está desactivado, establece el texto "Contacto vacío"
                    activarContacto2.setText(getString(R.string.contacto_desactivado));
                    guardarContacto2=false;
                }
            }
        });
        //Contacto 3
        tableLayout3=v.findViewById(R.id.tableLayoutContacto3);
        contacto3=v.findViewById(R.id.textViewContacto3);
        contacto3.setText(getString(R.string.contacto)+"3 ▼");
        contacto3.setOnClickListener(this);
        nombre3=v.findViewById(R.id.editTextNombreContacto3);
        apelldios3=v.findViewById(R.id.editTextApellidoContacto3);
        telefono3=v.findViewById(R.id.editTextTelefonoContacto3);
        relacion3=v.findViewById(R.id.editTextRelacionContacto3);
        disponibilidad3=v.findViewById(R.id.editTextDisponibilidad3);
        tiempoADomicilio3=v.findViewById(R.id.editTextTiempoADomicilio3);
        llaves3=v.findViewById(R.id.toggleButtonLlaves3);
        conviviente3=v.findViewById(R.id.toggleButtonConviviente3);
        activarContacto3=v.findViewById(R.id.switchActivarContacto3);
        activarContacto3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Verifica si el Switch está activado o desactivado
                if (isChecked) {
                    // Si está activado, establece el texto "Guardar contacto"
                    activarContacto3.setText(getString(R.string.contacto_activado));
                    guardarContacto3=true;
                } else {
                    // Si está desactivado, establece el texto "Contacto vacío"
                    activarContacto3.setText(getString(R.string.contacto_desactivado));
                    guardarContacto3=false;
                }
            }
        });
        //Contacto 4
        tableLayout4=v.findViewById(R.id.tableLayoutContacto4);
        contacto4=v.findViewById(R.id.textViewContacto4);
        contacto4.setText(getString(R.string.contacto)+"4 ▼");
        contacto4.setOnClickListener(this);
        nombre4=v.findViewById(R.id.editTextNombreContacto4);
        apelldios4=v.findViewById(R.id.editTextApellidoContacto4);
        telefono4=v.findViewById(R.id.editTextTelefonoContacto4);
        relacion4=v.findViewById(R.id.editTextRelacionContacto4);
        disponibilidad4=v.findViewById(R.id.editTextDisponibilidad4);
        tiempoADomicilio4=v.findViewById(R.id.editTextTiempoADomicilio4);
        llaves4=v.findViewById(R.id.toggleButtonLlaves4);
        conviviente4=v.findViewById(R.id.toggleButtonConviviente4);
        activarContacto4=v.findViewById(R.id.switchActivarContacto4);
        activarContacto4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Verifica si el Switch está activado o desactivado
                if (isChecked) {
                    // Si está activado, establece el texto "Guardar contacto"
                    activarContacto4.setText(getString(R.string.contacto_activado));
                    guardarContacto4=true;
                } else {
                    // Si está desactivado, establece el texto "Contacto vacío"
                    activarContacto4.setText(getString(R.string.contacto_desactivado));
                    guardarContacto4=false;
                }
            }
        });
        //Contacto 5
        tableLayout5=v.findViewById(R.id.tableLayoutContacto5);
        contacto5=v.findViewById(R.id.textViewContacto5);
        contacto5.setText(getString(R.string.contacto)+"5 ▼");
        contacto5.setOnClickListener(this);
        nombre5=v.findViewById(R.id.editTextNombreContacto5);
        apelldios5=v.findViewById(R.id.editTextApellidoContacto5);
        telefono5=v.findViewById(R.id.editTextTelefonoContacto5);
        relacion5=v.findViewById(R.id.editTextRelacionContacto5);
        disponibilidad5=v.findViewById(R.id.editTextDisponibilidad5);
        tiempoADomicilio5=v.findViewById(R.id.editTextTiempoADomicilio5);
        llaves5=v.findViewById(R.id.toggleButtonLlaves5);
        conviviente5=v.findViewById(R.id.toggleButtonConviviente5);
        activarContacto5=v.findViewById(R.id.switchActivarContacto5);
        activarContacto5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Verifica si el Switch está activado o desactivado
                if (isChecked) {
                    // Si está activado, establece el texto "Guardar contacto"
                    activarContacto5.setText(getString(R.string.contacto_activado));
                    guardarContacto5=true;
                } else {
                    // Si está desactivado, establece el texto "Contacto vacío"
                    activarContacto5.setText(getString(R.string.contacto_desactivado));
                    guardarContacto5=false;
                }
            }
        });
        return v;
    }
    private void volver() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, datosViviendaFragment)
                .addToBackStack(null)
                .commit();
    }
    private void contraerContacto(TableLayout tableLayout){
        int nuevaAlturaDp = 25; // Cambia esta altura a la que desees en dp
        int nuevaAlturaPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, nuevaAlturaDp, getResources().getDisplayMetrics());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, nuevaAlturaPx);
        // Aplica los nuevos parámetros de diseño al TableLayout
        tableLayout.setLayoutParams(params);
    }
    private void desplegarContacto(TableLayout tableLayout){
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableLayout.setLayoutParams(params);
    }
    private void guardarContactos(){
        if(guardarContacto){
            RelacionPacientePersona relacionPacientePersona=new RelacionPacientePersona();
            relacionPacientePersona.setNombre(nombre1.getText().toString());
            relacionPacientePersona.setApellidos(apelldios1.getText().toString());
            relacionPacientePersona.setTelefono(telefono1.getText().toString());
            relacionPacientePersona.setTipoRelacion(relacion1.getText().toString());
            boolean estado;
            if(llaves1.isChecked()){
                estado=true;
            }else{
                estado=false;
            }
            relacionPacientePersona.setTieneLlavesVivienda(estado);
            relacionPacientePersona.setDisponibilidad(disponibilidad1.getText().toString());
            relacionPacientePersona.setObservaciones("Ninguna");
            relacionPacientePersona.setPrioridad(2);
            boolean isConviviente;
            if (conviviente1.isChecked()){
                isConviviente=true;
            }else{
                isConviviente=false;
            }
            relacionPacientePersona.setEsConviviente(isConviviente);
            try {
                relacionPacientePersona.setTiempoDomicilio(Integer.parseInt(tiempoADomicilio1.getText().toString()));
                //set id del paciente
                //post del objeto
            }catch (NullPointerException e){
                Toast.makeText(getContext(), "Error en el tiempo al domicilio", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void pasarPacienteAlSiguienteFragmento(Paciente paciente) {
        DispositivosFragment siguienteFragment = new DispositivosFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("paciente", paciente); //Se carga el objeto en el bundle
        bundle.putSerializable("terminal",this.terminal);
        siguienteFragment.setContactosPacienteFragment(this);
        siguienteFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, siguienteFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGuardar:
                guardarContactos();
                pasarPacienteAlSiguienteFragmento(this.paciente);
                break;
            case R.id.buttonVolver:
                volver();
                break;
            case R.id.textViewContacto:
                if (desplegado1){
                    contraerContacto(tableLayout1);
                    desplegado1=false;
                }else{
                    desplegarContacto(tableLayout1);
                    desplegado1=true;
                }
                break;
            case R.id.textViewContacto2:
                if (desplegado2){
                    contraerContacto(tableLayout2);
                    desplegado2=false;
                }else{
                    desplegarContacto(tableLayout2);
                    desplegado2=true;
                }
            break;
            case R.id.textViewContacto3:
                if (desplegado3){
                    contraerContacto(tableLayout3);
                    desplegado3=false;
                }else{
                    desplegarContacto(tableLayout3);
                    desplegado3=true;
                }
                break;
            case R.id.textViewContacto4:
                if (desplegado4){
                    contraerContacto(tableLayout4);
                    desplegado4=false;
                }else{
                    desplegarContacto(tableLayout4);
                    desplegado4=true;
                }
                break;
            case R.id.textViewContacto5:
                if (desplegado5){
                    contraerContacto(tableLayout5);
                    desplegado5=false;
                }else{
                    desplegarContacto(tableLayout5);
                    desplegado5=true;
                }
                break;
        }
    }
}