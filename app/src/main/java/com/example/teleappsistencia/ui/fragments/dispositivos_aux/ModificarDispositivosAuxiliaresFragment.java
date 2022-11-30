package com.example.teleappsistencia.ui.fragments.dispositivos_aux;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.modelos.DispositivoAuxiliar;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarDispositivosAuxiliaresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarDispositivosAuxiliaresFragment extends Fragment {

    private DispositivoAuxiliar dispositivoAuxiliar;

    private Button btn_insertar;
    private Button btn_volver;
    private Spinner spinner_terminal;
    private Spinner spinner_tipoAlarma;

    public ModificarDispositivosAuxiliaresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dispositivoAuxiliar
     * @return A new instance of fragment InsertarDispositivosAuxiliaresFragment.
     */
    public static ModificarDispositivosAuxiliaresFragment newInstance(DispositivoAuxiliar dispositivoAuxiliar) {
        ModificarDispositivosAuxiliaresFragment fragment = new ModificarDispositivosAuxiliaresFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.DISPOSITIVO_AUXILIAR, dispositivoAuxiliar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            dispositivoAuxiliar = (DispositivoAuxiliar) getArguments().getSerializable(Constantes.DISPOSITIVO_AUXILIAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar_dispositivos_auxiliares, container, false);

        this.btn_insertar = view.findViewById(R.id.btn_guardar_dispositivosAux);
        this.btn_volver = view.findViewById(R.id.btn_volver_dispositivosAux);
        this.spinner_terminal = view.findViewById(R.id.spinner_terminal_dispositivosAux);
        this.spinner_tipoAlarma = view.findViewById(R.id.spinner_tipoAlarma_dispositivosAux);

        inicializarSpinnerTerminal();
        inicializarSpinnerTipoAlarma();

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDispositivoAuxiliar()) {
                    insertarDispositivoAuxiliar();
                }
            }
        });

        this.btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    /**
     * Método encargado de realizar una petición a la API y inicializar el spinnerTerminal
     * con los terminales recibidos por la petición y con el terminal del dispositivoAuxiliar como principal.
     */
    private void inicializarSpinnerTerminal() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<Object>> call = apiService.getTerminales(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    List<Object> objectList = response.body(); // Recogo la lista de Terminales.
                    // Creo el adapter y le asigno la lista.
                    List<Terminal> terminalList = (ArrayList<Terminal>) Utilidad.getObjeto(objectList, Constantes.AL_TERMINAL);
                    Terminal terminal = (Terminal) Utilidad.getObjeto(dispositivoAuxiliar.getTerminal(), Constantes.TERMINAL);
                    if(terminal != null) {
                        terminalList.add(0, terminal);  // Asigno el terminal del dispositivoAuxiliar en la posición 0.
                    }
                    ArrayAdapter<Terminal> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, terminalList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_terminal.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método encargado de realizar una petición a la API y inicializar el spinnerTerminal
     * con los terminales recibidos por la petición y con el tipoAlarma del dispositivoAuxiliar como principal.
     */
    private void inicializarSpinnerTipoAlarma() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<TipoAlarma>> call = apiService.getTiposAlarmas(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoAlarma>>() {
            @Override
            public void onResponse(Call<List<TipoAlarma>> call, Response<List<TipoAlarma>> response) {
                if (response.isSuccessful()) {
                    List<TipoAlarma> tipoAlarmaList = response.body();
                    TipoAlarma tipoAlarma = (TipoAlarma) Utilidad.getObjeto(dispositivoAuxiliar.getTipoAlarma(), Constantes.TIPOALARMA);
                    tipoAlarmaList.add(0, tipoAlarma);
                    ArrayAdapter<TipoAlarma> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipoAlarmaList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tipoAlarma.setAdapter(adapter);                }
            }

            @Override
            public void onFailure(Call<List<TipoAlarma>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método para modificar un dispositivo auxilar en terminal de la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void insertarDispositivoAuxiliar() {
        Terminal terminal = (Terminal) this.spinner_terminal.getSelectedItem();
        TipoAlarma tipoAlarma = (TipoAlarma) this.spinner_tipoAlarma.getSelectedItem();

        DispositivoAuxiliar dispositivoAuxiliar = new DispositivoAuxiliar();
        dispositivoAuxiliar.setTerminal(terminal.getId());
        dispositivoAuxiliar.setTipoAlarma(tipoAlarma.getId());

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.modifyDispositivoAuxiliar(this.dispositivoAuxiliar.getId(), dispositivoAuxiliar, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object dispositivoAuxiliar = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_MODIFICADO_DISPOSITIVO_AUXILIAR);
                    getActivity().onBackPressed();
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método que revisa si los datos de los EditText son válidos.
     * @return Devuelve true si es válido de lo contrario devuelve false.
     */
    private boolean validarDispositivoAuxiliar() {
        boolean validTerminal, validarTipoAlarma;

        validTerminal = validarTerminal();
        validarTipoAlarma = validarTipoAlarma();

        if((validTerminal) && (validarTipoAlarma)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método para validar el campo terminal.
     * @return
     */
    private boolean validarTerminal() {
        if(spinner_terminal.getSelectedItem() != null) {
            return true;
        } else{
            return false;
        }
    }

    /**
     * Método para validar el campo tipoAlarma.
     * @return
     */
    private boolean validarTipoAlarma() {
        if(spinner_tipoAlarma.getSelectedItem() != null) {
            return true;
        } else{
            return false;
        }
    }
}