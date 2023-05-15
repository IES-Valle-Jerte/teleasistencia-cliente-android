package com.example.teleappsistencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.Token;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_iniciar_sesion;
    private EditText editText_usuario;
    private EditText editText_password;
    private TextView textView_error_usuario;
    private TextView textView_error_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Toolbar de la aplicación
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        this.editText_usuario = findViewById(R.id.editText_usuario);
        this.editText_password = findViewById(R.id.editText_password);
        this.textView_error_usuario = findViewById(R.id.textView_error_nombre_usuario);
        this.textView_error_password = findViewById(R.id.textView_error_password);

        textView_error_usuario.setVisibility(View.GONE);
        textView_error_password.setVisibility(View.GONE);

        this.btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCredenciales()) {
                    peticionToken();
                }
            }
        });

        // Añado un TextWatcher para mostrar los errores del nombre del usuario.
        this.editText_usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarNombreUsuario(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Añadimos un TextWatcher para mostrar los errores de la contraseña.
        this.editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * Método que valida el nombre del usuario y la contraseña.
     *
     * @return Devuelve true si los dos son válidos, de lo contrario devuelve false.
     */
    private boolean validarCredenciales() {
        boolean validNombreUsuario, validPassword;

        validNombreUsuario = validarNombreUsuario(editText_usuario.getText().toString());
        validPassword = validarPassword(editText_password.getText().toString());

        if ((validNombreUsuario) && (validPassword)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que recibe un nombre de usuario y comprueba si es válido.
     *
     * @param userName Nombre del usuario.
     * @return Retorna true si es válido o de lo contrario retornará false.
     */
    public boolean validarNombreUsuario(String userName) {
        boolean valid = false;
        if ((userName.isEmpty() || (userName.trim().equals("")))) {    // Reviso si el nombre de usuario está vacio.
            textView_error_usuario.setText(getResources().getString(R.string.textview_nombre_usuario_obligatorio));
            textView_error_usuario.setVisibility(View.VISIBLE);
            valid = false;                  // Si está vacio entonces le asigno al textView_error_usuario
                                            // el texto de que es obligatorio el nombre de usuario y devuelvo false.
        } else {
            if (userName.length() < 4) {    // Si no está vacio reviso si el nombre de usuario tiene menos de 4 carácteres.
                textView_error_usuario.setText(getResources().getString(R.string.textview_longitud_minima_nombre_usuario));
                textView_error_usuario.setVisibility(View.VISIBLE);
                valid = false;              // Si tiene menos de 4, le asigno al textView_error_usuario
                                            // el texto de la longitud que tiene que tener el nombre del usuario y devuelvo false.
            } else {
                textView_error_usuario.setVisibility(View.GONE);
                valid = true;               // Si tiene más de 4, entonces devuelvo true.
            }
        }
        return valid;
    }

    /**
     * Método que recibe una contraseña y comprueba si es válida.
     *
     * @param password
     * @return
     */
    public boolean validarPassword(String password) {
        boolean valid = false;
        if ((password.isEmpty()) || (password.trim().equals(""))) {     // Reviso si la contraseña está vacia.
            textView_error_password.setText(R.string.textview_password_obligatoria);
            textView_error_password.setVisibility(View.VISIBLE);
            valid = false;                                      // Si está vacia entonces le asigno al textView_error_password
                                                                // el texto de que es obligatoria y devuelvo false.
        } else {                                                // De lo contrario devuelvo true y hago que el textView_error_password desaparezca.
            textView_error_password.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método que realiza una petición a la API, enviandole el nombre de usuario y la contraseña.
     * Si se recibe el token correctamente llamará al método peticionUsuarioLogueado(), de lo contrario mostrará un AlertDialog con el error ocurrido.
     */
    public void peticionToken() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Token> call = apiService.getToken(this.editText_usuario.getText().toString(), this.editText_password.getText().toString());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Utilidad.setToken(response.body());

                    peticionUsuarioLogueado();
                } else {
                    if (response.message().equalsIgnoreCase(Constantes.UNAUTHORIZED)) {
                        AlertDialogBuilder.crearInfoAlerDialog(LoginActivity.this, Constantes.INFO_ALERTDIALOG_CREDENCIALES_INCORRECTOS_LOGIN);
                    } else {
                        AlertDialogBuilder.crearErrorAlerDialog(LoginActivity.this, Integer.toString(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
                AlertDialogBuilder.crearInfoAlerDialog(LoginActivity.this, Constantes.ERROR_AL_CONECTARSE_AL_SERVIDOR);
            }
        });
    }

    /**
     * Método que realiza una petición a la API, para guardar al usuario que haya iniciado sesión.
     * Este método a su vez llamará al método asignarUsuarioALaClaseUtilidad y cambiará a la MainActivity.
     */
    private void peticionUsuarioLogueado() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Usuario>> call = apiService.getUsuarioLogueado(Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuariosList = response.body();
                    Usuario usuario = usuariosList.get(0);

                    // Para reducir el tamaño del método peticionUsuarioLogueado(), se ha separado en otro método la signación del usuario de la clase Utils.
                    asignarUsuarioALaClaseUtilidad(usuario);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(LoginActivity.this, Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                t.printStackTrace();
                AlertDialogBuilder.crearInfoAlerDialog(LoginActivity.this, Constantes.ERROR_AL_CONECTARSE_AL_SERVIDOR);
            }
        });
    }

    /**
     * Método para asignar el usuario logeado a la clase Utils.
     * Además mira si el usuario tiene permisos de admin y lo asigna a la variable isAdmin de la clase Utils.
     * @param usuario
     */
    private void asignarUsuarioALaClaseUtilidad(Usuario usuario){
        // Asigno el usuario logueado.
        Utilidad.setUserLogged(usuario);

        List<Grupo> gruposList= (ArrayList) usuario.getGroups();  // Recogo su lista de grupos para ver a cual pertenece.
        Grupo grupo = (Grupo) Utilidad.getObjeto(gruposList.get(0), Constantes.GRUPO);  // LLamo al método Utils.getObjeto() para evitar
                                                                                        // el error al castear una LinkedTreeMap a un Object.

        if (grupo.getName().equalsIgnoreCase(Constantes.ADMINISTRADOR)) {
            // Si pertenece al grupo "Administrador", tanto isAdmin como isSuperUser se asignarán a true.
            Utilidad.setIsAdmin(true);
            Utilidad.setIsSuperUser(true);
        } else if (grupo.getName().equalsIgnoreCase(Constantes.PROFESOR)) {
            // Si pertenece a el grupo con el nombre "Profesor" entonces se asigna la variable isAdmin a true, pero no a isSuperUser.
            Utilidad.setIsAdmin(true);
            Utilidad.setIsSuperUser(false);
        } else {
            // De lo contrario se le asigna false a ambos.
            Utilidad.setIsAdmin(false);
            Utilidad.setIsSuperUser(false);
        }
    }
}