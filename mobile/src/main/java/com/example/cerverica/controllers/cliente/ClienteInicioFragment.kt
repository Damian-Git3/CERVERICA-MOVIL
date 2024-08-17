package com.example.cerverica.controllers.cliente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cerverica.adapter.cliente.Favorito2Adapter
import com.example.cerverica.adapter.cliente.FavoritoAdapter
import com.example.cerverica.adapter.cliente.RecetaAdapter
import com.example.cerverica.adapter.cliente.RecetaPackAdapter
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.databinding.FragmentInicioClienteBinding
import com.example.cerverica.models.cliente.RecetaFavoritoModel
import com.example.cerverica.models.cliente.RecetaModel
import com.example.cerverica.models.cliente.RecetaPackModel
import com.example.cerverica.viewmodels.cliente.FavoritoViewModel
import com.example.cerverica.viewmodels.cliente.PackViewModel
import com.example.cerverica.viewmodels.cliente.RecetaViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteInicioFragment : Fragment() {
    private lateinit var viewModel: RecetaViewModel
    private lateinit var packViewModel: PackViewModel
    private lateinit var favoritoViewModel: FavoritoViewModel
    private lateinit var binding: FragmentInicioClienteBinding
    private lateinit var favoritoAdapter: FavoritoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittextFiltrar.addTextChangedListener { text ->
            realizarBusqueda(text.toString())
        }

        binding.verTodoPacks.setOnClickListener{
            (binding.recyclerViewPacks.adapter as RecetaPackAdapter).filter("")
        }
        binding.verTodoFavoritos.setOnClickListener{
            //(binding.recyclerViewFavoritos.adapter as FavoritoAdapter).filter("")
            val intent = Intent(requireContext(),ClienteFavoritosActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)

        }

        initRecetas()
        initPacks()
        initFavoritos()
    }

    override fun onResume() {
        super.onResume()

        if (::binding.isInitialized && binding.recyclerViewFavoritos.adapter != null) {
            (binding.recyclerViewFavoritos.adapter as FavoritoAdapter).filter("")
        }
    }

    fun initRecetas(){
        viewModel = ViewModelProvider(this).get(RecetaViewModel::class.java)

        // Suscribirse a los LiveData del ViewModel
        viewModel.recetas.observe(viewLifecycleOwner) { recetas ->
            if (recetas != null) {
                binding.recyclerViewReceta.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL, false
                )
                binding.recyclerViewReceta.adapter = RecetaAdapter(recetas) { receta ->
                    Log.d("busquedaEtiqueta", "busqueda receta")
                    realizarBusqueda(receta.nombre)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarReceta.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Iniciar la carga de recetas
        viewModel.fetchRecetas()
    }

    fun initPacks(){
        packViewModel = ViewModelProvider(this).get(PackViewModel::class.java)

        // Suscribirse a los LiveData del ViewModel
        packViewModel.packs.observe(viewLifecycleOwner) { packs ->
            if (packs != null) {
                binding.recyclerViewPacks.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL, false
                )
                binding.recyclerViewPacks.adapter = RecetaPackAdapter(packs){
                    onAgregarFavorito(it)
                }
            }
        }

        packViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarPacks.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        packViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Iniciar la carga de recetas
        packViewModel.fetchPacks()
    }

    fun initFavoritos(){
        favoritoViewModel = ViewModelProvider(this).get(FavoritoViewModel::class.java)

        // Inicializar el adapter y el RecyclerView
        favoritoAdapter = FavoritoAdapter(emptyList()) { favorito -> onQuitarFavorito(favorito) }
        binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.recyclerViewFavoritos.adapter = favoritoAdapter

        // Suscribirse a los LiveData del ViewModel
        favoritoViewModel.favoritos.observe(viewLifecycleOwner) { favoritos ->
            favoritos?.let {
                favoritoAdapter.updateFavoritos(it)
            }
        }

        favoritoViewModel.eliminacionExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(),"Se quitó el favorito",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No se pudo eliminar el favorito", Toast.LENGTH_SHORT).show()
            }
        }

        favoritoViewModel.agregadoExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(),"Se agregó el favorito",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No se pudo agregar el favorito", Toast.LENGTH_SHORT).show()
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

    fun onAgregarFavorito(pack: RecetaPackModel) {

        favoritoViewModel.agregarFavorito(pack.idReceta, binding.root.context)
    }

    fun realizarBusqueda(text: String){
        (binding.recyclerViewPacks.adapter as RecetaPackAdapter).filter(text)
        (binding.recyclerViewFavoritos.adapter as FavoritoAdapter).filter(text)
    }
}