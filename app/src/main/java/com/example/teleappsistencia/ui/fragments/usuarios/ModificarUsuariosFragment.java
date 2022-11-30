package com.example.teleappsistencia.ui.fragments.usuarios;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarUsuariosFragment extends Fragment {

    private Usuario usuario;

    private Button btn_insertar;
    private Button btn_volver;
    private EditText editText_nombreUsuario;
    private EditText editText_password;
    private EditText editText_email;
    private Spinner spinner_grupo;

    private TextView textView_error_nombreUsuario;
    private TextView textView_error_password;
    private TextView textView_error_email;

    public ModificarUsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuario
     * @return A new instance of fragment InsertarUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModificarUsuariosFragment newInstance(Usuario usuario) {
        ModificarUsuariosFragment fragment = new ModificarUsuariosFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(Constantes.USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar_usuarios, container, false);

        this.btn_insertar = view.findViewById(R.id.btn_guardar_usuario);
        this.btn_volver = view.findViewById(R.id.btn_volver_usuario);
        this.editText_nombreUsuario = view.findViewById(R.id.editText_nombreUsuario_usuario);
        this.editText_password = view.findViewById(R.id.editText_password_usuario);
        this.editText_email = view.findViewById(R.id.editText_email_usuario);
        this.spinner_grupo = view.findViewById(R.id.spinner_grupos_usuario);

        this.textView_error_nombreUsuario = view.findViewById(R.id.textView_error_nombreUsuario_usuario);
        this.textView_error_password = view.findViewById(R.id.textView_error_password_usuario);
        this.textView_error_email = view.findViewById(R.id.textView_error_email_usuario);

        inicializarSpinnerGrupos();

        rellenarDatos();

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarUsuario()){
                    modificarUsuario();
                }
            }
        });

        this.btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        inicializarListeners();

        return view;
    }

    /**
     * Método encargado de rellenar los editText con los datos del modelo.
     */
    private void rellenarDatos(){
        this.editText_nombreUsuario.setText(this.usuario.getUsername());
        this.editText_password.setText(this.usuario.getPassword());
        this.editText_email.setText(this.usuario.getEmail());
    }

    /**
     * Método encargado de realizar una petición a la API y inicializar el spinnerGrupos
     * con los grupos recibidos por la petición y con el grupo del usuario como principal.
     */
    private void inicializarSpinnerGrupos() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<Grupo>> call = apiService.getGrupos(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Grupo>>() {
            @Override
            public void onResponse(Call<List<Grupo>> call, Response<List<Grupo>> response) {
                if (response.isSuccessful()) {
                    List<Grupo> grupos = response.body();
                    List<Grupo> gruposUsuario = (List<Grupo>) usuario.getGroups();
                    Grupo grupo = (Grupo) Utilidad.getObjeto(gruposUsuario.get(0), Constantes.GRUPO);
                    if(!gruposUsuario.isEmpty()){
                        grupos.add(0, grupo);  // Asigno el grupo del usuario en la posición 0.
                    }
                    ArrayAdapter<Grupo> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, grupos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_grupo.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Grupo>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }


    /**
     * Método para modificar un usuario de la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void modificarUsuario() {
        String password = this.editText_password.getText().toString();
        String username = this.editText_nombreUsuario.getText().toString();
        String email = this.editText_email.getText().toString();
        Grupo group = (Grupo) this.spinner_grupo.getSelectedItem();

        Usuario usuario = new Usuario();

        usuario.setPassword(password);
        usuario.setUsername(username);
        usuario.setEmail(email);

        if(group.getId() == 0){
            usuario.setGroups(group.getPk());
        } else {
            usuario.setGroups(group.getId());
        }

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.modifyUsuario(this.usuario.getPk(), usuario, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object usuario = response.body();
                    System.out.println(usuario);
                    if(usuario instanceof String){
                        AlertDialogBuilder.crearInfoAlerDialog(getContext(), usuario.toString());
                    } else {
                        AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_MODIFICADO_USUARIO);;
                        getActivity().onBackPressed();
                    }
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método que revisa si los datos de los EditText son válidos.
     * @return Devuelve true si es válido de lo contrario devuelve false.
     */
    private boolean validarUsuario() {
        boolean validNombreUsuario, validPassword, validEmail, validGrupo;

        validNombreUsuario = validarNombreUsuario(editText_nombreUsuario.getText().toString());
        validPassword = validarPassword(editText_password.getText().toString());
        validEmail = validarEmail(editText_email.getText().toString());
        validGrupo = validarGrupo();

        if((validNombreUsuario) && (validPassword) && (validEmail) && (validGrupo)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que inicializa todos los TextWachers de los EditTexts.
     * Los TextWachers se encuentran constantemente revisando si se ha añadido algo a los EditText.
     * Si se ha añadido algo, revisa si lo escrito en el EditText es válido o no.
     */
    private void inicializarListeners() {
        this.editText_nombreUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarNombreUsuario(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Método para validar el campo userName.
     * @param userName
     * @return
     */
    public boolean validarNombreUsuario(String userName) {
        boolean valid = false;
        if ((userName.isEmpty() || (userName.trim().equals(Constantes.STRING_VACIO)))) {    // Reviso si el nombre de usuario está vacio.
            textView_error_nombreUsuario.setText(getResources().getString(R.string.textview_nombre_usuario_obligatorio));
            textView_error_nombreUsuario.setVisibility(View.VISIBLE);
            valid = false;                  // Si está vacio entonces el texto de que es obligatorio y devuelvo false.
        } else {
            if (userName.length() < 4) {    // Si no está vacio reviso si el nombre de usuario tiene menos de 4 carácteres.
                textView_error_nombreUsuario.setText(getResources().getString(R.string.textview_longitud_minima_nombre_usuario));
                textView_error_nombreUsuario.setVisibility(View.VISIBLE);
                valid = false;              // Si tiene menos de 4, le asigno el texto de que la longitud que tiene que tener y devuelvo false.
            } else {
                textView_error_nombreUsuario.setVisibility(View.GONE);
                valid = true;               // Si tiene más de 4, entonces devuelvo true.
            }
        }
        return valid;
    }

    /**
     * Método para validar el campo password.
     * @param password
     * @return
     */
    public boolean validarPassword(String password) {
        boolean valid = false;
        if ((password.isEmpty()) || (password.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la contraseña está vacia.
            textView_error_password.setText(R.string.textview_password_obligatoria);
            textView_error_password.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_password.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo email.
     * @param email
     * @return
     */
    public boolean validarEmail(String email) {
        boolean valid = false;
        if ((email.isEmpty()) || (email.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si el email está vacio.
            textView_error_email.setText(R.string.textview_email_obligatorio);
            textView_error_email.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacio entonces le asigno el texto de que es obligatorio y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_email.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo grupo.
     * @return
     */
    private boolean validarGrupo() {
        if(spinner_grupo.getSelectedItem() != null) {
            return true;
        } else{
            return false;
        }
    }
}