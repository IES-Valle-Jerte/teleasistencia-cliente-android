package com.example.teleappsistencia;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.modelos.Database;
import com.example.teleappsistencia.modelos.ProfilePatch;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.usuarios_sistema.DatabaseAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity invocado al pulsar el icono de la imagen de perfil de un usuario.
 * Este activity le permitirá al usuario modificar su imagen de perfil, entre otras cosas.
 */
public class ModificarPerfilActivity extends AppCompatActivity {
    // Panel: Datos
    ConstraintLayout panelDatos;
    TextView tvUsername;
    ImageView ivFotoPerfil;

    ImageButton btnCargarFoto;

    Button btnCambiarDatabase, btnGuardarCambios, btnCambiarPassword;
    EditText edtNombre, edtApellidos, edtEmail;

    // Panel: Cambio de Contraseña
    ConstraintLayout panelCambiarPassword;
    EditText edtNuevaPassword, edtRepetirNuevaPassword;
    Button btnGuardarNuevaPassword, btnCancelarCambioPassword;

    // Panel: Cambio de Database
    ConstraintLayout panelCambiarDatabase;
    Spinner spinDatabases;
    Button btnGuardarNuevaDatabase, btnCancelarCambioDatabase;

    // Elementos necesarios para el funcionamiento de la clase
    Usuario usuario; ProfilePatch patches; ProfilePatch passwordPatch;
    ActivityResultLauncher<Intent> getImagenLauncher;

    List<Database> databases;
    private DatabaseAdapter adapter;

    // Permisos necesarios
    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    );
    private boolean tienePermisos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initIntentLaunchers();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);
        // Toolbar de la aplicación
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Ocultar ciertos campos si usuario no pertenece al grupo "administrador"
        // Extraer referencias
        getReferenciasGUI();

        cargarDatosUser();
        cargarImagenUser();

        // Conectar acciones
        btnCargarFoto.setOnClickListener(_v -> pedirFoto());
        btnGuardarCambios.setOnClickListener(_v -> guardarCambiosDatos());

        btnCambiarDatabase.setOnClickListener(_v -> alternarPanelesCambioDatabase());
        btnCambiarPassword.setOnClickListener(_v -> alternarPanelesCambioPassword());

        btnGuardarNuevaDatabase.setOnClickListener(_v -> guardarNuevaDatabase());
        btnCancelarCambioDatabase.setOnClickListener(_v -> {
            Toast.makeText(this, Constantes.TOAST_MODPERFIL_CAMBIODB_CANCELADO, Toast.LENGTH_SHORT).show();
            alternarPanelesCambioDatabase();
        });

        btnGuardarNuevaPassword.setOnClickListener(_v -> guardarNuevaPassword());
        btnCancelarCambioPassword.setOnClickListener(_v -> {
            Toast.makeText(this, Constantes.TOAST_MODPERFIL_CAMBIOPASS_CANCELADO, Toast.LENGTH_SHORT).show();
            alternarPanelesCambioPassword();
        });
    }

    private final void initIntentLaunchers() {
        getImagenLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri imagen = (data != null) ? data.getData() : null;

                    if (imagen != null) {
                        patches.setNuevaFotoPerfil(imagen);
                        cargarImagenUser();
                    }
                }
            }
        );
    }

    /**
     * Extrae las referencias a las distintas vistas del layout
     */
    private void getReferenciasGUI() {
        // Panel: Datos
        panelDatos = findViewById(R.id.panelDatos);
        tvUsername = findViewById(R.id.tvUsername);
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtEmail = findViewById(R.id.edtEmail);
        btnCargarFoto = findViewById(R.id.btnCargarFoto);
        btnCambiarDatabase = findViewById(R.id.btnCambiarDatabase);
        btnGuardarNuevaDatabase = findViewById(R.id.btnGuardarNuevaDatabase);
        btnCancelarCambioDatabase = findViewById(R.id.btnCancelarCambioDatabase);
        btnCambiarPassword = findViewById(R.id.btnCambiarPassword);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        // Panel: Cambio de Contraseña
        panelCambiarPassword = findViewById(R.id.panelCambiarPassword);
        edtNuevaPassword = findViewById(R.id.edtNuevaPassword);
        edtRepetirNuevaPassword = findViewById(R.id.edtRepetirNuevaPassword);
        btnCancelarCambioPassword = findViewById(R.id.btnCancelarCambioPassword);
        btnGuardarNuevaPassword = findViewById(R.id.btnGuardarNuevaPassword);
        // Panel: Cambio de Database
        panelCambiarDatabase = findViewById(R.id.panelCambiarDatabase);
        spinDatabases = findViewById(R.id.spinDatabases);
        btnGuardarNuevaDatabase = findViewById(R.id.btnGuardarNuevaDatabase);
        btnCancelarCambioDatabase = findViewById(R.id.btnCancelarCambioDatabase);

        if (!Utilidad.isSuperUser()) {
            btnCambiarDatabase.setVisibility(View.GONE);
        } else {
            cargarOpcionesSpinnerDatabases();
        }
    }

    /**
     * Carga los distintos grupos como opciones del spinner.
     * Solo tendremos la opción de Administrador si somos administradores también, o el user es admin.
     */
    private void cargarOpcionesSpinnerDatabases() {
        APIService service = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Database>> call = service.getDatabases(Utilidad.getAuthorization());

        call.enqueue(new Callback<List<Database>>() {
            @Override
            public void onResponse(Call<List<Database>> call, Response<List<Database>> response) {
                if (response.isSuccessful()) {
                    databases = response.body();

                    // Cargar datos en el Spinner
                    adapter = new DatabaseAdapter(ModificarPerfilActivity.this, databases);

                    spinDatabases.setPrompt("");
                    spinDatabases.setAdapter(adapter);

                    // Encontrar la database actual
                    int databaseIndex = IntStream.range(0, databases.size())
                        // Los grupos sacados de APIService.getGrupos() tienen PK
                        // y los grupos de los Usuarios tienen ID
                        .filter(i -> databases.get(i).getId() == usuario.getDatabase())
                        .findFirst().orElse(-1);
                    spinDatabases.setSelection(databaseIndex);

                    // Proceder a cargar los datos del usuario
                    cargarDatosUser();
                } else {
                    Toast.makeText(ModificarPerfilActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Database>> call, Throwable t) {
                Toast.makeText(ModificarPerfilActivity.this, Constantes.TOAST_USUARIOSISTEMA_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga los datos del Usuario Loggeado ({@link Utilidad#getUserLogged()}) en la Actividad
     */
    private void cargarDatosUser() {
        this.usuario = Utilidad.getUserLogged();
        this.patches = new ProfilePatch(this.usuario);

        // Resto de datos
        tvUsername.setText(usuario.getUsername());
        edtNombre.setText(usuario.getFirstName());
        edtApellidos.setText(usuario.getLastName());
        edtEmail.setText(usuario.getEmail());
    }


    /**
     * Solicita al usuario los permisos necesarios que no tengamos ya.
     * @return Ddevuelve true solo si no se han tenido que pedir permisos.
     */
    private boolean pedirPermisos() {
        if (!tienePermisos) {
            List<String> missing = new ArrayList<>();

            // Anotar los permitos que faltan / si faltan
            for (String permission : REQUIRED_PERMISSIONS) {
                int permissionCheck = ActivityCompat.checkSelfPermission(this, permission);
                if (PackageManager.PERMISSION_DENIED == permissionCheck ) {
                    missing.add(permission);
                }
            }

            tienePermisos = missing.isEmpty();
            // Si hay permisos que nos falten, solicitarlos
            if (!tienePermisos) {
                String[] _missing = new String[missing.size()];
                missing.toArray(_missing);
                ActivityCompat.requestPermissions(this, _missing, 255);
            }
        }

        return tienePermisos;
    }

    /**
     * Solicita una imagen al usuario.
     */
    private void pedirFoto() {
        if (pedirPermisos()) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*"); // Que solo se puedan elegir imagenes
            getImagenLauncher.launch(intent);
        }
    }


    /**
     * Alterna la visibiliad de los paneles.
     */
    private void alternarPanelesCambioDatabase() {
        Utilidad.alternarVista(panelDatos);
        Utilidad.alternarVista(panelCambiarDatabase);
    }

    private void alternarPanelesCambioPassword() {
        Utilidad.alternarVista(panelDatos);
        Utilidad.alternarVista(panelCambiarPassword);

        edtNuevaPassword.setText("");
        edtRepetirNuevaPassword.setText("");
    }

    /**
     * Carga la imagen de perfil del usuario en el ImageView correspondiente.
     */
    private void cargarImagenUser() {
        // Nueva imagen cargada por el usuario
        if (patches.getNuevaFotoPerfil() != null) {
            Utilidad.cargarImagen(patches.getNuevaFotoPerfil(), ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS);
        } else if (usuario.getImagen() != null) {
            Utilidad.cargarImagen(usuario.getImagen().getUrl(), ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS);
        // Imagen por defecto si no tiene
        } else {
            Utilidad.cargarImagen(R.drawable.default_user, ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS);
        }
    }

    /**
     * Intenta confirmar los cambios y enviarlos al servidor
     */
    private void guardarCambiosDatos() {
        // Cargar los cambios para enviarlos
        patches.setNuevoEmail(edtEmail.getText().toString());

        // Si se han hecho alguna modificación continualmos
        if (patches.hasPatches()) {
            // Validamos los cambios y procedemos
            if (validarDatos()) {
                enviarModificacionesPerfil();
            }
        } else {
            cerrarActivity(Activity.RESULT_CANCELED);
        }
    }

    /**
     * Intenta validar todos los campos, si hay probelmas con alguno lo notificará
     * @return true si todos los campos son válidos.
     */
    private boolean validarDatos() {
        // Si se va a cambiar el email y no es valido (mal formato o vacio)
        if (patches.getNuevoEmail() != null && !Utilidad.validarFormatoEmail(edtEmail)) {
            Toast.makeText(this, Constantes.TOAST_MODPERFIL_CORREO_INVALIDO, Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    /**
     * Intenta enviar los datos al servidor, una vez se complete la petición se volverá al MainActivity
     */
    private void enviarModificacionesPerfil() {
        try {
            // Llamar a la API
            Call<Usuario> call = patches.createMultipartPatchAPICall(this, true);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        Usuario userModificado = response.body();
                        Utilidad.setUserLogged(userModificado);
                        cerrarActivity(Activity.RESULT_OK);
                    } else {
                        cerrarActivity(Constantes.RESULT_MODPERFIL_ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    t.printStackTrace();
                    cerrarActivity(Constantes.RESULT_MODPERFIL_ERROR);
                }
            });
        } catch (SecurityException e) {
            pedirPermisos();
        }
    };

    /**
     * Intenta confirmar los contraseñas y enviarlos al servidor
     */
    private void guardarNuevaPassword() {
        // Si los campos son validos procedemos
        if (validarNuevaPassword()) {
            passwordPatch = new ProfilePatch(usuario);
            passwordPatch.setNuevaPassword(edtNuevaPassword.getText().toString());

            enviarCambioPassword();
        }
    }

    /**
     * Intenta confirmar los contraseñas y enviarlos al servidor
     */
    private void guardarNuevaDatabase() {
        Database selectedDb = (Database) spinDatabases.getSelectedItem();

        if (selectedDb != null) {
            APIService servicio = ClienteRetrofit.getInstance().getAPIService();
            Call<Usuario> call = servicio.patchPerfilCambioBatabase(usuario.getPk(), selectedDb.getId() ,Utilidad.getAuthorization());

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        alternarPanelesCambioDatabase();
                        Toast.makeText(ModificarPerfilActivity.this, Constantes.TOAST_MODPERFIL_CAMBIODB_CORRECTO, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ModificarPerfilActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(ModificarPerfilActivity.this, Constantes.TOAST_MODPERFIL_CAMBIOPASS_API_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Intenta validar los EditText de las contaseñas nuevas.
     *
     * @return true si son válidas.
     * @see Utilidad#validatePassword(EditText)
     */
    private boolean validarNuevaPassword() {
        boolean valid = false;
        String pass1 = edtNuevaPassword.getText().toString(),
               pass2 = edtRepetirNuevaPassword.getText().toString();

        if (pass1.trim().isEmpty() && pass2.trim().isEmpty()) {
            Toast.makeText(this, Constantes.TOAST_MODPERFIL_CAMBIOPASS_INVALID_NOPASS, Toast.LENGTH_SHORT).show();
        }
        else if (!pass1.equals(pass2)) {
            Toast.makeText(this, Constantes.TOAST_MODPERFIL_CAMBIOPASS_INVALID_DIFFERENT, Toast.LENGTH_SHORT).show();
        }
        else if (!Utilidad.validatePassword(edtNuevaPassword) || !Utilidad.validatePassword(edtRepetirNuevaPassword)) {
            Toast.makeText(this, Constantes.TOAST_MODPERFIL_CAMBIOPASS_INVALID, Toast.LENGTH_SHORT).show();
        }
        else valid = true;

        return valid;
    }

    /**
     * Intenta enviar los datos al servidor, una vez se complete la petición se volverá al panel de datos.
     */
    private void enviarCambioPassword() {
        // Llamar a la API
        Call<Usuario> call = passwordPatch.createMultipartPatchAPICall(this, true);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    alternarPanelesCambioPassword();
                    Toast.makeText(ModificarPerfilActivity.this, Constantes.TOAST_MODPERFIL_CAMBIOPASS_CORRECTO, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModificarPerfilActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(ModificarPerfilActivity.this, Constantes.TOAST_MODPERFIL_CAMBIOPASS_API_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Salida de la activity
    /**
     * Cierra la actividad para
     * @param RESULT Uno de {@link Activity#RESULT_OK} o {@link Constantes#RESULT_MODPERFIL_ERROR}
     */
    private void cerrarActivity(int RESULT) {
        // Set the result code and data to be returned to the calling activity
        Intent resultIntent = new Intent();
        setResult(RESULT, resultIntent);

        // Cerrar la activity
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 255) {
            List<Integer> grantResultsList = Arrays.stream(grantResults)
                    .boxed().collect(Collectors.toList());

            // Si se ha procesado algúna petición de permisos, y no se nos ha denegado ninguno
            if (!grantResultsList.isEmpty() && !grantResultsList.contains(PackageManager.PERMISSION_DENIED)) {
                this.tienePermisos = true;
            } else {
                Toast.makeText(this, Constantes.TOAST_MODPERFIL_PERMISO_NECESARIO, Toast.LENGTH_SHORT).show();
            }
        }
    }
}