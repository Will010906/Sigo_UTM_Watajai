package com.example.sigo_utm_watajai
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.sigo_utm_watajai.AcademicHistoryActivity
import com.example.sigo_utm_watajai.ProfileActivity // 👈 Importación requerida para que el Intent funcione.
// Importa View para la toolbar si usas la separación visual o View


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Obtener referencia a la CardView de "Mi Perfil"
        val perfilCard: CardView = findViewById(R.id.card_perfil)

        // 2. Obtener referencia a la CardView de "Historial académico"
        val historialCard: CardView = findViewById(R.id.card_historial)

        // 3. Conectar el click para la tarjeta de Perfil
        perfilCard.setOnClickListener {
            navigateToProfile()
        }

        // 4. Conectar el click para la tarjeta de Historial Académico
        historialCard.setOnClickListener {
            navigateToAcademicHistory()
        }
    }

    // --- Funciones de Navegación ---

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAcademicHistory() {
        val intent = Intent(this, AcademicHistoryActivity::class.java)
        startActivity(intent)
    }
}