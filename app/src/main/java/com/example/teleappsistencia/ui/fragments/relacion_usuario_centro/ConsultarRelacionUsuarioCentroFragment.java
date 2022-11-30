package com.example.teleappsistencia.ui.fragments.relacion_usuario_centro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.RelacionTerminalRecursoComunitario;
import com.example.teleappsistencia.modelos.RelacionUsuarioCentro;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarRelacionUsuarioCentroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarRelacionUsuarioCentroFragment extends Fragment {

    private RelacionUsuarioCentro relacionUsuarioCentro;

    private TextView textViewRelacionUsuarioCentroConsultar;
    private TextView textViewpersonaContactoConsultar;
    private TextView textViewConsultarDistanciaRelacionUsuario;
    private TextView textViewConsultarTiempo;
    private TextView textViewobservacionesConsultarRelacionUsuario;
    private TextView textViewConsultarNumeroSeguridadSocialPaciente;
    private TextView textViewNombreCentroSanitarioConsultar;

    public ConsultarRelacionUsuarioCentroFragment() {
        // Required empty public constructor
    }

    public static ConsultarRelacionUsuarioCentroFragment newInstance(RelacionUsuarioCentro relacionUsuarioCentro) {
        ConsultarRelacionUsuarioCentroFragment fragment = new ConsultarRelacionUsuarioCentroFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoRelacionUsuarioCentro", relacionUsuarioCentro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            relacionUsuarioCentro = (RelacionUsuarioCentro) getArguments().getSerializable("objetoRelacionUsuarioCentro");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_consultar_relacion_usuario_centro, container, false);
        textViewRelacionUsuarioCentroConsultar = root.findViewById(R.id.textViewRelacionUsuarioCentroConsultar);
        textViewpersonaContactoConsultar = root.findViewById(R.id.textViewpersonaContactoConsultar);
        textViewConsultarDistanciaRelacionUsuario = root.findViewById(R.id.textViewConsultarDistanciaRelacionUsuario);
        textViewConsultarTiempo = root.findViewById(R.id.textViewConsultarTiempo);
        textViewobservacionesConsultarRelacionUsuario = root.findViewById(R.id.textViewobservacionesConsultarRelacionUsuario);
        textViewConsultarNumeroSeguridadSocialPaciente = root.findViewById(R.id.textViewConsultarNumeroSeguridadSocialPaciente);
        textViewNombreCentroSanitarioConsultar = root.findViewById(R.id.textViewNombreCentroSanitarioConsultar);
        textViewRelacionUsuarioCentroConsultar.setText(String.valueOf(relacionUsuarioCentro.getId()));
        textViewpersonaContactoConsultar.setText(relacionUsuarioCentro.getPersonaContacto());
        textViewConsultarDistanciaRelacionUsuario.setText(String.valueOf(relacionUsuarioCentro.getDistancia()));
        textViewConsultarTiempo.setText(String.valueOf(relacionUsuarioCentro.getTiempo()));
        textViewobservacionesConsultarRelacionUsuario.setText(relacionUsuarioCentro.getObservaciones());
        if(relacionUsuarioCentro.getIdPaciente() != null){
            Paciente paciente = (Paciente) Utilidad.getObjeto(relacionUsuarioCentro.getIdPaciente(), "Paciente");
            textViewConsultarNumeroSeguridadSocialPaciente.setText(paciente.getNumeroSeguridadSocial());
        }else{
            textViewConsultarNumeroSeguridadSocialPaciente.setText("");
        }
        CentroSanitario centroSanitario = (CentroSanitario) Utilidad.getObjeto(relacionUsuarioCentro.getIdCentroSanitario(), "CentroSanitario");
        textViewNombreCentroSanitarioConsultar.setText(centroSanitario.getNombre());
        return root;
    }

}