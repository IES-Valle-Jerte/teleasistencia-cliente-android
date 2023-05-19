package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Grupo;
import com.example.teleappsistencia.modelos.ProfilePatch;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarUsuarioSistemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarUsuarioSistemaFragment extends Fragment {
    private static final String ARG_USUARIO = "usuario";
    private static final String ARG_EDITMODE = "editMode";

    private Usuario usuario;
    private ProfilePatch patches; // Usar en
    private boolean editMode;

    private List<Grupo> grupos;
    private GrupoAdapter adapter;

    // Referencias GUI
    SwitchCompat switchActivo;
    TextView tvId, tvUrl;
    ImageView ivFotoPerfil;
    ImageButton btnCargarFoto;
    Spinner spinRoles;
    EditText edtUsername, edtNombre, edtApellidos, edtEmail;
    EditText edtNuevaPassword, edtRepetirNuevaPassword;
    Button btnGuardar, btnCancelar;

    // Para la petición de permisos
    ActivityResultLauncher<Intent> getImagenLauncher;

    // Permisos necesarios
    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    );
    private boolean tienePermisos = false;

    // Required empty public constructor
    public EditarUsuarioSistemaFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuario Usuario a modificar
     * @param editMode booleano que especifica si se quiere modificar o crear un nuevo usuario (para reutilizar el fragment)
     * @return A new instance of fragment EditarUsuarioSistemaFragment.
     */
    public static EditarUsuarioSistemaFragment newInstance(Usuario usuario, boolean editMode) {
        EditarUsuarioSistemaFragment fragment = new EditarUsuarioSistemaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USUARIO, usuario);
        args.putBoolean(ARG_EDITMODE, editMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(ARG_USUARIO);
            editMode = getArguments().getBoolean(ARG_EDITMODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initIntentLaunchers();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_usuario_sistema, container, false);

        getReferenciasGUI(view);
        // Conectar acciones
        btnCargarFoto.setOnClickListener(_v -> pedirFoto());
        btnGuardar.setOnClickListener(_v -> guardar());
        btnCancelar.setOnClickListener(_v -> cerrarFragment());

        // Cargar opciones de los spinners y después cargar los datos del usuario
        cargarOpcionesSpinnerRoles();

        return view;
    }

    /**
     * Extrae las referencias a las distintas vistas del layout
     */
    private void getReferenciasGUI(View view) {
        switchActivo = view.findViewById(R.id.switchActivo);

        tvId = view.findViewById(R.id.tvId);
        tvUrl = view.findViewById(R.id.tvUrl);
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil);
        btnCargarFoto = view.findViewById(R.id.btnCargarFoto);
        spinRoles = view.findViewById(R.id.spinRoles);

        edtUsername = view.findViewById(R.id.edtUsername);
        edtNombre = view.findViewById(R.id.edtNombre);
        edtApellidos = view.findViewById(R.id.edtApellidos);
        edtEmail = view.findViewById(R.id.edtEmail);

        edtNuevaPassword = view.findViewById(R.id.edtNuevaPassword);
        edtRepetirNuevaPassword = view.findViewById(R.id.edtRepetirNuevaPassword);

        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);
    }

    /**
     * Carga los distintos grupos como opciones del spinner.
     * Solo tendremos la opción de Administrador si somos administradores también, o el user es admin.
     */
    private void cargarOpcionesSpinnerRoles() {
        APIService service = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Grupo>> call = service.getGrupos(Utilidad.getAuthorization());

        call.enqueue(new Callback<List<Grupo>>() {
            @Override
            public void onResponse(Call<List<Grupo>> call, Response<List<Grupo>> response) {
                if (response.isSuccessful()) {
                    grupos = response.body();

                    // Si no somos administradores, quitaremos la opción de ser administrador
                    // A menos que estemos modificando un usuario y sea administrador
                    if (!(Utilidad.isSuperUser() || Utilidad.isSuperUser(usuario))
                    ) {
                        grupos = grupos.stream()
                            // Filtramos todos los grupos que no sean el de administrador
                            .filter(g -> !g.getName().equalsIgnoreCase(Constantes.ADMINISTRADOR))
                            // Volvemos a parar a una lista de objetos
                            .collect(Collectors.toCollection(ArrayList::new));
                    }

                    // Cargar datos en el Spinner
                    adapter = new GrupoAdapter(getContext(), grupos);

                    spinRoles.setPrompt("");
                    spinRoles.setAdapter(adapter);

                    // Encontrar el rol de Teleoperador
                    int teleoperadorIndex = IntStream.range(0, grupos.size())
                        // Los grupos sacados de APIService.getGrupos() tienen PK
                        // y los grupos de los Usuarios tienen ID
                        .filter(i -> grupos.get(i).getName().equalsIgnoreCase("teleoperador"))
                        .findFirst().orElse(-1);
                    spinRoles.setSelection(teleoperadorIndex);

                    // Proceder a cargar los datos del usuario
                    cargarDatosUser();
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Grupo>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga los datos del Usuario en las distintas vistas.
     * No hace nada en el caso de que {@link #editMode} sea false.
     *
     */
    private void cargarDatosUser() {
        patches = new ProfilePatch(usuario);

        adaptarViews();
        cargarImagenUser();

        // No cargaremos ningun dato si estamos creando un nuevo usuario
        if (editMode) {
            switchActivo.setChecked(null != usuario.getActive() && usuario.getActive());

            tvId.setText(String.format("ID: %d", usuario.getPk()));
            tvUrl.setText(usuario.getUrl());

            // Seleccionar grupo
            Grupo grupoUser = Utilidad.getGrupoUser(usuario);
            if (null != grupoUser) {
                // Creamos un "iterador" para los grupos
                int groupIndex = IntStream.range(0, grupos.size())
                    // Los grupos sacados de APIService.getGrupos() tienen PK
                    // y los grupos de los Usuarios tienen ID
                    .filter(i -> grupoUser.getId() == grupos.get(i).getPk())
                    .findFirst().orElse(-1);

                // Si encontramos el grupo, lo ponemos en el
                if (groupIndex >= 0) spinRoles.setSelection(groupIndex);
            }

            // Resto de campos
            edtUsername.setText(usuario.getUsername());
            edtNombre.setText(usuario.getFirstName());
            edtApellidos.setText(usuario.getLastName());
            edtEmail.setText(usuario.getEmail());
        }
    }

    /**
     * Carga la imagen de perfil del usuario en el ImageView correspondiente.
     */
    private void cargarImagenUser() {
        // Nueva imagen cargada por el usuario
        if (null != patches.getNuevaFotoPerfil())
            Utilidad.cargarImagen(patches.getNuevaFotoPerfil(), ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS_LISTA);

        // Imagen del usuario
        else if (null != usuario.getImagen())
            Utilidad.cargarImagen(usuario.getImagen().getUrl(), ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS_LISTA);

        // Imagen por defecto si no tiene
        else Utilidad.cargarImagen(R.drawable.default_user, ivFotoPerfil, Constantes.IMG_PERFIL_RADIOUS_LISTA);

    }

    /**
     * Adapta el layout del fragment dependiendo de {@link #editMode}.
     * Además oculta y/o desactiva ciertas vistas dependiendo de isAdmin() y isSuperUser().
     */
    private void adaptarViews() {
        // Si estamos en modo de creación de usuario
        if (editMode) {
            // Si modificamos un administrador y no lo somos, bloqueamos el spinner
            if (Utilidad.isSuperUser(usuario) && !Utilidad.isSuperUser()) {
                spinRoles.setEnabled(false);
            }
        } else {
            btnGuardar.setText(R.string.modificar_perfil_btnGuardarNuevoUsuario);
            edtNuevaPassword.setHint(R.string.modificar_perfil_tvPass);
            switchActivo.setVisibility(View.GONE);
            tvId.setVisibility(View.GONE);
            tvUrl.setVisibility(View.GONE);
        }
    }

    private void cerrarFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void guardar() {
        // Cargar los cambios
        patches.setActive(switchActivo.isChecked());
        patches.setNuevoUsername(edtUsername.getText().toString().trim());
        patches.setNuevoGrupo(((Grupo) spinRoles.getSelectedItem()).getPk());
        patches.setNuevoNombre(edtNombre.getText().toString().trim());
        patches.setNuevosApellidos(edtApellidos.getText().toString().trim());
        patches.setNuevoEmail(edtEmail.getText().toString().trim());
        patches.setNuevaPassword(edtNuevaPassword.getText().toString());

        // Procesar
        if (editMode) actualizarUsuario();
        else crearUsuario();
    }

    private void crearUsuario() {
        if (patches.hasPatches()) {
            if (validarDatos()) {
                Call<Usuario> call = patches.createMultipartPostAPICall(getContext());
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()) {
                            Usuario userModificado = response.body();
                            Utilidad.setUserLogged(userModificado);
                            Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_OK_CREAR, Toast.LENGTH_SHORT).show();
                            cerrarFragment();
                        } else {
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_ERROR_CREAR, Toast.LENGTH_SHORT).show();
                        cerrarFragment();
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_EMPTY_PATCHES, Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarUsuario() {
        // Si se han hecho alguna modificación continualmos
        if (patches.hasPatches()) {
            if (validarDatos()) {
                Call<Usuario> call = patches.createMultipartPatchAPICall(getContext(), false);
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()) {
                            Usuario userModificado = response.body();
                            Utilidad.setUserLogged(userModificado);
                            Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_OK_EDITAR, Toast.LENGTH_SHORT).show();
                            cerrarFragment();
                        } else {
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_ERROR_EDITAR, Toast.LENGTH_SHORT).show();
                        cerrarFragment();
                    }
                });
            }
        } else cerrarFragment();
    }

    /**
     * Intenta validar todos los campos, si hay probelmas con alguno lo notificará.
     * @return true si todos los campos son válidos.
     */
    private boolean validarDatos() {
        // Quitarle el username cuando modificamos, o no le porle un username cuando creamos
        if (edtUsername.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_EMPTY_USERNAME, Toast.LENGTH_SHORT).show();
            return false;
        }
        // Nombre no relleno
        else if (edtNombre.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_EMPTY_FIRSTNAME, Toast.LENGTH_SHORT).show();
            return false;
        }
        // Apellidos no rellenos
        else if (edtApellidos.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_EMPTY_LASTNAME, Toast.LENGTH_SHORT).show();
            return false;
        }
        // Email relleno e invalido
        else if (!Utilidad.validarFormatoEmail(edtEmail)) {
            Toast.makeText(getContext(), Constantes.TOAST_USUARIOSISTEMA_EMPTY_INVALID_MAIL, Toast.LENGTH_SHORT).show();
            return false;
        }
        else return validarNuevaPassword();
    }

    /**
     * Intenta validar los EditText de las contaseñas (obligatorias si {@link #editMode} es false).
     *
     * @return true si son válidas.
     * @see Utilidad#validatePassword(EditText)
     */
    private boolean validarNuevaPassword() {
        // Nos saltamos la verificación si no se intenta cambiar la contraseña durante la modificación
        if (editMode && null == patches.getNuevaPassword()) return true;

        String pass1 = edtNuevaPassword.getText().toString(),
               pass2 = edtRepetirNuevaPassword.getText().toString();

        if (pass1.trim().isEmpty() && pass2.trim().isEmpty()) {
            Toast.makeText(getContext(), Constantes.TOAST_MODPERFIL_CAMBIOPASS_INVALID_NOPASS, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!pass1.equals(pass2)) {
            Toast.makeText(getContext(), Constantes.TOAST_MODPERFIL_CAMBIOPASS_INVALID_DIFFERENT, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Utilidad.validatePassword(edtNuevaPassword) || !Utilidad.validatePassword(edtRepetirNuevaPassword)) {
            Toast.makeText(getContext(), Constantes.TOAST_MODPERFIL_CAMBIOPASS_INVALID, Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
    }

    // =============================== Para peticion de ficheros ===============================
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
     * Solicita al usuario los permisos necesarios que no tengamos ya.
     * @return Ddevuelve true solo si no se han tenido que pedir permisos.
     */
    private boolean pedirPermisos() {
        if (!tienePermisos) {
            List<String> missing = new ArrayList<>();

            // Anotar los permitos que faltan / si faltan
            for (String permission : REQUIRED_PERMISSIONS) {
                int permissionCheck = ActivityCompat.checkSelfPermission(getContext(), permission);
                if (PackageManager.PERMISSION_DENIED == permissionCheck ) {
                    missing.add(permission);
                }
            }

            tienePermisos = missing.isEmpty();
            // Si hay permisos que nos falten, solicitarlos
            if (!tienePermisos) {
                String[] _missing = new String[missing.size()];
                missing.toArray(_missing);
                ActivityCompat.requestPermissions(getActivity(), _missing, 255);
            }
        }

        return tienePermisos;
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
                Toast.makeText(getContext(), Constantes.TOAST_MODPERFIL_PERMISO_NECESARIO, Toast.LENGTH_SHORT).show();
            }
        }
    }
}