package mx.univa.controldegastos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.LoginRequest
import mx.univa.controldegastos.model.response.LoginResponse
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.ApiService
import mx.univa.controldegastos.service.LoginService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private var txtUser: EditText? = null
    private var txtPassword: EditText? = null

    lateinit var service: ApiService
    lateinit var serviceLogin: LoginService


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtUser = findViewById(R.id.edt_user) as EditText
        txtPassword = findViewById(R.id.edt_password) as EditText

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        service = retrofit.create<ApiService>(ApiService::class.java)
        serviceLogin = retrofit.create<LoginService>(LoginService::class.java)
    }

    fun IniciarSesion(view :View){
        if(txtUser?.text.toString().trim().equals(""))
            txtUser?.error = "Ingrese usuario"
            //Toast.makeText(this, "Ingrese usuario", Toast.LENGTH_LONG).show()
        else if(txtPassword?.text.toString().trim().equals(""))
            txtPassword?.error = "Ingrese contraseña"
        else{
            progressBar.visibility = View.VISIBLE

            getLogin()

            //getPing()

        }
    }

    fun Registrar(view :View){
        var i = Intent(this, RegistroUsuario::class.java)
        startActivity(i)
    }

    fun getPing(){
        val contexto = this
        service.getPing().enqueue(object: Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                val ping = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(ping))
            }
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(contexto, "No se pudo obtener acceso a internet, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getLogin(){

        var request = LoginRequest()
        request.setUsername(txtUser?.text.toString().trim())
        request.setPassword(txtPassword?.text.toString().trim())


        var i = Intent(this, PrincipalMain::class.java)
        val contexto = this
        serviceLogin.getLogin(request).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))
                progressBar.visibility = View.GONE
                if(respuesta!!.isExito()){
                    i.putExtra("estado_sesion", true)
                    i.putExtra("usuario", txtUser?.text)
                    i.putExtra("correo", respuesta.usuario.correo)
                    i.putExtra("nombre_completo", "${respuesta.usuario.nombre} ${respuesta.usuario.appaterno} ${respuesta.usuario.apmaterno}")
                    i.putExtra("id_usuario", respuesta.usuario.idusuario)
                    startActivity(i)
                    finish()
                }else{
                    Toast.makeText(contexto, respuesta?.getMensaje(), Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                progressBar.visibility = View.GONE
                t?.printStackTrace()
                Toast.makeText(contexto, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
            }
        })

    }

}
