package com.example.teleappsistencia.ui.fragments.tipo_recurso_comunitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarTipoRecursoComunitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarTipoRecursoComunitario extends Fragment {

    // Declaración de atributos.
    private TextView nombreTipoRecursoComunitario;
    private TipoRecursoComunitario tipoRecursoComunitario;

    public ConsultarTipoRecursoComunitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters..
     * @return A new instance of fragment ConsultarTipoRecursoComunitario.
     * @param tipoRecursoComunitario: Recibe el objeto a consultar.
     */
    public static ConsultarTipoRecursoComunitario newInstance(TipoRecursoComunitario tipoRecursoComunitario) {
        ConsultarTipoRecursoComunitario fragment = new ConsultarTipoRecursoComunitario();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.TIPO_RECURSO_COMUNITARIO_OBJETO, tipoRecursoComunitario);
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
            this.tipoRecursoComunitario = (TipoRecursoComunitario) getArguments().getSerializable(Constantes.TIPO_RECURSO_COMUNITARIO_OBJETO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_consultar_tipo_recurso_comunitario, container, false);

        // Se inicializan las variables.
        this.nombreTipoRecursoComunitario = (TextView) root.findViewById(R.id.nombreTipoRecursoComunitario);

        // Método que muestra los valores del tipo de recurso comunitario.
        this.nombreTipoRecursoComunitario.setText(this.tipoRecursoComunitario.getNombreTipoRecursoComunitario());

        // Inflate the layout for this fragment
        return root;
    }
}