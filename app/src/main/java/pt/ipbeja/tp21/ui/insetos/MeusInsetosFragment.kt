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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.databinding.FragmentMeusInsetosBinding


class MeusInsetosFragment : Fragment() {

    private lateinit var viewModel: InsetoViewModel
    //private val viewModel: InsetoViewModel by navGraphViewModels(R.id.classificar)
    private lateinit var binding: FragmentMeusInsetosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMeusInsetosBinding.inflate(inflater).let {
        this.binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /**
         * Creation of a spinner so that the user can have multiple choices if he wants to order the different classifications that he has (Ascending Order, Descending Order,
         * Order by Name and Order by Date/Hour.
         * The spinner values are populated by a array with the options described above, those which come from a string-array called "ordenar" that comes from a string.xml file
         * A switch case was created to know when each diferent opsition of the string-array inside of the spinner are pressed the items inside the recyclerview are placed in position
         */
        ArrayAdapter.createFromResource(requireContext(), R.array.ordenar, R.layout.spinner_layout).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.ordenarSpinner.adapter = adapter
            binding.ordenarSpinner.onItemSelectedListener = object :

                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val adapterList = ListAdapter()
                    binding.rvInsects.adapter = adapterList
                    binding.rvInsects.layoutManager = LinearLayoutManager(context)
                    when (position) {
                        0 -> viewModel.readAllData.observe(viewLifecycleOwner, { inseto -> adapterList.setData(inseto) })
                        1 -> viewModel.readAllDataDesc.observe(viewLifecycleOwner, { inseto -> adapterList.setData(inseto) })
                        2 -> viewModel.readAllDataOrdem.observe(viewLifecycleOwner, { inseto -> adapterList.setData(inseto) })
                        3 -> viewModel.readAllDataOrdemData.observe(viewLifecycleOwner, { inseto -> adapterList.setData(inseto) })
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

        viewModel = ViewModelProvider(this)[InsetoViewModel::class.java]

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)





    }

}