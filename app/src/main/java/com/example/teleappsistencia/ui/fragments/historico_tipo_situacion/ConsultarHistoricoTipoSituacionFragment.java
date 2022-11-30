package com.example.teleappsistencia.ui.fragments.historico_tipo_situacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.HistoricoTipoSituacion;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoSituacion;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarHistoricoTipoSituacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarHistoricoTipoSituacionFragment extends Fragment {

    private HistoricoTipoSituacion historicoTipoSituacion;

    private TextView textView_id;
    private TextView textView_fecha;
    private TextView textView_idTerminal;
    private TextView textView_idTipoSituacion;

    public ConsultarHistoricoTipoSituacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param historicoTipoSituacion
     * @return A new instance of fragment ConsultarHistoricoTipoSituacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarHistoricoTipoSituacionFragment newInstance(HistoricoTipoSituacion historicoTipoSituacion) {
        ConsultarHistoricoTipoSituacionFragment fragment = new ConsultarHistoricoTipoSituacionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.HISTORICO_TIPO_SITUACION, historicoTipoSituacion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            historicoTipoSituacion = (HistoricoTipoSituacion) getArguments().getSerializable(Constantes.HISTORICO_TIPO_SITUACION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_historico_tipo_situacion, container, false);

        this.textView_id = view.findViewById(R.id.textView_consultar_id_historicoTipoSituacion);
        this.textView_fecha = view.findViewById(R.id.textView_consultar_fecha_historicoTipoSituacion);
        this.textView_idTerminal = view.findViewById(R.id.textView_consultar_idTerminal_historicoTipoSituacion);
        this.textView_idTipoSituacion = view.findViewById(R.id.textView_consultar_idTipoSituacion_historicoTipoSituacion);

        Terminal terminal = (Terminal) Utilidad.getObjeto(historicoTipoSituacion.getTerminal(), Constantes.TERMINAL);
        TipoSituacion tipoSituacion = (TipoSituacion) Utilidad.getObjeto(historicoTipoSituacion.getIdTipoSituacion(), Constantes.TIPO_SITUACION);

        this.textView_id.setText(Integer.toString(historicoTipoSituacion.getId()));
        this.textView_fecha.setText(historicoTipoSituacion.getFecha());
        this.textView_idTerminal.setText(terminal.getNumeroTerminal());
        this.textView_idTipoSituacion.setText(Integer.toString(tipoSituacion.getId()));

        return view;
    }
}