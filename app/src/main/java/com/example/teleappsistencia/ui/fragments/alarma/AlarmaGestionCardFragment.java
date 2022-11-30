package com.example.teleappsistencia.ui.fragments.alarma;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmaGestionCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmaGestionCardFragment extends Fragment {



    public AlarmaGestionCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlarmaCardFragment.
     */

    public static AlarmaGestionCardFragment newInstance() {
        AlarmaGestionCardFragment fragment = new AlarmaGestionCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarma_gestion_card, container, false);
    }
}