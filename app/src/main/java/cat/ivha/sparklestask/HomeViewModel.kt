package cat.ivha.sparklestask

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Calendar
import java.util.Date

class HomeViewModel : ViewModel() {

    private var _allTasks = MutableLiveData<List<Task>>()

    private val _filteredTasks = MutableLiveData<List<Task>>()

    private val _selectedData = MutableLiveData<Date?>()

    val selectedData = _selectedData
    val filteredTasks = _filteredTasks

    init {
        carregarTasques()
    }

    fun carregarTasques() {
        viewModelScope.launch{
            try{
                val response = TaskAPI.API().llistaTasks()
                if (response.isSuccessful) {
                    _allTasks.value = response.body() ?: emptyList()
                    _filteredTasks.value = response.body() ?: emptyList()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception){
                Log.e("API", "Error de connexió: " + e.message)
            }
        }
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
            normalitzaData(task.dataLimit) == normalitzaData(data)
        }
        _filteredTasks.value = filtered
    }

    fun mostraTasques() {
        _selectedData.value = null
        _filteredTasks.value = _allTasks.value
    }

    fun afegirTasca(task: TaskRequest) {
        viewModelScope.launch {
            try{
                val response = TaskAPI.API().createTask(task)
                if (response.isSuccessful) {
                   carregarTasques()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception){
                Log.e("API", "Error de connexió: " + e.message)
            }
        }

    }

    fun updateTask(id: Long, updatedTask: TaskRequest) {
        viewModelScope.launch {
            try{
                val response = TaskAPI.API().updateTask(id,updatedTask)
                if (response.isSuccessful) {
                    carregarTasques()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception){
                Log.e("API", "Error de connexió: " + e.message)
            }
        }
    }

    fun deleteTaska(taskId: Long) {
        viewModelScope.launch{
            try{
                val response = TaskAPI.API().deleteTask(taskId)
                if (response.isSuccessful) {
                    carregarTasques()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception){
                Log.e("API", "Error de connexió: " + e.message)
            }
        }
    }
}
