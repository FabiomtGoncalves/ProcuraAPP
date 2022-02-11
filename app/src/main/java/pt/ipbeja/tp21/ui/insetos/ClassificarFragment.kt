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
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.databinding.FragmentClassificarBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.content.res.AssetManager
import android.widget.Toast
import androidx.core.view.isVisible
import java.io.InputStream


class ClassificarFragment : Fragment() {

    /**
     * Porperties of values and variables initialized
     *
     * @viewModel a ViewModel class (InsetoViewModel) which is where most of the data is going to be stored to give access to them for the next fragments
     * @binding a replacement of "findViewById", and to facilitate calling objects from layout (.xml files)
     * @idQuestionList a ArrayList of Strings that has the current question result id
     * @idResultList a ArrayList of Strings that has the current result id from the "results" array inside .json file
     * @questionList a ArrayList of Strings that is going to save the current "question" text that's inside the .json file to display to the user
     * @optianAList a ArrayList of Strings that stores the current details of option A (goto, text, description and imageLocation), it is an array inside options[0]
     * in the .json file
     * @optianBList a ArrayList of Strings that stores the current details of option B (goto, text, description and imageLocation), it is an Array inside options[1]
     * in the .json file
     * @ordemList a ArrayList of Strings that stores the current "order" of the insect inside array Results in the .json file
     * @descriptionList a ArrayList of Strings that stores the "description" from array "results" inside the .json file
     * @ordem a variable that stores the current order in the "results" array inside the .json file
     * @description a variable that stores the current description in the "results" array inside the .json file
     * @question a variable that stores the current id in the "nodes" array inside the .json file that allows the questions to loop until it finds the correct the id
     * @idResult a variable that stores the result at the end of the insect classification
     * @copyQuestion a variable that stores the current question in the "nodes" array inside the .json file that allows the user to read the current question displayed on screen
     * @optionA a variable that stores the text of the option A that the user previously selected
     * @optionB a variable that stores the text of the option A that the user previously selected
     * @imgA a variable that stores the current image url (imageLocation from .json file) for option A that displays the image of the current insect to the user to see
     * @imgB a variable that stores the current image url (imageLocation from .json file) for option B that displays the image of the current insect to the user to see
     * @result a variable that stores a String of the final result in the end of the classification so that the user can go to the next fragment
     * @jsonFile a variable that stores the name of the jsonFile ("chave.json" or "chave_en.json"), this variable changes dependently of the user's smartphone language
     */

    private val viewModel: InsetoViewModel by navGraphViewModels(R.id.classificar)
    private lateinit var binding: FragmentClassificarBinding

    private var idQuestionList: ArrayList<String> = ArrayList()
    private var idResultList: ArrayList<String> = ArrayList()
    private var questionList: ArrayList<String> = ArrayList()
    private var optionAList: ArrayList<String> = ArrayList()
    private var optionBList: ArrayList<String> = ArrayList()
    private var ordemList: ArrayList<String> = ArrayList()
    private var descriptionList: ArrayList<String> = ArrayList()

    private var ordem = ""
    private var description = ""
    private var question = ""
    private var idResult = ""
    private var copyQuestion = ""
    private var optionA = ""
    private var optionB = ""
    private var imgA = ""
    private var imgB = ""
    private var result = ""
    private var jsonFile = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentClassificarBinding.inflate(inflater).let {
        this.binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /**
         * If the TextView that belongs to the question, is written "Question" it means that the user's smartphone is not in portuguese and so the .json file that
         * is going to be used is the one called "chave_en.json", otherwise if the language is in portuguese it will the "chave.json" file instead
         */
        if (binding.tvQuestionTitle.text.equals("Question")) {
            jsonFile = "chave_en.json"
        }
        else {
            jsonFile = "chave.json"
        }

        binding.pbLoading.isVisible = false
        binding.tvLoading.isVisible = false


        readJSON()

        binding.btnImgTaken.setOnClickListener {
            findNavController().navigate(ClassificarFragmentDirections.actionClassificarFragmentToPopUpFragment())
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)


        binding.btnAnterior.setOnClickListener {
            back()
        }


        /**
         * When Button A is pressed a condition is going to be triggered and it will check if the last String in ArrayList (gotoA) that is in InsetoViewModel constains
         * the character 'R', if that is true it will add the variable optionA to an arrayList inside InsetoViewModel, and equal the variable result to the last String
         * in the end of the gotoA array, that means it's the last option that the user has clicked, so obviously it's always the last one that it's the more recent
         * After that it will go to the function that reads the json results array and finnaly go to the next fragment.
         * Otherwise if the condition it's false it will simply add the option A to the InsetoViewModel arrayList (choicesList) and start the function where it reads
         * the "nodes" again until it eventually contains a character 'R'.
         */
        binding.btnA.setOnClickListener{
            if (viewModel.gotoA.last().contains('R')) {
                binding.pbLoading.isVisible = true
                binding.tvLoading.isVisible = true
                viewModel.choicesList.add(optionA)
                result = viewModel.gotoA.last()
                readJSONResults()
                findNavController().navigate(ClassificarFragmentDirections.actionClassificarFragmentToResultadoFragment())
            }
            else {
                viewModel.choicesList.add(optionA)
                viewModel.questionsList.add(viewModel.gotoA.last())
                readJSON()
            }
        }

        /**
         * Same logic and implementation as Button A (described above) but only with different results.
         */
        binding.btnB.setOnClickListener{
            if (viewModel.gotoB.last().contains('R')) {
                binding.pbLoading.isVisible = true
                binding.tvLoading.isVisible = true
                viewModel.choicesList.add(optionB)
                result = viewModel.gotoB.last()
                readJSONResults()
                findNavController().navigate(ClassificarFragmentDirections.actionClassificarFragmentToResultadoFragment())
            }
            else {
                viewModel.choicesList.add(optionB)
                viewModel.questionsList.add(viewModel.gotoB.last())
                readJSON()
            }
        }

    }

    /**
     * A function that will read the json file inside and will go through the "nodes" array that's inside.
     * It will simply be inside a for loop that will go through every line (if needed), until it finds the question that belongs to the path to the next question that the
     * user has given.
     * It will always give values to the variables initialized at the start of the code so that they store the current value, so that when it finds the right question,
     * it has all the parameters that belong to it (id, question, options, [goto, text, description, imageLocation]x2)
     */
    private fun readJSON() {
        try {

            val obj = JSONObject(loadJSON())
            val nodesArray = obj.getJSONArray("nodes")

            for (i in 0 until nodesArray.length()) {

                if (viewModel.questionsList.isEmpty()) {
                    viewModel.questionsList.add("Q1")
                }

                if (question == viewModel.questionsList.last()){
                    binding.tvQuestion.text = copyQuestion
                    binding.tvOpcaoA.text = optionA
                    binding.tvOpcaoB.text = optionB
                    imgA = imgA.replace("\\", "")
                    imgB = imgB.replace("\\", "")
                    binding.imgA.setImageBitmap(getBitmapFromAssets(imgA));
                    binding.imgB.setImageBitmap(getBitmapFromAssets(imgB));
                    break;
                }
                else {
                    val data = nodesArray.getJSONObject(i)
                    idQuestionList.add(data.getString("id"))
                    question = idQuestionList[i]
                    questionList.add(data.getString("question"))
                    copyQuestion = questionList[i]
                    val options = data.getJSONArray("options")

                    optionAList.add(options.getString(0))
                    val contentA = optionAList[i].split('"').toTypedArray()
                    optionA = contentA[7]
                    viewModel.gotoA.add(contentA[3])
                    imgA = contentA[15]

                    optionBList.add(options.getString(1))
                    val contentB = optionBList[i].split('"').toTypedArray()
                    optionB = contentB[7]
                    viewModel.gotoB.add(contentB[3])
                    imgB = contentB[15]
                }


            }

        }

        catch (e: JSONException) {
            Toast.makeText(context,R.string.json_erro,Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            e.printStackTrace()
        }
    }

    /**
     * Same logic and implementation as the function above (readJson), the only difference is that it will go only through the "results" array inside of the .json file
     * instead of the "nodes"
     */
    private fun readJSONResults() {
        try {
            val obj = JSONObject(loadJSON())
            val nodesArray = obj.getJSONArray("results")

            for (i in 0 until nodesArray.length()) {
                if (idResult == result){
                    viewModel.ordemInsect = ordem
                    viewModel.descriptionInsect = description
                    break;
                }
                else {
                    val data = nodesArray.getJSONObject(i)
                    idResultList.add(data.getString("id"))
                    idResult = idResultList[i]
                    ordemList.add(data.getString("ordem"))
                    ordem = ordemList[i]
                    descriptionList.add(data.getString("description"))
                    description = descriptionList[i]
                }
            }
        }

        catch (e: JSONException) {
            Toast.makeText(context,R.string.json_erro,Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            e.printStackTrace()
        }
    }

    /**
     * Function that loads the .json file from the assets folder
     */
    private fun loadJSON(): String {
        val json: String?
        try {
            val inputStream = context!!.assets.open(jsonFile)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            val charset: Charset = Charsets.UTF_8
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset)
        }
        catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    /**
     * Function that allows the user to go back to the previous question or to go back to the main menu.
     * If the last string inside the questionsList array is "Q1" it means that the user is in the first question, so if the user clicks the back button it will popup a
     * alert message to confirm if he actually wants to do that
     * If the last question in the array isn't "Q1" then it will mean that the user is in another question and if he presses the back button it will go back to the
     * previous question, removing the last question that was stored in the arrays of the current question (questionsList, choicesList, gotoA, gotoB), and then start again
     * the readJSON() function
     */
    private fun back() {
        if (viewModel.questionsList.last() == "Q1") {
            AlertDialog.Builder(requireContext(), R.style.DialogColor)
                .setTitle(R.string.pagina_anterior)
                .setIcon(R.drawable.icon_app)
                .setMessage(R.string.fotografar_voltar)
                .setPositiveButton(R.string.voltar) { _, _ -> findNavController().popBackStack() }
                .setNegativeButton(R.string.cancelar) { _, _ ->  }
                .show()
        }
        else {
            viewModel.questionsList.removeLast()
            viewModel.choicesList.removeLast()
            viewModel.gotoA.removeLast()
            viewModel.gotoB.removeLast()
            readJSON()
        }
    }

    /**
     * This function allows the images from the jsonfile "imageLocation" be loaded from the "assets" folder and converted to a Bitmap Image so that they can be displayed
     * for the user.
     */
    private fun getBitmapFromAssets(fileName: String): Bitmap? {

        val am: AssetManager? = null
        var inputStream: InputStream? = null
        try {

            inputStream = context!!.assets.open(fileName)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return BitmapFactory.decodeStream(inputStream)
    }

}


