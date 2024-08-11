package com.example.cerverica.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cerverica.models.RecetaModel
import com.example.cerverica.R

class RecetaAdapter(private val recetas: List<RecetaModel>) : RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder>() {

    class RecetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreReceta: TextView = itemView.findViewById(R.id.nombreReceta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_receta, parent, false)
        return RecetaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val currentReceta = recetas[position]
        holder.nombreReceta.text = currentReceta.nombre
    }

    override fun getItemCount() = recetas.size
}