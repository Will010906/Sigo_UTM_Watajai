package com.example.sigo_utm_watajai

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sigo_utm_watajai.adapter.UnidadTematicaAdapter
import com.example.sigo_utm_watajai.data.AppDatabase
import com.example.sigo_utm_watajai.data.db.entity.Asignatura
import com.example.sigo_utm_watajai.data.db.entity.UnidadTematica
import com.example.sigo_utm_watajai.data.AsignaturaRepository
import com.example.sigo_utm_watajai.viewmodel.SubjectDetailViewModel
import com.example.sigo_utm_watajai.viewmodel.SubjectDetailViewModelFactory

// ⭐ FUNCIÓN DE EXTENSIÓN PARA UN SOLO EVENTO ⭐
/**
 * Función de extensión para LiveData que observa un valor una sola vez.
 * Es crucial en esta Activity para cargar las Unidades Temáticas de cada Asignatura
 * sin mantener múltiples observadores activos, mejorando la eficiencia.
 */
fun <T> LiveData<T>.observeOnce(owner: AppCompatActivity, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            // 1. Remueve el observador inmediatamente
            removeObserver(this)
            // 2. Ejecuta el código de manejo del valor
            observer(value)
        }
    })
}

/**
 * Activity que muestra el detalle de las asignaturas para un cuatrimestre seleccionado.
 * Implementa una vista dinámica que carga y expande el detalle de las unidades temáticas.
 */
class SubjectDetailActivity : AppCompatActivity() {

    // 1. Conexión a Room (Inicializaciones lazy para las dependencias MVVM)
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repository by lazy { AsignaturaRepository(database.asignaturaDao(), database.unidadTematicaDao()) }
    private val viewModelFactory by lazy { SubjectDetailViewModelFactory(repository) }
    private val viewModel: SubjectDetailViewModel by viewModels { viewModelFactory }

    // Contenedor principal donde se añadirán dinámicamente las vistas de cada asignatura.
    private lateinit var containerSubjects: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_detail)

        containerSubjects = findViewById(R.id.container_subjects)

        // Obtiene el nombre del cuatrimestre pasado desde la Activity anterior (AcademicHistoryActivity).
        val cuatrimestreNombre = intent.getStringExtra("CUATRIMESTRE_NOMBRE") ?: "Detalle"

        setupToolbar(cuatrimestreNombre)
        observeAsignaturas(cuatrimestreNombre) // Inicia la carga de asignaturas.
    }

    /**
     * Configura la Toolbar y el título principal de la Activity.
     */
    private fun setupToolbar(cuatrimestre: String) {
        val toolbar = findViewById<View>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.tv_title).text = cuatrimestre
        findViewById<TextView>(R.id.tv_cuatrimestre_title).text = "Asignaturas de $cuatrimestre"
        toolbar.findViewById<android.widget.ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    /**
     * Observa la lista de asignaturas para el cuatrimestre seleccionado.
     * Esta función se activa cuando el LiveData `asignaturas` del ViewModel emite un nuevo valor.
     */
    private fun observeAsignaturas(cuatrimestreNombre: String) {
        // Notifica al ViewModel qué cuatrimestre debe consultar (dispara el switchMap).
        viewModel.setNombreCuatrimestre(cuatrimestreNombre)

        viewModel.asignaturas.observe(this) { asignaturas ->
            // Limpia las vistas antiguas antes de redibujar.
            containerSubjects.removeAllViews()

            if (asignaturas != null && asignaturas.isNotEmpty()) {

                // Itera sobre cada asignatura recibida de la DB.
                asignaturas.forEach { asignatura ->

                    // ⭐ LÓGICA DE CARGA ANIDADA: Usa observeOnce para obtener las unidades temáticas.
                    // Solo consultamos la DB por las unidades una vez por cada asignatura.
                    viewModel.obtenerUnidadesPorAsignatura(asignatura.nombre).observeOnce(this) { unidades ->

                        // Llama al método para inflar la vista de la asignatura y sus unidades.
                        inflateSubjectView(
                            asignatura = asignatura,
                            unidades = unidades ?: emptyList(),
                            container = containerSubjects
                        )
                    }
                }
            } else {
                // Muestra un mensaje si no hay datos.
                val noDataView = TextView(this).apply { text = "No se encontraron asignaturas para este cuatrimestre." }
                containerSubjects.addView(noDataView)
            }
        }
    }

    /**
     * Infla y configura la vista de una asignatura, incluyendo el RecyclerView de sus unidades temáticas.
     */
    private fun inflateSubjectView(asignatura: Asignatura, unidades: List<UnidadTematica>, container: LinearLayout) {
        val inflater = LayoutInflater.from(this)
        // Infla el layout de la tarjeta de asignatura (item_subject_detail.xml)
        val subjectView = inflater.inflate(R.layout.item_subject_detail, container, false)

        // Referencias a las vistas del layout inflado
        val tvSubjectHeader: TextView = subjectView.findViewById(R.id.tv_subject_name_combined)
        // Contenedor que se expande/contrae para mostrar las unidades temáticas
        val layoutUnits: LinearLayout = subjectView.findViewById(R.id.layout_units)

        // 1. Llenar los datos principales
        tvSubjectHeader.text = asignatura.nombre
        subjectView.findViewById<TextView>(R.id.tv_teacher_name).text = asignatura.profesor
        subjectView.findViewById<TextView>(R.id.tv_progress).text = asignatura.progreso
        subjectView.findViewById<TextView>(R.id.tv_evaluation).text = asignatura.evaluacion
        subjectView.findViewById<TextView>(R.id.tv_desempeno).text = asignatura.desempenoGeneral


        // 2. ⭐ IMPLEMENTACIÓN DEL DESPLIEGUE (Expandable View) ⭐
        tvSubjectHeader.setOnClickListener {
            if (layoutUnits.visibility == View.GONE) {
                // Mostrar contenido (Expande)
                layoutUnits.visibility = View.VISIBLE
                // Cambiar ícono a flecha hacia arriba
                tvSubjectHeader.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_up, 0
                )
            } else {
                // Ocultar contenido (Contrae)
                layoutUnits.visibility = View.GONE
                // Cambiar ícono a flecha hacia abajo
                tvSubjectHeader.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_down, 0
                )
            }
        }

        // 3. ⭐ CONFIGURAR EL RECYCLERVIEW DE UNIDADES TEMÁTICAS ⭐
        val recyclerViewUnidades: RecyclerView = subjectView.findViewById(R.id.recycler_unidades_tematicas)

        // Crear y configurar el adaptador para la lista de unidades
        val adapterUnidadesInterno = UnidadTematicaAdapter()
        recyclerViewUnidades.adapter = adapterUnidadesInterno
        // Se usa LinearLayoutManager para la disposición vertical de las unidades
        recyclerViewUnidades.layoutManager = LinearLayoutManager(this)

        // 4. Pasamos los datos de las unidades temáticas al adaptador interno
        adapterUnidadesInterno.updateList(unidades)

        // Finalmente, añade la vista completa de la asignatura al contenedor principal
        container.addView(subjectView)
    }
}