package com.example.cerverica.controllers.empleado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cerverica.R
import com.example.cerverica.adapter.cliente.HistorialComprasAdapter
import com.example.cerverica.adapter.pedidos.PedidoAdapter
import com.example.cerverica.databinding.FragmentCuentaClienteBinding
import com.example.cerverica.databinding.FragmentEmpleadoListaPedidosBinding
import com.example.cerverica.models.Pedido
import com.example.cerverica.viewmodels.cliente.HistorialViewModel
import com.example.cerverica.viewmodels.pedidos.PedidoViewModel

class EmpleadoListaPedidosFragment : Fragment() {
    private lateinit var pedidoViewModel: PedidoViewModel
    private lateinit var binding: FragmentEmpleadoListaPedidosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmpleadoListaPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarComponentes()

        obtenerPedidos()
    }

    private fun inicializarComponentes() {
        pedidoViewModel = ViewModelProvider(this).get(PedidoViewModel::class.java)
    }

    fun obtenerPedidos() {
        pedidoViewModel.pedidos.observe(viewLifecycleOwner) { pedidos ->
            if (pedidos != null) {
                binding.RVPedidos.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                binding.RVPedidos.adapter = PedidoAdapter(pedidos) { pedido ->
                    cargarProcesarPedido(pedido.id)
                }
            }
        }

        pedidoViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarListaPedidos.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        pedidoViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        pedidoViewModel.obtenerPedidos()
    }

    private fun cargarProcesarPedido(idPedido: Int) {
        val fragment = EmpleadoProcesarPedidoFragment.newInstance(idPedido)

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}