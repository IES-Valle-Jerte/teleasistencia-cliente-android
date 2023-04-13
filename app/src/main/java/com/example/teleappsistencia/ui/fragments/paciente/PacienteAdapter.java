package com.example.teleappsistencia.ui.fragments.paciente;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Una clase {@link RecyclerView.Adapter} para recoger los listar los pacientes.
 * <p> Esta clase es una subclase de {@link RecyclerView.Adapter} y hereda de ella todos sus métodos y atributos.
 */
public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder> {

    // La lista de objetos Paciente a mostrar dentro del RecyclerView.
    private List<Paciente> items;
    private PacienteViewHolder pacienteViewHolder;
    //Item seleccionado
    private Paciente pacienteSelecionado;
    //Interfaz que establece el elemento sleccionado
    private OnItemSelectedListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    /**
     * Una clase {@link RecyclerView.ViewHolder} para recoger los datos a mostrar dentro de la tarjeta Paciente del RecyclerView.
     * <p> Esta clase es una subclase de {@link RecyclerView.ViewHolder} y hereda de ella todos sus métodos y atributos.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
    //Devuelve el paciete seleccionado
    public Paciente getPacienteSelecionado() {
        return pacienteSelecionado;
    }

    // Define el método getItemAtPosition para obtener el elemento en la posición indicada
    public Paciente getItemAtPosition(int position) {
        return items.get(position);
    }
    public static class PacienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Contexto de la aplicación.
        public Context context;

        // Atributos de la tarjeta en la interfaz de usuario (UI).
        private ImageButton imageButtonModificarPaciente;
        private ImageButton imageButtonVerPaciente;
        private ImageButton imageButtonBorrarPaciente;
        private TextView numSeguridadSocialCard;
        private TextView numeroExpedienteCard;
        private TextView tieneUCRCard;
        private TextView tipoModalidadPacienteCard;

        // Atributo Paciente que se muestra en la tarjeta.
        private Paciente paciente;

        /**
         * Constructor de la clase {@link PacienteViewHolder}.
         *
         * @param v Vista de la tarjeta.
         */
        public PacienteViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.imageButtonModificarPaciente = v.findViewById(R.id.imageButtonModificarPaciente);
            this.imageButtonVerPaciente = v.findViewById(R.id.imageButtonVerPaciente);
            this.imageButtonBorrarPaciente = v.findViewById(R.id.imageButtonBorrarPaciente);
            this.numSeguridadSocialCard = v.findViewById(R.id.numSeguridadSocialCard);
            this.numeroExpedienteCard = v.findViewById(R.id.numeroExpedienteCard);
            this.tieneUCRCard = v.findViewById(R.id.tieneUCRCard);
            this.tipoModalidadPacienteCard = v.findViewById(R.id.tipoModalidadPacienteCard);
        }


        /**
         * Esta función establece onClickListeners para los imageButtons en el RecyclerView
         */
        public void setOnClickListeners() {
            this.imageButtonModificarPaciente.setOnClickListener(this);
            this.imageButtonVerPaciente.setOnClickListener(this);
            this.imageButtonBorrarPaciente.setOnClickListener(this);
        }

        /**
         * Recogemos los onClickListeners de los imageButtons en el RecyclerView
         *
         * @param view La vista en la que se hizo clic.
         */
        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            switch (view.getId()) {
                // Modificar paciente.
                case R.id.imageButtonModificarPaciente:
                    ModificarPacienteFragment modificarPacienteFragment = ModificarPacienteFragment.newInstance(this.paciente);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, modificarPacienteFragment).addToBackStack(null).commit();
                    break;
                // Ver paciente.
                case R.id.imageButtonVerPaciente:
                    ConsultarPacienteFragment consultarPacienteFragment = ConsultarPacienteFragment.newInstance(this.paciente);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarPacienteFragment).addToBackStack(null).commit();
                    break;
                // Borrar paciente.
                case R.id.imageButtonBorrarPaciente:
                    accionBorrarPaciente();
                    break;
            }
        }

        private void accionBorrarPaciente() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deletePaciente(String.valueOf(this.paciente.getId()), "Bearer " + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.PACIENTE_BORRADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                        recargarFragment();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_AL_BORRAR_EL_PACIENTE, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

        private void recargarFragment() {
            MainActivity activity = (MainActivity) this.context;
            ListarPacienteFragment listarPacienteFragment = new ListarPacienteFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, listarPacienteFragment)
                    .addToBackStack(null)
                    .commit();
        }

        /**
         * Establece los datos del paciente a mostrar en la tarjeta.
         *
         * @param paciente El paciente a mostrar.
         */
        public void setPaciente(Paciente paciente) {
            this.paciente = paciente;
        }
    }

    /**
     * Constructor de la clase {@link PacienteAdapter}.
     *
     * @param items La lista de objetos Paciente a mostrar dentro del RecyclerView.
     */
    public PacienteAdapter(List<Paciente> items) {
        this.items = items;
    }


    /**
     * La función devuelve el número de elementos de la lista.
     *
     * @return El número de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Se llama a OnCreateViewHolder() cuando RecyclerView necesita un nuevo RecyclerView.ViewHolder
     * del tipo dado para representar un elemento.
     *
     * @param viewGroup El ViewGroup en el que se agregará la nueva vista después de vincularla a una
     *                  posición de adaptador.
     * @param i         El tipo de vista de la nueva Vista.
     * @return El objeto ViewHolder.
     */

    @Override
    public PacienteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_paciente_card, viewGroup, false);
        pacienteViewHolder = new PacienteViewHolder(v);
        return pacienteViewHolder;
    }


    /**
     * Método que configura el texto a mostrar dentro de la CardView.
     *
     * @param viewHolder El ViewHolder que debe actualizarse para representar el contenido del elemento
     *                   en la posición dada en el conjunto de datos.
     * @param i          La posición del elemento en el conjunto de datos del adaptador.
     */
    @Override
    public void onBindViewHolder(PacienteViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.setPaciente(items.get(i));
        viewHolder.numSeguridadSocialCard.setText(items.get(i).getNumeroSeguridadSocial());
        viewHolder.numeroExpedienteCard.setText("Exp:" + items.get(i).getNumeroExpediente());
        if (items.get(i).isTieneUcr()) {
            viewHolder.tieneUCRCard.setText("El paciente tiene UCR");
            viewHolder.tieneUCRCard.setTextColor(Color.RED);
        } else {
            viewHolder.tieneUCRCard.setText("El paciente no tiene UCR");
            viewHolder.tieneUCRCard.setTextColor(Color.GREEN);
        }
        TipoModalidadPaciente tipoModalidadPaciente = (TipoModalidadPaciente) Utilidad.getObjeto(items.get(i).getTipoModalidadPaciente(), "TipoModalidadPaciente");
        if (tipoModalidadPaciente != null) {
            viewHolder.tipoModalidadPacienteCard.setText(tipoModalidadPaciente.getNombre());
        } else {
            viewHolder.tipoModalidadPacienteCard.setText("");
        }

        // Establecer un click listener para ViewHolder
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar la posición seleccionada y notificar al adapter
                int previousSelectedPosition = selectedPosition;
                selectedPosition = viewHolder.getAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);

                // Llamar al método onItemSelected de OnItemSelectedListener
                if (listener != null) {
                    listener.onItemSelected(selectedPosition);
                }
                pacienteSelecionado= viewHolder.paciente;
            }
        });

        // Establecer el color de fondo del ViewHolder en función de si está seleccionado o no
        if (selectedPosition == i) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.azul));
        } else {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), android.R.color.white));
        }
    }

}
