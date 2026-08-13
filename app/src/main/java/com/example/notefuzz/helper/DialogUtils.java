package com.example.notefuzz.helper;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.notefuzz.R;

public class DialogUtils {

    private NoteDbHelper dbHelper;

    public static void confirmDelete(Context context, NoteDbHelper dbHelper, long noteId, Runnable onDeleted) {
        //creacion de alerta con botones de confirmacion
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dbHelper.deleteNote(noteId);
                    Toast.makeText(context, R.string.note_deleted, Toast.LENGTH_SHORT).show();
                    onDeleted.run(); // cada activity decide qué hacer
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

}
