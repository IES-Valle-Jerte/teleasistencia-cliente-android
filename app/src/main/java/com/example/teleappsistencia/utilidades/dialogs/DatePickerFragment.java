package com.example.teleappsistencia.utilidades.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Clase encargada de crear un Dialog para elegir una fecha.
 */
public class DatePickerFragment extends DialogFragment {

    /**
     * Listener OnDateSetListener del DatePickerDialog.
     */
    private DatePickerDialog.OnDateSetListener listener;

    /**
     * Método para crear una nueva instancia del DatePickerFragment.
     * @param listener
     * @return
     */
    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    /**
     * Setter para asignar el listener.
     * @param listener
     */
    private void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Se utiliza la fecha actual como valor por defecto.
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea una nueva instancia del DatePicketDialog y lo devuelve.
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
