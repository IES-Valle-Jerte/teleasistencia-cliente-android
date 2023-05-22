package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.HistoricoTipoSituacion;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoSituacion;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.historico_tipo_situacion.HistoricoTipoSituacionAdapter;
import com.example.teleappsistencia.ui.fragments.tipo_situacion.TipoSituacionAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DispositivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DispositivosFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ContactosPacienteFragment contactosPacienteFragment;
    private Paciente paciente;
    private Terminal terminal;

    private Button buttonGuardar;
    private Button buttonVolver;
    private Spinner situacionTerminal;
    private EditText fechaAlta;
    private EditText numTerminal;
    private EditText modeloTerminal;
    private Switch tieneUCR;
    private HistoricoTipoSituacion historicoTipoSituacion;

    private List<HistoricoTipoSituacion> items;

    private boolean edit;

    public DispositivosFragment() {
        // Required empty public constructor
    }
    public DispositivosFragment(boolean editar) {
        // Required empty public constructor
        this.edit=editar;
    }

    public void setContactosPacienteFragment(ContactosPacienteFragment contactosPacienteFragment) {
        this.contactosPacienteFragment = contactosPacienteFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DispositivosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DispositivosFragment newInstance(String param1, String param2) {
        DispositivosFragment fragment = new DispositivosFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            Paciente paciente = (Paciente) bundle.getSerializable("paciente"); // Reemplaza "objeto" con la clave que hayas utilizado en el primer fragmento
            // Haz lo que desees con el objeto en este fragmento
            this.paciente=paciente;
            Terminal terminal= (Terminal) bundle.get("terminal");
            this.terminal=terminal;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_dispositivos, container, false);
        buttonGuardar=v.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(this);
        buttonVolver=v.findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(this);
        situacionTerminal=v.findViewById(R.id.spinnerSituacionTerminal);
        fechaAlta=v.findViewById(R.id.editTextFechaAlta);
        fechaAlta.requestFocus();
        numTerminal=v.findViewById(R.id.editTextNumeroTerminal);
        modeloTerminal=v.findViewById(R.id.editTextModeloTerminal);
        tieneUCR=v.findViewById(R.id.switchUCR);
        inicializarSpinnerTipoSituacion();
        return v;
    }
    public void rellenarCamposEdit(){
        numTerminal.setText(this.terminal.getNumeroTerminal());
        if (this.paciente.isTieneUcr()){
            tieneUCR.setChecked(true);
        }else{
            tieneUCR.setChecked(false);
        }
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<HistoricoTipoSituacion>> call = apiService.getHistoricoTipoSituacion(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<HistoricoTipoSituacion>>() {
            @Override
            public void onResponse(Call<List<HistoricoTipoSituacion>> call, Response<List<HistoricoTipoSituacion>> response) {
                if (response.isSuccessful()) {
                    items = response.body();
                    for (int i = 0; i < items.size(); i++) {
                        HistoricoTipoSituacion hts= (HistoricoTipoSituacion) Utilidad.getObjeto(items.get(i),"HistoricoTipoSituacion");
                        historicoTipoSituacion=hts;
                        Terminal t= (Terminal) Utilidad.getObjeto(hts.getTerminal(),Constantes.TERMINAL);
                        Terminal t2=(Terminal) Utilidad.getObjeto(paciente.getTerminal(),Constantes.TERMINAL);
                        if (t.getId()==t2.getId()){
                            APIService apiService = ClienteRetrofit.getInstance().getAPIService();

                            Call<List<TipoSituacion>> call2 = apiService.getTipoSituacion(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
                            call2.enqueue(new Callback<List<TipoSituacion>>() {
                                @Override
                                public void onResponse(Call<List<TipoSituacion>> call2, Response<List<TipoSituacion>> response) {
                                    if (response.isSuccessful()) {
                                        List<TipoSituacion> tipoSituaciones= response.body();
                                        TipoSituacion ts= (TipoSituacion) Utilidad.getObjeto(hts.getIdTipoSituacion(),"TipoSituacion");
                                        for (int i = 0; i <tipoSituaciones.size(); i++) {
                                            if (tipoSituaciones.get(i).getId()==ts.getId()){
                                                situacionTerminal.setSelection(i);
                                            }
                                        }
                                    } else {
                                        AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<TipoSituacion>> call, Throwable t) {
                                    t.printStackTrace();
                                    System.out.println(t.getMessage());
                                }
                            });
                            fechaAlta.setText(hts.getFecha());
                        }
                    }
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
    private void inicializarSpinnerTipoSituacion(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<TipoSituacion>> call = apiService.getTipoSituacion(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoSituacion>>() {
            @Override
            public void onResponse(Call<List<TipoSituacion>> call, Response<List<TipoSituacion>> response) {
                if (response.isSuccessful()) {
                    List<TipoSituacion> tipoSituacionList = response.body();
                    ArrayAdapter<TipoSituacion> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipoSituacionList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    situacionTerminal.setAdapter(adapter);
                }
                rellenarCamposEdit();
            }

            @Override
            public void onFailure(Call<List<TipoSituacion>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }
    private void volver() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, contactosPacienteFragment)
                .addToBackStack(null)
                .commit();
    }
    private void modificarTerminal(int id, Terminal terminalModificar) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Terminal> call = apiService.updateTerminal(id,terminalModificar, Constantes.BEARER +Utilidad.getToken().getAccess());
        call.enqueue(new Callback <Terminal>() {
            @Override
            public void onResponse(Call<Terminal> call, Response<Terminal> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.TERMINAL_MODIFICADA,Toast.LENGTH_SHORT).show();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_MODIFICAR_TERMINAL,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Terminal> call, Throwable t) {
                Toast.makeText(getContext(),Constantes.ERROR_AL_MODIFICAR_TERMINAL,Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
    private void modificarPacienteBD(int id, Paciente pacienteModificar) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Paciente> call = apiService.updatePaciente(id, pacienteModificar, Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.PACIENTE_MODIFICADO, Toast.LENGTH_SHORT).show();
                    volver();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_MODIFICAR_EL_PACIENTE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable t) {

            }
        });
    }
    public void modificarTerminalYPaciente(){
        this.terminal.setNumeroTerminal(numTerminal.getText().toString());
        //set modelo terminal si finalmente se deja el campo
        boolean ucr=false;
        if(tieneUCR.isChecked()){
            ucr=true;
        }
        paciente.setTieneUcr(ucr);
        //paciente.setTerminal(this.terminal.getId());
        //mod terminal
        modificarTerminal(terminal.getId(),terminal);
        //mod paciente
        modificarPacienteBD(paciente.getId(),paciente);
    }
    public void guardarSituacion(){
        TipoSituacion tipoSituacion = (TipoSituacion) this.situacionTerminal.getSelectedItem();
        HistoricoTipoSituacion historicoTipoSituacion=new HistoricoTipoSituacion();
        historicoTipoSituacion.setFecha(fechaAlta.getText().toString());
        historicoTipoSituacion.setTerminal(this.terminal.getId());

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.addHistoricoTipoSituacion(historicoTipoSituacion, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object historicoTipoSituacion = response.body();
                    Toast.makeText(getContext(), Constantes.INFO_ALERTDIALOG_CREADO_HISTORICO_TIPO_SITUACION, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }
    private void modificarHistoricoTipoSituacion() {
        HistoricoTipoSituacion hts= (HistoricoTipoSituacion) Utilidad.getObjeto(historicoTipoSituacion,"HistoricoTipoSituacion");
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Object> call = apiService.modifyHistoricoTipoSituacion(this.historicoTipoSituacion.getId(), historicoTipoSituacion, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object historicoTipoSituacion = response.body();
                    Toast.makeText(getContext(), Constantes.INFO_ALERTDIALOG_MODIFICADO_HISTORICO_TIPO_SITUACION, Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }
    public void pasarAVentanaListarPacientes() {
        ListarPacienteFragment siguienteFragment = new ListarPacienteFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, siguienteFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonGuardar:
                modificarTerminalYPaciente();
                if (!edit){
                    guardarSituacion();
                }else{
                    modificarHistoricoTipoSituacion();
                }
                pasarAVentanaListarPacientes();
                break;
            case R.id.buttonVolver:
                volver();
                break;
        }

    }
}