package com.example.teleappsistencia.ui.fragments.terminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.RelacionUsuarioCentro;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoVivienda;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarTerminalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarTerminalFragment extends Fragment {

    private Terminal terminal;

    private TextView textViewIdTerminalConsultar;
    private TextView textViewNumeroTerminalConsultarTerminal;
    private TextView textViewConsultarModoAccesoVivienda;
    private TextView textViewConsultarBarrerasArquitectonicas;
    private TextView textViewNumeroSeguridadSocialTerminalConsultar;
    private TextView textViewTipoViviendaConsultar;

    public ConsultarTerminalFragment() {
        // Required empty public constructor
    }

    public static ConsultarTerminalFragment newInstance(Terminal terminal) {
        ConsultarTerminalFragment fragment = new ConsultarTerminalFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoTerminal", terminal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            terminal = (Terminal) getArguments().getSerializable("objetoTerminal");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_consultar_terminal, container, false);
        textViewIdTerminalConsultar = root.findViewById(R.id.textViewIdTerminalConsultar);
        textViewNumeroTerminalConsultarTerminal = root.findViewById(R.id.textViewNumeroTerminalConsultarTerminal);
        textViewConsultarModoAccesoVivienda = root.findViewById(R.id.textViewConsultarModoAccesoVivienda);
        textViewConsultarBarrerasArquitectonicas = root.findViewById(R.id.textViewConsultarBarrerasArquitectonicas);
        textViewNumeroSeguridadSocialTerminalConsultar = root.findViewById(R.id.textViewNumeroSeguridadSocialTerminalConsultar);
        textViewTipoViviendaConsultar = root.findViewById(R.id.textViewTipoViviendaConsultar);
        textViewIdTerminalConsultar.setText(String.valueOf(terminal.getId()));
        textViewNumeroTerminalConsultarTerminal.setText(terminal.getNumeroTerminal());
        textViewConsultarModoAccesoVivienda.setText(terminal.getModoAccesoVivienda());
        textViewConsultarBarrerasArquitectonicas.setText(terminal.getBarrerasArquitectonicas());
        Paciente paciente = (Paciente) Utilidad.getObjeto(terminal.getTitular(), "Paciente");
        if (paciente != null) {
            textViewNumeroSeguridadSocialTerminalConsultar.setText(paciente.getNumeroSeguridadSocial());
        }
        TipoVivienda tipoVivienda = (TipoVivienda) Utilidad.getObjeto(terminal.getTipoVivienda(), "TipoVivienda");
        if (tipoVivienda != null) {
            textViewNumeroSeguridadSocialTerminalConsultar.setText(paciente.getNumeroSeguridadSocial());
        }
        return root;
    }

}