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
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.databinding.FragmentPopUpBinding


class ConsultarImgRefFragment : Fragment() {

    private val viewModel: InsetoViewModel by navGraphViewModels(R.id.classificar)
    private lateinit var binding: FragmentPopUpBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPopUpBinding.inflate(inflater).let {
        this.binding = it
        it.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        /**
         * loads the image taken by the user (that is currently stored in the InsetoViewmOdel), and loads it in a ImageView so that the user can see it in bigger size
         */
        with(binding.imgReferencia) {
            viewModel.photoRef?.let { load(it) }
        }


        binding.btnVoltar.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}