package mx.univa.controldegastos.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_fragment_abonos.*
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.model.Deuda
import mx.univa.controldegastos.model.Ingreso
import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaAbonoRequest
import mx.univa.controldegastos.model.request.CreaCargoRequest
import mx.univa.controldegastos.model.response.ConsultaDeudasResponse
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.AbonoService
import mx.univa.controldegastos.service.CargoService
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
 * [FragmentCargos.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentCargos.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentCargos : Fragment() {

    lateinit var serviceDeudas: DeudasService
    lateinit var serviceCargos: CargoService
    var listaDeuda: List<Deuda> = ArrayList()

    var spinnerDeudas: Spinner? = null
    var btnRegistrar : Button? = null

    var progress : ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceDeudas = retrofit.create<DeudasService>(DeudasService::class.java)
        serviceCargos = retrofit.create<CargoService>(CargoService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista : View = inflater.inflate(R.layout.fragment_fragment_cargos, container, false)

        spinnerDeudas = vista.findViewById(R.id.spinner_deudas) as? Spinner
        btnRegistrar = vista.findViewById(R.id.btn_registrar_cargo) as? Button

        progress = vista.findViewById(R.id.progressCargos) as? ProgressBar
        btnRegistrar?.setOnClickListener({
            if(Validar())
                GuardarCargo()
        })
        initDeudas()
        return vista
    }

    private fun GuardarCargo() {
        var actividad = activity as? PrincipalMain
        var request = CreaCargoRequest()

        var deuda : Deuda =  spinnerDeudas?.getSelectedItem() as Deuda

        request.IdDeuda = deuda.iddeuda
        request.IdUsuario = actividad!!.obtieneIdUsuarioSesion() //Obtener Id Usuario
        request.Cantidad = edt_cantidad?.text.toString().toDouble()

        progress?.visibility = View.VISIBLE
        serviceCargos.creaCargo(request).enqueue(object: Callback<Respuesta> {

            override fun onResponse(call: Call<Respuesta>?, response: Response<Respuesta>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta!!.isExito()){
                    var actividad = activity as? PrincipalMain
                    actividad?.CargarFragment(FragmentInicio())
                }
                Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                progress?.visibility = View.GONE
            }
            override fun onFailure(call: Call<Respuesta>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                progress?.visibility = View.GONE
            }
        })
    }

    fun Validar():Boolean{

        var resultado = true

        if(edt_cantidad?.text.toString().isNullOrEmpty()) {
            Toast.makeText(activity, "Agrega cantidad del cargo", Toast.LENGTH_LONG).show()
            resultado = false
        }

        if(spinnerDeudas?.getSelectedItem() == null){
            Toast.makeText(activity, "Selecciona una deuda", Toast.LENGTH_LONG).show()
            resultado = false
        }

        return resultado
    }

    fun initDeudas(){
        var actividad = activity as? PrincipalMain
        var request = actividad?.obtieneIdUsuarioSesion() //Obtener Id Usuario

        progress?.visibility = View.VISIBLE
        serviceDeudas.getDeudasByUser(request!!).enqueue(object: Callback<ConsultaDeudasResponse> {
            override fun onResponse(call: Call<ConsultaDeudasResponse>?, response: Response<ConsultaDeudasResponse>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta!!.isExito()){
                    listaDeuda = respuesta.Deudas
                    //val dataSource = arrayListOf(listaDeuda)

                    val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listaDeuda)
                    spinnerDeudas?.adapter = arrayAdapter

                    //Log.i(globales.TAG_LOGS, listaDeuda.toString())
                }else{
                    Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                }

                progress?.visibility = View.GONE
            }
            override fun onFailure(call: Call<ConsultaDeudasResponse>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                progress?.visibility = View.GONE
            }
        })
    }
}
