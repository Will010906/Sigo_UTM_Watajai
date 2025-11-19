package com.example.sigo_utm_watajai

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ContactInfoActivity : AppCompatActivity() {

    // Referencias para Contacto General
    private lateinit var etTelefono: EditText
    private lateinit var etCorreo: EditText

    // Referencias para Contacto SOS
    private lateinit var etSosNombre: EditText
    private lateinit var etSosCelular: EditText
    private lateinit var etSosTrabajo: EditText
    private lateinit var spinnerParentesco: Spinner

    // Objeto para manejar la persistencia de datos simulada
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiPerfilData", MODE_PRIVATE)

        setupToolbar()
        initializeViews() // Inicializa las variables de UI
        loadCurrentData() // Carga datos guardados
        setupUpdateButton() // Configura el botón
    }

    private fun initializeViews() {
        // Inicializar referencias (IDs de activity_contact_info.xml)
        etTelefono = findViewById(R.id.et_telefono)
        etCorreo = findViewById(R.id.et_correo)

        etSosNombre = findViewById(R.id.et_sos_nombre)
        etSosCelular = findViewById(R.id.et_sos_celular)
        etSosTrabajo = findViewById(R.id.et_sos_trabajo)
        spinnerParentesco = findViewById(R.id.spinner_parentesco)

        // Configurar el Spinner de Parentesco
        val parentescos = arrayOf("Selecciona", "Padre", "Madre", "Hermano/a", "Cónyuge", "Tutor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, parentescos)
        spinnerParentesco.adapter = adapter
    }

    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Contacto"
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun loadCurrentData() {
        // Carga de Contacto General (Lee desde SharedPreferences)
        etTelefono.setText(sharedPreferences.getString("telefono_key", "999-555-1234")) // Valor por defecto
        etCorreo.setText(sharedPreferences.getString("correo_key", "wilmer.e.lobato@mail.com"))

        // Carga de Contacto SOS
        etSosNombre.setText(sharedPreferences.getString("sos_nombre_key", "Laura Elena Pérez"))
        etSosCelular.setText(sharedPreferences.getString("sos_celular_key", "999-111-5678"))
        etSosTrabajo.setText(sharedPreferences.getString("sos_trabajo_key", ""))

        // Carga la selección del Spinner
        val parentescoGuardado = sharedPreferences.getString("parentesco_key", "Padre")
        val adapter = spinnerParentesco.adapter as ArrayAdapter<String>
        val index = adapter.getPosition(parentescoGuardado)
        spinnerParentesco.setSelection(index)
    }

    private fun setupUpdateButton() {
        val btnActualizar: Button = findViewById(R.id.btn_actualizar)

        btnActualizar.setOnClickListener {
            // Recolección de datos
            val telefono = etTelefono.text.toString()
            val sosNombre = etSosNombre.text.toString()
            val sosParentesco = spinnerParentesco.selectedItem.toString()

            // 1. Validación básica
            if (telefono.length < 10 || sosNombre.isBlank() || sosParentesco == "Selecciona") {
                Toast.makeText(this, "Asegúrate de ingresar datos válidos de contacto y SOS.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 2. Guardar los nuevos datos en SharedPreferences (Persistencia simulada)
            with(sharedPreferences.edit()) {
                // Contacto General
                putString("telefono_key", telefono)
                putString("correo_key", etCorreo.text.toString())
                // Contacto SOS
                putString("sos_nombre_key", sosNombre)
                putString("sos_celular_key", etSosCelular.text.toString())
                putString("sos_trabajo_key", etSosTrabajo.text.toString())
                putString("parentesco_key", sosParentesco)
                apply()
            }

            Toast.makeText(this, "¡Información de Contacto y SOS actualizada!", Toast.LENGTH_LONG).show()
        }
    }
}