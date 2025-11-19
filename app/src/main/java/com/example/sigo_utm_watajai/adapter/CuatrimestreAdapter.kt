package com.example.sigo_utm_watajai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigo_utm_watajai.R
import com.example.sigo_utm_watajai.data.Cuatrimestre

class CuatrimestreAdapter(
    private val cuatrimestres: List<Cuatrimestre>,
    private val clickListener: (Cuatrimestre) -> Unit // Función para manejar el clic
) : RecyclerView.Adapter<CuatrimestreAdapter.CuatrimestreViewHolder>() {

    // Define el ViewHolder (encuentra las vistas dentro del item_cuatrimestre.xml)
    class CuatrimestreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tv_nombre_cuatrimestre)
        val periodo: TextView = itemView.findViewById(R.id.tv_periodo)
        val carrera: TextView = itemView.findViewById(R.id.tv_carrera)
        val grupo: TextView = itemView.findViewById(R.id.tv_grupo)
        val tutor: TextView = itemView.findViewById(R.id.tv_tutor)
        val desempeno: TextView = itemView.findViewById(R.id.tv_desempeno)

        fun bind(cuatrimestre: Cuatrimestre, clickListener: (Cuatrimestre) -> Unit) {
            nombre.text = cuatrimestre.nombre
            periodo.text = cuatrimestre.periodo
            carrera.text = cuatrimestre.carrera
            grupo.text = cuatrimestre.grupo
            tutor.text = cuatrimestre.tutor
            desempeno.text = cuatrimestre.desempeno

            // Define el manejo del clic para ir a la siguiente pantalla
            itemView.setOnClickListener { clickListener(cuatrimestre) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuatrimestreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cuatrimestre, parent, false)
        return CuatrimestreViewHolder(view)
    }

    override fun onBindViewHolder(holder: CuatrimestreViewHolder, position: Int) {
        holder.bind(cuatrimestres[position], clickListener)
    }

    override fun getItemCount() = cuatrimestres.size
}