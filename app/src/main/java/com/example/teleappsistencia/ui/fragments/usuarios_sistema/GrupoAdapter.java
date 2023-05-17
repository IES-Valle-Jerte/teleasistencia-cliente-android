package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.teleappsistencia.modelos.Grupo;

import java.util.List;
import java.util.Locale;


/**
 * Adaptador para cargar objetos Grupo en un Spinner
 */
public class GrupoAdapter extends ArrayAdapter<Grupo> {
    public GrupoAdapter(Context context, List<Grupo> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        vistaGrupo(position, view);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        vistaGrupo(position, view);
        return view;
    }

    private void vistaGrupo(int position, View view) {
        TextView textView = view.findViewById(android.R.id.text1);
        Grupo grupo = getItem(position);
        if (grupo != null) {
            // Poner primera letra mayuscula y resto minuscula
            String name = grupo.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            // Poner texto a mostrar
            textView.setText(name);
        }
    }
}
