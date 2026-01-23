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
    val items = mutableListOf(
        Task(dateOf(2026,1,25),"Recollir",10),
        Task(dateOf(2026,1,25),"Fer la compra",20),
        Task(dateOf(2026,1,25),"Estudiar",10),
        Task(dateOf(2026,1,26),"Netejar",10),
        Task(dateOf(2026,1,26),"Rentadora",15),
        Task(dateOf(2026,2,9),"Secadora",5),
        Task(dateOf(2026,2,14),"Examen",30),
        Task(dateOf(2026,2,21),"Fer esport",20),
        Task(dateOf(2026,3,12),"Entrega treball",50),
        Task(dateOf(2026,3,14),"Descansar",5),
        Task(dateOf(2026,4,17),"Llegir",20),
        Task(dateOf(2026,5,19),"Veure una pel·lícula",15),
    )
}