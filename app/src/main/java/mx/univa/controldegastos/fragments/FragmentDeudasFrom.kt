package mx.univa.controldegastos.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_fragment_deudas_from.*
import kotlinx.android.synthetic.main.fragment_fragment_ingresos_form.*
import kotlinx.android.synthetic.main.fragment_fragment_ingresos_form.edt_nombre
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaDeudaRequest
import mx.univa.controldegastos.model.request.CreaIngresoRequest
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.DeudasService
import mx.univa.controldegastos.service.IngresosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentDeudasFrom.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentDeudasFrom.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentDeudasFrom : Fragment() {

    var btnRegistrar : Button? = null
    lateinit var service: DeudasService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<DeudasService>(DeudasService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista : View = inflater.inflate(R.layout.fragment_fragment_deudas_from, container, false)
        btnRegistrar = vista.findViewById(R.id.btn_registrar_deuda) as? Button

        btnRegistrar?.setOnClickListener({
            GuardarDeuda()
        })
        return vista
    }

    fun GuardarDeuda(){
        var actividad = activity as? PrincipalMain
        var request = CreaDeudaRequest()
        request.IdUsuario = actividad!!.obtieneIdUsuarioSesion() //Obtener Id Usuario
        request.Nombre = edt_nombre?.text.toString()
        request.DiaCorte = edt_dia_corte?.text.toString().toInt()
        request.DiaLimitePago = edt_dia_limite_pago?.text.toString().toInt()

        var progress = progressAddDeudas
        progress.visibility = View.VISIBLE
        service.creaDeuda(request).enqueue(object: Callback<Respuesta> {

            override fun onResponse(call: Call<Respuesta>?, response: Response<Respuesta>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta!!.isExito()){
                    var actividad = activity as? PrincipalMain
                    actividad?.CargarFragment(FragmentDeudas())
                }
                Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                progress.visibility = View.GONE
            }
            override fun onFailure(call: Call<Respuesta>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                progress.visibility = View.GONE
            }
        })
    }
}
