package mx.univa.controldegastos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import mx.univa.controldegastos.resourses.globales

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        if(bundle != null)
            getData(bundle)
        else if(!obtieneEstadoSesion()){
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    fun getData(bundle: Bundle){

        if (bundle.get("estado_sesion") != null){
            val estado_sesion = bundle.get("estado_sesion") as Boolean
            guardaEstadoSesion(estado_sesion)
            Toast.makeText(this, bundle.get("usuario").toString(), Toast.LENGTH_LONG).show()
        }

        //tvGreeting.text = getString(R.string.welcome, name)
    }

    fun guardaEstadoSesion(pEstadoSesion: Boolean){
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(globales.PREFERENCE_ESTADO_SESION, pEstadoSesion).apply()
    }

    fun obtieneEstadoSesion(): Boolean {
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(globales.PREFERENCE_ESTADO_SESION, false)
    }


}
