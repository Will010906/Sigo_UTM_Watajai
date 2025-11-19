package com.example.sigo_utm_watajai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sigo_utm_watajai.data.db.entity.Perfil
import com.example.sigo_utm_watajai.data.repository.PerfilRepository
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de negocio y los datos para el Perfil del usuario.
 * Es responsable de exponer el perfil y manejar las operaciones de guardar/actualizar en la base de datos.
 *
 * @param repository Instancia del PerfilRepository, inyectada para acceder a los datos de Room.
 */
class ProfileViewModel(private val repository: PerfilRepository) : ViewModel() {

    /** * 1. LiveData del perfil. Expone el perfil del usuario activo al Activity/Fragment.
     * Este LiveData se obtiene directamente del repositorio y se actualiza automáticamente
     * con los cambios en la base de datos Room.
     */
    val perfil = repository.perfil

    /**
     * Lógica para guardar o actualizar el perfil.
     * Es la función que la Activity llama cuando el usuario edita y guarda su información.
     * * @param perfil El objeto Perfil con la información nueva o actualizada a guardar.
     */
    fun guardarPerfil(perfil: Perfil) {
        // Inicia una coroutine dentro del ámbito del ViewModel (viewModelScope).
        // Esto asegura que la operación de la DB (que es suspendida) se ejecute de forma asíncrona
        // y se cancele si el ViewModel se destruye.
        viewModelScope.launch {
            // ⭐ Llamar a la función del Repository: insertarPerfil ⭐
            repository.insertarPerfil(perfil)
        }
    }
}

/**
 * 2. Factoría (Factory) para instanciar el ViewModel.
 * Es necesaria para crear una instancia de ProfileViewModel, ya que su constructor
 * requiere un argumento (el PerfilRepository).
 */
class ProfileViewModelFactory(private val repository: PerfilRepository) : ViewModelProvider.Factory {

    /** * Método central de la factoría. Se llama para crear la instancia del ViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que la clase solicitada sea ProfileViewModel
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            // Suprime la advertencia de casting inseguro, ya que se verifica la clase previamente.
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        // Lanza una excepción si se intenta crear una clase de ViewModel no soportada.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}