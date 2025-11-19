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

/**
 * Activity para editar y guardar la información de Contacto General y Contacto SOS (Emergencia).
 * Actualmente utiliza SharedPreferences para la persistencia de datos simulada.
 */
class ContactInfoActivity : AppCompatActivity() {

    // Referencias para Contacto General (Teléfono y Correo)
    private lateinit var etTelefono: EditText
    private lateinit var etCorreo: EditText

    // Referencias para Contacto SOS (Nombre, Celular, Teléfono de Trabajo y Parentesco)
    private lateinit var etSosNombre: EditText
    private lateinit var etSosCelular: EditText
    private lateinit var etSosTrabajo: EditText
    private lateinit var spinnerParentesco: Spinner

    // Objeto para manejar la persistencia de datos simulada (SharedPreferences)
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)

        // Inicializar SharedPreferences (nombre del archivo: "MiPerfilData")
        sharedPreferences = getSharedPreferences("MiPerfilData", MODE_PRIVATE)

        setupToolbar()
        initializeViews() // Inicializa las variables de UI
        loadCurrentData() // Carga datos guardados desde SharedPreferences
        setupUpdateButton() // Configura el botón de guardar/actualizar
    }

    /**
     * Inicializa las referencias a los elementos de la interfaz de usuario (UI).
     * También configura el adaptador para el Spinner de parentesco.
     */
    private fun initializeViews() {
        // Inicializar referencias (IDs de activity_contact_info.xml)
        etTelefono = findViewById(R.id.et_telefono)
        etCorreo = findViewById(R.id.et_correo)

        etSosNombre = findViewById(R.id.et_sos_nombre)
        etSosCelular = findViewById(R.id.et_sos_celular)
        etSosTrabajo = findViewById(R.id.et_sos_trabajo)
        spinnerParentesco = findViewById(R.id.spinner_parentesco)

        // Configurar el Spinner de Parentesco con una lista de opciones
        val parentescos = arrayOf("Selecciona", "Padre", "Madre", "Hermano/a", "Cónyuge", "Tutor")
        // Crear ArrayAdapter para vincular la lista de strings al Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, parentescos)
        spinnerParentesco.adapter = adapter
    }

    /**
     * Configura la barra de herramientas (Toolbar).
     */
    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        // Establecer el título
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Contacto"
        // Configurar el botón de regreso
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    /**
     * Carga los datos de contacto actualmente guardados desde SharedPreferences
     * y los muestra en los campos de la interfaz.
     */
    private fun loadCurrentData() {
        // Carga de Contacto General (Lee desde SharedPreferences)
        // Utiliza valores por defecto si la clave no existe
        etTelefono.setText(sharedPreferences.getString("telefono_key", "999-555-1234"))
        etCorreo.setText(sharedPreferences.getString("correo_key", "wilmer.e.lobato@mail.com"))

        // Carga de Contacto SOS
        etSosNombre.setText(sharedPreferences.getString("sos_nombre_key", "Laura Elena Pérez"))
        etSosCelular.setText(sharedPreferences.getString("sos_celular_key", "999-111-5678"))
        etSosTrabajo.setText(sharedPreferences.getString("sos_trabajo_key", ""))

        // Carga la selección del Spinner (busca el índice del valor guardado)
        val parentescoGuardado = sharedPreferences.getString("parentesco_key", "Padre")
        @Suppress("UNCHECKED_CAST") // Suprime la advertencia de casting
        val adapter = spinnerParentesco.adapter as ArrayAdapter<String>
        val index = adapter.getPosition(parentescoGuardado) // Obtiene la posición del valor
        spinnerParentesco.setSelection(index) // Establece la selección en el Spinner
    }

    /**
     * Configura el Listener de clic para el botón de "Actualizar" (guardar datos).
     */
    private fun setupUpdateButton() {
        val btnActualizar: Button = findViewById(R.id.btn_actualizar)

        btnActualizar.setOnClickListener {
            // Recolección de datos de los campos de texto
            val telefono = etTelefono.text.toString()
            val sosNombre = etSosNombre.text.toString()
            // Obtener el valor seleccionado del Spinner
            val sosParentesco = spinnerParentesco.selectedItem.toString()

            // 1. Validación básica: Verifica campos obligatorios y formato simple
            if (telefono.length < 10 || sosNombre.isBlank() || sosParentesco == "Selecciona") {
                Toast.makeText(this, "Asegúrate de ingresar datos válidos de contacto y SOS.", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Sale de la función si la validación falla
            }

            // 2. Guardar los nuevos datos en SharedPreferences (Persistencia simulada)
            // Se usa 'with(sharedPreferences.edit())' para aplicar los cambios de forma síncrona/asíncrona.
            with(sharedPreferences.edit()) {
                // Contacto General
                putString("telefono_key", telefono)
                putString("correo_key", etCorreo.text.toString())
                // Contacto SOS
                putString("sos_nombre_key", sosNombre)
                putString("sos_celular_key", etSosCelular.text.toString())
                putString("sos_trabajo_key", etSosTrabajo.text.toString())
                putString("parentesco_key", sosParentesco)
                apply() // Aplica los cambios de forma asíncrona
            }

            // Muestra un mensaje de éxito al usuario
            Toast.makeText(this, "¡Información de Contacto y SOS actualizada!", Toast.LENGTH_LONG).show()
        }
    }
}