package com.example.teleappsistencia;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.ui.fragments.acercaDe.AcercaDeFragment;
import com.example.teleappsistencia.ui.fragments.alarma.InsertarAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.alarma.ListarAlarmasDeHoyFragment;
import com.example.teleappsistencia.ui.fragments.alarma.ListarAlarmasFragment;
import com.example.teleappsistencia.ui.fragments.alarma.ListarAlarmasOrdenadasFragment;
import com.example.teleappsistencia.ui.fragments.alarma.ListarAlarmasSinAsignarFragment;
import com.example.teleappsistencia.ui.fragments.alarma.ListarMisAlarmasFragment;
import com.example.teleappsistencia.ui.fragments.centroSanitarioEnAlarma.InsertarCentroSanitarioEnAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.centroSanitarioEnAlarma.ListarCentrosSanitariosEnAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.centro_sanitario.FragmentInsertarCentroSanitario;
import com.example.teleappsistencia.ui.fragments.centro_sanitario.FragmentListarCentroSanitario;
import com.example.teleappsistencia.ui.fragments.centro_sanitario.FragmentModificarCentroSanitario;
import com.example.teleappsistencia.ui.fragments.clasificacionAlarma.InsertarClasificacionAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.clasificacionAlarma.ListarClasificacionAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.direccion.InsertarDireccionFragment;
import com.example.teleappsistencia.ui.fragments.direccion.ListarDireccionFragment;
import com.example.teleappsistencia.ui.fragments.dispositivos_aux.InsertarDispositivosAuxiliaresFragment;
import com.example.teleappsistencia.ui.fragments.dispositivos_aux.ListarDispositivosAuxiliaresFragment;
import com.example.teleappsistencia.ui.fragments.grupos.InsertarGruposFragment;
import com.example.teleappsistencia.ui.fragments.grupos.ListarGruposFragment;
import com.example.teleappsistencia.ui.fragments.historico_tipo_situacion.InsertarHistoricoTipoSituacionFragment;
import com.example.teleappsistencia.ui.fragments.historico_tipo_situacion.ListarHistoricoTipoSituacionFragment;
import com.example.teleappsistencia.ui.fragments.paciente.InsertarPacienteFragment;
import com.example.teleappsistencia.ui.fragments.paciente.ListarPacienteFragment;
import com.example.teleappsistencia.ui.fragments.persona.InsertarPersonaFragment;
import com.example.teleappsistencia.ui.fragments.persona.ListarPersonaFragment;
import com.example.teleappsistencia.ui.fragments.personaContactoEnAlarma.InsertarPersonaContactoEnAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.personaContactoEnAlarma.ListarPersonasContactoEnAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.recurso_comunitario.FragmentInsertarRecursoComunitario;
import com.example.teleappsistencia.ui.fragments.recurso_comunitario.FragmentListarRecursoComunitario;
import com.example.teleappsistencia.ui.fragments.recurso_comunitario.FragmentModificarRecursoComunitario;
import com.example.teleappsistencia.ui.fragments.recursosComunitariosEnAlarma.InsertarRecursosComunitariosEnAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.recursosComunitariosEnAlarma.ListarRecursosComunitariosEnAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.relacion_paciente_persona.InsertarRelacionPacientePersonaFragment;
import com.example.teleappsistencia.ui.fragments.relacion_paciente_persona.ListarRelacionPacientePersonaFragment;
import com.example.teleappsistencia.ui.fragments.relacion_terminal_recurso_comunitario.InsertarRelacionTerminalRecursoComunitarioFragment;
import com.example.teleappsistencia.ui.fragments.relacion_terminal_recurso_comunitario.ListarRelacionTerminalRecursoComunitarioFragment;
import com.example.teleappsistencia.ui.fragments.relacion_usuario_centro.InsertarRelacionUsuarioCentroFragment;
import com.example.teleappsistencia.ui.fragments.relacion_usuario_centro.ListarRelacionUsuarioCentroFragment;
import com.example.teleappsistencia.ui.fragments.terminal.InsertarTerminalFragment;
import com.example.teleappsistencia.ui.fragments.terminal.ListarTerminalFragment;
import com.example.teleappsistencia.ui.fragments.tipoAlarma.InsertarTipoAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.tipoAlarma.ListarTipoAlarmaFragment;
import com.example.teleappsistencia.ui.fragments.tipo_centro_sanitario.FragmentInsertarTipoCentroSanitario;
import com.example.teleappsistencia.ui.fragments.tipo_centro_sanitario.FragmentListarTipoCentroSanitario;
import com.example.teleappsistencia.ui.fragments.tipo_centro_sanitario.FragmentModificarTipoCentroSanitario;
import com.example.teleappsistencia.ui.fragments.tipo_modalidad_paciente.FragmentInsertarTipoModalidadPaciente;
import com.example.teleappsistencia.ui.fragments.tipo_modalidad_paciente.FragmentListarTipoModalidadPaciente;
import com.example.teleappsistencia.ui.fragments.tipo_modalidad_paciente.FragmentModificarTipoModalidadPaciente;
import com.example.teleappsistencia.ui.fragments.tipo_recurso_comunitario.FragmentInsertarTipoRecursoComunitario;
import com.example.teleappsistencia.ui.fragments.tipo_recurso_comunitario.FragmentListarTipoRecursoComunitario;
import com.example.teleappsistencia.ui.fragments.tipo_recurso_comunitario.FragmentModificarTipoRecursoComunitario;
import com.example.teleappsistencia.ui.fragments.tipo_situacion.InsertarTipoSituacionFragment;
import com.example.teleappsistencia.ui.fragments.tipo_situacion.ListarTipoSituacionFragment;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.InsertarTipoViviendaFragment;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.ListarTipoViviendaFragment;
import com.example.teleappsistencia.ui.fragments.usuarios.InsertarUsuariosFragment;
import com.example.teleappsistencia.ui.fragments.usuarios.ListarUsuariosFragment;
import com.example.teleappsistencia.ui.menu.ExpandableListAdapter;
import com.example.teleappsistencia.ui.menu.MenuModel;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Esta clase aparte de ser la clase principal, contendrá el menu con un fragment, el cual cambiará según las necesidades del usuario.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Atributo con el adapter de la lista expandible (menú).
     */
    private ExpandableListAdapter expandableListAdapter;

    /**
     * Atributo con la vista de la lista expandible (menú).
     */
    private ExpandableListView expandableListView;

    /**
     * Atributo con una lista de opciones principales (Estas opciones son las que se mostrarán primero en el menú).
     */
    private List<MenuModel> headerList = new ArrayList<>();
    private HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    private TextView textView_nombre_usuarioLogged;
    private TextView textView_email_usuarioLogged;
    private ImageView imageView_fotoPerfil;

    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Realizo una petición a la API para cargar la cabecera del menu con los datos del usuario logueado.
        loadMenuHeader();


        /* Iniciamos el servicio de notificación de Alarmas. Sólo para usuarios no admin (Teleoperadores) */
        if(!Utilidad.isAdmin()) {
            Utilidad.iniciarEscuchaAlarmas(this);
        }

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * Método que carga los datos del usuario en la cabezera del menú.
     */
    private void loadMenuHeader() {
        // Recogo el NavigationView para poder asignar los datos.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        textView_nombre_usuarioLogged = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_nombre_usuarioLogged);
        textView_email_usuarioLogged = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_email_usuarioLogged);
        imageView_fotoPerfil = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView_usuario);

        Usuario usuario = Utilidad.getUserLogged();    // Recogo el usuario de la clase Utils.
        if (usuario != null) {  // Si existe el usuario.
            // Le asigno el nombre y apellidos.
            textView_nombre_usuarioLogged.setText(usuario.getFirstName() + Constantes.ESPACIO_EN_BLANCO + usuario.getLastName());
            textView_email_usuarioLogged.setText(usuario.getEmail());

            if(usuario.getImagen() != null) {  // Si el usuario cuenta con una imagen.
                String img_url = usuario.getImagen().getUrl(); // Recogo la imagen del usuario.

                Picasso.get()       // LLamo a Picasso para poder asignar una imagen por URL.
                        .load(img_url)  // Cargo la URL.
                        .error(R.drawable.rounded_default_user) // Si sucede un error se utiliza la imagen por defecto.
                        .resize(78, 80)
                        .centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(imageView_fotoPerfil, new Callback() {
                            @Override
                            public void onSuccess() {
                                System.out.println("\nPerfe\n");
                            }

                            @Override
                            public void onError(Exception e) {
                                System.out.println("\nError\n");
                                e.printStackTrace();
                            }
                        }); // Carga la imagen en el imageView.
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Método que se encarga de añadir las opciones y sub-opciones al menú.
     */
    private void prepareMenuData() {
        String[] childNames = {Constantes.SUBMENU_INSERTAR, Constantes.SUBMENU_LISTAR, Constantes.SUBMENU_MODIFICAR}; // Nombres de las sub-opciones
        List<MenuModel> childModelsList; // Lista para las sub-opciones
        MenuModel menuModel; // Modelo de la opción.


        // Menu Alarmas.
        // Las alarmas están ordenadas por abiertas y por hora de registro
        menuModel = new MenuModel(getResources().getString(R.string.menu_alarmas), false, false, new ListarAlarmasOrdenadasFragment());
        headerList.add(menuModel);



        // Menu Alarma.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_alarma), true, true, null);
        headerList.add(menuModel);

        if(Utilidad.isAdmin()) {    // Si el usuario es admin se mostrarán esta opcion.
            childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarAlarmaFragment()));
        }
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarAlarmasFragment()));
        childModelsList.add(new MenuModel(Constantes.SIN_ASIGNAR, false, false, new ListarAlarmasSinAsignarFragment()));
        childModelsList.add(new MenuModel(Constantes.MIS_ALARMAS, false, false, new ListarMisAlarmasFragment()));
        childModelsList.add(new MenuModel(Constantes.ALARMAS_DE_HOY, false, false, new ListarAlarmasDeHoyFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Centro_Sanitario.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_centroSanitario), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new FragmentInsertarCentroSanitario()));
        childModelsList.add(new MenuModel(childNames[2], false, false, new FragmentModificarCentroSanitario()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new FragmentListarCentroSanitario()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else {
            childList.put(menuModel, null);
        }

        // Menu Centro Sanitario en Alarma.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_centro_sanitario_en_alarma), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarCentroSanitarioEnAlarmaFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarCentrosSanitariosEnAlarmaFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Clasificacion Alarma.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_clasificacion_alarma), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarClasificacionAlarmaFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarClasificacionAlarmaFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Dirección.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_direccion), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarDireccionFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarDireccionFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else {
            childList.put(menuModel, null);
        }

        // Menu Dispositivos Auxiliares.
        if(Utilidad.isAdmin()) {    // Si el usuario es admin se mostrarán estas opciones.
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(getResources().getString(R.string.menu_dispositivos_auxiliares_terminal), true, true, null);
            headerList.add(menuModel);
            childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarDispositivosAuxiliaresFragment()));
            childModelsList.add(new MenuModel(childNames[1], false, false, new ListarDispositivosAuxiliaresFragment()));

            if (menuModel.hasChildren()) {
                childList.put(menuModel, childModelsList);
            } else {
                childList.put(menuModel, null);
            }
        }

        // Menu Grupos.
        if(Utilidad.isAdmin()) {    // Si el usuario es admin se mostrarán estas opciones.
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(getResources().getString(R.string.menu_grupos), true, true, null);
            headerList.add(menuModel);
            childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarGruposFragment()));
            childModelsList.add(new MenuModel(childNames[1], false, false, new ListarGruposFragment()));

            if (menuModel.hasChildren()) {
                childList.put(menuModel, childModelsList);
            } else {
                childList.put(menuModel, null);
            }
        }

        // Menu Histórico Tipo Situación.
        if(Utilidad.isAdmin()) {    // Si el usuario es admin se mostrarán estas opciones.
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(getResources().getString(R.string.menu_historico_tipo_situacion), true, true, null);
            headerList.add(menuModel);
            childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarHistoricoTipoSituacionFragment()));
            childModelsList.add(new MenuModel(childNames[1], false, false, new ListarHistoricoTipoSituacionFragment()));

            if (menuModel.hasChildren()) {
                childList.put(menuModel, childModelsList);
            } else {
                childList.put(menuModel, null);
            }
        }

        // Menu Paciente.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_paciente), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarPacienteFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarPacienteFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Personas.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_persona), true, true, null); // Se crea una opción pricipal.
        headerList.add(menuModel);  // Se añe a la lista de opciones principales.
        // Se crean las dos sub-opciones insertar y listar.
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarPersonaFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarPersonaFragment()));

        if (menuModel.hasChildren()) { // Si la opción principal tiene sub-opciones.
            childList.put(menuModel, childModelsList); // Se le añade al atributo con todas las opciones,
            // la opción principal y sus sub-opciones.
        } else{
            childList.put(menuModel, null);
        }

        // Menu Persona de contacto en alarma.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_persona_de_contacto_en_alarma), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarPersonaContactoEnAlarmaFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarPersonasContactoEnAlarmaFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Recurso_Comunitario.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_recursoComunitario), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new FragmentInsertarRecursoComunitario()));
        childModelsList.add(new MenuModel(childNames[2], false, false, new FragmentModificarRecursoComunitario()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new FragmentListarRecursoComunitario()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else {
            childList.put(menuModel, null);
        }

        // Menu Recursos Comunitarios en Alarma.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_recursos_comunitarios_en_alarma), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarRecursosComunitariosEnAlarmaFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarRecursosComunitariosEnAlarmaFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Relacion Paciente Persona.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_relacion_paciente_persona), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarRelacionPacientePersonaFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarRelacionPacientePersonaFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Relación Terminal Recurso Comunitario.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_relacion_terminal_recurso_comunitario), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarRelacionTerminalRecursoComunitarioFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarRelacionTerminalRecursoComunitarioFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Relacion Usuario Centro.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_relacion_usuario_centro), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarRelacionUsuarioCentroFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarRelacionUsuarioCentroFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Terminal.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_terminal), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarTerminalFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarTerminalFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Tipo Alarma.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_tipo_alarma), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarTipoAlarmaFragment()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new ListarTipoAlarmaFragment()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else{
            childList.put(menuModel, null);
        }

        // Menu Tipo_Centro_Sanitario.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_tipoCentroSanitario), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new FragmentInsertarTipoCentroSanitario()));
        childModelsList.add(new MenuModel(childNames[2], false, false, new FragmentModificarTipoCentroSanitario()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new FragmentListarTipoCentroSanitario()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else {
            childList.put(menuModel, null);            // De lo contrario se le añade solo la opción principal.
        }

        // Menu Tipo Situación.
        if(Utilidad.isAdmin()) {    // Si el usuario es admin se mostrarán estas opciones.
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(getResources().getString(R.string.menu_tipo_situacion), true, true, null);
            headerList.add(menuModel);
            childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarTipoSituacionFragment()));
            childModelsList.add(new MenuModel(childNames[1], false, false, new ListarTipoSituacionFragment()));

            if (menuModel.hasChildren()) {
                childList.put(menuModel, childModelsList);
            } else {
                childList.put(menuModel, null);
            }
        }

        // Menu Tipo Vivienda.
        if(Utilidad.isAdmin()) {    // Si el usuario es admin se mostrarán estas opciones.
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(getResources().getString(R.string.menu_tipo_vivienda), true, true, null);
            headerList.add(menuModel);
            childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarTipoViviendaFragment()));
            childModelsList.add(new MenuModel(childNames[1], false, false, new ListarTipoViviendaFragment()));

            if (menuModel.hasChildren()) {
                childList.put(menuModel, childModelsList);
            } else {
                childList.put(menuModel, null);
            }
        }

        // Menu Tipo_Modidalidad_Paciente.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_tipoModalidadPaciente), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new FragmentInsertarTipoModalidadPaciente()));
        childModelsList.add(new MenuModel(childNames[2], false, false, new FragmentModificarTipoModalidadPaciente()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new FragmentListarTipoModalidadPaciente()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else {
            childList.put(menuModel, null);
        }

        // Menu Tipo_Recurso_Comunitario.
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getResources().getString(R.string.menu_tipoRecursoComunitario), true, true, null);
        headerList.add(menuModel);
        childModelsList.add(new MenuModel(childNames[0], false, false, new FragmentInsertarTipoRecursoComunitario()));
        childModelsList.add(new MenuModel(childNames[2], false, false, new FragmentModificarTipoRecursoComunitario()));
        childModelsList.add(new MenuModel(childNames[1], false, false, new FragmentListarTipoRecursoComunitario()));

        if (menuModel.hasChildren()) {
            childList.put(menuModel, childModelsList);
        } else {
            childList.put(menuModel, null);
        }

        // Menu Usuarios.
        if(Utilidad.isAdmin()) {  // Si el usuario es admin se mostrarán estas opciones.
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(getResources().getString(R.string.menu_usuarios), true, true, null);
            headerList.add(menuModel);
            childModelsList.add(new MenuModel(childNames[0], false, false, new InsertarUsuariosFragment()));
            childModelsList.add(new MenuModel(childNames[1], false, false, new ListarUsuariosFragment()));

            if (menuModel.hasChildren()) {
                childList.put(menuModel, childModelsList);
            } else {
                childList.put(menuModel, null);
            }

        }

        // Menu Acerca De.
        menuModel = new MenuModel(getResources().getString(R.string.menu_acercaDe), false, false, new AcercaDeFragment());
        headerList.add(menuModel);

    }

    /**
     * Método que define que se hará cuando el ususario pulse una opción.
     */
    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);


        // Aquí se define que pasará cuando el usuario pulse en una de las opciones principales.
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                MenuModel menuModel = headerList.get(groupPosition);
                Fragment fragment = menuModel.getFragment();

                // Carga el fragmento si existe
                if (fragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                /*if (headerList.get(groupPosition).isGroup()) {
                    if (!headerList.get(groupPosition).hasChildren()) {
                        // En este caso no hay nada que hacer al pulsar en una opción principal.
                    }
                }*/
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });


        // Aquí se define que pasará cuando el usuario pulse en una de las sub-opciones.
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, model.getFragment())
                            .addToBackStack(null)
                            .commit();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
    }


}
