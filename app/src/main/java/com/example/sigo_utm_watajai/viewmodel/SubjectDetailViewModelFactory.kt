package com.example.sigo_utm_watajai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sigo_utm_watajai.data.AsignaturaRepository

/**
 * Factoría (Factory) que le permite al sistema crear el SubjectDetailViewModel.
 * Es necesaria porque el ViewModel tiene dependencias en su constructor (el Repositorio).
 * Esto implementa el patrón de Inyección de Dependencias para los ViewModels.
 */
class SubjectDetailViewModelFactory(
    private val repository: AsignaturaRepository // Dependencia que se debe inyectar
) : ViewModelProvider.Factory {

    /**
     * Método central de la factoría. Es invocado por el sistema Android para crear el ViewModel.
     * @param modelClass La clase del ViewModel que se desea crear.
     * @return T Una nueva instancia del ViewModel solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica si la clase solicitada es SubjectDetailViewModel
        if (modelClass.isAssignableFrom(SubjectDetailViewModel::class.java)) {

            // Si es así, crea una nueva instancia de SubjectDetailViewModel pasándole la dependencia (el Repositorio).
            @Suppress("UNCHECKED_CAST")
            return SubjectDetailViewModel(repository) as T
        }

        // Si se solicita un ViewModel diferente que esta factoría no maneja, lanza una excepción.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}