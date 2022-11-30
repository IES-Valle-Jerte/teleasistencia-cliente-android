package com.example.teleappsistencia.ui.fragments.relacion_paciente_persona;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.RelacionPacientePersona;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarRelacionPacientePersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarRelacionPacientePersonaFragment extends Fragment {

    private RelacionPacientePersona relacionPacientePersona;
    private TextView textViewConsultarTipoRelacion;
    private TextView textViewConsultarTieneLlaveViviendas;
    private TextView textViewConsultarDisponibilidad;
    private TextView textViewConsultarObservaciones;
    private TextView textViewConsultarPrioridad;
    private TextView textViewConsultarNumeroSeguridadSocialPaciente;
    private TextView textViewConsultarNombrePersona;

    public ConsultarRelacionPacientePersonaFragment() {
        // Required empty public constructor
    }

    public static ConsultarRelacionPacientePersonaFragment newInstance(RelacionPacientePersona relacionPacientePersona) {
        ConsultarRelacionPacientePersonaFragment fragment = new ConsultarRelacionPacientePersonaFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoRelacionPacientePersona", relacionPacientePersona);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            relacionPacientePersona = (RelacionPacientePersona) getArguments().getSerializable("objetoRelacionPacientePersona");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_consultar_relacion_paciente_persona, container, false);
        // Obtener los elementos del layout
        textViewConsultarTipoRelacion = root.findViewById(R.id.textViewConsultarTipoRelacion);
        textViewConsultarTieneLlaveViviendas = root.findViewById(R.id.textViewConsultarTieneLlaveViviendas);
        textViewConsultarDisponibilidad = root.findViewById(R.id.textViewConsultarDisponibilidad);
        textViewConsultarObservaciones = root.findViewById(R.id.textViewConsultarObservaciones);
        textViewConsultarPrioridad = root.findViewById(R.id.textViewConsultarPrioridad);
        textViewConsultarNumeroSeguridadSocialPaciente = root.findViewById(R.id.textViewConsultarNumeroSeguridadSocialPaciente);
        textViewConsultarNombrePersona = root.findViewById(R.id.textViewConsultarNombrePersona);
        // Mostrar los datos del objeto en los elementos del layout
        textViewConsultarTipoRelacion.setText(relacionPacientePersona.getTipoRelacion());
        if (relacionPacientePersona.isTieneLlavesVivienda()) {
            textViewConsultarTieneLlaveViviendas.setText("Sí");
            textViewConsultarTieneLlaveViviendas.setTextColor(Color.GREEN);
        } else {
            textViewConsultarTieneLlaveViviendas.setText("No");
            textViewConsultarTieneLlaveViviendas.setTextColor(Color.RED);
        }
        textViewConsultarDisponibilidad.setText(relacionPacientePersona.getDisponibilidad());
        textViewConsultarObservaciones.setText(relacionPacientePersona.getObservaciones());
        textViewConsultarPrioridad.setText(String.valueOf(relacionPacientePersona.getPrioridad()));
        Paciente paciente = (Paciente) Utilidad.getObjeto(relacionPacientePersona.getIdPaciente(), "Paciente");
        if (paciente != null) {
            textViewConsultarNumeroSeguridadSocialPaciente.setText(paciente.getNumeroSeguridadSocial());
        } else {
            textViewConsultarNumeroSeguridadSocialPaciente.setText("");
        }
        Persona persona = (Persona) Utilidad.getObjeto(relacionPacientePersona.getIdPersona(), "Persona");
        if (persona != null) {
            textViewConsultarNombrePersona.setText(persona.getNombre()+" "+persona.getApellidos());
        } else {
            textViewConsultarNombrePersona.setText("");
        }
        // Inflate the layout for this fragment
        return root;
    }

}