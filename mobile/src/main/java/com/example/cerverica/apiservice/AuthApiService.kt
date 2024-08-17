package com.example.cerverica.apiservice


import com.example.cerverica.models.LoginRequest
import com.example.cerverica.models.LoginResponse
import com.example.cerverica.models.NotificacionModel
import com.example.cerverica.models.Pedido
import com.example.cerverica.models.cliente.RecetaModel
import com.example.cerverica.models.RegisterRequest
import com.example.cerverica.models.RegisterResponse
import com.example.cerverica.models.cliente.AccountModel
import com.example.cerverica.models.cliente.RecetaAgregarFavoritoRequest
import com.example.cerverica.models.cliente.RecetaEliminarFavoritoRequest
import com.example.cerverica.models.cliente.RecetaFavoritoModel
import com.example.cerverica.models.cliente.RecetaPackModel
import com.example.cerverica.models.cliente.VentaModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApiService {
    @POST("Account/login")
    fun postLogin(@Body params: LoginRequest): Call<LoginResponse>

    @POST("Account/register")
    fun postRegister(@Body params: RegisterRequest): Call<RegisterResponse>

    @POST("Account/logout")
    fun postLogout(): Call<ResponseBody>

    @GET("Account/detail")
    fun postAccountDetail(): Call<AccountModel>

    @GET("Receta")
    fun getRecetas():Call<List<RecetaModel>>

    @GET("Receta/obtener-recetas-landing")
    fun getRecetasPack():Call<List<RecetaPackModel>>

    @GET("Favoritos/obtener-favoritos/{id}")
    fun getRecetasFavoritos(@Path("id") id: String):Call<List<RecetaFavoritoModel>>

    @POST("Favoritos/agregar-favorito")
    fun postAgregarFavoritos(@Body recetaRequest: RecetaAgregarFavoritoRequest):Call<ResponseBody>

    @POST("Favoritos/eliminar-favorito")
    fun postEliminarFavoritos(@Body recetaRequest: RecetaEliminarFavoritoRequest):Call<ResponseBody>

    @GET("Ventas/cliente")
    fun getClientBuys(): Call<List<VentaModel>>

    @GET("Ventas/pedidos")
    fun getPedidos():Call<List<Pedido>>

    @GET("Ventas/pedidos/{id}")
    fun getPedido(@Path("id") idPedido: Int): Call<Pedido>

    @GET("Ventas/siguiente-estatus/{id}")
    fun marcarSiguienteEstatus(@Path("id") idPedido: Int): Call<Void>

    @GET("Notificacion")
    fun getNotificaciones() : Call<List<NotificacionModel>>

    @PUT("Notificacion/{id}")
    fun putNotificacionVista(@Path("id") id: Int) : Call<ResponseBody>
}