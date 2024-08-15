package com.example.cerverica.viewmodels.pedidos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.Pedido
import com.example.cerverica.models.cliente.RecetaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoViewModel: ViewModel() {
    private val _pedidos = MutableLiveData<List<Pedido>>()
    val pedidos: LiveData<List<Pedido>> get() = _pedidos

    private val _pedido = MutableLiveData<Pedido>()
    val pedido: LiveData<Pedido> get() = _pedido

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun obtenerPedidos() {
        _loading.value = true

        RetrofitClient.instance.getPedidos().enqueue(object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                _loading.value = false

                if (response.isSuccessful) {
                    _pedidos.value = response.body()
                } else {
                    _error.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                _loading.value = false
                _error.value = "Fallo en la consulta: ${t.message}"
            }
        })

    }

    fun obtenerPedido(idPedido: Int) {
        _loading.value = true

        RetrofitClient.instance.getPedido(idPedido).enqueue(object : Callback<Pedido> {
            override fun onResponse(call: Call<Pedido>, response: Response<Pedido>) {
                _loading.value = false

                if (response.isSuccessful) {
                    _pedido.value = response.body()
                } else {
                    _error.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<Pedido>, t: Throwable) {
                _loading.value = false
                _error.value = "Fallo en la consulta: ${t.message}"
            }
        })
    }

    fun siguienteEstatus(idPedido: Int, onSuccess: () -> Unit, onFailure: () -> Unit) {
        _loading.value = true

        RetrofitClient.instance.marcarSiguienteEstatus(idPedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _loading.value = false

                if (response.isSuccessful) {
                    // Si el estatus es 200
                    onSuccess()
                } else {
                    _error.value = "Error al actualizar el estatus: ${response.errorBody()?.string()}"
                    onFailure()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _loading.value = false
                _error.value = "Fallo en la actualización: ${t.message}"
                onFailure()
            }
        })
    }

}