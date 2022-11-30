package com.example.teleappsistencia.ui.fragments.relacion_paciente_persona;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.modelos.RelacionPacientePersona;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarRelacionPacientePersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarRelacionPacientePersonaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static List<LinkedTreeMap> lRelacionPacientePersona;


    public ListarRelacionPacientePersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListarPacienteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarRelacionPacientePersonaFragment newInstance(String param1, String param2) {
        ListarRelacionPacientePersonaFragment fragment = new ListarRelacionPacientePersonaFragment();
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
        View view = inflater.inflate(R.layout.fragment_listar_relacion_paciente_persona, container, false);
        lRelacionPacientePersona = new ArrayList<>();
        //ListView listView = view.findViewById(R.id.listViewPacientes);

        //Obtenemos el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        //Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        //Obtenemos los datos de Relación Paciente Persona y pasamos los datos al adaptador mientras mostramos la capa de espera
        ConstraintLayout dataConstraintLayout = (ConstraintLayout) view.findViewById(R.id.listViewDataRelacionPacientePersona);
        Utilidad.generarCapaEspera(view, dataConstraintLayout);
        listarPacientes(view, recycler);

        return view;
    }

    private void listarPacientes(View view, RecyclerView recycler) {


        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<LinkedTreeMap>> call = apiService.getListadoPacientesPersona(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<LinkedTreeMap>>() {
            @Override
            public void onResponse(Call<List<LinkedTreeMap>> call, Response<List<LinkedTreeMap>> response) {
                if(response.isSuccessful()){
                    lRelacionPacientePersona = response.body();
                    List<RelacionPacientePersona> listadoRelacionPacientePersona = new ArrayList<>();
                    for (LinkedTreeMap relacionPacientePersona : lRelacionPacientePersona) {
                        listadoRelacionPacientePersona.add((RelacionPacientePersona) Utilidad.getObjeto(relacionPacientePersona, "RelacionPacientePersonaViewholder"));
                    }
                    //Pasamos los datos al adaptador
                    adapter = new RelacionPacientePersonaAdapter(listadoRelacionPacientePersona);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<LinkedTreeMap>> call, Throwable t) {

            }
        });
    }

    private static void fijarListado(List<LinkedTreeMap> listado) {
        lRelacionPacientePersona = listado;
    }

}
