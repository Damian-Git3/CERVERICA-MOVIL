package com.example.cerverica.controllers.cliente

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
import com.example.cerverica.adapter.cliente.FavoritoAdapter
import com.example.cerverica.adapter.cliente.HistorialComprasAdapter
import com.example.cerverica.adapter.cliente.RecetaPackAdapter
import com.example.cerverica.databinding.FragmentHistorialClienteBinding
import com.example.cerverica.databinding.FragmentInicioClienteBinding
import com.example.cerverica.viewmodels.cliente.FavoritoViewModel
import com.example.cerverica.viewmodels.cliente.HistorialViewModel
import com.example.cerverica.viewmodels.cliente.PackViewModel

class ClienteComprasFragment: Fragment() {
    private lateinit var historialViewModel: HistorialViewModel
    private lateinit var binding: FragmentHistorialClienteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistorialClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittextFiltrar.addTextChangedListener { text ->
            realizarBusqueda(text.toString())
        }
        var ascendente = true
        binding.btnAscendente.setOnClickListener {
            if (ascendente){
                ordenAscendente(true)
                binding.btnAscendente.text = "Fecha Ascendente"
            } else{
                ordenAscendente(false)
                binding.btnAscendente.text = "Fecha Descendente"
            }
            ascendente = !ascendente
        }

        initHistorial()
    }

    fun initHistorial(){
        historialViewModel = ViewModelProvider(this).get(HistorialViewModel::class.java)

        // Suscribirse a los LiveData del ViewModel
        historialViewModel.compras.observe(viewLifecycleOwner) { compras ->
            if (compras != null) {
                binding.recyclerViewCompras.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerViewCompras.adapter = HistorialComprasAdapter(compras)
            }
        }

        historialViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarReceta.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        historialViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Iniciar la carga de recetas
        historialViewModel.fetchHistorial()
    }

    fun realizarBusqueda(text: String){
        (binding.recyclerViewCompras.adapter as HistorialComprasAdapter).filter(text)
    }

    fun ordenAscendente(orden: Boolean){
        (binding.recyclerViewCompras.adapter as HistorialComprasAdapter).ordenarAscendente(orden)
    }

}