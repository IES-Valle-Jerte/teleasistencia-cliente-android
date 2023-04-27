package com.example.teleappsistencia.ui.fragments.alarma;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmaAdapter extends RecyclerView.Adapter<AlarmaAdapter.AlarmaViewHolder> {
    private List<Alarma> items;

    private AlarmaViewHolder alarmaViewHolder;

    private Alarma alarmaSeleccionada;

    private OnItemSelectedListener listener;

    private int selectedPosition = RecyclerView.NO_POSITION;

    /**
     * Una clase {@link RecyclerView.ViewHolder} para recoger los datos a mostrar dentro de la tarjeta Paciente del RecyclerView.
     * <p> Esta clase es una subclase de {@link RecyclerView.ViewHolder} y hereda de ella todos sus métodos y atributos.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    //devuelve la alarma seleccionada
    public Alarma getAlarmaSeleccionada() {
        return alarmaSeleccionada;
    }
    // Define el método getItemAtPosition para obtener el elemento en la posición indicada
    public Alarma getItemAtPosition(int position) {
        return items.get(position);
    }

    public static class AlarmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        private Context context;
        private TextView idAlarma;
        private TextView txtCardEstadoAlarma;
        private TextView txtCardFechaRegistroAlarma;
        private TextView txtCardTipoAlarma;
        private ImageButton imageButtonVerAlarma;
        private ImageButton imageButtonModificarAlarma;
        private ImageButton imageButtonBorrarAlarma;
        private Alarma alarma;

        public AlarmaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            // Capturamos los elementos del layout
            this.idAlarma = (TextView) v.findViewById(R.id.txtCardIdAlarma);
            this.txtCardEstadoAlarma = (TextView) v.findViewById(R.id.txtCardEstadoAlarma);
            this.txtCardFechaRegistroAlarma = (TextView) v.findViewById(R.id.txtCardFechaRegistroAlarma);
            this.txtCardTipoAlarma = (TextView) v.findViewById(R.id.txtCardTipoAlarma);
            this.imageButtonVerAlarma = (ImageButton) v.findViewById(R.id.imageButtonVerAlarma);
            this.imageButtonModificarAlarma = (ImageButton) v.findViewById(R.id.imageButtonModificarAlarma);
            this.imageButtonBorrarAlarma = (ImageButton) v.findViewById(R.id.imageButtonBorrarAlarma);
        }
        //Asignamos listeners
        public void setOnClickListeners() {
            this.imageButtonVerAlarma.setOnClickListener(this);
            this.imageButtonModificarAlarma.setOnClickListener(this);
            this.imageButtonBorrarAlarma.setOnClickListener(this);
        }

        /**
         * Recogemos los onClickListeners de los imageButtons en el RecyclerView
         *
         * @param view La vista en la que se hizo clic.
         */
        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            switch (view.getId()) {
                // Modificar alarma.
                case R.id.imageButtonModificarAlarma:
                    ModificarAlarmaFragment modificarAlarmaFragment = ModificarAlarmaFragment.newInstance(this.alarma);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, modificarAlarmaFragment).addToBackStack(null).commit();
                    break;
                // Ver Alarma.
                case R.id.imageButtonVerAlarma:
                    ConsultarAlarmaFragment consultarAlarmaFragment = ConsultarAlarmaFragment.newInstance(this.alarma);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarAlarmaFragment).addToBackStack(null).commit();
                    break;
                // Borrar Alarma.
                case R.id.imageButtonBorrarAlarma:
                    borrarAlarma();
                    break;
            }
        }

        // Setter para poder pasarle la alarma desde del Adapter
        public void setAlarma(Alarma alarma){
            this.alarma = alarma;
        }

        /**
         * Método que lanza la petición DELETE a la API REST para borrar la alarma
         */
        private void borrarAlarma(){
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deleteAlarmabyId(this.alarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, Constantes.ALARMA_BORRADA, Toast.LENGTH_LONG).show();
                        volver();
                    }else{
                        Toast.makeText(context, Constantes.ERROR_BORRADO + response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * Este método vuelve a cargar el fragment con el listado.
         */
        private void volver(){
            MainActivity activity = (MainActivity) this.context;
            ListarAlarmasOrdenadasFragment listarAlarmasOrdenadasFragment = new ListarAlarmasOrdenadasFragment();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, listarAlarmasOrdenadasFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }



    /**
     * Se le carga la lista de items al Adapter, en este caso de Alarmas
     * @param items
     */
    public AlarmaAdapter(List<Alarma> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AlarmaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_alarma_card, viewGroup, false);
        return new AlarmaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlarmaViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        // En el bind, le cargamos los atributos al layout de la tarjeta
        Alarma alarma = items.get(i);
        viewHolder.setAlarma(alarma);
        TipoAlarma tipo = (TipoAlarma) Utilidad.getObjeto(alarma.getId_tipo_alarma(), Constantes.TIPOALARMA);
        viewHolder.idAlarma.setText(Constantes.ID_ALARMA_DP_SP + String.valueOf(alarma.getId()));
        viewHolder.txtCardEstadoAlarma.setText(Constantes.ESTADO_DP_SP + alarma.getEstado_alarma());

        ColorDrawable colorFondo = new ColorDrawable(Color.argb((int)(0.08*255), 255, 0, 0));
        if(alarma.getEstado_alarma().toString().equalsIgnoreCase("Abierta")){ // mostrar abiertas en rojo y cerradas en verde
            viewHolder.itemView.setBackground(new ColorDrawable(Color.argb((int)(0.08*255), 255, 0, 0)));
        }else{
            viewHolder.itemView.setBackground(new ColorDrawable(Color.argb((int)(0.08*255), 64, 255, 0)));
        }
        viewHolder.txtCardFechaRegistroAlarma.setText(Constantes.FECHA_DP_SP + alarma.getFecha_registro());
        if(tipo != null){ // Control de error si el nombre es nulo.
            viewHolder.txtCardTipoAlarma.setText(Constantes.TIPO_DP_SP + tipo.getNombre());
        }else{
            viewHolder.txtCardTipoAlarma.setText(Constantes.TIPO_DP_SP + Constantes.ESPACIO_EN_BLANCO);
        }

        // Establece un click listener para ViewHolder
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
                alarmaSeleccionada= viewHolder.alarma;
            }
        });

        // Establecer el color de fondo del ViewHolder en función de si está seleccionado o no
        if (selectedPosition == i) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.azul));
        }/* else {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), android.R.color.white));
        }*/

    }


}
