package cat.ivha.sparklestask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.Date

class HomeViewModel : ViewModel() {

    private val _allTasks = MutableLiveData<List<Task>>()

    private val _filteredTasks = MutableLiveData<List<Task>>()

    private val _selectedData = MutableLiveData<Date?>()

    val selectedData = _selectedData
    val filteredTasks = _filteredTasks

    init {
        carregarTasques()
    }

    fun carregarTasques() {
        val tasks = TasksList.items
        _allTasks.value = tasks
        _filteredTasks.value = tasks
    }

    fun normalitzaData(data: Date): Date {
        return Calendar.getInstance().apply {
            time = data
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    fun filtraTaskaPerData(data: Date) {
        _selectedData.value = data

        val allTasks = _allTasks.value ?: emptyList()

        val filtered = allTasks.filter { task ->
            normalitzaData(task.data) == normalitzaData(data)
        }
        _filteredTasks.value = filtered
    }

    fun mostraTasques() {
        _selectedData.value = null
        _filteredTasks.value = _allTasks.value
    }

    fun afegirTaska(task: Task) {
        val tasquesActuals = _allTasks.value?.toMutableList() ?: mutableListOf()
        tasquesActuals.add(task)
        _allTasks.value = tasquesActuals

        TasksList.items.add(task)

        val dataActual = _selectedData.value
        if (dataActual != null) {
            filtraTaskaPerData(dataActual)
        } else {
            _filteredTasks.value = tasquesActuals
        }
    }

    fun updateTask(updatedTask: Task) {
        val tasquesActuals = _allTasks.value?.toMutableList() ?: return
        val index = tasquesActuals.indexOfFirst { it.id == updatedTask.id }

        if (index != -1) {
            tasquesActuals[index] = updatedTask
            _allTasks.value = tasquesActuals

            val taskListIndex = TasksList.items.indexOfFirst { it.id == updatedTask.id }
            if (taskListIndex != -1) {
                TasksList.items[taskListIndex] = updatedTask
            }

            val dataActual = _selectedData.value
            if (dataActual != null) {
                filtraTaskaPerData(dataActual)
            } else {
                _filteredTasks.value = tasquesActuals
            }
        }
    }

    fun deleteTaska(taskId: Long) {
        val tasquesActuals = _allTasks.value?.toMutableList() ?: return
        tasquesActuals.removeAll { it.id == taskId }
        _allTasks.value = tasquesActuals

        TasksList.items.removeAll { it.id == taskId }

        val dataActual = _selectedData.value
        if (dataActual != null) {
            filtraTaskaPerData(dataActual)
        } else {
            _filteredTasks.value = tasquesActuals
        }
    }
}
