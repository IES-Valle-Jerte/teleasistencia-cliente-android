package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarUsuarioSistemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarUsuarioSistemaFragment extends Fragment {
    private static final String ARG_USUARIO = "usuario";
    private static final String ARG_EDITMODE = "editMode";

    private Usuario usuario;
    private boolean editMode;



    // Referencias GUI



    // Required empty public constructor
    public EditarUsuarioSistemaFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuario Usuario a modificar
     * @param editMode booleano que especifica si se quiere modificar o crear un nuevo usuario (para reutilizar el fragment)
     * @return A new instance of fragment EditarUsuarioSistemaFragment.
     */
    public static EditarUsuarioSistemaFragment newInstance(Usuario usuario, boolean editMode) {
        EditarUsuarioSistemaFragment fragment = new EditarUsuarioSistemaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USUARIO, usuario);
        args.putBoolean(ARG_EDITMODE, editMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(ARG_USUARIO);
            editMode = getArguments().getBoolean(ARG_EDITMODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_usuario_sistema, container, false);

        getReferenciasGUI(view);
        cargarDatosUser();

        return view;
    }

    /**
     * Extrae las referencias a las distintas vistas del layout
     */
    private void getReferenciasGUI(View view) {

    }

    /**
     * Carga los datos del Usuario en las distintas vistas.
     */
    private void cargarDatosUser() {

    }
}