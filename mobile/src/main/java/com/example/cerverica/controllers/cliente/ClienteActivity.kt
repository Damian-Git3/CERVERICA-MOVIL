package com.example.cerverica.controllers.cliente

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.cerverica.BaseActivity
import com.example.cerverica.R
import com.example.cerverica.databinding.ActivityClienteBinding

class ClienteActivity : BaseActivity() {
    lateinit var binding: ActivityClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationInicio.setOnClickListener {
            loadFragment(ClienteInicioFragment())
        }
        binding.navigationWeb.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=h_GEuFmxsS8"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.navigationHistorial.setOnClickListener {
            loadFragment(ClienteComprasFragment())
        }
        binding.navigationCuenta.setOnClickListener {
            loadFragment(ClienteCuentaFragment())
        }
        binding.navigationFavoritos.setOnClickListener {
            loadFragment(ClienteFavoritosFragment())
        }
        if (savedInstanceState == null) {
            loadFragment(ClienteInicioFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
