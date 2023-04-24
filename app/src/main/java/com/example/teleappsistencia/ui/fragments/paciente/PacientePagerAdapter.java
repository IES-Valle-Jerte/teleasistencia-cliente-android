package com.example.teleappsistencia.ui.fragments.paciente;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PacientePagerAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 4;

    public PacientePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DatosPersonalesFragment();
            case 1:
                return new DatosSanitariosFragment();
            case 2:
                return new DatosViviendaFragment();
            case 3:
                return new ContactosPacienteFragment();
            default:
                System.out.println("null------------------------------------------");
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
