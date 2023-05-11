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
import com.example.teleappsistencia.modelos.Desarrollador;
import com.example.teleappsistencia.modelos.Tecnologia;
import com.example.teleappsistencia.ui.fragments.alarma.AlarmaAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TecnologiaAdapter extends RecyclerView.Adapter<TecnologiaAdapter.TecnologiaViewHolder>{

    private List<Tecnologia> lTecnologias;

    private TecnologiaViewHolder tecnologiaViewHolder;

    private Context context;

    public TecnologiaAdapter(Context context, List<Tecnologia> lTecnologias){
        this.context = context;
        this.lTecnologias = lTecnologias;
    }
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public Tecnologia getItemAtPosition(int position) {
        return lTecnologias.get(position);
    }

    public static class TecnologiaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;

        private ImageView imagen_tecnologia;

        public TecnologiaViewHolder(View v){
            super(v);
            imagen_tecnologia = (ImageView) v.findViewById(R.id.imagen_tecnologia);
        }

        public void setOnClickListeners(){

        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public int getItemCount() {
        return lTecnologias.size();
    }

    @Override
    public TecnologiaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_card_tecnologia, viewGroup, false);
        return new TecnologiaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TecnologiaViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        Tecnologia tecnologia = lTecnologias.get(i);

        Picasso.get().load(tecnologia.getImagen()).into(viewHolder.imagen_tecnologia);
    }

    public void updateTecnologiasList(List<Tecnologia> tecnologiasList) {
        this.lTecnologias = tecnologiasList;
        notifyDataSetChanged(); // Notifica al adaptador de que la lista ha sido actualizada
    }





}
