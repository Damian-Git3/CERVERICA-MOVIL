package com.example.cerverica.viewmodels.cliente

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cerverica.MyApplication
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.cliente.RecetaAgregarFavoritoRequest
import com.example.cerverica.models.cliente.RecetaEliminarFavoritoRequest
import com.example.cerverica.models.cliente.RecetaFavoritoModel
import com.example.cerverica.models.cliente.RecetaPackModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritoViewModel : ViewModel() {
        private val _favoritos = MutableLiveData<List<RecetaFavoritoModel>>()
        val favoritos: LiveData<List<RecetaFavoritoModel>> get() = _favoritos

        private val _loading = MutableLiveData<Boolean>()
        val loading: LiveData<Boolean> get() = _loading

        private val _error = MutableLiveData<String>()
        val error: LiveData<String> get() = _error

        private fun getIdUsuario(): String? {
            val sharedPref = MyApplication.context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            return sharedPref.getString("idUsuario", null)
        }

        private val idUsuario = getIdUsuario()

        fun fetchFavoritos() {
            _loading.value = true

            if(idUsuario!=null){
                RetrofitClient.instance.getRecetasFavoritos(idUsuario).enqueue(object :
                    Callback<List<RecetaFavoritoModel>> {
                    override fun onResponse(call: Call<List<RecetaFavoritoModel>>, response: Response<List<RecetaFavoritoModel>>) {
                        _loading.value = false

                        if (response.isSuccessful) {
                            _favoritos.value = response.body()
                        } else {
                            _error.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                        }
                    }

                    override fun onFailure(call: Call<List<RecetaFavoritoModel>>, t: Throwable) {
                        _loading.value = false
                        _error.value = "Fallo en la consulta: ${t.message}"
                    }
                })
            }else{
                _loading.value = false
                _error.value = "No se encuentra la id del usuario actual"
            }
        }

    fun agregarFavorito(idReceta:Int, context: Context){
        if(idUsuario!=null){
            val newFavorito = RecetaAgregarFavoritoRequest(idUsuario, idReceta)

            RetrofitClient.instance.postAgregarFavoritos(newFavorito).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("Favorito", "onResponse: ${response}")
                    if (!response.isSuccessful) {
                        _error.value = "Advertencia: ${response.errorBody()?.string()}"
                    }else{
                        Toast.makeText(context,"Se agregó el favorito.\nRecarge la página para ver cambios.",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _error.value = "Fallo en la consulta: ${t.message}"
                }
            })
        }
    }

    fun eliminarFavorito(idReceta:Int, context: Context){
        if(idUsuario!=null){
            val newFavorito = RecetaEliminarFavoritoRequest(idUsuario, idReceta)

            RetrofitClient.instance.postEliminarFavoritos(newFavorito).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    if (!response.isSuccessful) {
                        _error.value = "Advertencia: ${response.errorBody()?.string()}"
                    }else{
                        Toast.makeText(context,"Se quitó el favorito.\nRecarge la página para ver cambios.",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _error.value = "Fallo en la consulta: ${t.message}"
                }
            })
        }
    }
}