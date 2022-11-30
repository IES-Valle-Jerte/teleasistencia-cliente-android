package com.example.teleappsistencia.ui.fragments.centroSanitarioEnAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Alarma;
import com.example.teleappsistencia.modelos.CentroSanitario;
import com.example.teleappsistencia.modelos.CentroSanitarioEnAlarma;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarCentroSanitarioEnAlarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarCentroSanitarioEnAlarmaFragment extends Fragment {


    private CentroSanitarioEnAlarma centroSanitarioEnAlarma;
    private TextView textViewConsultarIdCSEA;
    private TextView textViewConsultarFechaCSEA;
    private TextView textViewConsultarPersonaCSEA;
    private TextView textViewConsultarAcuerdoCSEA;
    private TextView textViewConsultarIdAlarmaCSEA;
    private TextView textViewConsultarNombreCentroCSEA;

    public ConsultarCentroSanitarioEnAlarmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConsultarCentroSanitarioEnAlarmaFragment.
     */

    public static ConsultarCentroSanitarioEnAlarmaFragment newInstance(CentroSanitarioEnAlarma centroSanitarioEnAlarma) {
        ConsultarCentroSanitarioEnAlarmaFragment fragment = new ConsultarCentroSanitarioEnAlarmaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_CENTROSANITARIOEA, centroSanitarioEnAlarma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.centroSanitarioEnAlarma = (CentroSanitarioEnAlarma) getArguments().getSerializable(Constantes.ARG_CENTROSANITARIOEA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_consultar_centro_sanitario_en_alarma, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Cargamos los datos
        if(this.centroSanitarioEnAlarma != null){
            cargarDatos();
        }

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view) {
        this.textViewConsultarIdCSEA = (TextView) view.findViewById(R.id.textViewConsultarIdCSEA);
        this.textViewConsultarFechaCSEA = (TextView) view.findViewById(R.id.textViewConsultarFechaCSEA);
        this.textViewConsultarPersonaCSEA = (TextView) view.findViewById(R.id.textViewConsultarPersonaCSEA);
        this.textViewConsultarAcuerdoCSEA = (TextView) view.findViewById(R.id.textViewConsultarAcuerdoCSEA);
        this.textViewConsultarIdAlarmaCSEA = (TextView) view.findViewById(R.id.textViewConsultarIdAlarmaCSEA);
        this.textViewConsultarNombreCentroCSEA = (TextView) view.findViewById(R.id.textViewConsultarNombreCentroCSEA);
    }

    /**
     * Este método carga los datos en el layout.
     */
    private void cargarDatos() {
        Alarma alarma = (Alarma) Utilidad.getObjeto(centroSanitarioEnAlarma.getIdAlarma(), Constantes.ALARMA);
        CentroSanitario centroSanitario = (CentroSanitario) Utilidad.getObjeto(centroSanitarioEnAlarma.getIdCentroSanitario(), Constantes.CENTRO_SANITARIO);

        this.textViewConsultarIdCSEA.setText(String.valueOf(this.centroSanitarioEnAlarma.getId()));
        this.textViewConsultarFechaCSEA.setText(this.centroSanitarioEnAlarma.getFechaRegistro());
        this.textViewConsultarPersonaCSEA.setText(this.centroSanitarioEnAlarma.getPersona());
        this.textViewConsultarAcuerdoCSEA.setText(this.centroSanitarioEnAlarma.getAcuerdoAlcanzado());
        this.textViewConsultarIdAlarmaCSEA.setText(String.valueOf(alarma.getId()));
        this.textViewConsultarNombreCentroCSEA.setText(centroSanitario.getNombre());
    }
}