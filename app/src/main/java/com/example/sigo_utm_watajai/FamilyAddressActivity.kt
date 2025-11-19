package com.example.sigo_utm_watajai

import android.content.Context
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
 * Activity para editar y guardar el domicilio familiar (o de tutores) del estudiante.
 * Utiliza SharedPreferences para la persistencia de datos simulada.
 */
class FamilyAddressActivity : AppCompatActivity() {

    // Referencias a los componentes de UI del domicilio familiar
    private lateinit var etCodigoPostal: EditText
    private lateinit var etCalle: EditText
    private lateinit var etNumExterior: EditText
    private lateinit var etNumInterior: EditText
    private lateinit var etEstado: EditText
    private lateinit var etMunicipio: EditText
    private lateinit var spinnerColonia: Spinner

    // Objeto para manejar la persistencia de datos simulada (SharedPreferences)
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_address) // Layout específico para el domicilio familiar

        // Inicializar SharedPreferences con el nombre del archivo "MiPerfilData"
        sharedPreferences = getSharedPreferences("MiPerfilData", Context.MODE_PRIVATE)

        setupToolbar()
        initializeViews() // Conecta las variables con los IDs del XML
        loadCurrentData() // Carga los datos previamente guardados
        setupUpdateButton() // Configura el botón para guardar los cambios
    }

    /**
     * Inicializa las referencias a las vistas de la interfaz de usuario.
     * También configura el adaptador para el Spinner de selección de Colonia.
     */
    private fun initializeViews() {
        // Inicializar referencias (Asegúrate de que los IDs existan en activity_family_address.xml)
        etCodigoPostal = findViewById(R.id.et_codigo_postal)
        etCalle = findViewById(R.id.et_calle)
        etNumExterior = findViewById(R.id.et_num_exterior)
        etNumInterior = findViewById(R.id.et_num_interior)
        etEstado = findViewById(R.id.et_estado)
        etMunicipio = findViewById(R.id.et_municipio)
        spinnerColonia = findViewById(R.id.spinner_colonia)

        // Configurar el Spinner de Colonia (Ejemplo de colonias)
        val colonias = arrayOf("Selecciona", "Las Américas", "Villas del Pedregal", "Morelos Oriente")
        // Crear ArrayAdapter para vincular la lista de strings al Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colonias)
        spinnerColonia.adapter = adapter
    }

    /**
     * Configura la barra de herramientas (Toolbar), establece el título y el botón de regreso.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Domicilio Familiar"
        // Configurar el botón de regreso
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    /**
     * Carga los datos del domicilio familiar actualmente guardados desde SharedPreferences.
     * Utiliza claves con el prefijo "fam_" para distinguirlas de las claves del domicilio actual.
     */
    private fun loadCurrentData() {
        // --- Carga de datos desde SharedPreferences (usando claves "fam_...") ---

        // Carga de campos de texto, utilizando valores por defecto si la clave no existe
        etCodigoPostal.setText(sharedPreferences.getString("fam_cp_key", "58000"))
        etCalle.setText(sharedPreferences.getString("fam_calle_key", "Av. Francisco I. Madero"))
        etNumExterior.setText(sharedPreferences.getString("fam_num_ext_key", "500"))
        etNumInterior.setText(sharedPreferences.getString("fam_num_int_key", "201"))

        // Simular campos llenados por el CP (Estado y Municipio)
        etEstado.setText(sharedPreferences.getString("fam_estado_key", "Michoacán de Ocampo"))
        etMunicipio.setText(sharedPreferences.getString("fam_municipio_key", "Morelia"))

        // Carga la selección del Spinner (Colonia): busca la posición del valor guardado
        val coloniaGuardada = sharedPreferences.getString("fam_colonia_key", "Las Américas")
        @Suppress("UNCHECKED_CAST")
        val adapter = spinnerColonia.adapter as ArrayAdapter<String>
        val index = adapter.getPosition(coloniaGuardada)
        spinnerColonia.setSelection(index)
    }

    /**
     * Configura el Listener de clic para el botón de "Actualizar" (guardar datos).
     */
    private fun setupUpdateButton() {
        val btnActualizar: Button = findViewById(R.id.btn_actualizar)

        btnActualizar.setOnClickListener {
            // Recolección de datos
            val cp = etCodigoPostal.text.toString()
            val calle = etCalle.text.toString()
            val colonia = spinnerColonia.selectedItem.toString()

            // 1. Validación básica: Verifica campos obligatorios y formato
            if (cp.length != 5 || calle.isBlank() || colonia == "Selecciona") {
                Toast.makeText(this, "El CP, la Calle y la Colonia son obligatorios.", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Sale de la función si la validación falla
            }

            // 2. Guardar los nuevos datos en SharedPreferences (Persistencia simulada)
            with(sharedPreferences.edit()) {
                putString("fam_cp_key", cp)
                putString("fam_calle_key", calle)
                putString("fam_num_ext_key", etNumExterior.text.toString())
                putString("fam_num_int_key", etNumInterior.text.toString())
                putString("fam_estado_key", etEstado.text.toString())
                putString("fam_municipio_key", etMunicipio.text.toString())
                putString("fam_colonia_key", colonia)
                apply() // Aplica los cambios de forma asíncrona
            }

            // Muestra un mensaje de éxito al usuario
            Toast.makeText(this, "¡Domicilio Familiar actualizado con éxito!", Toast.LENGTH_LONG).show()
        }
    }
}