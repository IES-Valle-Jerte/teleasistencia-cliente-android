package com.example.teleappsistencia.ui.fragments.persona;

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
import com.example.teleappsistencia.ui.fragments.historico_tipo_situacion.ListarHistoricoTipoSituacionFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder> {

    private List<Persona> items;
    private PersonaViewHolder personaViewHolder;

    public static class PersonaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Campos respectivos de un item.
        public Context context;
        public TextView textView_nombreApellidos;
        public TextView textView_dni;
        public TextView textView_fechaNacimiento;
        private ImageButton imgBtn_modificar;
        private ImageButton imgBtn_ver;
        private ImageButton imgBtn_borrar;

        private Persona persona;

        public PersonaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            this.textView_nombreApellidos = v.findViewById(R.id.textView_nombreApellidos_persona);
            this.textView_dni = v.findViewById(R.id.textView_dni_persona);
            this.textView_fechaNacimiento = v.findViewById(R.id.textView_fechaNacimiento_persona);
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
                    // Llamar al Fragment ModificarPersonaFragment.
                    ModificarPersonaFragment fragmentModificar = ModificarPersonaFragment.newInstance(this.persona);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentModificar).addToBackStack(null).commit();
                    break;
                case R.id.imageButtonVer:
                    // Llamar al Fragment ConsultarPersonaFragment.
                    ConsultarPersonaFragment fragmentConsultar = ConsultarPersonaFragment.newInstance(this.persona);
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
                            borrarPersona();
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
         * Método que realiza una petición a la API para borrar una Persona.
         */
        private void borrarPersona() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

            Call<Response<String>> call = apiService.deletePersona(persona.getId(), Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if (response.isSuccessful()) {
                        Response<String> respuesta = response.body();
                        AlertDialogBuilder.crearInfoAlerDialog(context, Constantes.INFO_ALERTDIALOG_ELIMINADO_PERSONA);
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
            ListarPersonaFragment fragment = new ListarPersonaFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        public void setPersona(Persona persona) {
            this.persona = persona;
        }
    }

    public PersonaAdapter(List<Persona> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public PersonaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_card_persona, viewGroup, false);
        personaViewHolder = new PersonaViewHolder(v);
        return personaViewHolder;
    }

    @Override
    public void onBindViewHolder(PersonaViewHolder viewHolder, int i) {
        viewHolder.setOnClickListeners();
        viewHolder.textView_nombreApellidos.setText(items.get(i).getNombre() + Constantes.ESPACIO_EN_BLANCO + items.get(i).getApellidos());
        viewHolder.textView_dni.setText(items.get(i).getDni());
        viewHolder.textView_fechaNacimiento.setText(items.get(i).getFechaNacimiento());
        personaViewHolder.setPersona(items.get(i));
    }
}
