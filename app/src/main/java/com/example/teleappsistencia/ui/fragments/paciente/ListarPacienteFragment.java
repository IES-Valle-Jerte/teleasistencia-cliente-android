package com.example.teleappsistencia.ui.fragments.paciente;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.ui.fragments.opciones_listas.OpcionesListaFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.Paciente;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Una clase {@link Fragment} para recoger los listar los pacientes.
 * <p> Esta clase es una subclase de {@link Fragment} y hereda de ella todos sus métodos y atributos.
 */
public class ListarPacienteFragment extends Fragment implements View.OnClickListener, OpcionesListaFragment.OnButtonClickListener,PacienteAdapter.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Atributos de la interfaz de usuario (UI) del fragment.
    private RecyclerView recycler;
    //private RecyclerView.Adapter adapter;
    private PacienteAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button botonNuevoPaciente;

    private SearchView searchView;
    private List<Paciente> filtroItemsBusqueda;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Posicion seleccionada en la lista
    private int selectedPosition = RecyclerView.NO_POSITION;

    // Lista de pacientes que se van a mostrar.
    static List<Object> lPacientes;
    private List<Paciente> listadoPacientes;

    // Constructor por defecto.
    public ListarPacienteFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ListarPacienteFragment newInstance(String param1, String param2) {
        ListarPacienteFragment fragment = new ListarPacienteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    /**
     * Estoy inflando un fragmento, obteniendo una vista de reciclador, configurando un administrador
     * de diseño y luego llamando a una función para obtener los datos y pasarlos al adaptador.
     * 
     * @param inflater El objeto LayoutInflater que se puede usar para inflar cualquier vista en el
     * fragmento,
     * @param container El padre al que se debe adjuntar la interfaz de usuario de este fragmento.
     * @param savedInstanceState Si el fragmento se vuelve a crear a partir de un estado guardado
     * anterior, este es el estado.
     * @return Se está devolviendo la vista.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_paciente, container, false);lPacientes = new ArrayList<>();
        //ListView listView = view.findViewById(R.id.listViewPacientes);

        //Obtenemos el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);
        //Obtenemos el botón
        botonNuevoPaciente=view.findViewById(R.id.buttonNuevoPaciente);
        botonNuevoPaciente.setOnClickListener(this);

        //Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        searchView = (SearchView) view.findViewById(R.id.search_view);
        crearFiltroBusqueda();
        //Obtenemos el layout con los datos de los pacientes y pasamos los datos al adaptador mientras mostramos la capa de espera
        ConstraintLayout dataConstraintLayout = (ConstraintLayout) view.findViewById(R.id.listViewDataPacientes);
        Utilidad.generarCapaEspera(view,dataConstraintLayout);

        listarPacientes(view,recycler);
        //Creamos el fragment inferior con las opciones de la lista
        OpcionesListaFragment myFragment = new OpcionesListaFragment();
        //Acciones de los botones del fragment
        myFragment.setOnButtonClickListener(this);
        //Cargmaos el fragment en el fragmentContainer
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerViewOpcionesListas, myFragment)
                .commit();

        return view;
    }

    /**
     * Método para obtener una lista de pacientes de la API para luego mostrarlos en un
     * RecyclerView
     * 
     * @param view Vista
     * @param recycler RecyclerView
     */

    private void listarPacientes(View view, RecyclerView recycler) {


        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<Object>> call = apiService.getPacientes(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    lPacientes = response.body();
                    listadoPacientes = (ArrayList<Paciente>) Utilidad.getObjeto(lPacientes, Constantes.AL_PACIENTE);

                    //Adaptador
                    adapter = new PacienteAdapter(listadoPacientes);
                    recycler.setAdapter(adapter);

                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_LISTAR_LAS_DIRECCIONES, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
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
                adapter = new PacienteAdapter(filtroItemsBusqueda);
                recycler.setAdapter(adapter);
                return true;
            }
        });
    }
    public void filtrarBusqueda(String secuencia) {
        String cadenaUsuario = secuencia.toLowerCase();
        String auxNombre;
        filtroItemsBusqueda = new ArrayList<>();

        if(cadenaUsuario.isEmpty()){
            filtroItemsBusqueda = listadoPacientes;
        }else{
            List<Paciente> filtroRecursos = new ArrayList<>();
            for(Paciente aux : listadoPacientes){
                Persona p= (Persona) Utilidad.getObjeto(aux.getPersona(),Constantes.PERSONA);
                auxNombre = Normalizer.normalize(p.getNombre(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                if(auxNombre.toLowerCase().contains(cadenaUsuario)){
                    filtroRecursos.add(aux);
                }
            }
            filtroItemsBusqueda = filtroRecursos;
        }
    }
    /**
     * Toma una lista de objetos LinkedTreeMap y establece el valor del atributo lPacientes.
     * 
     * @param listado Lista de objetos LinkedTreeMap
     */
    private static void fijarListado(List<Object> listado) {
        lPacientes = listado;
    }

    public void cargarFragmentInsertar(){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.main_fragment,new ViewPagerPacientesFragment());
        fragmentTransaction.replace(R.id.main_fragment,new DatosPersonalesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void cargarFragmentModificar(){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.main_fragment,new ViewPagerPacientesFragment());
        fragmentTransaction.replace(R.id.main_fragment,new DatosPersonalesFragment(adapter.getPacienteSelecionado()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonNuevoPaciente:
                cargarFragmentInsertar();
                break;
        }
    }

    @Override
    public void onViewDetailsButtonClicked() {
        showDetailsFragment();
    }

    @Override
    public void onDeleteButtonClicked() {
        accionBorrarPaciente();
    }


    @Override
    public void onEditButtonClicked() {
        cargarFragmentModificar();
    }

    @Override
    public void onItemSelected(int position) {
        selectedPosition = position;
    }
    private void showDetailsFragment() {
        //Pasar el paciente seleccionado al fragment
        AppCompatActivity activity = (AppCompatActivity) getContext();
        ConsultarPacienteFragment consultarPacienteFragment = ConsultarPacienteFragment.newInstance(adapter.getPacienteSelecionado());
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarPacienteFragment).addToBackStack(null).commit();
    }
    private void accionBorrarPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.deletePaciente(String.valueOf(adapter.getPacienteSelecionado().getId()), "Bearer " + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.PACIENTE_BORRADO_CORRECTAMENTE, Toast.LENGTH_SHORT).show();
                    recargarFragment();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_BORRAR_EL_PACIENTE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void recargarFragment() {
        MainActivity activity = (MainActivity) getContext();
        ListarPacienteFragment listarPacienteFragment = new ListarPacienteFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, listarPacienteFragment)
                .addToBackStack(null)
                .commit();
    }
}
