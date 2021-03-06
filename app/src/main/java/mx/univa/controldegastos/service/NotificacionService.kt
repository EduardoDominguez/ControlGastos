package mx.univa.controldegastos.service

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.ActualizaTokenNotificacionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface NotificacionService {

    @Headers("Content-Type: application/json")
    @PUT("Notificacion")
    fun actualizaToken(@Body token: ActualizaTokenNotificacionRequest): Call<Respuesta>
}