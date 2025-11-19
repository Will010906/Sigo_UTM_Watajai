package com.example.sigo_utm_watajai

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels // Delegado para obtener el ViewModel
import com.example.sigo_utm_watajai.ProfileEditActivity
import com.example.sigo_utm_watajai.ContactInfoActivity
import com.example.sigo_utm_watajai.CurrentAddressActivity
import com.example.sigo_utm_watajai.FamilyAddressActivity
// IMPORTS DE ROOM Y MVVM
import com.example.sigo_utm_watajai.data.AppDatabase
import com.example.sigo_utm_watajai.data.db.entity.Perfil // Entidad de datos
import com.example.sigo_utm_watajai.data.repository.PerfilRepository
import com.example.sigo_utm_watajai.viewmodel.ProfileViewModel
import com.example.sigo_utm_watajai.viewmodel.ProfileViewModelFactory


/**
 * Activity que muestra el perfil completo del estudiante.
 * Utiliza Room (a través del ViewModel) para cargar y observar los datos del perfil activo.
 */
class ProfileActivity : AppCompatActivity() {

    // ===========================================================================
    // CONEXIÓN A ROOM: Inicializaciones lazy para las dependencias
    // ===========================================================================
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repository by lazy { PerfilRepository(database.perfilDao()) }
    private val viewModelFactory by lazy { ProfileViewModelFactory(repository) }

    // ⭐ Obtiene la instancia del ViewModel, inyectando la factoría.
    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupToolbar()
        setupMenuOptions()

        // ⭐ CONEXIÓN A ROOM: Observar los datos del perfil
        // Este bloque se ejecuta cada vez que el perfil se guarda o actualiza en la DB.
        viewModel.perfil.observe(this) { perfil: Perfil? ->
            if (perfil != null) {
                // Si hay datos, se cargan los valores dinámicos.
                loadProfileSummary(perfil)
            } else {
                // Si el perfil es nulo (ej. error o primera ejecución sin login), se cargan datos por defecto.
                loadInitialData()
            }
        }
    }

    /**
     * Configura la barra de herramientas (Toolbar).
     * Establece el título y el manejador de clic para el botón de regreso.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar_profile)

        if (toolbar != null) {
            val tvTitle = toolbar.findViewById<TextView>(R.id.tv_toolbar_title)
            if (tvTitle != null) {
                tvTitle.text = "Mi Perfil"
            }
            toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
                finish() // Cierra la actividad.
            }
        }
    }

    /**
     * ⭐ FUNCIÓN: Carga datos fijos/iniciales si Room aún no tiene perfil o si ocurre un error.
     */
    private fun loadInitialData() {
        // --- Obtener referencias de las vistas ---
        val tvName: TextView = findViewById(R.id.tv_student_name_full)
        val tvEmailInst: TextView = findViewById(R.id.tv_email_inst)
        val tvPassInst: TextView = findViewById(R.id.tv_pass_inst)
        val tvUserPlataforma: TextView = findViewById(R.id.tv_user_plataforma)
        val tvPassPlataforma: TextView = findViewById(R.id.tv_pass_plataforma)

        // --- Asignación de Datos Fijos/Simulados ---
        tvName.text = "Nombre de Estudiante no definido"
        tvEmailInst.text = "correo@ejemplo.edu.mx"
        tvPassInst.text = "********"
        tvUserPlataforma.text = "usuario@plataforma.edu.mx"
        tvPassPlataforma.text = "********"
    }

    /**
     * ⭐ FUNCIÓN MODIFICADA: Carga datos dinámicos desde Room (desde el objeto Perfil).
     */
    private fun loadProfileSummary(perfil: Perfil) {
        // --- 1. Obtener referencias de las vistas ---
        val tvName: TextView = findViewById(R.id.tv_student_name_full)
        val tvEmailInst: TextView = findViewById(R.id.tv_email_inst)
        val tvPassInst: TextView = findViewById(R.id.tv_pass_inst)
        val tvUserPlataforma: TextView = findViewById(R.id.tv_user_plataforma)
        val tvPassPlataforma: TextView = findViewById(R.id.tv_pass_plataforma)

        // ⭐ 2. USAR EL CAMPO ÚNICO Y SEGURO PARA EL NOMBRE ⭐
        // Utiliza 'nombreCompleto' para mostrar el nombre tal como viene del login,
        // evitando problemas de separación o concatenación.
        tvName.text = perfil.nombreCompleto

        // 3. Asignación del resto de los datos
        tvEmailInst.text = perfil.email
        tvUserPlataforma.text = perfil.email

        // Contraseñas (mostrar seguras, ya que no se almacenan las reales)
        tvPassInst.text = "********"
        tvPassPlataforma.text = "********"
    }

    /**
     * Configura las opciones del menú de edición de perfil (tarjetas).
     */
    private fun setupMenuOptions() {
        /**
         * Función helper para inicializar una opción de menú y su comportamiento de navegación.
         */
        fun setOption(view: View, title: String, subtitle: String, iconResId: Int, destinationActivity: Class<*>, statusIconResId: Int = 0) {
            // Asignación de textos e íconos principales
            view.findViewById<TextView>(R.id.tv_title).text = title
            view.findViewById<TextView>(R.id.tv_subtitle).text = subtitle
            view.findViewById<ImageView>(R.id.iv_icon).setImageResource(iconResId)

            // Manejo del ícono de estatus (secundario, opcional)
            val statusIcon = view.findViewById<ImageView>(R.id.iv_status_icon)
            if (statusIconResId != 0) {
                statusIcon.setImageResource(statusIconResId)
                statusIcon.visibility = View.VISIBLE
            } else {
                statusIcon.visibility = View.GONE
            }

            // Define la acción de clic (navegación)
            view.setOnClickListener {
                val intent = Intent(this, destinationActivity)
                startActivity(intent)
            }
        }

        // --- Configuración de Opciones ---

        // 1. Información Personal (Nombre, CURP, etc.)
        val optPersonal = findViewById<View>(R.id.option_personal_info)
        setOption(optPersonal,
            "Información Personal",
            "Nombre, Sexo, CURP, Seguridad Social",
            R.drawable.ic_fingerprint,
            ProfileEditActivity::class.java
        )

        // 2. Contacto (Teléfonos y Correo de Emergencia)
        val optContact = findViewById<View>(R.id.option_contact_info)
        setOption(optContact,
            "Contacto",
            "Teléfonos y Correo de Emergencia",
            R.drawable.ic_contact_phone,
            ContactInfoActivity::class.java
        )

        // 3. Domicilio Actual
        val optCurrentAddress = findViewById<View>(R.id.option_current_address)
        setOption(optCurrentAddress,
            "Domicilio Actual",
            "Dirección donde resides actualmente",
            R.drawable.ic_contacts,
            CurrentAddressActivity::class.java
        )

        // 4. Domicilio Familiar
        val optFamilyAddress = findViewById<View>(R.id.option_family_address)
        setOption(optFamilyAddress,
            "Domicilio Familiar",
            "Dirección de tu familia/tutores",
            R.drawable.ic_contact_emergency,
            FamilyAddressActivity::class.java
        )
    }
}