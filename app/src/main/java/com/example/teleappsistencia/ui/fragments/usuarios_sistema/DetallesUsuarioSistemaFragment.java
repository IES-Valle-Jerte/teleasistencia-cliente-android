package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallesUsuarioSistemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallesUsuarioSistemaFragment extends Fragment {
    private static final String ARG_USUARIO = "usuario";

    private Usuario usuario;

    ImageView ivFotoPerfil;
    TextView tvId;
    TextView tvUrl;
    TextView tvLastLogin;
    TextView tvUsername;
    TextView tvNombre;
    TextView tvApellidos;
    TextView tvEmail;
    TextView tvFechaAlta;
    TextView tvGrupo;

    // Required empty public constructor
    public DetallesUsuarioSistemaFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuario Usuario a mostrar.
     * @return A new instance of fragment DetallesUsuarioSistemaFragment.
     */
    public static DetallesUsuarioSistemaFragment newInstance(Usuario usuario) {
        DetallesUsuarioSistemaFragment fragment = new DetallesUsuarioSistemaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(ARG_USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalles_usuario_sistema, container, false);
        getReferenciasGUI(view);
        cargarDatosUser();
        return view;
    }

    /**
     * Extrae las referencias a las distintas vistas del layout.
     */
    private void getReferenciasGUI(View view) {
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil);
        tvId = view.findViewById(R.id.tvId);
        tvUrl = view.findViewById(R.id.tvUrl);
        tvLastLogin = view.findViewById(R.id.tvLastLogin);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvNombre = view.findViewById(R.id.tvNombre);
        tvApellidos = view.findViewById(R.id.tvApellidos);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvFechaAlta = view.findViewById(R.id.tvFechaAlta);
        tvGrupo = view.findViewById(R.id.tvGrupo);
    }

    /**
     * Carga los datos del Usuario en las distintas vistas.
     */
    private void cargarDatosUser() {
        // Intentar cargar la imagen
        if (usuario.getImagen() != null) {
            Utilidad.cargarImagen(usuario.getImagen().getUrl(), ivFotoPerfil, 0);
        } else {
            Utilidad.cargarImagen(R.drawable.default_user, ivFotoPerfil, 0);
        }

        // Datos que no pueden ser nulos por la API
        tvId.setText(Integer.toString(usuario.getPk()));
        tvUrl.setText(usuario.getUrl());
        tvFechaAlta.setText(Utilidad.getStringDate(usuario.getDateJoined()));
        tvUsername.setText(usuario.getUsername());

        // Resto de datos
        if (null != usuario.getFirstName() && !usuario.getFirstName().trim().isEmpty()) {
            tvNombre.setText(usuario.getFirstName());
        }
        if (null != usuario.getLastName() && !usuario.getLastName().trim().isEmpty()) {
            tvApellidos.setText(usuario.getLastName());
        }
        if (null != usuario.getLastLogin()) {
            String lastLogin = Utilidad.getStringDatetime(usuario.getLastLogin());
            if (null != lastLogin) tvLastLogin.setText(lastLogin);
        }
        if (null != usuario.getEmail() && !usuario.getEmail().trim().isEmpty()) {
            tvEmail.setText(usuario.getEmail());
        }
        if (null != usuario.getGroups()) {
            StringBuilder roles = new StringBuilder();
            List<Object> grupos = (ArrayList) usuario.getGroups();

            if (!grupos.isEmpty()) {
                grupos.forEach(g -> {
                    Grupo _g = (Grupo) Utilidad.getObjeto(g, Constantes.GRUPO);
                    String nombre = _g.getName();
                    nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
                    roles.append(nombre).append(", ");
                });
                // Eliminar comas sobrantes
                roles.delete(roles.length() - 2, roles.length());

                tvGrupo.setText(roles.toString());
            }
        }
    }
}