# Sparkle's Task

## Introducció

**Sparkle's Task** és una aplicació d'organització de tasques amb un enfocament lúdic i gamificat. L'aplicació transforma les tasques diàries en una experiència semblant a un joc, facilitant la seva finalització mitjançant un sistema de recompenses.

### Característiques principals

- Gestió de tasques diàries
- Experiència gamificada
- Sistema de recompenses amb Sparks (monedes virtuals)
- Personalització del perfil amb accessoris
- Motivació per completar tasques

## Com funciona

1. **Crea tasques** - Organitza les teves activitats diàries
2. **Completa-les** - Marca les tasques com a finalitzades
3. **Guanya Sparks** - Obté monedes virtuals com a recompensa
4. **Compra accessoris** - Utilitza els Sparks per millorar el teu perfil

---

## Part Tècnica

En aquesta secció s'expliquen les eines i tecnologies utilitzades per implementar les diferents funcionalitats de l'aplicació.

# Documentació Tècnica - Sparkle's Task

## Índex
1. RecyclerView: Adapter i ViewHolder
2. Splash Screen
3. Menú de Navegació
4. Dialogs

---

## RecyclerView: Adapter i ViewHolder

El **RecyclerView** és un component d'Android que permet mostrar llistes grans de dades de manera eficient. A Sparkle's Task, s'utilitza per mostrar la col·lecció d'accessoris disponibles a la botiga del perfil i les tasques a home.

### Components principals

#### 1. Data Class - Item

La classe `Item` defineix l'estructura de cada accessori:

```kotlin
data class Item (
    val nom: String,          // Nom de l'accessori
    val desc: String,         // Descripció
    val img: Int,            // ID del recurs drawable de la imatge
    val categoria: Categoria // Categoria a la qual pertany
)
```

**Característiques:**
- `data class`: Tipus especial de classe en Kotlin que genera automàticament mètodes com `equals()`, `hashCode()`, `toString()` i `copy()`
- Tots els camps són immutables (`val`) per garantir la seguretat de les dades
- `img` és de tipus `Int` perquè emmagatzema l'ID del recurs (per exemple, `R.drawable.gafasrosas`)

#### 2. Enum Class - Categoria

L'enumeració defineix les categories possibles d'accessoris:

```kotlin
enum class Categoria {
    COLLARS,
    GORROS,
    ULLERES
}
```

**Avantatges d'utilitzar enum:**
- Garanteix que només es poden utilitzar valors vàlids
- Evita errors tipogràfics en strings
- Proporciona autocompletat a l'IDE
- Facilita el manteniment del codi

#### 3. DataSource - Proveïdor de dades

L'`object DataSource` actua com a repositori centralitzat de dades:

```kotlin
object DataSource {
    val items: MutableList<Item> = mutableListOf(
        Item("Gafa rosa", "Un accesori per l'avatar", R.drawable.gafasrosas, Categoria.ULLERES),
        Item("Cowboy Marron", "Un accesori per l'avatar", R.drawable.cowmarron, Categoria.GORROS),
        // ... més items
    )
}
```

**Característiques:**
- `object`: Patró Singleton en Kotlin. Només existeix una instància durant tota l'execució
- `MutableList`: Llista modificable que permet afegir o eliminar elements dinàmicament
- Centralitza totes les dades en un sol lloc, facilitant la seva gestió i actualització

### ViewHolder - Patró de disseny

El **ViewHolder** és un patró de disseny que millora significativament el rendiment del RecyclerView.

```kotlin
class MyViewHolder (
    itemView: View,
    private val onItemClick: (Item) -> Unit
): RecyclerView.ViewHolder(itemView){
    
    private val ivImg: ImageView = itemView.findViewById(R.id.ivImatge)

    fun bind (item: Item){
        ivImg.setImageResource(item.img)
        
        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
```

**Funcionament detallat:**

1. **Constructor:**
   - Rep la `View` inflada (el layout de cada element)
   - Rep una funció lambda `onItemClick` per gestionar els clics
   - Busca i emmagatzema les referències als elements visuals

2. **Mètode `bind()`:**
   - Assigna les dades de l'objecte `Item` als elements visuals
   - `setImageResource()`: Carrega la imatge corresponent
   - `setOnClickListener()`: Configura l'acció quan l'usuari fa clic sobre l'element

3. **Per què és important?**
   - **Rendiment**: `findViewById()` és una operació costosa. El ViewHolder la fa només una vegada per vista
   - **Reutilització**: Quan fas scroll, les vistes que surten de la pantalla es reutilitzen per mostrar nous elements
   - **Memòria**: Evita crear noves vistes constantment, reduint l'ús de memòria

### Adapter - Pont entre dades i interfície

L'**Adapter** és el component que connecta les dades amb la interfície visual del RecyclerView.

```kotlin
class MyAdapter(
    private val itemsComplets: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<MyViewHolder>(){

    private var itemsFiltrados = itemsComplets.toList()

    override fun getItemCount(): Int = itemsFiltrados.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_items_perfil, parent, false)
        return MyViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsFiltrados[position]
        holder.bind(item)
    }
}
```

**Mètodes fonamentals:**

1. **`getItemCount()`:**
   - Indica el nombre total d'elements a mostrar
   - **Important**: Utilitza `itemsFiltrados`, no `itemsComplets`, per reflectir els filtres aplicats

2. **`onCreateViewHolder()`:**
   - S'executa quan el RecyclerView necessita crear una nova vista
   - **LayoutInflater**: Converteix el fitxer XML del layout en un objecte View
   - `inflate()`: Procés de crear la vista a partir del XML
   - Paràmetres d'inflate:
     - `R.layout.rv_items_perfil`: El layout a inflar
     - `parent`: El ViewGroup contenidor
     - `false`: No adjuntar la vista al parent immediatament
   - Retorna un nou ViewHolder amb la vista creada

3. **`onBindViewHolder()`:**
   - S'executa cada vegada que un element es mostra a la pantalla
   - Obté l'`Item` de la posició corresponent
   - Crida al mètode `bind()` del ViewHolder per actualitzar la interfície

**Flux de funcionament:**

```
Inici del RecyclerView
    ↓
getItemCount() → RecyclerView sap quants elements hi ha
    ↓
onCreateViewHolder() → Crea ViewHolders (només els necessaris per pantalla)
    ↓
onBindViewHolder() → Assigna dades als ViewHolders visibles
    ↓
L'usuari fa scroll
    ↓
onBindViewHolder() → Reutilitza ViewHolders existents amb noves dades
```

### Layout de cada element

El layout `rv_items_perfil.xml` defineix com es veu cada accessori:

```xml
<androidx.constraintlayout.widget.ConstraintLayout 
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="4dp">

    <ImageView
        android:id="@+id/ivImatge"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:scaleType="centerCrop"
        app:layout_constraintDimensionRatio="1:1"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

**Aspectes clau:**

- **`layout_width="0dp"` i `layout_height="0dp"`**: En ConstraintLayout, `0dp` significa "match_constraint", permet que les constraints defineixin la mida
- **`app:layout_constraintDimensionRatio="1:1"`**: Manté una proporció 1:1 (quadrat), adaptant-se a qualsevol mida de pantalla
- **`scaleType="centerCrop"`**: Escala la imatge per omplir tot l'espai, retallant si cal per mantenir les proporcions
- **`layout_margin="4dp"`**: Espai entre elements per evitar que estiguin enganxats

### Sistema de filtres

El sistema de filtres permet mostrar només els accessoris d'una categoria específica.

#### Mètode de filtratge a l'Adapter

```kotlin
fun filtra(categoria : Categoria?){
    itemsFiltrados = if (categoria == null){
        itemsComplets.toList()
    } else {
        itemsComplets.filter { it.categoria == categoria }
    }
    notifyDataSetChanged()
}
```

**Funcionament pas a pas:**

1. **Paràmetre `categoria: Categoria?`:**
   - El `?` indica que pot ser `null`
   - `null` significa "sense filtre" (mostrar tot)
   - Qualsevol altra categoria aplicarà el filtre corresponent

2. **Expressió condicional:**
   ```kotlin
   if (categoria == null) {
       itemsComplets.toList()  // Copia tota la llista original
   } else {
       itemsComplets.filter { it.categoria == categoria }  // Filtra
   }
   ```

3. **`filter { }`:**
   - Funció d'ordre superior que recorre la llista
   - `it` representa cada element
   - Només manté els elements on `it.categoria == categoria` és `true`
   - Retorna una nova llista amb els elements filtrats

4. **`notifyDataSetChanged()`:**
   - Notifica al RecyclerView que les dades han canviat
   - Força la re-renderització de tots els elements visibles
   - **Important**: Aquest mètode és necessari perquè el RecyclerView actualitzi la interfície

#### Implementació al Fragment

```kotlin
class PerfilFragment : Fragment(R.layout.perfil_rv) {
    
    private var ultimClicat: String? = null
    private val COLLARS = "Collars"
    private val ULLERES = "Ulleres"
    private val GORROS = "Gorros"
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Configuració del RecyclerView
        val items = DataSource.items
        adapter = MyAdapter(
            itemsComplets = items,
            onItemClick = { item ->
                Toast.makeText(requireContext(), item.desc, Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        
        // Configuració dels filtres
        btnCollars.setOnClickListener {
            adapter.filtra(if (ultimClicat == COLLARS) null else Categoria.COLLARS)
            ultimClicat = if (ultimClicat == COLLARS) null else COLLARS
        }
        
        btnUlleres.setOnClickListener {
            adapter.filtra(if (ultimClicat == ULLERES) null else Categoria.ULLERES)
            ultimClicat = if (ultimClicat == ULLERES) null else ULLERES
        }
        
        btnGorros.setOnClickListener {
            adapter.filtra(if (ultimClicat == GORROS) null else Categoria.GORROS)
            ultimClicat = if (ultimClicat == GORROS) null else GORROS
        }
    }
}
```

**Lògica del toggle (activar/desactivar filtre):**

```
Estat inicial:
- ultimClicat = null
- Mostrant tots els accessoris

Usuari clica "Collars":
├─ ultimClicat != COLLARS → Aplicar filtre
├─ adapter.filtra(Categoria.COLLARS)
├─ ultimClicat = "Collars"
└─ Resultat: Mostra només collars

Usuari clica "Collars" de nou:
├─ ultimClicat == COLLARS → Eliminar filtre
├─ adapter.filtra(null)
├─ ultimClicat = null
└─ Resultat: Mostra tots els accessoris

Usuari clica "Ulleres":
├─ ultimClicat != ULLERES → Aplicar nou filtre
├─ adapter.filtra(Categoria.ULLERES)
├─ ultimClicat = "Ulleres"
└─ Resultat: Mostra només ulleres
```

**Per què aquesta implementació?**

- **Només un filtre actiu**: No pots tenir collars i ulleres filtrats simultàniament
- **Toggle intuïtiu**: Clicar el mateix botó dues vegades elimina el filtre
- **Canvi de categoria**: Clicar un botó diferent canvia directament al nou filtre

**Variables utilitzades:**

- **`ultimClicat: String?`**: Guarda quin filtre està actiu actualment (`null` si no n'hi ha cap)
- **Constants `COLLARS`, `ULLERES`, `GORROS`**: Eviten errors tipogràfics i faciliten comparacions

**`GridLayoutManager(requireContext(), 3)`:**
- Organitza els elements en una graella de 3 columnes
- S'adapta automàticament a l'amplada de la pantalla
- Cada element ocupa 1/3 de l'amplada disponible

---

## Splash Screen

La **Splash Screen** és la pantalla inicial que apareix quan l'usuari obre l'aplicació, mentre es carreguen els recursos necessaris. Proporciona una experiència visual professional i fluida.

### 1. Afegir la dependència

Al fitxer `build.gradle.kts` (nivell app), s'afegeix la llibreria de Splash Screen:

```kotlin
dependencies {
    // ... altres dependències
    implementation(libs.androidx.core.splashscreen)
}
```

**Què fa aquesta dependència?**
- Proporciona la classe `SplashScreen` i les seves funcionalitats
- Gestiona la transició entre la splash screen i l'activitat principal
- Compatible amb versions antigues d'Android (API 21+)

### 2. Crear el theme de la Splash Screen

Al fitxer `themes.xml`, es defineix l'estil visual de la splash screen:

```xml
<style name="Theme.App.SplashScreen" parent="Theme.SplashScreen">
    <item name="windowSplashScreenAnimatedIcon">@drawable/ic_splash_animation</item>
    <item name="postSplashScreenTheme">@style/Pink.SparklesTask</item>
    <item name="windowSplashScreenAnimationDuration">10000</item>
</style>
```

**Propietats explicades:**

1. **`parent="Theme.SplashScreen"`:**
   - Hereta del theme base de la llibreria SplashScreen
   - Proporciona tot el comportament estàndar de les splash screens

2. **`windowSplashScreenAnimatedIcon`:**
   - Defineix la imatge/animació que es mostrarà
   - Pot ser un drawable estàtic o un AnimatedVectorDrawable
   - En aquest cas: `@drawable/ic_splash_animation`

3. **`postSplashScreenTheme`:**
   - **Molt important**: Indica quin theme utilitzar DESPRÉS de la splash screen
   - Aquí s'especifica `Pink.SparklesTask`, el theme principal de l'aplicació
   - Sense aquesta línia, l'aplicació perdria tot l'estil en acabar la splash screen

4. **`windowSplashScreenAnimationDuration`:**
   - Durada de l'animació en mil·lisegons (10000ms = 10 segons)
   - Controla quant de temps es mostrarà l'animació

### 3. Crear l'animació

Fitxer `ic_splash_animation.xml` (drawable animat):

```xml
<set xmlns:android="http://schemas.android.com/apk/res/android">

    <objectAnimator
        android:propertyName="scaleX"
        android:valueFrom="0.1"
        android:valueTo="0.6"
        android:duration="500"
        android:interpolator="@android:interpolator/fast_out_slow_in" />

    <objectAnimator
        android:propertyName="scaleY"
        android:valueFrom="0.1"
        android:valueTo="0.6"
        android:duration="500"
        android:interpolator="@android:interpolator/fast_out_slow_in" />

    <objectAnimator
        android:propertyName="alpha"
        android:valueFrom="0"
        android:valueTo="1"
        android:duration="1000" />

</set>
```

**Desglossament de l'animació:**

1. **`<set>`:** Contenidor que agrupa múltiples animacions per executar-les simultàniament

2. **Primera animació - `scaleX`:**
   - **`propertyName="scaleX"`**: Escala horitzontal
   - **`valueFrom="0.1"`**: Comença al 10% de la mida original
   - **`valueTo="0.6"`**: Arriba al 60% de la mida original
   - **`duration="500"`**: Durada de 500ms (mig segon)
   - **`interpolator="fast_out_slow_in"`**: Comença ràpid i alenteix al final

3. **Segona animació - `scaleY`:**
   - Idèntica a scaleX però per l'escala vertical
   - S'executa simultàniament per mantenir les proporcions

4. **Tercera animació - `alpha`:**
   - **`propertyName="alpha"`**: Opacitat/transparència
   - **`valueFrom="0"`**: Comença invisible (transparent)
   - **`valueTo="1"`**: Acaba completament visible (opac)
   - **`duration="1000"`**: Durada d'1 segon (més llarga que l'escala)

**Efecte visual resultant:**
- La imatge apareix des del centre
- Creix des de molt petita (10%) fins al 60% de mida
- Simultàniament, va apareixent gradualment (fade in)
- L'efecte és suau gràcies a l'interpolador

### 4. Configurar el Manifest

Al fitxer `AndroidManifest.xml`, es configura quina activitat utilitzarà la splash screen:

```xml
<application
    android:theme="@style/Theme.SparklesTask">
    
    <activity
        android:name=".Inici"
        android:exported="true"
        android:theme="@style/Theme.App.SplashScreen">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    
    <!-- Altres activitats -->
</application>
```

**Elements clau:**

1. **`android:exported="true"`:**
   - Permet que el sistema Android pugui llançar aquesta activitat
   - **Obligatori** per a l'activitat LAUNCHER (punt d'entrada de l'app)

2. **`android:theme="@style/Theme.App.SplashScreen"`:**
   - Aplica el theme de la splash screen **només** a aquesta activitat
   - Sobreescriu el theme per defecte de l'aplicació

3. **`<intent-filter>`:**
   - **`MAIN`**: Indica que és un punt d'entrada principal
   - **`LAUNCHER`**: Fa que l'activitat aparegui al llançador d'aplicacions
   - **Aquesta activitat serà la primera que es mostri** quan s'obri l'app

### 5. Gestionar la Splash Screen al codi

A l'activitat `Inici.kt`:

```kotlin
class Inici : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // IMPORTANT: installSplashScreen() s'ha de cridar ABANS de super.onCreate()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inici)
        
        // Aquí continuaria el codi de la teva activitat
    }
}
```

**Per què `installSplashScreen()` va primer?**
- Ha de configurar-se abans que l'activitat es creï completament
- Si es crida després de `super.onCreate()`, la splash screen no funcionarà correctament

### Flux complet de la Splash Screen

```
1. L'usuari obre l'aplicació
    ↓
2. Android llegeix el Manifest i veu que Inici és LAUNCHER
    ↓
3. Android aplica Theme.App.SplashScreen a Inici
    ↓
4. Es mostra la splash screen amb l'animació
    ↓
5. L'animació s'executa durant el temps especificat
    ↓
6. installSplashScreen() gestiona la transició
    ↓
7. S'aplica el postSplashScreenTheme (Pink.SparklesTask)
    ↓
8. L'activitat continua carregant normalment amb el theme correcte
```

**Avantatges d'aquest sistema:**
- **Professional**: Proporciona una experiència polida
- **Temps de càrrega**: Oculta el temps que triga l'app a inicialitzar-se
- **Branding**: Mostra el logotip de l'aplicació
- **Transició suau**: Canvi fluid entre la splash screen i el contingut principal

---

## Menú de Navegació

El menú de navegació inferior (Bottom Navigation) permet als usuaris moure's entre les diferents seccions principals de l'aplicació mitjançant fragments.

### Components del menú

#### 1. Fitxer del menú - `bottom_menu.xml`

Defineix els ítems que apareixen al menú inferior:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/iHome"
        android:icon="@drawable/selector_home"
        android:title="Home"/>

    <item
        android:id="@+id/iPerfil"
        android:icon="@drawable/selector_perfil"
        android:title="Perfil"/>

    <item
        android:id="@+id/iSetting"
        android:icon="@drawable/selector_setting"
        android:title="Settings"/>
</menu>
```

**Estructura de cada `<item>`:**

- **`android:id`**: Identificador únic per referenciar l'ítem al codi
- **`android:icon`**: Drawable que es mostrarà (en aquest cas, selectors que canvien segons l'estat)
- **`android:title`**: Text que apareix sota la icona

**Selectors d'icones:**

Els selectors són drawables especials que canvien automàticament segons l'estat:

```xml
<!-- Exemple: selector_home.xml -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/ic_home_filled" android:state_checked="true"/>
    <item android:drawable="@drawable/ic_home_outline" android:state_checked="false"/>
</selector>
```

- **`state_checked="true"`**: Icona quan l'ítem està seleccionat (ple)
- **`state_checked="false"`**: Icona quan l'ítem NO està seleccionat (contorn)
- El canvi és automàtic, no cal programar-ho

#### 2. Layout principal - `menu_main.xml`

Defineix l'estructura visual de la pantalla amb menú:

```xml
<androidx.constraintlayout.widget.ConstraintLayout 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/fragmentContainer"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toTopOf="@id/menu_bottom"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/menu_bottom"
        android:layout_width="match_parent"
        android:layout_height="80dp"
        app:itemGravity="center"
        app:layout_constraintBottom_toBottomOf="parent"
        app:menu="@menu/bottom_menu"
        android:background="@color/pinkPrimary"
        app:itemIconTint="@color/white"
        app:itemTextColor="@color/white"
        app:itemActiveIndicatorStyle="@null"
        app:itemRippleColor="@android:color/transparent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**Anàlisi dels components:**

**FragmentContainerView:**
- **`android:layout_height="0dp"`**: En ConstraintLayout, `0dp` significa "match_constraint"
- **`app:layout_constraintBottom_toTopOf="@id/menu_bottom"`**: S'estén fins just a sobre del menú
- **`app:layout_constraintTop_toTopOf="parent"`**: Comença a dalt de tot
- **Resultat**: Ocupa tot l'espai disponible entre la part superior i el menú inferior

**BottomNavigationView - Propietats visuals:**

1. **`android:layout_height="80dp"`**: Altura fixa del menú
2. **`app:itemGravity="center"`**: Centra verticalment les icones i text
3. **`android:background="@color/pinkPrimary"`**: Color de fons del menú
4. **`app:itemIconTint="@color/white"`**: Color de les icones (blanc)
5. **`app:itemTextColor="@color/white"`**: Color del text (blanc)

**BottomNavigationView - Personalització del comportament:**

1. **`app:itemActiveIndicatorStyle="@null"`:**
   - Elimina l'indicador per defecte (la "bombolla" que apareix darrere l'ítem seleccionat)
   - Permet utilitzar només els selectors d'icones per indicar l'estat

2. **`app:itemRippleColor="@android:color/transparent"`:**
   - Elimina l'efecte ripple (onada) quan es fa clic
   - Proporciona una interacció més neta i minimalista

3. **`app:menu="@menu/bottom_menu"`:**
   - Enllaça amb el fitxer XML del menú creat anteriorment

#### 3. Activitat - `MenuBottom.kt`

Gestiona la lògica de navegació entre fragments:

```kotlin
class MenuBottom : AppCompatActivity() {

    lateinit var menu_nav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.menu_main)

        initComponents()
        initListeners()
    }

    private fun initComponents(){
        menu_nav = findViewById(R.id.menu_bottom)
        
        // Carregar el fragment inicial
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()
    }

    private fun initListeners(){
        menu_nav.setOnItemSelectedListener { item ->
            val selectedFragment : Fragment? = when (item.itemId) {
                R.id.iHome -> HomeFragment()
                R.id.iPerfil -> PerfilFragment()
                R.id.iSetting -> SettingsFragment()
                else -> null
            }
            
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit()
            }
            true
        }
    }
}
```

**Desglossament del codi:**

**`initComponents()`:**

```kotlin
supportFragmentManager.beginTransaction()
    .replace(R.id.fragmentContainer, HomeFragment())
    .commit()
```

- **`supportFragmentManager`**: Gestor de fragments de l'activitat
- **`beginTransaction()`**: Inicia una transacció de fragments (conjunt d'operacions)
- **`.replace()`**: Substitueix el contingut del contenidor pel fragment especificat
- **`.commit()`**: Confirma i executa la transacció
- **Per què?**: Carrega el `HomeFragment` quan s'obre l'aplicació (pantalla inicial)

**`initListeners()`:**

```kotlin
menu_nav.setOnItemSelectedListener { item ->
    // Lambda que s'executa quan es selecciona un ítem
}
```

**`when (item.itemId)`:**
- Estructura similar al `switch` d'altres llenguatges
- Comprova quin ítem del menú s'ha seleccionat
- Retorna el fragment corresponent

```kotlin
val selectedFragment : Fragment? = when (item.itemId) {
    R.id.iHome -> HomeFragment()      // Si és Home, crea HomeFragment
    R.id.iPerfil -> PerfilFragment()  // Si és Perfil, crea PerfilFragment
    R.id.iSetting -> SettingsFragment() // Si és Settings, crea SettingsFragment
    else -> null                       // Si no coincideix amb cap, retorna null
}
```

**Canvi de fragment:**

```kotlin
if (selectedFragment != null) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, selectedFragment)
        .commit()
}
true  // Retorna true per indicar que l'event s'ha gestionat
```

- **Comprova que `selectedFragment` no sigui null**: Seguretat per evitar errors
- **Substitueix el fragment actual** pel nou al `FragmentContainerView`
- **Retorna `true`**: Indica al sistema que l'event s'ha processat correctament

### Què són els Fragments?

Un **Fragment** és un component reutilitzable que representa una porció de la interfície d'usuari dins d'una Activity.

**Característiques dels fragments:**

- **Modularitat**: Cada fragment és independent i pot reutilitzar-se
- **Cicle de vida propi**: Tenen els seus propis mètodes `onCreate()`, `onCreateView()`, etc.
- **Flexibilitat**: Múltiples fragments poden existir simultàniament en una activitat
- **Navegació lleugera**: Canviar entre fragments és més eficient que canviar entre activitats

**Per què utilitzar fragments al menú?**

1. **Rendiment**: Canviar fragments és més ràpid que canviar activitats
2. **Memòria**: Només hi ha una activitat principal, reduint l'ús de recursos
3. **Estat compartit**: Facilita compartir dades entre seccions
4. **UX consistent**: El menú inferior es manté sempre visible

**Fragments de Sparkle's Task:**

1. **HomeFragment**:
   - Mostra el calendari i la llista de tasques
   - Permet afegir noves tasques
   - És el fragment per defecte a l'inici

2. **PerfilFragment**:
   - Mostra el perfil de l'usuari i les monedes (Sparks)
   - Conté la botiga d'accessoris amb RecyclerView
   - Implementa el sistema de filtres per categories

3. **SettingsFragment**:
   - Permet modificar preferències de l'usuari
   - Inclou opcions per canviar nom d'usuari, contrasenya i email
   - Botó per tancar sessió

### Flux complet de navegació

```
1. L'app s'inicia → MenuBottom.onCreate()
    ↓
2. initComponents() → Carrega HomeFragment per defecte
    ↓
3. L'usuari veu Home amb el calendari i tasques
    ↓
4. L'usuari clica la icona "Perfil" al menú
    ↓
5. setOnItemSelectedListener detecta el clic
    ↓
6. when(item.itemId) identifica R.id.iPerfil
    ↓
7. Crea una instància de PerfilFragment
    ↓
8. FragmentTransaction substitueix HomeFragment per PerfilFragment
    ↓
9. L'usuari veu ara la pantalla de Perfil amb els accessoris
    ↓
10. El selector d'icona canvia automàticament (outline → filled)
```

---

## Dialogs

Els **Dialogs** són finestres modals que apareixen sobre la interfície principal per sol·licitar informació a l'usuari o mostrar missatges importants. A Sparkle's Task, s'utilitzen per crear i modificar tasques.

### Què és un DialogFragment?

Un `DialogFragment` és una classe especial que combina les característiques d'un Dialog i un Fragment:

**Avantatges sobre Dialog tradicional:**
- **Gestió automàtica del cicle de vida**: S'adapta als canvis de configuració (rotacions, etc.)
- **Integració amb FragmentManager**: Es gestiona com qualsevol altre fragment
- **Més flexible**: Permet personalitzar completament l'aparença
- **Segur**: Evita memory leaks i crashes en canvis d'estat

### 1. Dialog per crear tasques - `CreateTask`

```kotlin
class CreateTask : DialogFragment() {

    lateinit var btnCancel: Button
    lateinit var btnCreate: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_afegir_tasca, null)

            btnCancel = view.findViewById(R.id.btnCancel)
            btnCreate = view.findViewById(R.id.btnCreate)

            builder.setView(view)
            initListeners()

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}

private fun CreateTask.initListeners() {
    btnCancel.setOnClickListener {
        dismiss()
    }

    btnCreate.setOnClickListener {
        dismiss()
    }
}
```

**Desglossament del codi:**

**`onCreateDialog()`:**

```kotlin
return activity?.let {
    // Codi del dialog
} ?: throw IllegalStateException("Activity cannot be null")
```

- **`activity?.let`**: Operador de seguretat de Kotlin
  - Si `activity` no és null, executa el bloc
  - Si és null, llança una excepció
- **Per què és necessari?**: Els fragments necessiten una activitat per funcionar

**Construcció del Dialog:**

```kotlin
val builder = AlertDialog.Builder(it)
```
- **`AlertDialog.Builder`**: Constructor de dialogs amb estil Material Design
- Proporciona mètodes per configurar el dialog fàcilment

```kotlin
val inflater = requireActivity().layoutInflater
val view = inflater.inflate(R.layout.activity_afegir_tasca, null)
```
- **`layoutInflater`**: Objecte que converteix XML en Views
- **`inflate()`**: Crea la vista a partir del layout XML
- **`R.layout.activity_afegir_tasca`**: Layout personalitzat del dialog

```kotlin
btnCancel = view.findViewById(R.id.btnCancel)
btnCreate = view.findViewById(R.id.btnCreate)
```
- Obté les referències als botons del layout inflat
- Es fa sobre `view`, no sobre `activity`, perquè són elements del layout personalitzat

```kotlin
builder.setView(view)
initListeners()
builder.create()
```
- **`setView(view)`**: Assigna el layout personalitzat al dialog
- **`initListeners()`**: Configura els listeners dels botons
- **`create()`**: Construeix i retorna el dialog

**`onStart()`:**

```kotlin
override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
}
```

- **Per què en `onStart()` i no en `onCreateDialog()`?**
  - En `onStart()`, el dialog ja està creat i la finestra està disponible
  - Permet modificar propietats visuals de la finestra

- **`setLayout(WRAP_CONTENT, WRAP_CONTENT)`:**
  - Ajusta la mida del dialog al contingut
  - Evita que ocupi tota la pantalla

- **`setBackgroundDrawableResource(android.R.color.transparent)`:**
  - Fa el fons transparent
  - Permet que el layout personalitzat amb corners arrodonits es vegi correctament
  - Sense això, hi hauria un fons gris rectangular per defecte

**Listeners:**

```kotlin
private fun CreateTask.initListeners() {
    btnCancel.setOnClickListener {
        dismiss()
    }

    btnCreate.setOnClickListener {
        dismiss()
    }
}
```

- **`dismiss()`**: Tanca el dialog
- **`btnCancel`**: Tanca sense guardar res
- **`btnCreate`**: Aquí s'afegiria la lògica per crear la tasca abans de tancar

### 2. Dialog per actualitzar tasques - `ActualitzaTasca`

```kotlin
class ActualitzaTasca : DialogFragment() {

    lateinit var btnCancel: Button
    lateinit var btnSave: Button
    lateinit var btnDelete: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_actualitza_tasca, null)

            btnCancel = view.findViewById(R.id.btnCancel)
            btnSave = view.findViewById(R.id.btnSave)
            btnDelete = view.findViewById(R.id.btnDelete)

            builder.setView(view)
            initListeners()
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun initListeners() {
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            dismiss()
        }
        btnDelete.setOnClickListener {
            dismiss()
        }
    }
}
```

**Diferències amb CreateTask:**

1. **Tres botons** en lloc de dos:
   - `btnCancel`: Cancel·la sense canvis
   - `btnSave`: Guarda les modificacions
   - `btnDelete`: Elimina la tasca

2. **Layout diferent**: Utilitza `activity_actualitza_tasca.xml`

3. **Propòsit**: Modificar una tasca existent en lloc de crear-ne una de nova

### Layout dels Dialogs

**`activity_afegir_tasca.xml`** (Crear tasca):

```xml
<androidx.constraintlayout.widget.ConstraintLayout>
    
    <TextView
        android:id="@+id/tvModifica_title"
        android:text="@string/titolAfegeix"
        android:background="@drawable/round_pink" />
    
    <androidx.cardview.widget.CardView
        android:id="@+id/cardModifica">
        
        <LinearLayout>
            <!-- Camps per al nom de la tasca -->
            <EditText android:hint="@string/nomTasca" />
            
            <!-- Camps per als Sparks -->
            <EditText android:hint="@string/sparksObtinguts" />
            
            <!-- Camps per a la data -->
            <EditText android:hint="@string/datePlaceholder" />
        </LinearLayout>
        
    </androidx.cardview.widget.CardView>
    
    <LinearLayout>
        <Button android:id="@+id/btnCancel" android:text="@string/cancel" />
        <Button android:id="@+id/btnCreate" android:text="@string/creaTasca" />
    </LinearLayout>
    
</androidx.constraintlayout.widget.ConstraintLayout>
```

**Estructura visual:**
1. **Títol**: TextView amb fons arrodonit que indica "Afegir Tasca"
2. **Card blanc**: Conté els camps d'entrada de dades
3. **Botons**: Cancel·lar i Crear, en una fila horitzontal

**`activity_actualitza_tasca.xml`** (Modificar tasca):

Idèntic a l'anterior però amb:
- Títol diferent: "Actualitzar Tasca"
- Botó addicional: "Eliminar"
- ID de botó canviat: `btnCreate` → `btnSave`

### Com utilitzar els Dialogs

Des del `HomeFragment`:

```kotlin
btnAfegir.setOnClickListener {
    CreateTask().show(parentFragmentManager, "Crear Tasca")
}
```

**Explicació:**

- **`CreateTask()`**: Crea una nova instància del DialogFragment
- **`.show()`**: Mostra el dialog
  - **`parentFragmentManager`**: El FragmentManager del fragment pare (necessari perquè el dialog és un fragment)
  - **`"Crear Tasca"`**: Tag per identificar el dialog (útil per depuració)

### Flux complet d'un Dialog

```
1. L'usuari clica el botó "Afegir" a HomeFragment
    ↓
2. btnAfegir.setOnClickListener s'executa
    ↓
3. CreateTask().show(parentFragmentManager, "Crear Tasca")
    ↓
4. El FragmentManager crida onCreateDialog()
    ↓
5. S'infla el layout activity_afegir_tasca.xml
    ↓
6. Es crea l'AlertDialog amb el layout personalitzat
    ↓
7. onStart() configura la mida i transparència
    ↓
8. El dialog es mostra sobre la pantalla actual
    ↓
9. L'usuari omple els camps i clica "Crear"
    ↓
10. btnCreate.setOnClickListener s'executa
    ↓
11. dismiss() tanca el dialog
    ↓
12. L'usuari torna a veure HomeFragment
```

**Per què utilitzar DialogFragment?**

1. **Modal**: Bloqueja la interacció amb la pantalla de fons
2. **Focus**: L'usuari se centra en una tasca específica
3. **Temporal**: Només apareix quan és necessari
4. **Reutilitzable**: Es pot utilitzar des de diferents llocs de l'app
5. **Fàcil de tancar**: Clicar fora del dialog o prémer "Enrere" el tanca automàticament

---

## Resum de l'arquitectura

L'aplicació Sparkle's Task utilitza un conjunt de components ben integrats:

1. **RecyclerView amb Adapter i ViewHolder**: Mostra llistes eficients amb filtres
2. **Splash Screen**: Proporciona una entrada visual professional
3. **Bottom Navigation amb Fragments**: Navegació intuïtiva entre seccions
4. **DialogFragments**: Interacció modal per crear i modificar tasques

Aquesta estructura segueix les millors pràctiques d'Android i proporciona una base sòlida per a futures ampliacions de l'aplicació.

