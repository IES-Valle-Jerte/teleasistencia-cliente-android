package com.example.teleappsistencia.ui.fragments.centro_sanitario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.TipoCentroSanitario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarCentroSanitario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarCentroSanitario extends Fragment {

    // Declaración de atributos.
    private TextView nombreCentroSanitario;
    private TextView telefonoCentroSanitario;
    private TextView tipoCentroSanitarioCentroSanitario;
    private TextView localidadCentroSanitario;
    private TextView provinciaCentroSanitario;
    private TextView direccionCentroSanitario;
    private TextView codigoPostalCentroSanitario;
    private CentroSanitario centroSanitario;

    public ConsultarCentroSanitario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ConsultarCentroSanitario.
     * @param centroSanitario: Recibe el objeto a consultar.
     */
    public static ConsultarCentroSanitario newInstance(CentroSanitario centroSanitario) {
        ConsultarCentroSanitario fragment = new ConsultarCentroSanitario();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.CENTRO_SANITARIO_OBJETO, centroSanitario);
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
            this.centroSanitario = (CentroSanitario) getArguments().getSerializable(Constantes.CENTRO_SANITARIO_OBJETO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_consultar_centro_sanitario, container, false);

        // Se inicializan las variables.
        this.nombreCentroSanitario = (TextView) root.findViewById(R.id.nombreCentroSanitario);
        this.telefonoCentroSanitario = (TextView) root.findViewById(R.id.telefonoCentroSanitario);
        this.tipoCentroSanitarioCentroSanitario = (TextView) root.findViewById(R.id.tipoCentroSanitarioCentroSanitario);
        this.localidadCentroSanitario = (TextView) root.findViewById(R.id.localidadCentroSanitario);
        this.provinciaCentroSanitario = (TextView) root.findViewById(R.id.provinciaCentroSanitario);
        this.direccionCentroSanitario = (TextView) root.findViewById(R.id.direccionCentroSanitario);
        this.codigoPostalCentroSanitario = (TextView) root.findViewById(R.id.codigoPostalCentroSanitario);

        // Método que muestra los valores del centro sanitario.
        TipoCentroSanitario tipoCentroSanitario = (TipoCentroSanitario) Utilidad.getObjeto(centroSanitario, Constantes.TIPO_CENTRO_SANITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(centroSanitario.getDireccion(), Constantes.DIRECCION);

        this.nombreCentroSanitario.setText(this.centroSanitario.getNombre());
        this.telefonoCentroSanitario.setText(this.centroSanitario.getTelefono());
        this.tipoCentroSanitarioCentroSanitario.setText(tipoCentroSanitario.getNombreTipoCentroSanitario());
        this.localidadCentroSanitario.setText(direccion.getLocalidad());
        this.provinciaCentroSanitario.setText(direccion.getProvincia());
        this.direccionCentroSanitario.setText(direccion.getDireccion());
        this.codigoPostalCentroSanitario.setText(direccion.getCodigoPostal());

        // Inflate the layout for this fragment
        return root;
    }
}