package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;

/**
 * Una clase {@link Fragment} para recoger los datos de consulta de un Paciente particular.
 * <p> Esta clase es una subclase de {@link Fragment} y hereda de ella todos sus métodos y atributos.
 */
public class ConsultarPacienteFragment extends Fragment{

    /**
     * El paciente que se va a consultar.
     */
    private Paciente paciente;

    
    // Atributos de la interfaz de usuario (UI) del fragment.
    
    private TextView textViewConsultarIdTerminal;
    private TextView textViewConsultarIdPersona;
    private TextView textViewConsultarTieneUCR;
    private TextView textViewConsultarNumeroExpediente;
    private TextView textViewConsultarNumeroSeguridadSocial;
    private TextView textViewConsultarPrestacionOtrosServiciosSociales;
    private TextView textViewConsultarObservacionesMedicas;
    private TextView textViewConsultarInteresesActividades;
    private TextView textViewConsultarModalidadPaciente;
    
    // Constructor por defecto.
    public ConsultarPacienteFragment() {
    }

    /**
     * Método que crea una instancia de la clase.
     *
     * @param paciente El paciente que se va a consultar.
     * @return Una instancia de la clase.
     */
    public static ConsultarPacienteFragment newInstance(Paciente paciente) {
        ConsultarPacienteFragment fragment = new ConsultarPacienteFragment();
        Bundle args = new Bundle();
        args.putSerializable("objetoPaciente", paciente);
        fragment.setArguments(args);
        return fragment;
    }

    
    /**
     * Esta función crea una nueva instancia del fragmento y establece los argumentos para el paquete.
     * 
     * @return Una nueva instancia del fragmento.
     */
    /**
     * La función se llama cuando se crea el fragmento.
     * 
     * @param savedInstanceState Un objeto Bundle que contiene el estado guardado previamente de la
     * actividad. Si la actividad nunca ha existido antes, el valor del objeto Bundle es nulo.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paciente = (Paciente) getArguments().getSerializable("objetoPaciente");
        }
    }


    /**
        * La función se llama cuando se crea la vista del fragmento.
        *
        * @param inflater Un objeto LayoutInflater que contiene la representación del layout XML
        * que se va a usar como contenido de este fragmento.
        * @param container Un objeto ViewGroup que contiene el fragmento.
        * @param savedInstanceState Un objeto Bundle que contiene el estado guardado previamente de la
        * actividad. Si la actividad nunca ha existido antes, el valor del objeto Bundle es nulo.
        * @return Un objeto View que contiene el fragmento.
        */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View root = inflater.inflate(R.layout.fragment_consultar_paciente, container, false);
        // Inicialización de los atributos de la interfaz de usuario (UI).
        textViewConsultarIdTerminal = root.findViewById(R.id.textViewConsultarIdTerminal);
        textViewConsultarIdPersona = root.findViewById(R.id.textViewConsultarIdPersona);
        textViewConsultarTieneUCR = root.findViewById(R.id.textViewConsultarTieneUCR);
        textViewConsultarNumeroExpediente = root.findViewById(R.id.textViewConsultarNumeroExpediente);
        textViewConsultarNumeroSeguridadSocial = root.findViewById(R.id.textViewConsultarNumeroSeguridadSocial);
        textViewConsultarPrestacionOtrosServiciosSociales = root.findViewById(R.id.textViewConsultarPrestacionOtrosServiciosSociales);
        textViewConsultarObservacionesMedicas = root.findViewById(R.id.textViewConsultarObservacionesMedicas);
        textViewConsultarInteresesActividades = root.findViewById(R.id.textViewConsultarInteresesActividades);
        // Se establece el valor de los atributos de la interfaz de usuario (UI).
        textViewConsultarIdTerminal.setText(String.valueOf(paciente.getId()));
        if (paciente.isTieneUcr()) {
            textViewConsultarTieneUCR.setText("El paciente tiene UCR");
        } else {
            textViewConsultarTieneUCR.setText("El paciente no tiene UCR");
        }
        textViewConsultarNumeroExpediente.setText(paciente.getNumeroExpediente());
        textViewConsultarNumeroSeguridadSocial.setText(paciente.getNumeroSeguridadSocial());
        textViewConsultarPrestacionOtrosServiciosSociales.setText(paciente.getPrestacionOtrosServiciosSociales());
        textViewConsultarObservacionesMedicas.setText(paciente.getObservacionesMedicas());
        textViewConsultarInteresesActividades.setText(paciente.getInteresesYActividades());
        return root;
    }

}