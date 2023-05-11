package com.example.teleappsistencia.ui.fragments.acercaDe;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Tecnologia;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DesarrolladorCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DesarrolladorCardFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context context;

    List<Tecnologia> lTecnologias;

    private RecyclerView recycler;

    TecnologiaAdapter adapter;

    private RecyclerView.LayoutManager lManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DesarrolladorCardFragment() {
        // Required empty public constructor
    }

    public DesarrolladorCardFragment(TecnologiaAdapter adapter, RecyclerView recyclerView, RecyclerView.LayoutManager lManager, List<Tecnologia> lTecnologias) {
       this.adapter = adapter;
       this.recycler = recyclerView;
       this.lManager = lManager;
       this.lTecnologias = lTecnologias;
    }

    public List<Tecnologia> getlTecnologias() {
        return lTecnologias;
    }

    public void setlTecnologias(List<Tecnologia> lTecnologias) {
        this.lTecnologias = lTecnologias;
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

    public void setRecycler(RecyclerView recycler) {
        this.recycler = recycler;
    }

    public TecnologiaAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TecnologiaAdapter adapter) {
        this.adapter = adapter;
    }

    public RecyclerView.LayoutManager getlManager() {
        return lManager;
    }

    public void setlManager(RecyclerView.LayoutManager lManager) {
        this.lManager = lManager;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment alumnosCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DesarrolladorCardFragment newInstance(String param1, String param2) {
        DesarrolladorCardFragment fragment = new DesarrolladorCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desarrollador_card, container, false);
        return root;
    }


}