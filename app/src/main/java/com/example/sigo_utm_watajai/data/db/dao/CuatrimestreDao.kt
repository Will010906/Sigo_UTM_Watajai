package com.example.sigo_utm_watajai.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sigo_utm_watajai.data.db.entity.Cuatrimestre // Importa la entidad de datos

/**
 * Data Access Object (DAO) para la entidad Cuatrimestre.
 * Define los métodos para realizar operaciones (CRUD) en la tabla 'historial_cuatrimestres' de la base de datos Room.
 */
@Dao
interface CuatrimestreDao {

    /**
     * Obtiene todos los cuatrimestres registrados en el historial.
     * Los resultados se ordenan por nombre en orden descendente.
     * * @return LiveData<List<Cuatrimestre>> que automáticamente notifica a los observadores sobre cambios.
     */
    @Query("SELECT * FROM historial_cuatrimestres ORDER BY nombre DESC")
    fun obtenerCuatrimestres(): LiveData<List<Cuatrimestre>>

    /**
     * Inserta una lista completa de cuatrimestres en la base de datos.
     * Utiliza OnConflictStrategy.REPLACE para reemplazar cualquier registro existente (basándose en la clave primaria)
     * en caso de conflicto.
     * * La función es 'suspend' porque esta operación de escritura debe ejecutarse de forma asíncrona fuera del hilo principal.
     * * @param cuatrimestres La lista de objetos Cuatrimestre a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(cuatrimestres: List<Cuatrimestre>)
}