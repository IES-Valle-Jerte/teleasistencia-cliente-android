package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.opciones_listas.OpcionesListaFragment;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsuariosSistemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsuariosSistemaFragment extends Fragment implements OpcionesListaFragment.OnButtonClickListener, UsuarioSistemaAdapter.OnItemSelectedListener {
    // ! REFEREENCIAS GUI
    private Button btnNewUser;
    private RecyclerView recycler;

    private ConstraintLayout layoutContenido;
    private ShimmerFrameLayout shimmerPlaceholder;

    // Otras Variables
    private OpcionesListaFragment frag_acciones;
    private UsuarioSistemaAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    static List<Usuario> lUsuarios;
    private int selectedPosition = RecyclerView.NO_POSITION;

    // Constructor público vacío requerido por newInstance()
    public UsuariosSistemaFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UsuariosSistemaFragment.
     */
    public static UsuariosSistemaFragment newInstance() {
        UsuariosSistemaFragment fragment = new UsuariosSistemaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    /**
     * Cuando se infle la vista del fragment, insertamos el OpcionesListaFragment, conectamos los eventos
     * de los botones y los listeners, configuramos el RecyclerView y mostramos el Shimmer
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuarios_sistema, container, false);
        lUsuarios = new ArrayList<>();

        // Extraer referencias GUI
        btnNewUser = view.findViewById(R.id.btnNewUser);
        recycler   = view.findViewById(R.id.lv_recyclerView);
        layoutContenido = view.findViewById(R.id.layoutContenido);
        shimmerPlaceholder = view.findViewById(R.id.shimmerPlaceholder);
        frag_acciones = new OpcionesListaFragment();

        // Cargar fragment de acciones
        getActivity().getSupportFragmentManager().beginTransaction()
            .add(R.id.fragView_botonesAcciones, frag_acciones)
            .commit();

        // Configurar acciones
        btnNewUser.setOnClickListener(v -> cargarFragmentNuevoUsuario());
        frag_acciones.setOnButtonClickListener(this);

        // Configurar el RecyclerView
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Cargar datos en el RecyclerView
        cargarUsuarios();

        return view;
    }

    /**
     * Método para obtener una lista de usuarios de la API para luego mostrarlos en un RecyclerView
     */
    private void cargarUsuarios() {
        // Mostrar el Shimmer mientras cargan los datos
        iniciarShimmer();

        // Empezar la llamada a la API
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<Usuario>> call = apiService.getUsuarios(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    lUsuarios = response.body();

                    // Crear Adaptador para el RecyclerView
                    adapter = new UsuarioSistemaAdapter(lUsuarios, UsuariosSistemaFragment.this);
                    recycler.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

                // Detener el shimmer
                detenerShimmer();
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                t.printStackTrace();

                // Detener el shimmer
                detenerShimmer();
            }
        });
    }

    /**
     * Muestra e inicia la animación del Shimmer.
     */
    private void iniciarShimmer() {
        layoutContenido.setVisibility(View.GONE);
        shimmerPlaceholder.startShimmer();
        shimmerPlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Finaliza la animación del Shimmer y lo oculta.
     */
    private void detenerShimmer() {
        shimmerPlaceholder.stopShimmer();
        shimmerPlaceholder.setVisibility(View.GONE);
        layoutContenido.setVisibility(View.VISIBLE);
    }

    // ! Métodos del UsuarioSistemaAdapter
    @Override
    public void onItemSelected(int position) {
        selectedPosition = position;
    }

    private void cargarFragmentNuevoUsuario() {
        Usuario newUser = new Usuario();
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_fragment, EditarUsuarioSistemaFragment.newInstance(
                newUser, false
            ))
            .addToBackStack(null).commit();
    }

    // ! Acciones del OpcionesListaFragment
    @Override
    public void onViewDetailsButtonClicked() {
        Usuario user = adapter.getUsuarioSelecionado();
        if (user != null) {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, DetallesUsuarioSistemaFragment.newInstance(user))
                .addToBackStack(null).commit();
        }
    }

    @Override
    public void onEditButtonClicked() {
        Usuario user = adapter.getUsuarioSelecionado();
        if (user != null) {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, EditarUsuarioSistemaFragment.newInstance(
                    user, true
                ))
                .addToBackStack(null).commit();
        }
    }

    @Override
    public void onDeleteButtonClicked() {
        Usuario user = adapter.getUsuarioSelecionado();

        if (user != null) {
            new AlertDialog.Builder(getContext())
                .setMessage(Constantes.MSG_CONFIRMAR_ELEIMINAR_USUARIO_SISTEMA)
                .setPositiveButton("Si", (dialog, which) -> {
                    APIService service = ClienteRetrofit.getInstance().getAPIService();
                    Call<String> call = service.deleteUser(user.getPk(), Utilidad.getAuthorization());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                            if (response.isSuccessful()) {
                                lUsuarios.remove(selectedPosition);
                                recycler.getAdapter().notifyItemRemoved(selectedPosition);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
        }
    }
}