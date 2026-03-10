package cat.ivha.sparklestask

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await




object EstadisticaProvider {
    //private val firestore = FirebaseFirestore.getInstance()
    //La variable s'inicialitzarà la primera vegada que s'utilitzi.
    val db: FirebaseFirestore by lazy { Firebase.firestore }
    var dataEstadistica=Estadistica()

    //Retorna Unit perque no necessitem cap valor
    //només si ha funcionat o no. El valor d'estadistica es guarda
    //en la variable estadistica.


    suspend  fun guardarEstadistica(idDispositiu:String, estadistica:Estadistica): Result<Unit> {
        return try {
            db.collection("Sessions").document(idDispositiu).set(estadistica).await()
            dataEstadistica=estadistica
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun afegeixTasca(){
        dataEstadistica.creades++

    }

    fun completaTasca(){
        dataEstadistica.completades++
    }

    fun eliminaTasca(){
        dataEstadistica.eliminades++
    }
}