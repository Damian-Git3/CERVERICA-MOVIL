package com.example.cerverica.adapter.cliente

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cerverica.R
import com.example.cerverica.databinding.ViewholderFavoritoBinding
import com.example.cerverica.models.cliente.RecetaFavoritoModel
import com.example.cerverica.models.cliente.RecetaPackModel

class FavoritoAdapter(private var recetas: List<RecetaFavoritoModel>, private val onClickListener:(RecetaFavoritoModel) -> Unit) : RecyclerView.Adapter<FavoritoAdapter.RecetaFavoritoViewHolder>() {

    private var filteredList: MutableList<RecetaFavoritoModel> = recetas.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaFavoritoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_favorito, parent, false)
        return RecetaFavoritoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecetaFavoritoViewHolder, position: Int) {
        holder.render(filteredList[position], onClickListener)

    }

    fun updateFavoritos(nuevosFavoritos: List<RecetaFavoritoModel>) {
        this.recetas = nuevosFavoritos
        filter("")  // Reaplicar el filtro para actualizar la vista
    }

    override fun getItemCount() :Int {
        return filteredList.size
    }

    fun filter(query: String) {
        this.filteredList.clear()
        if (query.isEmpty()) {
            this.filteredList = this.recetas.toMutableList()
        } else {
            this.filteredList = this.recetas.filter { favorito -> favorito.nombreReceta.lowercase().contains(query.toString().toLowerCase()) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    class RecetaFavoritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding =  ViewholderFavoritoBinding.bind(itemView)

        fun render(receta: RecetaFavoritoModel, onClickListener:(RecetaFavoritoModel) -> Unit){
            binding.nombreRecetaPack.text = receta.nombreReceta

            binding.especificacionesRecetaPack.text = receta.especificacionesReceta

            Glide.with(binding.nombreRecetaPack.context).load(receta.imagenReceta).into(binding.imagen)
            Glide.with(binding.nombreRecetaPack.context).load(receta.imagenFondoReceta).into(binding.imagenFondo)

            binding.quitarFavorito.setOnClickListener {
                onClickListener(receta)
            }
        }
    }
}