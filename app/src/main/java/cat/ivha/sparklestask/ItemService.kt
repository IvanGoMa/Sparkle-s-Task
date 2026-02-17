package cat.ivha.sparklestask

import retrofit2.Response
import retrofit2.http.GET

interface ItemService {

    @GET("task")
    suspend fun llistaItems(): Response<List<Task>>



}