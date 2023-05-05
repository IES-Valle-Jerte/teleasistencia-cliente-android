package com.example.teleappsistencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        // TODO: Ocultar ciertos campos si usuario no pertenece al grupo "administrador"
        // Extraer referencias
        getReferenciasGUI();
        cargarDatosUser();

        // Conectar acciones
        btnGuardar.setOnClickListener(_v -> enviarDatos());
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
}