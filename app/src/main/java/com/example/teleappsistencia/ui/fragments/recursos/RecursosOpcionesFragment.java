package com.example.teleappsistencia.ui.fragments.recursos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.RecursoComunitario;
import com.example.teleappsistencia.modelos.TipoRecursoComunitario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

public class RecursosOpcionesFragment extends Fragment implements View.OnClickListener{

    // Declaración de atributos.
    private TextView textViewErrorPedirNombre;
    private TextView textViewErrorPedirTelefono;
    private TextView textViewErrorPedirLocalidad;
    private TextView textViewErrorPedirProvincia;
    private TextView textViewErrorPedirDireccion;
    private TextView textViewErrorPedirCodigoPostal;
    private EditText nombreRecursoComunitario;
    private EditText telefonoRecursoComunitario;
    private EditText editTextTipoRecursoComunitario;
    private EditText localidadRecursoComunitario;
    private EditText provinciaRecursoComunitario;
    private EditText direccionRecursoComunitario;
    private EditText codigoPostalRecursoComunitario;
    private Button buttonGuardar;
    private Button buttonVolver;
    private RecursoComunitario recursoComunitario;
    private int idBoton;
    private Spinner spinnerTipoRecursoComunitario;

    public RecursosOpcionesFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConsultarRecursoComunitario.
     * @param recursoComunitario: Recibe el Recurso Comunitario y el Id del Boton presionado.
     */
    public static RecursosOpcionesFragment newInstance(RecursoComunitario recursoComunitario, int idBoton){
        RecursosOpcionesFragment fragment = new RecursosOpcionesFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.RECURSO_COMUNITARIO_OBJETO, recursoComunitario);
        args.putSerializable("idBoton", idBoton);
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
            this.idBoton = (int) getArguments().getSerializable("idBoton");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Se guarda la vista.
        View root = inflater.inflate(R.layout.fragment_opciones_recursos, container, false);

        // Método que muestra los valores del recurso comunitario.
        TipoRecursoComunitario tipoRecursoComunitario = (TipoRecursoComunitario) Utilidad.getObjeto(recursoComunitario.getTipoRecursoComunitario(), Constantes.TIPO_RECURSO_COMUNITARIO);
        Direccion direccion = (Direccion) Utilidad.getObjeto(this.recursoComunitario.getDireccion(), Constantes.DIRECCION);

        // Se inicializan las variables.
        this.textViewErrorPedirNombre = (TextView) root.findViewById(R.id.textViewErrorNombre);
        this.nombreRecursoComunitario = (EditText) root.findViewById(R.id.editTextNombre);
        this.textViewErrorPedirTelefono = (TextView) root.findViewById(R.id.textViewErrorTelefono);
        this.telefonoRecursoComunitario = (EditText) root.findViewById(R.id.editTextPhoneTelefono);
        this.textViewErrorPedirLocalidad = (TextView) root.findViewById(R.id.textViewErrorPedirLocalidad);
        this.localidadRecursoComunitario = (EditText) root.findViewById(R.id.editTextLocalidad);
        this.textViewErrorPedirProvincia = (TextView) root.findViewById(R.id.textViewErrorProvincia);
        this.provinciaRecursoComunitario = (EditText) root.findViewById(R.id.editTextProvincia);
        this.textViewErrorPedirDireccion = (TextView) root.findViewById(R.id.textViewErrorDireccion);
        this.direccionRecursoComunitario = (EditText) root.findViewById(R.id.editTextDireccion);
        this.textViewErrorPedirCodigoPostal = (TextView) root.findViewById(R.id.textViewErrorCodigoPostal);
        this.codigoPostalRecursoComunitario = (EditText) root.findViewById(R.id.editTextTextPostalAddressCodigoPostal);

        this.editTextTipoRecursoComunitario = (EditText) root.findViewById(R.id.editTextTipoRecursoComunitario);
        this.spinnerTipoRecursoComunitario = (Spinner) root.findViewById(R.id.spinnerTipoRecursoComunitario);

        this.buttonGuardar = (Button) root.findViewById(R.id.buttonGuardar);
        this.buttonVolver = (Button) root.findViewById(R.id.buttonVolver);

        switch (idBoton){

            // Consultar.
            case 1:
                this.nombreRecursoComunitario.setKeyListener(null);
                this.telefonoRecursoComunitario.setKeyListener(null);
                this.localidadRecursoComunitario.setKeyListener(null);
                this.provinciaRecursoComunitario.setKeyListener(null);
                this.direccionRecursoComunitario.setKeyListener(null);
                this.codigoPostalRecursoComunitario.setKeyListener(null);
                this.editTextTipoRecursoComunitario.setVisibility(View.VISIBLE);
                this.editTextTipoRecursoComunitario.setKeyListener(null);
                this.spinnerTipoRecursoComunitario.setVisibility(View.INVISIBLE);
                this.buttonGuardar.setVisibility(View.GONE);
                this.buttonVolver.setOnClickListener(this);

                this.nombreRecursoComunitario.setText(this.recursoComunitario.getNombre());
                this.telefonoRecursoComunitario.setText(this.recursoComunitario.getTelefono());
                this.editTextTipoRecursoComunitario.setText(tipoRecursoComunitario.getNombreTipoRecursoComunitario());
                this.localidadRecursoComunitario.setText(direccion.getLocalidad());
                this.provinciaRecursoComunitario.setText(direccion.getProvincia());
                this.direccionRecursoComunitario.setText(direccion.getDireccion());
                this.codigoPostalRecursoComunitario.setText(direccion.getCodigoPostal());
                break;

            // Editar.
            case 2:
                this.buttonGuardar.setOnClickListener(this);
                this.buttonVolver.setOnClickListener(this);

                this.nombreRecursoComunitario.setText(this.recursoComunitario.getNombre());
                this.telefonoRecursoComunitario.setText(this.recursoComunitario.getTelefono());
                this.localidadRecursoComunitario.setText(direccion.getLocalidad());
                this.provinciaRecursoComunitario.setText(direccion.getProvincia());
                this.direccionRecursoComunitario.setText(direccion.getDireccion());
                this.codigoPostalRecursoComunitario.setText(direccion.getCodigoPostal());
                break;

            // Nuevo.
            case 3:

                break;
        }

        return root;
    }

    /**
     * Método para establecer las acciones de los botones, según sea un botón u otro.
     *
     * @param view: Recibe por parámetros la vista.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGuardar:
                //accionBotonGuardar();
                break;
            case R.id.buttonVolver:
                accionBotonVolver();
                break;
        }
    }
    /**
     * Método para el botón volver.
     */
    private void accionBotonVolver() {
        getActivity().onBackPressed();
    }
}
