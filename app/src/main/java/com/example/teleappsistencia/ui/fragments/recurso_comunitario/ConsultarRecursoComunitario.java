package com.example.teleappsistencia.ui.fragments.recurso_comunitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarRecursoComunitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarRecursoComunitario extends Fragment {

    // Declaración de atributos.
    private TextView nombreRecursoComunitario;
    private TextView telefonoRecursoComunitario;
    private TextView tipoRecursoComunitarioRecursoComunitario;
    private TextView localidadRecursoComunitario;
    private TextView provinciaRecursoComunitario;
    private TextView direccionRecursoComunitario;
    private TextView codigoPostalRecursoComunitario;
    private RecursoComunitario recursoComunitario;

    public ConsultarRecursoComunitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ConsultarRecursoComunitario.
     * @param recursoComunitario: Recibe el objeto a consultar.
     */
    public static ConsultarRecursoComunitario newInstance(RecursoComunitario recursoComunitario) {
        ConsultarRecursoComunitario fragment = new ConsultarRecursoComunitario();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.RECURSO_COMUNITARIO_OBJETO, recursoComunitario);
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
            this.recursoComunitario = (RecursoComunitario) getArguments().getSerializable(Constantes.RECURSO_COMUNITARIO_OBJETO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_consultar_recurso_comunitario, container, false);

        // Se inicializan las variables.
        this.nombreRecursoComunitario = (TextView) root.findViewById(R.id.nombreRecursoComunitario);
        this.telefonoRecursoComunitario = (TextView) root.findViewById(R.id.telefonoRecursoComunitario);
        this.tipoRecursoComunitarioRecursoComunitario = (TextView) root.findViewById(R.id.tipoRecursoComunitarioRecursoComunitario);
        this.localidadRecursoComunitario = (TextView) root.findViewById(R.id.localidadRecursoComunitario);
        this.provinciaRecursoComunitario = (TextView) root.findViewById(R.id.provinciaRecursoComunitario);
        this.direccionRecursoComunitario = (TextView) root.findViewById(R.id.direccionRecursoComunitario);
        this.codigoPostalRecursoComunitario = (TextView) root.findViewById(R.id.codigoPostalRecursoComunitario);

        // Método que muestra los valores del recurso comunitario.
        TipoRecursoComunitario tipoRecursoComunitario = (TipoRecursoComunitario) Utilidad.getObjeto(recursoComunitario, Constantes.TIPO_RECURSO_COMUNITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(this.recursoComunitario.getDireccion(), Constantes.DIRECCION);

        this.nombreRecursoComunitario.setText(this.recursoComunitario.getNombre());
        this.telefonoRecursoComunitario.setText(this.recursoComunitario.getTelefono());
        this.tipoRecursoComunitarioRecursoComunitario.setText(tipoRecursoComunitario.getNombreTipoRecursoComunitario());
        this.localidadRecursoComunitario.setText(direccion.getLocalidad());
        this.provinciaRecursoComunitario.setText(direccion.getProvincia());
        this.direccionRecursoComunitario.setText(direccion.getDireccion());
        this.codigoPostalRecursoComunitario.setText(direccion.getCodigoPostal());

        // Inflate the layout for this fragment
        return root;
    }
}