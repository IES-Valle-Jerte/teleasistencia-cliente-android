package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.modelos.TipoVivienda;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosViviendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosViviendaFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatosSanitariosFragment datosSanitariosFragment;
    private Paciente paciente;
    private Terminal terminal;

    private Button buttonGuardar;
    private Button buttonVolver;
    private Spinner tipoVivienda;
    private EditText accesoVivienda;
    private EditText otrosDatos;

    private boolean edit=false;

    private List<TipoVivienda> listadoTipoVivienda=null;



    public DatosViviendaFragment() {
        // Required empty public constructor
    }
    public DatosViviendaFragment(boolean editar) {
        // Required empty public constructor
        this.edit=editar;
    }

    public void setDatosSanitariosFragment(DatosSanitariosFragment datosSanitariosFragment) {
        this.datosSanitariosFragment = datosSanitariosFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosViviendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosViviendaFragment newInstance(String param1, String param2) {
        DatosViviendaFragment fragment = new DatosViviendaFragment();
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
            Terminal terminal= (Terminal) bundle.getSerializable("terminal");
            this.terminal=terminal;
        }
    }
    private List<String> convertirListaTipoVivienda(List<TipoVivienda> listadoTipoVienda) {
        List<String> listadoString = new ArrayList<>();
        for (TipoVivienda tipoVienda : listadoTipoVienda) {
            listadoString.add(tipoVienda.getId() + "-" + tipoVienda.getNombre());
        }
        return listadoString;
    }
    public void rellenarCamposEdit(){
        if (this.terminal.getTipoVivienda()!=null && this.terminal.getTipoVivienda()!=""){
            accesoVivienda.setText(this.terminal.getModoAccesoVivienda());
            otrosDatos.setText(this.terminal.getBarrerasArquitectonicas());
            TipoVivienda tipoViv= (TipoVivienda) Utilidad.getObjeto(paciente.getTipoModalidadPaciente(),"TipoVivienda");
            for (int i = 0; i <listadoTipoVivienda.size() ; i++) {
                if (listadoTipoVivienda.get(i).getNombre().equals(tipoViv.getNombre())){
                    tipoVivienda.setSelection(i);
                }
            }
        }
    }
    private void listarTipoVivienda() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<TipoVivienda>> call = apiService.getTipoVivienda(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<TipoVivienda>>() {
            @Override
            public void onResponse(Call<List<TipoVivienda>> call, Response<List<TipoVivienda>> response) {
                if (response.isSuccessful()) {
                    listadoTipoVivienda = response.body();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaTipoVivienda(listadoTipoVivienda));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tipoVivienda.setAdapter(adapter);
                    //Si el objeto se va a editar se rellenan los campos
                    rellenarCamposEdit();
                } else {
                    Toast.makeText(getContext(), Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoVivienda>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_datos_vivienda, container, false);
        buttonGuardar=v.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(this);
        buttonVolver=v.findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(this);
        tipoVivienda=v.findViewById(R.id.spinnerTipoVivienda);
        accesoVivienda=v.findViewById(R.id.editTextAccesoVivienda);
        otrosDatos=v.findViewById(R.id.editTextOtrosDatosVivienda);
        listarTipoVivienda();
        return v;
    }
    private void volver() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, datosSanitariosFragment)
                .addToBackStack(null)
                .commit();
    }
    public void pasarObjetosAlSiguienteFragmento(Paciente paciente,Terminal terminal) {
        ContactosPacienteFragment siguienteFragment = new ContactosPacienteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("paciente", paciente); //Se carga el paciente en el bundle
        bundle.putSerializable("terminal",terminal);//Se pasa el terminal mediante el bundle
        siguienteFragment.setDatosViviendaFragment(this);
        siguienteFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, siguienteFragment)
                .addToBackStack(null)
                .commit();
    }
    public void pasarObjetosAlSiguienteFragmentoModificar(Paciente paciente,Terminal terminal) {
        ContactosPacienteFragment siguienteFragment = new ContactosPacienteFragment(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("paciente", paciente); //Se carga el paciente en el bundle
        bundle.putSerializable("terminal",terminal);//Se pasa el terminal mediante el bundle
        siguienteFragment.setDatosViviendaFragment(this);
        siguienteFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, siguienteFragment)
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGuardar:
                this.terminal.setModoAccesoVivienda(accesoVivienda.getText().toString());
                this.terminal.setBarrerasArquitectonicas(otrosDatos.getText().toString());
                //Extraer String del tipo de vivienda
                String seleccionado= (String) tipoVivienda.getSelectedItem();
                boolean encontrado=false;
                int i=0;
                while (!encontrado){
                    String aux=listadoTipoVivienda.get(i).getId()+ "-" + listadoTipoVivienda.get(i).getNombre();
                    if (aux.equalsIgnoreCase(seleccionado)){
                        encontrado=true;
                    }else{
                        i++;
                    }
                }
                TipoVivienda tipoVivienda= listadoTipoVivienda.get(i);
                terminal.setTipoVivienda(tipoVivienda.getId());
                modificarTerminal(terminal.getId(),terminal);
                if (!edit){//Dependiendo si se esta creando o modificando el paciente
                    pasarObjetosAlSiguienteFragmento(paciente,terminal);
                }else{
                    pasarObjetosAlSiguienteFragmentoModificar(paciente,terminal);
                }

                break;
            case R.id.buttonVolver:
                volver();
                break;
        }
    }
}