package com.example.teleappsistencia.ui.fragments.tipo_centro_sanitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListarTipoCentroSanitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListarTipoCentroSanitario extends Fragment {

    // Declaración de atributos.
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List<TipoCentroSanitario> items;

    public FragmentListarTipoCentroSanitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentListarTipoCentroSanitario.
     */
    public static FragmentListarTipoCentroSanitario newInstance() {
        FragmentListarTipoCentroSanitario fragment = new FragmentListarTipoCentroSanitario();
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
        View root = inflater.inflate(R.layout.fragment_listar_tipo_centro_sanitario, container, false);

        // Obtener el Recycler.
        recycler = (RecyclerView) root.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Se llama al método que lista los tipos de centros sanitarios.
        listarTipoCentroSanitario();

        // Inflate the layout for this fragment
        return root;
    }

    /**
     * Método que lista los tipos de centros sanitarios.
     */
    private void listarTipoCentroSanitario() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<TipoCentroSanitario>> call = apiService.getTipoCentroSanitario(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoCentroSanitario>>() {
            @Override
            public void onResponse(Call<List<TipoCentroSanitario>> call, Response<List<TipoCentroSanitario>> response) {
                if (response.isSuccessful()) {
                    items = response.body();

                    // Crear un nuevo adaptador.
                    adapter = new TipoCentroSanitarioAdapter(items);
                    recycler.setAdapter(adapter);

                } else {
                    Toast.makeText(getContext(), Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoCentroSanitario>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}