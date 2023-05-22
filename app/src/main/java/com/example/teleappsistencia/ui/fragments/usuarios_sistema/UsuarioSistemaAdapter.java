package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.ui.fragments.paciente.PacienteAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

/**
 *
 */
public class UsuarioSistemaAdapter extends RecyclerView.Adapter<UsuarioSistemaAdapter.UsuarioSistemaViewHolder> {
    //? Atributos
    private List<Usuario> items;

    private Usuario selectedItem;
    private int selectedPosition = RecyclerView.NO_POSITION;

//    private UsuarioSistemaViewHolder usuarioViewHolder;
    private OnItemSelectedListener listener;

    // ? Getters y Setters
    // Devuelve el usuario seleccionado
    public Usuario getUsuarioSelecionado() {
        return selectedPosition != RecyclerView.NO_POSITION ? selectedItem : null;
    }

    // Define el método getItemAtPosition para obtener el elemento en la posición indicada
    public Usuario getItemAtPosition(int position) {
        return items.get(position);
    }

    // Interfaz para poder conectar un listener y que gestione los eventos
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    // ? Clases internas
    /**
     * Una sub-clase de {@link RecyclerView.ViewHolder} para recoger los datos a mostrar dentro de la tarjeta del RecyclerView.
     */
    public static class UsuarioSistemaViewHolder extends RecyclerView.ViewHolder {
        // Contexto de la aplicación.
        public Context context;

        // Usuario que se muestra en la tarjeta.
        private Usuario usuario;

        // Atributos de la tarjeta en la interfaz de usuario (UI).
        private ImageView cardIV_imagen;
        private TextView cardTV_groups;
        private TextView cardTV_nom_ape;
        private TextView cardTV_username;

        /**
         * Constructor de la clase {@link UsuarioSistemaAdapter.UsuarioSistemaViewHolder}.
         *
         * @param v Vista de la tarjeta.
         */
        public UsuarioSistemaViewHolder(View v) {
            super(v);
            this.context = v.getContext();

            // Extraer referencias GUI
            cardIV_imagen = v.findViewById(R.id.cardIV_imagen);
            cardTV_groups = v.findViewById(R.id.cardTV_groups);
            cardTV_nom_ape = v.findViewById(R.id.cardTV_nom_ape);
            cardTV_username = v.findViewById(R.id.cardTV_username);
        }

        /**
         * Establece los datos del paciente a mostrar en la tarjeta.
         *
         * @param usuario El usuario del sistema a mostrar.
         */
        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
    }

    // ? Constructores y métodos
    /**
     * Constructor de la clase {@link UsuarioSistemaAdapter}.
     *
     * @param usuarios_sistema La lista de objetos Usuario a mostrar dentro del RecyclerView.
     */
    public UsuarioSistemaAdapter(List<Usuario> usuarios_sistema, OnItemSelectedListener listener) {
        this.items = usuarios_sistema;
        this.listener = listener;
    }


    /**
     * La función devuelve el número de elementos de la lista.
     *
     * @return El número de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Se llama a OnCreateViewHolder() cuando RecyclerView necesita un nuevo
     * RecyclerView.ViewHolder para representar un elemento.
     *
     * @param viewGroup El ViewGroup en el que se agregará la nueva vista después de vincularla a una
     *                  posición de adaptador.
     * @param i         El tipo de vista de la nueva Vista.
     * @return El objeto ViewHolder.
     */
    @Override
    public UsuarioSistemaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.fragment_usuario_sistema_card, viewGroup, false);

        UsuarioSistemaViewHolder viewHolder = new UsuarioSistemaViewHolder(v);

        // Establecer un click listener para ViewHolder
        viewHolder.itemView.setOnClickListener(_v -> {
            // Actualizar la posición seleccionada y notificar al adapter
            int previousSelectedPosition = selectedPosition;
            selectedPosition = viewHolder.getAdapterPosition();
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);

            // Llamar al método onItemSelected de OnItemSelectedListener
            if (listener != null) {
                listener.onItemSelected(selectedPosition);
            }
            selectedItem = viewHolder.usuario;
        });

        return viewHolder;
    }


    /**
     * Método que configura el texto a mostrar dentro de la CardView.
     *
     * @param viewHolder El ViewHolder que debe actualizarse para representar el contenido del elemento
     *                   en la posición dada en el conjunto de datos.
     * @param i          La posición del elemento en el conjunto de datos del adaptador.
     */
    @Override
    public void onBindViewHolder(UsuarioSistemaViewHolder viewHolder, int i) {
        Usuario usuario = items.get(i);
        viewHolder.setUsuario(usuario);

        // Actualizar GUI con los datos del usuario
        // Cargar textos
        if (usuario.getGroups() != null) {
            StringBuilder roles = new StringBuilder("");
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

                viewHolder.cardTV_groups.setText(roles.toString());
            } else {
                viewHolder.cardTV_groups.setText(R.string.usuario_card_placeholder_grupos);
            }
        } else {
            viewHolder.cardTV_groups.setText(R.string.usuario_card_placeholder_grupos);
        }
        if (usuario.getFirstName() != null && usuario.getLastName() != null) {
            StringBuilder nom_ape = new StringBuilder("");

            nom_ape.append(usuario.getFirstName()).append(' ');
            nom_ape.append(usuario.getLastName());

            viewHolder.cardTV_nom_ape.setText(nom_ape.toString());
        } else {
            viewHolder.cardTV_nom_ape.setText(R.string.usuario_card_placeholder_nom_ape);
        }
        if (usuario.getUsername() != null && !usuario.getUsername().isEmpty()) {
            viewHolder.cardTV_username.setText(usuario.getUsername());
        } else {
            viewHolder.cardTV_username.setText(R.string.usuario_card_placeholder_username);
        }
        // Intentar cargar la imagen
        if (usuario.getImagen() != null) {
            Utilidad.cargarImagen(usuario.getImagen().getUrl(), viewHolder.cardIV_imagen, Constantes.IMG_PERFIL_RADIOUS_LISTA);
        } else {
            Utilidad.cargarImagen(R.drawable.default_user, viewHolder.cardIV_imagen, Constantes.IMG_PERFIL_RADIOUS_LISTA);
        }


        // Establecer el color de fondo del ViewHolder en función de si está seleccionado o no
        if (selectedPosition == i) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.azul));
        } else {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), android.R.color.white));
        }
    }
}
