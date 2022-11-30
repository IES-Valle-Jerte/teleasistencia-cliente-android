package com.example.teleappsistencia.ui.fragments.usuarios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarUsuariosFragment extends Fragment {

    private Usuario usuario;

    private TextView textView_nombreUsuario;
    private TextView textView_nombre;
    private TextView textView_apellidos;
    private TextView textView_email;
    private TextView textView_grupo;

    public ConsultarUsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuario
     * @return A new instance of fragment ConsultarUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarUsuariosFragment newInstance(Usuario usuario) {
        ConsultarUsuariosFragment fragment = new ConsultarUsuariosFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(Constantes.USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_usuarios, container, false);

        this.textView_nombreUsuario = view.findViewById(R.id.textView_consultar_nombreUsuario_usuario);
        this.textView_nombre = view.findViewById(R.id.textView_consultar_nombre_usuario);
        this.textView_apellidos = view.findViewById(R.id.textView_consultar_apellidos_usuario);
        this.textView_email = view.findViewById(R.id.textView_consultar_email_usuario);
        this.textView_grupo = view.findViewById(R.id.textView_consultar_grupo_usuario);

        this.textView_nombreUsuario.setText(usuario.getUsername());
        this.textView_nombre.setText(usuario.getFirstName());
        this.textView_apellidos.setText(usuario.getLastName());
        this.textView_email.setText(usuario.getEmail());

        List<Grupo> grupos = (List<Grupo>) usuario.getGroups();
        Grupo grupo;
        if(!grupos.isEmpty()) {
            grupo = (Grupo) Utilidad.getObjeto(grupos.get(0), "Grupo");
            this.textView_grupo.setText(grupo.getName());
        } else {
            this.textView_grupo.setText(Constantes.STRING_VACIO);
        }

        return view;
    }
}