package mx.univa.controldegastos.service

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaCargoRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CargoService {
    @POST("Cargos")
    fun creaCargo(@Body cargo: CreaCargoRequest): Call<Respuesta>


}