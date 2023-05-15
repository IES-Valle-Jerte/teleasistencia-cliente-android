package com.example.teleappsistencia.utilidades.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;

/**
 * Clase utilizada para crear AlertDialogs.
 */
public class AlertDialogBuilder {

    /**
     * Método que muestra un AlertDialog con información pasada por parámetros.
     * @param context Contexto de la aplicación.
     * @param message Mensaje que se mostrará al usuario.
     */
    public static void crearInfoAlerDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constantes.INFORMACION);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.btn_volver), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    /**
     * Método que muestra un AlertDialog con un mensaje de error y el código del error pasado por parámetros.
     * @param context Contexto de la aplicación.
     * @param errorCode Mensaje que se mostrará al usuario.
     */
    public static void crearErrorAlerDialog(Context context, String errorCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constantes.ERROR);
        builder.setMessage(Constantes.ERROR_ALERTDIALOG.concat(errorCode));
        builder.setPositiveButton(context.getString(R.string.btn_volver), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
