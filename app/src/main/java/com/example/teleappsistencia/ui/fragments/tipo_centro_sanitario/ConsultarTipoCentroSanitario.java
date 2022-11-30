package com.example.teleappsistencia.ui.fragments.tipo_centro_sanitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarTipoCentroSanitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarTipoCentroSanitario extends Fragment {

    // Declaración de atributos.
    private TextView nombreTipoCentroSanitario;
    private TipoCentroSanitario tipoCentroSanitario;

    public ConsultarTipoCentroSanitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ConsultarTipoCentroSanitario.
     * @param tipoCentroSanitario: Recibe el objeto a consultar.
     */
    public static ConsultarTipoCentroSanitario newInstance(TipoCentroSanitario tipoCentroSanitario) {
        ConsultarTipoCentroSanitario fragment = new ConsultarTipoCentroSanitario();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.TIPO_CENTRO_SANITARIO_OBJETO, tipoCentroSanitario);
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
            this.tipoCentroSanitario = (TipoCentroSanitario) getArguments().getSerializable(Constantes.TIPO_CENTRO_SANITARIO_OBJETO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_consultar_tipo_centro_sanitario, container, false);

        // Se inicializan las variables.
        this.nombreTipoCentroSanitario = (TextView) root.findViewById(R.id.nombreTipoCentroSanitario);

        // Método que muestra los valores del tipo de centro sanitario.
        this.nombreTipoCentroSanitario.setText(this.tipoCentroSanitario.getNombreTipoCentroSanitario());

        // Inflate the layout for this fragment
        return root;
    }
}