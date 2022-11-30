package com.example.teleappsistencia.ui.fragments.personaContactoEnAlarma;

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
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.PersonaContactoEnAlarma;
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


public class PersonaContactoEnAlarmaAdapter extends RecyclerView.Adapter<PersonaContactoEnAlarmaAdapter.PersonaContactoEnAlarmaViewHolder> {
    private List<PersonaContactoEnAlarma> items;

    public static class PersonaContactoEnAlarmaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        private Context context;
        private TextView txtCardIdPersonaContactoEnAlarma;
        private TextView txtCardFechaPersonaContactoEnAlarma;
        private TextView txtCardIdAlarmaPersonaContactoEnAlarma;
        private TextView txtCardNombrePersonaContactoEnAlarma;
        private ImageButton imageButtonVerPersonaContactoEnAlarma;
        private ImageButton imageButtonModificarPersonaContactoEnAlarma;
        private ImageButton imageButtonBorrarPersonaContactoEnAlarma;
        private PersonaContactoEnAlarma personaContactoEnAlarma;

        public PersonaContactoEnAlarmaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            // Capturamos los elementos del layout
            this.txtCardIdPersonaContactoEnAlarma = (TextView) v.findViewById(R.id.txtCardIdPersonaContactoEnAlarma);
            this.txtCardFechaPersonaContactoEnAlarma = (TextView) v.findViewById(R.id.txtCardFechaPersonaContactoEnAlarma);
            this.txtCardIdAlarmaPersonaContactoEnAlarma = (TextView) v.findViewById(R.id.txtCardIdAlarmaPersonaContactoEnAlarma);
            this.txtCardNombrePersonaContactoEnAlarma = (TextView) v.findViewById(R.id.txtCardNombrePersonaContactoEnAlarma);
            this.imageButtonVerPersonaContactoEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonVerPersonaContactoEnAlarma);
            this.imageButtonModificarPersonaContactoEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonModificarPersonaContactoEnAlarma);
            this.imageButtonBorrarPersonaContactoEnAlarma = (ImageButton) v.findViewById(R.id.imageButtonBorrarPersonaContactoEnAlarma);
        }
        //Asignamos listeners
        public void setOnClickListeners() {
            this.imageButtonVerPersonaContactoEnAlarma.setOnClickListener(this);
            this.imageButtonModificarPersonaContactoEnAlarma.setOnClickListener(this);
            this.imageButtonBorrarPersonaContactoEnAlarma.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity) context;
            switch (view.getId()) {
                case R.id.imageButtonVerPersonaContactoEnAlarma:
                    ConsultarPersonaContactoEnAlarmaFragment cPCEA = ConsultarPersonaContactoEnAlarmaFragment.newInstance(this.personaContactoEnAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, cPCEA)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonModificarPersonaContactoEnAlarma:
                    ModificarPersonaContactoEnAlarmaFragment mPCEA = ModificarPersonaContactoEnAlarmaFragment.newInstance(this.personaContactoEnAlarma);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, mPCEA)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.imageButtonBorrarPersonaContactoEnAlarma:
                    borrarPersonaContactoEnAlarma();
                    break;
            }
        }
        // Setter para poder pasarle el atributo desde del Adapter
        public void setPersonaContactoEnAlarma(PersonaContactoEnAlarma personaContactoEnAlarma){
            this.personaContactoEnAlarma = personaContactoEnAlarma;
        }

        /**
         * Método que lanza la petición DELETE a la API REST para borrar la la persona de contacto en alarma
         */
        private void borrarPersonaContactoEnAlarma(){
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deletePersonaContactoEnAlarmabyId(this.personaContactoEnAlarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, Constantes.PERSONA_CONTACTO_EN_ALARMA_BORRADA, Toast.LENGTH_LONG).show();
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
            ListarPersonasContactoEnAlarmaFragment lPCEN = new ListarPersonasContactoEnAlarmaFragment();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, lPCEN)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Se le carga la lista de items al Adapter, en este caso, de Personas de Contacto en Alarma
     * @param items
     */
    public PersonaContactoEnAlarmaAdapter(List<PersonaContactoEnAlarma> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public PersonaContactoEnAlarmaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_persona_contacto_en_alarma_card, viewGroup, false);
        return new PersonaContactoEnAlarmaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonaContactoEnAlarmaViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        // En el bind, le cargamos los atributos al layout de la tarjeta
        PersonaContactoEnAlarma personaContactoEnAlarma = items.get(i);
        viewHolder.setPersonaContactoEnAlarma(personaContactoEnAlarma);

        Alarma alarma = (Alarma) Utilidad.getObjeto(personaContactoEnAlarma.getIdAlarma(), Constantes.ALARMA);
        Persona persona = (Persona) Utilidad.getObjeto(personaContactoEnAlarma.getIdPersonaContacto(), Constantes.PERSONA);

        viewHolder.txtCardIdPersonaContactoEnAlarma.setText(Constantes.ID_DP_SP + String.valueOf(personaContactoEnAlarma.getId()));
        viewHolder.txtCardFechaPersonaContactoEnAlarma.setText(Constantes.FECHA_DP_SP + personaContactoEnAlarma.getFechaRegistro());
        viewHolder.txtCardNombrePersonaContactoEnAlarma.setText(Constantes.PERSONA_DP_SP + persona.getNombre() + Constantes.ESPACIO + persona.getApellidos());
        viewHolder.txtCardIdAlarmaPersonaContactoEnAlarma.setText(Constantes.ID_ALARMA_DP_SP + String.valueOf(alarma.getId()));
    }
}
