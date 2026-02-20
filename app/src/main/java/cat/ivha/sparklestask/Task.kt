package cat.ivha.sparklestask

import java.util.Date

data class Task(
    var dataLimit: Date,
    var nomTasca: String,
    var sparks: Long,
    var id: Long = nextId()
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
    var dataLimit: Date,
    var nomTasca: String,
    var sparks: Long,
)