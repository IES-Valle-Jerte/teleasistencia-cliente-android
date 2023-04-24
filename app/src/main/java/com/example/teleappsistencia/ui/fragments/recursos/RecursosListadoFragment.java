package com.example.teleappsistencia.ui.fragments.recursos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.opciones_listas.OpcionesListaFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecursosListadoFragment extends Fragment implements View.OnClickListener, OpcionesListaFragment.OnButtonClickListener, RecursosAdapter.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Atributos de la interfaz de usuario (UI) del fragment.
    private RecyclerView recycler;
    private RecursosAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView tituloFragment;
    private SearchView searchView;
    private OpcionesListaFragment opcionesListaFragment;

    //private Button botonNuevoRecurso;

    //Posicion seleccionada en la lista
    private int selectedPosition = RecyclerView.NO_POSITION;

    private List<RecursoComunitario> items;

    private List<RecursoComunitario> filtroItemsMenu;

    private List<RecursoComunitario> filtroItemsBusqueda;

    // Objeto pasado desde el Main. (id, titulo)
    private Bundle bundle;
    private int id;
    private String titulo;

    // Constructor por defecto.
    public RecursosListadoFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static RecursosListadoFragment newInstance(String param1, String param2) {
        RecursosListadoFragment fragment = new RecursosListadoFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_listar_recursos, container, false);

        // Obtener el Bundle (Id y nombre de la opción pulsada del submenu)
        bundle = getArguments();
        id = (int) bundle.getSerializable(Constantes.KEY_ID_CLASIFICACION_RECURSOS);
        titulo = (String) bundle.getSerializable(Constantes.KEY_NOMBRE_CLASIFICACION_RECURSOS);

        // Obtener el titulo
        tituloFragment = (TextView) root.findViewById(R.id.textViewTituloRecursosComunitarios);
        tituloFragment.setText(Constantes.TITULO_RECURSOS+titulo);

        // Obtener el SearchView
        searchView = (SearchView) root.findViewById(R.id.search_view);

        // Obtener el Recycler.
        recycler = (RecyclerView) root.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Obtenemos las Opciones.
        opcionesListaFragment = new OpcionesListaFragment();
        opcionesListaFragment.setOnButtonClickListener(this);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, opcionesListaFragment).commit();


        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Se llama al método que lista los recursos comunitarios.
        listarRecursoComunitario();

        // Se llama al método que genera un filtro de busqueda con el SearchView
        crearFiltroBusqueda();

        // Inflate the layout for this fragment
        return root;
    }

    private void crearFiltroBusqueda() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filtrarBusqueda(s);
                adapter = new RecursosAdapter(filtroItemsBusqueda);
                recycler.setAdapter(adapter);
                return true;
            }
        });
    }

    private void listarRecursoComunitario() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<RecursoComunitario>> call = apiService.getRecursoComunitario(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<RecursoComunitario>>() {
            @Override
            public void onResponse(Call<List<RecursoComunitario>> call, Response<List<RecursoComunitario>> response) {
                if (response.isSuccessful()) {
                    items = response.body();
                    filtroItemsMenu = new ArrayList<>();

                    /*
                    Filtro para saber que Recursos son los que tenemos que mostrar
                    según su id en la clasificación de recursos (Submenu).
                     */
                    for (RecursoComunitario aux : items) {
                        TipoRecursoComunitario auxTRC = (TipoRecursoComunitario) Utilidad.getObjeto(aux.getTipoRecursoComunitario(), Constantes.TIPO_RECURSO_COMUNITARIO);
                        if(auxTRC.getId_clasificacion_recurso_comunitario() == id){
                            filtroItemsMenu.add(aux);
                        }
                    }

                    // Crear un nuevo adaptador.
                    adapter = new RecursosAdapter(filtroItemsMenu);
                    recycler.setAdapter(adapter);

                } else {
                    Toast.makeText(getContext(), Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecursoComunitario>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void cargarFragmentInsertar(){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.main_fragment,new ViewPagerPacientesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case 1 /*R.id.buttonNuevoPaciente*/:
                cargarFragmentInsertar();
                break;
        }
    }

    @Override
    public void onViewDetailsButtonClicked() {
        int idBotonConsultar = 1;
        //Pasar el recurso seleccionado al fragment
        if(adapter.getRecursoSeleccionado() != null){
            this.searchView.setQuery("", false);
            AppCompatActivity activity = (AppCompatActivity) getContext();
            RecursosOpcionesFragment consultarRecursoFragment = RecursosOpcionesFragment.newInstance(adapter.getRecursoSeleccionado(), idBotonConsultar);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarRecursoFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onEditButtonClicked() {
        int idBotonEditar = 2;
        //Pasar el recurso seleccionado al fragment
        if(adapter.getRecursoSeleccionado() != null){
            this.searchView.setQuery("", false);
            AppCompatActivity activity = (AppCompatActivity) getContext();
            RecursosOpcionesFragment consultarRecursoFragment = RecursosOpcionesFragment.newInstance(adapter.getRecursoSeleccionado(), idBotonEditar);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarRecursoFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDeleteButtonClicked() {

    }

    @Override
    public void onItemSelected(int position) {
        selectedPosition = position;
    }


    /**
     * Método para generar el filtro de busqueda con el SearchView en el Listado.
     *
     * @return
     */
    public void filtrarBusqueda(String secuencia) {
        String cadenaUsuario = secuencia.toLowerCase();
        String auxNombre;
        filtroItemsBusqueda = new ArrayList<>();

        if(cadenaUsuario.isEmpty()){
            filtroItemsBusqueda = filtroItemsMenu;
        }else{
            List<RecursoComunitario> filtroRecursos = new ArrayList<>();
            for(RecursoComunitario aux : filtroItemsMenu){
                // Guarda en la variable "auxNombre" el nombre del Recurso sin acentos.
                auxNombre = Normalizer.normalize(aux.getNombre(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                if(auxNombre.toLowerCase().contains(cadenaUsuario)){
                    filtroRecursos.add(aux);
                }
            }
            filtroItemsBusqueda = filtroRecursos;
        }
    }

}
