package com.example.cerverica.adapter.cliente

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cerverica.R
import com.example.cerverica.databinding.ViewholderVentaBinding
import com.example.cerverica.models.cliente.VentaModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistorialComprasAdapter(private var historialCompras: List<VentaModel>): RecyclerView.Adapter<HistorialComprasAdapter.HistorialCompraViewHolder>() {

    private var filteredList: MutableList<VentaModel> = historialCompras.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialCompraViewHolder {
        val binding = ViewholderVentaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistorialCompraViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: HistorialCompraViewHolder, position: Int) {
        Log.d("compras", "prueba onBindViewHolder historial: ")
        holder.render(filteredList[position])
    }

    fun filter(query: String) {
        this.filteredList.clear()
        if (query.isEmpty()) {
            this.filteredList = this.historialCompras.toMutableList()
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            this.filteredList = this.historialCompras.filter { compra ->
                val estatusVenta = when (compra.estatusVenta) {
                    3 -> "Listo"
                    2 -> "Empaquetado"
                    else -> "Recibido"
                }.lowercase(Locale.getDefault())

                val metodoEnvio = when (compra.metodoEnvio) {
                    1 -> "Recoger en Tienda"
                    else -> "Envio a Domicilio"
                }.lowercase(Locale.getDefault())

                val metodoPago = when (compra.metodoPago) {
                    1 -> "Contra Entrega"
                    else -> "Tarjeta Crédito"
                }.lowercase(Locale.getDefault())

                val fechaVenta = formatFecha(compra.fechaVenta).lowercase(Locale.getDefault())

                estatusVenta.contains(lowerCaseQuery) ||
                        metodoEnvio.contains(lowerCaseQuery) ||
                        metodoPago.contains(lowerCaseQuery) ||
                        fechaVenta.contains(lowerCaseQuery) ||
                        compra.total.toString().contains(lowerCaseQuery) ||
                        compra.id.toString().contains(lowerCaseQuery)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun ordenarAscendente(ascendente: Boolean) {
        filteredList.sortWith { compra1, compra2 ->
            val date1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).parse(compra1.fechaVenta)
            val date2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).parse(compra2.fechaVenta)

            if (ascendente) {
                date1?.compareTo(date2) ?: 0
            } else {
                date2?.compareTo(date1) ?: 0
            }
        }
        notifyDataSetChanged()
    }

    class HistorialCompraViewHolder(private val binding: ViewholderVentaBinding) : RecyclerView.ViewHolder(binding.root){
        fun render(compra: VentaModel){
            binding.apply {
                codigoVenta.text = compra.id.toString()
                if(compra.estatusVenta==3){
                    iconoEstatus.setImageResource(R.drawable.listo)
                    estatusVenta.text = "Listo"
                }else if(compra.estatusVenta==2){
                    iconoEstatus.setImageResource(R.drawable.box)
                    estatusVenta.text = "Empaquetado"
                }else{
                    iconoEstatus.setImageResource(R.drawable.beer)
                    estatusVenta.text = "Recibido"
                }
                codigoVenta.text = compra.estatusVenta.toString()
                fechaVenta.text = formatFecha(compra.fechaVenta)
                totalVenta.text = formatPrice(compra.total)

                if(compra.metodoEnvio==1){
                    metodoEnvio.text = "Recoger en Tienda"
                }else{
                    metodoEnvio.text = "Envio a Domicilio"
                }

                if(compra.metodoPago==1){
                    metodoPago.text = "Contra Entrega"
                }else{
                    metodoPago.text = "Tarjeta Crédito"
                }
            }
        }

        private fun formatPrice(price: Float): String {
            return String.format("$ %.2f", price)
        }

        private fun formatFecha(fecha: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val date: Date? = inputFormat.parse(fecha)

            date?.let {
                val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
                val dateYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(it)

                val monthDayFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
                val monthDay = monthDayFormat.format(it)

                // Capitalizar la primera letra del mes
                val capitalizedMonthDay = monthDay.replaceFirstChar { it.uppercaseChar() }

                return if (dateYear == currentYear) {
                    capitalizedMonthDay // Mostrar solo el mes y día
                } else {
                    "$capitalizedMonthDay $dateYear" // Mostrar mes, día y año
                }
            }
            return "Fecha inválida"
        }
    }

    private fun formatFecha(fecha: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val date: Date? = inputFormat.parse(fecha)

        date?.let {
            val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
            val dateYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(it)

            val monthDayFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
            val monthDay = monthDayFormat.format(it)

            // Capitalizar la primera letra del mes
            val capitalizedMonthDay = monthDay.replaceFirstChar { it.uppercaseChar() }

            return if (dateYear == currentYear) {
                capitalizedMonthDay // Mostrar solo el mes y día
            } else {
                "$capitalizedMonthDay $dateYear" // Mostrar mes, día y año
            }
        }
        return "Fecha inválida"
    }
}

