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
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import androidx.activity.viewModels
import com.example.sigo_utm_watajai.data.AppDatabase
import com.example.sigo_utm_watajai.data.db.entity.Perfil
import com.example.sigo_utm_watajai.data.repository.PerfilRepository
import com.example.sigo_utm_watajai.viewmodel.ProfileViewModel
import com.example.sigo_utm_watajai.viewmodel.ProfileViewModelFactory


/**
 * Activity para editar y guardar la información personal básica del estudiante:
 * Nombre, Apellidos, Fecha y Estado de Nacimiento, Sexo, CURP y NSS.
 * Utiliza Room a través del ProfileViewModel para la persistencia de datos.
 */
class ProfileEditActivity : AppCompatActivity() {

    // ===========================================================================
    // CONEXIÓN A ROOM (MVVM)
    // ===========================================================================
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repository by lazy { PerfilRepository(database.perfilDao()) }
    private val viewModelFactory by lazy { ProfileViewModelFactory(repository) }

    // Delegado para obtener la instancia del ViewModel.
    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    // Lista de Views (TextInputEditText es preferido para campos en Material Design)
    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellidoPaterno: TextInputEditText
    private lateinit var etApellidoMaterno: TextInputEditText
    private lateinit var etCurp: TextInputEditText
    private lateinit var etNss: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var spinnerEstadoNacimiento: Spinner
    private lateinit var spinnerSexo: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        setupToolbar()
        initializeViews()
        setupUpdateButton() // Configuración del Listener para guardar

        // ⭐ CONEXIÓN A ROOM: Observar los datos del perfil
        // El LiveData del ViewModel emitirá la última versión del perfil.
        viewModel.perfil.observe(this) { perfil: Perfil? ->

            if (perfil != null) {
                // Si existe el perfil, cargamos los datos y configuramos los spinners.
                loadCurrentDataFromRoom(perfil)
            } else {
                // Si el perfil es nulo (caso inicial o error), solo configuramos los spinners.
                setupSpinners()
                Toast.makeText(this, "Advertencia: Perfil no cargado, usando datos iniciales.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Inicializa las referencias a los campos de la interfaz de usuario.
     */
    private fun initializeViews() {
        etNombre = findViewById(R.id.et_nombre)
        etApellidoPaterno = findViewById(R.id.et_apellido_paterno)
        etApellidoMaterno = findViewById(R.id.et_apellido_materno)
        etCurp = findViewById(R.id.et_curp)
        etNss = findViewById(R.id.et_nss)
        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento)

        spinnerEstadoNacimiento = findViewById(R.id.spinner_estado_nacimiento)
        spinnerSexo = findViewById(R.id.spinner_sexo)

        // Configurar el clic en Fecha de Nacimiento para abrir un selector de fecha
        etFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }
    }

    /**
     * Configura los adaptadores para los Spinners de Estado de Nacimiento y Sexo.
     */
    private fun setupSpinners() {
        // Configurar el Spinner de Estado de Nacimiento
        val estados = arrayOf("Michoacán", "Selecciona", "Morelos", "Ciudad de México", "Jalisco")
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
        spinnerEstadoNacimiento.adapter = adapterEstado

        // Configurar el Spinner de Sexo
        val opcionesSexo = arrayOf("Hombre", "Selecciona", "Mujer", "No Binario")
        val adapterSexo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesSexo)
        spinnerSexo.adapter = adapterSexo
    }

    /**
     * Configura la barra de herramientas.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Información Personal"
        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    /**
     * ⭐ FUNCIÓN CLAVE: Carga los datos desde el objeto Perfil (Room) a la UI.
     * @param perfil El objeto Perfil que contiene los datos actuales.
     */
    private fun loadCurrentDataFromRoom(perfil: Perfil) {
        // 1. Configurar Spinners si aún no tienen adaptador
        if (spinnerEstadoNacimiento.adapter == null) {
            setupSpinners()
        }

        // 2. Cargar campos de texto
        etNombre.setText(perfil.nombre)
        etApellidoPaterno.setText(perfil.apellidoPaterno)
        etApellidoMaterno.setText(perfil.apellidoMaterno)
        etCurp.setText(perfil.curp)
        etNss.setText(perfil.nss)
        etFechaNacimiento.setText(perfil.fechaNacimiento)

        // 3. Seleccionar valores en los Spinners
        @Suppress("UNCHECKED_CAST")
        val adapterEstado = spinnerEstadoNacimiento.adapter as ArrayAdapter<String>
        spinnerEstadoNacimiento.setSelection(adapterEstado.getPosition(perfil.estadoNacimiento))

        @Suppress("UNCHECKED_CAST")
        val adapterSexo = spinnerSexo.adapter as ArrayAdapter<String>
        spinnerSexo.setSelection(adapterSexo.getPosition(perfil.sexo))
    }

    /**
     * Configura el Listener del botón de "Actualizar" para guardar los cambios en Room.
     */
    private fun setupUpdateButton() {
        val btnActualizar: Button = findViewById(R.id.btn_actualizar)

        btnActualizar.setOnClickListener {
            // 1. Obtener el perfil actual del LiveData
            val perfilExistente = viewModel.perfil.value

            if (perfilExistente == null) {
                Toast.makeText(this, "Error: No se pudo cargar el perfil para actualizar.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 2. CREACIÓN DEL PERFIL ACTUALIZADO
            // Usamos el método `copy()` de la data class para crear una nueva instancia
            // con los campos actualizados, manteniendo intactos los campos no editados
            // (como id, matricula, email, etc.).
            val perfilActualizado = perfilExistente.copy(
                nombre = etNombre.text.toString(),
                apellidoPaterno = etApellidoPaterno.text.toString(),
                apellidoMaterno = etApellidoMaterno.text.toString(),
                fechaNacimiento = etFechaNacimiento.text.toString(),
                estadoNacimiento = spinnerEstadoNacimiento.selectedItem.toString(),
                sexo = spinnerSexo.selectedItem.toString(),
                curp = etCurp.text.toString(),
                nss = etNss.text.toString()
            )

            // 3. Llama al ViewModel para guardar la nueva instancia en la base de datos
            viewModel.guardarPerfil(perfilActualizado)

            Toast.makeText(this, "¡Información Personal actualizada con éxito!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Muestra el diálogo de selección de fecha (DatePickerDialog).
     */
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Formato de fecha a mostrar en el campo de texto
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            etFechaNacimiento.setText(sdf.format(calendar.time))
        }

        // Crear y mostrar el diálogo
        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}