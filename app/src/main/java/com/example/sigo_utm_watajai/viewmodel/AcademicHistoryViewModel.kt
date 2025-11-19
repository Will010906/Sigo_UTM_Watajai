package com.example.sigo_utm_watajai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sigo_utm_watajai.data.db.entity.Cuatrimestre
import com.example.sigo_utm_watajai.data.CuatrimestreRepository
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de negocio y los datos para el historial académico.
 * Es responsable de obtener la lista de cuatrimestres del repositorio y exponerla a la UI.
 *
 * @param repository Instancia del CuatrimestreRepository, inyectada para acceder a los datos de Room.
 */
class AcademicHistoryViewModel(private val repository: CuatrimestreRepository) : ViewModel() {

    /** * 1. LiveData que expone la lista de cuatrimestres al Activity/Fragment.
     * Este LiveData se obtiene directamente del repositorio (que a su vez lo obtiene de Room)
     * para mantener los datos frescos automáticamente.
     */
    val cuatrimestres = repository.listaCuatrimestres

    /**
     * Bloque de inicialización que se ejecuta al crear la instancia del ViewModel.
     */
    init {
        // Lanza una coroutine dentro del ámbito del ViewModel (viewModelScope),
        // lo que asegura que la operación se cancelará automáticamente si el ViewModel se destruye.
        viewModelScope.launch {
            // Verifica si la base de datos está vacía para precargar datos de ejemplo.
            if (cuatrimestres.value.isNullOrEmpty()) {
                precargarDatos()
            }
        }
    }

    /**
     * Función suspendida para precargar datos de cuatrimestres de ejemplo en la base de datos Room.
     * En una aplicación real, esta función contendría la lógica para llamar al API.
     */
    private suspend fun precargarDatos() {
        // Lista de objetos Cuatrimestre para inicializar la DB.
        val datosIniciales = listOf(
            Cuatrimestre(
                nombre = "1er cuatrimestre",
                periodo = "Sep - Dic 2024",
                carrera = "Tecnologías de la información",
                grupo = "1 B Matutino",
                tutor = "MATI Gerardo Chávez",
                desempeno = "A (Autónomo)", // ⭐ Valor CORREGIDO a String
            ),
            // ...
            Cuatrimestre(
                nombre = "4to cuatrimestre",
                periodo = "Ene - Abr 2025",
                carrera = "Tecnologías de la información",
                grupo = "4 B",
                tutor = "Dr. X Y Z",
                desempeno = "No calificado", // ⭐ Valor CORREGIDO a String
            )
        )
        // Llama al método del repositorio para insertar la lista en Room.
        repository.insertarLista(datosIniciales)
    }
}

/**
 * 2. Factoría (Factory) para instanciar el ViewModel.
 * Es necesaria cuando el ViewModel tiene dependencias en su constructor (como el Repository).
 * Esto permite al sistema de Android crear el ViewModel con sus dependencias inyectadas.
 */
class AcademicHistoryViewModelFactory(private val repository: CuatrimestreRepository) : ViewModelProvider.Factory {

    /** * Método central de la factoría. Se llama para crear la instancia del ViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que la clase solicitada sea AcademicHistoryViewModel
        if (modelClass.isAssignableFrom(AcademicHistoryViewModel::class.java)) {
            // Suprime la advertencia de casting inseguro, ya que se verifica la clase previamente.
            @Suppress("UNCHECKED_CAST")
            return AcademicHistoryViewModel(repository) as T
        }
        // Lanza una excepción si se intenta crear una clase de ViewModel no soportada por esta factoría.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}