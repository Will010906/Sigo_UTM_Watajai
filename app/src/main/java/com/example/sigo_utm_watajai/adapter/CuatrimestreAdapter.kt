package com.example.sigo_utm_watajai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigo_utm_watajai.R
import com.example.sigo_utm_watajai.data.db.entity.Cuatrimestre

/**
 * Adaptador (Adapter) para mostrar la lista de cuatrimestres en un RecyclerView.
 * * Este componente es esencial en la arquitectura de Android para tomar datos de una fuente
 * (como LiveData/Room) y representarlos visualmente en un formato de lista desplazable.
 * * @property clickListener Función lambda que se ejecuta al hacer clic en un elemento de la lista.
 */
class CuatrimestreAdapter(
    // Función de callback que recibe un objeto Cuatrimestre. Permite manejar el clic en la Activity.
    private val clickListener: (Cuatrimestre) -> Unit
) : RecyclerView.Adapter<CuatrimestreAdapter.CuatrimestreViewHolder>() {

    // Lista interna de datos que el adaptador maneja. Inicialmente está vacía.
    private var cuatrimestres: List<Cuatrimestre> = emptyList()

    /**
     * ViewHolder: Contiene las referencias a las vistas (TextViews, etc.)
     * de un solo elemento de la lista (`item_cuatrimestre.xml`).
     */
    class CuatrimestreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Inicialización de las vistas del layout item_cuatrimestre.xml
        val nombre: TextView = itemView.findViewById(R.id.tv_nombre_cuatrimestre)
        val periodo: TextView = itemView.findViewById(R.id.tv_periodo)
        val carrera: TextView = itemView.findViewById(R.id.tv_carrera)
        val grupo: TextView = itemView.findViewById(R.id.tv_grupo)
        val tutor: TextView = itemView.findViewById(R.id.tv_tutor)
        val desempeno: TextView = itemView.findViewById(R.id.tv_desempeno)

        /**
         * Asocia (bind) los datos del objeto Cuatrimestre a las vistas.
         * * @param cuatrimestre El objeto de datos Cuatrimestre a mostrar.
         * @param clickListener La función a ejecutar cuando se hace clic.
         */
        fun bind(cuatrimestre: Cuatrimestre, clickListener: (Cuatrimestre) -> Unit) {
            // Asignación de textos
            nombre.text = cuatrimestre.nombre
            periodo.text = cuatrimestre.periodo
            carrera.text = cuatrimestre.carrera
            grupo.text = cuatrimestre.grupo
            tutor.text = cuatrimestre.tutor
            desempeno.text = cuatrimestre.desempeno

            // Manejador de eventos de clic en todo el elemento (itemView)
            itemView.setOnClickListener { clickListener(cuatrimestre) }
        }
    }

    /**
     * Llamado por el RecyclerView para crear nuevos ViewHolders.
     * Infla el archivo de layout `item_cuatrimestre.xml`.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuatrimestreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cuatrimestre, parent, false)
        return CuatrimestreViewHolder(view)
    }

    /**
     * Llamado por el RecyclerView para mostrar los datos en una posición específica.
     * Esto reutiliza un ViewHolder existente y llama a `bind` para actualizar su contenido.
     */
    override fun onBindViewHolder(holder: CuatrimestreViewHolder, position: Int) {
        holder.bind(cuatrimestres[position], clickListener)
    }

    /**
     * Devuelve el número total de elementos en el conjunto de datos que tiene el adaptador.
     */
    override fun getItemCount() = cuatrimestres.size

    /**
     * Método para actualizar la lista de cuatrimestres.
     * Utilizado para alimentar al adaptador con nuevos datos (ej. desde un LiveData).
     * * @param newList La nueva lista de objetos Cuatrimestre.
     */
    fun updateList(newList: List<Cuatrimestre>) {
        this.cuatrimestres = newList
        // Notifica al RecyclerView que los datos han cambiado y debe redibujar la lista.
        notifyDataSetChanged()
    }
}