package com.example.sigo_utm_watajai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText // Importación necesaria para campos de texto
import android.widget.Toast // Importación para mostrar mensajes
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Obtener referencias a los campos de texto
        val userField: EditText = findViewById(R.id.et_usuario)
        val passwordField: EditText = findViewById(R.id.et_contrasena)

        // 2. Obtener referencia al botón "Iniciar Sesión"
        val loginButton: Button = findViewById(R.id.btn_login)

        // 3. Definir la acción al hacer clic
        loginButton.setOnClickListener {
            val user = userField.text.toString()
            val password = passwordField.text.toString()

            // Llamar a la función de verificación
            performLogin(user, password)
        }
    }

    /**
     * Simula el proceso de verificación de credenciales.
     * En una aplicación real, esto se conectaría a un servidor (API).
     */
    private fun performLogin(user: String, pass: String) {
        // Validación básica de campos vacíos
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu usuario y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        // --- LÓGICA DE SIMULACIÓN DE AUTH ---
        // Credenciales de prueba: "testuser" / "password"
        if (user == "testuser" && pass == "password") {
            // Éxito: Navegar a Home
            Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_LONG).show()
            navigateToHome()
        } else {
            // Fallo: Mostrar mensaje de error
            Toast.makeText(this, "Usuario o contraseña incorrectos. Intenta de nuevo.", Toast.LENGTH_LONG).show()
        }
    }

    // Función que inicia la HomeActivity
    private fun navigateToHome() {
        // Intent es lo que usa Android para ir a otra pantalla
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        // Usamos finish() para que el usuario no pueda regresar al Login con el botón Atrás
        finish()
    }
}