package com.example.teleappsistencia.ui.fragments.acercaDe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Desarrollador;
import com.example.teleappsistencia.modelos.Desarrollador_tecnologia;
import com.example.teleappsistencia.modelos.Tecnologia;
import com.example.teleappsistencia.modelos.TecnologiaRel;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DesarrolladorAdapter extends RecyclerView.Adapter<DesarrolladorAdapter.DesarrolladorViewHolder>{

    private List<TecnologiaRel> items;

    private DesarrolladorViewHolder desarrolladorViewHolder;

    public static class DesarrolladorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Context context;

        private ImageView imagenTecnologia;

        private TecnologiaRel tecnologiaRel;

        public TecnologiaRel getTecnologiaRel() {
            return tecnologiaRel;
        }

        public void setDesarrollador_tecnologia(TecnologiaRel tecnologiaRel) {
            this.tecnologiaRel = tecnologiaRel;
        }

        public DesarrolladorViewHolder(View v){
            super(v);
            this.imagenTecnologia = (ImageView) v.findViewById(R.id.foto_tecnologia);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public DesarrolladorAdapter(List<TecnologiaRel> items){
        this.items = items;
    }

    @NonNull
    @Override
    public DesarrolladorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_tecnologia_card, viewGroup, false);
        return new DesarrolladorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DesarrolladorViewHolder viewHolder, int i) {
        TecnologiaRel tecnologiaRel = items.get(i);
        viewHolder.setDesarrollador_tecnologia(tecnologiaRel);

        Picasso.get().load(tecnologiaRel.getImagen()).into(viewHolder.imagenTecnologia);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
