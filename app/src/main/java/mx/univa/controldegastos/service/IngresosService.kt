package mx.univa.controldegastos.service

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaUsuarioRequest
import mx.univa.controldegastos.model.request.LoginRequest
import mx.univa.controldegastos.model.response.ConsultaIngresosResponse
import mx.univa.controldegastos.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IngresosService {

    @POST("Ingresos")
    fun creaIngreso(@Body login: LoginRequest): Call<LoginResponse>

    @GET("Ingresos")
    fun getIngresos(@Body usuario: CreaUsuarioRequest): Call<Respuesta>

    @GET("Ingresos/{idUsuario}")
    fun getIngresosByUser(@Path("idUsuario") idUsuario: Int) : Call<ConsultaIngresosResponse>


}