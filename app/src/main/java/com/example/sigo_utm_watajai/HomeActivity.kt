package com.example.sigo_utm_watajai
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
// IMPORTS PARA NAVEGACIÓN
import com.example.sigo_utm_watajai.AcademicHistoryActivity
import com.example.sigo_utm_watajai.ProfileActivity
// IMPORTS PARA ROOM Y MVVM
import com.example.sigo_utm_watajai.data.AppDatabase
import com.example.sigo_utm_watajai.data.db.entity.Perfil // Entidad de datos del perfil
import com.example.sigo_utm_watajai.data.repository.PerfilRepository
import com.example.sigo_utm_watajai.viewmodel.ProfileViewModel
import com.example.sigo_utm_watajai.viewmodel.ProfileViewModelFactory
import androidx.activity.viewModels // Delegado para obtener el ViewModel
import android.widget.TextView // Para mostrar la matrícula


/**
 * Activity principal que sirve como menú de navegación (Dashboard) de la aplicación.
 * Muestra el identificador del usuario (matrícula) y las opciones principales.
 */
class HomeActivity : AppCompatActivity() {

    // ===========================================================================
    // ⭐ 1. DECLARACIÓN DE DEPENDENCIAS DE ROOM (MVVM) ⭐
    // Inicialización lazy de las dependencias necesarias para Room y el ViewModel.
    // ===========================================================================
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repository by lazy { PerfilRepository(database.perfilDao()) }
    private val viewModelFactory by lazy { ProfileViewModelFactory(repository) }

    // Obtiene la instancia del ProfileViewModel, inyectando la factoría.
    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    // ⭐ 2. DECLARACIÓN DE VISTA (Solo la Matrícula) ⭐
    // TextView para mostrar el ID del usuario (matrícula).
    private lateinit var tvUserId: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 3. OBTENER REFERENCIAS DE VISTAS
        tvUserId = findViewById(R.id.tv_user_id) // ID para la matrícula (el usuario)

        val perfilCard: CardView = findViewById(R.id.card_perfil) // Tarjeta "Mi Perfil"
        val historialCard: CardView = findViewById(R.id.card_historial) // Tarjeta "Historial académico"

        // 4. OBSERVACIÓN DE DATOS DE ROOM (MVVM)
        // Observa el LiveData del perfil. Esto permite actualizar la UI cada vez que el perfil cambie en la DB.
        viewModel.perfil.observe(this) { perfil: Perfil? ->
            if (perfil != null) {
                // Si hay un perfil, se carga la matrícula.
                loadUserMatricula(perfil)
            }
        }

        // 5. Conexión de Clicks (Configuración de navegación)
        perfilCard.setOnClickListener {
            navigateToProfile()
        }
        historialCard.setOnClickListener {
            navigateToAcademicHistory()
        }
    }

    /**
     * ⭐ FUNCIÓN DE CARGA: Asigna el valor de la matrícula al TextView correspondiente.
     * @param perfil El objeto Perfil que contiene los datos del usuario guardados en Room.
     */
    private fun loadUserMatricula(perfil: Perfil) {
        // Asignar la matrícula que se guardó durante el login
        tvUserId.text = perfil.matricula

        // Se omite la carga de otros campos (como el cuatrimestre) según la especificación.
    }

    // --- Funciones de Navegación ---

    /** Navega a la Activity de Perfil. */
    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    /** Navega a la Activity de Historial Académico. */
    private fun navigateToAcademicHistory() {
        val intent = Intent(this, AcademicHistoryActivity::class.java)
        startActivity(intent)
    }
}