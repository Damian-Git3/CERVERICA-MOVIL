package com.example.cerverica

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class InicioActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val logoutButton = findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            sendLogoutRequestToMobile()
        }
    }

    private fun sendLogoutRequestToMobile() {
        val putDataMapRequest = PutDataMapRequest.create("/logout_request")
        val dataMap = putDataMapRequest.dataMap
        dataMap.putString("action", "logout")
        val request = putDataMapRequest.asPutDataRequest()
        Wearable.getDataClient(this).putDataItem(request)
            .addOnSuccessListener {
                Log.d("WEAR_PROCES", "Logout request sent to mobile.")
            }
            .addOnFailureListener {
                Log.e("WEAR_PROCES", "Failed to send logout request to mobile.")
            }
    }
}
