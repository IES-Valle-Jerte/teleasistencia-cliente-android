package com.example.teleappsistencia.ui.fragments.persona;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarPersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarPersonaFragment extends Fragment {

    private Persona persona;

    private TextView textView_nombre;
    private TextView textView_apellidos;
    private TextView textView_dni;
    private TextView textView_fechaNacimiento;
    private TextView textView_sexo;
    private TextView textView_telefonoFijo;
    private TextView textView_telefonoMovil;
    private TextView textView_idDireccion;

    public ConsultarPersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param persona
     * @return A new instance of fragment ConsultarPersonaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarPersonaFragment newInstance(Persona persona) {
        ConsultarPersonaFragment fragment = new ConsultarPersonaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.PERSONA, persona);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            persona = (Persona) getArguments().getSerializable(Constantes.PERSONA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_persona, container, false);

        this.textView_nombre = view.findViewById(R.id.textView_consultar_nombre_usuario);
        this.textView_apellidos = view.findViewById(R.id.textView_consultar_apellidos_usuario);
        this.textView_dni = view.findViewById(R.id.textView_consultar_dni_persona);
        this.textView_fechaNacimiento = view.findViewById(R.id.textView_consultar_fechaNacimiento_persona);
        this.textView_sexo = view.findViewById(R.id.textView_consultar_sexo_persona);
        this.textView_telefonoFijo = view.findViewById(R.id.textView_consultar_telefonoFijo_persona);
        this.textView_telefonoMovil = view.findViewById(R.id.textView_consultar_telefonoMovil_persona);
        this.textView_idDireccion = view.findViewById(R.id.textView_consultar_idDireccion_persona);

        this.textView_nombre.setText(persona.getNombre());
        this.textView_apellidos.setText(persona.getApellidos());
        this.textView_dni.setText(persona.getDni());
        this.textView_fechaNacimiento.setText(persona.getFechaNacimiento());
        this.textView_sexo.setText(persona.getSexo());
        this.textView_telefonoFijo.setText(persona.getTelefonoFijo());
        this.textView_telefonoMovil.setText(persona.getTelefonoMovil());

        Direccion direccion = (Direccion) Utilidad.getObjeto(persona.getDireccion(), Constantes.DIRECCION);
        if(direccion != null) {
            this.textView_idDireccion.setText(Integer.toString(direccion.getId()));
        } else{
            this.textView_idDireccion.setText(Constantes.ERROR);
        }
        return view;
    }
}