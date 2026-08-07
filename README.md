# NoteFuzz — Proyecto Android (Java + SQLite)

## Cómo abrirlo
1. Abre Android Studio.
2. File > Open... y selecciona la carpeta `NoteFuzz`.
3. Deja que Android Studio configure el Gradle Wrapper automáticamente
   (te lo va a pedir la primera vez que abras el proyecto).
4. Espera al Gradle Sync y ejecuta la app (Run ▶).

## Estructura (arquitectura en capas)
```
com.example.notefuzz
 ├── model/          -> Note.java (POJO de la tabla "note")
 ├── data/            -> NoteDbHelper.java (SQLiteOpenHelper, todo el CRUD)
 ├── adapter/          -> NoteAdapter.java (RecyclerView.Adapter)
 ├── MainActivity.java        -> lista + búsqueda en tiempo real + FAB "+"
 └── NoteDetailActivity.java  -> crear / editar / eliminar una nota
```

## Base de datos
Una sola tabla `note`:
| Campo        | Tipo    |
|--------------|---------|
| id           | INTEGER PRIMARY KEY AUTOINCREMENT |
| created_at   | TEXT    |
| title        | TEXT NOT NULL |
| description  | TEXT    |
| edited_at    | TEXT    |
| status       | INTEGER DEFAULT 1 |

## Funcionalidad cubierta
- CRUD completo en SQLite (insert / update / delete / query).
- Navegación entre Activities con Intents explícitos, pasando el `id`
  de la nota como extra (`MainActivity.EXTRA_NOTE_ID`).
- Búsqueda en tiempo real por título con `TextWatcher` + `LIKE ?`.
- Validación de título obligatorio y diálogo de confirmación al eliminar.
- Separación en capas: modelo, DB helper, adapter y activities.
