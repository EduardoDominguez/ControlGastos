package mx.univa.controldegastos.service

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaAbonoRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AbonoService {
    @POST("Abono")
    fun creaAbono(@Body abono: CreaAbonoRequest): Call<Respuesta>
}