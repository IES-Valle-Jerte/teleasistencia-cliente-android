package com.example.teleappsistencia.ui.fragments.relacion_usuario_centro;

import android.content.Context;
import android.graphics.Color;
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
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.RelacionUsuarioCentro;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.relacion_terminal_recurso_comunitario.ListarRelacionTerminalRecursoComunitarioFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelacionUsuarioCentroAdapter extends RecyclerView.Adapter<RelacionUsuarioCentroAdapter.RelacionUsuarioCentroViewHolder> {
    private List<RelacionUsuarioCentro> items;
    private RelacionUsuarioCentroViewHolder relacionUsuarioCentroViewHolder;

    public static class RelacionUsuarioCentroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        private ImageButton imageButtonModificarRelacionUsuarioCentro;
        private ImageButton imageButtonVerRelacionUsuarioCentro;
        private ImageButton imageButtonBorrarRelacionUsuarioCentro;
        private TextView personaContactoCard;
        private TextView distanciaCard;
        private TextView idPacienteCard;
        private TextView centroSanitarioCard;
        private RelacionUsuarioCentro relacionUsuarioCentro;

        public RelacionUsuarioCentroViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.centroSanitarioCard = v.findViewById(R.id.centroSanitarioCard);
            this.distanciaCard = v.findViewById(R.id.distanciaCard);
            this.idPacienteCard = v.findViewById(R.id.idPacienteCard);
            this.personaContactoCard = v.findViewById(R.id.personaContactoCard);
            this.imageButtonVerRelacionUsuarioCentro = v.findViewById(R.id.imageButtonVerRelacionUsuarioCentro);
            this.imageButtonModificarRelacionUsuarioCentro = v.findViewById(R.id.imageButtonModificarRelacionUsuarioCentro);
            this.imageButtonBorrarRelacionUsuarioCentro = v.findViewById(R.id.imageButtonBorrarRelacionUsuarioCentro);
        }

        public void setOnClickListeners() {
            this.imageButtonVerRelacionUsuarioCentro.setOnClickListener(this);
            this.imageButtonModificarRelacionUsuarioCentro.setOnClickListener(this);
            this.imageButtonBorrarRelacionUsuarioCentro.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            switch (view.getId()) {
                case R.id.imageButtonModificarRelacionUsuarioCentro:
                    ModificarRelacionUsuarioCentroFragment modificarRelacionUsuarioCentroFragment = ModificarRelacionUsuarioCentroFragment.newInstance(this.relacionUsuarioCentro);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, modificarRelacionUsuarioCentroFragment).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerRelacionUsuarioCentro:
                    ConsultarRelacionUsuarioCentroFragment consultarRelacionUsuarioCentroFragment = ConsultarRelacionUsuarioCentroFragment.newInstance(this.relacionUsuarioCentro);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarRelacionUsuarioCentroFragment).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarRelacionUsuarioCentro:
                    accionBorrarRelacionUsuarioCentro();
                    break;
            }
        }

        private void accionBorrarRelacionUsuarioCentro() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deleteRelacionUsuarioCentro(String.valueOf(this.relacionUsuarioCentro.getId()), Constantes.BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.RELACIÓN_USUARIO_CENTRO_BORRADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                        recargarFragment();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_AL_BORRAR_LA_RELACIÓN_USUARIO_CENTRO, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

        private void recargarFragment() {
            MainActivity activity = (MainActivity) this.context;
            ListarRelacionUsuarioCentroFragment listarRelacionUsuarioCentroFragment = new ListarRelacionUsuarioCentroFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, listarRelacionUsuarioCentroFragment)
                    .addToBackStack(null)
                    .commit();
        }

        public void setRelacionUsuarioCentro(RelacionUsuarioCentro relacionUsuarioCentro) {
            this.relacionUsuarioCentro = relacionUsuarioCentro;
        }


    }

    public RelacionUsuarioCentroAdapter(List<RelacionUsuarioCentro> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RelacionUsuarioCentroViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_relacion_usuario_centro_card, viewGroup, false);
        relacionUsuarioCentroViewHolder = new RelacionUsuarioCentroViewHolder(v);
        return relacionUsuarioCentroViewHolder;
    }

    @Override
    public void onBindViewHolder(RelacionUsuarioCentroViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.setRelacionUsuarioCentro(items.get(i));
        viewHolder.personaContactoCard.setText(items.get(i).getPersonaContacto());
        viewHolder.distanciaCard.setText("Distancia: "+ String.valueOf(items.get(i).getDistancia()) + " Km");
        CentroSanitario centroSanitario = (CentroSanitario) Utilidad.getObjeto(items.get(i).getIdCentroSanitario(), "CentroSanitario");
        viewHolder.centroSanitarioCard.setText(centroSanitario.getNombre());
        if(items.get(i).getIdPaciente() != null) {
            Paciente paciente = (Paciente) Utilidad.getObjeto(items.get(i).getIdPaciente(), "Paciente");
            viewHolder.idPacienteCard.setText("ID del paciente: "+ paciente.getId());
        }else{
            viewHolder.idPacienteCard.setText("ID del paciente: No asignado");
        }
        relacionUsuarioCentroViewHolder.setRelacionUsuarioCentro(items.get(i));
    }
}
