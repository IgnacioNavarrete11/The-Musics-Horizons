package com.example.themusicshorizons.utils;

import android.content.Context;
import android.content.SharedPreferences;

// Esta clase es la "memoria" del usuario. Se encarga de guardar y recuperar los datos de la sesión (como el ID del usuario)
// para que la app recuerde quién es el usuario, incluso si la cierra y la vuelve a abrir.
public class SessionManager {

    // El nombre del archivo de preferencias donde se guardarán los datos.
    private static final String PREF_NAME = "TheMusicsHorizonsSession";
    // La clave para saber si el usuario ha iniciado sesión o no.
    private static final String IS_LOGGED_IN = "IsLoggedIn";

    // Las claves para guardar los datos específicos del usuario.
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USERNAME = "username";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // El modo de acceso al archivo: solo esta app puede leerlo y escribirlo.
    int PRIVATE_MODE = 0;

    // Constructor de la clase.
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    // Este método se llama cuando el usuario inicia sesión correctamente.
    public void createLoginSession(long userId, String username) {
        // Guarda en el archivo de preferencias que el usuario ha iniciado sesión.
        editor.putBoolean(IS_LOGGED_IN, true);
        // Guarda el ID y el nombre del usuario.
        editor.putLong(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        // Confirma los cambios y los escribe en el archivo.
        editor.commit();
    }

    // Comprueba si el usuario tiene una sesión activa.
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    // Obtiene el ID del usuario que está guardado en la sesión.
    public long getUserId() {
        return pref.getLong(KEY_USER_ID, 0); // Devuelve 0 si no encuentra ningún ID.
    }

    // Borra todos los datos de la sesión. Se utiliza cuando el usuario cierra sesión.
    public void logoutUser() {
        // Borra todos los datos guardados en el archivo de preferencias.
        editor.clear();
        editor.commit();

        // Aquí se podría redirigir al usuario a la pantalla de Login, pero esa lógica está en el ProfileFragment para mayor control.
    }
}
