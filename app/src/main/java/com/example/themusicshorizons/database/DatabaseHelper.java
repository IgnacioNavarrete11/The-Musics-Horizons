package com.example.themusicshorizons.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.themusicshorizons.database.MusicHorizonsContract.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Esta clase es la arquitecta de la base de datos. Se encarga de crearla, actualizarla y proporcionar los métodos para interactuar con ella.
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "musichorizons.db";
    // Si se cambia la versión, se ejecutará el método onUpgrade.
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Este método se ejecuta solo una vez, la primera vez que se crea la base de datos.
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_USERS = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntry.COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                UserEntry.COLUMN_IS_ADMIN + " INTEGER NOT NULL DEFAULT 0);";

        final String CREATE_TABLE_DISCOVERED_SONGS = "CREATE TABLE " + DiscoveredSongEntry.TABLE_NAME + " (" +
                DiscoveredSongEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DiscoveredSongEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                DiscoveredSongEntry.COLUMN_SONG_DATA + " TEXT NOT NULL, " +
                DiscoveredSongEntry.COLUMN_DETECTED_DATE + " TEXT NOT NULL, " +
                "artwork_url TEXT, " +
                "spotify_url TEXT, " +
                "FOREIGN KEY(" + DiscoveredSongEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

        final String CREATE_TABLE_SAVED_EVENTS = "CREATE TABLE " + SavedEventEntry.TABLE_NAME + " (" +
                SavedEventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SavedEventEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                SavedEventEntry.COLUMN_EVENT_ID + " TEXT UNIQUE NOT NULL, " +
                SavedEventEntry.COLUMN_EVENT_DATA + " TEXT NOT NULL, " +
                SavedEventEntry.COLUMN_SAVED_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + SavedEventEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DISCOVERED_SONGS);
        db.execSQL(CREATE_TABLE_SAVED_EVENTS);
    }

    // Se ejecuta si se incrementa el número de DATABASE_VERSION para actualizar la estructura de la base de datos.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DiscoveredSongEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SavedEventEntry.TABLE_NAME);
        onCreate(db);
    }

    // --- MÉTODOS DE USUARIO ---

    public boolean addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USERNAME, username);
        values.put(UserEntry.COLUMN_PASSWORD, password);
        values.put(UserEntry.COLUMN_EMAIL, email);
        long result = db.insert(UserEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { UserEntry._ID, UserEntry.COLUMN_USERNAME };
        String selection = UserEntry.COLUMN_USERNAME + " = ?" + " AND " + UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        return db.query(UserEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    public String getUserEmail(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String email = null;
        try {
            cursor = db.query(UserEntry.TABLE_NAME,
                    new String[]{UserEntry.COLUMN_EMAIL},
                    UserEntry._ID + " = ?",
                    new String[]{String.valueOf(userId)},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                email = cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_EMAIL));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return email;
    }

    // --- MÉTODOS DE CANCIONES ---

    public boolean addDiscoveredSong(long userId, String songData, String artworkUrl, String spotifyUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        values.put(DiscoveredSongEntry.COLUMN_USER_ID, userId);
        values.put(DiscoveredSongEntry.COLUMN_SONG_DATA, songData);
        values.put(DiscoveredSongEntry.COLUMN_DETECTED_DATE, currentDate);
        values.put("artwork_url", artworkUrl);
        values.put("spotify_url", spotifyUrl);
        long result = db.insert(DiscoveredSongEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getSongsForUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(DiscoveredSongEntry.TABLE_NAME, null, DiscoveredSongEntry.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, DiscoveredSongEntry.COLUMN_DETECTED_DATE + " DESC");
    }

    public void deleteDiscoveredSong(long songId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DiscoveredSongEntry.TABLE_NAME, DiscoveredSongEntry._ID + " = ?", new String[]{String.valueOf(songId)});
    }

    // --- MÉTODOS DE EVENTOS ---

    public boolean addSavedEvent(long userId, String eventId, String eventData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        values.put(SavedEventEntry.COLUMN_USER_ID, userId);
        values.put(SavedEventEntry.COLUMN_EVENT_ID, eventId);
        values.put(SavedEventEntry.COLUMN_EVENT_DATA, eventData);
        values.put(SavedEventEntry.COLUMN_SAVED_DATE, currentDate);
        long result = db.insert(SavedEventEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    public void deleteSavedEvent(long userId, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SavedEventEntry.TABLE_NAME, SavedEventEntry.COLUMN_USER_ID + " = ? AND " + SavedEventEntry.COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(userId), eventId});
    }

    public boolean isEventSaved(long userId, String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SavedEventEntry.TABLE_NAME, new String[]{SavedEventEntry._ID}, SavedEventEntry.COLUMN_USER_ID + " = ? AND " + SavedEventEntry.COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(userId), eventId}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public Cursor getSavedEventsForUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(SavedEventEntry.TABLE_NAME, null, SavedEventEntry.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, SavedEventEntry.COLUMN_SAVED_DATE + " DESC");
    }
}
