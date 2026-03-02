package cat.ivha.sparklestask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TasksViewHolder (
    itemView: View,
    private val onItemClick: (Task) -> Unit,
    private val onBinClick: (Long) -> Unit,
    private val onCheckboxClick: (Long) -> Unit
): RecyclerView.ViewHolder(itemView){
    private val tvNom: TextView = itemView.findViewById(R.id.tvNom)
    private val tvSparks: TextView = itemView.findViewById(R.id.tvSparks)
    private val tvData: TextView = itemView.findViewById(R.id.tvData)
    private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    private val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    fun bind (task: Task){

        tvNom.text = task.nomTasca
        tvSparks.text = task.sparks.toString()
        tvData.text = df.format(task.dataLimit)
        btnDelete.setOnClickListener { onBinClick(task.id) }
        checkbox.setOnClickListener { onCheckboxClick(task.id) }

        itemView.setOnClickListener {
            onItemClick(task)
        }
    }
}

class TasksAdapter(
    private var itemsComplets: List<Task>,
    private val onItemClick: (Task) -> Unit,
    private val onBinClick: (Long) -> Unit,
    private val onCheckboxClick: (Long) -> Unit
) : RecyclerView.Adapter<TasksViewHolder>(){


    fun updateTasks(newTasks: List<Task>) {
        itemsComplets = newTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_tasques_home, parent, false)
        return TasksViewHolder(view, onItemClick, onBinClick, onCheckboxClick)
    }

    override fun getItemCount(): Int = itemsComplets.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val item = itemsComplets[position]
        holder.bind(item)
    }


}