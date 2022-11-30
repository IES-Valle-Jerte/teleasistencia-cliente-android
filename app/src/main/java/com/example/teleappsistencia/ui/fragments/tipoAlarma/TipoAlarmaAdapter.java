package com.example.teleappsistencia.ui.fragments.tipoAlarma;

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
import com.example.teleappsistencia.modelos.ClasificacionAlarma;
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

public class TipoAlarmaAdapter extends RecyclerView.Adapter<TipoAlarmaAdapter.TipoAlarmaViewHolder> {
    private List<TipoAlarma> items;

    public static class TipoAlarmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        private Context context;
        private TextView txtCardIdTipoAlarma;
        private TextView txtCardNombreTipoAlarma;
        private TextView txtCardCodigoTipoAlarma;
        private TextView txtCardEsDispositivoTipoAlarma;
        private TextView txtCardClasificacionTipoAlarma;
        private ImageButton imageButtonVerTipoAlarma;
        private ImageButton imageButtonModificarTipoAlarma;
        private ImageButton imageButtonBorrarTipoAlarma;
        private TipoAlarma tipoAlarma;

        public TipoAlarmaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            // Capturamos los elementos del layout
            this.txtCardIdTipoAlarma = (TextView) v.findViewById(R.id.txtCardIdTipoAlarma);
            this.txtCardNombreTipoAlarma = (TextView) v.findViewById(R.id.txtCardNombreTipoAlarma);
            this.txtCardCodigoTipoAlarma = (TextView) v.findViewById(R.id.txtCardCodigoTipoAlarma);
            this.txtCardEsDispositivoTipoAlarma = (TextView) v.findViewById(R.id.txtCardEsDispositivoTipoAlarma);
            this.txtCardClasificacionTipoAlarma = (TextView) v.findViewById(R.id.txtCardClasificacionTipoAlarma);
            this.imageButtonVerTipoAlarma = (ImageButton) v.findViewById(R.id.imageButtonVerTipoAlarma);
            this.imageButtonModificarTipoAlarma = (ImageButton) v.findViewById(R.id.imageButtonModificarTipoAlarma);
            this.imageButtonBorrarTipoAlarma = (ImageButton) v.findViewById(R.id.imageButtonBorrarTipoAlarma);
        }
        //Asignamos listeners
        public void setOnClickListeners() {
            this.imageButtonVerTipoAlarma.setOnClickListener(this);
            this.imageButtonModificarTipoAlarma.setOnClickListener(this);
            this.imageButtonBorrarTipoAlarma.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity) context;
            switch (view.getId()) {
                case R.id.imageButtonVerTipoAlarma:
                    ConsultarTipoAlarmaFragment consultarTipoAlarmaFragment = ConsultarTipoAlarmaFragment.newInstance(this.tipoAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, consultarTipoAlarmaFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonModificarTipoAlarma:
                    ModificarTipoAlarmaFragment modificarTipoAlarmaFragment = ModificarTipoAlarmaFragment.newInstance(this.tipoAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, modificarTipoAlarmaFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonBorrarTipoAlarma:
                    borrarTipoAlarma();
                    break;
            }
        }
        // Setter para poder pasarle el atributo desde del Adapter
        public void setTipoAlarma(TipoAlarma tipoAlarma){
            this.tipoAlarma = tipoAlarma;
        }

        /**
         * Método que lanza la petición DELETE a la API REST para borrar el Tipo de Alarma
         */
        private void borrarTipoAlarma(){
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deleteTipoAlarmabyId(this.tipoAlarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, Constantes.TIPO_ALARMA_BORRADO, Toast.LENGTH_LONG).show();
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
            ListarTipoAlarmaFragment listarTipoAlarmaFragment = new ListarTipoAlarmaFragment();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, listarTipoAlarmaFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Se le carga la lista de items al Adapter, en este caso, lista de Tipos de Alarma
     * @param items
     */
    public TipoAlarmaAdapter(List<TipoAlarma> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TipoAlarmaAdapter.TipoAlarmaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_tipo_alarma_card, viewGroup, false);
        return new TipoAlarmaAdapter.TipoAlarmaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TipoAlarmaAdapter.TipoAlarmaViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        // En el bind, le cargamos los atributos al layout de la tarjeta
        TipoAlarma tipoAlarma = items.get(i);
        viewHolder.setTipoAlarma(tipoAlarma);

        ClasificacionAlarma clasificacionAlarma = (ClasificacionAlarma) Utilidad.getObjeto(tipoAlarma.getClasificacionAlarma(), Constantes.CLASIFICACION_ALARMA);

        viewHolder.txtCardIdTipoAlarma.setText(Constantes.ID_DP_SP+String.valueOf(tipoAlarma.getId()));
        viewHolder.txtCardNombreTipoAlarma.setText(Constantes.NOMBRE_DP_SP +tipoAlarma.getNombre());
        viewHolder.txtCardCodigoTipoAlarma.setText(Constantes.CODIGO_DP_SP+tipoAlarma.getCodigo());
        viewHolder.txtCardEsDispositivoTipoAlarma.setText(Constantes.DISPOSITIVO_DP_SP + Utilidad.trueSifalseNo(tipoAlarma.isEsDispositivo()));
        viewHolder.txtCardClasificacionTipoAlarma.setText(Constantes.CLASIFICACION_DP_SP + clasificacionAlarma.getNombre());
    }
}
