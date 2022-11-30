package com.example.teleappsistencia.ui.fragments.relacion_terminal_recurso_comunitario;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.RelacionPacientePersona;
import com.example.teleappsistencia.modelos.RelacionTerminalRecursoComunitario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarRelacionTerminalRecursoComunitarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarRelacionTerminalRecursoComunitarioFragment extends Fragment {

    private RelacionTerminalRecursoComunitario relacionTerminalRecursoComunitario;

    private TextView textViewConsultarIdRelacionTerminalRecursoComunitario;
    private TextView textViewConsultarNumeroTerminal;
    private TextView textViewConsultarNombreRecursoComunitario;

    public ConsultarRelacionTerminalRecursoComunitarioFragment() {
        // Required empty public constructor
    }

    public static ConsultarRelacionTerminalRecursoComunitarioFragment newInstance(RelacionTerminalRecursoComunitario relacionTerminalRecursoComunitario) {
        ConsultarRelacionTerminalRecursoComunitarioFragment fragment = new ConsultarRelacionTerminalRecursoComunitarioFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoRelacionTerminalRecursoComunitario", relacionTerminalRecursoComunitario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            relacionTerminalRecursoComunitario = (RelacionTerminalRecursoComunitario) getArguments().getSerializable("objetoRelacionTerminalRecursoComunitario");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_consultar_relacion_terminal_recurso_comunitario, container, false);
        textViewConsultarIdRelacionTerminalRecursoComunitario = root.findViewById(R.id.textViewConsultarIdRelacionTerminalRecursoComunitario);
        textViewConsultarNumeroTerminal = root.findViewById(R.id.textViewConsultarNumeroTerminal);
        textViewConsultarNombreRecursoComunitario = root.findViewById(R.id.textViewConsultarNombreRecursoComunitario);
        textViewConsultarIdRelacionTerminalRecursoComunitario.setText(String.valueOf(relacionTerminalRecursoComunitario.getId()));
        Terminal terminal = (Terminal) Utilidad.getObjeto(relacionTerminalRecursoComunitario.getIdTerminal(), "Terminal");
        if (terminal != null) {
            textViewConsultarNumeroTerminal.setText(terminal.getNumeroTerminal());
        }
        RecursoComunitario recursoComunitario = (RecursoComunitario) Utilidad.getObjeto(relacionTerminalRecursoComunitario.getIdRecursoComunitario(), "RecursoComunitario");
        if (recursoComunitario != null) {
            textViewConsultarNombreRecursoComunitario.setText(recursoComunitario.getNombre());
        }
        return root;
    }

}