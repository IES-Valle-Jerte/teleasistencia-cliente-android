package com.example.teleappsistencia.ui.fragments.acercaDe;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Convocatoria;
import com.example.teleappsistencia.modelos.Desarrollador;
import com.example.teleappsistencia.modelos.DesarrolladorRel;
import com.example.teleappsistencia.modelos.Desarrollador_tecnologia;
import com.example.teleappsistencia.modelos.Tecnologia;
import com.example.teleappsistencia.modelos.TecnologiaRel;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DesarrolladorCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DesarrolladorCardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<TecnologiaRel> lTecnologiaRel;
    private RecyclerView recycler;
    private DesarrolladorAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView nombre;
    private String nombreString;

    public DesarrolladorCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment alumnosCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DesarrolladorCardFragment newInstance(String param1, String param2) {
        DesarrolladorCardFragment fragment = new DesarrolladorCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_desarrollador_card, container, false);

        // Obtener el Recycler.
        recycler = (RecyclerView) root.findViewById(R.id.recycler_view_tecnologias);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        //Cargamos un adaptador vacío mientras se carga la lista desde la API REST
        this.lTecnologiaRel = new ArrayList<>();
        adapter = new DesarrolladorAdapter(lTecnologiaRel);
        recycler.setAdapter(adapter);

        this.nombre = root.findViewById(R.id.textViewNombreDesarrolladorCard);
        this.nombreString = nombre.getText().toString();

        this.lTecnologiaRel = new ArrayList<>();
        cargarLista();

        return root;
    }

    private void cargarLista(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getDesarrolladores(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            List<Convocatoria> lConvocatorias = new ArrayList<>();
            List<Desarrollador> lDesarrolladorAux = new ArrayList<>();
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lConvocatorias = (ArrayList<Convocatoria>) Utilidad.getObjeto(lObjetos, Constantes.AL_CONVOCATORIA);

                    //recorro las 2 convocatorias
                    for (int i = 0; i < lConvocatorias.size(); i++) {
                        //recorro los desarrolladores y los almaceno en una lista de tipo object auxiliar
                        for (int j = 0; j < lConvocatorias.get(i).getlDesarrolladores().size(); j++) {
                            lDesarrolladorAux.add((Desarrollador) Utilidad.getObjeto(lConvocatorias.get(i).getlDesarrolladores().get(j),Constantes.DESARROLLADOR));
                            for (int k = 0; k < lDesarrolladorAux.size(); k++) {
                                TecnologiaRel tecnologiaRelAux = (TecnologiaRel) Utilidad.getObjeto(lDesarrolladorAux.get(k), Constantes.TECNOLOGIAREL);
                                DesarrolladorRel desarrolladorRel = (DesarrolladorRel) Utilidad.getObjeto(lDesarrolladorAux.get(k), Constantes.DESARROLLADORREL);
                                if(desarrolladorRel.getNombre().equals(nombreString)){
                                    lTecnologiaRel.add(tecnologiaRelAux);
                                }
                            }
                        }
                    }


                    adapter = new DesarrolladorAdapter(lTecnologiaRel);
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