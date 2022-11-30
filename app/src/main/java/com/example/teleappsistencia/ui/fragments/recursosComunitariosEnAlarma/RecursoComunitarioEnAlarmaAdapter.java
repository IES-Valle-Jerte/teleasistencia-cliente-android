package com.example.teleappsistencia.ui.fragments.recursosComunitariosEnAlarma;

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
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.RecursoComunitarioEnAlarma;
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


public class RecursoComunitarioEnAlarmaAdapter extends RecyclerView.Adapter<RecursoComunitarioEnAlarmaAdapter.RecursoComunitarioEnAlarmaViewHolder> {
    private List<RecursoComunitarioEnAlarma> items;

    public static class RecursoComunitarioEnAlarmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        private Context context;
        private TextView txtCardIdRecursoComunitarioEnAlarma;
        private TextView txtCardFechaRecursoComunitarioEnAlarma;
        private TextView txtCardPersonaRecursoComunitarioEnAlarma;
        private TextView txtCardAlarmaRecursoComunitarioEnAlarma;
        private TextView txtCardNombreRecursoComunitarioEnAlarma;
        private ImageButton imageButtonVerRecursoComunitarioEnAlarma;
        private ImageButton imageButtonModificarRecursoComunitarioEnAlarma;
        private ImageButton imageButtonBorrarRecursoComunitarioEnAlarma;
        private RecursoComunitarioEnAlarma recursoComunitarioEnAlarma;

        public RecursoComunitarioEnAlarmaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            // Capturamos los elementos del layout
            this.txtCardIdRecursoComunitarioEnAlarma = (TextView) v.findViewById(R.id.txtCardIdRecursoComunitarioEnAlarma);
            this.txtCardFechaRecursoComunitarioEnAlarma = (TextView) v.findViewById(R.id.txtCardFechaRecursoComunitarioEnAlarma);
            this.txtCardPersonaRecursoComunitarioEnAlarma = (TextView) v.findViewById(R.id.txtCardPersonaRecursoComunitarioEnAlarma);
            this.txtCardAlarmaRecursoComunitarioEnAlarma = (TextView) v.findViewById(R.id.txtCardAlarmaRecursoComunitarioEnAlarma);
            this.txtCardNombreRecursoComunitarioEnAlarma = (TextView) v.findViewById(R.id.txtCardNombreRecursoComunitarioEnAlarma);
            this.imageButtonVerRecursoComunitarioEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonVerRecursoComunitarioEnAlarma);
            this.imageButtonModificarRecursoComunitarioEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonModificarRecursoComunitarioEnAlarma);
            this.imageButtonBorrarRecursoComunitarioEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonBorrarRecursoComunitarioEnAlarma);
        }
        //Asignamos listeners
        public void setOnClickListeners() {
            this.imageButtonVerRecursoComunitarioEnAlarma.setOnClickListener(this);
            this.imageButtonModificarRecursoComunitarioEnAlarma.setOnClickListener(this);
            this.imageButtonBorrarRecursoComunitarioEnAlarma.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity) context;
            switch (view.getId()) {
                case R.id.imageButtonVerRecursoComunitarioEnAlarma:
                    ConsultarRecursoComunitarioEnAlarmaFragment cRCEA =  ConsultarRecursoComunitarioEnAlarmaFragment.newInstance(this.recursoComunitarioEnAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, cRCEA)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonModificarRecursoComunitarioEnAlarma:
                    ModificarRecursoComunitarioEnAlarmaFragment mRCEA = ModificarRecursoComunitarioEnAlarmaFragment.newInstance(this.recursoComunitarioEnAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, mRCEA)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonBorrarRecursoComunitarioEnAlarma:
                    borrarRecursoComunitarioEnAlarma();
                    break;
            }
        }
        // Setter para poder pasarle el atributo desde del Adapter
        public void setRecursoComunitarioEnAlarma(RecursoComunitarioEnAlarma recursoComunitarioEnAlarma){
            this.recursoComunitarioEnAlarma = recursoComunitarioEnAlarma;
        }

        /**
         * Método que lanza la petición DELETE a la API REST para borrar el recurso comunitario en alarma
         */
        private void borrarRecursoComunitarioEnAlarma(){
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deleteRecursoComunitarioEnAlarmabyId(this.recursoComunitarioEnAlarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, Constantes.RECURSO_EN_ALARMA_BORRADO, Toast.LENGTH_LONG).show();
                        volver();
                    }else{
                        Toast.makeText(context, Constantes.ERROR_BORRADO + response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, Constantes.ERROR_BORRADO, Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * Este método vuelve a cargar el fragment con el listado.
         */
        private void volver(){
            MainActivity activity = (MainActivity) context;
            ListarRecursosComunitariosEnAlarmaFragment lRCEA = new ListarRecursosComunitariosEnAlarmaFragment();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, lRCEA)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Se le carga la lista de items al Adapter, en este caso de Recursos Comunitarios en Alarma
     * @param items
     */
    public RecursoComunitarioEnAlarmaAdapter(List<RecursoComunitarioEnAlarma> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecursoComunitarioEnAlarmaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_recursos_comunitariosen_alarma_card, viewGroup, false);
        return new RecursoComunitarioEnAlarmaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecursoComunitarioEnAlarmaViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        // En el bind, le cargamos los atributos al layout de la tarjeta
        RecursoComunitarioEnAlarma recursoComunitarioEnAlarma = items.get(i);
        viewHolder.setRecursoComunitarioEnAlarma(recursoComunitarioEnAlarma);

        Alarma alarma = (Alarma) Utilidad.getObjeto(recursoComunitarioEnAlarma.getIdAlarma(), Constantes.ALARMA);
        RecursoComunitario recursoComunitario = (RecursoComunitario) Utilidad.getObjeto(recursoComunitarioEnAlarma.getIdRecursoComunitairo(), Constantes.RECURSO_COMUNITARIO);

        viewHolder.txtCardIdRecursoComunitarioEnAlarma.setText(Constantes.ID_DP_SP + String.valueOf(recursoComunitarioEnAlarma.getId()));
        viewHolder.txtCardFechaRecursoComunitarioEnAlarma.setText(Constantes.FECHA_DP_SP + recursoComunitarioEnAlarma.getFechaRegistro());
        viewHolder.txtCardPersonaRecursoComunitarioEnAlarma.setText(Constantes.PERSONA_DP_SP + recursoComunitarioEnAlarma.getPersona());
        viewHolder.txtCardAlarmaRecursoComunitarioEnAlarma.setText(Constantes.ID_ALARMA_DP_SP + String.valueOf(alarma.getId()));
        viewHolder.txtCardNombreRecursoComunitarioEnAlarma.setText(Constantes.RECURSO_COMUNITARIO_DP_SP + recursoComunitario.getNombre());

    }
}
