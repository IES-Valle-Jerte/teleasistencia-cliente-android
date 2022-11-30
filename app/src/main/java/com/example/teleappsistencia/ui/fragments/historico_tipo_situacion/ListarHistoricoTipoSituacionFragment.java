package com.example.teleappsistencia.ui.fragments.historico_tipo_situacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.modelos.HistoricoTipoSituacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarHistoricoTipoSituacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarHistoricoTipoSituacionFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private List<HistoricoTipoSituacion> items;

    public ListarHistoricoTipoSituacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListarHistoricoTipoSituacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarHistoricoTipoSituacionFragment newInstance() {
        ListarHistoricoTipoSituacionFragment fragment = new ListarHistoricoTipoSituacionFragment();
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
        View view = inflater.inflate(R.layout.fragment_listar_historico_tipo_situacion, container, false);

        // Obtener el Recycler.
        recycler = view.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        listarHistoricoTipoSituacion();
        return view;
    }

    /**
     * Método que realiza una petición a la API y recoge todos los HistoricoTipoSituaciones.
     * Añadiendo también el adapter de los HistoricoTipoSituaciones.
     */
    private void listarHistoricoTipoSituacion() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<HistoricoTipoSituacion>> call = apiService.getHistoricoTipoSituacion(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<HistoricoTipoSituacion>>() {
            @Override
            public void onResponse(Call<List<HistoricoTipoSituacion>> call, Response<List<HistoricoTipoSituacion>> response) {
                if (response.isSuccessful()) {
                    items = response.body();

                    // Crear un nuevo adaptador.
                    adapter = new HistoricoTipoSituacionAdapter(items);
                    recycler.setAdapter(adapter);
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<HistoricoTipoSituacion>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }
}