package com.example.notefuzz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notefuzz.R;
import com.example.notefuzz.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    //interfaz para implementar un "listener"
    public interface OnNoteClickListener {
        void onNoteClick(Note note);//metodo de la interfaz
    }

    private List<Note> noteList;
    private final OnNoteClickListener listener; //referencia de la interfaz

    //constructor
    public NoteAdapter(List<Note> noteList, OnNoteClickListener listener) {
        this.noteList = noteList != null ? noteList : new ArrayList<>();
        this.listener = listener;
    }

    //metodo que actualiza la lista
    public void updateList(List<Note> newList) {
        this.noteList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    //sobreescritura para inflar el recyclerview
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    //llenado del recycleview de datos
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNoteClick(note);
            }
        });
    }

    //obtencion de cantidad de elementos en lista
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    //guarda referencias de los items de la lista
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
        }
    }
}
