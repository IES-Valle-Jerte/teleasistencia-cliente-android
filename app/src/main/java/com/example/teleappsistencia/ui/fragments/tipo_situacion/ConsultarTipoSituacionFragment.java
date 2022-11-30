package com.example.teleappsistencia.ui.fragments.tipo_situacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.TipoSituacion;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarTipoSituacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarTipoSituacionFragment extends Fragment {

    private TipoSituacion tipoSituacion;

    private TextView textView_nombre;

    public ConsultarTipoSituacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tipoSituacion
     * @return A new instance of fragment ConsultarTipoSituacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarTipoSituacionFragment newInstance(TipoSituacion tipoSituacion) {
        ConsultarTipoSituacionFragment fragment = new ConsultarTipoSituacionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.TIPO_SITUACION, tipoSituacion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipoSituacion = (TipoSituacion) getArguments().getSerializable(Constantes.TIPO_SITUACION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_tipo_situacion, container, false);

        this.textView_nombre = view.findViewById(R.id.textView_consultar_nombre_tipoSituacion);

        this.textView_nombre.setText(this.tipoSituacion.getNombre());
        return view;
    }
}