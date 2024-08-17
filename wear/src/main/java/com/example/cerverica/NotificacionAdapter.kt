package com.example.cerverica

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class NotificacionAdapter(
    private var notificaciones: List<NotificacionModel>,
    private val onClickListener: (NotificacionModel) -> Unit
) : RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>() {

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

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String) {
        this.filteredList.clear()
        if (query.isEmpty()) {
            this.filteredList = this.notificaciones.toMutableList()
        } else {
            this.filteredList = this.notificaciones.filter { noti ->
                noti.mensaje.lowercase().contains(query.lowercase())
                        || formatFecha(noti.fecha).lowercase().contains(query.lowercase())
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun ordenarAscendente(ascendente: Boolean) {
        filteredList.sortWith { noti1, noti2 ->
            val date1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(noti1.fecha)
            val date2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(noti2.fecha)

            if (ascendente) {
                date1?.compareTo(date2) ?: 0
            } else {
                date2?.compareTo(date1) ?: 0
            }
        }
        notifyDataSetChanged()
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

                when {
                    minutes < 60 -> "Hace ${minutes} minutos"
                    hours < 24 -> "Hace ${hours} horas"
                    days == 1L -> "Ayer a las " + SimpleDateFormat("h:mm a", Locale.getDefault()).format(it)
                    days < 7 -> SimpleDateFormat("EEEE 'a las' h:mm a", Locale.getDefault()).format(it)
                    days < 365 && SimpleDateFormat("yyyy", Locale.getDefault()).format(it) == SimpleDateFormat("yyyy", Locale.getDefault()).format(now) -> {
                        SimpleDateFormat("d MMMM 'a las' h:mm a", Locale.getDefault()).format(it)
                    }
                    else -> SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(it)
                }
            } ?: fecha // En caso de que la fecha no pueda ser parseada
        } catch (e: Exception) {
            fecha // En caso de excepción, devolver la fecha original
        }
    }

    class NotificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mensajeTextView: TextView = itemView.findViewById(R.id.mensaje)
        private val iconoTipoImageView: ImageView = itemView.findViewById(R.id.icono_tipo)
        private val fechaTextView: TextView = itemView.findViewById(R.id.fecha)
        private val botonVisto: ImageView = itemView.findViewById(R.id.boton_visto)

        fun render(notificacion: NotificacionModel, onClickListener: (NotificacionModel) -> Unit) {
            mensajeTextView.text = notificacion.mensaje
            when (notificacion.tipo) {
                1 -> iconoTipoImageView.setImageResource(R.drawable.cuenta)
                2 -> iconoTipoImageView.setImageResource(R.drawable.favorito)
                3 -> iconoTipoImageView.setImageResource(R.drawable.ingredients)
                4 -> iconoTipoImageView.setImageResource(R.drawable.lotes)
                5 -> iconoTipoImageView.setImageResource(R.drawable.cook)
                6 -> iconoTipoImageView.setImageResource(R.drawable.proveedor)
                7 -> iconoTipoImageView.setImageResource(R.drawable.recipe)
                8 -> iconoTipoImageView.setImageResource(R.drawable.happy)
                else -> iconoTipoImageView.setImageResource(R.drawable.beer)
            }
            iconoTipoImageView.setColorFilter(ContextCompat.getColor(itemView.context, R.color.mandarin))
            fechaTextView.text = formatFecha(notificacion.fecha)
            botonVisto.setImageResource(R.drawable.fire)
            botonVisto.setColorFilter(ContextCompat.getColor(itemView.context, R.color.black))
            botonVisto.setOnClickListener {
                onClickListener(notificacion)
                botonVisto.setColorFilter(ContextCompat.getColor(itemView.context, R.color.red))
            }
        }

        private fun formatFecha(fecha: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

                val date: Date? = inputFormat.parse(fecha)

                date?.let {
                    val now = Date()
                    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    outputFormat.timeZone = TimeZone.getDefault() // Cambia a la zona horaria local del dispositivo

                    val diffInMillis = now.time - it.time
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                    when {
                        minutes < 60 -> "Hace ${minutes} minutos"
                        hours < 24 -> "Hace ${hours} horas"
                        days == 1L -> "Ayer a las " + SimpleDateFormat("h:mm a", Locale.getDefault()).format(it)
                        days < 7 -> SimpleDateFormat("EEEE 'a las' h:mm a", Locale.getDefault()).format(it)
                        days < 365 && SimpleDateFormat("yyyy", Locale.getDefault()).format(it) == SimpleDateFormat("yyyy", Locale.getDefault()).format(now) -> {
                            SimpleDateFormat("d MMMM 'a las' h:mm a", Locale.getDefault()).format(it)
                        }
                        else -> SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(it)
                    }
                } ?: fecha // En caso de que la fecha no pueda ser parseada
            } catch (e: Exception) {
                fecha // En caso de excepción, devolver la fecha original
            }
        }

    }
}
