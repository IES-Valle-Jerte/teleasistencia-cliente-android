package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.R;

/**
 * Una clase {@link Fragment} para recoger los datos a mostrar dentro de la tarjeta Paciente del RecyclerView.
 * <p> Esta clase es una subclase de {@link Fragment} y hereda de ella todos sus métodos y atributos.
 */
public class paciente_card extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Constructor por defecto.
    public paciente_card() {
    }

    // TODO: Rename and change types and number of parameters
    public static paciente_card newInstance(String param1, String param2) {
        paciente_card fragment = new paciente_card();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_paciente_card, container, false);
        // Inflate the layout for this fragment
        return root;
    }
}