/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.ui.insetos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipbeja.tp21.data.Inseto
import pt.ipbeja.tp21.data.InsetoDatabase
import java.io.File
import java.time.LocalDate

class InsetoViewModel (private val app: Application) : AndroidViewModel(app){

    /**
     * @photoRef variable that will store a temporary image file that was taken by the user's smartphone camera
     * @questionsList a ArrayList that stores every question that the user seen
     * @choicesList a ArrayList that stores every option that the user selected until finding the final insect
     * @gotoA a ArrayList that stores every question id that the user has gone through while selection option A, with the final String always containing an 'R'
     * @gotoB a ArrayList that stores every question id that the user has gone through while selection option B, with the final String always containing an 'R'
     * @ordem a variable String that stores the order of the final insect
     * @descriptionInsect a variable String that stores the description of the final insect
     * @location a variable String that contains the coordinate numbers (latitude and longitude) that comes from google maps that
     */
    var photoRef: File? = null
    val questionsList: ArrayList<String> = ArrayList()
    val choicesList: ArrayList<String> = ArrayList()
    val gotoA: ArrayList<String> = ArrayList()
    val gotoB: ArrayList<String> = ArrayList()
    var ordemInsect: String? = null
    var descriptionInsect: String? = null
    var location: String? = null

    /**
     * readAllData are values that come from data/InsetoDAO in LiveData<List> format that stores Inseto, that contains a database table with values
     */
    val readAllData: LiveData<List<Inseto>>
    val readAllDataDesc: LiveData<List<Inseto>>
    val readAllDataOrdem: LiveData<List<Inseto>>
    val readAllDataOrdemData: LiveData<List<Inseto>>

    val insetoDao = InsetoDatabase.getDatabase(app).insetoDAO()

    /**
     * init constructor will be the first one to start and it will start the values that come from the database
     */
    init {
        readAllData = insetoDao.readAllData()
        readAllDataDesc = insetoDao.readAllDataDesc()
        readAllDataOrdem = insetoDao.readAllDataOrdem()
        readAllDataOrdemData = insetoDao.readAllDataOrdemData()
    }

    /**
     * function that allows an insect to be added to the Room Database, this process will be ran in the background
     */
    fun addInseto(inseto: Inseto){
        viewModelScope.launch(Dispatchers.IO) {
            insetoDao.addInsect(inseto)
        }
    }

    /**
     * function that allows an insect to be deleted from the Room Database, this process will be ran in the background
     */
    fun deleteInseto(inseto: Inseto) {
        viewModelScope.launch(Dispatchers.IO) {
            insetoDao.deleteInsect(inseto)
        }
    }

    fun updateLocation(inseto: Inseto){
        viewModelScope.launch(Dispatchers.IO) {
            updateLocation(inseto)
        }
    }

    /*fun setLocation(lat: Double, long: Double): Boolean {
        if (lat !in -90.0..90.0 || long !in -180.0..180.0) return false
        this.location = Coordinates(lat, long)
        return true
    }*/


}