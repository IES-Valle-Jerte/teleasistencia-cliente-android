package com.example.teleappsistencia.ui.fragments.historico_tipo_situacion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.grupos.ListarGruposFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.modelos.HistoricoTipoSituacion;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoTipoSituacionAdapter extends RecyclerView.Adapter<HistoricoTipoSituacionAdapter.HistoricoTipoSituacionViewHolder> {

    private List<HistoricoTipoSituacion> items;
    private HistoricoTipoSituacionViewHolder historicoTipoSituacionViewHolder;

    public static class HistoricoTipoSituacionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        public TextView textView_id;
        private ImageButton imgBtn_modificar;
        private ImageButton imgBtn_ver;
        private ImageButton imgBtn_borrar;

        private HistoricoTipoSituacion historicoTipoSituacion;

        public HistoricoTipoSituacionViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.textView_id = v.findViewById(R.id.textView_id_historicoTipoSituacion);
            this.imgBtn_modificar = v.findViewById(R.id.imageButtonModificar);
            this.imgBtn_ver = v.findViewById(R.id.imageButtonVer);
            this.imgBtn_borrar = v.findViewById(R.id.imageButtonBorrar);
        }

        public void setOnClickListeners() {
            this.imgBtn_modificar.setOnClickListener(this);
            this.imgBtn_ver.setOnClickListener(this);
            this.imgBtn_borrar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            switch (view.getId()) {
                case R.id.imageButtonModificar:
                    // Llamar al Fragment ModificarHistoricoTipoSituacionFragment.
                    ModificarHistoricoTipoSituacionFragment fragmentModificar = ModificarHistoricoTipoSituacionFragment.newInstance(this.historicoTipoSituacion);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentModificar).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVer:
                    // Llamar al Fragment ConsultarHistoricoTipoSituacionFragment.
                    ConsultarHistoricoTipoSituacionFragment fragmentConsultar = ConsultarHistoricoTipoSituacionFragment.newInstance(this.historicoTipoSituacion);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentConsultar).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrar:
                    // Creo un alertDialog para preguntar si se desea eliminar el modelo.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(Constantes.ELIMINAR_ELEMENTO);
                    builder.setMessage(Constantes.ESTAS_SEGURO_ELIMINAR);
                    builder.setPositiveButton(Constantes.SI, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            borrarHistoricoTipoSituacion();
                            dialogInterface.cancel();
                        }
                    });
                    builder.setNegativeButton(Constantes.NO, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                    break;
            }
        }

        /**
         * Método que realiza una petición a la API para borrar un HistoricoTipoSituacion.
         */
        private void borrarHistoricoTipoSituacion() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deleteHistoricoTipoSituacion(historicoTipoSituacion.getId(), Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Response<String> respuesta = response.body();
                        AlertDialogBuilder.crearInfoAlerDialog(context, Constantes.INFO_ALERTDIALOG_ELIMINADO_HISTORICO_TIPO_SITUACION);
                        recargarFragment();
                    } else {
                        AlertDialogBuilder.crearErrorAlerDialog(context, Integer.toString(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Response<String>> call, Throwable t) {
                    t.printStackTrace();
                    System.out.println(t.getMessage());
                }
            });
        }

        /**
         * Método para recargar el fragment listar.
         */
        private void recargarFragment() {
            MainActivity activity = (MainActivity) this.context;
            ListarHistoricoTipoSituacionFragment fragment = new ListarHistoricoTipoSituacionFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        public void setHistoricoTipoSituacion(HistoricoTipoSituacion historicoTipoSituacion) {
            this.historicoTipoSituacion = historicoTipoSituacion;
        }
    }

    public HistoricoTipoSituacionAdapter(List<HistoricoTipoSituacion> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public HistoricoTipoSituacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_card_historico_tipo_situacion, viewGroup, false);
        historicoTipoSituacionViewHolder = new HistoricoTipoSituacionViewHolder(v);
        return historicoTipoSituacionViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoricoTipoSituacionViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.textView_id.setText(Constantes.ID_CON_DOS_PUNTOS + items.get(i).getId());
        historicoTipoSituacionViewHolder.setHistoricoTipoSituacion(items.get(i));
    }
}
