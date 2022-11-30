package com.example.teleappsistencia.ui.fragments.grupos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarGruposFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarGruposFragment extends Fragment {

    private Grupo grupo;

    private TextView textView_nombre;

    public ConsultarGruposFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param grupo
     * @return A new instance of fragment ConsultarGruposFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarGruposFragment newInstance(Grupo grupo) {
        ConsultarGruposFragment fragment = new ConsultarGruposFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.GRUPO, grupo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            grupo = (Grupo) getArguments().getSerializable(Constantes.GRUPO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_grupos, container, false);

        this.textView_nombre = view.findViewById(R.id.textView_consultar_nombre_grupos);

        this.textView_nombre.setText(grupo.getName());

        return view;
    }
}