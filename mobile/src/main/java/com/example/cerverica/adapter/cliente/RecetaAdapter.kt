package com.example.cerverica.adapter.cliente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cerverica.models.cliente.RecetaModel
import com.example.cerverica.R
import com.example.cerverica.databinding.ViewholderRecetaBinding
import com.example.cerverica.models.cliente.RecetaFavoritoModel

class RecetaAdapter(private val recetas: List<RecetaModel>, private val onClickListener:(RecetaModel) -> Unit) : RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_receta, parent, false)
        return RecetaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        holder.render(recetas[position])
        holder.binding.root.setOnClickListener{
            onClickListener(recetas[position])

            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        if(selectedPosition == position){
            holder.binding.nombreReceta.setBackgroundResource(R.drawable.orange_bg)
        }else
        {
            holder.binding.nombreReceta.setBackgroundResource(R.drawable.edittext_bg)
        }
    }

    override fun getItemCount() :Int {
        return recetas.size
    }

    class RecetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding =  ViewholderRecetaBinding.bind(itemView)

        fun render(receta: RecetaModel){
            binding.nombreReceta.text = receta.nombre

        }
    }
}