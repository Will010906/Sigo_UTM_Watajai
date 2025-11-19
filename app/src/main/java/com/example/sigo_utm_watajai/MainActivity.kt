package com.example.sigo_utm_watajai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.sigo_utm_watajai.model.LoginRequest
import com.example.sigo_utm_watajai.model.LoginResponse
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
// IMPORTS PARA LA CAPA DE DATOS
import com.example.sigo_utm_watajai.data.AppDatabase
import com.example.sigo_utm_watajai.data.repository.PerfilRepository
import com.example.sigo_utm_watajai.data.db.entity.Perfil
import com.example.sigo_utm_watajai.data.remote.RetrofitClient
import com.example.sigo_utm_watajai.data.local.TokenManager


/**
 * Activity principal de la aplicación, responsable de la interfaz y la lógica de inicio de sesión.
 * Gestiona la autenticación con la API, la persistencia del token (DataStore) y el guardado del perfil (Room).
 */
class MainActivity : AppCompatActivity() {

    // ⭐ 1. INICIALIZACIÓN DE DEPENDENCIAS DE LA CAPA DE DATOS ⭐
    // Inicializa la base de datos (Room) de forma lazy.
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    // Inicializa el gestor de tokens (DataStore) de forma lazy.
    private val tokenManager by lazy { TokenManager(applicationContext) }
    // Inicializa el repositorio de perfil, inyectando el DAO de perfil de la DB.
    private val profileRepository by lazy { PerfilRepository(database.perfilDao()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout activity_main.xml
        setContentView(R.layout.activity_main)

        // 2. Obtener referencias a los campos de texto
        val userField: EditText = findViewById(R.id.et_usuario)
        val passwordField: EditText = findViewById(R.id.et_contrasena)

        // ⭐ LLAMADA A LA FUNCIÓN DE TINTADO ⭐
        // Asegura que los íconos de los EditText se vean correctamente.
        tintInputDrawables(userField, passwordField)


        // 3. Obtener referencia al botón "Iniciar Sesión"
        val loginButton: Button = findViewById(R.id.btn_login)

        // 4. Definir la acción al hacer clic
        loginButton.setOnClickListener {
            val user = userField.text.toString()
            val password = passwordField.text.toString()

            // Llamar a la función principal de autenticación
            performLogin(user, password)
        }
    }

    /**
     * ⭐ FUNCIÓN DE TINTADO DE ÍCONOS ⭐
     * Aplica un color fijo a los Drawables compuestos (íconos) de los EditTexts.
     */
    private fun tintInputDrawables(userField: EditText, passwordField: EditText) {
        val iconColor = ContextCompat.getColor(this, R.color.text_dark)

        fun applyTint(field: EditText) {
            // Se asume que el ícono está en la posición 0 (start/left)
            field.compoundDrawablesRelative[0]?.let { drawable ->
                val wrappedDrawable = DrawableCompat.wrap(drawable)
                DrawableCompat.setTint(wrappedDrawable, iconColor)
                // Reestablece los drawables, usando el drawable envuelto y tintado.
                field.setCompoundDrawablesRelative(wrappedDrawable, null, field.compoundDrawablesRelative[2], null)
            }
        }

        applyTint(userField)
        applyTint(passwordField)
    }

    /**
     * ⭐ FUNCIÓN DE LÓGICA DE INICIO DE SESIÓN CON API (Retrofit) ⭐
     * Realiza la llamada de red de forma asíncrona usando Coroutines.
     */
    private fun performLogin(user: String, pass: String) {
        // Validación básica
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu usuario y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(user, pass)

        // ⭐ 1. INICIAR LA COROUTINE ⭐
        // Se lanza una coroutine en el ámbito de IO (Entrada/Salida) ya que se hará una operación de red.
        CoroutineScope(Dispatchers.IO).launch {

            try {
                // ⭐ 2. LLAMADA A LA API (Retrofit) ⭐
                // La función 'loginUser' es suspendida y se ejecuta de forma segura aquí.
                val response = RetrofitClient.apiService.loginUser(loginRequest)

                // 3. Volver al Hilo Principal (Main) para actualizar la UI (Toast, navegación)
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        val loginResponse = response.body()

                        if (loginResponse != null && !loginResponse.bearer.isNullOrEmpty()) {
                            // Éxito: Token recibido
                            Toast.makeText(this@MainActivity, "Inicio de sesión exitoso.", Toast.LENGTH_LONG).show()

                            // 4. Guardar Token y Perfil
                            // Estas operaciones son suspendidas/IO, por lo que se ejecutan en segundo plano.
                            CoroutineScope(Dispatchers.IO).launch {
                                tokenManager.saveToken(loginResponse.bearer!!)
                                saveProfileData(loginResponse)
                            }

                            // 5. Navegar a Home
                            navigateToHome()
                        } else {
                            // Falla de Lógica (Credenciales incorrectas, mensaje del servidor)
                            val errorMessage = loginResponse?.messageControl ?: "Usuario o contraseña incorrectos."
                            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        // Falla HTTP (Ej. 404, 500, etc.)
                        Toast.makeText(this@MainActivity, "Error en el servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                // Falla de Red (Sin conexión, timeout, etc.)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * ⭐ FUNCIÓN DE PERSISTENCIA (Room con Coroutines) ⭐
     * Guarda la información esencial del perfil en la base de datos Room.
     * Esta función debe ser llamada desde un ámbito de Coroutine (Dispatchers.IO).
     */
    private fun saveProfileData(response: LoginResponse) {
        // Se lanza una nueva coroutine para asegurar que se ejecuta en IO (aunque ya se llama desde IO)
        CoroutineScope(Dispatchers.IO).launch {

            val fullName = response.personFullName?.trim() ?: "Alumno Sin Apellidos"

            // Lógica de separación simple (se guarda el fullName completo en 'nombreCompleto' para la visualización)
            val nombreSimple = fullName.substringBefore(" ")
            val apellidoPaternoSimple = fullName.substringAfter(" ")?.substringBefore(" ") ?: ""
            val apellidoMaternoSimple = fullName.substringAfterLast(" ") ?: ""

            // Creación de la entidad Perfil
            val newProfile = Perfil(
                id = 1,
                nombreCompleto = fullName, // ⭐ SOLUCIÓN FINAL: Cadena completa para visualización ⭐
                nombre = nombreSimple,
                apellidoPaterno = apellidoPaternoSimple,
                apellidoMaterno = apellidoMaternoSimple,

                matricula = response.username ?: "",
                email = response.email ?: "",
                cuatrimestreActual = "4to cuatrimestre", // Valor fijo/simulado

                fechaNacimiento = "",
                estadoNacimiento = "",
                sexo = "",
                curp = "",
                nss = ""
            )

            // Llama al repositorio para insertar/reemplazar el perfil en Room.
            profileRepository.insertarPerfil(newProfile)

            withContext(Dispatchers.Main) {
                // Muestra un Toast de depuración para confirmar qué se guardó.
                Toast.makeText(this@MainActivity, "Guardado: $fullName", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Función que inicia la HomeActivity y finaliza la MainActivity (evita que el usuario regrese con el botón back).
     */
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Cierra la MainActivity
    }
}