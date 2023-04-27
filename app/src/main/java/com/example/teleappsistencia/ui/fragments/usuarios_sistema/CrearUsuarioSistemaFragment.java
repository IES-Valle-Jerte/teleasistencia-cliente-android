package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearUsuarioSistemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearUsuarioSistemaFragment extends Fragment {


    // Constructor público vacío requerido por newInstance()
    public CrearUsuarioSistemaFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrearUsuarioSistemaFragment.
     */
    public static CrearUsuarioSistemaFragment newInstance() {
        CrearUsuarioSistemaFragment fragment = new CrearUsuarioSistemaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_usuario_sistema, container, false);
    }
}