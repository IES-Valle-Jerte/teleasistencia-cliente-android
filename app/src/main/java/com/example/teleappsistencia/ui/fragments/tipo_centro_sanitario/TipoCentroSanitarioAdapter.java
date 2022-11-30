package com.example.teleappsistencia.ui.fragments.tipo_centro_sanitario;

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
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipoCentroSanitarioAdapter extends RecyclerView.Adapter<TipoCentroSanitarioAdapter.TipoCentroSanitarioViewHolder> {

    // Declaración de atributos.
    private List<TipoCentroSanitario> items;
    private TipoCentroSanitarioViewHolder tipoCentroSanitarioViewHolder;

    public static class TipoCentroSanitarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        public TextView nombreTipoCentroSanitario;
        private ImageButton imageButtonModificarTipoCentroSanitario;
        private ImageButton imageButtonVerTipoCentroSanitario;
        private ImageButton imageButtonBorrarTipoCentroSanitario;
        private TipoCentroSanitario tipoCentroSanitario;

        /**
         * Método para establecer el tipo de centro sanitario.
         *
         * @param tipoCentroSanitario: Recibe por parámetros el tipo de centro sanitario.
         */
        public void setTipoCentroSanitario(TipoCentroSanitario tipoCentroSanitario) {
            this.tipoCentroSanitario = tipoCentroSanitario;
        }

        /**
         * Se inicializan las variables.
         *
         * @param v: Recibe por parámetros la vista.
         */
        public TipoCentroSanitarioViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.nombreTipoCentroSanitario = (TextView) v.findViewById(R.id.nombreTipoCentroSanitario);
            this.imageButtonModificarTipoCentroSanitario = (ImageButton) v.findViewById(R.id.imageButtonModificarTipoCentroSanitario);
            this.imageButtonVerTipoCentroSanitario = (ImageButton) v.findViewById(R.id.imageButtonVerTipoCentroSanitario);
            this.imageButtonBorrarTipoCentroSanitario = (ImageButton) v.findViewById(R.id.imageButtonBorrarTipoCentroSanitario);
        }

        /**
         * Se establece la acción de pulsar los botones.
         */
        public void setOnClickListeners() {
            this.imageButtonModificarTipoCentroSanitario.setOnClickListener(this);
            this.imageButtonVerTipoCentroSanitario.setOnClickListener(this);
            this.imageButtonBorrarTipoCentroSanitario.setOnClickListener(this);
        }

        /**
         * Método para establecer las acciones de los botones, según sea un botón u otro.
         *
         * @param view: Recibe por parámetros la vista.
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonModificarTipoCentroSanitario:
                    // Llamar al Fragment FragmentModificarTipoCentroSanitario.
                    AppCompatActivity activityModificar = (AppCompatActivity) view.getContext();
                    FragmentModificarTipoCentroSanitario fm = FragmentModificarTipoCentroSanitario.newInstance(this.tipoCentroSanitario);
                    activityModificar.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fm).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerTipoCentroSanitario:
                    // Llamar al Fragment ConsultarTipoCentroSanitario.
                    AppCompatActivity activityVer = (AppCompatActivity) view.getContext();
                    ConsultarTipoCentroSanitario fc = ConsultarTipoCentroSanitario.newInstance(this.tipoCentroSanitario);
                    activityVer.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fc).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarTipoCentroSanitario:
                    // Se llama al método que borra el tipo de centro sanitario.
                    borrarTipoCentroSanitario();
                    break;
            }
        }

        /**
         * Método Se que borra el tipo de centro sanitario.
         */
        private void borrarTipoCentroSanitario() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deleteTipoCentroSanitario(tipoCentroSanitario.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.MENSAJE_ELIMINAR_TIPO_CENTRO_SANITARIO, Toast.LENGTH_SHORT).show();
                        volver();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_MENSAJE_ELIMINAR_TIPO_CENTRO_SANITARIO, Toast.LENGTH_SHORT).show();
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
            FragmentListarTipoCentroSanitario fragmentListarTipoCentroSanitario = new FragmentListarTipoCentroSanitario();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragmentListarTipoCentroSanitario)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param items
     */
    public TipoCentroSanitarioAdapter(List<TipoCentroSanitario> items) {
        this.items = items;
    }

    /**
     * Método para devolver el tamaño de la lista de tipos de centros sanitarios.
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
    public TipoCentroSanitarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_tipo_centro_sanitario_card, viewGroup, false);
        this.tipoCentroSanitarioViewHolder = new TipoCentroSanitarioViewHolder(v);
        return tipoCentroSanitarioViewHolder;
    }

    /**
     * Método que muestra los valores de los tipos de centros sanitarios.
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(TipoCentroSanitarioViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.nombreTipoCentroSanitario.setText(items.get(i).getNombreTipoCentroSanitario());
        this.tipoCentroSanitarioViewHolder.setTipoCentroSanitario(items.get(i));
    }
}