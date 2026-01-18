package cat.ivha.sparklestask

import java.util.Calendar
import java.util.Date


fun dateOf(year: Int, month: Int, day: Int): Date {
    return Calendar.getInstance().apply {
        set(year, month - 1, day)
        set(Calendar.MILLISECOND, 0)
    }.time
}


object TasksList {
    val items = mutableListOf<Task>(
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
        Task(dateOf(2026,1,19),"Recollir",10),
    )
}