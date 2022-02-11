/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.data


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import android.graphics.Bitmap


/**
 * Creates a Room Database Table that will store all the needed values of the classification
 */
@kotlinx.parcelize.Parcelize
@Entity(tableName = "tabela_inseto")
data class Inseto(
    //val imagem: File?,
    val ordem: String,
    val data: String,
    val descricao: String,
    val questoes: String,
    val location: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
): Parcelable