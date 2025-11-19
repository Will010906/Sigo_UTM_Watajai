package com.example.sigo_utm_watajai.data

import androidx.lifecycle.LiveData
import com.example.sigo_utm_watajai.data.db.dao.AsignaturaDao
import com.example.sigo_utm_watajai.data.db.dao.UnidadTematicaDao
import com.example.sigo_utm_watajai.data.db.entity.Asignatura
import com.example.sigo_utm_watajai.data.db.entity.UnidadTematica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio de Asignaturas y Unidades Temáticas.
 * Actúa como una fuente única de verdad para los datos académicos, abstrae la fuente de datos
 * (en este caso, los DAOs de Room) de los ViewModels.
 * * @param asignaturaDao DAO para acceder a los datos de la tabla 'asignaturas'.
 * @param unidadDao DAO para acceder a los datos de la tabla 'unidades_tematicas'.
 */
class AsignaturaRepository(
    // Recibe los dos DAOs necesarios (Inyección de Dependencias)
    private val asignaturaDao: AsignaturaDao,
    private val unidadDao: UnidadTematicaDao
) {
    /** * 1. Obtiene la lista de asignaturas activas o cursadas por el estudiante.
     * @param nombreCuatrimestre El cuatrimestre a filtrar (ej: "4to cuatrimestre").
     * @return LiveData<List<Asignatura>> que se actualiza automáticamente al haber cambios en la DB.
     */
    fun obtenerAsignaturasPorCuatrimestre(nombreCuatrimestre: String): LiveData<List<Asignatura>> {
        return asignaturaDao.obtenerAsignaturasPorCuatrimestre(nombreCuatrimestre)
    }

    /** * 2. Obtener las unidades temáticas de una asignatura específica.
     * Se usa típicamente para cargar el detalle de una asignatura seleccionada.
     * @param nombreAsignatura El nombre de la asignatura a filtrar.
     * @return LiveData<List<UnidadTematica>> que se actualiza automáticamente.
     */
    fun obtenerUnidadesPorAsignatura(nombreAsignatura: String): LiveData<List<UnidadTematica>> {
        return unidadDao.obtenerUnidadesPorAsignatura(nombreAsignatura)
    }

    /** * 3. Precarga de datos iniciales.
     * * Esta función es 'suspend' porque realiza operaciones de escritura en Room.
     * Inserta datos de ejemplo de asignaturas y unidades para pruebas o para inicializar la DB.
     */
    suspend fun precargarDatos() {
        // Asegura que las operaciones de Room se ejecuten en el hilo de IO (Entrada/Salida)
        withContext(Dispatchers.IO) {

            // Datos de ejemplo para las asignaturas principales
            val asignaturas = listOf(
                Asignatura(id = 1, cuatrimestreNombre = "4to cuatrimestre", nombre = "Inglés", profesor = "Lic. Maria Veronica Alvarez Ríos", progreso = "100%", evaluacion = "Ordinaria", desempenoGeneral = "E (Estratégico)", calificacion = 9.5, estatus = "Cursando"),
                Asignatura(id = 2, cuatrimestreNombre = "4to cuatrimestre", nombre = "Física", profesor = "Dr. Raul Lopez", progreso = "80%", evaluacion = "Ordinaria", desempenoGeneral = "B (Satisfactorio)", calificacion = 8.0, estatus = "Cursando"),
                Asignatura(id = 3, cuatrimestreNombre = "3er cuatrimestre", nombre = "Desarrollo Humano", profesor = "Lic. Maria Paz", progreso = "100%", evaluacion = "Ordinaria", desempenoGeneral = "A (Autónomo)", calificacion = 10.0, estatus = "Aprobada")
            )

            // Datos de ejemplo para las unidades temáticas (asociadas a "Inglés")
            val unidades = listOf(
                UnidadTematica(asignaturaNombre = "Inglés", nombre = "Presentación Personal", desempenoUnidad = "(Estratégico)"),
                UnidadTematica(asignaturaNombre = "Inglés", nombre = "Actividades Diarias y de Rutina", desempenoUnidad = "(Estratégico)")
            )

            // Inserción asíncrona de los datos en Room
            asignaturaDao.insertarTodas(asignaturas)
            unidadDao.insertarTodas(unidades)
        }
    }
}