package com.example.sigo_utm_watajai.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de datos (Data Entity) para representar una Asignatura en la base de datos Room.
 * Cada instancia de esta clase corresponde a una fila en la tabla 'asignaturas'.
 *
 * @param tableName Define el nombre de la tabla en la base de datos.
 */
@Entity(tableName = "asignaturas")
data class Asignatura(
    /**
     * Clave primaria (Primary Key) de la tabla.
     * autoGenerate = true permite que Room asigne un ID único automáticamente a cada registro.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Clave foránea o de asociación para identificar a qué cuatrimestre pertenece esta asignatura.
     * Permite filtrar el historial académico.
     */
    val cuatrimestreNombre: String,

    /** Nombre completo de la asignatura (Ej: "Inglés"). */
    val nombre: String,

    /** Nombre del profesor que impartió la asignatura (Ej: "Lic. Maria Veronica Alvarez Ríos"). */
    val profesor: String,

    /** Indicador del progreso del curso o finalización (Ej: "100%"). */
    val progreso: String,

    /** Tipo de evaluación (Ej: "Ordinaria", "Extraordinaria"). */
    val evaluacion: String,

    /** Nivel de desempeño general (Ej: "E (Estrategico)", "A (Autónomo)"). */
    val desempenoGeneral: String,

    /** Calificación numérica obtenida en la asignatura. */
    val calificacion: Double,

    /** Estatus final de la asignatura (Ej: "Aprobada", "Reprobada", "En curso"). */
    val estatus: String
)