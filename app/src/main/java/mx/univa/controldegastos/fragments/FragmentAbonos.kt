package mx.univa.controldegastos.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_fragment_abonos.*
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.adapter.AdaptadorDeudas
import mx.univa.controldegastos.adapter.AdaptadorIngresos
import mx.univa.controldegastos.model.Deuda
import mx.univa.controldegastos.model.Ingreso
import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaAbonoRequest
import mx.univa.controldegastos.model.request.CreaDeudaRequest
import mx.univa.controldegastos.model.response.ConsultaDeudasResponse
import mx.univa.controldegastos.model.response.ConsultaIngresosResponse
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.AbonoService
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
 * [FragmentAbonos.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentAbonos.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentAbonos : Fragment() {


    lateinit var serviceIngresos: IngresosService
    lateinit var serviceDeudas: DeudasService
    lateinit var serviceAbono: AbonoService

    var listaIngresos: List<Ingreso> = ArrayList()
    var listaDeuda: List<Deuda> = ArrayList()

    var spinnerIngresos: Spinner? = null
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
        serviceIngresos = retrofit.create<IngresosService>(IngresosService::class.java)
        serviceAbono = retrofit.create<AbonoService>(AbonoService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista : View = inflater.inflate(R.layout.fragment_fragment_abonos, container, false)
        spinnerIngresos = vista.findViewById(R.id.spinner_ingresos) as? Spinner
        spinnerDeudas = vista.findViewById(R.id.spinner_deudas) as? Spinner
        btnRegistrar = vista.findViewById(R.id.btn_registrar_abono) as? Button

        progress = vista.findViewById(R.id.progressAbonos) as? ProgressBar
        btnRegistrar?.setOnClickListener({
            if(Validar())
                GuardarAbono()
        })
        initIngresos()
        initDeudas()
        return vista
    }

    fun Validar():Boolean{

        var resultado = true

        if(edt_cantidad?.text.toString().isNullOrEmpty()){
            Toast.makeText(activity, "Agrega cantidad", Toast.LENGTH_LONG).show()
            resultado = false
        }

        if(spinnerIngresos?.getSelectedItem() == null){
            Toast.makeText(activity, "Selecciona un ingreso", Toast.LENGTH_LONG).show()
            resultado = false
        }

        if(spinnerDeudas?.getSelectedItem() == null){
            Toast.makeText(activity, "Selecciona una deuda", Toast.LENGTH_LONG).show()
            resultado = false
        }

        return resultado
    }

    fun GuardarAbono(){

        var actividad = activity as? PrincipalMain
        var request = CreaAbonoRequest()

        var ingreso : Ingreso =  spinnerIngresos?.getSelectedItem() as Ingreso
        var deuda : Deuda =  spinnerDeudas?.getSelectedItem() as Deuda

        request.IdUsuario = actividad!!.obtieneIdUsuarioSesion() //Obtener Id Usuario
        request.Cantidad = edt_cantidad?.text.toString().toDouble()
        request.IdIngreso = ingreso.idingreso
        request.IdDeuda = deuda.iddeuda

        progress?.visibility = View.VISIBLE

        serviceAbono.creaAbono(request).enqueue(object: Callback<Respuesta> {

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

                    //Log.i("globales.TAG_LOGS", listaDeuda.toString())
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

    fun initIngresos(){
        var actividad = activity as? PrincipalMain
        var request = actividad?.obtieneIdUsuarioSesion() //Obtener Id Usuario
        progress?.visibility = View.VISIBLE
        serviceIngresos.getIngresosByUser(request!!).enqueue(object: Callback<ConsultaIngresosResponse> {
            override fun onResponse(call: Call<ConsultaIngresosResponse>?, response: Response<ConsultaIngresosResponse>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta!!.isExito()){
                    listaIngresos = respuesta.Ingresos
                    val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listaIngresos)
                    spinnerIngresos?.adapter = arrayAdapter

                    //Log.i("globales.TAG_LOGS", listaIngresos.toString())
                }else{
                    Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                }

                progress?.visibility = View.GONE

            }
            override fun onFailure(call: Call<ConsultaIngresosResponse>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                progress?.visibility = View.GONE
            }
        })
    }
}
