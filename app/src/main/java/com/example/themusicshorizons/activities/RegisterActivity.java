package com.example.themusicshorizons.activities;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.os.Bundle; // Herramienta para pasar datos entre pantallas.
import android.view.View; // Es la base de todos los componentes visuales.
import android.widget.Button; // Específicamente, el componente de botón.
import android.widget.EditText; // Específicamente, el componente de campo de texto editable.
import android.widget.Toast; // Herramienta para mostrar mensajes flotantes.

import androidx.appcompat.app.AppCompatActivity; // Clase base para crear pantallas.

import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.database.DatabaseHelper; // Nuestro propio "fontanero" de la base de datos.

// Esta Activity es la pantalla que permite a un nuevo usuario registrarse en la aplicación.
public class RegisterActivity extends AppCompatActivity {

    // Se declaran las variables para los componentes visuales y la herramienta de base de datos.
    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Se inicializa la herramienta para hablar con la base de datos.
        dbHelper = new DatabaseHelper(this);

        // Se asocian las variables con los componentes del layout XML.
        usernameEditText = findViewById(R.id.usernameRegisterEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordRegisterEditText);
        registerButton = findViewById(R.id.registerButton);

        // Se asigna la acción que ocurre al pulsar el botón de "Registrarse".
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    // Este método contiene la lógica para crear un nuevo usuario en la base de datos.
    private void registerUser() {
        // Se recoge el texto que el usuario ha escrito en los campos, quitando espacios sobrantes.
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Se comprueba que ninguno de los campos esté vacío antes de continuar.
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se llama al DatabaseHelper para que intente añadir el nuevo usuario a la tabla "users".
        boolean isInserted = dbHelper.addUser(username, password, email);

        // Si la base de datos confirma que la operación fue exitosa...
        if (isInserted) {
            Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
            finish(); // ...se muestra un mensaje de éxito y se cierra esta pantalla para volver al Login.
        } else {
            // Si falla, es probable que sea porque el nombre de usuario o el email ya existen, así que se notifica al usuario.
            Toast.makeText(this, "Error en el registro. El usuario o email ya podría existir.", Toast.LENGTH_LONG).show();
        }
    }
}
