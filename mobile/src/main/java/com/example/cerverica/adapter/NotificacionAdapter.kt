package com.example.cerverica.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cerverica.R
import com.example.cerverica.databinding.ViewholderNotificacionBinding
import com.example.cerverica.models.NotificacionModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class NotificacionAdapter(private var notificaciones: List<NotificacionModel>, private val onClickListener:(NotificacionModel) -> Unit) : RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>() {

    private var filteredList: MutableList<NotificacionModel> = notificaciones.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_notificacion, parent, false)
        return NotificacionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        holder.render(filteredList[position], onClickListener)

    }

    fun updateNotificaciones(nuevasNotificaciones: List<NotificacionModel>) {
        this.notificaciones = nuevasNotificaciones
        filter("")
    }

    override fun getItemCount() :Int {
        return filteredList.size
    }

    fun filter(query: String) {
        this.filteredList.clear()
        if (query.isEmpty()) {
            this.filteredList = this.notificaciones.toMutableList()
        } else {
            this.filteredList = this.notificaciones.filter { noti ->

                noti.mensaje.lowercase().contains(query.toString().toLowerCase())
                    || formatFecha(noti.fecha).lowercase(Locale.getDefault()).contains(query.toString().toLowerCase())
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun ordenarAscendente(ascendente: Boolean) {
        filteredList.sortWith { noti1, noti2 ->
            val date1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).parse(noti1.fecha)
            val date2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).parse(noti2.fecha)

            if (ascendente) {
                date1?.compareTo(date2) ?: 0
            } else {
                date2?.compareTo(date1) ?: 0
            }
        }
        notifyDataSetChanged()
    }

    class NotificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding =  ViewholderNotificacionBinding.bind(itemView)

        fun render(notificacion: NotificacionModel, onClickListener:(NotificacionModel) -> Unit){

            binding.mensaje.text = notificacion.mensaje
            if(notificacion.tipo==1){
                binding.iconoTipo.setImageResource(R.drawable.cuenta)
            }else if(notificacion.tipo==2){
                binding.iconoTipo.setImageResource(R.drawable.favorito)
            }else if(notificacion.tipo==3){
                binding.iconoTipo.setImageResource(R.drawable.ingredients)
            }else if(notificacion.tipo==4){
                binding.iconoTipo.setImageResource(R.drawable.lotes)
            }else if(notificacion.tipo==5){
                binding.iconoTipo.setImageResource(R.drawable.cook)
            }else if(notificacion.tipo==6){
                binding.iconoTipo.setImageResource(R.drawable.proveedor)
            }else if(notificacion.tipo==7){
                binding.iconoTipo.setImageResource(R.drawable.recipe)
            }else if(notificacion.tipo==8){
                binding.iconoTipo.setImageResource(R.drawable.happy)
            }else{
                binding.iconoTipo.setImageResource(R.drawable.beer)
            }
            Log.d("NOTI_PROCESS", "..")
            binding.fecha.text = formatFecha(notificacion.fecha)
            Log.d("NOTI_PROCESS", "${formatFecha(notificacion.fecha)}")

            binding.botonVisto.setOnClickListener {
                onClickListener(notificacion)
            }
        }

        private fun formatFecha(fecha: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date: Date? = inputFormat.parse(fecha)

                date?.let {
                    val now = Date()
                    val diffInMillis = now.time - it.time
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                    return when {
                        minutes < 60 -> "Hace ${minutes} minutos"
                        hours < 24 -> "Hace ${hours} horas"
                        days == 1L -> "Ayer a las " + SimpleDateFormat("h:mm a", Locale.getDefault()).format(it)
                        days < 7 -> SimpleDateFormat("EEEE 'a las' h:mm a", Locale.getDefault()).format(it)
                        days < 365 && SimpleDateFormat("yyyy", Locale.getDefault()).format(it) == SimpleDateFormat("yyyy", Locale.getDefault()).format(now) -> {
                            SimpleDateFormat("d MMMM 'a las' h:mm a", Locale.getDefault()).format(it)
                        }
                        else -> SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(it)
                    }
                }

                fecha // En caso de que la fecha no pueda ser parseada
            } catch (e: Exception) {
                fecha // En caso de excepción, devolver la fecha original
            }
        }
    }

    private fun formatFecha(fecha: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date: Date? = inputFormat.parse(fecha)

            date?.let {
                val now = Date()
                val diffInMillis = now.time - it.time
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                return when {
                    minutes < 60 -> "Hace ${minutes} minutos"
                    hours < 24 -> "Hace ${hours} horas"
                    days == 1L -> "Ayer a las " + SimpleDateFormat("h:mm a", Locale.getDefault()).format(it)
                    days < 7 -> SimpleDateFormat("EEEE 'a las' h:mm a", Locale.getDefault()).format(it)
                    days < 365 && SimpleDateFormat("yyyy", Locale.getDefault()).format(it) == SimpleDateFormat("yyyy", Locale.getDefault()).format(now) -> {
                        SimpleDateFormat("d MMMM 'a las' h:mm a", Locale.getDefault()).format(it)
                    }
                    else -> SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(it)
                }
            }

            fecha // En caso de que la fecha no pueda ser parseada
        } catch (e: Exception) {
            fecha // En caso de excepción, devolver la fecha original
        }
    }
}