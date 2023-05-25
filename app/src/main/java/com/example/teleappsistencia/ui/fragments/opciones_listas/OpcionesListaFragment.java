package com.example.teleappsistencia.ui.fragments.opciones_listas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.teleappsistencia.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpcionesListaFragment} factory method to
 * create an instance of this fragment.
 */
public class OpcionesListaFragment extends Fragment {
    //Interfaz con las acciones de los botones
    public interface OnButtonClickListener {
        void onViewDetailsButtonClicked();
        void onDeleteButtonClicked();
        void onEditButtonClicked();
    }

    private OnButtonClickListener mListener;

    public OpcionesListaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_opciones_lista, container, false);

        // Asignar listeners a los botones
        ImageButton viewDetailsButton = view.findViewById(R.id.view_details_button);

        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onViewDetailsButtonClicked();
            }
        });

        ImageButton deleteButton = view.findViewById(R.id.delete_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeleteButtonClicked();
            }
        });

        ImageButton editButton = view.findViewById(R.id.edit_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEditButtonClicked();
            }
        });

        return view;
    }
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

}
