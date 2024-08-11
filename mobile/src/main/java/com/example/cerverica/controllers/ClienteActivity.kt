package com.example.cerverica.controllers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cerverica.BaseActivity
import com.example.cerverica.R
import com.example.cerverica.adapter.RecetaAdapter
import com.example.cerverica.models.RecetaModel
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.databinding.ActivityClienteBinding
import com.example.cerverica.viewmodel.RecetaViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteActivity : BaseActivity() {
    //private lateinit var recyclerViewReceta: RecyclerView
    //private lateinit var progressBarReceta: ProgressBar
    lateinit var binding: ActivityClienteBinding
    private lateinit var recetaViewModel: RecetaViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecetas()

        /*
        setContentView(R.layout.activity_cliente)

        recyclerViewReceta = findViewById(R.id.recyclerViewReceta)
        recyclerViewReceta.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        progressBarReceta = findViewById(R.id.progressBarReceta)
        fetchRecetas()
        * */
    }

    private fun initRecetas(){
        binding.progressBarReceta.visibility = View.VISIBLE
        recetaViewModel = ViewModelProvider(this).get(RecetaViewModel::class.java)

        // Observadores para los LiveData del ViewModel
        recetaViewModel.recetas.observe(this, Observer { recetas ->
            if (recetas != null) {
                binding.recyclerViewReceta.adapter = RecetaAdapter(recetas)
            }
        })

        recetaViewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBarReceta.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        recetaViewModel.error.observe(this, Observer { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        // Llamada para obtener las recetas
        recetaViewModel.fetchRecetas()
    }
/*
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
    }*/
}
