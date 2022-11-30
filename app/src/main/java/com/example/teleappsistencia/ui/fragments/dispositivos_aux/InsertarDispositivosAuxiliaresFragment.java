package com.example.teleappsistencia.ui.fragments.dispositivos_aux;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.modelos.DispositivoAuxiliar;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoAlarma;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertarDispositivosAuxiliaresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarDispositivosAuxiliaresFragment extends Fragment {

    private Button btn_insertar;
    private Spinner spinner_terminal;
    private Spinner spinner_tipoAlarma;

    public InsertarDispositivosAuxiliaresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarDispositivosAuxiliaresFragment.
     */
    public static InsertarDispositivosAuxiliaresFragment newInstance() {
        InsertarDispositivosAuxiliaresFragment fragment = new InsertarDispositivosAuxiliaresFragment();
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
        View view = inflater.inflate(R.layout.fragment_insertar_dispositivos_auxiliares, container, false);

        this.btn_insertar = (Button) view.findViewById(R.id.btn_guardar_dispositivosAux);
        this.spinner_terminal = (Spinner) view.findViewById(R.id.spinner_terminal_dispositivosAux);
        this.spinner_tipoAlarma = (Spinner) view.findViewById(R.id.spinner_tipoAlarma_dispositivosAux);

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

        return view;
    }

    /**
     * Método encargado de realizar una petición a la API y inicializar el spinnerTerminal
     * con los terminales recibidos por la petición.
     */
    private void inicializarSpinnerTerminal() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService(); // Recogo la instancia APIService de la clase ClienteRetrofit.

        Call<List<Object>> call = apiService.getTerminales(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    List<Object> objectList = response.body(); // Recogo la lista de Terminales.
                    // Creo el adapter y le asigno la lista.
                    List<Terminal> terminalList = (ArrayList<Terminal>) Utilidad.getObjeto(objectList, Constantes.AL_TERMINAL);
                    ArrayAdapter<Terminal> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, terminalList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Le asigno el adapter al spinner.
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
     * Método encargado de realizar una petición a la API y inicializar el spinnerTipoAlarma
     * con los tipoAlarma recibidos por la petición y con el terminal del dispositivoAuxiliar como principal.
     */
    private void inicializarSpinnerTipoAlarma() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService(); // Recogo la instancia APIService de la clase ClienteRetrofit.

        Call<List<TipoAlarma>> call = apiService.getTiposAlarmas(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoAlarma>>() {
            @Override
            public void onResponse(Call<List<TipoAlarma>> call, Response<List<TipoAlarma>> response) {
                if (response.isSuccessful()) {
                    List<TipoAlarma> tipoAlarmaList = response.body(); // Recogo la lista de Tipos de Alarmas.
                    // Creo el adapter y le asigno la lista.
                    ArrayAdapter<TipoAlarma> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipoAlarmaList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Le asigno el adapter al spinner.
                    spinner_tipoAlarma.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TipoAlarma>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método para insertar un nuevo dispositivo auxilar en terminal en la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void insertarDispositivoAuxiliar() {
        Terminal terminal = (Terminal) this.spinner_terminal.getSelectedItem();
        TipoAlarma tipoAlarma = (TipoAlarma) this.spinner_tipoAlarma.getSelectedItem();

        DispositivoAuxiliar dispositivoAuxiliar = new DispositivoAuxiliar();
        dispositivoAuxiliar.setTerminal(terminal.getId());
        dispositivoAuxiliar.setTipoAlarma(tipoAlarma.getId());

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.addDispositivoAuxiliar(dispositivoAuxiliar, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object dispositivoAuxiliar = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_CREADO_DISPOSITIVO_AUXILIAR);
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