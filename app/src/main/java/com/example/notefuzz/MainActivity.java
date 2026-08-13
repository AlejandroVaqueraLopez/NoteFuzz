package com.example.notefuzz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notefuzz.helper.DialogUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.notefuzz.adapter.NoteAdapter;
import com.example.notefuzz.helper.NoteDbHelper;
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
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        //definicion del layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //instancia de NoteAdapter
        adapter = new NoteAdapter(dbHelper.getAllNotes(), this);
        //asignacion de contenidos en el recyclerview
        recyclerView.setAdapter(adapter);

        //boton para movernos de vista
        //uso de intent explicito para CREAR un item inexistente
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            startActivity(intent);
        });

        //boton de menu de 3 puntitos
        btnMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, btnMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_view_tutorial) {
                    showOnboardingAgain();
                    return true;
                }
                return false;
            });
            popupMenu.show();
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
    //cuando una nota es seleccionada, los datos de la misma se guardan en un intent
    //uso de intent explicito mandando ID de una nota existente a vista de edicion
    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, note.getId()); //"(extra_note_id, 1)"
        startActivity(intent);
    }

    //sobreescribir el metodo de onNoteLongClick, adentro ejecuta el helper de "confirmDelete"
    // y despues de eliminar satisfactoriamente, ejecuta manualmente la funcion de "loadNotes"
    @Override
    public void onNoteLongClick(Note note) {
        DialogUtils.confirmDelete(this, dbHelper, note.getId(), () -> loadNotes(etSearch.getText().toString()));
    }

    //resetea la bandera de "primera vez" y vuelve a mostrar el onboarding
    private void showOnboardingAgain() {
        SharedPreferences prefs = getSharedPreferences(OnboardingActivity.PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(OnboardingActivity.KEY_FIRST_RUN, true).apply();
        startActivity(new Intent(this, OnboardingActivity.class));
        finish();
    }
}
