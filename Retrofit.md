# Retrofit - Comunicació amb l'API REST

## Índex
1. [Què és Retrofit](#què-és-retrofit)
2. [Dependències necessàries](#dependències-necessàries)
3. [Arquitectura de la comunicació](#arquitectura-de-la-comunicació)
4. [Configuració de Retrofit](#configuració-de-retrofit)
5. [Interfície del servei](#interfície-del-servei)
6. [Models de dades](#models-de-dades)
7. [Ús al ViewModel](#ús-al-viewmodel)
8. [Gestió de respostes i errors](#gestió-de-respostes-i-errors)
9. [Coroutines i programació asíncrona](#coroutines-i-programació-asíncrona)
10. [Serialització amb Gson](#serialització-amb-gson)

---

## Què és Retrofit

Retrofit és una llibreria de Square que simplifica enormement les comunicacions HTTP amb servidors web. És la solució estàndard de facto per a aplicacions Android que necessiten comunicar-se amb APIs REST.

### Per què utilitzar Retrofit

**El problema que resol**: Sense Retrofit, hauríem de gestionar manualment cada petició HTTP utilitzant classes de baix nivell com HttpURLConnection. Això implica:
- Escriure moltíssim codi repetitiu per cada petició
- Gestionar manualment la conversió de JSON a objectes Kotlin
- Implementar la lògica de threading per no bloquejar el thread principal
- Gestionar errors de connexió de manera consistent
- Mantenir codi complex i propens a errors

**La solució de Retrofit**: Retrofit converteix aquestes operacions complexes en simples crides de funcions. Només cal definir una interfície amb anotacions i Retrofit genera automàticament tota la implementació necessària.

### Avantatges principals

1. **Simplicitat**: Converteix peticions HTTP complexes en simples crides de funcions
2. **Type-safe**: Treballa amb classes Kotlin tipades, detectant errors en temps de compilació
3. **Integració amb coroutines**: Suport natiu per a programació asíncrona moderna
4. **Conversors automàtics**: Transforma JSON ↔ objectes Kotlin automàticament amb Gson
5. **Gestió d'errors**: Proporciona un sistema consistent per gestionar errors de xarxa i HTTP
6. **Rendiment**: Optimitzat per a Android amb pooling de connexions i caching

### Conceptes clau

**API REST**: És un estil d'arquitectura per a serveis web on cada recurs (tasques, usuaris, etc.) té una URL única i s'accedeix mitjançant mètodes HTTP estàndard:
- **GET**: Obtenir dades (Read)
- **POST**: Crear nous recursos (Create)
- **PUT**: Actualitzar recursos existents (Update)
- **DELETE**: Eliminar recursos (Delete)

**JSON**: Format de text per intercanviar dades estructurades entre client i servidor. Exemple:
```json
{
  "nomTasca": "Estudiar",
  "sparks": 10,
  "dataLimit": "2026-01-25T00:00:00"
}
```

---

## Dependències necessàries

Per utilitzar Retrofit al projecte, cal afegir les següents dependències al fitxer `build.gradle.kts` (Module: app):

```kotlin
dependencies {
    // Retrofit - Client HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    
    // Gson Converter - Converteix JSON ↔ objectes Kotlin
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Gson - Llibreria de serialització JSON
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Coroutines - Per a operacions asíncrones
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

### Què fa cada dependència

- **retrofit**: Nucli de la llibreria que gestiona les peticions HTTP
- **converter-gson**: Plugin que converteix automàticament entre JSON i objectes Kotlin
- **gson**: Llibreria de Google per serialitzar i deserialitzar JSON
- **coroutines**: Permet executar codi de manera asíncrona sense bloquejar la UI

---

## Arquitectura de la comunicació

L'arquitectura de comunicació amb l'API segueix aquest flux:

```
Fragment/Activity → ViewModel → TaskAPI → TaskService → Servidor
                                    ↓
                                  Retrofit
                                    ↓
                                  Gson
```

1. **Fragment/Activity**: La interfície d'usuari que mostra les dades
2. **ViewModel**: Conté la lògica de negoci i gestiona l'estat
3. **TaskAPI**: Classe singleton que configura i proporciona la instància de Retrofit
4. **TaskService**: Interfície que defineix tots els endpoints de l'API
5. **Retrofit**: Genera la implementació real de TaskService
6. **Gson**: Converteix les dades JSON del servidor en objectes Kotlin i viceversa

### Flux d'una petició completa

Exemple: L'usuari vol veure les seves tasques

```
1. HomeFragment s'inicialitza
2. HomeViewModel.carregarTasques() s'executa
3. TaskAPI.API() proporciona la instància de TaskService
4. TaskService.llistaTasks() fa la petició GET al servidor
5. Retrofit envia la petició HTTP a http://129.159.110.118:8082/api/task
6. El servidor respon amb JSON: [{"id":1,"nomTasca":"Estudiar",...}, ...]
7. Gson converteix el JSON en List<Task>
8. ViewModel actualitza el LiveData amb les tasques
9. Fragment observa el canvi i actualitza el RecyclerView
```

---

## Configuració de Retrofit

### TaskAPI.kt - Singleton amb Retrofit

La classe `TaskAPI` implementa el patró Singleton per garantir que només existeixi una instància de Retrofit durant tota l'execució de l'aplicació. Això és important per a l'eficiència i la gestió de recursos.

```kotlin
package cat.ivha.sparklestask

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class TaskAPI {
    companion object {
        private var mTaskAPI: TaskService? = null

        @Synchronized
        fun API(): TaskService {
            if (mTaskAPI == null) {

                val gsondateformat = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                mTaskAPI = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                    .baseUrl("http://129.159.110.118:8082/api/")
                    .build()
                    .create(TaskService::class.java)
            }

            return mTaskAPI!!
        }
    }
}
```

### Explicació component per component

#### 1. Companion Object i Singleton

```kotlin
companion object {
    private var mTaskAPI: TaskService? = null
```

- **companion object**: Permet definir membres estàtics dins d'una classe Kotlin
- **mTaskAPI**: Variable privada que emmagatzema la instància única de TaskService
- **Nullable (?)**: Inicialment és null fins que es crea la primera vegada

#### 2. Mètode API() amb @Synchronized

```kotlin
@Synchronized
fun API(): TaskService {
    if (mTaskAPI == null) {
        // ... configuració ...
    }
    return mTaskAPI!!
}
```

- **@Synchronized**: Garanteix que només un thread pugui executar aquest mètode alhora, evitant crear múltiples instàncies
- **Lazy initialization**: Només crea la instància quan es necessita per primera vegada
- **!!**: Operador de non-null assertion - estem segurs que mTaskAPI no serà null després del if

#### 3. Configuració de Gson

```kotlin
val gsondateformat = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .create()
```

Gson necessita saber com convertir les dates entre Java/Kotlin i JSON:

**Format de data**: `yyyy-MM-dd'T'HH:mm:ss`
- `yyyy`: Any amb 4 dígits (2026)
- `MM`: Mes amb 2 dígits (01)
- `dd`: Dia amb 2 dígits (25)
- `'T'`: Separador literal entre data i hora (ISO 8601)
- `HH`: Hora amb 2 dígits (14)
- `mm`: Minuts amb 2 dígits (30)
- `ss`: Segons amb 2 dígits (00)

**Exemple**: `2026-01-25T14:30:00` representa el 25 de gener de 2026 a les 14:30:00

Aquest format és el que el servidor espera rebre i envia. Sense aquesta configuració, Gson no sabria com convertir objectes Date.

#### 4. Construcció de Retrofit

```kotlin
mTaskAPI = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
    .baseUrl("http://129.159.110.118:8082/api/")
    .build()
    .create(TaskService::class.java)
```

Analitzem cada mètode:

**addConverterFactory**: Indica a Retrofit com convertir les dades
- Utilitza el Gson configurat amb el format de dates personalitzat
- Automàticament converteix JSON → objectes Kotlin en les respostes
- Automàticament converteix objectes Kotlin → JSON en les peticions

**baseUrl**: URL base de l'API
- Totes les peticions començaran amb aquesta URL
- Exemple: si un endpoint és `@GET("task")`, la URL completa serà `http://129.159.110.118:8082/api/task`
- **Important**: Ha d'acabar amb `/`

**build()**: Construeix la instància de Retrofit amb la configuració especificada

**create(TaskService::class.java)**: Genera la implementació de la interfície TaskService
- Retrofit genera automàticament tot el codi necessari per fer les peticions HTTP
- Retorna un objecte que implementa TaskService

---

## Interfície del servei

### TaskService.kt - Definició dels endpoints

La interfície `TaskService` defineix tots els endpoints de l'API mitjançant anotacions. Retrofit llegeix aquestes anotacions i genera automàticament la implementació.

```kotlin
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
    suspend fun createTask(@Body task: TaskRequest): Response<Void>

    @PUT("task/update/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: TaskRequest): Response<Void>

    @DELETE("task/delete/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<Void>

    @DELETE("task/complete/{id}")
    suspend fun completeTask(@Path("id") id: Long): Response<Void>

    @GET("user/{username}/sparks")
    suspend fun getSparks(@Path("username") username: String): Response<String>
}
```

### Anotacions HTTP

#### @GET - Obtenir dades

```kotlin
@GET("task")
suspend fun llistaTasks(): Response<List<Task>>
```

- **Mètode HTTP**: GET (obtenir dades sense modificar-les)
- **URL completa**: `http://129.159.110.118:8082/api/task`
- **Retorna**: Una llista de totes les tasques
- **Ús**: Carregar les tasques al iniciar l'aplicació o refrescar

```kotlin
@GET("task/{id}")
suspend fun getTaskById(@Path("id") id: Long): Response<Task>
```

- **Path parameter**: `{id}` és un placeholder que es substitueix pel valor real
- **@Path("id")**: Indica que el paràmetre `id` s'insertarà a la URL
- **Exemple**: `getTaskById(5)` → GET `http://129.159.110.118:8082/api/task/5`
- **Retorna**: Una tasca específica identificada pel seu ID

```kotlin
@GET("user/{username}/sparks")
suspend fun getSparks(@Path("username") username: String): Response<String>
```

- **URL completa**: `http://129.159.110.118:8082/api/user/Hanna/sparks`
- **Retorna**: Els Sparks de l'usuari com a String (després es converteix a Long)
- **Particularitat**: El servidor retorna un String en lloc d'un número directament

#### @POST - Crear nous recursos

```kotlin
@POST("task")
suspend fun createTask(@Body task: TaskRequest): Response<Void>
```

- **Mètode HTTP**: POST (crear un nou recurs)
- **@Body**: El paràmetre `task` s'envia al cos de la petició com a JSON
- **Procés**:
  1. Gson converteix l'objecte `TaskRequest` a JSON
  2. Retrofit envia el JSON al servidor
  3. El servidor crea la tasca i retorna un codi d'estat
- **Response<Void>**: No esperem cap dada de retorn, només el codi d'estat HTTP
- **Exemple de JSON enviat**:
  ```json
  {
    "nomTasca": "Estudiar",
    "sparks": 10,
    "dataLimit": "2026-01-25T00:00:00"
  }
  ```

#### @PUT - Actualitzar recursos existents

```kotlin
@PUT("task/update/{id}")
suspend fun updateTask(@Path("id") id: Long, @Body task: TaskRequest): Response<Void>
```

- **Mètode HTTP**: PUT (actualitzar un recurs existent)
- **Combina**: Path parameter (id) + Body (dades noves)
- **URL exemple**: `PUT http://129.159.110.118:8082/api/task/update/5`
- **Procés**:
  1. L'ID indica quina tasca actualitzar
  2. El body conté les noves dades de la tasca
  3. El servidor substitueix les dades antigues per les noves

#### @DELETE - Eliminar recursos

```kotlin
@DELETE("task/delete/{id}")
suspend fun deleteTask(@Path("id") id: Long): Response<Void>

@DELETE("task/complete/{id}")
suspend fun completeTask(@Path("id") id: Long): Response<Void>
```

- **Mètode HTTP**: DELETE (eliminar un recurs)
- **deleteTask**: Elimina permanentment una tasca
- **completeTask**: Marca una tasca com a completada (l'elimina i afegeix Sparks)
- **Diferència**: `completeTask` actualitza els Sparks de l'usuari abans d'eliminar

### Response<T> - Tipus de resposta

Totes les funcions retornen `Response<T>` en lloc de `T` directament. Això proporciona informació completa sobre la petició:

```kotlin
Response<List<Task>>
```

**Avantatges de usar Response**:
- Accés al codi d'estat HTTP (200, 404, 500, etc.)
- Possibilitat de gestionar errors amb més detall
- Accés als headers de la resposta
- Distinció entre èxit i error

**Mètodes importants**:
```kotlin
response.isSuccessful  // true si el codi és 200-299
response.code()        // Codi d'estat HTTP (200, 404, etc.)
response.body()        // Les dades deserialitzades (pot ser null)
response.message()     // Missatge d'estat HTTP
```

### suspend - Funcions suspensibles

Totes les funcions tenen el modificador `suspend`:

```kotlin
suspend fun llistaTasks(): Response<List<Task>>
```

**Què significa suspend**:
- La funció pot ser pausada i represa sense bloquejar el thread
- Només es pot cridar des d'una coroutine o altra funció suspend
- Permet fer operacions llargues (xarxa) sense bloquejar la UI

**Per què és necessari**:
- Les peticions de xarxa poden trigar segons
- No es pot bloquejar el thread principal (UI thread)
- Les coroutines permeten escriure codi asíncron de manera seqüencial

---

## Models de dades

### Task.kt - Estructures de dades

```kotlin
package cat.ivha.sparklestask

import java.util.Date

data class Task(
    val dataLimit: Date,
    val nomTasca: String,
    val sparks: Long,
    val id: Long = nextId()
) {
    companion object {
        private var currentId = 0L

        fun nextId(): Long = ++currentId

        fun resetId() {
            currentId = 0L
        }
    }
}

data class TaskRequest(
    val dataLimit: Date,
    val nomTasca: String,
    val sparks: Long,
)
```

### Task - Model complet

**Data class**: Genera automàticament `equals()`, `hashCode()`, `toString()` i `copy()`

**Propietats**:
- **dataLimit**: Data límit per completar la tasca (tipus Date)
- **nomTasca**: Nom descriptiu de la tasca (String)
- **sparks**: Recompensa en Sparks per completar-la (Long)
- **id**: Identificador únic de la tasca (generat automàticament)

**Companion object**: Gestiona la generació d'IDs únics
- **currentId**: Comptador privat que s'incrementa
- **nextId()**: Retorna el següent ID disponible
- **resetId()**: Reinicia el comptador (útil per testing)

**Ús**: Aquest model representa una tasca completa retornada pel servidor

### TaskRequest - Model per peticions

**Diferència amb Task**: No té ID perquè:
- En **crear** (POST): El servidor genera l'ID automàticament
- En **actualitzar** (PUT): L'ID va a la URL, no al body

**Serialització**: Quan enviem un `TaskRequest`, Gson el converteix a:
```json
{
  "dataLimit": "2026-01-25T00:00:00",
  "nomTasca": "Estudiar",
  "sparks": 10
}
```

**Deserialització**: Quan rebem un `Task`, Gson converteix des de:
```json
{
  "id": 1,
  "dataLimit": "2026-01-25T00:00:00",
  "nomTasca": "Estudiar",
  "sparks": 10
}
```

### Per què dos models diferents

Podríem fer servir només `Task` amb `id` nullable, però separar-los té avantatges:

1. **Claredat**: Immediatament es veu si és una tasca existent o una petició
2. **Type safety**: Impossibilita enviar un ID en crear una tasca nova
3. **Semàntica**: Reflecteix millor la diferència entre dades del servidor i dades que enviem

---

## Ús al ViewModel

### HomeViewModel.kt - Implementació real

El `HomeViewModel` és on realment utilitzem Retrofit per comunicar-nos amb l'API. Conté tota la lògica de negoci relacionada amb les tasques.

```kotlin
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

    // ... altres mètodes ...
}
```

### LiveData - Gestió d'estat reactiu

```kotlin
private var _allTasks = MutableLiveData<List<Task>>()
private val _filteredTasks = MutableLiveData<List<Task>>()
val filteredTasks = _filteredTasks
```

**Patró de nomenclatura**:
- **_allTasks** (privat, MutableLiveData): Pot ser modificat dins del ViewModel
- **filteredTasks** (públic, LiveData): Només lectura per al Fragment

**Per què aquest patró**:
- Encapsulació: Només el ViewModel pot modificar les dades
- El Fragment només pot observar i llegir
- Evita modificacions accidentals des de la UI

### Inicialització automàtica

```kotlin
init {
    carregarTasques()
    carregarSparks()
}
```

El bloc `init` s'executa quan es crea el ViewModel:
- Carrega les tasques automàticament
- Carrega els Sparks de l'usuari
- Garanteix que les dades estiguin disponibles quan el Fragment s'adjunta

### Carregar tasques - GET

```kotlin
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
```

**Pas a pas**:

1. **viewModelScope.launch**: Inicia una coroutine
   - `viewModelScope`: Scope vinculat al cicle de vida del ViewModel
   - Automàticament cancel·la la coroutine quan el ViewModel es destrueix
   - Evita memory leaks

2. **try-catch**: Gestiona dos tipus d'errors
   - Errors de xarxa (connexió perduda, timeout)
   - Errors de serialització (JSON malformat)

3. **TaskAPI.API().llistaTasks()**: Crida a l'endpoint GET /task
   - Obté la instància singleton de TaskService
   - Executa la petició HTTP
   - Retorna un Response<List<Task>>

4. **response.isSuccessful**: Comprova si el codi HTTP és 2xx
   - true: Codi entre 200-299 (èxit)
   - false: Qualsevol altre codi (error del servidor o client)

5. **response.body()**: Obté les dades deserialitzades
   - Pot ser null si la resposta està buida
   - `?: emptyList()`: Si és null, usa una llista buida

6. **Actualització de LiveData**: 
   - `_allTasks.value`: Emmagatzema totes les tasques
   - `_filteredTasks.value`: Inicialment mostra totes, després pot filtrar

7. **Log.e()**: Registra errors per debugging
   - Facilita detectar problemes durant el desenvolupament
   - En producció es podrien mostrar missatges a l'usuari

### Carregar Sparks - GET amb path parameter

```kotlin
fun carregarSparks() {
    viewModelScope.launch {
        try {
            val response = TaskAPI.API().getSparks("Hanna")
            if (response.isSuccessful) {
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
```

**Particularitats**:

1. **getSparks("Hanna")**: Usuari hardcoded
   - En una aplicació real vindria de les preferències o sessió
   - URL generada: `http://129.159.110.118:8082/api/user/Hanna/sparks`

2. **response.body()?.toLongOrNull()**: Conversió segura
   - El servidor retorna String en lloc de número
   - `toLongOrNull()`: Si no es pot convertir, retorna null
   - `?: 0L`: Si és null, usa 0 com a valor per defecte

3. **Log.d()**: Log de debug (menys prioritat que error)
   - Útil per confirmar que les dades s'han carregat correctament

### Crear tasca - POST amb body

```kotlin
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
```

**Procés complet**:

1. **Recepció del paràmetre**: TaskRequest amb les dades de la nova tasca
   ```kotlin
   val novaTasca = TaskRequest(
       dataLimit = Date(),
       nomTasca = "Estudiar",
       sparks = 10
   )
   viewModel.afegirTasca(novaTasca)
   ```

2. **Serialització automàtica**: Gson converteix TaskRequest → JSON
   ```json
   {
     "dataLimit": "2026-01-25T00:00:00",
     "nomTasca": "Estudiar",
     "sparks": 10
   }
   ```

3. **Enviament**: POST http://129.159.110.118:8082/api/task
   - Headers: `Content-Type: application/json`
   - Body: El JSON generat per Gson

4. **Resposta del servidor**: 
   - Èxit: Codi 200/201, tasca creada
   - Error: Codi 4xx/5xx amb missatge d'error

5. **Refresc de dades**: `carregarTasques()`
   - Torna a carregar totes les tasques des del servidor
   - Això garanteix que la llista està sincronitzada
   - La nova tasca apareixerà automàticament

### Actualitzar tasca - PUT amb ID i body

```kotlin
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
```

**Crida des d'ActualitzaTasca.kt**:

```kotlin
val tascaActualitzada = TaskRequest(
    dataLimit = novaData,
    nomTasca = nouNom,
    sparks = nousSparks
)
viewModel.updateTask(taskId, tascaActualitzada)
```

**Detalls importants**:

1. **Dos paràmetres**:
   - `id`: Indica quina tasca modificar (va a la URL)
   - `updatedTask`: Conté les noves dades (va al body)

2. **URL generada**: `PUT http://129.159.110.118:8082/api/task/update/5`

3. **Body de la petició**: Les noves dades en JSON
   ```json
   {
     "dataLimit": "2026-02-15T00:00:00",
     "nomTasca": "Estudiar matemàtiques",
     "sparks": 15
   }
   ```

4. **Comportament del servidor**:
   - Busca la tasca amb `id = 5`
   - Substitueix els seus camps amb les noves dades
   - Retorna èxit o error

### Eliminar tasca - DELETE

```kotlin
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
```

**Característiques**:

1. **Només necessita l'ID**: 
   - URL: `DELETE http://129.159.110.118:8082/api/task/delete/5`
   - No té body, l'ID identifica completament el recurs

2. **Operació destructiva**:
   - No es pot desfer
   - El servidor elimina permanentment la tasca

3. **Ús típic**: Quan l'usuari decideix que no vol completar una tasca

### Completar tasca - DELETE especial

```kotlin
fun completeTask(taskId: Long) {
    viewModelScope.launch {
        try {
            val response = TaskAPI.API().completeTask(taskId)
            if (response.isSuccessful) {
                Log.d("API", "Tarea completada exitosamente")
                
                carregarTasques()
                carregarSparks()
            } else {
                Log.e("API", "Error HTTP al completar tarea: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("API", "Error al completar tarea: ${e.message}")
        }
    }
}
```

**Diferències amb delete normal**:

1. **Endpoint diferent**: `/task/complete/{id}` vs `/task/delete/{id}`

2. **Lògica del servidor**:
   - Obté els Sparks de la tasca
   - Afegeix els Sparks a l'usuari
   - Elimina la tasca
   - Tot això en una transacció atòmica

3. **Refresc doble**:
   - `carregarTasques()`: Actualitza la llista (la tasca ja no apareix)
   - `carregarSparks()`: Actualitza el total de Sparks de l'usuari

4. **Experiència d'usuari**:
   - L'usuari veu desaparèixer la tasca
   - El comptador de Sparks s'incrementa
   - Tot de manera automàtica i sincronitzada

### Filtrar tasques per data

```kotlin
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
```

**Filtratge local vs servidor**:

- **No fa petició HTTP**: Filtra les dades que ja té carregades
- **Més eficient**: No necessita esperar resposta del servidor
- **Experiència instantània**: El filtre s'aplica immediatament

**normalitzaData**:
- Elimina hores, minuts, segons i mil·lisegons
- Permet comparar només dates (dia/mes/any)
- Exemple: `25/01/2026 14:30:45` → `25/01/2026 00:00:00`

---

## Gestió de respostes i errors

### Tipus de respostes HTTP

#### Codis d'èxit (2xx)

```kotlin
if (response.isSuccessful) {
    // Codis 200-299
}
```

- **200 OK**: Petició processada correctament (GET, PUT, DELETE)
- **201 Created**: Recurs creat correctament (POST)
- **204 No Content**: Èxit sense contingut de retorn

#### Codis d'error del client (4xx)

```kotlin
when (response.code()) {
    400 -> // Bad Request - Dades invàlides
    401 -> // Unauthorized - No autenticat
    403 -> // Forbidden - No autoritzat
    404 -> // Not Found - Recurs no existeix
}
```

**400 Bad Request**: Les dades enviades són invàlides
- Exemple: Data amb format incorrecte, camps obligatoris buits
- Solució: Validar les dades abans d'enviar

**404 Not Found**: La tasca amb aquest ID no existeix
- Exemple: Intentar actualitzar la tasca ID=999 que no existeix
- Solució: Comprovar que l'ID és vàlid

#### Codis d'error del servidor (5xx)

```kotlin
when (response.code()) {
    500 -> // Internal Server Error
    503 -> // Service Unavailable
}
```

**500 Internal Server Error**: Error en el codi del servidor
- No és culpa de l'aplicació Android
- Contactar amb el desenvolupador del backend

**503 Service Unavailable**: Servidor saturat o en manteniment
- Solució temporal: Reintent automàtic amb backoff exponencial

### Gestió d'excepcions

```kotlin
try {
    val response = TaskAPI.API().llistaTasks()
    // ...
} catch (e: Exception) {
    Log.e("API", "Error de connexió: " + e.message)
}
```

**Tipus d'excepcions comunes**:

1. **UnknownHostException**: No s'ha pogut resoldre el domini
   - Causa: Sense connexió a internet o IP incorrecta
   - Solució: Comprovar connectivitat

2. **SocketTimeoutException**: La petició ha trigat massa
   - Causa: Servidor lent o connexió molt lenta
   - Solució: Augmentar el timeout o mostrar loading

3. **JsonSyntaxException**: JSON malformat
   - Causa: El servidor ha retornat JSON invàlid
   - Solució: Revisar el format del servidor

### Millora: Gestió d'errors amb sealed class

Una millora professional seria utilitzar sealed classes per representar estats:

```kotlin
sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val code: Int, val message: String) : ApiResult<T>()
    data class Exception<T>(val exception: Throwable) : ApiResult<T>()
}

fun carregarTasques() {
    viewModelScope.launch {
        val result = try {
            val response = TaskAPI.API().llistaTasks()
            if (response.isSuccessful) {
                ApiResult.Success(response.body() ?: emptyList())
            } else {
                ApiResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
        
        when (result) {
            is ApiResult.Success -> {
                _allTasks.value = result.data
            }
            is ApiResult.Error -> {
                // Mostrar missatge d'error a l'usuari
            }
            is ApiResult.Exception -> {
                // Comprovar connectivitat
            }
        }
    }
}
```

---

## Coroutines i programació asíncrona

### Per què són necessàries les coroutines

**El problema**: Les peticions HTTP poden trigar segons
- Bloquejar el thread principal congelaria la UI
- L'aplicació semblaria que ha crashejat
- Android mataria l'aplicació (ANR - Application Not Responding)

**La solució**: Executar el codi en un thread diferent
- El thread principal segueix gestionant la UI
- La petició s'executa en paral·lel
- Quan arriba la resposta, s'actualitza la UI

### Threads tradicionals vs Coroutines

**Threads tradicionals** (l'antiga manera):
```kotlin
Thread {
    val data = TaskAPI.API().llistaTasks() // Bloquejant
    runOnUiThread {
        // Actualitzar UI
    }
}.start()
```

Problemes:
- Verbós i complex
- Difícil de gestionar múltiples operacions
- Propens a errors (race conditions, memory leaks)
- Codi difícil de llegir i mantenir

**Coroutines** (la manera moderna):
```kotlin
viewModelScope.launch {
    val response = TaskAPI.API().llistaTasks() // No bloquejant
    // Actualitzar UI directament
}
```

Avantatges:
- Codi seqüencial fàcil de llegir
- Gestió automàtica del cicle de vida
- Menys propens a errors
- Més eficients en ús de recursos

### viewModelScope - Scope de coroutines

```kotlin
viewModelScope.launch {
    // Codi asíncron
}
```

**Què és viewModelScope**:
- Un CoroutineScope vinculat al cicle de vida del ViewModel
- Automàticament cancel·la totes les coroutines quan el ViewModel es destrueix
- Evita memory leaks i crashes

**Cicle de vida**:
```
ViewModel creat → viewModelScope actiu
    ↓
Coroutines iniciades
    ↓
ViewModel destruït → Totes les coroutines cancel·lades
```

**Per què és important**:
- Si l'usuari surt de la pantalla mentre carrega dades
- Les peticions en curs es cancel·len automàticament
- No s'intenta actualitzar una UI que ja no existeix

### suspend functions - Funcions suspensibles

```kotlin
suspend fun llistaTasks(): Response<List<Task>>
```

**Què significa suspend**:
- La funció pot pausar la seva execució
- Quan es pausa, no bloqueja el thread
- Altres coroutines poden executar-se
- Quan la dada està disponible, la funció repren

**Exemple visual**:
```
Thread Principal: [UI] → [Pausa] → [Altres tasques UI] → [Represa] → [UI]
                    ↓                                        ↑
                 Petició HTTP (en paral·lel)
```

**Regles**:
- Només es pot cridar des d'una coroutine o altra funció suspend
- No es pot cridar directament des d'una funció normal

### Dispatchers - On s'executa el codi

```kotlin
viewModelScope.launch(Dispatchers.IO) {
    // Operacions de xarxa o disc
}

viewModelScope.launch(Dispatchers.Main) {
    // Actualitzacions de UI
}
```

**Dispatchers disponibles**:

- **Dispatchers.Main**: Thread principal (UI thread)
  - Per actualitzar vistes
  - És on passa viewModelScope.launch per defecte

- **Dispatchers.IO**: Pool de threads per operacions I/O
  - Peticions HTTP
  - Lectura/escriptura de fitxers
  - Operacions de base de dades

- **Dispatchers.Default**: Per càlculs intensius
  - Processament d'imatges
  - Ordenar llistes grans
  - Càlculs matemàtics complexos

**Exemple amb múltiples dispatchers**:
```kotlin
viewModelScope.launch {
    // Dispatchers.Main per defecte
    
    val response = withContext(Dispatchers.IO) {
        // Canvia a IO per la petició
        TaskAPI.API().llistaTasks()
    }
    
    // Automàticament torna a Main
    _allTasks.value = response.body()
}
```

### Flow d'execució amb coroutines

Exemple complet del que passa quan es crida `carregarTasques()`:

```
1. Fragment crida viewModel.carregarTasques()
   ↓
2. viewModelScope.launch { } inicia una coroutine
   ↓
3. TaskAPI.API().llistaTasks() inicia petició HTTP
   ↓ (funció suspend - es pausa aquí)
4. La coroutine cedeix el thread
   ↓
5. UI continua funcionant normalment
   ↓ (petició HTTP en curs en paral·lel)
6. Arriba la resposta del servidor
   ↓
7. La coroutine es reprèn amb la resposta
   ↓
8. if (response.isSuccessful) s'avalua
   ↓
9. _allTasks.value = ... (LiveData notifica al Fragment)
   ↓
10. Fragment actualitza el RecyclerView
```

**Tot això sense bloquejar la UI ni un sol instant!**

---

## Serialització amb Gson

### Què és la serialització

**Serialització**: Convertir objectes Kotlin → text JSON
**Deserialització**: Convertir text JSON → objectes Kotlin

```kotlin
// Serialització (Kotlin → JSON)
TaskRequest(Date(), "Estudiar", 10)
    ↓
{"dataLimit":"2026-01-25T00:00:00","nomTasca":"Estudiar","sparks":10}

// Deserialització (JSON → Kotlin)
{"id":1,"dataLimit":"2026-01-25T00:00:00","nomTasca":"Estudiar","sparks":10}
    ↓
Task(Date(), "Estudiar", 10, 1)
```

### Configuració de Gson per dates

```kotlin
val gsondateformat = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .create()
```

**Per què és necessari**:
- Java/Kotlin utilitza objectes `Date`
- JSON només entén text
- Cal definir el format de conversió

**Format ISO 8601**:
- `yyyy-MM-dd'T'HH:mm:ss`
- Exemple: `2026-01-25T14:30:00`
- Estàndard internacional per representar dates en text

**Sense aquesta configuració**:
```kotlin
// Gson per defecte usaria:
"Jan 25, 2026 2:30:00 PM"

// El servidor espera:
"2026-01-25T14:30:00"

// Resultat: Error de desserialització ❌
```

### Serialització automàtica de TaskRequest

Quan fem:
```kotlin
val task = TaskRequest(
    dataLimit = dateOf(2026, 1, 25),
    nomTasca = "Estudiar",
    sparks = 10
)

TaskAPI.API().createTask(task)
```

Gson automàticament crea:
```json
{
  "dataLimit": "2026-01-25T00:00:00",
  "nomTasca": "Estudiar",
  "sparks": 10
}
```

**Procés**:
1. Gson inspecciona les propietats de TaskRequest
2. Converteix cada propietat segons el seu tipus:
   - Date → String amb el format configurat
   - String → String (sense canvis)
   - Long → número
3. Crea la estructura JSON amb els noms de les propietats
4. Retrofit envia aquest JSON al servidor

### Deserialització automàtica de Task

Quan el servidor respon:
```json
{
  "id": 1,
  "dataLimit": "2026-01-25T00:00:00",
  "nomTasca": "Estudiar",
  "sparks": 10
}
```

Gson automàticament crea:
```kotlin
Task(
    id = 1,
    dataLimit = Date(...), // Parsejat des del String
    nomTasca = "Estudiar",
    sparks = 10
)
```

**Procés**:
1. Gson llegeix el JSON
2. Inspecciona les propietats de Task
3. Assigna cada camp JSON a la propietat corresponent:
   - "id" → id: Long
   - "dataLimit" → dataLimit: Date (parseja el String)
   - "nomTasca" → nomTasca: String
   - "sparks" → sparks: Long
4. Crea la instància de Task

### Noms de propietats i @SerializedName

Per defecte, els noms de les propietats han de coincidir exactament:

```kotlin
data class Task(
    val nomTasca: String  // JSON ha de tenir "nomTasca"
)
```

Si el servidor usa noms diferents, podem usar @SerializedName:

```kotlin
data class Task(
    @SerializedName("task_name")
    val nomTasca: String  // JSON té "task_name", Kotlin usa nomTasca
)
```

**En el nostre projecte**: No cal perquè els noms coincideixen

### Gestió de nullabilitat

```kotlin
data class Task(
    val nomTasca: String,      // Obligatori
    val descripció: String?    // Opcional
)
```

**JSON vàlid**:
```json
{
  "nomTasca": "Estudiar",
  "descripció": "Per l'examen de demà"
}
```

**També vàlid** (descripció omesa):
```json
{
  "nomTasca": "Estudiar"
}
```

Gson assigna `descripció = null` automàticament

### Llistes i col·leccions

```kotlin
Response<List<Task>>
```

Gson automàticament deserialitza arrays JSON:

```json
[
  {
    "id": 1,
    "nomTasca": "Estudiar",
    ...
  },
  {
    "id": 2,
    "nomTasca": "Fer esport",
    ...
  }
]
```

Convertit a:
```kotlin
listOf(
    Task(id=1, nomTasca="Estudiar", ...),
    Task(id=2, nomTasca="Fer esport", ...)
)
```

### Debugging de serialització

Per veure el JSON generat:

```kotlin
val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
val json = gson.toJson(tascaActualitzada)
Log.d("DEBUG_JSON", json)
```

Útil per:
- Verificar que el format és correcte
- Detectar problemes abans d'enviar al servidor
- Comparar amb el que el servidor espera

---

## Resum i millors pràctiques

### Arquitectura completa

```
UI Layer (Fragment)
    ↓ observa LiveData
ViewModel (Lògica de negoci)
    ↓ crida funcions suspend
TaskAPI (Configuració Retrofit)
    ↓ proporciona
TaskService (Definició endpoints)
    ↓ implementat per
Retrofit (Generació automàtica)
    ↓ usa
Gson (Serialització JSON)
    ↓ comunica amb
Servidor REST API
```

### Avantatges d'aquesta arquitectura

1. **Separació de responsabilitats**: Cada component té una funció clara
2. **Testabilitat**: Es pot provar cada capa independentment
3. **Mantenibilitat**: Fàcil modificar o ampliar funcionalitats
4. **Escalabilitat**: Afegir nous endpoints és trivial
5. **Robustesa**: Gestió consistent d'errors i cicle de vida

### Millors pràctiques implementades

✅ **Singleton per Retrofit**: Una sola instància durant tota l'aplicació
✅ **Coroutines amb viewModelScope**: Gestió automàtica del cicle de vida
✅ **Response<T>**: Accés complet a codis d'estat i errors
✅ **Try-catch**: Gestió d'excepcions de xarxa
✅ **LiveData**: Notificacions reactives a la UI
✅ **Models separats**: Task per lectura, TaskRequest per escriptura
✅ **Logging**: Facilita el debugging durant el desenvolupament

### Millores potencials

🔧 **Repository Pattern**: Capa intermèdia entre ViewModel i API
```kotlin
class TaskRepository {
    suspend fun getTasks(): List<Task> {
        return TaskAPI.API().llistaTasks().body() ?: emptyList()
    }
}
```

🔧 **Gestió d'errors centralitzada**: Sealed classes per representar estats
🔧 **Retry automàtic**: Reintentar peticions fallides
🔧 **Cache local**: Room database per funcionar offline
🔧 **Loading states**: Indicadors de càrrega mentre espera respostes
🔧 **Autenticació**: Interceptors per afegir tokens JWT
🔧 **Timeouts configurables**: Ajustar segons la qualitat de connexió

---

## Exemples pràctics complets

### Flux complet: Crear una tasca

**1. Usuari omple el formulari (CreateTask.kt)**:
```kotlin
private fun crearTasca() {
    val nom = binding.etNom.text.toString()
    val sparks = binding.etSparks.text.toString().toLongOrNull()
    val data = df.parse(binding.etData.text.toString())
    
    val novaTasca = TaskRequest(
        dataLimit = data,
        nomTasca = nom,
        sparks = sparks
    )
    
    viewModel.afegirTasca(novaTasca)
    dismiss()
}
```

**2. ViewModel processa la petició (HomeViewModel.kt)**:
```kotlin
fun afegirTasca(task: TaskRequest) {
    viewModelScope.launch {
        try {
            val response = TaskAPI.API().createTask(task)
            if (response.isSuccessful) {
                carregarTasques()
            }
        } catch (e: Exception) {
            Log.e("API", "Error: " + e.message)
        }
    }
}
```

**3. Retrofit serialitza i envia**:
```
POST http://129.159.110.118:8082/api/task
Content-Type: application/json

{
  "dataLimit": "2026-01-25T00:00:00",
  "nomTasca": "Estudiar",
  "sparks": 10
}
```

**4. Servidor crea la tasca i respon**:
```
HTTP/1.1 200 OK
```

**5. ViewModel recarrega les tasques**:
```kotlin
carregarTasques() // Torna a fer GET /task
```

**6. Fragment actualitza la UI**:
```kotlin
viewModel.filteredTasks.observe(viewLifecycleOwner) { tasks ->
    adapter.updateTasks(tasks)
}
```

### Flux complet: Completar una tasca

**1. Usuari marca checkbox (HomeFragment.kt)**:
```kotlin
adapter = TasksAdapter(
    onCheckboxClick = { id ->
        viewModel.completeTask(id)
    }
)
```

**2. ViewModel executa la petició**:
```kotlin
fun completeTask(taskId: Long) {
    viewModelScope.launch {
        try {
            val response = TaskAPI.API().completeTask(taskId)
            if (response.isSuccessful) {
                carregarTasques()
                carregarSparks()
            }
        } catch (e: Exception) {
            Log.e("API", "Error: " + e.message)
        }
    }
}
```

**3. Retrofit envia DELETE**:
```
DELETE http://129.159.110.118:8082/api/task/complete/5
```

**4. Servidor**:
- Obté sparks de la tasca (10)
- Afegeix 10 sparks a l'usuari "Hanna"
- Elimina la tasca
- Respon amb èxit

**5. ViewModel actualitza dades**:
```kotlin
carregarTasques()  // La tasca ja no apareix
carregarSparks()   // Sparks actualitzats
```

**6. UI es refresca automàticament**:
- RecyclerView elimina l'item
- Comptador de Sparks s'incrementa
- Tot de manera fluida i reactiva

---

## Conclusió

Retrofit transforma la complexitat de les comunicacions HTTP en simples crides de funcions tipades. Combinat amb coroutines, LiveData i l'arquitectura MVVM, proporciona una base sòlida per a aplicacions Android modernes que necessiten comunicar-se amb servidors.

Els conceptes clau a recordar són:
- **Configuració única** amb TaskAPI singleton
- **Interfície declarativa** amb anotacions
- **Serialització automàtica** amb Gson
- **Programació asíncrona** amb coroutines
- **Gestió reactiva** amb LiveData

Aquesta arquitectura garanteix que Sparkle's Task sigui eficient, mantenible i escalable.
