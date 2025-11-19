package com.example.sigo_utm_watajai

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sigo_utm_watajai.adapter.CuatrimestreAdapter
import com.example.sigo_utm_watajai.data.Cuatrimestre

class AcademicHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic_history)

        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        // El título principal ya está en el layout, solo configuramos el botón Atrás
        toolbar.findViewById<TextView>(R.id.tv_title).text = "Historial Académico"

        toolbar.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_cuatrimestres)

        // Datos de prueba basados en tu boceto
        val listaCuatrimestres = listOf(
            Cuatrimestre(
                "1er cuatrimestre",
                "Sep - Dic 2024",
                "Tecnologías de la información",
                "1 B Matutino",
                "MATI Gerardo Chávez",
                "A (Autónomo)"
            ),
            Cuatrimestre(
                "2er cuatrimestre",
                "Ene - Abr 2025",
                "Tecnologías de la información",
                "2 B",
                "Dra. Olga Leticia Robles",
                "E (Estratégico)"
            )
            // Añadir más cuatrimestres aquí...
        )

        // Definimos la acción al hacer clic
        val clickListener: (Cuatrimestre) -> Unit = { cuatrimestre ->
            navigateToSubjectDetail(cuatrimestre.nombre)
        }

        val adapter = CuatrimestreAdapter(listaCuatrimestres, clickListener)
        recyclerView.adapter = adapter
        // Ya definimos el LayoutManager en el XML, pero si no lo estuviera, se haría aquí:
        // recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Función para navegar a la siguiente Activity: Detalle de Asignaturas
    private fun navigateToSubjectDetail(cuatrimestreNombre: String) {
        val intent = Intent(this, SubjectDetailActivity::class.java)
        // Pasamos el nombre del cuatrimestre para saber qué materias mostrar
        intent.putExtra("CUATRIMESTRE_NOMBRE", cuatrimestreNombre)
        startActivity(intent)
    }
}