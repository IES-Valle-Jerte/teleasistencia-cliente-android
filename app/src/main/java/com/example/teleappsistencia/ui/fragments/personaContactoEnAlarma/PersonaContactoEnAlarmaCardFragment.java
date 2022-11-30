package com.example.teleappsistencia.ui.fragments.personaContactoEnAlarma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonaContactoEnAlarmaCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonaContactoEnAlarmaCardFragment extends Fragment {

    public PersonaContactoEnAlarmaCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PersonaContactoEnAlarmaCardFragment.
     */

    public static PersonaContactoEnAlarmaCardFragment newInstance() {
        PersonaContactoEnAlarmaCardFragment fragment = new PersonaContactoEnAlarmaCardFragment();
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
        return inflater.inflate(R.layout.fragment_persona_contacto_en_alarma_card, container, false);
    }
}