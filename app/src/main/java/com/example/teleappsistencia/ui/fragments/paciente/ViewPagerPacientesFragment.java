package com.example.teleappsistencia.ui.fragments.paciente;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.teleappsistencia.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPagerPacientesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerPacientesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TableLayout tableLayout;
    private ViewPager2 viewPager;

    PacientePagerAdapter adapter;
    private DatosPersonalesFragment datosPersonalesFragment;
    private DatosSanitariosFragment datosSanitariosFragment;

    public ViewPagerPacientesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerPacientesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerPacientesFragment newInstance(String param1, String param2) {
        ViewPagerPacientesFragment fragment = new ViewPagerPacientesFragment();
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
        View v= inflater.inflate(R.layout.fragment_view_pager_pacientes, container, false);
        tableLayout=v.findViewById(R.id.tab_layout_pacientes);
        datosPersonalesFragment = new DatosPersonalesFragment();
        datosSanitariosFragment = new DatosSanitariosFragment();
        /*viewPager=v.findViewById(R.id.view_pager_pacientes);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);*/
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager_pacientes);
        adapter = new PacientePagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }
}