package com.example.teleappsistencia.ui.fragments.usuarios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
 * Use the {@link InsertarUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertarUsuariosFragment extends Fragment {

    private Button btn_insertar;
    private EditText editText_nombreUsuario;
    private EditText editText_password;
    private EditText editText_nombre;
    private EditText editText_apellidos;
    private EditText editText_email;
    private Spinner spinner_grupo;

    private TextView textView_error_nombreUsuario;
    private TextView textView_error_password;
    private TextView textView_error_nombre;
    private TextView textView_error_apellidos;
    private TextView textView_error_email;

    public InsertarUsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertarUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InsertarUsuariosFragment newInstance() {
        InsertarUsuariosFragment fragment = new InsertarUsuariosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insertar_usuarios, container, false);

        this.btn_insertar = (Button) view.findViewById(R.id.btn_guardar_usuario);
        this.editText_nombreUsuario = (EditText) view.findViewById(R.id.editText_nombreUsuario_usuario);
        this.editText_password = (EditText) view.findViewById(R.id.editText_password_usuario);
        this.editText_nombre = (EditText) view.findViewById(R.id.editText_nombre_usuario);
        this.editText_apellidos = (EditText) view.findViewById(R.id.editText_apellidos_usuario);
        this.editText_email = (EditText) view.findViewById(R.id.editText_email_usuario);
        this.spinner_grupo = (Spinner) view.findViewById(R.id.spinner_grupos_usuario);

        this.textView_error_nombreUsuario = (TextView) view.findViewById(R.id.textView_error_nombreUsuario_usuario);
        this.textView_error_password = (TextView) view.findViewById(R.id.textView_error_password_usuario);
        this.textView_error_nombre = (TextView) view.findViewById(R.id.textView_error_nombre_usuario);
        this.textView_error_apellidos = (TextView) view.findViewById(R.id.textView_error_apellidos_usuario);
        this.textView_error_email = (TextView) view.findViewById(R.id.textView_error_email_usuario);

        inicializarSpinnerGrupos();

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarUsuario()){
                    insertarUsuario();
                }
            }
        });

        inicializarListeners();

        return view;
    }

    /**
     * Método encargado de realizar una petición a la API y inicializar el spinnerGrupos
     * con los grupos recibidos por la petición.
     */
    private void inicializarSpinnerGrupos() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<Grupo>> call = apiService.getGrupos(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Grupo>>() {
            @Override
            public void onResponse(Call<List<Grupo>> call, Response<List<Grupo>> response) {
                if (response.isSuccessful()) {
                    List<Grupo> grupos = response.body();
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
     * Método para insertar un nuevo usuario en la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void insertarUsuario() {
        String password = this.editText_password.getText().toString();
        String username = this.editText_nombreUsuario.getText().toString();
        String firstName = this.editText_nombre.getText().toString();
        String lastName = this.editText_apellidos.getText().toString();
        String email = this.editText_email.getText().toString();
        Grupo group = (Grupo) this.spinner_grupo.getSelectedItem();

        Usuario usuario = new Usuario();

        usuario.setPassword(password);
        usuario.setUsername(username);
        usuario.setFirstName(firstName);
        usuario.setLastName(lastName);
        usuario.setEmail(email);
        usuario.setGroups(group.getPk());

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.addUsuario(usuario, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object usuario = response.body();
                    if(usuario instanceof String){
                        AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_USUARIO_YA_EXISTENTE);
                    } else {
                        AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_CREADO_USUARIO);
                        borrarEditTexts();
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
     * Método que borra todos los datos de los EditText y quita los mensajes de error.
     */
    private void borrarEditTexts() {
        this.editText_nombreUsuario.setText(Constantes.STRING_VACIO);
        this.editText_password.setText(Constantes.STRING_VACIO);
        this.editText_nombre.setText(Constantes.STRING_VACIO);
        this.editText_apellidos.setText(Constantes.STRING_VACIO);
        this.editText_email.setText(Constantes.STRING_VACIO);

        this.textView_error_nombreUsuario.setVisibility(View.GONE);
        this.textView_error_password.setVisibility(View.GONE);
        this.textView_error_nombre.setVisibility(View.GONE);
        this.textView_error_apellidos.setVisibility(View.GONE);
        this.textView_error_email.setVisibility(View.GONE);
    }

    /**
     * Método que revisa si los datos de los EditText son válidos.
     * @return Devuelve true si es válido de lo contrario devuelve false.
     */
    private boolean validarUsuario() {
        boolean validNombreUsuario, validPassword, validNombre, validApellidos, validEmail, validGrupo;

        validNombreUsuario = validarNombreUsuario(editText_nombreUsuario.getText().toString());
        validPassword = validarPassword(editText_password.getText().toString());
        validNombre = validarNombre(editText_nombre.getText().toString());
        validApellidos = validarApellidos(editText_apellidos.getText().toString());
        validEmail = validarEmail(editText_email.getText().toString());
        validGrupo = validarGrupo();

        if((validNombreUsuario) && (validPassword) && (validNombre) && (validApellidos) && (validEmail) && (validGrupo)){
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

        this.editText_nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarNombre(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_apellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarApellidos(charSequence.toString());
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
     * Método para validar el campo nombre.
     * @param nombre
     * @return
     */
    public boolean validarNombre(String nombre) {
        boolean valid = false;
        if ((nombre.isEmpty()) || (nombre.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si el nombre está vacio.
            textView_error_nombre.setText(R.string.textview_nombre_obligatorio);
            textView_error_nombre.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatorio y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_nombre.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo apellidos.
     * @param apellidos
     * @return
     */
    public boolean validarApellidos(String apellidos) {
        boolean valid = false;
        if ((apellidos.isEmpty()) || (apellidos.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si el apellido está vacio.
            textView_error_apellidos.setText(R.string.textview_apellidos_obligatorios);
            textView_error_apellidos.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacio entonces le asigno el texto de que es obligatorio y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_apellidos.setVisibility(View.GONE);
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