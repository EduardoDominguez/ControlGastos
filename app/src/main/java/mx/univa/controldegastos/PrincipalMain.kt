package mx.univa.controldegastos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_principal_main.*
import kotlinx.android.synthetic.main.app_bar_principal_main.*
import mx.univa.controldegastos.fragments.*
import mx.univa.controldegastos.resourses.globales

class PrincipalMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var txtUserHeader: TextView? = null
    private var txtCorreoHeader: TextView? = null
    private var navBar: NavigationView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navBar = findViewById(R.id.nav_view) as? NavigationView
        val hView = navBar?.getHeaderView(0)
        txtUserHeader = hView?.findViewById(R.id.txv_nombre_usuario_header) as? TextView
        txtCorreoHeader = hView?.findViewById(R.id.txv_correo_header) as? TextView

        nav_view.setNavigationItemSelectedListener(this)


        val bundle = intent.extras
        if(bundle != null && bundle.get("estado_sesion") != null){
            getData(bundle)
            CargarFragment(FragmentInicio())
            nav_view.menu.getItem(0).setChecked(true)
        }
        else if(!obtieneEstadoSesion()){
            enviarALogin()
        }else{
            //txtUserHeader?.text = obtieneCorreoSesion()
            //txtCorreoHeader?.text = obtieneNombreCompletoSesion()
            CargarFragment(FragmentInicio())
            nav_view.menu.getItem(0).setChecked(true)

        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.principal_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            //R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                CargarFragment(FragmentInicio())
            }
            R.id.nav_ingresos -> {
                // Handle the camera action
                CargarFragment(FragmentIngresos())
            }
            /*R.id.nav_gastos -> {
                CargarFragment(FragmentGastos())
            }*/
            R.id.nav_deudas -> {
                CargarFragment(FragmentDeudas())
            }
            R.id.nav_abonos -> {
                CargarFragment(FragmentAbonos())
            }
            R.id.nav_cargos -> {
                CargarFragment(FragmentCargos())
            }
            /*R.id.nav_password -> {

            }*/
            R.id.nav_cerrar_sesion -> {
                guardaEstadoSesion(false)
                enviarALogin()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getData(bundle: Bundle){

        if (bundle.get("estado_sesion") != null){
            val estado_sesion = bundle.get("estado_sesion") as Boolean

            var correo:String = ""
            var nombre_completo:String = ""
            var id_usuario : Int = 0

            if(!bundle.get("correo").toString().isNullOrEmpty())
                correo = bundle.get("correo").toString()

            if(!bundle.get("nombre_completo").toString().isNullOrEmpty())
                nombre_completo = bundle.get("nombre_completo").toString()


            if(!bundle.get("id_usuario").toString().isNullOrEmpty())
                id_usuario = Integer.parseInt(bundle.get("id_usuario").toString())


            guardaEstadoSesion(estado_sesion, id_usuario, bundle.get("usuario").toString(), correo, nombre_completo)

            txtCorreoHeader?.text = obtieneCorreoSesion()
            txtUserHeader?.text = obtieneNombreCompletoSesion()

        }
    }

    fun guardaEstadoSesion(pEstadoSesion: Boolean, pIdUsuario: Int = 0, pNombreUsuario: String  = "", pCorreo:String = "", pNombreCompleto:String =""){
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(globales.PREFERENCE_ESTADO_SESION, pEstadoSesion).apply()
        preferences.edit().putString(globales.PREFERENCE_USUARIO, pNombreUsuario).apply()
        preferences.edit().putString(globales.PREFERENCE_CORREO, pCorreo).apply()
        preferences.edit().putString(globales.PREFERENCE_NOMBRE, pNombreCompleto).apply()
        preferences.edit().putInt(globales.PREFERENCE_ID_USUARIO, pIdUsuario).apply()
    }

    fun obtieneEstadoSesion(): Boolean {
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(globales.PREFERENCE_ESTADO_SESION, false)
    }

    fun obtieneUsuarioSesion(): String {
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getString(globales.PREFERENCE_USUARIO, "")
    }

    fun obtieneIdUsuarioSesion(): Int {
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getInt(globales.PREFERENCE_ID_USUARIO, 0)
    }

    fun obtieneNombreCompletoSesion(): String {
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getString(globales.PREFERENCE_NOMBRE, "")
    }

    fun obtieneCorreoSesion(): String {
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getString(globales.PREFERENCE_CORREO, "")
    }

    fun enviarALogin(){
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    fun CargarFragment(fragmento: Fragment){
        /*var fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contenedor, fragmento).commit()
        */
        val transaction = getSupportFragmentManager().beginTransaction()
        transaction.replace(R.id.contenedor, fragmento)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun onFragmentInteraction(uri: Uri) {}
}
