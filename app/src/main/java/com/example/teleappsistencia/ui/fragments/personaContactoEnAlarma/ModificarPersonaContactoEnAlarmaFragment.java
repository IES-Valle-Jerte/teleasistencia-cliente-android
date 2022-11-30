package com.example.teleappsistencia.ui.fragments.personaContactoEnAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.PersonaContactoEnAlarma;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarPersonaContactoEnAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarPersonaContactoEnAlarmaFragment extends Fragment implements View.OnClickListener{

    private PersonaContactoEnAlarma personaContactoEnAlarma;
    private EditText editTextNumberIdAlarmaPCEAModificar;
    private EditText editTextNumberIdPersonaPCEAModificar;
    private EditText editTextFechaRegistroPCEAModificar;
    private EditText editTextAcuerdoPCEAModificar;
    private Button buttonGuardarPCEAModificado;
    private Button buttonVolverPCEA;

    public ModificarPersonaContactoEnAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModificarPersonaContactoEnAlarmaFragment.
     */

    public static ModificarPersonaContactoEnAlarmaFragment newInstance(PersonaContactoEnAlarma personaContactoEnAlarma) {
        ModificarPersonaContactoEnAlarmaFragment fragment = new ModificarPersonaContactoEnAlarmaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_PERSONACONTACTOEA, personaContactoEnAlarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.personaContactoEnAlarma = (PersonaContactoEnAlarma) getArguments().getSerializable(Constantes.ARG_PERSONACONTACTOEA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modificar_persona_contato_en_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos
        if(this.personaContactoEnAlarma != null){
            cargarDatos();
        }

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view) {
        this.editTextNumberIdAlarmaPCEAModificar = (EditText) view.findViewById(R.id.editTextNumberIdAlarmaPCEAModificar);
        this.editTextNumberIdPersonaPCEAModificar = (EditText) view.findViewById(R.id.editTextNumberIdPersonaPCEAModificar);
        this.editTextFechaRegistroPCEAModificar = (EditText) view.findViewById(R.id.editTextFechaRegistroPCEAModificar);
        this.editTextAcuerdoPCEAModificar = (EditText) view.findViewById(R.id.editTextAcuerdoPCEAModificar);
        this.buttonGuardarPCEAModificado = (Button) view.findViewById(R.id.buttonGuardarPCEAModificado);
        this.buttonVolverPCEA = (Button) view.findViewById(R.id.buttonVolverPCEA);
    }

    /**
     * Asignamos la propia clase como OnClickListener, ya que abajo tenemos el método implementado
     */
    private void asignarListener() {
        this.buttonGuardarPCEAModificado.setOnClickListener(this);
        this.buttonVolverPCEA.setOnClickListener(this);
        this.editTextFechaRegistroPCEAModificar.setOnClickListener(this);
    }

    /**
     * Este método carga los datos de la Persona de contacto en Alarma en el layout.
     */
    private void cargarDatos() {
        Alarma alarma = (Alarma) Utilidad.getObjeto(personaContactoEnAlarma.getIdAlarma(), Constantes.ALARMA);
        Persona persona = (Persona) Utilidad.getObjeto(personaContactoEnAlarma.getIdPersonaContacto(), Constantes.PERSONA);

        this.editTextNumberIdAlarmaPCEAModificar.setText(String.valueOf(alarma.getId()));
        this.editTextFechaRegistroPCEAModificar.setText(this.personaContactoEnAlarma.getFechaRegistro());
        this.editTextNumberIdPersonaPCEAModificar.setText(String.valueOf(persona.getId()));
        this.editTextAcuerdoPCEAModificar.setText(this.personaContactoEnAlarma.getAcuerdoAlcanzado());
    }


    /**
     * Este método realiza las comprobaciones básicas imprescindibles
     * TODO: se pueden mejorar estas comprobaciones
     * @return
     */
    private boolean comprobaciones(){
        if(this.editTextFechaRegistroPCEAModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_FECHA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdAlarmaPCEAModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_ALARMA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNumberIdPersonaPCEAModificar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_ID_PERSONA, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Modificamos los datos del objeto PersonaContactoEnAlarma que nos llegó al crear el fragment
     */
    private void modificarDatos(){
        this.personaContactoEnAlarma.setIdAlarma(Integer.parseInt(this.editTextNumberIdAlarmaPCEAModificar.getText().toString()));
        this.personaContactoEnAlarma.setIdPersonaContacto(Integer.parseInt(this.editTextNumberIdPersonaPCEAModificar.getText().toString()));
        this.personaContactoEnAlarma.setFechaRegistro(this.editTextFechaRegistroPCEAModificar.getText().toString());
        this.personaContactoEnAlarma.setAcuerdoAlcanzado(this.editTextAcuerdoPCEAModificar.getText().toString());
    }

    /**
     * Este método lanza la petición PUT a la API REST para modificar la Persona de Contacto en Alarma
     */
    private void persistirPersonaContactoEnAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarPersonaContactoEnAlarma(personaContactoEnAlarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), this.personaContactoEnAlarma);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.errorBody() == null){
                    Toast.makeText(getContext(), Constantes.MODIFICADO_CON_EXITO, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_MODIFICACION + Constantes.PISTA_ALARMA_PERSONA_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        ListarPersonasContactoEnAlarmaFragment lPCEA = new ListarPersonasContactoEnAlarmaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, lPCEA)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editTextFechaRegistroPCEAModificar:
                Utilidad.dialogoFecha(getContext(), editTextFechaRegistroPCEAModificar);
                break;
            case R.id.buttonGuardarPCEAModificado:
                if(comprobaciones()){
                    modificarDatos();
                    persistirPersonaContactoEnAlarma();
                }
                break;
            case R.id.buttonVolverPCEA:
                volver();
                break;
        }
    }
}