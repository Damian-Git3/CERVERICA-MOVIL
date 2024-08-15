package com.example.cerverica

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.cerverica.apiservice.RetrofitClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileWearableService : Service(), DataClient.OnDataChangedListener {

    private lateinit var dataClient: DataClient

    override fun onCreate() {
        super.onCreate()
        Log.d("WEAR_PROCES", "onCreate: se inicio el servicio")
        dataClient = Wearable.getDataClient(this)
        dataClient.addListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("WEAR_PROCES", "paso 4: recibir la peticion desde broadcast en service")
        val action = intent?.getStringExtra("action")
        when (action) {
            "login" -> {
                Log.d("WEAR_PROCES", "paso 5:  reconocer la accion de login")
                val idUsuario = intent.getStringExtra("idUsuario")
                val nombre = intent.getStringExtra("nombre")
                val role = intent.getStringExtra("role")
                sendLoginToWearOS(idUsuario!!, nombre!!, role!!)
            }
            "logout" -> {
                Log.d("WEAR_PROCES", "paso 5:  reconocer la accion de logout")
                handleLogout(this)

            }
        }
        return START_STICKY
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("WEAR_PROCES", "DATA HAS CHANGED")
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                Log.d("WEAR_PROCES", "DataItem URI Path: ${dataItem.uri.path}")
                if (dataItem.uri.path == "/session_data") {
                    val dataMap = dataItem.data?.let { DataMap.fromByteArray(it) }
                    val action = dataMap?.getString("action")

                    Log.d("WEAR_PROCES", "Received action: $action")

                    if (action == "logout") {
                        handleLogout(this)
                    }
                }
            }
        }
    }


    private fun handleLogout(context: Context) {
        RetrofitClient.instance.postLogout().enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (!response.isSuccessful) {
                    val error = "Advertencia: ${response.errorBody()?.string()}"
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("logout", "onResponse: ${response.body()}")
                    Toast.makeText(context, "Adiós", Toast.LENGTH_SHORT).show()
                    // Llamar al callback de éxito
                    sendLogoutToWearOs()

                    // Redireccionar al LoginActivity
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val error = "Fallo en la consulta: ${t.message}"
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun sendLogoutToWearOs(){
        val putDataMapRequest = PutDataMapRequest.create("/session_data")
        val dataMap = putDataMapRequest.dataMap
        dataMap.putString("action", "logout")  // Añadido para cambiar la actividad
        //se pone un numero para que el DataItem tenga un cambio siempre y de este modo se active onDataChange
        dataMap.putInt("controlNumber", 2)
        val request = putDataMapRequest.asPutDataRequest()
        val dataItemTask: Task<DataItem> = dataClient.putDataItem(request)

        dataItemTask.addOnSuccessListener {
            Log.d("WEAR_PROCES", "Logout data sent successfully to Wear OS.")
        }

        dataItemTask.addOnFailureListener {
            Log.e("WEAR_PROCES", "Failed to send logout data to Wear OS.")
        }
    }

    private fun sendLoginToWearOS(idUsuario: String, nombre: String, role: String) {
        Log.d("WEAR_PROCES", "paso 6: crear el map de datos y ponerlos en el data client ")
        val putDataMapRequest = PutDataMapRequest.create("/session_data")
        val dataMap = putDataMapRequest.dataMap
        dataMap.putString("idUsuario", idUsuario)
        dataMap.putString("nombre", nombre)
        dataMap.putString("role", role)
        //se pone un numero para que el DataItem tenga un cambio siempre y de este modo se active onDataChange
        dataMap.putInt("controlNumber", 0)
        dataMap.putString("action", "login")  // Añadido para cambiar la actividad
        val request = putDataMapRequest.asPutDataRequest()
        val dataItemTask: Task<DataItem> = dataClient.putDataItem(request)

        dataItemTask.addOnSuccessListener {
            Log.d("WEAR_PROCES", "Login data sent successfully to Wear OS.")
        }

        dataItemTask.addOnFailureListener {
            Log.e("WEAR_PROCES", "Failed to send login data to Wear OS.")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WEAR_PROCES", "#################")
        dataClient.removeListener(this)
    }
}
