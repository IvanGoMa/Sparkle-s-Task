package cat.ivha.sparklestask

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class HomeViewModel : ViewModel() {

    private var _allTasks = MutableLiveData<List<Task>>()
    private val _filteredTasks = MutableLiveData<List<Task>>()
    private val _selectedData = MutableLiveData<Date?>()

    private val _totalSparks = MutableLiveData<Long>(0L)
    val totalSparks: LiveData<Long> = _totalSparks

    val selectedData = _selectedData
    val filteredTasks = _filteredTasks


    init {
        carregarTasques()
        carregarSparks()
    }

    fun carregarSparks() {
        viewModelScope.launch {
            try {
                val response = TaskAPI.API().getSparks("Hanna")
                if (response.isSuccessful) {
                    // La API devuelve un String, convertir a Long
                    val sparks = response.body()?.toLongOrNull() ?: 0L
                    _totalSparks.value = sparks
                    Log.d("API", "Sparks cargados: $sparks")
                } else {
                    Log.e("API", "Error HTTP al cargar sparks: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error al cargar sparks: ${e.message}")
            }
        }
    }

    fun carregarTasques() {
        viewModelScope.launch {
            try {
                val response = TaskAPI.API().llistaTasks()
                if (response.isSuccessful) {
                    _allTasks.value = response.body() ?: emptyList()
                    _filteredTasks.value = response.body() ?: emptyList()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
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
            try {
                val response = TaskAPI.API().createTask(task)
                if (response.isSuccessful) {
                    carregarTasques()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error de connexió: " + e.message)
            }
        }
    }

    fun updateTask(id: Long, updatedTask: TaskRequest) {
        viewModelScope.launch {
            try {
                val response = TaskAPI.API().updateTask(id, updatedTask)
                if (response.isSuccessful) {
                    carregarTasques()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error de connexió: " + e.message)
            }
        }
    }

    fun deleteTaska(taskId: Long) {
        viewModelScope.launch {
            try {
                val response = TaskAPI.API().deleteTask(taskId)
                if (response.isSuccessful) {
                    carregarTasques()
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error de connexió: " + e.message)
            }
        }
    }

    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            try {
                val response = TaskAPI.API().completeTask(taskId)
                if (response.isSuccessful) {
                    Log.d("API", "Tarea completada exitosamente")

                    // Recargar las tareas (la completada ya no aparecerá)
                    carregarTasques()

                    // Recargar los sparks actualizados desde el servidor
                    carregarSparks()
                } else {
                    Log.e("API", "Error HTTP al completar tarea: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error al completar tarea: ${e.message}")
            }
        }
    }
}