package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.R;

/**
 * Un {@link Fragment} que representa una tarjeta de Usuario para el RecyclerView.
 */
public class usuario_card extends Fragment {

    public usuario_card() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment usuario_card.
     */
    public static usuario_card newInstance() {
        usuario_card fragment = new usuario_card();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_usuario_sistema_card, container, false);
    }
}