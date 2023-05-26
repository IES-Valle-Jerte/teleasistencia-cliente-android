package com.example.teleappsistencia.ui.fragments.alarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
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
 * Use the {@link ConsultarAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 * Fragment donde se mostrarán los datos de una alarma.
 */
public class ConsultarAlarmaFragment extends Fragment implements View.OnClickListener {

    private Alarma alarma;
    private TextView textViewConsultarEstadoAlarma;
    private TextView textViewConsultarTeleoperadorAlarma;
    private TextView textViewConsultarObservacionesAlarma;
    private TextView textViewConsultarResumenAlarma;

    private Button botonCancelar;

    private Button botonFinalizar;


    public ConsultarAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param alarma recibe la alarma para pasarla al onCreate
     * @return A new instance of fragment ConsultarAlarmaFragment.
     */
    public static ConsultarAlarmaFragment newInstance(Alarma alarma) {
        ConsultarAlarmaFragment fragment = new ConsultarAlarmaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_ALARMA, alarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobamos que la instancia se ha creado con argumentos y si es así las recogemos.
        if (getArguments() != null) {
            this.alarma = (Alarma) getArguments().getSerializable(Constantes.ARG_ALARMA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_consultar_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Cargamos los datos si hemos pasado una alarma (el fragment se ha creado a través de newInstance()
        if(this.alarma != null){
            cargarDatos();
        }

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view) {
        this.textViewConsultarEstadoAlarma = (TextView) view.findViewById(R.id.textViewConsultarEstadoAlarma);
        this.textViewConsultarTeleoperadorAlarma  = (TextView) view.findViewById(R.id.textViewConsultarTeleoperadorAlarma);
        this.textViewConsultarObservacionesAlarma = (TextView) view.findViewById(R.id.textViewConsultarObservacionesAlarma);
        this.textViewConsultarResumenAlarma = (TextView) view.findViewById(R.id.textViewConsultarResumenAlarma);

        this.botonCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        botonCancelar.setOnClickListener(this);
        this.botonFinalizar = (Button) view.findViewById(R.id.btn_finalizar);
        botonFinalizar.setOnClickListener(this);
    }

    /**
     * Este método carga los datos de la alarma en el layout.
     */
    private void cargarDatos() {
        Terminal terminal;
        Paciente paciente;
        this.textViewConsultarEstadoAlarma.setText(alarma.getEstado_alarma());
        this.textViewConsultarObservacionesAlarma.setText(alarma.getObservaciones());
        this.textViewConsultarResumenAlarma.setText(alarma.getResumen());
        if(alarma.getId_teleoperador() != null){
            Teleoperador teleoperador = (Teleoperador) Utilidad.getObjeto(alarma.getId_teleoperador(), Constantes.TELEOPERADOR);
            this.textViewConsultarTeleoperadorAlarma.setText(Integer.toString(teleoperador.getId()));
        }

        //Dependiendo de como fuese creada la alarma, hay que coger los datos de una forma u otra
        if(alarma.getId_paciente_ucr() != null){
            paciente = (Paciente) Utilidad.getObjeto(alarma.getId_paciente_ucr(), Constantes.PACIENTE);
        }
        else{
            terminal = (Terminal) Utilidad.getObjeto(alarma.getId_terminal(), Constantes.TERMINAL);
            paciente = (Paciente) Utilidad.getObjeto(terminal.getId(), Constantes.PACIENTE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancelar:
                volver();
                break;
            case R.id.btn_finalizar:
                modificarAlarma();
                break;
        }
    }

    public void modificarAlarma(){
        modificarDatos();
        persistirAlarma();
        volver();
    }

    private void modificarDatos(){
        alarma.setObservaciones(this.textViewConsultarObservacionesAlarma.getText().toString());
        alarma.setResumen(this.textViewConsultarResumenAlarma.getText().toString());
        alarma.setEstado_alarma(Constantes.CERRADA);
        alarma.setId_teleoperador(Integer.parseInt(this.textViewConsultarTeleoperadorAlarma.getText().toString()));
    }
    private void persistirAlarma(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarAlarma(alarma.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), alarma);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.ALARMA_MODIFICADA, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_MODIFICACION + Constantes.PISTA_TELEOPERADOR_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ +t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void volver(){
        ListarAlarmasOrdenadasFragment listarAlarmasOrdenadasFragment = new ListarAlarmasOrdenadasFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarAlarmasOrdenadasFragment)
                .addToBackStack(null)
                .commit();
    }
}