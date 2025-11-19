package com.example.sigo_utm_watajai

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater

class SubjectDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_detail)

        // 1. Obtener el nombre del cuatrimestre enviado desde AcademicHistoryActivity
        val cuatrimestreNombre = intent.getStringExtra("CUATRIMESTRE_NOMBRE") ?: "Detalle"

        // 2. Configurar la barra de herramientas y el título
        setupToolbar(cuatrimestreNombre)

        // 3. Cargar y mostrar la lista de asignaturas para ese cuatrimestre
        loadSubjects(cuatrimestreNombre)
    }

    private fun setupToolbar(cuatrimestre: String) {
        val toolbar = findViewById<android.view.View>(R.id.toolbar)
        // Usamos el nombre del cuatrimestre para el título
        toolbar.findViewById<TextView>(R.id.tv_title).text = cuatrimestre

        // El título "Asignaturas" también puede ser útil
        findViewById<TextView>(R.id.tv_cuatrimestre_title).text = "Asignaturas de $cuatrimestre"

        toolbar.findViewById<android.widget.ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun loadSubjects(cuatrimestre: String) {
        // En un proyecto real, aquí harías una llamada a la base de datos o API.
        // Por ahora, usamos datos de prueba.
        val container = findViewById<LinearLayout>(R.id.container_subjects)
        val subjectsList = getSampleSubjects(cuatrimestre)

        val inflater = LayoutInflater.from(this)

        subjectsList.forEach { subject ->
            // 4. Inflar el layout de la materia (item_subject_detail.xml)
            val subjectView = inflater.inflate(R.layout.item_subject_detail, container, false)

            // 5. Llenar los datos de la materia
            subjectView.findViewById<TextView>(R.id.tv_subject_name_combined).text = subject.name
            subjectView.findViewById<TextView>(R.id.tv_teacher_name).text = subject.teacher
            subjectView.findViewById<TextView>(R.id.tv_progress).text = subject.progress
            subjectView.findViewById<TextView>(R.id.tv_evaluation).text = subject.evaluation
            subjectView.findViewById<TextView>(R.id.tv_desempeno).text = subject.desempeno
            subjectView.findViewById<TextView>(R.id.tv_units_list).text = subject.units.joinToString("\n")

            // 6. Añadir la vista al contenedor
            container.addView(subjectView)

            // TODO: Añadir lógica para expandir/contraer las unidades al tocar la flecha.
        }
    }

    // Función de datos de prueba (Debería estar en una capa de datos separada en un proyecto real)
    private fun getSampleSubjects(cuatrimestre: String): List<Subject> {
        // En un proyecto real, esta lógica filtraría las materias por cuatrimestre
        return listOf(
            Subject("Inglés", "Lic. María Veronica Alvarez Ríos", "100%", "Ordinaria", "E",
                listOf("Presentación Personal (Estratégico)", "Actividades Diarias y de Rutina (Estratégico)")),
            Subject("Desarrollo Humano y Valores", "Lic. Laura Perez Pazos", "95%", "Ordinaria", "B",
                listOf("Liderazgo (Avanzado)", "Ética Profesional (Avanzado)")),
            Subject("Física Básica", "Dr. Juan Sánchez", "80%", "Ordinaria", "I",
                listOf("Mecánica Clásica (Regular)", "Termodinámica (Insuficiente)"))
        )
    }
}

// Modelo de datos para las asignaturas (puedes crear un archivo Subject.kt en el paquete model)
data class Subject(
    val name: String,
    val teacher: String,
    val progress: String,
    val evaluation: String,
    val desempeno: String,
    val units: List<String>
)