package com.example.sigo_utm_watajai

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sigo_utm_watajai.ProfileEditActivity
import com.example.sigo_utm_watajai.ContactInfoActivity
import com.example.sigo_utm_watajai.CurrentAddressActivity
import com.example.sigo_utm_watajai.FamilyAddressActivity
// Importación de ProfileActivity es redundante si está en el mismo paquete,
// pero se mantiene si se requiere por configuración.

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 1. Configurar la Toolbar
        setupToolbar()

        // 2. Cargar los datos del resumen del perfil (SOLO UNA LLAMADA)
        loadProfileSummary()

        // 3. Configurar las opciones de menú y sus clicks
        setupMenuOptions()
    }

    private fun setupToolbar() {
        // 1. Obtener la referencia al contenedor de la Toolbar (esto ya lo corrigió)
        val toolbar = findViewById<View>(R.id.toolbar_profile)

        if (toolbar != null) {
            // 🚨 CORRECCIÓN: Usar el ID del TextView del título
            val tvTitle = toolbar.findViewById<TextView>(R.id.tv_toolbar_title)

            // Asignar el texto solo si encontramos el TextView
            if (tvTitle != null) {
                tvTitle.text = "Mi Perfil"
            }

            // Configurar el botón de regreso
            toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
                finish()
            }
        }
    }

    private fun loadProfileSummary() {
        // --- 1. Obtener referencias de las vistas ---
        val tvName: TextView = findViewById(R.id.tv_student_name_full)
        val tvEmailInst: TextView = findViewById(R.id.tv_email_inst)
        val tvPassInst: TextView = findViewById(R.id.tv_pass_inst)
        val tvUserPlataforma: TextView = findViewById(R.id.tv_user_plataforma)
        val tvPassPlataforma: TextView = findViewById(R.id.tv_pass_plataforma)

        // --- 2. Asignación de Datos Fijos/Simulados ---
        tvName.text = "Lobato Alcantar Wilmer Ernesto"
        tvEmailInst.text = "utm2410061@morelia.edu.mx"
        tvPassInst.text = "2006090+"
        tvUserPlataforma.text = "utm2410061@butmorelia.edu.mx"
        tvPassPlataforma.text = "pass2024+"
    }

    private fun setupMenuOptions() {
        // ... (La definición de setOption es la misma) ...
        fun setOption(view: View, title: String, subtitle: String, iconResId: Int, destinationActivity: Class<*>, statusIconResId: Int = 0) {
            view.findViewById<TextView>(R.id.tv_title).text = title
            view.findViewById<TextView>(R.id.tv_subtitle).text = subtitle
            view.findViewById<ImageView>(R.id.iv_icon).setImageResource(iconResId)

            // Manejo del ícono de estatus (secundario)
            val statusIcon = view.findViewById<ImageView>(R.id.iv_status_icon)
            if (statusIconResId != 0) {
                statusIcon.setImageResource(statusIconResId)
                statusIcon.visibility = View.VISIBLE
            } else {
                statusIcon.visibility = View.GONE
            }

            // Define la acción de clic
            view.setOnClickListener {
                val intent = Intent(this, destinationActivity)
                startActivity(intent)
            }
        }

        // ***************************************************************
        // AHORA LLAMAMOS A setOption
        // ***************************************************************

        // ... (La lógica de llamada es la misma) ...
        val optPersonal = findViewById<View>(R.id.option_personal_info)
        setOption(optPersonal,
            "Información Personal",
            "Nombre, Sexo, CURP, Seguridad Social",
            R.drawable.ic_fingerprint,
            ProfileEditActivity::class.java
        )
        // --- 2. Contacto ---
        val optContact = findViewById<View>(R.id.option_contact_info)
        setOption(optContact,
            "Contacto",
            "Teléfonos y Correo de Emergencia",
            R.drawable.ic_contact_phone,
            ContactInfoActivity::class.java
        )

        // --- 3. Domicilio Actual ---
        val optCurrentAddress = findViewById<View>(R.id.option_current_address)
        setOption(optCurrentAddress,
            "Domicilio Actual",
            "Dirección donde resides actualmente",
            R.drawable.ic_contacts,
            CurrentAddressActivity::class.java
        )

        // --- 4. Domicilio Familiar ---
        val optFamilyAddress = findViewById<View>(R.id.option_family_address)
        setOption(optFamilyAddress,
            "Domicilio Familiar",
            "Dirección de tu familia/tutores",
            R.drawable.ic_contact_emergency,
            FamilyAddressActivity::class.java
        )
    } // <--- ESTE CIERRE DE setupMenuOptions() AHORA ES CORRECTO
}