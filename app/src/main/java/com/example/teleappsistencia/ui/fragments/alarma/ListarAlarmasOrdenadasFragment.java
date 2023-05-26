package com.example.teleappsistencia.ui.fragments.alarma;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Contacto;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.gestionAlarmasFragments.GestionAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.opciones_listas.OpcionesListaFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarAlarmasOrdenadasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarAlarmasOrdenadasFragment extends Fragment implements View.OnClickListener, OpcionesListaFragment.OnButtonClickListener, AlarmaAdapter.OnItemSelectedListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Alarma> lAlarmas;
    private RecyclerView recycler;
    private AlarmaAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button btn_add_alerta;

    private Context context;

    private String mParam1;
    private String mParam2;

    private int selectedPosition = RecyclerView.NO_POSITION;
    private SearchView searchView;

    private List<Alarma> filtroItemsBusqueda;

    public ListarAlarmasOrdenadasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListarAlarmasFragment.
     */
    public static ListarAlarmasOrdenadasFragment newInstance(String param1, String param2) {
        ListarAlarmasOrdenadasFragment fragment = new ListarAlarmasOrdenadasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listar_alarmas_ordenadas, container, false);
        this.searchView = (SearchView) root.findViewById(R.id.search_view);
        crearFiltroBusqueda();

        // Mostrar botón nueva para administradores
        this.btn_add_alerta = (Button) root.findViewById(R.id.btn_add_alarma);
        this.btn_add_alerta.setOnClickListener(this);
        this.context = root.getContext();

        if(Utilidad.isAdmin()){ // Se muestra el botón solo para administradores
            this.btn_add_alerta.setVisibility(View.VISIBLE);
            this.btn_add_alerta.setEnabled(true);
        }else{
            this.btn_add_alerta.setVisibility(View.INVISIBLE);
            this.btn_add_alerta.setEnabled(false);
        }

        // Obtener el Recycler.
        recycler = (RecyclerView) root.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        //Cargamos un adaptador vacío mientras se carga la lista desde la API REST
        this.lAlarmas = new ArrayList<>();
        adapter = new AlarmaAdapter(lAlarmas);
        recycler.setAdapter(adapter);

        //Cargamos lista desde la API REST
        cargarLista();

        OpcionesListaFragment myFragment = new OpcionesListaFragment();
        myFragment.setOnButtonClickListener(this);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerViewOpciones, myFragment)
                .commit();


        return root;
    }

    private void crearFiltroBusqueda() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            // Filtra los items y se los pasa al Recycler.
            @Override
            public boolean onQueryTextChange(String s) {
                filtrarBusqueda(s);
                adapter = new AlarmaAdapter(filtroItemsBusqueda);
                recycler.setAdapter(adapter);
                return true;
            }
        });
    }

    public void filtrarBusqueda(String secuencia) {
        String secuenciaBuscada = secuencia.toLowerCase();
        String auxNombre, auxNombreTeleoperador;
        filtroItemsBusqueda = new ArrayList<>();

        if(secuenciaBuscada.isEmpty()){
            filtroItemsBusqueda = lAlarmas;
        }else{
            List<Alarma> filtroAlarmas = new ArrayList<>();
            for(Alarma aux : lAlarmas){
                Terminal terminal;
                Paciente paciente;
                Persona persona;

                if(aux.getId_paciente_ucr() != null){
                    paciente = (Paciente) Utilidad.getObjeto(aux.getId_paciente_ucr(), Constantes.PACIENTE);
                    terminal = (Terminal) Utilidad.getObjeto(paciente.getTerminal(), Constantes.TERMINAL);

                }
                else{
                    terminal = (Terminal) Utilidad.getObjeto(aux.getId_terminal(), Constantes.TERMINAL);
                    paciente = (Paciente) Utilidad.getObjeto(terminal.getTitular(), Constantes.PACIENTE);
                }
                TipoAlarma tipoAlarmaAux = (TipoAlarma) (Utilidad.getObjeto(aux.getId_tipo_alarma(), Constantes.TIPO_ALARMA));
                persona = (Persona) Utilidad.getObjeto(paciente.getPersona(), Constantes.PERSONA);
                auxNombre = Normalizer.normalize(tipoAlarmaAux.getNombre(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

                Teleoperador teleoperador = (Teleoperador) (Utilidad.getObjeto(aux.getId_teleoperador(), Constantes.TELEOPERADOR));
                if(teleoperador != null){ // evitar errores si no hay teleoperador en la alarma
                    auxNombreTeleoperador = teleoperador.getFirstName() + Constantes.ESPACIO_EN_BLANCO + teleoperador.getLastName();
                }else{
                    auxNombreTeleoperador = Constantes.ESPACIO_EN_BLANCO;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(aux.getFecha_registro());

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String formattedTime = sdf.format(aux.getFecha_registro());

                if(aux.getId_paciente_ucr() != null){
                    if(auxNombre.toLowerCase().contains(secuenciaBuscada) || formattedTime.toLowerCase().contains(secuenciaBuscada) || paciente.toString().toLowerCase().contains(secuenciaBuscada) || auxNombreTeleoperador.toLowerCase().contains(secuenciaBuscada)){
                        filtroAlarmas.add(aux);
                    }
                }else{
                    if(auxNombre.toLowerCase().contains(secuenciaBuscada) || formattedTime.toLowerCase().contains(secuenciaBuscada) || auxNombreTeleoperador.toLowerCase().contains(secuenciaBuscada)){
                        filtroAlarmas.add(aux);
                    }
                }

            }
            filtroItemsBusqueda = filtroAlarmas;
        }
    }

    /**
     * Este método carga la lista de alarmas que vamos a mostrar de forma ordenada desde la API REST y una vez que se ha cargada
     * se las añade al adapter y se las cargamos al recycler.
     */
    private void cargarLista(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getAlarmas(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lAlarmas = (ArrayList<Alarma>) Utilidad.getObjeto(lObjetos, Constantes.AL_ALARMA);
                    Collections.sort(lAlarmas); // Se ordena la lista de alarmas por estado, en caso de ser igual se ordena por fecha y hora de registro
                    adapter = new AlarmaAdapter(lAlarmas);
                    recycler.setAdapter(adapter);
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_alarma: // Muestra un fragment con el formulario para crear la alarma
                mostrarCrearAlarma();
                break;
        }
    }

    // Muestra el formulario de creación de alarmas
    public void mostrarCrearAlarma(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_fragment, new InsertarAlarmaFragment());

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Estos métodos son los que hacen las funciones del layout de interación con los cardviews
    @Override
    public void onViewDetailsButtonClicked() {
        if(this.adapter.getAlarmaSeleccionada()!=null){
            showAlarmDetails();
        }
    }

    @Override
    public void onDeleteButtonClicked() {
        if(this.adapter.getAlarmaSeleccionada()!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(Constantes.DIALOGO_CONFIRMAR_ALARMA)
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            borrarAlarma();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onEditButtonClicked() {
        if(this.adapter.getAlarmaSeleccionada()!=null){
            if(this.adapter.getAlarmaSeleccionada().getEstado_alarma().equals(Constantes.CERRADA)){
                Toast.makeText(context, Constantes.ALARMA_YA_CERRADA, Toast.LENGTH_SHORT).show();
            }else{
                modificarAlarma();
            }
        }

    }

    public void showAlarmDetails(){
        Paciente paciente;
        Persona persona;
        Terminal terminal;
        int color;

        Alarma alarmaNotificada = adapter.getAlarmaSeleccionada();

        if(alarmaNotificada.getId_paciente_ucr() != null){
            color = getResources().getColor(R.color.azul, getActivity().getTheme());
            paciente = (Paciente) Utilidad.getObjeto(alarmaNotificada.getId_paciente_ucr(), Constantes.PACIENTE);
            terminal = (Terminal) Utilidad.getObjeto(paciente.getTerminal(), Constantes.TERMINAL);

        }
        else{
            color = getResources().getColor(R.color.verde, getActivity().getTheme());
            terminal = (Terminal) Utilidad.getObjeto(alarmaNotificada.getId_terminal(), Constantes.TERMINAL);
            paciente = (Paciente) Utilidad.getObjeto(terminal.getTitular(), Constantes.PACIENTE);
        }
        persona = (Persona) Utilidad.getObjeto(paciente.getPersona(), Constantes.PERSONA);

        //extraerContactos(paciente, alarmaNotificada, persona, terminal, color);

        AppCompatActivity activity = (AppCompatActivity) getContext();
        GestionAlarmaFragment gestionAlarmaFragment = GestionAlarmaFragment.newInstance(alarmaNotificada, persona.getNombre(),persona.getTelefonoMovil(),new ArrayList<Object>(), paciente, terminal, color);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, gestionAlarmaFragment)
                .addToBackStack(null)
                .commit();

    }

   /* public void extraerContactos(Paciente paciente, Alarma alarmaNotificada, Persona persona, Terminal terminal, int color){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getContactosbyIdPaciente(paciente.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                List<Object> lObjectAux;
                if(response.isSuccessful()){
                    lObjectAux = response.body();
                    lContactos = (ArrayList<Object>) lObjectAux;

                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    GestionAlarmaFragment gestionAlarmaFragment = GestionAlarmaFragment.newInstance(alarmaNotificada, persona.getNombre(), persona.getTelefonoMovil(), lContactos, paciente, terminal, color);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, gestionAlarmaFragment)
                            .addToBackStack(null)
                            .commit();
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    public void modificarAlarma(){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        ConsultarAlarmaFragment consultarAlarmaFragment = ConsultarAlarmaFragment.newInstance(adapter.getAlarmaSeleccionada());
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, consultarAlarmaFragment)
                .addToBackStack(null)
                .commit();
    }

    public void borrarAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.deleteAlarmabyId(this.adapter.getAlarmaSeleccionada().getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, Constantes.ALARMA_BORRADA, Toast.LENGTH_LONG).show();
                    volver();
                }else{
                    Toast.makeText(context, Constantes.ERROR_BORRADO + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void volver(){
        MainActivity activity = (MainActivity) this.context;
        ListarAlarmasOrdenadasFragment listarAlarmasOrdenadasFragment = new ListarAlarmasOrdenadasFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarAlarmasOrdenadasFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemSelected(int position) {
        this.selectedPosition = position;
    }


}