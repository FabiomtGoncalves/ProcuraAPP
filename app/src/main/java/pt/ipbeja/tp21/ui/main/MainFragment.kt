/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.databinding.MainFragmentBinding
import kotlin.system.exitProcess

class MainFragment : Fragment() {

    //private lateinit var viewModel: NotificacaoViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = MainFragmentBinding.inflate(inflater).let {
        this.binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext(), R.style.DialogColor)
                    .setTitle(R.string.fechar_aplicacao)
                    .setIcon(R.drawable.icon_app)
                    .setMessage(R.string.deseja_sair)
                    .setPositiveButton(R.string.sair) { _, _ -> exitProcess(0) }
                    .setNegativeButton(R.string.cancelar) { _, _ ->  }
                    .show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.btnClassificar.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToPhotoFragment())
            Toast.makeText(context,R.string.fotografar,Toast.LENGTH_SHORT).show()
        }

        binding.btnMeusInsetos.setOnClickListener{
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMeusInsetosFragment())
        }

    }



}