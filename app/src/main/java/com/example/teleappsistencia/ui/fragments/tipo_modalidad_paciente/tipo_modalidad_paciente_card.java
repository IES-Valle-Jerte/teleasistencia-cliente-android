package com.example.teleappsistencia.ui.fragments.tipo_modalidad_paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tipo_modalidad_paciente_card#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tipo_modalidad_paciente_card extends Fragment {

    public tipo_modalidad_paciente_card() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment tipo_modalidad_paciente_card.
     */
    public static tipo_modalidad_paciente_card newInstance() {
        tipo_modalidad_paciente_card fragment = new tipo_modalidad_paciente_card();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_tipo_modalidad_paciente_card, container, false);

        // Inflate the layout for this fragment
        return root;
    }
}