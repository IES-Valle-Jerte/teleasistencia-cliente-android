package com.example.teleappsistencia.ui.fragments.direccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarDireccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarDireccionFragment extends Fragment {

    private Direccion direccion;

    private TextView textView_id;
    private TextView textView_localidad;
    private TextView textView_provincia;
    private TextView textView_direccion;
    private TextView textView_codigoPostal;

    public ConsultarDireccionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param direccion
     * @return A new instance of fragment ConsultarDireccionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarDireccionFragment newInstance(Direccion direccion) {
        ConsultarDireccionFragment fragment = new ConsultarDireccionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.DIRECCION, direccion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            direccion = (Direccion) getArguments().getSerializable(Constantes.DIRECCION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_direccion, container, false);

        this.textView_id = view.findViewById(R.id.textView_consultar_id_direccion);
        this.textView_localidad = view.findViewById(R.id.textView_consultar_localidad_direccion);
        this.textView_provincia = view.findViewById(R.id.textView_consultar_provincia_direccion);
        this.textView_direccion = view.findViewById(R.id.textView_consultar_direccion_direccion);
        this.textView_codigoPostal = view.findViewById(R.id.textView_consultar_codigoPostal_direccion);

        this.textView_id.setText(Integer.toString(direccion.getId()));
        this.textView_localidad.setText(direccion.getLocalidad());
        this.textView_provincia.setText(direccion.getProvincia());
        this.textView_direccion.setText(direccion.getDireccion());
        this.textView_codigoPostal.setText(direccion.getCodigoPostal());

        return view;
    }
}