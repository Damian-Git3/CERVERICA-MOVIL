package com.example.cerverica.controllers.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.cerverica.BaseActivity
import com.example.cerverica.R
import com.example.cerverica.controllers.empleado.EmpleadoCuentaFragment
import com.example.cerverica.controllers.empleado.EmpleadoInicioFragment
import com.example.cerverica.databinding.ActivityAdminBinding
import com.example.cerverica.databinding.ActivityEmpleadoBinding

class AdminActivity : BaseActivity() {
    lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationInsumos.setOnClickListener {
            //loadFragment(())
        }

        binding.navigationNotificaciones.setOnClickListener {
            //loadFragment(())
        }
        binding.navigationCuenta.setOnClickListener {
            loadFragment((AdminCuentaFragment()))
        }
        binding.navigationProducciones.setOnClickListener {
            //loadFragment(())
        }
        if (savedInstanceState == null) {
            loadFragment(AdminInicioFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}