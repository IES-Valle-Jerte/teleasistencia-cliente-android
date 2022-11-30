package com.example.teleappsistencia.ui.fragments.alarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.Teleoperador;
import com.example.teleappsistencia.modelos.Terminal;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 * Fragment donde se mostrarán los datos de una alarma.
 */
public class ConsultarAlarmaFragment extends Fragment {

    private Alarma alarma;
    private TextView textViewConsultarIdAlarma;
    private TextView textViewConsultarEstadoAlarma;
    private TextView textViewConsultarPacienteAlarma;
    private TextView textViewConsultarTeleoperadorAlarma;
    private TextView textViewConsultarFechaRegistroAlarma;
    private TextView textViewConsultarObservacionesAlarma;
    private TextView textViewConsultarResumenAlarma;


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
        this.textViewConsultarIdAlarma = (TextView) view.findViewById(R.id.textViewConsultarIdAlarma);
        this.textViewConsultarEstadoAlarma = (TextView) view.findViewById(R.id.textViewConsultarEstadoAlarma);
        this.textViewConsultarPacienteAlarma = (TextView) view.findViewById(R.id.textViewConsultarPacienteAlarma);
        this.textViewConsultarTeleoperadorAlarma  = (TextView) view.findViewById(R.id.textViewConsultarTeleoperadorAlarma);
        this.textViewConsultarFechaRegistroAlarma = (TextView) view.findViewById(R.id.textViewConsultarFechaRegistroAlarma);
        this.textViewConsultarObservacionesAlarma = (TextView) view.findViewById(R.id.textViewConsultarObservacionesAlarma);
        this.textViewConsultarResumenAlarma = (TextView) view.findViewById(R.id.textViewConsultarResumenAlarma);
    }

    /**
     * Este método carga los datos de la alarma en el layout.
     */
    private void cargarDatos() {
        Terminal terminal;
        Paciente paciente;
        Persona persona;
        this.textViewConsultarIdAlarma.setText(String.valueOf(alarma.getId()));
        this.textViewConsultarEstadoAlarma.setText(alarma.getEstado_alarma());
        this.textViewConsultarFechaRegistroAlarma.setText(alarma.getFecha_registro().toString());
        this.textViewConsultarObservacionesAlarma.setText(alarma.getObservaciones());
        this.textViewConsultarResumenAlarma.setText(alarma.getResumen());
        if(alarma.getId_teleoperador() != null){
            Teleoperador teleoperador = (Teleoperador) Utilidad.getObjeto(alarma.getId_teleoperador(), Constantes.TELEOPERADOR);
            this.textViewConsultarTeleoperadorAlarma.setText(teleoperador.getFirstName()+Constantes.ESPACIO+teleoperador.getLastName());
        }

        //Dependiendo de como fuese creada la alarma, hay que coger los datos de una forma u otra
        if(alarma.getId_paciente_ucr() != null){
            paciente = (Paciente) Utilidad.getObjeto(alarma.getId_paciente_ucr(), Constantes.PACIENTE);
        }
        else{
            terminal = (Terminal) Utilidad.getObjeto(alarma.getId_terminal(), Constantes.TERMINAL);
            paciente = (Paciente) Utilidad.getObjeto(terminal.getId(), Constantes.PACIENTE);
        }
        persona = (Persona) Utilidad.getObjeto(paciente.getPersona(), Constantes.PERSONA);
        this.textViewConsultarPacienteAlarma.setText(persona.getNombre() + Constantes.ESPACIO + persona.getApellidos());
    }
}