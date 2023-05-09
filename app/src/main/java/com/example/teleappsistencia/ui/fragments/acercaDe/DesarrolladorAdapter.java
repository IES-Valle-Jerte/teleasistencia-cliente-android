package com.example.teleappsistencia.ui.fragments.acercaDe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Desarrollador;
import com.example.teleappsistencia.ui.fragments.alarma.AlarmaAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DesarrolladorAdapter extends RecyclerView.Adapter<DesarrolladorAdapter.DesarrolladorViewHolder>{

    List<Desarrollador> lDesarrolladores;

    DesarrolladorViewHolder desarrolladorViewHolder;

    Desarrollador desarrolladorSeleccionado;

    private AlarmaAdapter.OnItemSelectedListener listener;

    private int selectedPosition = RecyclerView.NO_POSITION;

    public DesarrolladorAdapter(List<Desarrollador> lDesarrolladores) {
        this.lDesarrolladores = lDesarrolladores;
    }


    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public Desarrollador getDesarrolladorSeleccionado() {
        return desarrolladorSeleccionado;
    }

    public void setDesarrolladorSeleccionado(Desarrollador desarrolladorSeleccionado) {
        this.desarrolladorSeleccionado = desarrolladorSeleccionado;
    }

    public Desarrollador getItemAtPosition(int position) {
        return lDesarrolladores.get(position);
    }

    public static class DesarrolladorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;

        private ImageView imagen_perfil;

        private TextView text_nombre;

        private RecyclerView recyclerView_tecnologias;

        private Desarrollador desarrollador;

        public DesarrolladorViewHolder(View v){
            super(v);
            imagen_perfil = (ImageView) v.findViewById(R.id.imagenPerfilDesarrolladorCard);
            text_nombre = (TextView) v.findViewById(R.id.textViewNombreDesarrolladorCard);
            recyclerView_tecnologias = (RecyclerView) v.findViewById(R.id.recyclerView_tecnologias);
        }

        public void setOnClickListeners() {

        }


        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public int getItemCount() {
        return lDesarrolladores.size();
    }

    @Override
    public DesarrolladorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_desarrollador_card, viewGroup, false);
        return new DesarrolladorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DesarrolladorViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        Desarrollador desarrollador = lDesarrolladores.get(i);

        viewHolder.text_nombre.setText(desarrollador.getNombre());
        Picasso.get().load(desarrollador.getImagen()).into(viewHolder.imagen_perfil);

        //TODO trabajar con el desarrollador
    }



}
