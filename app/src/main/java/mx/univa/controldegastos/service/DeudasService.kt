package mx.univa.controldegastos.service

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaDeudaRequest
import mx.univa.controldegastos.model.response.ConsultaDeudasResponse
import mx.univa.controldegastos.model.response.TotalDeudasResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DeudasService {
    @POST("Deudas")
    fun creaDeuda(@Body ingreso: CreaDeudaRequest): Call<Respuesta>

    @GET("Deudas")
    fun getDeudas(): Call<ConsultaDeudasResponse>

    @GET("Deudas/ByUser/{idUsuario}")
    fun getDeudasByUser(@Path("idUsuario") idUsuario: Int): Call<ConsultaDeudasResponse>

    @GET("Deudas/Sumatoria/{idUsuario}")
    fun getTotalDeudas(@Path("idUsuario") idUsuario: Int) : Call<TotalDeudasResponse>
}