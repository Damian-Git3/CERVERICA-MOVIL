package com.example.cerverica.viewmodels.cliente

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.cliente.RecetaPackModel
import com.example.cerverica.models.cliente.VentaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialViewModel : ViewModel() {
    private val _compras = MutableLiveData<List<VentaModel>>()
    val compras: LiveData<List<VentaModel>> get() = _compras

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchHistorial() {
        _loading.value = true

        RetrofitClient.instance.getClientBuys().enqueue(object : Callback<List<VentaModel>> {
            override fun onResponse(call: Call<List<VentaModel>>, response: Response<List<VentaModel>>) {
                _loading.value = false

                if (response.isSuccessful) {
                    _compras.value = response.body()
                } else {
                    _error.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<VentaModel>>, t: Throwable) {
                _loading.value = false
                _error.value = "Fallo en la consulta: ${t.message}"
            }
        })
    }
}