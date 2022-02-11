/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.data

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Insert, update and delete methods to change values in the database
 * Queries to obtain different type of orders so that the user can interact with them and order his collection of insects
 */
@Dao
abstract class InsetoDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun addInsect(inseto: Inseto)

    @Update
    abstract fun updateLocation(inseto:Inseto)

    @Delete
    abstract fun deleteInsect(inseto: Inseto)

    @Query("SELECT * FROM tabela_inseto ORDER BY id DESC")
    abstract fun readAllDataDesc(): LiveData<List<Inseto>>

    @Query("SELECT * FROM tabela_inseto ORDER BY id ASC")
    abstract fun readAllData(): LiveData<List<Inseto>>

    @Query("SELECT * FROM tabela_inseto ORDER BY ordem")
    abstract fun readAllDataOrdem(): LiveData<List<Inseto>>

    @Query("SELECT * FROM tabela_inseto ORDER BY data")
    abstract fun readAllDataOrdemData(): LiveData<List<Inseto>>

}