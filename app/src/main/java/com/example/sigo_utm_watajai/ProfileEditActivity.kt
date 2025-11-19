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
import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*

class ProfileEditActivity : AppCompatActivity() {

    // Lista de Views que necesitamos manipular
    private lateinit var etNombre: EditText
    private lateinit var etApellidoPaterno: EditText
    private lateinit var etApellidoMaterno: EditText // NUEVO
    private lateinit var etCurp: EditText // NUEVO
    private lateinit var etNss: EditText // NUEVO
    private lateinit var etFechaNacimiento: EditText // NUEVO
    private lateinit var spinnerEstadoNacimiento: Spinner
    private lateinit var spinnerSexo: Spinner // NUEVO

    // Objeto para manejar la persistencia de datos simulada
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiPerfilData", Context.MODE_PRIVATE)

        setupToolbar()
        initializeViews()
        loadCurrentData()
        setupUpdateButton()
    }

    private fun initializeViews() {
        // Inicializar referencias de EditTexts
        etNombre = findViewById(R.id.et_nombre)
        etApellidoPaterno = findViewById(R.id.et_apellido_paterno)
        etApellidoMaterno = findViewById(R.id.et_apellido_materno)
        etCurp = findViewById(R.id.et_curp)
        etNss = findViewById(R.id.et_nss)
        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento)

        spinnerEstadoNacimiento = findViewById(R.id.spinner_estado_nacimiento)
        spinnerSexo = findViewById(R.id.spinner_sexo)

        // Configurar el Spinner de Estado de Nacimiento
        val estados = arrayOf("Michoacán", "Selecciona", "Morelos", "Ciudad de México", "Jalisco")
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
        spinnerEstadoNacimiento.adapter = adapterEstado

        // Configurar el Spinner de Sexo
        val opcionesSexo = arrayOf("Hombre", "Selecciona", "Mujer", "No Binario")
        val adapterSexo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesSexo)
        spinnerSexo.adapter = adapterSexo

        // Configurar el clic en Fecha de Nacimiento para abrir un selector de fecha
        etFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Información Personal"
        // Nota: Si usaste un drawableStart en el TextView del formulario, este lo tapa.
        // Asegúrate de que el título "Información Personal" en el layout sea un TextView simple, no la toolbar.
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun loadCurrentData() {
        // --- Carga de datos desde SharedPreferences ---

        // Carga de campos de texto
        etNombre.setText(sharedPreferences.getString("nombre_key", "Wilmer Ernesto"))
        etApellidoPaterno.setText(sharedPreferences.getString("apellido_paterno_key", "Lobato"))
        etApellidoMaterno.setText(sharedPreferences.getString("apellido_materno_key", "Alcantar"))
        etCurp.setText(sharedPreferences.getString("curp_key", "LOAW980101HXXLXX01"))
        etNss.setText(sharedPreferences.getString("nss_key", "01987654321"))
        etFechaNacimiento.setText(sharedPreferences.getString("fecha_nac_key", "01/01/1998"))

        // --- Carga de Spinners ---
        fun setSpinnerSelection(spinner: Spinner, key: String, defaultValue: String) {
            val guardado = sharedPreferences.getString(key, defaultValue)
            val adapter = spinner.adapter as ArrayAdapter<String>
            val index = adapter.getPosition(guardado)
            if (index >= 0) spinner.setSelection(index)
        }

        setSpinnerSelection(spinnerEstadoNacimiento, "estado_nacimiento_key", "Michoacán")
        setSpinnerSelection(spinnerSexo, "sexo_key", "Hombre")
    }

    private fun setupUpdateButton() {
        val btnActualizar: Button = findViewById(R.id.btn_actualizar)

        btnActualizar.setOnClickListener {
            // Recolección de datos
            val nuevoNombre = etNombre.text.toString()
            val nuevoCurp = etCurp.text.toString()
            val nuevoEstado = spinnerEstadoNacimiento.selectedItem.toString()
            val nuevoSexo = spinnerSexo.selectedItem.toString()

            // 1. Validación básica (ejemplo)
            if (nuevoNombre.isBlank() || nuevoCurp.length != 18) {
                Toast.makeText(this, "El nombre y el CURP (18 caracteres) son obligatorios.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 2. Guardar todos los nuevos datos en SharedPreferences
            with(sharedPreferences.edit()) {
                putString("nombre_key", nuevoNombre)
                putString("apellido_paterno_key", etApellidoPaterno.text.toString())
                putString("apellido_materno_key", etApellidoMaterno.text.toString())
                putString("curp_key", nuevoCurp)
                putString("nss_key", etNss.text.toString())
                putString("fecha_nac_key", etFechaNacimiento.text.toString())
                putString("estado_nacimiento_key", nuevoEstado)
                putString("sexo_key", nuevoSexo)
                apply()
            }

            Toast.makeText(this, "¡Información Personal actualizada con éxito!", Toast.LENGTH_LONG).show()
        }
    }

    // Función para mostrar el selector de fecha
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            etFechaNacimiento.setText(sdf.format(calendar.time))
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}