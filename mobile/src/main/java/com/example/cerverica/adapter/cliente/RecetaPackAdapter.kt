package com.example.cerverica.adapter.cliente

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cerverica.R
import com.example.cerverica.databinding.ViewholderPackBinding
import com.example.cerverica.models.cliente.RecetaPackModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecetaPackAdapter(private var recetaspacks: List<RecetaPackModel>) : RecyclerView.Adapter<RecetaPackAdapter.RecetaPackViewHolder>() {

    private var filteredList: MutableList<RecetaPackModel> = recetaspacks.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaPackViewHolder {
        val binding = ViewholderPackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecetaPackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecetaPackViewHolder, position: Int) {
        holder.render(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun insertList(packs: List<RecetaPackModel>){
        this.recetaspacks = packs
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        this.filteredList.clear()
        if (query.isEmpty()) {
            this.filteredList = this.recetaspacks.toMutableList()
        } else {
            this.filteredList = this.recetaspacks.filter { pack -> pack.nombreReceta.lowercase().contains(query.toString().toLowerCase()) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    class RecetaPackViewHolder(private val binding: ViewholderPackBinding) : RecyclerView.ViewHolder(binding.root) {

        fun render(packReceta: RecetaPackModel) {
            binding.apply {
                nombreRecetaPack.text = packReceta.nombreReceta
                precioRecetaPack.text = formatPrice(packReceta.precioPaquete1Receta)
                especificacionesRecetaPack.text = packReceta.especificacionesReceta

                Glide.with(nombreRecetaPack.context).load(packReceta.imagenReceta).into(imagen)
                Glide.with(precioRecetaPack.context).load(packReceta.imagenFondoReceta).into(imagenFondo)

                anuncioNuevo.visibility = if (isFechaReciente(packReceta.fechaRegistroReceta)) View.VISIBLE else View.GONE

                // Asignar la lógica de los botones de manera simplificada
                setPriceClickListener(precioRecetaPack1, packReceta.precioPaquete1Receta)
                setPriceClickListener(precioRecetaPack6, packReceta.precioPaquete6Receta)
                setPriceClickListener(precioRecetaPack12, packReceta.precioPaquete12Receta)
                setPriceClickListener(precioRecetaPack24, packReceta.precioPaquete24Receta)

            }
        }

        private fun setPriceClickListener(button: View, price: Float) {
            button.setOnClickListener {
                binding.apply {
                    precioRecetaPack.text = formatPrice(price)
                    resetButtonStyles()
                    button.setBackgroundResource(R.drawable.showorange_bg)
                    (button as? TextView)?.setTextColor(button.context.resources.getColor(R.color.white))
                }
            }
        }

        private fun resetButtonStyles() {
            binding.apply {
                precioRecetaPack1.setBackgroundResource(R.drawable.show_bg)
                precioRecetaPack6.setBackgroundResource(R.drawable.show_bg)
                precioRecetaPack12.setBackgroundResource(R.drawable.show_bg)
                precioRecetaPack24.setBackgroundResource(R.drawable.show_bg)

                // Restablecer el color del texto de todos los botones a negro
                precioRecetaPack1.setTextColor(precioRecetaPack1.context.resources.getColor(R.color.black, null))
                precioRecetaPack6.setTextColor(precioRecetaPack1.context.resources.getColor(R.color.black, null))
                precioRecetaPack12.setTextColor(precioRecetaPack1.context.resources.getColor(R.color.black, null))
                precioRecetaPack24.setTextColor(precioRecetaPack1.context.resources.getColor(R.color.black, null))
            }
        }

        private fun formatPrice(price: Float): String {
            return String.format("$ %.2f", price)
        }

        private fun isFechaReciente(fechaString: String): Boolean {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

            return try {
                val fecha = LocalDateTime.parse(fechaString, formatter)
                val hoy = LocalDateTime.now()

                // Definir el umbral de "reciente" (ej. 7 días)
                val fechaLimite = hoy.minusDays(7)

                // Verificar si la fecha está después de la fecha límite y antes o igual a hoy
                fecha.isAfter(fechaLimite) && !fecha.isAfter(hoy)
            } catch (e: Exception) {
                false
            }
        }

    }
}