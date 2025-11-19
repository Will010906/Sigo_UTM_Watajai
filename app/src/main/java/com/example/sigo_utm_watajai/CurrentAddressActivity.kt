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

class CurrentAddressActivity : AppCompatActivity() {

    // Referencias a los componentes de UI del domicilio
    private lateinit var etCodigoPostal: EditText
    private lateinit var etCalle: EditText
    private lateinit var etNumExterior: EditText
    private lateinit var etNumInterior: EditText
    private lateinit var etEstado: EditText
    private lateinit var etMunicipio: EditText
    private lateinit var spinnerColonia: Spinner

    // Objeto para manejar la persistencia de datos simulada
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_address)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiPerfilData", MODE_PRIVATE)

        setupToolbar()
        initializeViews()
        loadCurrentData()
        setupUpdateButton()
    }

    private fun initializeViews() {
        // Inicializar referencias (Asegúrate de que los IDs existan en activity_current_address.xml)
        etCodigoPostal = findViewById(R.id.et_codigo_postal)
        etCalle = findViewById(R.id.et_calle)
        etNumExterior = findViewById(R.id.et_num_exterior)
        etNumInterior = findViewById(R.id.et_num_interior)
        etEstado = findViewById(R.id.et_estado)
        etMunicipio = findViewById(R.id.et_municipio)
        spinnerColonia = findViewById(R.id.spinner_colonia)

        // Configurar el Spinner de Colonia (Ejemplo de colonias para CP 58090)
        val colonias = arrayOf("Selecciona", "Centro Histórico", "Jardines de Morelos", "Vasco de Quiroga")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colonias)
        spinnerColonia.adapter = adapter
    }

    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Domicilio Actual"
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun loadCurrentData() {
        // --- Carga de datos desde SharedPreferences ---

        // Carga de campos de texto
        etCodigoPostal.setText(sharedPreferences.getString("dom_cp_key", "58090"))
        etCalle.setText(sharedPreferences.getString("dom_calle_key", "Av. Universidad"))
        etNumExterior.setText(sharedPreferences.getString("dom_num_ext_key", "123"))
        etNumInterior.setText(sharedPreferences.getString("dom_num_int_key", ""))

        // Simular campos llenados por el CP
        etEstado.setText(sharedPreferences.getString("dom_estado_key", "Michoacán de Ocampo"))
        etMunicipio.setText(sharedPreferences.getString("dom_municipio_key", "Morelia"))

        // Carga la selección del Spinner (Colonia)
        val coloniaGuardada = sharedPreferences.getString("dom_colonia_key", "Centro Histórico")
        val adapter = spinnerColonia.adapter as ArrayAdapter<String>
        val index = adapter.getPosition(coloniaGuardada)
        spinnerColonia.setSelection(index)
    }

    private fun setupUpdateButton() {
        val btnActualizar: Button = findViewById(R.id.btn_actualizar)

        btnActualizar.setOnClickListener {
            val cp = etCodigoPostal.text.toString()
            val calle = etCalle.text.toString()
            val colonia = spinnerColonia.selectedItem.toString()

            // 1. Validación básica
            if (cp.length != 5 || calle.isBlank() || colonia == "Selecciona") {
                Toast.makeText(this, "El CP, la Calle y la Colonia son obligatorios.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 2. Guardar los nuevos datos en SharedPreferences
            with(sharedPreferences.edit()) {
                putString("dom_cp_key", cp)
                putString("dom_calle_key", calle)
                putString("dom_num_ext_key", etNumExterior.text.toString())
                putString("dom_num_int_key", etNumInterior.text.toString())
                putString("dom_estado_key", etEstado.text.toString())
                putString("dom_municipio_key", etMunicipio.text.toString())
                putString("dom_colonia_key", colonia)
                apply()
            }

            Toast.makeText(this, "¡Domicilio Actual actualizado con éxito!", Toast.LENGTH_LONG).show()
        }
    }
}