package com.example.notefuzz;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notefuzz.data.NoteDbHelper;
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

        dbHelper = new NoteDbHelper(this);

        ImageButton btnBack = findViewById(R.id.btnBack);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvDate = findViewById(R.id.tvDate);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        btnBack.setOnClickListener(v -> finish());

        noteId = getIntent().getLongExtra(MainActivity.EXTRA_NOTE_ID, -1);

        if (noteId != -1) {
            loadNoteData();
        } else {
            btnDelete.setVisibility(android.view.View.GONE);
            tvDate.setText(R.string.new_note);
        }

        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

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

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError(getString(R.string.error_title_required));
            etTitle.requestFocus();
            return;
        }

        String now = getCurrentTimestamp();

        if (noteId == -1) {
            Note note = new Note();
            note.setTitle(title);
            note.setDescription(description);
            note.setCreatedAt(now);
            note.setEditedAt(now);
            note.setStatus(1);
            dbHelper.insertNote(note);
        } else {
            currentNote.setTitle(title);
            currentNote.setDescription(description);
            currentNote.setEditedAt(now);
            dbHelper.updateNote(currentNote);
        }

        Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dbHelper.deleteNote(noteId);
                    Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
