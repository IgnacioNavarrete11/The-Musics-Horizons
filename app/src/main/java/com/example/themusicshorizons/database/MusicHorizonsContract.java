package com.example.themusicshorizons.database;

import android.provider.BaseColumns;

// Esta clase actúa como un "contrato" o un plano para la base de datos.
// Define los nombres de las tablas y columnas de forma centralizada para evitar errores de tipeo en el resto del código.
public final class MusicHorizonsContract {

    // Constructor privado para evitar que alguien cree una instancia de esta clase por error.
    private MusicHorizonsContract() {}

    // Define la estructura de la tabla de usuarios.
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_IS_ADMIN = "is_admin";
    }

    // Define la estructura de la tabla de canciones descubiertas.
    public static class DiscoveredSongEntry implements BaseColumns {
        public static final String TABLE_NAME = "discovered_songs";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_SONG_DATA = "song_data"; // Guarda un JSON con la info de la canción.
        public static final String COLUMN_DETECTED_DATE = "detected_date";
    }

    // Define la estructura de la tabla de escaneos visuales (no se usa actualmente).
    public static class VisualScanEntry implements BaseColumns {
        public static final String TABLE_NAME = "visual_scans";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_SCAN_TYPE = "scan_type";
        public static final String COLUMN_RESULT_DATA = "result_data";
    }

    // Define la estructura de la tabla de eventos guardados.
    public static class SavedEventEntry implements BaseColumns {
        public static final String TABLE_NAME = "saved_events";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_EVENT_ID = "event_id"; // El ID único que viene de Ticketmaster.
        public static final String COLUMN_EVENT_DATA = "event_data"; // Guarda un JSON con la info del evento.
        public static final String COLUMN_SAVED_DATE = "saved_date";
    }
}
