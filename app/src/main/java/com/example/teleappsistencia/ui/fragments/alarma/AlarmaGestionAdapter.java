package com.example.teleappsistencia.ui.fragments.alarma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.gestionAlarmasFragments.InfoGestionAlarmaFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Este adapter será el que carge las tarjetas en la lista. Las tarjetas serán sobre las alarmas que
 * le pasemos en el adapter. Se usa para dos casos, ListarMisAlarmasFragment, donde se listan todas
 * las alarmas que ha gestionado el usuario, y para AlarmasSinAsignarFragment, donde se listan todas
 * las alarmas que no tienen teleoperador asignado.
 */
public class AlarmaGestionAdapter extends RecyclerView.Adapter<AlarmaGestionAdapter.AlarmaViewHolder> {
    private List<Alarma> items;

    public static class AlarmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        private Context context;
        private TextView idAlarma;
        private TextView txtCardEstadoAlarma;
        private TextView txtCardFechaRegistroAlarma;
        private TextView txtCardTipoAlarma;
        private ImageButton imageButtonGestionAlarma;
        private ImageButton imageButtonVerAlarma;
        private Alarma alarma;
        private MainActivity activity;

        public AlarmaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.activity = (MainActivity) this.context;

            // Capturamos los elementos del layout
            this.idAlarma = (TextView) v.findViewById(R.id.txtCardIdAlarma);
            this.txtCardEstadoAlarma = (TextView) v.findViewById(R.id.txtCardEstadoAlarma);
            this.txtCardFechaRegistroAlarma = (TextView) v.findViewById(R.id.txtCardFechaRegistroAlarma);
            this.txtCardTipoAlarma = (TextView) v.findViewById(R.id.txtCardTipoAlarma);
            this.imageButtonGestionAlarma = (ImageButton) v.findViewById(R.id.imageButtonGestionarAlarma);
            this.imageButtonVerAlarma = (ImageButton) v.findViewById(R.id.imageButtonVerGestionarAlarma);
        }

        //Asignamos listeners
        public void setOnClickListeners() {
            this.imageButtonGestionAlarma.setOnClickListener(this);
            this.imageButtonVerAlarma.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonGestionarAlarma:
                    /* Si la alarma ya está cerrada, no podremos volver a gestionarla */
                    if(alarma.getEstado_alarma().equals(Constantes.ABIERTA)) {
                        comprobarAlarma();
                    }
                    else{
                        Toast.makeText(context, Constantes.ALARMA_YA_CERRADA, Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.imageButtonVerGestionarAlarma:
                    ConsultarAlarmaFragment consultarAlarmaFragment = ConsultarAlarmaFragment.newInstance(this.alarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, consultarAlarmaFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
            }
        }

        // Setter para poder pasarle la alarma desde del Adapter
        public void setAlarma(Alarma alarma){
            this.alarma = alarma;
        }

        /**
         * Este método llama a la API REST para traer la Alarma. Cuando el usuario haga click en el botón
         * con el Icono de Teleoperador, se le va a asignar la alarma (cargando su ID de teleoperador en ella) y se va a persistir
         * en la base de datos. Pero previamente HAY QUE COMPROBAR QUE NIGÚN OTRO TELEOPERADOR FUE MÁS
         * RÁPIDO Y SE ASIGNÓ LA ALARMA ANTES. Aquí se hace esta comprobación.
         */
        private void comprobarAlarma(){
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<Alarma> callAlarma = apiService.getAlarmabyId(this.alarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            callAlarma.enqueue(new Callback<Alarma>() {
                @Override
                public void onResponse(Call<Alarma> callAlarma, Response<Alarma> response) {
                    if(response.isSuccessful()){
                        // Traemos la alarma recibida y sacamos el teleoperador
                        Alarma alarmaRecibida = response.body();
                        // Determinamos qué acción será la siguiente
                        determinarAccion(alarmaRecibida);
                    } else{
                        Toast.makeText(context, Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<Alarma> callAlarma, Throwable t) {
                    Toast.makeText(context, Constantes.ERROR_ + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * En este método se determina qué acción realizar: si el teleoperador de la alarma sigue siendo nulo
         * quiere decir que todavía no se ha asignado a nadie, y que podemos modificarla y ponerle nuestro id.
         * Puede darse el caso de que nuestro teleoperador ya tuviese asignada la alarma pero por cualquier problema
         * no la hubiese cerrado. Se controla la opción de que el teleoperador de la alarma sea él mismo y pueda
         * continuar con la gestión.
         * Si el teleoperador es otro, se muestra un mensaje y no se hace nada más.
         * @param alarmaRecibida
         */
        private void determinarAccion(Alarma alarmaRecibida){
            Teleoperador teleoperador = (Teleoperador) Utilidad.getObjeto(alarmaRecibida.getId_teleoperador(), Constantes.TELEOPERADOR);
            // Si el teleoperador es nulo, podemos continuar con la modificación (donde se le asignará el ID de Teleoperador)
            if(teleoperador == null){
                modificarAlarma(alarmaRecibida);
            }else{
                /* Si el teleoperador no es nulo, comprobamos si es nuestra alarma, para continuar directamente con la gestión.
                 * Esto se hace por si el Teleoperador ha teniddo algún problema y no cerró la alarma la primera vez. */
                if(teleoperador.getId() == Utilidad.getUserLogged().getPk()){
                    InfoGestionAlarmaFragment iGAF = InfoGestionAlarmaFragment.newInstance(alarmaRecibida);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, iGAF)
                            .addToBackStack(null)
                            .commit();
                }else{
                    Toast.makeText(context, Constantes.ERROR_ALARMA_YA_ASIGNADA, Toast.LENGTH_LONG).show();
                }
            }
        }

        /**
         * Finalmente, este método realiza la petición PUT a la API REST para modificar la alarma. La modificación
         * consiste en ponerle el id_teleoperador que corresponda al usuario de la aplicación.
         * @param alarmaRecibida
         */
        private void modificarAlarma(Alarma alarmaRecibida){
            /* Siempre que hagamos un PUT tenemos que darle a la petición los datatos de la forma
                 que requiere. En este caso, idTeleoperador SIEMPRE tiene que ser un intger. */
            alarmaRecibida.setId_teleoperador(Utilidad.getUserLogged().getPk());
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.actualizarAlarma(alarmaRecibida.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), alarmaRecibida);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        InfoGestionAlarmaFragment iGAF = InfoGestionAlarmaFragment.newInstance(alarmaRecibida);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, iGAF)
                                .addToBackStack(null)
                                .commit();
                    }
                    else{
                        Toast.makeText(activity, Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(activity, Constantes.ERROR_CARGAR_DATOS, Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    /**
     * Se le carga la lista de items al Adapter, en este caso de Alarmas
     * @param items
     */
    public AlarmaGestionAdapter(List<Alarma> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AlarmaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_alarma_gestion_card, viewGroup, false);
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
        viewHolder.txtCardFechaRegistroAlarma.setText(Constantes.FECHA_DP_SP + alarma.getFecha_registro());
        viewHolder.txtCardTipoAlarma.setText(Constantes.TIPO_DP_SP + tipo.getNombre());
    }
}
