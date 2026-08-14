package com.example.notefuzz;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notefuzz.helper.NoteDbHelper;
import com.example.notefuzz.helper.DialogUtils;
import com.example.notefuzz.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailActivity extends AppCompatActivity {
    private NoteDbHelper dbHelper;
    private EditText etTitle;
    private EditText etDescription;
    private TextView tvDate;
    private Button btnSave;
    private Button btnDelete;

    private long noteId = -1;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_detail);

        //preparar el contexto actual de la vista para el helper db
        dbHelper = new NoteDbHelper(this);

        //captura de elementos de la vista
        ImageButton btnBack = findViewById(R.id.btnBack);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvDate = findViewById(R.id.tvDate);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        //evento de salir de la vista detalle de nota
        btnBack.setOnClickListener(v -> finish());

        //obtener el ID de la nota mediante el "intent" enviado desde la vista principal
        //en caso de no haberse enviado un ID correctamente, se asignara "-1"
        noteId = getIntent().getLongExtra(MainActivity.EXTRA_NOTE_ID, -1);


        if (noteId != -1) {
            loadNoteData();
        } else {
            //en caso de que no se haya mandado un id de nota, aparecer elementos para nota nueva
            btnDelete.setVisibility(android.view.View.GONE);
            tvDate.setText(R.string.new_note);
        }

        //mostrar botones de edicion
        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    //modulo para cargar datos de nota seleccionada
    private void loadNoteData() {
        currentNote = dbHelper.getNoteById(noteId);
        if (currentNote == null) {
            finish();
            return;
        }
        etTitle.setText(currentNote.getTitle());
        etDescription.setText(currentNote.getDescription());

        String lastDate = currentNote.getEditedAt() != null
                ? currentNote.getEditedAt()
                : currentNote.getCreatedAt();
        tvDate.setText(getString(R.string.last_edited, lastDate));
        btnDelete.setVisibility(android.view.View.VISIBLE);
    }
    //modulo de validacion para limite de caracteres
    private Boolean validateContent(int limit, String content){
        return content.length() > limit;
    }

    private void saveNote() {
        //recoleccion de datos
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        //validacion aplicada para ver si la etiqueta esta vacia
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, R.string.error_title_required, Toast.LENGTH_SHORT).show();
            etTitle.requestFocus();
            return;
        }

        //validacion aplicada para limitar el alcance de caracteres a 100 en titulo
        if (validateContent(100, title)) {
            etTitle.setError(getString(R.string.error_title_length_max));
            etTitle.requestFocus();
            return;
        }
        //validacion aplicada para limitar el alcance de caracteres a 2000 en descripcion de nota
        if (validateContent(2000, description)) {
            etDescription.setError(getString(R.string.error_title_length_max));
            etDescription.requestFocus();
            return;
        }
        //obtencion de fecha actual
        String now = getCurrentTimestamp();
        //en caso de ser un "insert"
        if (noteId == -1) {
            Note note = new Note();
            note.setTitle(title);
            note.setDescription(description);
            note.setCreatedAt(now);
            note.setEditedAt(now);
            note.setStatus(NoteDbHelper.STATUS_ACTIVE);
            dbHelper.insertNote(note);
        } else {//en caso de ser "update"
            currentNote.setTitle(title);
            currentNote.setDescription(description);
            currentNote.setEditedAt(now);
            dbHelper.updateNote(currentNote);
        }
        //mensaje de confirmacion para nota guardada
        Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
        finish();
    }


    private void confirmDelete(){
        DialogUtils.confirmDelete(this, dbHelper, noteId, this::finish);
    }


    private String getCurrentTimestamp() {
        //formato de fecha y hora con metodo "format"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
