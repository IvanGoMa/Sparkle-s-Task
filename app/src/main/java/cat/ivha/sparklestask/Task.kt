package cat.ivha.sparklestask

import java.util.Date

data class Task(
    var data: Date,
    var title: String,
    var sparks: Int,
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