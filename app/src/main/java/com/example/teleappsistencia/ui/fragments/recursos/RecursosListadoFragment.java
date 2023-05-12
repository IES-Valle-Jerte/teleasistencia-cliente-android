package com.example.teleappsistencia.ui.fragments.recursos;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecursosListadoFragment extends Fragment implements View.OnClickListener, OpcionesListaFragment.OnButtonClickListener, RecursosAdapter.OnItemSelectedListener {

    // Atributos de la interfaz de usuario (UI) del fragment.
    private RecyclerView recycler;
    private RecursosAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView tituloFragment;
    private SearchView searchView;
    private OpcionesListaFragment opcionesListaFragment;

    private Button botonNuevoRecurso;

    //Posicion seleccionada en la lista
    private int selectedPosition = RecyclerView.NO_POSITION;

    //Recurso seleccionado
    private RecursoComunitario recursoComunitarioSeleccionado;

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
        tituloFragment.setText(titulo);

        // Obtenemos el boton de Nuevo
        botonNuevoRecurso = (Button) root.findViewById(R.id.buttonNuevoRecurso);
        botonNuevoRecurso.setOnClickListener(this);

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

    /**
     * Método que crea un filtro de busqueda utilizando el SearchView.
     */
    private void crearFiltroBusqueda() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            /*
            Cada vez que se ingresa una nueva letra (o se borra) se llama al método filtrarBusqueda
            con un nuevo String para realizar la busqueda en la base de datos y filtrar los resiltados.
             */
            @Override
            public boolean onQueryTextChange(String s) {
                filtrarBusqueda(s);
                adapter = new RecursosAdapter(filtroItemsBusqueda);
                recycler.setAdapter(adapter);
                return true;
            }
        });
    }

    /**
     * Método que lista los recursos comunitarios según la clasificación de recursos
     * elegida en el submenu principal. Genera un nuevo adaptardor y se lo pasa al
     * recycler para que lo muestre.
     */
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

    /**
     * Método que carga el fragment RecursosOpcionesFragment con los valores necesarios
     * para generar un nuevo recurso comunitario.
     */
    private void cargarFragmentInsertar(){
        this.searchView.setQuery(Constantes.STRING_VACIO, false);
        AppCompatActivity activity = (AppCompatActivity) getContext();
        RecursosOpcionesFragment nuevoRecursoFragment = RecursosOpcionesFragment.newInstance(Constantes.NUEVO, this.id);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, nuevoRecursoFragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonNuevoRecurso:
                cargarFragmentInsertar();
                break;
        }
    }

    @Override
    public void onViewDetailsButtonClicked() {
        recursoComunitarioSeleccionado = adapter.getRecursoSeleccionado();
        //Pasar el recurso seleccionado al fragment
        if(recursoComunitarioSeleccionado != null){
            this.searchView.setQuery(Constantes.STRING_VACIO, false);
            AppCompatActivity activity = (AppCompatActivity) getContext();
            RecursosOpcionesFragment consultarRecursoFragment = RecursosOpcionesFragment.newInstance(recursoComunitarioSeleccionado, Constantes.CONSULTAR, this.id);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarRecursoFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onEditButtonClicked() {
        recursoComunitarioSeleccionado = adapter.getRecursoSeleccionado();
        //Pasar el recurso seleccionado al fragment
        if(recursoComunitarioSeleccionado != null){
            this.searchView.setQuery(Constantes.STRING_VACIO, false);
            AppCompatActivity activity = (AppCompatActivity) getContext();
            RecursosOpcionesFragment consultarRecursoFragment = RecursosOpcionesFragment.newInstance(recursoComunitarioSeleccionado, Constantes.MODIFICAR, this.id);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, consultarRecursoFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDeleteButtonClicked() {
        recursoComunitarioSeleccionado = adapter.getRecursoSeleccionado();
        if(recursoComunitarioSeleccionado != null){
            this.searchView.setQuery(Constantes.STRING_VACIO, false);
            AlertDialogBuilder.crearBorrarAlerDialog(getContext(), Constantes.ELIMINAR_ELEMENTO, new AlertDialogBuilder.AlertDialogListener() {
                // Que sucede cuando el usuario pulsa Si.
                @Override
                public void onPositiveButtonClicked() {
                    // Borra el recurso comunitario en la base de datos.
                    borrarRecurso(recursoComunitarioSeleccionado);
                    // Refresca la lista de recursos comunitario que se muestra.
                    listarRecursoComunitario();
                }

                // Que sucede cuando el usuario punta No.
                @Override
                public void onNegativeButtonClicked() {
                    // No hace nada.
                }
            });
        }
    }

    @Override
    public void onItemSelected(int position) {
        selectedPosition = position;
    }


    /**
     * Método para generar el filtro de busqueda con el SearchView en el Listado.
     * @return
     */
    private void filtrarBusqueda(String secuencia) {
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

    /**
     * Método para borrar el recurso comunitario seleccionado utilizando una petición a la API.
     * @param recursoComunitario: Recurso comunitario que se va a borrar.
     */
    private void borrarRecurso(RecursoComunitario recursoComunitario) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Response<String>> call = apiService.deleteRecursoComunitario(recursoComunitario.getId(), Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                if(response.isSuccessful()){
                    Object string = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_ELIMINADO_RECURSO);
                }else{
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }
}
