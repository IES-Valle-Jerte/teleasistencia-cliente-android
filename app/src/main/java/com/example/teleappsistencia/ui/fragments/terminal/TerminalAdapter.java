package com.example.teleappsistencia.ui.fragments.terminal;

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
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoVivienda;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.paciente.ListarPacienteFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.ui.fragments.paciente.ConsultarPacienteFragment;
import com.example.teleappsistencia.ui.fragments.paciente.ModificarPacienteFragment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerminalAdapter extends RecyclerView.Adapter<TerminalAdapter.TerminalViewHolder> {
    private List<Terminal> items;
    private TerminalViewHolder terminalViewHolder;

    public static class TerminalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        private ImageButton imageButtonModificarTerminal;
        private ImageButton imageButtonVerTerminal;
        private ImageButton imageButtonBorrarTerminal;
        private TextView numTerminalCard;
        private TextView modoAccesoViviendaCard;
        private TextView titularTermianlCard;
        private TextView tipoViviendaCard;
        private Terminal terminal;

        public TerminalViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.imageButtonModificarTerminal = v.findViewById(R.id.imageButtonModificarTerminal);
            this.imageButtonVerTerminal = v.findViewById(R.id.imageButtonVerTerminal);
            this.imageButtonBorrarTerminal = v.findViewById(R.id.imageButtonBorrarTerminal);
            this.numTerminalCard = v.findViewById(R.id.numTerminalCard);
            this.modoAccesoViviendaCard = v.findViewById(R.id.modoAccesoViviendaCard);
            this.titularTermianlCard = v.findViewById(R.id.titularTermianlCard);
            this.tipoViviendaCard = v.findViewById(R.id.tipoViviendaCard);
        }

        public void setOnClickListeners() {
            this.imageButtonModificarTerminal.setOnClickListener(this);
            this.imageButtonVerTerminal.setOnClickListener(this);
            this.imageButtonBorrarTerminal.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            switch (view.getId()) {
                case R.id.imageButtonModificarTerminal:
                    ModificarTerminalFragment modificarTerminalFragment = ModificarTerminalFragment.newInstance(this.terminal);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, modificarTerminalFragment).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVerTerminal:
                    ConsultarTerminalFragment consultarTerminalFragment = ConsultarTerminalFragment.newInstance(this.terminal);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarTerminalFragment).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonBorrarTerminal:
                    accionBorrarTerminal();
                    break;
            }
        }

        private void accionBorrarTerminal() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<ResponseBody> call = apiService.deleteTerminal(String.valueOf(this.terminal.getId()), Constantes.BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, Constantes.TERMINAL_BORRADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                        recargarFragment();
                    } else {
                        Toast.makeText(context, Constantes.ERROR_AL_BORRAR_EL_TERMINAL, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

        public void setTerminal(Terminal terminal) {
            this.terminal = terminal;
        }

        private void recargarFragment() {
            MainActivity activity = (MainActivity) this.context;
            ListarTerminalFragment listarTerminalFragment = new ListarTerminalFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, listarTerminalFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public TerminalAdapter(List<Terminal> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TerminalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.terminal_card, viewGroup, false);
        terminalViewHolder = new TerminalViewHolder(v);
        return terminalViewHolder;
    }

    @Override
    public void onBindViewHolder(TerminalViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.setTerminal(items.get(i));
        viewHolder.numTerminalCard.setText("Nº Terminal: " +items.get(i).getNumeroTerminal());
        viewHolder.modoAccesoViviendaCard.setText(items.get(i).getModoAccesoVivienda());
        Paciente paciente = (Paciente) Utilidad.getObjeto(items.get(i).getTitular(), "Paciente");
        if (paciente != null) {
            viewHolder.titularTermianlCard.setText("Nº de expediente del titular: " + paciente.getNumeroExpediente());
        }
        TipoVivienda tipoVivienda = (TipoVivienda) Utilidad.getObjeto(items.get(i).getTipoVivienda(), "TipoVivienda");
        if (tipoVivienda != null) {
            viewHolder.tipoViviendaCard.setText("Tipo de vivienda: " + tipoVivienda.getNombre());
        }
    }


}
