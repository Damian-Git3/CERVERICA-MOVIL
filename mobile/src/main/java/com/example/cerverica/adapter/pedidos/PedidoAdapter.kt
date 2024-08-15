package com.example.cerverica.adapter.pedidos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cerverica.R
import com.example.cerverica.databinding.ViewholderPedidoBinding
import com.example.cerverica.databinding.ViewholderRecetaBinding
import com.example.cerverica.models.Pedido
import com.example.cerverica.models.cliente.RecetaFavoritoModel

class PedidoAdapter(
    private var pedidos: List<Pedido>,
    private val onClickListener:(Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    private var listener: View.OnClickListener? = null

    fun filtrarLista(listaFiltrada: List<Pedido>) {
        pedidos = listaFiltrada
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_pedido, parent, false)
        view.setOnClickListener(listener)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = pedidos[position]

        val estatus = when (pedido.estatusVenta) {
            1 -> {
                holder.tvEstatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                "Recibido"
            }
            2 -> {
                holder.tvEstatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
                "Empaquetando"
            }
            3 -> {
                holder.tvEstatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                "Listo"
            }
            else -> {
                holder.tvEstatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.darkGrey))
                "Estatus desconocido"
            }
        }

        val metodoEnvio = when (pedido.metodoEnvio) {
            1 -> "Recoger en tienda"
            2 -> "Envio a domicilio"
            else -> "Método envio desconocido"
        }

        holder.tvEstatus.text = estatus
        holder.tvCantidadTotalBotellas.text = pedido.totalCervezas.toString()
        holder.tvFechaVenta.text = pedido.fechaVenta
        holder.tvMetodoEnvio.text = metodoEnvio

        holder.binding.root.setOnClickListener{
            onClickListener(pedidos[position])
        }
    }

    override fun getItemCount(): Int {
        return pedidos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding =  ViewholderPedidoBinding.bind(itemView)

        val tvEstatus: TextView = itemView.findViewById(R.id.tvEstatus)
        val tvCantidadTotalBotellas: TextView = itemView.findViewById(R.id.tvCantidadTotalBotellas)
        val tvFechaVenta: TextView = itemView.findViewById(R.id.tvFechaVenta)
        val tvMetodoEnvio: TextView = itemView.findViewById(R.id.tvMetodoEnvio)
    }
}
