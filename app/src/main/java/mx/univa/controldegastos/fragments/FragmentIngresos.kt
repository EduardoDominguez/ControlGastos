package mx.univa.controldegastos.fragments

import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import com.google.gson.Gson
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.adapter.AdaptadorIngresos
import mx.univa.controldegastos.model.Ingreso
import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.response.ConsultaIngresosResponse
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.IngresosService
import mx.univa.controldegastos.service.LoginService
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null


    lateinit var service: IngresosService
    var fab : FloatingActionButton? = null

    var listaIngresos: List<Ingreso> = ArrayList<Ingreso>()
    var recyclerIngresos: RecyclerView?= null
    var adaptadorIngreos : AdaptadorIngresos? = null
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<IngresosService>(IngresosService::class.java)
    }

    override fun onResume() {
        super.onResume()
        //OnResume Fragment
        initIngresos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista: View = inflater.inflate(R.layout.fragment_fragment_ingresos, container, false)
        recyclerIngresos = vista.findViewById(R.id.recyclerIngresos)  as RecyclerView
        adaptadorIngreos = AdaptadorIngresos(ingresoList = listaIngresos, context = context!!)
        linearLayoutManager = LinearLayoutManager(context)
        (recyclerIngresos as RecyclerView).layoutManager = linearLayoutManager

        fab = vista.findViewById(R.id.fab) as FloatingActionButton
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
        var request = PrincipalMain().obtieneIdUsuarioSesion() //Obtener Id Usuario
        service.getIngresosByUser(request).enqueue(object: Callback<ConsultaIngresosResponse> {
            override fun onResponse(call: Call<ConsultaIngresosResponse>?, response: Response<ConsultaIngresosResponse>?) {
                val respuesta = response?.body()
                Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta!!.isExito()){
                    listaIngresos = respuesta.Ingresos
                    adaptadorIngreos = AdaptadorIngresos(ingresoList = listaIngresos, context = context!!)
                    (recyclerIngresos as RecyclerView).adapter = adaptadorIngreos
                    Log.i("DATA:", listaIngresos.toString())
                }

            }
            override fun onFailure(call: Call<ConsultaIngresosResponse>?, t: Throwable?) {
                t?.printStackTrace()
                //Toast.makeText(contexto, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
            }
        })

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }*/

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentIngresos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentIngresos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
