package com.example.teleappsistencia.ui.fragments.centro_sanitario;

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
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentroSanitarioAdapter extends RecyclerView.Adapter<CentroSanitarioAdapter.CentroSanitarioViewHolder> {

    // Declaración de atributos.
    private List<CentroSanitario> items;
    private CentroSanitarioViewHolder centroSanitarioViewHolder;

    public static class CentroSanitarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        public TextView nombreCentroSanitario;
        public TextView telefonoCentroSanitario;
        public TextView tipoCentroSanitarioCentroSanitario;
        public TextView direccionCentroSanitario;
        private ImageButton imageButtonModificarCentroSanitario;
        private ImageButton imageButtonVerCentroSanitario;
        private ImageButton imageButtonBorrarCentroSanitario;
        private CentroSanitario centroSanitario;

        /**
         * Método para establecer el centro sanitario.
         *
         * @param centroSanitario: Recibe por parámetros el centro sanitario.
         */
        public void setCentroSanitario(CentroSanitario centroSanitario) {
            this.centroSanitario = centroSanitario;
        }

        /**
         * Se inicializan las variables.
         *
         * @param v: Recibe por parámetros la vista.
         */
        public CentroSanitarioViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.nombreCentroSanitario = (TextView) v.findViewById(R.id.nombreCentroSanitario);
            this.telefonoCentroSanitario = (TextView) v.findViewById(R.id.telefonoCentroSanitario);
            this.tipoCentroSanitarioCentroSanitario = (TextView) v.findViewById(R.id.tipoCentroSanitarioCentroSanitario);
            this.direccionCentroSanitario = (TextView) v.findViewById(R.id.direccionCentroSanitario);
            this.imageButtonModificarCentroSanitario = (ImageButton) v.findViewById(R.id.imageButtonModificarCentroSanitario);
            this.imageButtonVerCentroSanitario = (ImageButton) v.findViewById(R.id.imageButtonVerCentroSanitario);
            this.imageButtonBorrarCentroSanitario = (ImageButton) v.findViewById(R.id.imageButtonBorrarCentroSanitario);
        }

        /**
         * Se establece la acción de pulsar los botones.
         */
        public void setOnClickListeners() {
            this.imageButtonModificarCentroSanitario.setOnClickListener(this);
            this.imageButtonVerCentroSanitario.setOnClickListener(this);
            this.imageButtonBorrarCentroSanitario.setOnClickListener(this);
        }

        /**
         * Método para establecer las acciones de los botones, según sea un botón u otro.
         *
         * @param view: Recibe por parámetros la vista.
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonModificarCentroSanitario:
                    // Llamar al Fragment FragmentModificarCentroSanitario.
                    AppCompatActivity activityModificar = (AppCompatActivity) view.getContext();
                    FragmentModificarCentroSanitario fm = FragmentModificarCentroSanitario.newInstance(this.centroSanitario);
                    activityModificar.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fm).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerCentroSanitario:
                    // Llamar al Fragment ConsultarCentroSanitario.
                    AppCompatActivity activityVer = (AppCompatActivity) view.getContext();
                    ConsultarCentroSanitario fc = ConsultarCentroSanitario.newInstance(this.centroSanitario);
                    activityVer.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fc).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarCentroSanitario:
                    // Se llama al método que borra el centro sanitario.
                    borrarCentroSanitario();
                    break;
            }
        }

        /**
         * Método Se que borra el centro sanitario.
         */
        private void borrarCentroSanitario() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deleteCentroSanitario(centroSanitario.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.MENSAJE_ELIMINAR_CENTRO_SANITARIO, Toast.LENGTH_SHORT).show();
                        volver();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_MENSAJE_ELIMINAR_CENTRO_SANITARIO, Toast.LENGTH_SHORT).show();
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
            FragmentListarCentroSanitario fragmentListarCentroSanitario = new FragmentListarCentroSanitario();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragmentListarCentroSanitario)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param items
     */
    public CentroSanitarioAdapter(List<CentroSanitario> items) {
        this.items = items;
    }

    /**
     * Método para devolver el tamaño de la lista de centros sanitarios.
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
    public CentroSanitarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_centro_sanitario_card, viewGroup, false);
        this.centroSanitarioViewHolder = new CentroSanitarioViewHolder(v);
        return centroSanitarioViewHolder;
    }

    /**
     * Método que muestra los valores del centro sanitario.
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(CentroSanitarioViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();

        CentroSanitario centroSanitario = items.get(i);
        TipoCentroSanitario tipoCentroSanitario = (TipoCentroSanitario) Utilidad.getObjeto(centroSanitario, Constantes.TIPO_CENTRO_SANITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(centroSanitario.getDireccion(), Constantes.DIRECCION);

        viewHolder.nombreCentroSanitario.setText(items.get(i).getNombre());
        viewHolder.telefonoCentroSanitario.setText(items.get(i).getTelefono());
        viewHolder.tipoCentroSanitarioCentroSanitario.setText(tipoCentroSanitario.getNombreTipoCentroSanitario());
        viewHolder.direccionCentroSanitario.setText(direccion.getDireccion());
        this.centroSanitarioViewHolder.setCentroSanitario(items.get(i));
    }
}