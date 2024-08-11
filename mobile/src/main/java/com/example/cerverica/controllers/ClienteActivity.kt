package com.example.cerverica.controllers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cerverica.R
import com.example.cerverica.adapter.RecetaAdapter
import com.example.cerverica.models.RecetaModel
import com.example.cerverica.apiservice.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteActivity : AppCompatActivity() {
    private lateinit var recyclerViewReceta: RecyclerView
    private lateinit var progressBarReceta: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        recyclerViewReceta = findViewById(R.id.recyclerViewReceta)
        recyclerViewReceta.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        progressBarReceta = findViewById(R.id.progressBarReceta)
        fetchRecetas()
    }

    private fun fetchRecetas() {
        progressBarReceta.visibility = View.VISIBLE

        RetrofitClient.instance.getRecetas().enqueue(object : Callback<List<RecetaModel>> {
            override fun onResponse(call: Call<List<RecetaModel>>, response: Response<List<RecetaModel>>) {
                progressBarReceta.visibility = View.GONE

                if (response.isSuccessful) {
                    val recetas = response.body()
                    if (recetas != null) {
                        Log.d("API_RESPONSE", "Recetas recibidas: $recetas")
                        recyclerViewReceta.adapter = RecetaAdapter(recetas)
                    } else {
                        Log.e("API_RESPONSE", "La respuesta fue exitosa pero el cuerpo es nulo.")
                    }
                } else {
                    Log.e("API_RESPONSE", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<RecetaModel>>, t: Throwable) {
                progressBarReceta.visibility = View.GONE
                Log.e("API_RESPONSE", "Fallo en la consulta: ${t.message}")
                Toast.makeText(this@ClienteActivity, "Error al cargar las recetas", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
