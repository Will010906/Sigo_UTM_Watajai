package com.example.sigo_utm_watajai.data.db.entity // Ajusta el paquete

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de datos (Data Entity) para representar un Cuatrimestre dentro del historial académico del estudiante.
 * Cada instancia de esta clase corresponde a una fila en la tabla 'historial_cuatrimestres'.
 *
 * @param tableName Define el nombre de la tabla en la base de datos.
 */
@Entity(tableName = "historial_cuatrimestres")
data class Cuatrimestre(
    /**
     * Clave primaria (Primary Key) de la tabla.
     * Se usa el 'nombre' del cuatrimestre como PK para asegurar la unicidad (Ej: "4to cuatrimestre").
     * Si intentas insertar dos cuatrimestres con el mismo nombre, uno reemplazará al otro.
     */
    @PrimaryKey
    val nombre: String, // Ejemplo: "4to cuatrimestre"

    /** El período de tiempo en el que se cursó el cuatrimestre (Ej: "Sep - Dic 2024"). */
    val periodo: String,

    /** La carrera o programa de estudio al que corresponde este cuatrimestre. */
    val carrera: String,

    /** El grupo al que perteneció el estudiante durante este periodo. */
    val grupo: String,

    /** Nombre del tutor asignado al estudiante durante el cuatrimestre. */
    val tutor: String,

    /** El nivel de desempeño general del estudiante en este cuatrimestre (Ej: "A (Autónomo)"). */
    val desempeno: String,

    // Puedes añadir otros campos necesarios, como el ID del estudiante, si lo necesitas.
)