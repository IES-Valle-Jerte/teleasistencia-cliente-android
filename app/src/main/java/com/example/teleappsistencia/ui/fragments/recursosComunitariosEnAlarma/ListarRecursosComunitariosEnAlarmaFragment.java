package com.example.teleappsistencia.ui.fragments.recursosComunitariosEnAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.PersonaContactoEnAlarma;
import com.example.teleappsistencia.modelos.RecursoComunitarioEnAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.personaContactoEnAlarma.PersonaContactoEnAlarmaAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarRecursosComunitariosEnAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarRecursosComunitariosEnAlarmaFragment extends Fragment {

    private ArrayList<RecursoComunitarioEnAlarma> lRecursoComunitarioEnAlarma;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    public ListarRecursosComunitariosEnAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListarRecursosComunitariosEnAlarmaFragment.
     */

    public static ListarRecursosComunitariosEnAlarmaFragment newInstance() {
        ListarRecursosComunitariosEnAlarmaFragment fragment = new ListarRecursosComunitariosEnAlarmaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_listar_recursos_comunitarios_en_alarma, container, false);

        // Obtener el Recycler.
        recycler = (RecyclerView) root.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        //Cargamos un adaptador vacío mientras se carga la lista desde la API REST
        this.lRecursoComunitarioEnAlarma = new ArrayList<>();
        adapter = new RecursoComunitarioEnAlarmaAdapter(lRecursoComunitarioEnAlarma);
        recycler.setAdapter(adapter);

        //Cargamos lista desde la API REST
        cargarLista();

        return root;
    }

    /**
     * Este método carga la lista que vamos a mostrar desde la API REST y una vez que se ha cargada
     * se las añade al adapter y se las cargamos al recycler.
     */
    private void cargarLista(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getRecursosComunitariosEnAlarma(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lRecursoComunitarioEnAlarma = (ArrayList<RecursoComunitarioEnAlarma>) Utilidad.getObjeto(lObjetos, Constantes.AL_RECURSOS_COMUNITARIOS_EN_ALARMA);
                    adapter = new RecursoComunitarioEnAlarmaAdapter(lRecursoComunitarioEnAlarma);
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
}