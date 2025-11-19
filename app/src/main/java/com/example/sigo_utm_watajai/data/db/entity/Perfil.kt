package com.example.sigo_utm_watajai.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de datos (Data Entity) para representar el perfil del usuario autenticado.
 * Esta clase define la estructura de la tabla 'perfil_usuario' en la base de datos Room.
 * Solo debe existir un registro en esta tabla.
 *
 * @param tableName Define el nombre de la tabla en la base de datos.
 */
@Entity(tableName = "perfil_usuario")
data class Perfil(
    /**
     * Clave primaria (Primary Key) de la tabla.
     * Siempre se establece a 1 (autoGenerate = false) para garantizar que solo exista un registro de perfil activo.
     */
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,

    /**
     * Campo utilizado para la visualización del nombre en la UI.
     * Guarda la cadena completa recibida de la API, eliminando problemas de separación y concatenación.
     */
    val nombreCompleto: String,

    /** Nombre(s) del usuario (Utilizado principalmente en formularios de edición). */
    val nombre: String,

    /** Apellido Paterno del usuario (Utilizado principalmente en formularios de edición). */
    val apellidoPaterno: String,

    /** Apellido Materno del usuario (Utilizado principalmente en formularios de edición). */
    val apellidoMaterno: String,

    // ⭐ CAMPOS DE INFORMACIÓN INSTITUCIONAL AÑADIDOS DURANTE EL LOGIN ⭐

    /** Matrícula o ID de usuario del estudiante. */
    val matricula: String,

    /** Correo electrónico institucional o de contacto principal. */
    val email: String,

    /** Cuatrimestre actual o más reciente del estudiante. */
    val cuatrimestreActual: String,
    // -----------------------------------------------------------------

    /** Fecha de nacimiento del usuario. */
    val fechaNacimiento: String,

    /** Estado o lugar de nacimiento. */
    val estadoNacimiento: String,

    /** Género o sexo del usuario. */
    val sexo: String,

    /** Clave Única de Registro de Población. */
    val curp: String,

    /** Número de Seguridad Social. */
    val nss: String
)