/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.ui.insetos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.databinding.FragmentResultadoBinding
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import pt.ipbeja.tp21.data.Inseto
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ResultadoFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: InsetoViewModel by navGraphViewModels(R.id.classificar)

    private lateinit var binding: FragmentResultadoBinding
    private lateinit var mMap: GoogleMap

    val beja = LatLng(38.01506, -7.86323)
    private val resultsList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentResultadoBinding.inflate(inflater).let {
        this.binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.tvOrdem.movementMethod = ScrollingMovementMethod()
        binding.tvDescription.movementMethod = ScrollingMovementMethod()
        binding.tvQuestoes.movementMethod = ScrollingMovementMethod()


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext(), R.style.DialogColor)
                    .setTitle(R.string.pagina_anterior)
                    .setIcon(R.drawable.icon_app)
                    .setMessage(R.string.voltar_classificar)
                    .setPositiveButton(R.string.voltar) { _, _ -> back() }
                    .setNegativeButton(R.string.cancelar) { _, _ ->  }
                    .show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        with(binding.ivReferencia) {
            viewModel.photoRef?.let { load(it) }
        }

        binding.tvDescription.text = viewModel.descriptionInsect

        binding.tvOrdem.text = viewModel.ordemInsect

        //val questao = R.string.questao.toString()

        for (i in 0 until viewModel.choicesList.size) {
            resultsList.add("• Questão " + (i+1) + " :\n" + viewModel.choicesList[i] + "\n")
        }

        binding.tvQuestoes.text = resultsList.joinToString()

        binding.btnCancel.setOnClickListener {
            AlertDialog.Builder(requireContext(), R.style.DialogColor)
                .setTitle(R.string.pagina_inicial_titulo)
                .setIcon(R.drawable.icon_app)
                .setMessage(R.string.pagina_inicial)
                .setPositiveButton(R.string.sim) { _, _ -> cancel() }
                .setNegativeButton(R.string.cancelar) { _, _ ->  }
                .show()

        }

        binding.btnSave.setOnClickListener {
            //viewModelNotificacao.notification = 1
            insertDataToDatabase()
        }

        binding.ivReferencia.setOnClickListener {
            resultsList.clear()
            findNavController().navigate(ResultadoFragmentDirections.actionResultadoFragmentToConsultarImgRefFragment())
        }

    }

    private fun insertDataToDatabase() {
        val ordem = viewModel.ordemInsect.toString()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val dataHora = Calendar.getInstance()
        val data = sdf.format(dataHora.time).toString()
        val descricao = viewModel.descriptionInsect.toString()
        val questoes = resultsList.joinToString()
        val localizacao = viewModel.location.toString()

        /*Pval path = requireContext().filesDir

        try {
            val encoded = Files.readAllBytes(Paths.get(path))
            println(Arrays.toString(encoded))
        } catch (e: IOException) {

        }*/

        val inseto = Inseto(ordem, data, descricao, questoes, localizacao)
        viewModel.addInseto(inseto)


        Toast.makeText(context,R.string.inseto_adicionado,Toast.LENGTH_SHORT).show()
        findNavController().navigate(ResultadoFragmentDirections.actionGlobalMainFragment())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        mMap.addMarker(
            MarkerOptions()
                .position(beja)
                .title("Location")
                .draggable(true))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(beja))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beja, 10f))

        val split =  beja.toString().split("lat/lng:","(", ")")
        //val latLng: List<String> = split[2].split(",")

        viewModel.location = split[2]

        /*mMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {
                TODO("Not yet implemented")
            }

            override fun onMarkerDragEnd(markerPosition: Marker) {
                val getPosition = markerPosition.position.toString()
                val splitMarkerDrag =  getPosition.split("lat/lng:","(", ")")
                viewModel.location = splitMarkerDrag[2]
            }

            override fun onMarkerDragStart(p0: Marker) {
                TODO("Not yet implemented")
            }

        })*/

    }


    private fun back(){
        viewModel.choicesList.removeLast()
        findNavController().popBackStack()
    }

    private fun cancel(){
        findNavController().navigate(ResultadoFragmentDirections.actionGlobalMainFragment())
    }




}