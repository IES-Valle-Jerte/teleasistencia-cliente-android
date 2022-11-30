package com.example.teleappsistencia.ui.fragments.dispositivos_aux;

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
import com.example.teleappsistencia.modelos.DispositivoAuxiliar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarDispositivosAuxiliaresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarDispositivosAuxiliaresFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private List<DispositivoAuxiliar> items;

    public ListarDispositivosAuxiliaresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListarDispositivosAuxiliaresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarDispositivosAuxiliaresFragment newInstance() {
        ListarDispositivosAuxiliaresFragment fragment = new ListarDispositivosAuxiliaresFragment();
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
        View view = inflater.inflate(R.layout.fragment_listar_dispositivos_auxiliares, container, false);

        // Obtener el Recycler.
        recycler = view.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        listarDispositivosAuxiliares();
        return view;
    }

    /**
     * Método que realiza una petición a la API y recoge todos los DispositivosAuxiliares.
     * Añadiendo también el adapter de los DispositivosAuxiliares.
     */
    private void listarDispositivosAuxiliares() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<DispositivoAuxiliar>> call = apiService.getDispositivosAuxiliares(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<DispositivoAuxiliar>>() {
            @Override
            public void onResponse(Call<List<DispositivoAuxiliar>> call, Response<List<DispositivoAuxiliar>> response) {
                if (response.isSuccessful()) {
                    items = response.body();

                    // Crear un nuevo adaptador.
                    adapter = new DispositivosAuxiliaresAdapter(items);
                    recycler.setAdapter(adapter);
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<DispositivoAuxiliar>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }
}