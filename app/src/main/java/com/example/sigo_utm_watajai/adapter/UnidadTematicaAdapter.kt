package com.example.sigo_utm_watajai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigo_utm_watajai.R
import com.example.sigo_utm_watajai.data.db.entity.UnidadTematica

/**
 * Adaptador (Adapter) para el RecyclerView que muestra una lista de objetos `UnidadTematica`.
 * Es responsable de mapear los datos del modelo (`UnidadTematica`) a los elementos de la interfaz (`item_unidad_tematica.xml`).
 */
class UnidadTematicaAdapter :
    RecyclerView.Adapter<UnidadTematicaAdapter.UnidadTematicaViewHolder>() {

    // Lista interna de datos que el adaptador maneja. Es inmutable para la vista externa,
    // y se actualiza mediante el método `updateList()`.
    private var unidades: List<UnidadTematica> = emptyList()

    /**
     * ViewHolder: Contiene y gestiona las referencias a las vistas de un solo elemento de la lista.
     */
    class UnidadTematicaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Referencias a los TextViews definidos en item_unidad_tematica.xml
        val nombreUnidad: TextView = itemView.findViewById(R.id.tv_unidad_nombre)
        val desempenoUnidad: TextView = itemView.findViewById(R.id.tv_unidad_desempeno)

        /**
         * Asocia (bind) los datos de la unidad temática a las vistas del ViewHolder.
         * @param unidad El objeto de datos `UnidadTematica` a mostrar.
         */
        fun bind(unidad: UnidadTematica) {
            nombreUnidad.text = unidad.nombre
            desempenoUnidad.text = unidad.desempenoUnidad
            // Nota: Aquí se podría añadir lógica para cambiar el color del desempeño según el valor (Estratégico, Autónomo, etc.)
        }
    }

    /**
     * Llamado por el RecyclerView para crear nuevos ViewHolders.
     * Infla el archivo de layout de cada elemento de la lista.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnidadTematicaViewHolder {
        // Infla el layout específico para una unidad temática
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_unidad_tematica, parent, false)
        return UnidadTematicaViewHolder(view)
    }

    /**
     * Llamado por el RecyclerView para mostrar los datos en una posición específica.
     * Reutiliza un ViewHolder existente y llama a `bind` para actualizar su contenido.
     */
    override fun onBindViewHolder(holder: UnidadTematicaViewHolder, position: Int) {
        holder.bind(unidades[position])
    }

    /**
     * Devuelve el número total de unidades temáticas en la lista.
     */
    override fun getItemCount() = unidades.size

    /**
     * Método crucial para actualizar los datos desde el LiveData del ViewModel.
     * Permite que la Activity/Fragment envíe una nueva lista al adaptador.
     * @param newList La nueva lista de objetos `UnidadTematica`.
     */
    fun updateList(newList: List<UnidadTematica>) {
        this.unidades = newList
        // Notifica al RecyclerView que los datos han cambiado y debe redibujar la lista.
        // Nota: Para grandes listas, se recomienda usar DiffUtil para optimizar el rendimiento.
        notifyDataSetChanged()
    }
}