package com.example.teleappsistencia.ui.fragments.grupos;

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
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GruposAdapter extends RecyclerView.Adapter<GruposAdapter.GruposViewHolder> {

    private List<Grupo> items;
    private GruposViewHolder grupoViewHolder;

    public static class GruposViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        public TextView textView_nombre;
        private ImageButton imgBtn_modificar;
        private ImageButton imgBtn_ver;
        private ImageButton imgBtn_borrar;

        private Grupo grupo;

        public GruposViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.textView_nombre = v.findViewById(R.id.textView_nombre_grupo);
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
                    // Llamar al Fragment ModificarGruposFragment.
                    ModificarGruposFragment fragmentModificar = ModificarGruposFragment.newInstance(this.grupo);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentModificar).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVer:
                    // Llamar al Fragment ConsultarGruposFragment.
                    ConsultarGruposFragment fragmentConsultar = ConsultarGruposFragment.newInstance(this.grupo);
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
                            borrarGrupo();
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
         * Método que realiza una petición a la API para borrar un Grupo.
         */
        private void borrarGrupo() {
            int id;

            if(grupo.getId() == 0){
                id = grupo.getPk();
            } else {
                id = grupo.getId();
            }

            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deleteGrupo(id, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Response<String> respuesta = response.body();
                        AlertDialogBuilder.crearInfoAlerDialog(context, Constantes.INFO_ALERTDIALOG_ELIMINADO_GRUPO);
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
            ListarGruposFragment fragment = new ListarGruposFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        public void setGrupo(Grupo grupo) {
            this.grupo = grupo;
        }
    }

    public GruposAdapter(List<Grupo> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public GruposViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_card_grupos, viewGroup, false);
        grupoViewHolder = new GruposViewHolder(v);
        return grupoViewHolder;
    }

    @Override
    public void onBindViewHolder(GruposViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.textView_nombre.setText(items.get(i).getName());
        grupoViewHolder.setGrupo(items.get(i));
    }
}
