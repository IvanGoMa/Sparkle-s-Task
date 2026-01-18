package cat.ivha.sparklestask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksViewHolder (
    itemView: View,
    private val onItemClick: (Item) -> Unit
): RecyclerView.ViewHolder(itemView){
    //private val tvNom: TextView = itemView.findViewById(R.id.tvNom)
    //private val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
    private val ivImg: ImageView = itemView.findViewById(R.id.ivImatge)

    fun bind (item: Item){
        //tvNom.text = item.nom
        //tvDesc.text = item.desc
        ivImg.setImageResource(item.img)

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}

class TasksAdapter(
    private val itemsComplets: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<MyViewHolder>(){

    private var itemsFiltrados = itemsComplets.toList()

    fun filtra(categoria : Categoria?){
        itemsFiltrados = if (categoria == null){
            itemsComplets.toList()
        } else {
            itemsComplets.filter { it.categoria == categoria }
        }
        notifyDataSetChanged() // Actualizar la vista
    }

    override fun getItemCount(): Int = itemsFiltrados.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsFiltrados[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_items_perfil, parent, false)
        return MyViewHolder(view, onItemClick)
    }
}