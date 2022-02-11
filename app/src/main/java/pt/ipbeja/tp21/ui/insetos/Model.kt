/**
 * @author Fábio Gonçalves
 * @version 27/01/2022
 */


package pt.ipbeja.tp21.ui.insetos

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val question: String,
    val options: List<Option> //= listOf()
)

@Serializable
data class Option(
    val goto: String,
    val text: String,
    val description: String,
    val imageLocation: String,
)