package com.example.sigo_utm_watajai.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sigo_utm_watajai.data.db.entity.Asignatura

/**
 * Data Access Object (DAO) para la entidad Asignatura.
 * Define los métodos para realizar operaciones (CRUD) en la tabla 'asignaturas' de la base de datos Room.
 */
@Dao
interface AsignaturaDao {

    /**
     * Obtiene todas las asignaturas que coinciden con un nombre de cuatrimestre dado.
     * * @param nombreCuatrimestre El nombre del cuatrimestre a buscar (ej: "4to cuatrimestre").
     * @return LiveData<List<Asignatura>> que automáticamente emite actualizaciones cuando cambian los datos.
     */
    @Query("SELECT * FROM asignaturas WHERE cuatrimestreNombre = :nombreCuatrimestre ORDER BY nombre ASC")
    fun obtenerAsignaturasPorCuatrimestre(nombreCuatrimestre: String): LiveData<List<Asignatura>>

    /**
     * Inserta o actualiza una lista de asignaturas en la base de datos.
     * Si ya existe una asignatura (basándose en la clave primaria), se reemplaza.
     * * La función es 'suspend' porque las operaciones de escritura en Room deben ser asíncronas
     * y ejecutarse fuera del hilo principal (UI).
     * * @param asignaturas La lista de objetos Asignatura a insertar o actualizar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(asignaturas: List<Asignatura>)
}