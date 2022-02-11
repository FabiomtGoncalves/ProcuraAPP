/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.ui.insetos

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.databinding.FragmentDetalheBinding


class DetalheFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: InsetoViewModel
    private lateinit var binding: FragmentDetalheBinding
    private val args by navArgs<DetalheFragmentArgs>()
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetalheBinding.inflate(inflater).let {
        this.binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /**
         * Allows the TextViews that have a lot of text in them to be scrollable by using the method ScrollingMovementMethod()
         */
        binding.tvOrdem.movementMethod = ScrollingMovementMethod()
        binding.tvDescription.movementMethod = ScrollingMovementMethod()
        binding.tvQuestoes.movementMethod = ScrollingMovementMethod()

        /**
         * Gets the current clicked classification by populating the textviews the the information about the insect that was clicked by using the args value
         */
        binding.tvOrdem.text = args.detalhesInseto.ordem
        binding.tvDataHoraDetalhes.text = args.detalhesInseto.data
        binding.tvDescription.text = args.detalhesInseto.descricao
        binding.tvQuestoes.text = args.detalhesInseto.questoes

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.ivReferencia.setOnClickListener {
            //findNavController().navigate(DetalheFragmentDirections.actionDetalheFragmentToClassificar())
        }

        viewModel = ViewModelProvider(this)[InsetoViewModel::class.java]

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext(), R.style.DialogColor)
                .setTitle(R.string.apagar_classificacao)
                .setIcon(R.drawable.icon_app)
                .setMessage(R.string.apagar_classificacao_message)
                .setPositiveButton(R.string.sim) { _, _ ->
                    viewModel.deleteInseto(args.detalhesInseto)
                    Toast.makeText(context,R.string.inseto_eliminado, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                .setNegativeButton(R.string.cancelar) { _, _ ->  }
                .show()
        }

        binding.btnSave.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun atualizarLocalizacao(){

        //val atualizar = Inseto(binding.tvOrdem.toString(), binding.tvDataHoraDetalhes.toString(),binding.tvDescription.toString(), binding.tvQuestoes.toString())

    }

    /**
     * Displays the google maps interface inside a fragment, that allows the user to interact with it and change the location that was previously added to a new one
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val location = args.detalhesInseto.location

        val latLng: List<String> = location.split(",")

        val latLng2 = LatLng(latLng[0].toDouble(), latLng[1].toDouble())

        mMap.addMarker(
            MarkerOptions()
                .position(latLng2)
                .title("Localização")
                .draggable(true))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 10f))
    }

}
