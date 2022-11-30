package com.example.teleappsistencia.ui.fragments.tipo_modalidad_paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.TipoModalidadPaciente;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarTipoModalidadPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarTipoModalidadPaciente extends Fragment {

    // Declaración de atributos.
    private TextView nombreTipoModalidadPaciente;
    private TipoModalidadPaciente tipoModalidadPaciente;

    public ConsultarTipoModalidadPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ConsultarTipoModalidadPaciente.
     * @param tipoModalidadPaciente: Recibe el objeto a consultar.
     */
    public static ConsultarTipoModalidadPaciente newInstance(TipoModalidadPaciente tipoModalidadPaciente) {
        ConsultarTipoModalidadPaciente fragment = new ConsultarTipoModalidadPaciente();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.TIPO_MODALIDAD_PACIENTE_OBJETO, tipoModalidadPaciente);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Método que inicializa el objeto a consultar.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tipoModalidadPaciente = (TipoModalidadPaciente) getArguments().getSerializable(Constantes.TIPO_MODALIDAD_PACIENTE_OBJETO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_consultar_tipo_modalidad_paciente, container, false);

        // Se inicializan las variables.
        this.nombreTipoModalidadPaciente = (TextView) root.findViewById(R.id.nombreTipoModalidadPaciente);

        // Método que muestra los valores del tipo de modalidad de paciente.
        this.nombreTipoModalidadPaciente.setText(this.tipoModalidadPaciente.getNombre());

        // Inflate the layout for this fragment
        return root;
    }
}