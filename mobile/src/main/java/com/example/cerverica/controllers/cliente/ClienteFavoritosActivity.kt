package com.example.cerverica.controllers.cliente

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cerverica.BaseActivity
import com.example.cerverica.adapter.cliente.Favorito2Adapter
import com.example.cerverica.controllers.admin.AdminActivity
import com.example.cerverica.databinding.ActivityFavoritosClienteBinding
import com.example.cerverica.models.cliente.RecetaFavoritoModel
import com.example.cerverica.viewmodels.cliente.FavoritoViewModel

class ClienteFavoritosActivity : BaseActivity() {
    private lateinit var favoritoViewModel: FavoritoViewModel
    private lateinit var binding: ActivityFavoritosClienteBinding
    private lateinit var favoritoAdapter: Favorito2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout
        binding = ActivityFavoritosClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el listener para el campo de búsqueda
        binding.edittextFiltrar.addTextChangedListener { text ->
            realizarBusqueda(text.toString())
        }

        binding.volver.setOnClickListener {
            startActivity(Intent(this, ClienteActivity::class.java))
            finish()
        }

        // Inicializar la lista de favoritos
        initFavoritos()
    }

    fun initFavoritos(){
        favoritoViewModel = ViewModelProvider(this).get(FavoritoViewModel::class.java)

        // Inicializar el adapter y el RecyclerView
        favoritoAdapter = Favorito2Adapter(emptyList()) { favorito -> onQuitarFavorito(favorito) }
        binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewFavoritos.adapter = favoritoAdapter

        // Suscribirse a los LiveData del ViewModel
        favoritoViewModel.favoritos.observe(this) { favoritos ->
            favoritos?.let {
                favoritoAdapter.updateFavoritos(it)
            }
        }

        favoritoViewModel.eliminacionExitoso.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this,"Se quitó el favorito",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo eliminar el favorito", Toast.LENGTH_SHORT).show()
            }
        }

        favoritoViewModel.loading.observe(this) { isLoading ->
            binding.progressBarFavoritos.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        favoritoViewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        favoritoViewModel.fetchFavoritos()
    }

    fun onQuitarFavorito(favorito: RecetaFavoritoModel){
        favoritoViewModel.eliminarFavorito(favorito.idReceta, this)
    }


    fun realizarBusqueda(text: String){
        (binding.recyclerViewFavoritos.adapter as Favorito2Adapter).filter(text)
    }
}