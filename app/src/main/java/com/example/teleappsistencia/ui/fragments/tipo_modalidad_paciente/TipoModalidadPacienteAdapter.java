package com.example.teleappsistencia.ui.fragments.tipo_modalidad_paciente;

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
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipoModalidadPacienteAdapter extends RecyclerView.Adapter<TipoModalidadPacienteAdapter.TipoModalidadPacienteViewHolder> {

    // Declaración de atributos.
    private List<TipoModalidadPaciente> items;
    private TipoModalidadPacienteViewHolder tipoModalidadPacienteViewHolder;

    public static class TipoModalidadPacienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        public TextView nombreTipoModalidadPaciente;
        private ImageButton imageButtonModificarTipoModalidadPaciente;
        private ImageButton imageButtonVerTipoModalidadPaciente;
        private ImageButton imageButtonBorrarTipoModalidadPaciente;
        private TipoModalidadPaciente tipoModalidadPaciente;

        /**
         * Método para establecer el tipo de modalidad de paciente.
         *
         * @param tipoModalidadPaciente: Recibe por parámetros el tipo de modalidad de paciente.
         */
        public void setTipoModalidadPaciente(TipoModalidadPaciente tipoModalidadPaciente) {
            this.tipoModalidadPaciente = tipoModalidadPaciente;
        }

        /**
         * Se inicializan las variables.
         *
         * @param v: Recibe por parámetros la vista.
         */
        public TipoModalidadPacienteViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.nombreTipoModalidadPaciente = (TextView) v.findViewById(R.id.nombreTipoModalidadPaciente);
            this.imageButtonModificarTipoModalidadPaciente = (ImageButton) v.findViewById(R.id.imageButtonModificarTipoModalidadPaciente);
            this.imageButtonVerTipoModalidadPaciente = (ImageButton) v.findViewById(R.id.imageButtonVerTipoModalidadPaciente);
            this.imageButtonBorrarTipoModalidadPaciente = (ImageButton) v.findViewById(R.id.imageButtonBorrarTipoModalidadPaciente);
        }

        /**
         * Se establece la acción de pulsar los botones.
         */
        public void setOnClickListeners() {
            this.imageButtonModificarTipoModalidadPaciente.setOnClickListener(this);
            this.imageButtonVerTipoModalidadPaciente.setOnClickListener(this);
            this.imageButtonBorrarTipoModalidadPaciente.setOnClickListener(this);
        }

        /**
         * Método para establecer las acciones de los botones, según sea un botón u otro.
         *
         * @param view: Recibe por parámetros la vista.
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonModificarTipoModalidadPaciente:
                    // Llamar al Fragment FragmentModificarTipoModalidadPaciente.
                    AppCompatActivity activityModificar = (AppCompatActivity) view.getContext();
                    FragmentModificarTipoModalidadPaciente fm = FragmentModificarTipoModalidadPaciente.newInstance(this.tipoModalidadPaciente);
                    activityModificar.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fm).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerTipoModalidadPaciente:
                    // Llamar al Fragment ConsultarTipoModalidadPaciente.
                    AppCompatActivity activityVer = (AppCompatActivity) view.getContext();
                    ConsultarTipoModalidadPaciente fc = ConsultarTipoModalidadPaciente.newInstance(this.tipoModalidadPaciente);
                    activityVer.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fc).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarTipoModalidadPaciente:
                    // Se llama al método que borra el tipo de modalidad de paciente.
                    borrarTipoModalidadPaciente();
                    break;
            }
        }

        /**
         * Método Se que borra el tipo de modalidad de paciente.
         */
        private void borrarTipoModalidadPaciente() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deleteTipoModalidadPaciente(tipoModalidadPaciente.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.MENSAJE_ELIMINAR_TIPO_MODALIDAD_PACIENTE, Toast.LENGTH_SHORT).show();
                        volver();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_MENSAJE_ELIMINAR_TIPO_MODALIDAD_PACIENTE, Toast.LENGTH_SHORT).show();
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
            FragmentListarTipoModalidadPaciente fragmentListarTipoModalidadPaciente = new FragmentListarTipoModalidadPaciente();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragmentListarTipoModalidadPaciente)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Inicializamos las variables en el constructor parametrizado.
     *
     * @param items
     */
    public TipoModalidadPacienteAdapter(List<TipoModalidadPaciente> items) {
        this.items = items;
    }

    /**
     * Método para devolver el tamaño de la lista de tipos de modalidades de pacientes.
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
    public TipoModalidadPacienteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_tipo_modalidad_paciente_card, viewGroup, false);
        this.tipoModalidadPacienteViewHolder = new TipoModalidadPacienteViewHolder(v);
        return tipoModalidadPacienteViewHolder;
    }

    /**
     * Método que muestra los valores de los tipos de modalidades de pacientes.
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(TipoModalidadPacienteViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.nombreTipoModalidadPaciente.setText(items.get(i).getNombre());
        this.tipoModalidadPacienteViewHolder.setTipoModalidadPaciente(items.get(i));
    }
}