package com.example.teleappsistencia.ui.fragments.relacion_terminal_recurso_comunitario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.RelacionTerminalRecursoComunitario;
import com.example.teleappsistencia.modelos.Terminal;
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
 * Use the {@link InsertarRelacionTerminalRecursoComunitarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarRelacionTerminalRecursoComunitarioFragment extends Fragment implements View.OnClickListener {

    private RelacionTerminalRecursoComunitario relacionTerminalRecursoComunitario;

    private Spinner spinnerTerminalInsertarRelacionTerminalRecursoComunitario;
    private Spinner spinnerRecursoComunitarioInsertarRelacionTerminalRecursoComunitario;
    private Button btnInsertarRelacionTerminalRecursoComunitario;
    private Button btnVolverRelacionTerminalRecursoComunitarioInsertar;

    public InsertarRelacionTerminalRecursoComunitarioFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InsertarRelacionTerminalRecursoComunitarioFragment newInstance() {
        InsertarRelacionTerminalRecursoComunitarioFragment fragment = new InsertarRelacionTerminalRecursoComunitarioFragment();
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
        View root = inflater.inflate(R.layout.fragment_insertar_relacion_terminal_recurso_comunitario, container, false);

        obtenerComponentes(root);
        inicializarSpinnerTerminal();
        inicializarSpinnerRecursoComunitario();
        // Inflate the layout for this fragment
        return root;
    }

    private void obtenerComponentes(View root) {
        spinnerTerminalInsertarRelacionTerminalRecursoComunitario = root.findViewById(R.id.spinnerTerminalInsertarRelacionTerminalRecursoComunitario);
        spinnerRecursoComunitarioInsertarRelacionTerminalRecursoComunitario = root.findViewById(R.id.spinnerRecursoComunitarioInsertarRelacionTerminalRecursoComunitario);
        btnInsertarRelacionTerminalRecursoComunitario = root.findViewById(R.id.btnInsertarRelacionTerminalRecursoComunitario);
        btnVolverRelacionTerminalRecursoComunitarioInsertar = root.findViewById(R.id.btnVolverRelacionTerminalRecursoComunitarioInsertar);

        btnInsertarRelacionTerminalRecursoComunitario.setOnClickListener(this);
        btnVolverRelacionTerminalRecursoComunitarioInsertar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInsertarRelacionTerminalRecursoComunitario:
                accionBotonGuardar();
                break;
            case R.id.btnVolverRelacionTerminalRecursoComunitarioInsertar:
                volver();
                break;
        }
    }

    private void volver() {
        ListarRelacionTerminalRecursoComunitarioFragment listarRelacionTerminalRecursoComunitarioFragment = new ListarRelacionTerminalRecursoComunitarioFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarRelacionTerminalRecursoComunitarioFragment)
                .addToBackStack(null)
                .commit();
    }

    private void accionBotonGuardar() {
        String terminalSeleccionado = spinnerTerminalInsertarRelacionTerminalRecursoComunitario.getSelectedItem().toString();
        String recursoComunitarioSeleccionado = spinnerRecursoComunitarioInsertarRelacionTerminalRecursoComunitario.getSelectedItem().toString();
        String[] terminalSeleccionadoSplit = terminalSeleccionado.split(Constantes.REGEX_SEPARADOR_GUION);
        String[] recursoComunitarioSeleccionadoSplit = recursoComunitarioSeleccionado.split(Constantes.REGEX_SEPARADOR_GUION);
        terminalSeleccionado = terminalSeleccionadoSplit[0];
        recursoComunitarioSeleccionado = recursoComunitarioSeleccionadoSplit[0];
        guardarRelacionTerminalRecursoComunitario(terminalSeleccionado, recursoComunitarioSeleccionado);
    }

    private void guardarRelacionTerminalRecursoComunitario(String terminalSeleccionado, String recursoComunitarioSeleccionado) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        RelacionTerminalRecursoComunitario relacionTerminalRecursoComunitario = new RelacionTerminalRecursoComunitario();
        relacionTerminalRecursoComunitario.setIdTerminal(Integer.parseInt(terminalSeleccionado));
        relacionTerminalRecursoComunitario.setIdRecursoComunitario(Integer.parseInt(recursoComunitarioSeleccionado));
        Call<RelacionTerminalRecursoComunitario> call = apiService.addRelacionTerminalRecursoComunitario(relacionTerminalRecursoComunitario, Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<RelacionTerminalRecursoComunitario>() {
            @Override
            public void onResponse(Call<RelacionTerminalRecursoComunitario> call, Response<RelacionTerminalRecursoComunitario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.RELACION_GUARDADA, Toast.LENGTH_SHORT).show();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_GUARDAR_RELACIÓN, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RelacionTerminalRecursoComunitario> call, Throwable t) {

            }
        });

    }


    private void inicializarSpinnerTerminal() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Terminal>> call = apiService.getListadoTerminal(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<Terminal>>() {
            @Override
            public void onResponse(Call<List<Terminal>> call, Response<List<Terminal>> response) {
                if (response.isSuccessful()) {
                    List<Terminal> listadoTerminales = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaTerminales(listadoTerminales));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTerminalInsertarRelacionTerminalRecursoComunitario.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Terminal>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_LOS_DATOS, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private List<String> convertirListaTerminales(List<Terminal> listadoTerminales) {
        List<String> listadoTerminalesString = new ArrayList<>();
        for (Terminal terminal : listadoTerminales) {
            listadoTerminalesString.add(terminal.getId() + Constantes.REGEX_SEPARADOR_GUION + "Nº de terminal: " + terminal.getNumeroTerminal());
        }
        return listadoTerminalesString;
    }

    private void inicializarSpinnerRecursoComunitario() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<RecursoComunitario>> call = apiService.getListadoRecursoComunitario(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new retrofit2.Callback<List<RecursoComunitario>>() {
            @Override
            public void onResponse(Call<List<RecursoComunitario>> call, Response<List<RecursoComunitario>> response) {
                if (response.isSuccessful()) {
                    List<RecursoComunitario> listadoRecursoComunitario = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaRecursoComunitario(listadoRecursoComunitario));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRecursoComunitarioInsertarRelacionTerminalRecursoComunitario.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<RecursoComunitario>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_AL_OBTENER_LOS_DATOS, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private List<String> convertirListaRecursoComunitario(List<RecursoComunitario> listadoRecursoComunitario) {
        List<String> listadoRecursoComunitarioString = new ArrayList<>();
        for (RecursoComunitario recursoComunitario : listadoRecursoComunitario) {
            listadoRecursoComunitarioString.add(recursoComunitario.getId() + Constantes.REGEX_SEPARADOR_GUION + recursoComunitario.getNombre());
        }
        return listadoRecursoComunitarioString;
    }


}