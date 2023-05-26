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
import com.example.teleappsistencia.modelos.ClasificacionAlarma;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.xml.validation.Validator;

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
        private TextView txtCardTipoAlarma;
        private TextView txtCardHoraRegistroAlarma;
        private TextView txtCardTeleoperadorAlarma;
        private TextView txtCardUsuarioServicioAlarma;
        private TextView txtCardTerminalAlarma;
        private Alarma alarma;

        public AlarmaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            // Capturamos los elementos del layout
            this.txtCardTerminalAlarma = (TextView) v.findViewById(R.id.txtCardTerminal);
            this.txtCardHoraRegistroAlarma = (TextView) v.findViewById(R.id.txtCardHoraRegistroAlarma);
            this.txtCardTeleoperadorAlarma = (TextView) v.findViewById(R.id.txtCardTeleoperador);
            this.txtCardUsuarioServicioAlarma = (TextView) v.findViewById(R.id.txtCardUsuarioDelServicio);
            this.txtCardTipoAlarma = (TextView) v.findViewById(R.id.txtCardTipoAlarma);
        }
        //Asignamos listeners
        public void setOnClickListeners() {
        }

        /**
         * Recogemos los onClickListeners de los imageButtons en el RecyclerView
         *
         * @param view La vista en la que se hizo clic.
         */
        @Override
        public void onClick(View view) {

        }

        // Setter para poder pasarle la alarma desde del Adapter
        public void setAlarma(Alarma alarma){
            this.alarma = alarma;
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
        ClasificacionAlarma clasificacionAlarma = (ClasificacionAlarma) Utilidad.getObjeto(tipo.getClasificacionAlarma(), Constantes.CLASIFICACION_ALARMA);
        Teleoperador teleoperador = (Teleoperador) (Utilidad.getObjeto(alarma.getId_teleoperador(), Constantes.TELEOPERADOR));
        Terminal terminal = (Terminal) (Utilidad.getObjeto(alarma.getId_terminal(), Constantes.TERMINAL));
        Paciente paciente = (Paciente) (Utilidad.getObjeto(alarma.getId_paciente_ucr(), Constantes.PACIENTE));

        if(paciente != null) {
            viewHolder.txtCardUsuarioServicioAlarma.setText(Constantes.USUARIO_SERVICIO_DP_SP + paciente);
        } else {
            viewHolder.txtCardUsuarioServicioAlarma.setText(Constantes.USUARIO_SERVICIO_DP_SP + Constantes.ESPACIO_EN_BLANCO);
        }

        if(teleoperador != null){
            viewHolder.txtCardTeleoperadorAlarma.setText(Constantes.TELEOPERADOR_DP_SP + teleoperador.getFirstName() + Constantes.ESPACIO_EN_BLANCO + teleoperador.getLastName());
        } else {
            viewHolder.txtCardTeleoperadorAlarma.setText(Constantes.TELEOPERADOR_DP_SP + Constantes.ESPACIO_EN_BLANCO);
        }

        if(terminal != null){
            viewHolder.txtCardTerminalAlarma.setText(Constantes.TERMINAL_DP_SP + terminal.getNumeroTerminal());
        } else {
            viewHolder.txtCardTerminalAlarma.setText(Constantes.TERMINAL_DP_SP + Constantes.ESPACIO_EN_BLANCO);
        }


        ColorDrawable colorFondo = new ColorDrawable(Color.argb((int)(0.08*255), 255, 0, 0));
        if(alarma.getEstado_alarma().toString().equalsIgnoreCase("Abierta")){ // mostrar abiertas en rojo y cerradas en verde
            viewHolder.itemView.setBackground(new ColorDrawable(Color.argb((int)(0.08*255), 255, 0, 0)));
        }else{
            viewHolder.itemView.setBackground(new ColorDrawable(Color.argb((int)(0.08*255), 64, 255, 0)));
        }


        if(alarma.getFecha_registro() != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(alarma.getFecha_registro());

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String formattedTime = sdf.format(alarma.getFecha_registro());

            viewHolder.txtCardHoraRegistroAlarma.setText(Constantes.HORA_DP_SP + formattedTime + Constantes.H);
        }



        if(tipo != null){ // Control de error si el nombre es nulo.
            viewHolder.txtCardTipoAlarma.setText(Constantes.TIPO_DP_SP + clasificacionAlarma.getNombre() + Constantes.ESPACIO_GUION_ESPACIO+ tipo.getNombre());
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
        }
    }


}
