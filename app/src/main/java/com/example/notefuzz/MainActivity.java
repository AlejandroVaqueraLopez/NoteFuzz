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

        dbHelper = new NoteDbHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);
        etSearch = findViewById(R.id.etSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(dbHelper.getAllNotes(), this);
        recyclerView.setAdapter(adapter);


        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            startActivity(intent);
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadNotes(etSearch.getText().toString());
    }

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

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, note.getId());
        startActivity(intent);
    }
}
