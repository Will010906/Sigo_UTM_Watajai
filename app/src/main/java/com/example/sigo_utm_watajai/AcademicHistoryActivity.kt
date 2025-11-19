package com.example.sigo_utm_watajai

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sigo_utm_watajai.adapter.CuatrimestreAdapter
import com.example.sigo_utm_watajai.data.AppDatabase
import com.example.sigo_utm_watajai.data.db.entity.Cuatrimestre
import com.example.sigo_utm_watajai.data.CuatrimestreRepository
import com.example.sigo_utm_watajai.viewmodel.AcademicHistoryViewModel
import com.example.sigo_utm_watajai.viewmodel.AcademicHistoryViewModelFactory
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Activity que muestra el historial académico del estudiante (lista de cuatrimestres).
 * Utiliza RecyclerView para la visualización y MVVM (Room + ViewModel + LiveData) para los datos.
 */
class AcademicHistoryActivity : AppCompatActivity() {

    // ===========================================================================
    // Conexión a Room: Inicializaciones lazy (se inicializan al primer uso)
    // ===========================================================================

    // Instancia de la base de datos Singleton.
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    // Instancia del Repositorio, que requiere el DAO.
    private val repository by lazy { CuatrimestreRepository(database.cuatrimestreDao()) }
    // Instancia de la Factoría, que requiere el Repositorio.
    private val viewModelFactory by lazy { AcademicHistoryViewModelFactory(repository) }


    // 1. Obtener el ViewModel de la actividad, usando la factoría para inyectar el Repositorio.
    private val viewModel: AcademicHistoryViewModel by viewModels { viewModelFactory }

    // Adaptador para gestionar la lista de cuatrimestres en el RecyclerView.
    private lateinit var cuatrimestreAdapter: CuatrimestreAdapter

    /**
     * Definición del listener (callback) que se ejecuta cuando el usuario hace clic en una tarjeta de cuatrimestre.
     * La acción es navegar a la Activity de detalle de Asignaturas.
     */
    private val clickListener: (Cuatrimestre) -> Unit = { cuatrimestre ->
        // Llama a la función de navegación, pasando el nombre del cuatrimestre.
        navigateToSubjectDetail(cuatrimestre.nombre)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic_history)

        setupToolbar()
        setupRecyclerView()
        observeCuatrimestres() // Iniciar la observación de datos
    }

    /**
     * Configura la barra de herramientas (Toolbar) de la actividad.
     * Establece el título y el manejador de clic para el botón de regreso.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        // Establecer el título
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Historial Académico"

        // Configurar el botón de regreso (volver a la actividad anterior)
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    /**
     * Configura el RecyclerView para mostrar la lista de cuatrimestres.
     */
    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_cuatrimestres)

        // Inicializar el adaptador, pasándole la función clickListener definida previamente.
        cuatrimestreAdapter = CuatrimestreAdapter(clickListener)

        // Asignar el adaptador al RecyclerView
        recyclerView.adapter = cuatrimestreAdapter

        // Asignar LayoutManager si no está definido en el XML (necesario para el desplazamiento)
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    /**
     * Inicia la observación del LiveData de la lista de cuatrimestres del ViewModel.
     * Cada vez que los datos cambian en Room, la lista se actualiza automáticamente.
     */
    private fun observeCuatrimestres() {
        // Observa el LiveData del ViewModel
        viewModel.cuatrimestres.observe(this) { lista ->
            // Cuando Room emite la lista, actualizamos el adapter.
            if (lista != null) {
                cuatrimestreAdapter.updateList(lista)
            }
        }
    }

    /**
     * Navega a la Activity de detalle de Asignaturas.
     * Pasa el nombre del cuatrimestre como un extra en el Intent para que la nueva Activity
     * sepa qué asignaturas debe cargar.
     *
     * @param cuatrimestreNombre El nombre del cuatrimestre seleccionado (ej. "4to cuatrimestre").
     */
    private fun navigateToSubjectDetail(cuatrimestreNombre: String) {
        val intent = Intent(this, SubjectDetailActivity::class.java)
        // Adjuntar el nombre del cuatrimestre para filtrar la siguiente consulta
        intent.putExtra("CUATRIMESTRE_NOMBRE", cuatrimestreNombre)
        startActivity(intent)
    }
}