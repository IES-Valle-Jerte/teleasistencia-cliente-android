package com.example.teleappsistencia.ui.fragments.recurso_comunitario;

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
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.RecursoComunitario;
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

public class RecursoComunitarioAdapter extends RecyclerView.Adapter<RecursoComunitarioAdapter.RecursoComunitarioViewHolder> {

    // Declaración de atributos.
    private List<RecursoComunitario> items;
    private RecursoComunitarioViewHolder recursoComunitarioViewHolder;

    public static class RecursoComunitarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        private TextView nombreRecursoComunitario;
        private TextView telefonoRecursoComunitario;
        private TextView tipoRecursoComunitarioRecursoComunitario;
        private TextView direccionRecursoComunitario;
        private ImageButton imageButtonModificarRecursoComunitario;
        private ImageButton imageButtonVerRecursoComunitario;
        private ImageButton imageButtonBorrarRecursoComunitario;
        private RecursoComunitario recursoComunitario;

        /**
         * Método para establecer el recurso comunitario.
         *
         * @param recursoComunitario: Recibe por parámetros el recurso comunitario.
         */
        public void setRecursoComunitario(RecursoComunitario recursoComunitario) {
            this.recursoComunitario = recursoComunitario;
        }

        /**
         * Se inicializan las variables.
         *
         * @param v: Recibe por parámetros la vista.
         */
        public RecursoComunitarioViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.nombreRecursoComunitario = (TextView) v.findViewById(R.id.nombreRecursoComunitario);
            this.telefonoRecursoComunitario = (TextView) v.findViewById(R.id.telefonoRecursoComunitario);
            this.tipoRecursoComunitarioRecursoComunitario = (TextView) v.findViewById(R.id.tipoRecursoComunitarioRecursoComunitario);
            this.direccionRecursoComunitario = (TextView) v.findViewById(R.id.direccionRecursoComunitario);
            this.imageButtonModificarRecursoComunitario = (ImageButton) v.findViewById(R.id.imageButtonModificarRecursoComunitario);
            this.imageButtonVerRecursoComunitario = (ImageButton) v.findViewById(R.id.imageButtonVerRecursoComunitario);
            this.imageButtonBorrarRecursoComunitario = (ImageButton) v.findViewById(R.id.imageButtonBorrarRecursoComunitario);
        }

        /**
         * Se establece la acción de pulsar los botones.
         */
        public void setOnClickListeners() {
            this.imageButtonModificarRecursoComunitario.setOnClickListener(this);
            this.imageButtonVerRecursoComunitario.setOnClickListener(this);
            this.imageButtonBorrarRecursoComunitario.setOnClickListener(this);
        }

        /**
         * Método para establecer las acciones de los botones, según sea un botón u otro.
         *
         * @param view: Recibe por parámetros la vista.
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonModificarRecursoComunitario:
                    // Llamar al Fragment FragmentModificarRecursoComunitario.
                    AppCompatActivity activityModificar = (AppCompatActivity) view.getContext();
                    FragmentModificarRecursoComunitario fm = FragmentModificarRecursoComunitario.newInstance(this.recursoComunitario);
                    activityModificar.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fm).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerRecursoComunitario:
                    // Llamar al Fragment ConsultarRecursoComunitario.
                    AppCompatActivity activityVer = (AppCompatActivity) view.getContext();
                    ConsultarRecursoComunitario fc = ConsultarRecursoComunitario.newInstance(this.recursoComunitario);
                    activityVer.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fc).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarRecursoComunitario:
                    // Se llama al método que borra el recurso comunitario.
                    borrarRecursoComunitario();
                    break;
            }
        }

        /**
         * Método Se que borra el recurso comunitario.
         */
        private void borrarRecursoComunitario() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deleteRecursoComunitario(recursoComunitario.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.MENSAJE_ELIMINAR_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
                        volver();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_MENSAJE_ELIMINAR_RECURSO_COMUNITARIO, Toast.LENGTH_SHORT).show();
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
            FragmentListarRecursoComunitario fragmentListarRecursoComunitario = new FragmentListarRecursoComunitario();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragmentListarRecursoComunitario)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param items
     */
    public RecursoComunitarioAdapter(List<RecursoComunitario> items) {
        this.items = items;
    }

    /**
     * Método para devolver el tamaño de la lista de recursos comunitarios.
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
    public RecursoComunitarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_recurso_comunitario_card, viewGroup, false);
        this.recursoComunitarioViewHolder = new RecursoComunitarioViewHolder(v);
        return recursoComunitarioViewHolder;
    }

    /**
     * Método que muestra los valores del recurso comunitario.
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(RecursoComunitarioViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();

        RecursoComunitario recursoComunitario = items.get(i);
        TipoRecursoComunitario tipoRecursoComunitario = (TipoRecursoComunitario) Utilidad.getObjeto(recursoComunitario, Constantes.TIPO_RECURSO_COMUNITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(recursoComunitario.getDireccion(), Constantes.DIRECCION);

        viewHolder.nombreRecursoComunitario.setText(items.get(i).getNombre());
        viewHolder.telefonoRecursoComunitario.setText(items.get(i).getTelefono());
        viewHolder.tipoRecursoComunitarioRecursoComunitario.setText(tipoRecursoComunitario.getNombreTipoRecursoComunitario());
        viewHolder.direccionRecursoComunitario.setText(direccion.getDireccion());
        this.recursoComunitarioViewHolder.setRecursoComunitario(items.get(i));
    }
}