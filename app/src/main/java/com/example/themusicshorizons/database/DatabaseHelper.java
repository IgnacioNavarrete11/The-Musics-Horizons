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

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "musichorizons.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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
                "FOREIGN KEY(" + DiscoveredSongEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

        final String CREATE_TABLE_VISUAL_SCANS = "CREATE TABLE " + VisualScanEntry.TABLE_NAME + " (" +
                VisualScanEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VisualScanEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                VisualScanEntry.COLUMN_SCAN_TYPE + " TEXT NOT NULL, " +
                VisualScanEntry.COLUMN_RESULT_DATA + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + VisualScanEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

        final String CREATE_TABLE_SAVED_EVENTS = "CREATE TABLE " + SavedEventEntry.TABLE_NAME + " (" +
                SavedEventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SavedEventEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                SavedEventEntry.COLUMN_EVENT_DATA + " TEXT NOT NULL, " +
                SavedEventEntry.COLUMN_SAVED_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + SavedEventEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DISCOVERED_SONGS);
        db.execSQL(CREATE_TABLE_VISUAL_SCANS);
        db.execSQL(CREATE_TABLE_SAVED_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DiscoveredSongEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VisualScanEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SavedEventEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(db);
    }

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

    public boolean addDiscoveredSong(long userId, String songData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        values.put(DiscoveredSongEntry.COLUMN_USER_ID, userId);
        values.put(DiscoveredSongEntry.COLUMN_SONG_DATA, songData);
        values.put(DiscoveredSongEntry.COLUMN_DETECTED_DATE, currentDate);
        long result = db.insert(DiscoveredSongEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getSongsForUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
            DiscoveredSongEntry.TABLE_NAME,
            null,
            DiscoveredSongEntry.COLUMN_USER_ID + " = ?",
            new String[]{String.valueOf(userId)},
            null,
            null,
            DiscoveredSongEntry.COLUMN_DETECTED_DATE + " DESC"
        );
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
}
