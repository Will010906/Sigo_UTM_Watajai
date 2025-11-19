package com.example.sigo_utm_watajai.data

import androidx.lifecycle.LiveData
import com.example.sigo_utm_watajai.data.db.dao.CuatrimestreDao
import com.example.sigo_utm_watajai.data.db.entity.Cuatrimestre

/**
 * Repositorio de Cuatrimestres.
 * Esta clase actúa como la fuente única de datos para la entidad Cuatrimestre,
 * abstrayendo la lógica de la base de datos (Room) del ViewModel.
 *
 * @param cuatrimestreDao DAO (Data Access Object) para acceder a los datos de la tabla 'historial_cuatrimestres'.
 */
class CuatrimestreRepository(private val cuatrimestreDao: CuatrimestreDao) {

    /**
     * 1. LiveData de la lista completa de cuatrimestres.
     * Al ser LiveData, la Activity/Fragment que observe esta variable se actualizará
     * automáticamente cada vez que haya cambios en la base de datos (DB).
     */
    val listaCuatrimestres: LiveData<List<Cuatrimestre>> = cuatrimestreDao.obtenerCuatrimestres()

    /**
     * 2. Inserta o actualiza una lista completa de cuatrimestres en la DB.
     * Esta función es 'suspend' porque las operaciones de escritura de Room son asíncronas
     * y deben ejecutarse fuera del hilo principal (UI).
     *
     * @param cuatrimestres La lista de objetos Cuatrimestre a insertar o actualizar.
     */
    suspend fun insertarLista(cuatrimestres: List<Cuatrimestre>) {
        cuatrimestreDao.insertarTodos(cuatrimestres)
    }
}