package cat.ivha.sparklestask

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    @GET("task")
    suspend fun llistaTasks(): Response<List<Task>>

    @GET("task/{id}")
    suspend fun getTaskById(@Path("id") id: Long): Response<Task>

    @POST("task")
    suspend fun createTask(@Body task: TaskRequest):Response<Void>

    @PUT("task/update/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: TaskRequest):Response<Void>

    @DELETE("task/delete/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<Void>

    @DELETE("task/complete/{id}")
    suspend fun completeTask(@Path("id") id: Long): Response<Void>

    @GET("user/Hanna/sparks")
    suspend fun getSparks(@Path("username") username: String): Response<String>






}