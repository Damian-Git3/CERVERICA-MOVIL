package com.example.cerverica.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cerverica.MyApplication
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.NotificacionModel
import com.example.cerverica.models.cliente.RecetaAgregarFavoritoRequest
import com.example.cerverica.models.cliente.RecetaEliminarFavoritoRequest
import com.example.cerverica.models.cliente.RecetaFavoritoModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class NotificacionViewModel: ViewModel() {

    private val _notificaciones = MutableLiveData<List<NotificacionModel>>()
    val notificaciones: LiveData<List<NotificacionModel>> get() = _notificaciones

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _eliminacionExitoso = MutableLiveData<Boolean>()
    val eliminacionExitoso: LiveData<Boolean> get() = _eliminacionExitoso

    fun fetchNotificaciones() {
        _loading.value = true

        RetrofitClient.instance.getNotificaciones().enqueue(object :
            Callback<List<NotificacionModel>> {
            override fun onResponse(call: Call<List<NotificacionModel>>, response: Response<List<NotificacionModel>>) {
                _loading.value = false

                if (response.isSuccessful) {

                    _notificaciones.value = response.body()
                } else {
                    _error.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<NotificacionModel>>, t: Throwable) {
                _loading.value = false
                _error.value = "Fallo en la consulta: ${t.message}"
            }
        })

    }

    fun eliminarNotificacion(id:Int, context: Context){
            RetrofitClient.instance.putNotificacionVista(id).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    if (!response.isSuccessful) {
                        _error.value = "Advertencia: ${response.errorBody()?.string()}"
                        _eliminacionExitoso.value = false
                    }else{
                        fetchNotificaciones()
                        _eliminacionExitoso.value = true
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _error.value = "Fallo en la consulta: ${t.message}"
                    _eliminacionExitoso.value = false
                }
            })

    }
}