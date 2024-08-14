package com.example.cerverica

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.wearable.*

class SessionMobileListenerService : Service(), DataClient.OnDataChangedListener {

    private lateinit var dataClient: DataClient

    override fun onCreate() {
        super.onCreate()
        dataClient = Wearable.getDataClient(this)
        dataClient.addListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (path == "/session_data") {
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val action = dataMapItem.dataMap.getString("action")

                    if (action == "logout") {
                        handleLogout()
                    }
                }
            }
        }
    }

    private fun handleLogout() {
        Log.d("WEAR_PROCES", "Handling logout on mobile")

        // Implement logout logic here
        clearUserSession()

        // Optionally, start LoginActivity or other action
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun clearUserSession() {
        val sharedPref = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        dataClient.removeListener(this)
    }
}
