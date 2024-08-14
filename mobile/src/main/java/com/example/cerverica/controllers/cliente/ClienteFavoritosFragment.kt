package com.example.cerverica.controllers.cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cerverica.adapter.cliente.Favorito2Adapter
import com.example.cerverica.databinding.FragmentFavoritosClienteBinding
import com.example.cerverica.models.cliente.RecetaFavoritoModel
import com.example.cerverica.viewmodels.cliente.FavoritoViewModel

class ClienteFavoritosFragment: Fragment() {
    private lateinit var favoritoViewModel: FavoritoViewModel
    private lateinit var binding: FragmentFavoritosClienteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittextFiltrar.addTextChangedListener { text ->
            realizarBusqueda(text.toString())
        }

        initFavoritos()
    }

    fun initFavoritos(){
        favoritoViewModel = ViewModelProvider(this).get(FavoritoViewModel::class.java)

        // Suscribirse a los LiveData del ViewModel
        favoritoViewModel.favoritos.observe(viewLifecycleOwner) { favoritos ->
            if (favoritos != null) {
                binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerViewFavoritos.adapter = Favorito2Adapter(favoritos
                ) { favorito -> onQuitarFavorito(favorito) }
            }
        }

        favoritoViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFavoritos.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        favoritoViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Iniciar la carga de recetas
        favoritoViewModel.fetchFavoritos()
    }

    fun onQuitarFavorito(favorito: RecetaFavoritoModel){
        favoritoViewModel.eliminarFavorito(favorito.idReceta, binding.root.context)
    }


    fun realizarBusqueda(text: String){
        (binding.recyclerViewFavoritos.adapter as Favorito2Adapter).filter(text)
    }
}