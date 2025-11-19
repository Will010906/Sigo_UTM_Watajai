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

class FamilyAddressActivity : AppCompatActivity() {

    // Referencias a los componentes de UI del domicilio familiar
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
        setContentView(R.layout.activity_family_address) // Asegúrate de usar el layout familiar

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiPerfilData", Context.MODE_PRIVATE)

        setupToolbar()
        initializeViews()
        loadCurrentData()
        setupUpdateButton()
    }

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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colonias)
        spinnerColonia.adapter = adapter
    }

    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Domicilio Familiar"
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun loadCurrentData() {
        // --- Carga de datos desde SharedPreferences (usando claves "fam_...") ---

        // Carga de campos de texto
        etCodigoPostal.setText(sharedPreferences.getString("fam_cp_key", "58000"))
        etCalle.setText(sharedPreferences.getString("fam_calle_key", "Av. Francisco I. Madero"))
        etNumExterior.setText(sharedPreferences.getString("fam_num_ext_key", "500"))
        etNumInterior.setText(sharedPreferences.getString("fam_num_int_key", "201"))

        // Simular campos llenados por el CP
        etEstado.setText(sharedPreferences.getString("fam_estado_key", "Michoacán de Ocampo"))
        etMunicipio.setText(sharedPreferences.getString("fam_municipio_key", "Morelia"))

        // Carga la selección del Spinner (Colonia)
        val coloniaGuardada = sharedPreferences.getString("fam_colonia_key", "Las Américas")
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

            // 2. Guardar los nuevos datos en SharedPreferences (Persistencia simulada)
            with(sharedPreferences.edit()) {
                putString("fam_cp_key", cp)
                putString("fam_calle_key", calle)
                putString("fam_num_ext_key", etNumExterior.text.toString())
                putString("fam_num_int_key", etNumInterior.text.toString())
                putString("fam_estado_key", etEstado.text.toString())
                putString("fam_municipio_key", etMunicipio.text.toString())
                putString("fam_colonia_key", colonia)
                apply()
            }

            Toast.makeText(this, "¡Domicilio Familiar actualizado con éxito!", Toast.LENGTH_LONG).show()
        }
    }
}