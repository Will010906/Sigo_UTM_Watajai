package com.example.sigo_utm_watajai.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de datos (Data Entity) para representar una Unidad Temática.
 * Estas unidades son los componentes individuales de contenido de una Asignatura.
 * Cada instancia de esta clase corresponde a una fila en la tabla 'unidades_tematicas'.
 *
 * @param tableName Define el nombre de la tabla en la base de datos.
 */
@Entity(tableName = "unidades_tematicas")
data class UnidadTematica(
    /**
     * Clave primaria (Primary Key) de la tabla.
     * autoGenerate = true permite que Room asigne un ID único automáticamente.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Clave de asociación/foránea que indica a qué asignatura pertenece esta unidad.
     * Es crucial para consultar las unidades de una materia específica.
     */
    val asignaturaNombre: String, // ⭐ CLAVE: Nombre de la Asignatura (Ej. "Inglés")

    /** Nombre o título de la unidad temática (Ej: "Presentación Personal"). */
    val nombre: String,

    /** Nivel de desempeño obtenido por el estudiante en esta unidad (Ej: "Estratégico", "Autónomo"). */
    val desempenoUnidad: String // Ej: "(Estratégico)"
)