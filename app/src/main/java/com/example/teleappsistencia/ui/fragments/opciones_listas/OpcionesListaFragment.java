package com.example.teleappsistencia.ui.fragments.opciones_listas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.teleappsistencia.R;
import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpcionesListaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpcionesListaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpcionesListaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpcionesListaFragment newInstance(String param1, String param2) {
        OpcionesListaFragment fragment = new OpcionesListaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_opciones_listas, container, false);
        // Asignar listeners a los botones
        Button viewDetailsButton = view.findViewById(R.id.view_details_button);
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onViewDetailsButtonClicked();
            }
        });

        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeleteButtonClicked();
            }
        });

        Button editButton = view.findViewById(R.id.edit_button);
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