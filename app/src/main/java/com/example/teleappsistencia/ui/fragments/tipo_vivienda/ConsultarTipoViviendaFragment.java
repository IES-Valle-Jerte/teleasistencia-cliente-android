package com.example.teleappsistencia.ui.fragments.tipo_vivienda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.TipoVivienda;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarTipoViviendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarTipoViviendaFragment extends Fragment {

    private TipoVivienda tipoVivienda;

    private TextView textView_nombre;

    public ConsultarTipoViviendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tipoVivienda
     * @return A new instance of fragment ConsultarTipoViviendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarTipoViviendaFragment newInstance(TipoVivienda tipoVivienda) {
        ConsultarTipoViviendaFragment fragment = new ConsultarTipoViviendaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.TIPO_VIVIENDA, tipoVivienda);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipoVivienda = (TipoVivienda) getArguments().getSerializable(Constantes.TIPO_VIVIENDA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_tipo_vivienda, container, false);

        this.textView_nombre = view.findViewById(R.id.textView_consultar_nombre_tipoVivienda);

        this.textView_nombre.setText(this.tipoVivienda.getNombre());

        return view;
    }
}