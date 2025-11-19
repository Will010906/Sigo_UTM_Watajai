package com.example.sigo_utm_watajai.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sigo_utm_watajai.data.db.entity.UnidadTematica
import androidx.lifecycle.LiveData

/**
 * Data Access Object (DAO) para la entidad UnidadTematica.
 * Define los métodos para la interacción con la tabla 'unidades_tematicas' en la base de datos Room.
 */
@Dao
interface UnidadTematicaDao {

    /**
     * Obtiene todas las unidades temáticas que pertenecen a una asignatura específica.
     * * @param nombreAsignatura El nombre de la asignatura de la que se desean obtener las unidades.
     * @return LiveData<List<UnidadTematica>> que emite automáticamente la lista de unidades al haber cambios.
     */
    @Query("SELECT * FROM unidades_tematicas WHERE asignaturaNombre = :nombreAsignatura")
    fun obtenerUnidadesPorAsignatura(nombreAsignatura: String): LiveData<List<UnidadTematica>>

    /**
     * Inserta una lista completa de unidades temáticas en la base de datos.
     * Utiliza OnConflictStrategy.REPLACE para sobrescribir registros existentes si hay conflicto de clave primaria.
     * * La función es 'suspend' porque esta operación debe ejecutarse de forma asíncrona fuera del hilo principal.
     * @param unidades La lista de objetos UnidadTematica a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(unidades: List<UnidadTematica>)
}