package mx.univa.controldegastos.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_fragment_inicio.*
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.model.response.ConsultaDeudasResponse
import mx.univa.controldegastos.model.response.ConsultaIngresosResponse
import mx.univa.controldegastos.model.response.TotalDeudasResponse
import mx.univa.controldegastos.model.response.TotalIngresosResponse
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.DeudasService
import mx.univa.controldegastos.service.IngresosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentInicio.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentInicio.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentInicio : Fragment() {

    lateinit var serviceIngresos: IngresosService
    lateinit var serviceDeudas: DeudasService

    var progress : ProgressBar? = null
    var totalIngresos : Double = 0.0
    var totalDeudas : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceDeudas = retrofit.create<DeudasService>(DeudasService::class.java)
        serviceIngresos = retrofit.create<IngresosService>(IngresosService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista : View  = inflater.inflate(R.layout.fragment_fragment_inicio, container, false)
        progress = vista.findViewById(R.id.progressHome) as? ProgressBar


        getTotalIngresos()
        return vista
    }

    fun getTotalDeudas(){
        var actividad = activity as? PrincipalMain
        var request = actividad?.obtieneIdUsuarioSesion() //Obtener Id Usuario

        if(request != null){
            progress?.visibility = View.VISIBLE
            serviceDeudas.getTotalDeudas(request!!).enqueue(object: Callback<TotalDeudasResponse> {
                override fun onResponse(call: Call<TotalDeudasResponse>?, response: Response<TotalDeudasResponse>?) {
                    val respuesta = response?.body()
                    //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))
                    val format = NumberFormat.getCurrencyInstance()
                    if(respuesta != null){
                        if(respuesta!!.isExito()){
                            val resuesta = respuesta.Total
                            val respuesta = respuesta?.Total

                            txv_cantidad_gastos?.text = format.format(respuesta).toString()
                            totalDeudas= respuesta

                            txv_diferencia?.text = format.format(totalIngresos - totalDeudas).toString()

                            //Log.i(globales.TAG_LOGS, resuesta.toString())
                        }else{

                            Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                        }
                    }else{
                        txv_cantidad_gastos?.text = format.format(0).toString()
                        Toast.makeText(activity, "No se pudo llamar al WS", Toast.LENGTH_SHORT).show()
                    }
                    progress?.visibility = View.GONE
                }
                override fun onFailure(call: Call<TotalDeudasResponse>?, t: Throwable?) {
                    t?.printStackTrace()
                    Toast.makeText(activity, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                    progress?.visibility = View.GONE
                }
            })
        }

    }

    fun getTotalIngresos(){
        var actividad = activity as? PrincipalMain
        var request = actividad?.obtieneIdUsuarioSesion() //Obtener Id Usuario
        progress?.visibility = View.VISIBLE
        serviceIngresos.getTotalIngresos(request!!).enqueue(object: Callback<TotalIngresosResponse> {
            override fun onResponse(call: Call<TotalIngresosResponse>?, response: Response<TotalIngresosResponse>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))
                val format = NumberFormat.getCurrencyInstance()
                if(respuesta != null) {
                    if (respuesta!!.isExito()) {
                        val respuesta = respuesta?.Total
                        txv_cantidad_ingresos?.text = format.format(respuesta).toString()
                        totalIngresos= respuesta
                        getTotalDeudas()
                        //Log.i(globales.TAG_LOGS, respuesta.toString())
                    } else {
                        Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                    }
                } else{
                    txv_cantidad_ingresos?.text = format.format(0).toString()
                    Toast.makeText(activity, "No se pudo llamar al WS", Toast.LENGTH_SHORT).show()
                }

                progress?.visibility = View.GONE

            }
            override fun onFailure(call: Call<TotalIngresosResponse>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                progress?.visibility = View.GONE
            }
        })
    }

}
