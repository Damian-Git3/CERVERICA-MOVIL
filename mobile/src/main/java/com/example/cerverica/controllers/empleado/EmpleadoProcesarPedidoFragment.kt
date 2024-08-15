package com.example.cerverica.controllers.empleado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.cerverica.databinding.FragmentEmpleadoProcesarPedidoBinding
import com.example.cerverica.models.DetalleVenta
import com.example.cerverica.models.Pedido
import com.example.cerverica.viewmodels.pedidos.PedidoViewModel

class EmpleadoProcesarPedidoFragment : Fragment() {
    private lateinit var pedidoViewModel: PedidoViewModel
    private lateinit var binding: FragmentEmpleadoProcesarPedidoBinding
    private var idPedido: Int? = null

    private lateinit var tvNumeroDetalleActual: TextView
    private lateinit var tvNumeroDetalleTotales: TextView
    private lateinit var tvNombreReceta: TextView
    private lateinit var tvNumeroPedido: TextView
    private lateinit var tvCantidadTotalBotellas: TextView
    private lateinit var tvFechaVenta: TextView
    private lateinit var tvMetodoEnvio: TextView
    private lateinit var tvPaquete: TextView
    private lateinit var tvCantidadPaquete: TextView
    private lateinit var pedido: Pedido
    private lateinit var detalleVentaActual: DetalleVenta

    private lateinit var btnSiguienteDetalle: AppCompatButton
    private lateinit var btnMarcarListo: AppCompatButton
    private var numeroDetalleVentaActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idPedido = it.getInt("idPedido")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmpleadoProcesarPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarComponentes()
        inicializarListeners()
        obtenerPedido()
    }

    private fun inicializarComponentes() {
        pedidoViewModel = ViewModelProvider(this).get(PedidoViewModel::class.java)

        tvNumeroDetalleActual = binding.tvNumeroDetalleActual
        tvNumeroDetalleTotales = binding.tvNumeroDetalleTotales
        tvNombreReceta = binding.tvNombreReceta
        tvNumeroPedido = binding.tvNumeroPedido
        tvCantidadTotalBotellas = binding.tvCantidadTotalBotellas
        tvFechaVenta = binding.tvFechaVenta
        tvMetodoEnvio = binding.tvMetodoEnvio
        tvPaquete = binding.tvPaquete
        tvCantidadPaquete = binding.tvCantidadPaquete

        btnSiguienteDetalle = binding.btnSiguienteDetalle
        btnMarcarListo = binding.btnMarcarListo
    }

    private fun inicializarListeners() {
        btnSiguienteDetalle.setOnClickListener {
            if (pedido.estatusVenta == 1) {
                pedidoViewModel.siguienteEstatus(idPedido!!, onSuccess = {
                    // Si la actualización del estatus es exitosa, continuar con el siguiente detalle
                    cambiarDetallePedidoActual()
                }, onFailure = {
                    // Si falla la actualización, mostrar un mensaje al usuario
                    Toast.makeText(
                        requireContext(),
                        "Error al actualizar el estatus. Inténtalo nuevamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            } else {
                // Si el estatusVenta no es 1, continuar con el siguiente detalle
                cambiarDetallePedidoActual()
            }
        }


        btnMarcarListo.setOnClickListener {
            pedidoViewModel.siguienteEstatus(idPedido!!, onSuccess = {
                Toast.makeText(
                    requireContext(),
                    "Pedido completado exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().onBackPressed() // Regresa al fragmento anterior
            }, onFailure = {
                // Manejar fallo si es necesario
            })
        }

    }

    private fun cargarInformacionPedido() {
        tvNumeroPedido.text = pedido.id.toString()
        tvCantidadTotalBotellas.text = pedido.totalCervezas.toString()
        tvNumeroDetalleTotales.text = pedido.productosPedido.size.toString()
        tvFechaVenta.text = pedido.fechaVenta
        tvMetodoEnvio.text = when (pedido.metodoEnvio) {
            1 -> "Recoger en tienda"
            2 -> "Envío a domicilio"
            else -> "Método de envío desconocido"
        }

        cambiarDetallePedidoActual()
    }

    private fun cambiarDetallePedidoActual() {
        detalleVentaActual = pedido.productosPedido[numeroDetalleVentaActual]

        tvNombreReceta.text = detalleVentaActual.stock?.receta?.nombre
        tvPaquete.text = detalleVentaActual.pack.toString()
        tvCantidadPaquete.text = detalleVentaActual.cantidad.toString()
        tvNumeroDetalleActual.text = (numeroDetalleVentaActual + 1).toString()

        numeroDetalleVentaActual++

        if (numeroDetalleVentaActual == pedido.productosPedido.size) {
            btnSiguienteDetalle.visibility = View.GONE
            btnMarcarListo.visibility = View.VISIBLE
        } else {
            btnSiguienteDetalle.visibility = View.VISIBLE
            btnMarcarListo.visibility = View.GONE
        }
    }

    private fun obtenerPedido() {
        pedidoViewModel.pedido.observe(viewLifecycleOwner) { pedido ->
            if (pedido != null) {
                this.pedido = pedido
                cargarInformacionPedido()
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

        idPedido?.let { pedidoViewModel.obtenerPedido(it) }
    }

    companion object {
        @JvmStatic
        fun newInstance(idPedido: Int) =
            EmpleadoProcesarPedidoFragment().apply {
                arguments = Bundle().apply {
                    putInt("idPedido", idPedido)
                }
            }
    }
}
