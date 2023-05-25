package com.example.teleappsistencia.ui.fragments.recursos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;

/**
 * Un simple {@link Fragment} subclass.
 * Utiliza el método de fábrica {@link recursos_card#newInstance} para
 * crear una instancia de este fragmento.
 */
public class recursos_card extends Fragment {

    /**
     * Constructor por defecto.
     */
    public recursos_card() {

    }

    /**
     * Utiliza este método de fábrica para crear una nueva instancia de
     * este fragmento utilizando los parámetros proporcionados.
     *
     * @return Una nueva instancia de recurso_card.
     */
    public static recursos_card newInstance() {
        recursos_card fragment = new recursos_card();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Se encarga de la inicialización del fragment y, al llamar a
     * super.onCreate(savedInstanceState), permite que la implementación base del fragmento
     * realice las tareas de inicialización necesarias.
     *
     * @param savedInstanceState Si el fragmento está siendo recreado a partir de un estado
     * previamente guardado, este es el estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Método que inicializa la vista.
     *
     * @param inflater El objeto LayoutInflater que se puede utilizar para inflar
     * cualquier vista en el fragmento.
     * @param container Si no es nulo, esta es la vista principal a la que la interfaz
     * de usuario del fragmento debe estar adjunta. El fragmento no debe agregar la vista
     * por sí mismo, pero esto se puede usar para generar los LayoutParams de la vista.
     * @param savedInstanceState Si no es nulo, este fragmento se está reconstruyendo
     * a partir de un estado previamente guardado como se indica aquí.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
            Se guarda la vista.
         */
        View root = inflater.inflate(R.layout.fragment_recursos_card, container, false);

        /*
            Inflate the layout for this fragment
         */
        return root;
    }
}
