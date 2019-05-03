package mx.univa.controldegastos.service

import retrofit2.http.GET
import mx.univa.controldegastos.resourses.globales
import retrofit2.Call
//${globales.URL_WS}

interface ApiService {
    @GET("Autenticar/echoping")
    fun getPing(): Call<Boolean>
}