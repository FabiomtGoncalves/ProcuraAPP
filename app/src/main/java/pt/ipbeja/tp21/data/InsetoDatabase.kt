/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */

package pt.ipbeja.tp21.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Creation of the Room Database
 */

@Database(entities = [Inseto::class], version = 1)
@TypeConverters(ConverterImagem::class)
abstract class InsetoDatabase : RoomDatabase() {

    abstract fun insetoDAO(): InsetoDAO

    companion object {
        @Volatile
        private var INSTANCE: InsetoDatabase? = null

        fun getDatabase(context: Context):InsetoDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InsetoDatabase::class.java,
                    "inseto_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}