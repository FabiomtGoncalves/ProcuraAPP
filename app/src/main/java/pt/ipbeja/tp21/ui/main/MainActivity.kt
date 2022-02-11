/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import pt.ipbeja.tp21.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setupActionBarWithNavController(findNavController(R.id.nav_host))
    }

    /*override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp()
    }*/
}