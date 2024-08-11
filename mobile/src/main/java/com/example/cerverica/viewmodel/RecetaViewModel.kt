package com.example.cerverica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.RecetaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecetaViewModel : ViewModel() {

    private val _recetas = MutableLiveData<List<RecetaModel>>()
    val recetas: LiveData<List<RecetaModel>> get() = _recetas

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchRecetas() {
        _isLoading.value = true

        RetrofitClient.instance.getRecetas().enqueue(object : Callback<List<RecetaModel>> {
            override fun onResponse(call: Call<List<RecetaModel>>, response: Response<List<RecetaModel>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _recetas.value = response.body()
                } else {
                    _error.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<RecetaModel>>, t: Throwable) {
                _isLoading.value = false
                _error.value = "Fallo en la consulta: ${t.message}"
            }
        })
    }
}
