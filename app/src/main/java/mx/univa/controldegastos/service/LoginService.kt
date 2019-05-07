package mx.univa.controldegastos.service

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaUsuarioRequest
import mx.univa.controldegastos.model.request.LoginRequest
import mx.univa.controldegastos.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @Headers("Content-Type: application/json")
    @POST("Login")
    fun getLogin(@Body login: LoginRequest ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("Usuarios")
    fun creaUsuario(@Body usuario: CreaUsuarioRequest): Call<Respuesta>
}