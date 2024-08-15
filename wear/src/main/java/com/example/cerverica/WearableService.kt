package com.example.cerverica

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.WearableListenerService
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlin.random.Random

class WearableService : Service(), DataClient.OnDataChangedListener {

    private lateinit var dataClient: DataClient

    override fun onCreate() {
        super.onCreate()
        Log.d("WEAR_PROCES", "Servicio creado y listo para ejecutar.")
        dataClient = Wearable.getDataClient(this)
        dataClient.addListener(this)
        getDevice(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("WEAR_PROCES", "service start")
        val action = intent?.getStringExtra("action")
        when (action) {
            "logout" -> {
                Log.d("WEAR_PROCES", "paso 5: se valido que fuera un logout")
                sendLogoutRequestToMobile()
            }
            "sinc" -> {
                Log.d("WEAR_PROCES", "revision de sincronizacion")
                getDevice(false)
            }
            "check" -> {
                Log.d("WEAR_PROCES", "comprobar login service")
                checkCurrentSessionStatus()
            }
        }

        Log.d("WEAR_PROCES", "comprobar el login")
        checkCurrentSessionStatus()


        return START_STICKY
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("WEAR_PROCES", "paso 1: se detecto un cambio")
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                if (dataItem.uri.path == "/session_data") {
                    Log.d("WEAR_PROCES", "paso 2: es un cambio en el estatus de la sesion")
                    val dataMap = dataItem.data?.let { DataMap.fromByteArray(it) }
                    val action = dataMap?.getString("action")

                    if (action == "login") {
                        Log.d("WEAR_PROCES", "paso 3: es un login")
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
                    }else if(action == "logout"){
                        Log.d("WEAR_PROCES", "paso 3: es un logout")
                        handleLogout()
                    }
                }
            }
        }
    }

    private fun handleLogout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun sendLogoutRequestToMobile() {
        Log.d("WEAR_PROCES", "Sending logout request to mobile")
        val putDataMapRequest = PutDataMapRequest.create("/session_data")
        val dataMap = putDataMapRequest.dataMap

        dataMap.putString("action", "logout")
        //se pone un numero para que el DataItem tenga un cambio siempre y de este modo se active onDataChange
        dataMap.putInt("controlNumber", Random.nextInt(1,100))
        val request = putDataMapRequest.asPutDataRequest()
        val dataItemTask: Task<DataItem> = dataClient.putDataItem(request)

        dataItemTask.addOnSuccessListener {
            Log.d("WEAR_PROCES", "Logout data sent successfully to mobile.")
        }

        dataItemTask.addOnFailureListener {
            Log.e("WEAR_PROCES", "Failed to send logout data to mobile.")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("WEAR_PROCES", "Servicio vinculado.")
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        dataClient.removeListener(this)
    }

    private fun getDevice(showSuccessToast: Boolean) {
        Wearable.getNodeClient(this).connectedNodes.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val connectedNodes = task.result
                if (connectedNodes.isNotEmpty()) {
                    val connectedNode = connectedNodes[0]
                    connectedNode?.let {
                        Log.d("DEBUG", "Nodo conectado: ${it.displayName}")
                        if(showSuccessToast) {
                            Toast.makeText(this, "Conectado: ${it.displayName}", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                } else {
                    Log.d("DEBUG", "No hay nodos conectados")
                    handleLogout()
                    Toast.makeText(this, "No estás conectado a ningún teléfono", Toast.LENGTH_LONG).show()
                }
            } else {
                Log.e("DEBUG", "Error al intentar obtener nodos conectados", task.exception)
                handleLogout()
                Toast.makeText(this, "Error al verificar la conexión", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkCurrentSessionStatus() {
        val dataClient = Wearable.getDataClient(this)

        // Obtén el DataItem para la ruta "/session_data"
        val dataItemTask = dataClient.getDataItems()

        dataItemTask.addOnSuccessListener { dataItems ->
            for (dataItem in dataItems) {
                if (dataItem.uri.path == "/session_data") {
                    val dataMap = dataItem.data?.let { DataMap.fromByteArray(it) }
                    val action = dataMap?.getString("action")

                    // Verifica el valor actual del "action"
                    if (action == "login") {
                        Log.d("WEAR_PROCES", "El estado actual es login")
                        // Llama a una función para manejar el estado "login"
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

                    } else if (action == "logout") {
                        Log.d("WEAR_PROCES", "El estado actual es logout")
                        // Maneja el estado de logout si es necesario
                    }
                    break
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("WEAR_PROCES", "Error al obtener los datos", exception)
        }
    }
}
