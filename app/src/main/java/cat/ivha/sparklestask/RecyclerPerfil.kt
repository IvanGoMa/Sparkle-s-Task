package cat.ivha.sparklestask

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder (
    itemView: View,
    private val onItemClick: (Item) -> Unit
    ): RecyclerView.ViewHolder(itemView){
        private val tvNom: TextView = itemView.findViewById(R.id.tvNom)
        private val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        private val ivImg: ImageView = itemView.findViewById(R.id.ivImatge)

    fun bind (item: Item){
        tvNom.text = item.nom
        tvDesc.text = item.desc
        ivImg.setImageResource(item.img)

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}

class MyAdapter(
    private val items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<MyViewHolder>(){

}