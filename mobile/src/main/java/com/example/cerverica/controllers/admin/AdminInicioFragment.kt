package com.example.cerverica.controllers.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.cerverica.databinding.FragmentInicioAdminBinding
import com.example.cerverica.databinding.FragmentInicioEmpleadoBinding

class AdminInicioFragment : Fragment() {

    private lateinit var binding: FragmentInicioAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittextFiltrar.addTextChangedListener { text ->
            realizarBusqueda(text.toString())
        }

        initNotificaciones()
    }

    fun initNotificaciones(){

    }

    fun realizarBusqueda(text: String){
        //(binding.recyclerViewPacks.adapter as RecetaPackAdapter).filter(text)
        //(binding.recyclerViewFavoritos.adapter as FavoritoAdapter).filter(text)
    }
}