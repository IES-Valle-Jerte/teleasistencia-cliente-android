package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.RelacionTerminalRecursoComunitario;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.recurso_comunitario.RecursoComunitarioAdapter;
import com.example.teleappsistencia.ui.fragments.relacion_terminal_recurso_comunitario.RelacionTerminalRecursoComunitarioAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosSanitariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosSanitariosFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Paciente paciente=null;
    private Terminal terminal=null;

    private DatosPersonalesFragment datosPersonalesFragment;

    private Button buttonGuardar;
    private Button buttonVolver;
    private ImageButton botonAdd;
    private ImageButton botonBorrar;
    private Spinner recursos;
    private EditText nuss;
    private EditText tiempo;
    private ListView listaRecursos;

    private boolean edit=false;

    static List<LinkedTreeMap> lRelacionTerminalRecursoComunitario;

    private ArrayList<RelacionTerminalRecursoComunitario> arrayListRecursos;
    private ArrayAdapter<RelacionTerminalRecursoComunitario> adaptador;

    private RelacionTerminalRecursoComunitario recursoSeleccionado;

    public DatosSanitariosFragment() {
        // Required empty public constructor
    }
    public DatosSanitariosFragment(boolean editar) {
        // Required empty public constructor
        this.edit=editar;
    }

    public void setDatosPersonalesFragment(DatosPersonalesFragment datosPersonalesFragment) {
        this.datosPersonalesFragment = datosPersonalesFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosSanitariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosSanitariosFragment newInstance(String param1, String param2) {
        DatosSanitariosFragment fragment = new DatosSanitariosFragment();
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
            this.terminal= (Terminal) Utilidad.getObjeto(this.paciente.getTerminal(),Constantes.TERMINAL);
        }
        //this.paciente.setPersona(datosPersonalesFragment.getIdpersona());
        //this.terminal.setId(datosPersonalesFragment.getIdTerminal());

        arrayListRecursos=new ArrayList<>();
        adaptador = new ArrayAdapter<RelacionTerminalRecursoComunitario>(getContext(), android.R.layout.simple_list_item_1, arrayListRecursos);
    }

    public void inicializarSpinnerTipoRecursoComunitario() {
            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<List<RecursoComunitario>> call = apiService.getListadoRecursoComunitario(Constantes.BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new retrofit2.Callback<List<RecursoComunitario>>() {
                @Override
                public void onResponse(Call<List<RecursoComunitario>> call, Response<List<RecursoComunitario>> response) {
                    if (response.isSuccessful()) {
                        List<RecursoComunitario> listadoRecursoComunitario = response.body();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, convertirListaRecursoComunitario(listadoRecursoComunitario));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        recursos.setAdapter(adapter);
                        //rellenar los campos si el la opcion es editar
                        rellenarCamposEdit();
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
    public void rellenarCamposEdit(){
        if(paciente.getNumeroSeguridadSocial()!=null && paciente.getNumeroSeguridadSocial()!=""){
            nuss.setText(paciente.getNumeroSeguridadSocial());

            APIService apiService = ClienteRetrofit.getInstance().getAPIService();
            Call<List<LinkedTreeMap>> call = apiService.getListadoRelacionTerminalRecursoComunitario(Constantes.BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<List<LinkedTreeMap>>() {
                @Override
                public void onResponse(Call<List<LinkedTreeMap>> call, Response<List<LinkedTreeMap>> response) {
                    if (response.isSuccessful()) {
                        lRelacionTerminalRecursoComunitario = response.body();
                        List<RelacionTerminalRecursoComunitario> listadoRelacionTerminalRecursoComunitario = new ArrayList<>();
                        for (LinkedTreeMap terminalRecursoComunitario : lRelacionTerminalRecursoComunitario) {
                            RelacionTerminalRecursoComunitario rtrc=((RelacionTerminalRecursoComunitario) Utilidad.getObjeto(terminalRecursoComunitario, "RelacionTerminalRecursoComunitario"));
                            Terminal t= (Terminal) Utilidad.getObjeto(rtrc.getIdTerminal(),Constantes.TERMINAL);
                            Terminal t2=(Terminal) Utilidad.getObjeto(paciente.getTerminal(),Constantes.TERMINAL);
                            if(t.getId()==t2.getId()){
                                arrayListRecursos.add(rtrc);
                            }

                        }
                        adaptador.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<LinkedTreeMap>> call, Throwable t) {

                }
            });
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_datos_sanitarios, container, false);
        buttonGuardar=v.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(this);
        buttonVolver=v.findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(this);
        recursos=v.findViewById(R.id.spinnerRecursos);
        inicializarSpinnerTipoRecursoComunitario();
        nuss=v.findViewById(R.id.editTextNUSS);
        tiempo=v.findViewById(R.id.editTextTiempo);
        listaRecursos=v.findViewById(R.id.listaRecursos);
        listaRecursos.setAdapter(adaptador);
        listaRecursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                recursoSeleccionado= arrayListRecursos.get(i);
            }
        });
        botonAdd=v.findViewById(R.id.imageButtonAdd);
        botonAdd.setOnClickListener(this);
        botonBorrar=v.findViewById(R.id.imageButtonEliminar);
        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarRecurso();
            }
        });

        // Obtener el Bundle y extraer el objeto de él
        /*Bundle bundle = getArguments();
        if (bundle != null) {
            paciente = (Paciente) bundle.getSerializable("paciente");
        }*/
        /*if (paciente != null) {
            TipoModalidadPaciente tipo= (TipoModalidadPaciente) paciente.getTipoModalidadPaciente();
            Toast.makeText(getContext(), tipo.getNombre(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Paciente null", Toast.LENGTH_SHORT).show();
        }*/

        return v;
    }

    private void volver() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, datosPersonalesFragment)
                .addToBackStack(null)
                .commit();
    }
    public void pasarPacienteAlSiguienteFragmento(Paciente paciente) {
        DatosViviendaFragment siguienteFragment = new DatosViviendaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("paciente", paciente); //Se carga el objeto en el bundle
        bundle.putSerializable("terminal",this.terminal);
        siguienteFragment.setDatosSanitariosFragment(this);
        siguienteFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, siguienteFragment)
                .addToBackStack(null)
                .commit();
    }
    public void pasarPacienteAlSiguienteFragmentoModificar(Paciente paciente) {
        DatosViviendaFragment siguienteFragment = new DatosViviendaFragment(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("paciente", paciente); //Se carga el objeto en el bundle
        bundle.putSerializable("terminal",this.terminal);
        siguienteFragment.setDatosSanitariosFragment(this);
        siguienteFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, siguienteFragment)
                .addToBackStack(null)
                .commit();
    }
    public void addRecurso(){
        RelacionTerminalRecursoComunitario recursoComunitario=new RelacionTerminalRecursoComunitario();
        String recursoComunitarioSeleccionado = recursos.getSelectedItem().toString();
        String[] recursoComunitarioSeleccionadoSplit = recursoComunitarioSeleccionado.split(Constantes.REGEX_SEPARADOR_GUION);
        recursoComunitarioSeleccionado = recursoComunitarioSeleccionadoSplit[0];

        recursoComunitario.setIdTerminal(terminal.getId());
        recursoComunitario.setIdRecursoComunitario(Integer.parseInt(recursoComunitarioSeleccionado));
        try {
            recursoComunitario.setTiempoEstimado(Integer.parseInt(tiempo.getText().toString()));

            arrayListRecursos.add(recursoComunitario);
            adaptador.notifyDataSetChanged();

        }catch (java.lang.NumberFormatException e){
            Toast.makeText(getContext(), "Debe indicar el tiempo", Toast.LENGTH_SHORT).show();
        }
        tiempo.setText("");


    }
    public void borrarRecurso(){
        arrayListRecursos.remove(recursoSeleccionado);
        adaptador.notifyDataSetChanged();
    }
    public void insertarRecursos(){
       APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        for (RelacionTerminalRecursoComunitario recurso: arrayListRecursos) {
            recurso.setIdTerminal(this.terminal.getId());
            Call<RelacionTerminalRecursoComunitario> call = apiService.addRelacionTerminalRecursoComunitario(recurso, Constantes.BEARER + Utilidad.getToken().getAccess());
            call.enqueue(new Callback<RelacionTerminalRecursoComunitario>() {
                @Override
                public void onResponse(Call<RelacionTerminalRecursoComunitario> call, Response<RelacionTerminalRecursoComunitario> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), Constantes.RELACION_GUARDADA, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), Constantes.ERROR_AL_GUARDAR_RELACIÓN, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RelacionTerminalRecursoComunitario> call, Throwable t) {

                }
            });
        }
    }
    private void modificarPacienteBD(int id, Paciente pacienteModificar) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Paciente> call = apiService.updatePaciente(id, pacienteModificar, Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), Constantes.PACIENTE_MODIFICADO, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_MODIFICAR_EL_PACIENTE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable t) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGuardar:
                paciente.setNumeroSeguridadSocial(nuss.getText().toString());
                paciente.setObservacionesMedicas("");
                paciente.setInteresesYActividades("");
                //Post de los recursos almacenados en el arrayList
                insertarRecursos();
                //Publicar modificaciones en el paciente
                //Pasar al siguiente fragment
                modificarPacienteBD(paciente.getId(),paciente);
                if (!edit){
                    pasarPacienteAlSiguienteFragmento(paciente);
                }else{
                    pasarPacienteAlSiguienteFragmentoModificar(paciente);
                }

                break;
            case R.id.buttonVolver:
                volver();
                break;
            case R.id.imageButtonAdd:
                addRecurso();
                break;
            case R.id.imageButtonBorrar:
                borrarRecurso();
                break;
        }
    }
}