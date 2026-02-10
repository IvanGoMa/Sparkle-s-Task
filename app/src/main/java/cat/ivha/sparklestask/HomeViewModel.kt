package cat.ivha.sparklestask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.currentCoroutineContext

class HomeViewModel: ViewModel() {

    private val _allTasks = MutableLiveData<List<Task>>()

    private val _filteredTasks = MutableLiveData<List<Task>>()

    private val _selectedData = MutableLiveData<String?>()

    val selectedData = LiveData<String?> = _selectedData

    init {
        carregarTasques()
    }

    fun carregarTasques(){
        val tasks = TasksList.items
        _allTasks.value = tasks
        _filteredTasks.value = tasks
    }

    fun filtraTaskaPerData(date: String){
        _selectedData.value = date

        val allTasks = _allTasks.value ?: emptyList()

        val filtered = allTasks.filter { task ->
            task.data == date // String i date
        }
        _filteredTasks.value = filtered
    }

    fun mostraTasques(){
        _selectedData.value = null
        _filteredTasks.value = _allTasks.value
    }

    fun afegirTaska(task: Task){
        val tasquesActuals = _allTasks.value?.toMutableList() ?: mutableListOf()
        tasquesActuals.add(task)
        _allTasks.value = tasquesActuals

        val dataActual = _selectedData.value
        if (dataActual != null){
            filtraTaskaPerData(dataActual)
        }else {
            _filteredTasks.value = dataActual
        }
    }

    fun updateTask(updatedTask: Task){
        val tasquesActuals = _allTasks.value?.toMutableList() ?: return
        val index = tasquesActuals.indexOfFirst { it.id == updatedTask.id }

        if (index != -1){
            tasquesActuals[index] = updatedTask
            _allTasks.value = tasquesActuals

            val dataActual = _selectedData.value
            if (dataActual != null){
                filtraTaskaPerData(dataActual)
            }else{
                _filteredTasks.value = tasquesActuals
            }
        }

        fun deleteTaska(taskId: Long){
            val tasquesActuals = _allTasks.value?.toMutableList() ?: return
            tasquesActuals.removeAll{it.id == taskId}
            _allTasks.value = tasquesActuals

            val dataActual = _selectedData.value
            if(dataActual != null){
                filtraTaskaPerData(dataActual)
            }else {
                _filteredTasks.value = tasquesActuals
            }
        }
    }
}