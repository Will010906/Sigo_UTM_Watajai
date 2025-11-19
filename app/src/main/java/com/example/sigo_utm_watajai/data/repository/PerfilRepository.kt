package com.example.sigo_utm_watajai.data.repository

import androidx.lifecycle.LiveData
import com.example.sigo_utm_watajai.data.db.dao.PerfilDao
import com.example.sigo_utm_watajai.data.db.entity.Perfil

/**
 * Repositorio de Perfil.
 * Esta clase sirve como una fuente única de datos para la información del usuario autenticado.
 * Aísla la lógica de acceso a datos (DAO) del ViewModel, implementando el patrón Repository.
 *
 * @param perfilDao El DAO (Data Access Object) de Room necesario para acceder a la tabla 'perfil_usuario'.
 */
class PerfilRepository(private val perfilDao: PerfilDao) {

    /**
     * LiveData que expone el perfil del usuario activo (el registro con ID=1).
     * Cualquier componente (Activity/Fragment) que observe esta variable se actualizará
     * automáticamente cada vez que el perfil cambie en la base de datos (DB).
     */
    val perfil: LiveData<Perfil> = perfilDao.obtenerPerfil()

    /**
     * Inserta o actualiza el perfil del usuario en la base de datos.
     * Dado que la clave primaria es siempre 1, esta función siempre reemplaza el perfil existente.
     *
     * ⭐ FUNCIÓN CORREGIDA: Usamos 'insertar' para que coincida con el DAO ⭐
     * @param perfil El objeto Perfil con la información actualizada o nueva del usuario.
     * La función es 'suspend' porque las operaciones de escritura en Room son asíncronas.
     */
    suspend fun insertarPerfil(perfil: Perfil) {
        perfilDao.insertar(perfil)
    }
}