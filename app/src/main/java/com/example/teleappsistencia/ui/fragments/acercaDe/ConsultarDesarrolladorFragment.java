package com.example.teleappsistencia.ui.fragments.acercaDe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Desarrollador;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarDesarrolladorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarDesarrolladorFragment extends Fragment {
    private Desarrollador desarrollador;
    private TextView textViewNombreDesarrollador;
    private TextView textViewDescripcionDesarrollador;

    public ConsultarDesarrolladorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConsultarDesarrolladorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarDesarrolladorFragment newInstance(Desarrollador desarrollador) {
        ConsultarDesarrolladorFragment fragment = new ConsultarDesarrolladorFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_DESARROLLADOR, desarrollador);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            desarrollador = (Desarrollador) getArguments().getSerializable(Constantes.ARG_DESARROLLADOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_consultar_desarrollador, container, false);

        // Captura los elementos textView de la ventana de consulta
        capturarElementos(root);

        if(this.desarrollador != null){
            cargarDatos();
        }

        return root;
    }

    public void capturarElementos(View root){
        this.textViewNombreDesarrollador = (TextView) root.findViewById(R.id.textViewNombreDesarrollador);
        this.textViewDescripcionDesarrollador = (TextView) root.findViewById(R.id.textViewDescripcionDesarrollador);
    }

    public void cargarDatos(){
        this.textViewNombreDesarrollador.setText(desarrollador.getNombre());
        this.textViewDescripcionDesarrollador.setText(desarrollador.getDescripcion());
    }
}