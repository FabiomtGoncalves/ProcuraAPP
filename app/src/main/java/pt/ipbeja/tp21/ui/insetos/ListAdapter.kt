/**
 * @author Fábio Gonçalves
 * @version 07/02/2022
 */


package pt.ipbeja.tp21.ui.insetos

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.ktor.client.engine.*
import pt.ipbeja.tp21.R
import pt.ipbeja.tp21.data.Inseto
import pt.ipbeja.tp21.databinding.CustomRowBinding

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>(){

    private var insectList = emptyList<Inseto>()

    class MyViewHolder(val binding: CustomRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)    }

    /**
     * Populates a the current viewHolder with the name of the insect, date of the classification that was made and the image taken by the user that will be displayed
     * with the help a row layout that will have that information so that the user can view -> layout/custom_row.xml
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = insectList[position]
        holder.binding.tvNomeClassificacao.text = currentItem.ordem
        holder.binding.tvDataHora.text = currentItem.data
        //holder.binding.ivImagem.load(currentItem.imagem)

        holder.binding.clRow.setOnClickListener{
            holder.itemView.findNavController().navigate(MeusInsetosFragmentDirections.actionMeusInsetosFragmentToDetalheFragment(currentItem))
        }

    }

    override fun getItemCount(): Int {
        return insectList.size
    }

    /**
     * Set database data inside the RecyclerView
     */
    fun setData(inseto: List<Inseto>){
        this.insectList = inseto
        notifyDataSetChanged()
    }


}