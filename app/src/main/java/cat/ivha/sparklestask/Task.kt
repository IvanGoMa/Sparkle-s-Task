package cat.ivha.sparklestask

import java.util.Date

data class Task(
    val dataLimit: Date,
    val nomTasca: String,
    val sparks: Long,
    val id: Long = nextId()
){
    companion object{
        private var currentId = 0L

        fun nextId(): Long = ++currentId

        fun resetId(){
            currentId = 0L
        }

    }
}

data class TaskRequest(
    val dataLimit: Date,
    val nomTasca: String,
    val sparks: Long,
)