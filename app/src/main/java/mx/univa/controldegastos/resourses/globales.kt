package mx.univa.controldegastos.resourses

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class globales {

    companion object {
        val STRING_PREFERENCES = "mx.univa.controldegastos"
        val PREFERENCE_ESTADO_SESION = "ESTADO.SESION"
        val PREFERENCE_USUARIO = "USUARIO.SESION"
        val PREFERENCE_CORREO = "CORREO.SESION"
        val PREFERENCE_NOMBRE = "NOMBRE.SESION"
        val PREFERENCE_ID_USUARIO = "IDUSUARIO.SESION"
        val PREFERENCE_TOKEN_FIREBASE = "TOKEN.SESION"
        val URL_WS = "http://www.reddam.com.mx/servicios/WSDeudas/api/"
        //val URL_WS = "http://192.168.1.72:58991/api/"
        val TAG_LOGS = "DEUDAS: "
    }

    fun getService() : Retrofit{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        return retrofit
    }

}