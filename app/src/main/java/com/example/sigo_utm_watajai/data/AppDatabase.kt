package com.example.sigo_utm_watajai.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sigo_utm_watajai.data.db.dao.AsignaturaDao
import com.example.sigo_utm_watajai.data.db.dao.CuatrimestreDao
import com.example.sigo_utm_watajai.data.db.entity.Cuatrimestre
import com.example.sigo_utm_watajai.data.db.dao.PerfilDao
import com.example.sigo_utm_watajai.data.db.dao.UnidadTematicaDao
import com.example.sigo_utm_watajai.data.db.entity.Asignatura
import com.example.sigo_utm_watajai.data.db.entity.Perfil
import com.example.sigo_utm_watajai.data.db.entity.UnidadTematica

/**
 * Clase abstracta que representa la base de datos de la aplicación, utilizando Room.
 * Hereda de RoomDatabase y es el punto central para acceder a la base de datos local.
 *
 * 1. Especifica las entidades (tablas) y la versión de la DB.
 * * @param entities Lista de todas las clases de entidades que conforman el esquema de la base de datos.
 * * @param version Número entero que indica la versión actual del esquema de la base de datos. Debe incrementarse si se cambia el esquema (ej. añadir/quitar columnas).
 * * @param exportSchema Se establece en false para evitar advertencias y la exportación del esquema.
 */
@Database(
    entities = [Perfil::class, Cuatrimestre::class, Asignatura::class, UnidadTematica::class],
    version = 6, // La versión actual de la base de datos.
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // 2. Define los DAOs (Data Access Objects) que contiene la DB.
    // Room genera el código de las implementaciones de estos métodos en tiempo de compilación.
    /** Retorna el DAO para las operaciones relacionadas con el perfil del usuario. */
    abstract fun perfilDao(): PerfilDao

    /** Retorna el DAO para las operaciones relacionadas con el historial de cuatrimestres. */
    abstract fun cuatrimestreDao(): CuatrimestreDao

    /** Retorna el DAO para las operaciones relacionadas con las asignaturas. */
    abstract fun asignaturaDao(): AsignaturaDao

    /** Retorna el DAO para las operaciones relacionadas con las unidades temáticas. */
    abstract fun unidadTematicaDao(): UnidadTematicaDao

    /**
     * 3. Companion object para crear la instancia de la base de datos (Singleton Pattern).
     * Esto asegura que solo se cree una instancia de la DB, mejorando el rendimiento y evitando conflictos.
     */
    companion object {
        @Volatile // Garantiza que los cambios en la variable sean visibles para todos los hilos inmediatamente.
        private var INSTANCE: AppDatabase? = null

        /**
         * Método para obtener la instancia única de AppDatabase. Si no existe, la crea.
         * @param context Contexto de la aplicación necesario para la construcción de la base de datos.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia ya existe, la retorna.
            return INSTANCE ?:
            // Si es nula, sincroniza para asegurar que solo un hilo cree la instancia.
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sigo_database" // Nombre del archivo de la base de datos
                )
                    // ⭐ ESTRATEGIA DE MIGRACIÓN: Destruye y recrea la base de datos si el esquema cambia (version++).
                    // Esto fue crucial para solucionar errores de integridad (IllegalStateException).
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}