package com.example.teleappsistencia.ui.fragments.acercaDe;

import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Desarrollador;
import com.example.teleappsistencia.modelos.Desarrollador_tecnologia;
import com.example.teleappsistencia.modelos.Tecnologia;
import com.example.teleappsistencia.ui.fragments.alarma.AlarmaAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DesarrolladorAdapter extends RecyclerView.Adapter<DesarrolladorAdapter.DesarrolladorViewHolder>{

    private List<Desarrollador> lDesarrolladores;

    private DesarrolladorViewHolder desarrolladorViewHolder;

    private Desarrollador desarrolladorSeleccionado;

    private AlarmaAdapter.OnItemSelectedListener listener;

    private int selectedPosition = RecyclerView.NO_POSITION;

    private TecnologiaAdapter tecnologiaAdapter;

    private Context context;

    public DesarrolladorAdapter(Context context, List<Desarrollador> lDesarrolladores) {
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

        private Context contextV;

        private ImageView imagen_perfil;

        private TextView text_nombre;

        private RecyclerView recyclerView_tecnologias;

        private Desarrollador desarrollador;

        public DesarrolladorViewHolder(View v){
            super(v);
            contextV = v.getContext();
            imagen_perfil = (ImageView) v.findViewById(R.id.imagenPerfilDesarrolladorCard);
            text_nombre = (TextView) v.findViewById(R.id.textViewNombreDesarrolladorCard);
            recyclerView_tecnologias = (RecyclerView) v.findViewById(R.id.recyclerView_tecnologias);
        }

        public Desarrollador getDesarrollador() {
            return desarrollador;
        }

        public void setDesarrollador(Desarrollador desarrollador) {
            this.desarrollador = desarrollador;
        }

        public void setOnClickListeners() {

        }

        public void consultarDesarrollador(Desarrollador desarrolladorSeleccionado){
            AppCompatActivity activity = (AppCompatActivity) contextV;
            Bundle agrs = new Bundle();
            agrs.putSerializable(Constantes.ARG_DESARROLLADOR,desarrolladorSeleccionado);
            ConsultarDesarrolladorFragment consultarDesarrolladorFragment = new ConsultarDesarrolladorFragment();
            consultarDesarrolladorFragment.setArguments(agrs);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, consultarDesarrolladorFragment)
                    .addToBackStack(null)
                    .commit();
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
        viewHolder.setDesarrollador(desarrollador);

        viewHolder.text_nombre.setText(desarrollador.getNombre());
        Picasso.get().load(desarrollador.getImagen()).into(viewHolder.imagen_perfil);

        //TODO Trabajar recycle tecnologia
        List<Desarrollador_tecnologia> lDesarrollador_tecnologia = (ArrayList<Desarrollador_tecnologia>) Utilidad.getObjeto(desarrollador.getlDesarrollador_tecnologia(), Constantes.AL_DESARROLLADOR_TECNOLOGIA);
        List<Tecnologia> lTecnologias = new ArrayList<>();

        for (int j = 0; j < lDesarrollador_tecnologia.size(); j++) {
           Tecnologia tecnologiaAux = (Tecnologia) Utilidad.getObjeto(lDesarrollador_tecnologia.get(j).getId_tecnologia(), Constantes.TECNOLOGIA);
            lTecnologias.add(tecnologiaAux);
        }

        tecnologiaAdapter = new TecnologiaAdapter(context, lTecnologias);
        viewHolder.recyclerView_tecnologias.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView_tecnologias.setAdapter(tecnologiaAdapter);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar la posición seleccionada y notificar al adapter
                int previousSelectedPosition = selectedPosition;
                selectedPosition = viewHolder.getAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);

                // Llamar al método onItemSelected de OnItemSelectedListener
                if (listener != null) {
                    listener.onItemSelected(selectedPosition);
                }
                desarrolladorSeleccionado= viewHolder.desarrollador;

                viewHolder.consultarDesarrollador(desarrolladorSeleccionado);
            }
        });
    }

    public void updateTecnologiasList(List<Tecnologia> Tecnologias) {
        if (tecnologiaAdapter != null) {
            tecnologiaAdapter.updateTecnologiasList(Tecnologias);
        }
    }



}
