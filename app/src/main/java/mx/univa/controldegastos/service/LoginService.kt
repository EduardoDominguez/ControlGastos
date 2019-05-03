package mx.univa.controldegastos.service

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaUsuarioRequest
import mx.univa.controldegastos.model.request.LoginRequest
import mx.univa.controldegastos.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {

    @POST("Login")
    fun getLogin(@Body login: LoginRequest ): Call<LoginResponse>

    @POST("Usuarios")
    fun creaUsuario(@Body usuario: CreaUsuarioRequest): Call<Respuesta>
}