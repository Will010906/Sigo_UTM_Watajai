package com.example.sigo_utm_watajai.model

/**
 * Clase de modelo de datos (Data Model) que representa la respuesta del servidor al intentar iniciar sesión.
 * Retrofit utiliza esta clase para deserializar la respuesta JSON y acceder a los datos de la sesión y del perfil.
 */
data class LoginResponse(
    // --- Datos de Autenticación y Sesión ---

    /** * El token JWT (JSON Web Token) emitido por el servidor.
     * Es el token Bearer necesario para realizar peticiones autenticadas posteriores.
     */
    val bearer: String?,

    /** Mensaje de control o estado devuelto por la API (útil para manejar errores de credenciales). */
    val messageControl: String?,

    // --- Datos de Perfil Iniciales ---

    /** * Nombre completo de la persona/estudiante, tal como lo envía el servidor.
     * Este campo fue clave para la solución del nombre incompleto (usando 'nombreCompleto' en Room).
     */
    val personFullName: String?,

    /** ID único de la persona en el sistema del servidor. */
    val personId: Int?,

    /** La matrícula o nombre de usuario del estudiante. Se guarda en Room como 'matricula'. */
    val username: String?,

    /** El correo electrónico del usuario (ej. correo institucional). */
    val email: String?

    // Agrega aquí otros campos relevantes como 'accessModule' o 'register' si los necesitas guardar.
)