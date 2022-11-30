package com.example.teleappsistencia.ui.fragments.tipo_recurso_comunitario;

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
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipoRecursoComunitarioAdapter extends RecyclerView.Adapter<TipoRecursoComunitarioAdapter.TipoRecursoComunitarioViewHolder> {

    // Declaración de atributos.
    private List<TipoRecursoComunitario> items;
    private TipoRecursoComunitarioViewHolder tipoRecursoComunitarioViewHolder;

    public static class TipoRecursoComunitarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        public TextView nombreTipoRecursoComunitario;
        private ImageButton imageButtonModificarTipoRecursoComunitario;
        private ImageButton imageButtonVerTipoRecursoComunitario;
        private ImageButton imageButtonBorrarTipoRecursoComunitario;
        private TipoRecursoComunitario tipoRecursoComunitario;

        /**
         * Método para establecer el tipo de recurso comunitario.
         *
         * @param tipoRecursoComunitario: Recibe por parámetros el tipo de recurso comunitario.
         */
        public void setTipoRecursoComunitario(TipoRecursoComunitario tipoRecursoComunitario) {
            this.tipoRecursoComunitario = tipoRecursoComunitario;
        }

        /**
         * Se inicializan las variables.
         *
         * @param v: Recibe por parámetros la vista.
         */
        public TipoRecursoComunitarioViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.nombreTipoRecursoComunitario = (TextView) v.findViewById(R.id.nombreTipoRecursoComunitario);
            this.imageButtonModificarTipoRecursoComunitario = (ImageButton) v.findViewById(R.id.imageButtonModificarTipoRecursoComunitario);
            this.imageButtonVerTipoRecursoComunitario = (ImageButton) v.findViewById(R.id.imageButtonVerTipoRecursoComunitario);
            this.imageButtonBorrarTipoRecursoComunitario = (ImageButton) v.findViewById(R.id.imageButtonBorrarTipoRecursoComunitario);
        }

        /**
         * Se establece la acción de pulsar los botones.
         */
        public void setOnClickListeners() {
            this.imageButtonModificarTipoRecursoComunitario.setOnClickListener(this);
            this.imageButtonVerTipoRecursoComunitario.setOnClickListener(this);
            this.imageButtonBorrarTipoRecursoComunitario.setOnClickListener(this);
        }

        /**
         * Método para establecer las acciones de los botones, según sea un botón u otro.
         *
         * @param view: Recibe por parámetros la vista.
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonModificarTipoRecursoComunitario:
                    // Llamar al Fragment FragmentModificarTipoRecursoComunitario.
                    AppCompatActivity activityModificar = (AppCompatActivity) view.getContext();
                    FragmentModificarTipoRecursoComunitario fm = FragmentModificarTipoRecursoComunitario.newInstance(this.tipoRecursoComunitario);
                    activityModificar.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fm).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerTipoRecursoComunitario:
                    // Llamar al Fragment ConsultarTipoRecursoComunitario.
                    AppCompatActivity activityVer = (AppCompatActivity) view.getContext();
                    ConsultarTipoRecursoComunitario fc = ConsultarTipoRecursoComunitario.newInstance(this.tipoRecursoComunitario);
                    activityVer.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fc).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarTipoRecursoComunitario:
                    // Se llama al método que borra el tipo de recurso comunitario.
                    borrarTipoRecursoComunitario();
                    break;
            }
        }

        /**
         * Método Se que borra el tipo de recurso comunitario.
         */
        private void borrarTipoRecursoComunitario() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deleteTipoRecursoComunitario(tipoRecursoComunitario.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.MENSAJE_ELIMINAR_TIPO_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
                        volver();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_MENSAJE_ELIMINAR_TIPO_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
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
         * Este método vuelve a cargar el fragment con el listado.
         */
        private void volver() {
            MainActivity activity = (MainActivity) context;
            FragmentListarTipoRecursoComunitario fragmentListarTipoRecursoComunitario = new FragmentListarTipoRecursoComunitario();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragmentListarTipoRecursoComunitario)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param items
     */
    public TipoRecursoComunitarioAdapter(List<TipoRecursoComunitario> items) {
        this.items = items;
    }

    /**
     * Método para devolver el tamaño de la lista de tipos de recursos comunitarios.
     *
     * @return: Retorna el tamaño.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Método que carga el layout de las card view.
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @Override
    public TipoRecursoComunitarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_tipo_recurso_comunitario_card, viewGroup, false);
        this.tipoRecursoComunitarioViewHolder = new TipoRecursoComunitarioViewHolder(v);
        return tipoRecursoComunitarioViewHolder;
    }

    /**
     * Método que muestra los valores de los tipos de recursos comunitarios.
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(TipoRecursoComunitarioViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.nombreTipoRecursoComunitario.setText(items.get(i).getNombreTipoRecursoComunitario());
        this.tipoRecursoComunitarioViewHolder.setTipoRecursoComunitario(items.get(i));
    }
}