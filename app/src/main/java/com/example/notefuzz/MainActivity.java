package com.example.notefuzz;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.notefuzz.adapter.NoteAdapter;
import com.example.notefuzz.data.NoteDbHelper;
import com.example.notefuzz.model.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    public static final String EXTRA_NOTE_ID = "extra_note_id";

    private NoteDbHelper dbHelper;
    private NoteAdapter adapter;
    private EditText etSearch;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instancia de "NoteDbHelper"
        dbHelper = new NoteDbHelper(this);

        //creacion de objeto recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);
        etSearch = findViewById(R.id.etSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        //definicion del layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //instancia de NoteAdapter
        adapter = new NoteAdapter(dbHelper.getAllNotes(), this);
        //asignacion de contenidos en el recyclerview
        recyclerView.setAdapter(adapter);

        //boton que transporta la informacion del elemento seleccionado a la vista detalle
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            startActivity(intent);
        });

        //filtro en tiempo real de la lista de notas por medio de un editText
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //cuando el texto cambia en EditText, se realiza una actualizacion en lista
                loadNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    //cuando usuario vuelve de nuevo a la pantalla de inicio, carga la lista de nuevo
    @Override
    protected void onResume() {
        super.onResume();

        loadNotes(etSearch.getText().toString());
    }

    //carga las notas en la vista
    private void loadNotes(String query) {
        List<Note> notes = (query == null || query.trim().isEmpty())
                ? dbHelper.getAllNotes()
                : dbHelper.searchNotesByTitle(query.trim());

        adapter.updateList(notes);

        if (notes.isEmpty()) {
            tvEmpty.setText(query == null || query.trim().isEmpty()
                    ? R.string.empty_list
                    : R.string.empty_search);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }
    //cuando una nota es seleccionada, los datos de la misma se guardan en un intent y
    //se mandan a la siguiente vista detalle.
    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, note.getId());
        startActivity(intent);
    }
}
