package com.example.teleappsistencia.ui.fragments.relacion_paciente_persona;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.RelacionPacientePersona;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelacionPacientePersonaAdapter extends RecyclerView.Adapter<RelacionPacientePersonaAdapter.RelacionPacientePersonaViewholder> {
    private List<com.example.teleappsistencia.modelos.RelacionPacientePersona> items;
    private RelacionPacientePersonaViewholder relacionPacientePersonaViewholder;

    public static class RelacionPacientePersonaViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        private ImageButton imageButtonModificarRelacionPacientePersona;
        private ImageButton imageButtonVerRelacionPacientePersona;
        private ImageButton imageButtonBorrarRelacionPacientePersona;
        private TextView tipoRelacionCard;
        private TextView prioridadCard;
        private TextView disponibilidadCard;
        private TextView pacienteRelacionCard;
        private TextView personaRelacionCard;
        private RelacionPacientePersona relacionPacientePersona;

        public RelacionPacientePersonaViewholder(View v) {
            super(v);
            this.context = v.getContext();
            this.imageButtonModificarRelacionPacientePersona = v.findViewById(R.id.imageButtonModificarRelacionPacientePersona);
            this.imageButtonVerRelacionPacientePersona = v.findViewById(R.id.imageButtonVerRelacionPacientePersona);
            this.imageButtonBorrarRelacionPacientePersona = v.findViewById(R.id.imageButtonBorrarRelacionPacientePersona);
            this.tipoRelacionCard = v.findViewById(R.id.tipoRelacionCard);
            this.prioridadCard = v.findViewById(R.id.prioridadCard);
            this.disponibilidadCard = v.findViewById(R.id.disponibilidadCard);
            this.pacienteRelacionCard = v.findViewById(R.id.pacienteRelacionCard);
            this.personaRelacionCard = v.findViewById(R.id.personaRelacionCard);
            this.setOnClickListeners();

        }

        public void setOnClickListeners() {
            this.imageButtonModificarRelacionPacientePersona.setOnClickListener(this);
            this.imageButtonVerRelacionPacientePersona.setOnClickListener(this);
            this.imageButtonBorrarRelacionPacientePersona.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            switch (view.getId()) {
                case R.id.imageButtonModificarRelacionPacientePersona:
                    ModificarRelacionPacientePersonaFragment modificarPacienteFragment = ModificarRelacionPacientePersonaFragment.newInstance(this.relacionPacientePersona);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, modificarPacienteFragment).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerRelacionPacientePersona:
                    ConsultarRelacionPacientePersonaFragment consultarPacienteFragment = ConsultarRelacionPacientePersonaFragment.newInstance(this.relacionPacientePersona);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarPacienteFragment).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarRelacionPacientePersona:
                    accionBorrarRelacionPacientePersona();
                    break;
            }
        }

        private void accionBorrarRelacionPacientePersona() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            double idSeleccionadoDouble = (double) this.relacionPacientePersona.getId();
            int idSeleccionado = (int) idSeleccionadoDouble;
            Call<ResponseBody> call = apiService.deleteRelacionPacientePersona(idSeleccionado, Constantes.BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.RELACION_PACIENTE_PERSONA_BORRADA_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                        recargarFragment();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_AL_BORRAR_RELACION_PACIENTE_PERSONA, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

        private void recargarFragment() {
            MainActivity activity = (MainActivity) this.context;
            ListarRelacionPacientePersonaFragment listarRelacionPacientePersonaFragment = new ListarRelacionPacientePersonaFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, listarRelacionPacientePersonaFragment)
                    .addToBackStack(null)
                    .commit();
        }

        public void setRelacionPacientePersona(RelacionPacientePersona relacionPacientePersona) {
            this.relacionPacientePersona = relacionPacientePersona;
        }

    }

    public RelacionPacientePersonaAdapter(List<RelacionPacientePersona> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RelacionPacientePersonaViewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_relacion_paciente_persona_card, viewGroup, false);
        relacionPacientePersonaViewholder = new RelacionPacientePersonaViewholder(v);
        return relacionPacientePersonaViewholder;
    }

    @Override
    public void onBindViewHolder(RelacionPacientePersonaViewholder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.setRelacionPacientePersona(items.get(i));
        viewHolder.tipoRelacionCard.setText(items.get(i).getTipoRelacion());
        viewHolder.prioridadCard.setText("Prioridad: " + String.valueOf(items.get(i).getPrioridad()));
        viewHolder.disponibilidadCard.setText(items.get(i).getDisponibilidad());
        Paciente paciente = (Paciente) Utilidad.getObjeto(items.get(i).getIdPaciente(), "Paciente");
        if (items.get(i).getIdPaciente() != null) {
            viewHolder.pacienteRelacionCard.setText("SS del paciente: " + paciente.getNumeroSeguridadSocial());
        } else {
            viewHolder.pacienteRelacionCard.setText("Paciente: ");
        }
        Persona persona = (Persona) Utilidad.getObjeto(items.get(i).getIdPersona(), "Persona");
        if (persona != null) {
            viewHolder.personaRelacionCard.setText("Persona de contacto: " + persona.getNombre() + " " + persona.getApellidos());
        }else {
            viewHolder.personaRelacionCard.setText("Persona de contacto: ");
        }
    }
}
