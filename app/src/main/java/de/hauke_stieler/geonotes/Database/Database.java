package de.hauke_stieler.geonotes.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.util.List;

import de.hauke_stieler.geonotes.notes.Note;
import de.hauke_stieler.geonotes.notes.NoteStore;
import de.hauke_stieler.geonotes.photo.PhotoStore;

public class Database extends SQLiteOpenHelper {
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "geonotes";

    private final NoteStore noteStore;
    private final PhotoStore photoStore;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        noteStore = new NoteStore();
        photoStore = new PhotoStore();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        noteStore.onCreate(db);
        photoStore.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        noteStore.onUpgrade(db, oldVersion, newVersion);
        photoStore.onUpgrade(db, oldVersion, newVersion);
    }

    public long addNote(String description, double lat, double lon) {
        return noteStore.addNote(getWritableDatabase(), description, lat, lon);
    }

    public void updateDescription(long id, String newDescription) {
        noteStore.updateDescription(getWritableDatabase(), id, newDescription);
    }

    public void updateLocation(long id, GeoPoint location) {
        noteStore.updateLocation(getWritableDatabase(), id, location);
    }

    public void removeNote(long id) {
        noteStore.removeNote(getWritableDatabase(), id);
        // TODO delete photos as well
    }

    public List<Note> getAllNotes() {
        return noteStore.getAllNotes(getWritableDatabase());
    }

    public void addPhoto(Long noteId, File photoFile) {
        photoStore.addPhoto(getWritableDatabase(), noteId, photoFile);
    }

    public List<String> getPhotos(String noteId) {
        return photoStore.getPhotos(getReadableDatabase(), noteId);
    }
}
