package mx.univa.controldegastos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaUsuarioRequest
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroUsuario : AppCompatActivity() {

    lateinit var service: LoginService

    private var txtNombre: EditText? = null
    private var txtAppaterno: EditText? = null
    private var txtApmaterno: EditText? = null
    private var txtEmail: EditText? = null
    private var txtNick: EditText? = null
    private var txtPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)


        txtNombre = findViewById(R.id.edt_nombre) as EditText
        txtAppaterno = findViewById(R.id.edt_ap_paterno) as EditText
        txtApmaterno = findViewById(R.id.edt_ap_materno) as EditText
        txtEmail = findViewById(R.id.edt_correo) as EditText
        txtNick = findViewById(R.id.edt_nick) as EditText
        txtPassword = findViewById(R.id.edt_password) as EditText

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<LoginService>(LoginService::class.java)
    }

    fun CreaUsuario(view: View){
        if(ValidaFormulario()){
            var request = CreaUsuarioRequest()

            request.Nombre=(txtNombre?.text.toString().trim())
            request.Apepaterno=(txtAppaterno?.text.toString().trim())
            request.Apematerno=(txtApmaterno?.text.toString().trim())
            request.Correo=(txtEmail?.text.toString().trim())
            request.Nick=(txtNick?.text.toString().trim())
            request.Password=(txtPassword?.text.toString().trim())


            var i = Intent(this, LoginActivity::class.java)
            val contexto = this
            service.creaUsuario(request).enqueue(object: Callback<Respuesta> {
                override fun onResponse(call: Call<Respuesta>?, response: Response<Respuesta>?) {
                    val respuesta = response?.body()
                    //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                    if(respuesta!!.isExito()){
                        Toast.makeText(contexto, "Registrado con éxito", Toast.LENGTH_LONG).show()
                        startActivity(i)
                        finish()
                    }else{
                        Toast.makeText(contexto, respuesta?.getMensaje(), Toast.LENGTH_LONG).show()
                    }

                }
                override fun onFailure(call: Call<Respuesta>?, t: Throwable?) {
                    t?.printStackTrace()
                    Toast.makeText(contexto, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    fun ValidaFormulario() : Boolean{
        var blnRespuesta = true

        if(txtNombre?.text.toString().trim().isNullOrEmpty()){
            //Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_LONG).show()
            txtNombre?.error = "Ingrese su nombre"
            blnRespuesta = false
        }

        if(txtAppaterno?.text.toString().trim().isNullOrEmpty()){
            //Toast.makeText(this, "Ingrese su apellido paterno", Toast.LENGTH_LONG).show()
            txtAppaterno?.error = "Ingrese su apellido paterno"
            blnRespuesta = false
        }


        if(txtApmaterno?.text.toString().trim().isNullOrEmpty()){
            //Toast.makeText(this, "Ingrese su apellido materno", Toast.LENGTH_LONG).show()
            txtApmaterno?.error = "Ingrese su apellido materno"
            blnRespuesta = false
        }


        if(txtEmail?.text.toString().trim().isNullOrEmpty()){
            //Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_LONG).show()
            txtEmail?.error = "Ingrese su correo"
            blnRespuesta = false
        }

        if(txtNick?.text.toString().trim().isNullOrEmpty()){
            //Toast.makeText(this, "Ingrese su nombre de usuario (nick)", Toast.LENGTH_LONG).show()
            txtNick?.error = "Ingrese su nombre de usuario (nick)"
            blnRespuesta = false
        }

        if(txtPassword?.text.toString().trim().isNullOrEmpty()){
            //Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_LONG).show()
            txtPassword?.error = "Ingrese su contraseña"
            blnRespuesta = false
        }

        return blnRespuesta
    }
}
