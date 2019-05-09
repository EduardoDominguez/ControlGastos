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
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.adapter.AdaptadorDeudas
import mx.univa.controldegastos.adapter.AdaptadorIngresos
import mx.univa.controldegastos.model.Deuda
import mx.univa.controldegastos.model.response.ConsultaDeudasResponse
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.DeudasService
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
 * [FragmentDeudas.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentDeudas.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentDeudas : Fragment() {

    lateinit var service: DeudasService
    var fab : FloatingActionButton? = null

    var listaDeudas : List<Deuda> = ArrayList<Deuda>()
    var recyclerDeudas: RecyclerView?= null
    var adaptadorDeudas : AdaptadorDeudas? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var progress : ProgressBar? = null;

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
        var vista: View = inflater.inflate(R.layout.fragment_fragment_deudas, container, false)

        recyclerDeudas = vista.findViewById(R.id.recyclerDeudas) as? RecyclerView
        adaptadorDeudas = AdaptadorDeudas(deudaList = listaDeudas, context = context!!)
        linearLayoutManager = LinearLayoutManager(context)
        (recyclerDeudas as RecyclerView).layoutManager = linearLayoutManager

        fab = vista.findViewById(R.id.fab) as FloatingActionButton
        progress = vista.findViewById(R.id.progressDeudas) as? ProgressBar

        initOperations()
        initDeudas()
        return vista
    }

    fun initOperations() {
        fab?.setOnClickListener { view ->

            var actividad = activity as? PrincipalMain
            actividad?.CargarFragment(FragmentDeudasFrom())
        }
    }


    fun initDeudas(){
        var actividad = activity as? PrincipalMain
        var request = actividad?.obtieneIdUsuarioSesion() //Obtener Id Usuario

        progress?.visibility = View.VISIBLE
        service.getDeudasByUser(request!!).enqueue(object: Callback<ConsultaDeudasResponse> {
            override fun onResponse(call: Call<ConsultaDeudasResponse>?, response: Response<ConsultaDeudasResponse>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta!!.isExito()){
                    listaDeudas = respuesta.Deudas
                    adaptadorDeudas = AdaptadorDeudas(deudaList = listaDeudas, context = context!!)
                    (recyclerDeudas as RecyclerView).adapter = adaptadorDeudas
                    //Log.i(globales.TAG_LOGS, listaDeudas.toString())
                }/*else{
                        Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                 }*/

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
