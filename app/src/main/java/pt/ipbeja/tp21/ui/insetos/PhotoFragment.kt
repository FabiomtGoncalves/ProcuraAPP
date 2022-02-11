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
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.databinding.FragmentPhotoBinding
import java.io.File


class PhotoFragment : Fragment() {

    private val viewModel: InsetoViewModel by navGraphViewModels(R.id.classificar)
    private lateinit var binding: FragmentPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPhotoBinding.inflate(inflater).let {
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


        binding.camera.setLifecycleOwner(viewLifecycleOwner)
        binding.shutterBtn.setOnClickListener {
            binding.camera.takePicture()
        }

        /**
         * When the user takes a picture of the insect that picture is temporarily stored in the code and lives inside of the InsetoViewModel and stays there at the end
         * of the classification so that the user has the possibility of seeing it again if he wants to
         */
        binding.camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                val dir = requireContext().filesDir
                val file = File(dir, "temp.jpg")

                result.toFile(file) {
                    viewModel.photoRef = it
                    findNavController().navigate(PhotoFragmentDirections.actionPhotoFragmentToClassificarFragment())
                }

            }
        })

    }

}