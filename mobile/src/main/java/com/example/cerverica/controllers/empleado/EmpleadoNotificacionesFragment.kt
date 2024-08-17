package com.example.cerverica.controllers.empleado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cerverica.adapter.NotificacionAdapter
import com.example.cerverica.databinding.FragmentNotificacionesBinding
import com.example.cerverica.models.NotificacionModel
import com.example.cerverica.viewmodels.NotificacionViewModel

class EmpleadoNotificacionesFragment: Fragment() {
    private lateinit var notificacionViewModel: NotificacionViewModel
    private lateinit var binding: FragmentNotificacionesBinding
    private lateinit var notificacionAdapter: NotificacionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificacionesBinding.inflate(inflater, container, false)
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

        initNotificaciones()
    }

    override fun onResume() {
        super.onResume()

        if (::binding.isInitialized && binding.recyclerViewNotificaciones.adapter != null) {
            (binding.recyclerViewNotificaciones.adapter as NotificacionAdapter).filter("")
        }
    }

    fun initNotificaciones(){
        notificacionViewModel = ViewModelProvider(this).get(NotificacionViewModel::class.java)

        // Inicializar el adapter y el RecyclerView
        notificacionAdapter = NotificacionAdapter(emptyList()) { notificacion -> onQuitarNotificacion(notificacion) }

        binding.recyclerViewNotificaciones.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        binding.recyclerViewNotificaciones.adapter = notificacionAdapter

        notificacionViewModel.notificaciones.observe(viewLifecycleOwner) { notificaciones ->

            notificaciones?.let {
                notificacionAdapter.updateNotificaciones(it)
            }
        }
        notificacionViewModel.eliminacionExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito) {
            } else {
                Toast.makeText(requireContext(), "No se pudo eliminar la notificación", Toast.LENGTH_SHORT).show()
            }
        }
        notificacionViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarNotificaciones.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        notificacionViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        // Iniciar la carga de recetas
        notificacionViewModel.fetchNotificaciones()
    }

    fun onQuitarNotificacion(notificacion: NotificacionModel){
        notificacionViewModel.eliminarNotificacion(notificacion.id, binding.root.context)
    }


    fun realizarBusqueda(text: String){
        (binding.recyclerViewNotificaciones.adapter as NotificacionAdapter).filter(text)
    }

    fun ordenAscendente(orden: Boolean){
        (binding.recyclerViewNotificaciones.adapter as NotificacionAdapter).ordenarAscendente(orden)
    }
}