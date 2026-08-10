package com.example.notefuzz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notefuzz.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notefuzz.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NOTE = "note";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_EDITED_AT = "edited_at";
    public static final String COLUMN_STATUS = "status";

    private static final String CREATE_TABLE_NOTE =
            "CREATE TABLE " + TABLE_NOTE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CREATED_AT + " TEXT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_EDITED_AT + " TEXT, " +
                    COLUMN_STATUS + " INTEGER DEFAULT 1)";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }
//create
    public long insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CREATED_AT, note.getCreatedAt());
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_EDITED_AT, note.getEditedAt());
        values.put(COLUMN_STATUS, note.getStatus());
        long id = db.insert(TABLE_NOTE, null, values);
        db.close();
        return id;
    }
//getbyid
    public Note getNoteById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTE, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Note note = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                note = cursorToNote(cursor);
            }
            cursor.close();
        }
        db.close();
        return note;
    }

    //getall
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTE, null, null, null, null, null,
                COLUMN_ID + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                notes.add(cursorToNote(cursor));
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    //searchbytitle
    public List<Note> searchNotesByTitle(String query) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTE, null,
                COLUMN_TITLE + " LIKE ?",
                new String[]{"%" + query + "%"},
                null, null, COLUMN_ID + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                notes.add(cursorToNote(cursor));
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    //update
    public int updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_EDITED_AT, note.getEditedAt());
        values.put(COLUMN_STATUS, note.getStatus());

        int rows = db.update(TABLE_NOTE, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return rows;
    }

    //delete
    public int deleteNote(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_NOTE, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        note.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
        note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
        note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        note.setEditedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EDITED_AT)));
        note.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
        return note;
    }
}
