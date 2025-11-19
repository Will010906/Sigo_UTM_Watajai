package com.example.sigo_utm_watajai.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sigo_utm_watajai.data.db.entity.Perfil // Importa la clase Perfil que creamos

/**
 * Data Access Object (DAO) para la entidad Perfil.
 * Define los métodos para la persistencia del perfil de usuario en la base de datos Room.
 * Dado que solo hay un usuario autenticado a la vez, se utiliza un ID fijo (id = 1).
 */
@Dao
interface PerfilDao {

    /**
     * Inserta un nuevo perfil en la base de datos o lo reemplaza si ya existe.
     * La estrategia OnConflictStrategy.REPLACE asegura que, si intentamos insertar un Perfil
     * con la misma clave primaria (que siempre es 1), el registro anterior se sobreescribe.
     * * * La función es 'suspend' porque esta operación de escritura en Room debe ser asíncrona.
     * @param perfil El objeto Perfil a insertar o actualizar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(perfil: Perfil) // Usamos 'suspend' porque esta operación debe ser asíncrona

    /**
     * Consulta el perfil completo del usuario (el único registro con id = 1).
     * * Retorna LiveData, lo que permite observar los cambios desde la Activity/Fragment. Cada vez
     * que el perfil se actualiza (ej. al editar datos), los observadores reciben automáticamente el nuevo objeto Perfil.
     * @return LiveData<Perfil> el objeto perfil del usuario autenticado.
     */
    @Query("SELECT * FROM perfil_usuario WHERE id = 1")
    fun obtenerPerfil(): LiveData<Perfil>
}