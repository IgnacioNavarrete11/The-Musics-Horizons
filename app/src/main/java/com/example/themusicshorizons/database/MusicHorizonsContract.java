package com.example.themusicshorizons.database;

import android.provider.BaseColumns;

public final class MusicHorizonsContract {

    // Para prevenir que alguien instancie la clase accidentalmente
    private MusicHorizonsContract() {}

    /* Clase interna que define el contenido de la tabla de usuarios */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_IS_ADMIN = "is_admin";
    }

    /* Clase interna para la tabla de canciones descubiertas */
    public static class DiscoveredSongEntry implements BaseColumns {
        public static final String TABLE_NAME = "discovered_songs";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_SONG_DATA = "song_data";
        public static final String COLUMN_DETECTED_DATE = "detected_date";
    }

    /* Clase interna para la tabla de escaneos visuales */
    public static class VisualScanEntry implements BaseColumns {
        public static final String TABLE_NAME = "visual_scans";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_SCAN_TYPE = "scan_type";
        public static final String COLUMN_RESULT_DATA = "result_data";
    }

    /* Clase interna para la tabla de eventos guardados */
    public static class SavedEventEntry implements BaseColumns {
        public static final String TABLE_NAME = "saved_events";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_EVENT_DATA = "event_data";
        public static final String COLUMN_SAVED_DATE = "saved_date";
    }
}
