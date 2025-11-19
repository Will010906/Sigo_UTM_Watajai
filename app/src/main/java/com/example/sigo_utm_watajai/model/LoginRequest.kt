package com.example.sigo_utm_watajai.model

/**
 * Clase de modelo de datos (Data Model) que representa la solicitud de inicio de sesión.
 * Se utiliza como el cuerpo (@Body) de la petición HTTP POST a la API de autenticación.
 * Retrofit utiliza esta clase para serializar los datos en formato JSON.
 */
data class LoginRequest(
    /** La matrícula o nombre de usuario que el estudiante ingresa. */
    val username: String,

    /** La contraseña que el estudiante ingresa. */
    val password: String
)