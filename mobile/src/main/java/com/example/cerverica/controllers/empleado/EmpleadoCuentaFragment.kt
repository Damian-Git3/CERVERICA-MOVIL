package com.example.cerverica.controllers.empleado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cerverica.LoginActivity
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.databinding.FragmentCuentaClienteBinding
import com.example.cerverica.models.cliente.AccountModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmpleadoCuentaFragment : Fragment() {
    private lateinit var binding: FragmentCuentaClienteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCuentaClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetalles()

        binding.logoutButton.setOnClickListener {
            logout()
        }


    }

    fun logout(){
        RetrofitClient.instance.postLogout().enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (!response.isSuccessful) {
                    val error = "Advertencia: ${response.errorBody()?.string()}"
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("logout", "onResponse: ${response.body()}")
                    Toast.makeText(context, "Adiós", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val error = "Fallo en la consulta: ${t.message}"
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun initDetalles(){
        RetrofitClient.instance.postAccountDetail().enqueue(object :
            Callback<AccountModel> {
            override fun onResponse(call: Call<AccountModel>, response: Response<AccountModel>) {

                if (!response.isSuccessful) {
                    val error = "Advertencia: ${response.errorBody()?.string()}"
                    Log.d("Detalles", "onResponse: ${response}")
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }else{
                    binding.fullName.text = response.body()?.nombre
                    binding.email.text = response.body()?.email
                    binding.tipoCuenta.text = response.body()?.roles?.get(0) ?: "como llegó uste aqui?"
                }
            }

            override fun onFailure(call: Call<AccountModel>, t: Throwable) {
                val error = "Fallo en la consulta: ${t.message}"
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })
    }
}