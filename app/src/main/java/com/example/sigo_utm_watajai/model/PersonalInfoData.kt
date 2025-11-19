package com.example.sigo_utm_watajai.model

/**
 * Clase de modelo de datos (Data Model) utilizada para manejar y transferir la información personal
 * del usuario en los formularios de edición (como ProfileEditActivity).
 *
 * Utiliza valores predeterminados (como cadenas vacías o "Selecciona") para inicializar los campos
 * de la UI y facilitar el manejo de datos en Kotlin.
 */
data class PersonalInfoData(
    /** Nombre(s) del usuario. Inicializado a vacío. */
    var nombre: String = "",

    /** Apellido Paterno del usuario. Inicializado a vacío. */
    var apellidoPaterno: String = "",

    /** Apellido Materno del usuario. Inicializado a vacío. */
    var apellidoMaterno: String = "",

    /** Clave Única de Registro de Población (CURP). Inicializado a vacío. */
    var curp: String = "",

    /** Número de Seguridad Social (NSS). Inicializado a vacío. */
    var nss: String = "",

    /** Fecha de nacimiento. Inicializado a vacío (espera formato de fecha). */
    var fechaNacimiento: String = "",

    /** Estado o lugar de nacimiento. Inicializado con un placeholder para selección. */
    var estadoNacimiento: String = "Selecciona",

    /** Género o sexo del usuario. Inicializado con un placeholder para selección. */
    var sexo: String = "Selecciona"
)