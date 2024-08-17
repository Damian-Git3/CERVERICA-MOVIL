package com.example.cerverica.controllers.empleado


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.cerverica.BaseActivity
import com.example.cerverica.R
import com.example.cerverica.controllers.cliente.ClienteComprasFragment
import com.example.cerverica.controllers.cliente.ClienteCuentaFragment
import com.example.cerverica.controllers.cliente.ClienteInicioFragment
import com.example.cerverica.databinding.ActivityClienteBinding
import com.example.cerverica.databinding.ActivityEmpleadoBinding

class EmpleadoActivity : BaseActivity() {
    lateinit var binding: ActivityEmpleadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpleadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationPedidos.setOnClickListener {
            loadFragment((EmpleadoListaPedidosFragment()))
        }
        binding.navigationInicio.setOnClickListener {
            loadFragment(EmpleadoInicioFragment())
        }

        binding.navigationNotificaciones.setOnClickListener {
            loadFragment(EmpleadoNotificacionesFragment())
        }
        binding.navigationCuenta.setOnClickListener {
            loadFragment((EmpleadoCuentaFragment()))
        }
        binding.navigationProducciones.setOnClickListener {
            //loadFragment(())
        }
        if (savedInstanceState == null) {
            loadFragment(EmpleadoInicioFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}