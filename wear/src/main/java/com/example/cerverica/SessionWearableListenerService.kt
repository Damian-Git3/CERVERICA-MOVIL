package com.example.cerverica

import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class SessionWearableListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                if (dataItem.uri.path == "/login_data") {
                    val dataMap = dataItem.data?.let { DataMap.fromByteArray(it) }
                    val action = dataMap?.getString("action")

                    if (action == "change_activity") {
                        // Cambiar a la actividad de inicio
                        val idUsuario = dataMap.getString("idUsuario")
                        val nombre = dataMap.getString("nombre")
                        val role = dataMap.getString("role")

                        val intent = Intent(this, InicioActivity::class.java).apply {
                            putExtra("idUsuario", idUsuario)
                            putExtra("nombre", nombre)
                            putExtra("role", role)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun handleLogout() {
        // Implement logout logic
        Log.d("WEAR_PROCES", "Handling logout on Wear OS")

        // Optionally, start a new activity or show a message
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun sendLogoutRequestToMobile() {
        val putDataMapRequest = PutDataMapRequest.create("/session_data")
        val dataMap = putDataMapRequest.dataMap
        dataMap.putString("action", "logout")
        val request = putDataMapRequest.asPutDataRequest()
        val dataItemTask = Wearable.getDataClient(this).putDataItem(request)

        dataItemTask.addOnSuccessListener {
            Log.d("WEAR_PROCES", "Logout request sent to mobile successfully.")
        }
        dataItemTask.addOnFailureListener {
            Log.e("WEAR_PROCES", "Failed to send logout request to mobile.")
        }
    }
}
