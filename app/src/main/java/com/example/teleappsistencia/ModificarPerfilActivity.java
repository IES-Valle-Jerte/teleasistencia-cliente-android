package com.example.teleappsistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Activity invocado al pulsar el icono de la imagen de perfil de un usuario.
 * Este activity le permitirá al usuario modificar su imagen de perfil, entre otras cosas.
 */
public class ModificarPerfilActivity extends AppCompatActivity {
    ImageView ivFotoPerfil;

    ImageButton btnCargarFoto;
    Button btnGuardar;

    TextView tvUsername;
    EditText edtNombre, edtApellidos, edtEmail;

    // Permisos necesarios
    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    );
    private boolean tienePermisos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);
        // Toolbar de la aplicación
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Ocultar ciertos campos si usuario no pertenece al grupo "administrador"
        // Extraer referencias
        getReferenciasGUI();
        cargarDatosUser();

        // Conectar acciones
        btnGuardar.setOnClickListener(_v -> enviarDatos());
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
     * Extrae las referencias a las distintas vistas del layout
     */
    private void getReferenciasGUI() {
        tvUsername = findViewById(R.id.tvUsername);
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtEmail = findViewById(R.id.edtEmail);
        btnCargarFoto = findViewById(R.id.btnCargarFoto);
        btnGuardar = findViewById(R.id.btnGuardarCambios);
    }

    /**
     * Carga los datos del Usiario Loggeado ({@link Utilidad#getUserLogged()}) en la Actividad
     */
    private void cargarDatosUser() {
        Usuario usuario = Utilidad.getUserLogged();

        // Imagen del usuario
        if (usuario.getImagen() != null) {
            Utilidad.cargarImagen(usuario.getImagen().getUrl(), ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS);
        } else {
            Utilidad.cargarImagen(R.drawable.default_user, ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS);
        }

        // Resto de datos
        tvUsername.setText(usuario.getUsername());
        edtNombre.setText(usuario.getFirstName());
        edtApellidos.setText(usuario.getLastName());
        edtEmail.setText(usuario.getEmail());
    }

    private void enviarDatos() {
        cerrarActivity(Activity.RESULT_OK);
    }

    /**
     * @param codigoSalida Uno de {@link Activity#RESULT_OK} o {@link Activity#RESULT_CANCELED}
     */
    private void cerrarActivity(int codigoSalida) {
        // Set the result code and data to be returned to the calling activity
        Intent resultIntent = new Intent();
//        Activity.RESULT_OK + Activity.RESULT_CANCELED;
        setResult(codigoSalida, resultIntent);

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