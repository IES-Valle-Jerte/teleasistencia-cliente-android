package com.example.teleappsistencia.ui.fragments.alarma;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.opciones_listas.OpcionesListaFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarAlarmasOrdenadasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarAlarmasOrdenadasFragment extends Fragment implements View.OnClickListener, OpcionesListaFragment.OnButtonClickListener, AlarmaAdapter.OnItemSelectedListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Alarma> lAlarmas;
    private RecyclerView recycler;
    private AlarmaAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button btn_add_alerta;

    private Context context;

    private String mParam1;
    private String mParam2;

    private int selectedPosition = RecyclerView.NO_POSITION;

    public ListarAlarmasOrdenadasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListarAlarmasFragment.
     */
    public static ListarAlarmasOrdenadasFragment newInstance(String param1, String param2) {
        ListarAlarmasOrdenadasFragment fragment = new ListarAlarmasOrdenadasFragment();
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

        View root = inflater.inflate(R.layout.fragment_listar_alarmas_ordenadas, container, false);

        // Mostrar botón nueva para administradores
        this.btn_add_alerta = (Button) root.findViewById(R.id.btn_add_alarma);
        this.btn_add_alerta.setOnClickListener(this);
        this.context = root.getContext();

        // TODO cambiar a solo para admin

        if(Utilidad.isAdmin()){ // Se muestra el botón solo para administradores
            this.btn_add_alerta.setVisibility(View.VISIBLE);
            this.btn_add_alerta.setEnabled(true);
        }else{
            this.btn_add_alerta.setVisibility(View.INVISIBLE);
            this.btn_add_alerta.setEnabled(false);
        }

        // Obtener el Recycler.
        recycler = (RecyclerView) root.findViewById(R.id.listRecyclerView);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout.
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        //Cargamos un adaptador vacío mientras se carga la lista desde la API REST
        this.lAlarmas = new ArrayList<>();
        adapter = new AlarmaAdapter(lAlarmas);
        recycler.setAdapter(adapter);

        //Cargamos lista desde la API REST
        cargarLista();


        return root;
    }

    /**
     * Este método carga la lista de alarmas que vamos a mostrar de forma ordenada desde la API REST y una vez que se ha cargada
     * se las añade al adapter y se las cargamos al recycler.
     */
    private void cargarLista(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getAlarmas(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lAlarmas = (ArrayList<Alarma>) Utilidad.getObjeto(lObjetos, Constantes.AL_ALARMA);
                    Collections.sort(lAlarmas); // Se ordena la lista de alarmas por estado, en caso de ser igual se ordena por fecha y hora de registro
                    adapter = new AlarmaAdapter(lAlarmas);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_alarma: // Muestra un fragment con el formulario para crear la alarma
                mostrarCrearAlarma();
                break;
        }
    }

    // Muestra el formulario de creación de alarmas
    public void mostrarCrearAlarma(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_fragment, new InsertarAlarmaFragment());

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Estos métodos son los que hacen las funciones del layout de interación con los cardviews
    @Override
    public void onViewDetailsButtonClicked() {
        //showAlarmDetails();
        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteButtonClicked() {

    }

    @Override
    public void onEditButtonClicked() {

    }

    public void showAlarmDetails(){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        ConsultarAlarmaFragment consultarAlarmaFragment = ConsultarAlarmaFragment.newInstance(adapter.getAlarmaSeleccionada());
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, consultarAlarmaFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemSelected(int position) {
        this.selectedPosition = position;
    }
}