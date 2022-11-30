package com.example.teleappsistencia.ui.fragments.recurso_comunitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListarRecursoComunitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListarRecursoComunitario extends Fragment {

    // Declaración de atributos.
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List<RecursoComunitario> items;

    public FragmentListarRecursoComunitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentListarRecursoComunitario.
     */
    public static FragmentListarRecursoComunitario newInstance() {
        FragmentListarRecursoComunitario fragment = new FragmentListarRecursoComunitario();
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
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_listar_recurso_comunitario, container, false);

        // Obtener el Recycler.
        recycler = (RecyclerView) root.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Se llama al método que lista los recursos comunitarios.
        listarRecursoComunitario();

        // Inflate the layout for this fragment
        return root;
    }

    /**
     * Método que lista los recursos comunitarios.
     */
    private void listarRecursoComunitario() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<RecursoComunitario>> call = apiService.getRecursoComunitario(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<RecursoComunitario>>() {
            @Override
            public void onResponse(Call<List<RecursoComunitario>> call, Response<List<RecursoComunitario>> response) {
                if (response.isSuccessful()) {
                    items = response.body();

                    // Crear un nuevo adaptador.
                    adapter = new RecursoComunitarioAdapter(items);
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
}