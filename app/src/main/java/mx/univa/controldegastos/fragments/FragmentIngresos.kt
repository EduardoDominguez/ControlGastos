package mx.univa.controldegastos.fragments

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
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_fragment_ingresos.*
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.adapter.AdaptadorIngresos
import mx.univa.controldegastos.model.Ingreso
import mx.univa.controldegastos.model.response.ConsultaIngresosResponse
import mx.univa.controldegastos.resourses.globales
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
 * [FragmentIngresos.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentIngresos.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentIngresos : Fragment() {

    lateinit var service: IngresosService
    var fab : FloatingActionButton? = null

    var listaIngresos: List<Ingreso> = ArrayList<Ingreso>()
    var recyclerIngresos: RecyclerView?= null
    var adaptadorIngreos : AdaptadorIngresos? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var progress : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<IngresosService>(IngresosService::class.java)
    }

    override fun onResume() {
        super.onResume()
        //OnResume Fragment
        //initIngresos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista: View = inflater.inflate(R.layout.fragment_fragment_ingresos, container, false)
        recyclerIngresos = vista.findViewById(R.id.recyclerIngresos) as? RecyclerView
        adaptadorIngreos = AdaptadorIngresos(ingresoList = listaIngresos, context = context!!)
        linearLayoutManager = LinearLayoutManager(context)
        (recyclerIngresos as RecyclerView).layoutManager = linearLayoutManager

        fab = vista.findViewById(R.id.fab) as FloatingActionButton
        progress = vista.findViewById(R.id.progressIngresos) as? ProgressBar
        initIngresos()
        initOperations()

        return vista

    }

    fun initOperations() {
        fab?.setOnClickListener { view ->

            var actividad = activity as? PrincipalMain
            actividad?.CargarFragment(FragmentIngresosForm())
        } }

    fun initIngresos(){
        var actividad = activity as? PrincipalMain
        var request = actividad?.obtieneIdUsuarioSesion() //Obtener Id Usuario

        progress?.visibility = View.VISIBLE
        service.getIngresosByUser(request!!).enqueue(object: Callback<ConsultaIngresosResponse> {
            override fun onResponse(call: Call<ConsultaIngresosResponse>?, response: Response<ConsultaIngresosResponse>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta != null){
                    if(respuesta!!.isExito()){
                        listaIngresos = respuesta.Ingresos
                        adaptadorIngreos = AdaptadorIngresos(ingresoList = listaIngresos, context = context!!)
                        (recyclerIngresos as RecyclerView).adapter = adaptadorIngreos
                        //Log.i(globales.TAG_LOGS, listaIngresos.toString())
                    }/*else{
                        Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                    }*/
                }else{
                    Toast.makeText(activity, "No se puedo llamar el servicio", Toast.LENGTH_LONG).show()
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
