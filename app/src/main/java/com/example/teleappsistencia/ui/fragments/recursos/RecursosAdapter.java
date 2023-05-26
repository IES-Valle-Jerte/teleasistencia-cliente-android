package com.example.teleappsistencia.ui.fragments.recursos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

public class RecursosAdapter extends RecyclerView.Adapter<RecursosAdapter.RecursoComunitarioViewHolder> {

    /*
        Declaración de atributos.
     */
    private List<RecursoComunitario> items;
    private RecursoComunitarioViewHolder recursoComunitarioViewHolder;

    /*
        Item seleccionado
     */
    private RecursoComunitario recursoSeleccionado;

    /*
        Interfaz que establece el item seleccionado
     */
    private OnItemSelectedListener listener;

    /*
        Posición seleccionada.
     */
    private int selectedPosition = RecyclerView.NO_POSITION;

    /**
     * Interfaz que crea el listener segun la posición del item.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    /**
     * Método que retorna el recursoSeleccionado.
     *
     * @return
     */
    public RecursoComunitario getRecursoSeleccionado() {
        return recursoSeleccionado;
    }

    /**
     * Método que retorna el item seleccionado.
     *
     * @param position
     * @return
     */
    public RecursoComunitario getItemAtPosition(int position) {
        return items.get(position);
    }

    /**
     * Método que inicializa el card de los recursos.
     */
    public static class RecursoComunitarioViewHolder extends RecyclerView.ViewHolder {

        // Campos respectivos de un item.
        public Context context;
        private TextView nombreRecursoComunitario;
        private TextView telefonoRecursoComunitario;
        private TextView tipoRecursoComunitario;
        private TextView direccionRecursoComunitario;
        private RecursoComunitario recursoComunitario;

        /**
         * Método para establecer el recurso comunitario.
         *
         * @param recursoComunitario: Recibe por parámetros el recurso comunitario.
         */
        public void setRecursoComunitario(RecursoComunitario recursoComunitario) {
            this.recursoComunitario = recursoComunitario;
        }

        /**
         * Se inicializan las variables.
         *
         * @param v: Recibe por parámetros la vista.
         */
        public RecursoComunitarioViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.nombreRecursoComunitario = (TextView) v.findViewById(R.id.nombreRecursoComunitario);
            this.telefonoRecursoComunitario = (TextView) v.findViewById(R.id.telefonoRecursoComunitario);
            this.tipoRecursoComunitario = (TextView) v.findViewById(R.id.tipoRecursoComunitario);
            this.direccionRecursoComunitario = (TextView) v.findViewById(R.id.direccionRecursoComunitario);
        }
    }

    /**
     * Constructor por defecto.
     */
    public RecursosAdapter() { }

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param items
     */
    public RecursosAdapter(List<RecursoComunitario> items) {
        this.items = items;
    }

    /**
     * Método para devolver el tamaño de la lista de recursos comunitarios.
     *
     * @return: Retorna el tamaño.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Método que carga el layout de las card view.
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @Override
    public RecursoComunitarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_recursos_card, viewGroup, false);
        this.recursoComunitarioViewHolder = new RecursoComunitarioViewHolder(v);
        return recursoComunitarioViewHolder;
    }

    /**
     * Método que muestra los valores del recurso comunitario.
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(RecursoComunitarioViewHolder viewHolder, int i) {

        RecursoComunitario recursoComunitario = items.get(i);
        TipoRecursoComunitario tipoRecursoComunitario = (TipoRecursoComunitario) Utilidad.getObjeto(recursoComunitario.getTipoRecursoComunitario(), Constantes.TIPO_RECURSO_COMUNITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(recursoComunitario.getDireccion(), Constantes.DIRECCION);

        viewHolder.nombreRecursoComunitario.setText(items.get(i).getNombre());
        viewHolder.telefonoRecursoComunitario.setText(items.get(i).getTelefono());
        viewHolder.tipoRecursoComunitario.setText(tipoRecursoComunitario.getNombreTipoRecursoComunitario());
        viewHolder.direccionRecursoComunitario.setText(direccion.getDireccion());

        this.recursoComunitarioViewHolder.setRecursoComunitario(items.get(i));

        // Establece un click listener para ViewHolder
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guarda la posición y notifica al adapter
                int previousSelectedPosition = selectedPosition;
                selectedPosition = viewHolder.getAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);

                if(listener != null){
                    listener.onItemSelected(selectedPosition);
                }

                recursoSeleccionado = viewHolder.recursoComunitario;
            }
        });

        if(selectedPosition == i){
            // Con esta forma, el cardView no pierde la forma (Bordes redondeados y margenes.).
            viewHolder.itemView.findViewById(R.id.fondo).setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.azul));

            // Con esta forma, el cardView pierde la forma.
            // viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.azul));
        } else {
            // Con esta forma, el cardView no pierde la forma (Bordes redondeados y margenes.).
            viewHolder.itemView.findViewById(R.id.fondo).setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white));

            // Con esta forma, el cardView pierde la forma.
            // viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white));
        }
    }
}