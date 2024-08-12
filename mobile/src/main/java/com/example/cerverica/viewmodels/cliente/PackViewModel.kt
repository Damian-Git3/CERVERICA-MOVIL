package com.example.cerverica.viewmodels.cliente

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.cliente.RecetaModel
import com.example.cerverica.models.cliente.RecetaPackModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackViewModel : ViewModel() {
    private val _packs = MutableLiveData<List<RecetaPackModel>>()
    val packs: LiveData<List<RecetaPackModel>> get() = _packs

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchPacks() {
        _loading.value = true

        RetrofitClient.instance.getRecetasPack().enqueue(object : Callback<List<RecetaPackModel>> {
            override fun onResponse(call: Call<List<RecetaPackModel>>, response: Response<List<RecetaPackModel>>) {
                _loading.value = false

                if (response.isSuccessful) {
                    _packs.value = response.body()
                } else {
                    _error.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<RecetaPackModel>>, t: Throwable) {
                _loading.value = false
                _error.value = "Fallo en la consulta: ${t.message}"
            }
        })
    }
}