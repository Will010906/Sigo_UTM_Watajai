package com.example.sigo_utm_watajai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sigo_utm_watajai.data.db.entity.Asignatura
import com.example.sigo_utm_watajai.data.db.entity.UnidadTematica
import com.example.sigo_utm_watajai.data.AsignaturaRepository
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica y los datos para la vista de detalle de asignaturas (SubjectDetail).
 * Es fundamental para cargar dinámicamente las asignaturas al seleccionar un cuatrimestre.
 *
 * @param repository Instancia del AsignaturaRepository, inyectada para acceder a los datos de Room.
 */
class SubjectDetailViewModel(private val repository: AsignaturaRepository) : ViewModel() {

    // 1. LiveData privado que almacena el nombre del cuatrimestre seleccionado.
    // Es la fuente de la verdad que dispara la recarga de datos.
    private val _nombreCuatrimestre = MutableLiveData<String>()

    /**
     * 2. LiveData principal para la Activity: Contiene la lista de asignaturas para el cuatrimestre cargado.
     * * Usamos switchMap: Cada vez que `_nombreCuatrimestre` emite un nuevo valor, se activa una nueva consulta
     * a Room con ese nombre, y el LiveData resultante se convierte en la fuente de `asignaturas`.
     */
    val asignaturas: LiveData<List<Asignatura>> =
        _nombreCuatrimestre.switchMap { nombre ->
            repository.obtenerAsignaturasPorCuatrimestre(nombre)
        }

    /**
     * 3. Función para obtener el LiveData de las Unidades Temáticas de una asignatura específica.
     * Se usa para cargar el detalle anidado cuando el usuario selecciona una asignatura.
     * @param nombreAsignatura El nombre de la asignatura para filtrar las unidades.
     * @return LiveData<List<UnidadTematica>> lista de unidades que pertenecen a esa asignatura.
     */
    fun obtenerUnidadesPorAsignatura(nombreAsignatura: String): LiveData<List<UnidadTematica>> {
        return repository.obtenerUnidadesPorAsignatura(nombreAsignatura)
    }

    /**
     * Bloque de inicialización: Se ejecuta al crear la instancia del ViewModel.
     */
    init {
        // Lanza una coroutine para precargar datos de ejemplo de asignaturas y unidades en Room.
        viewModelScope.launch {
            repository.precargarDatos()
        }
    }

    /**
     * Establece el nombre del cuatrimestre.
     * Al cambiar el valor de `_nombreCuatrimestre`, se dispara el `switchMap` para recargar las asignaturas.
     * @param nombre El nombre del cuatrimestre seleccionado por el usuario.
     */
    fun setNombreCuatrimestre(nombre: String) {
        // Verifica si el valor es diferente para evitar recargas innecesarias de la DB.
        if (_nombreCuatrimestre.value != nombre) {
            _nombreCuatrimestre.value = nombre
        }
    }
}

/**
 * Clase Factoría (Factory) para instanciar el ViewModel.
 * Es necesaria para inyectar el AsignaturaRepository al SubjectDetailViewModel.
 */
