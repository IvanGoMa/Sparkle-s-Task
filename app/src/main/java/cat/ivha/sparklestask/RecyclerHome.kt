package cat.ivha.sparklestask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class TasksViewHolder (
    itemView: View,
    private val onItemClick: (Task) -> Unit
): RecyclerView.ViewHolder(itemView){
    private val tvNom: TextView = itemView.findViewById(R.id.tvNom)
    private val tvSparks: TextView = itemView.findViewById(R.id.tvSparks)
    private val tvData: TextView = itemView.findViewById(R.id.tvData)

    fun bind (task: Task){
        tvNom.text = task.title
        tvSparks.text = task.sparks.toString()
        tvData.text = task.data.toString()

        itemView.setOnClickListener {
            onItemClick(task)
        }
    }
}

class TasksAdapter(
    private val itemsComplets: List<Task>,
    private val onItemClick: (Task) -> Unit
) : RecyclerView.Adapter<TasksViewHolder>(){

    private var itemsFiltrados = itemsComplets.toList()

    fun filtra(data : Date?){
        itemsFiltrados = if (data == null){
            itemsComplets.toList()
        } else {
            itemsComplets.filter { it.data == data }
        }
        notifyDataSetChanged() // Actualizar la vista
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_tasques_home, parent, false)
        return TasksViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int = itemsFiltrados.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val item = itemsFiltrados[position]
        holder.bind(item)
    }

}