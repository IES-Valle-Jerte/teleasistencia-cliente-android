package com.example.teleappsistencia.ui.fragments.centroSanitarioEnAlarma;

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
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.CentroSanitarioEnAlarma;
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

public class CentroSanitarioEnAlarmaAdapter extends RecyclerView.Adapter<CentroSanitarioEnAlarmaAdapter.CentroEnAlarmaViewHolder> {
    private List<CentroSanitarioEnAlarma> items;

    public static class CentroEnAlarmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        private Context context;
        private TextView txtCardIdCentroSanitarioEnAlarma;
        private TextView txtCardFechaCentroEnAlarma;
        private TextView txtCardPersonaCentroEnAlarma;
        private TextView txtCardAlarmaCentroEnAlarma;
        private TextView txtCardNombreCentroEnAlarma;
        private ImageButton imageButtonVerCentroSanitarioEnAlarma;
        private ImageButton imageButtonModificarCentroSanitarioEnAlarma;
        private ImageButton imageButtonBorrarCentroSanitarioEnAlarma;
        private CentroSanitarioEnAlarma centroSanitarioEnAlarma;

        public CentroEnAlarmaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            // Capturamos los elementos del layout
            this.txtCardIdCentroSanitarioEnAlarma = (TextView) v.findViewById(R.id.txtCardIdCentroSanitarioEnAlarma);
            this.txtCardFechaCentroEnAlarma = (TextView) v.findViewById(R.id.txtCardFechaCentroEnAlarma);
            this.txtCardPersonaCentroEnAlarma = (TextView) v.findViewById(R.id.txtCardPersonaCentroEnAlarma);
            this.txtCardAlarmaCentroEnAlarma = (TextView) v.findViewById(R.id.txtCardAlarmaCentroEnAlarma);
            this.txtCardNombreCentroEnAlarma = (TextView) v.findViewById(R.id.txtCardNombreCentroEnAlarma);
            this.imageButtonVerCentroSanitarioEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonVerCentroSanitarioEnAlarma);
            this.imageButtonModificarCentroSanitarioEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonModificarCentroSanitarioEnAlarma);
            this.imageButtonBorrarCentroSanitarioEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonBorrarCentroSanitarioEnAlarma);
        }
        //Asignamos listeners
        public void setOnClickListeners() {
            this.imageButtonVerCentroSanitarioEnAlarma.setOnClickListener(this);
            this.imageButtonModificarCentroSanitarioEnAlarma.setOnClickListener(this);
            this.imageButtonBorrarCentroSanitarioEnAlarma.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity) context;
            switch (view.getId()) {
                case R.id.imageButtonVerCentroSanitarioEnAlarma:
                    ConsultarCentroSanitarioEnAlarmaFragment cCSEA = ConsultarCentroSanitarioEnAlarmaFragment.newInstance(this.centroSanitarioEnAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, cCSEA)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonModificarCentroSanitarioEnAlarma:
                    ModificarCentroSanitarioEnAlarmaFragment mCSEA = ModificarCentroSanitarioEnAlarmaFragment.newInstance(this.centroSanitarioEnAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, mCSEA)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonBorrarCentroSanitarioEnAlarma:
                    borrarCentroSanitarioEnAlarma();
                    break;
            }
        }

        // Setter para poder pasarle el atributo desde del Adapter
        public void setCentroSanitarioEnAlarma(CentroSanitarioEnAlarma centroSanitarioEnAlarma){
            this.centroSanitarioEnAlarma = centroSanitarioEnAlarma;
        }

        /**
         * Método que lanza la petición DELETE a la API REST para borrar el Centro Sanitario en Alarma
         */
        private void borrarCentroSanitarioEnAlarma(){
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deleteCentroSanitarioEnAlarmabyId(this.centroSanitarioEnAlarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, Constantes.CENTRO_EN_ALARMA_BORRADO, Toast.LENGTH_LONG).show();
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
            MainActivity activity = (MainActivity) context;
            ListarCentrosSanitariosEnAlarmaFragment lCSEA = new ListarCentrosSanitariosEnAlarmaFragment();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, lCSEA)
                    .addToBackStack(null)
                    .commit();
        }

    }

    /**
     * Se le carga la lista de items al Adapter, en este caso Centros Sanitarios en Alarma
     * @param items
     */
    public CentroSanitarioEnAlarmaAdapter(List<CentroSanitarioEnAlarma> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CentroEnAlarmaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_centro_sanitario_en_alarma_card, viewGroup, false);
        return new CentroEnAlarmaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CentroEnAlarmaViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        // En el bind, le cargamos los atributos al layout de la tarjeta
        CentroSanitarioEnAlarma centroSanitarioEnAlarma = items.get(i);
        viewHolder.setCentroSanitarioEnAlarma(centroSanitarioEnAlarma);

        Alarma alarma = (Alarma) Utilidad.getObjeto(centroSanitarioEnAlarma.getIdAlarma(), Constantes.ALARMA);
        CentroSanitario centroSanitario = (CentroSanitario) Utilidad.getObjeto(centroSanitarioEnAlarma.getIdCentroSanitario(), Constantes.CENTRO_SANITARIO);

        viewHolder.txtCardIdCentroSanitarioEnAlarma.setText(Constantes.ID_DP_SP + String.valueOf(centroSanitarioEnAlarma.getId()));
        viewHolder.txtCardFechaCentroEnAlarma.setText(Constantes.FECHA_DP_SP + centroSanitarioEnAlarma.getFechaRegistro());
        viewHolder.txtCardPersonaCentroEnAlarma.setText(Constantes.PERSONA_DP_SP + centroSanitarioEnAlarma.getPersona());
        viewHolder.txtCardAlarmaCentroEnAlarma.setText(Constantes.ID_ALARMA_DP_SP + String.valueOf(alarma.getId()));
        viewHolder.txtCardNombreCentroEnAlarma.setText(Constantes.CENTRO_DP_SP + centroSanitario.getNombre());
    }
}
