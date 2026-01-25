# Sparkle's Task

## Introducció

Sparkle's Task és una aplicació d'organització de tasques amb un enfocament lúdic i gamificat. L'aplicació transforma les tasques diàries en una experiència semblant a un joc, facilitant la seva finalització mitjançant un sistema de recompenses.

Característiques principals:
· Gestió de tasques diàries
· Experiència gamificada
· Sistema de recompenses amb Sparks (monedes virtuals)
· Personalització del perfil amb accessoris
· Motivació per completar tasques

## Com funciona

El funcionament de l'aplicació és molt senzill i intuitiu. Primer, l'usuari crea tasques per organitzar les seves activitats diàries. Després, a mesura que completa aquestes tasques, les marca com a finalitzades i guanya Sparks, que són les monedes virtuals de l'aplicació. Finalment, amb aquests Sparks acumulats pot comprar accessoris per personalitzar i millorar el seu perfil dins de l'aplicació.

---

## Part Tècnica

Per desenvolupar Sparkle's Task hem utilitzat Kotlin com a llenguatge de programació principal, treballant sobre la plataforma Android amb Android Studio com a entorn de desenvolupament integrat. L'aplicació fa ús de diversos components importants com Fragments per gestionar diferents pantalles, RecyclerView per mostrar llistes eficients, BottomNavigationView per la navegació inferior, DialogFragment per finestres modals, ViewModel per gestionar l'estat i View Binding per accedir de manera segura a les vistes.

---

## RecyclerView: Adapter i ViewHolder

El RecyclerView és un dels components més importants de l'aplicació perquè permet mostrar llistes molt grans de dades de manera eficient. En el nostre cas, l'utilitzem principalment per mostrar la col·lecció d'accessoris disponibles a la botiga del perfil de l'usuari.

### Estructura de dades

Per organitzar la informació dels accessoris, hem creat una data class anomenada Item que defineix l'estructura de cada accessori. Aquesta classe conté quatre propietats bàsiques: el nom de l'accessori, la seva descripció, la imatge associada i la categoria a la qual pertany. Utilitzar una data class en lloc d'una classe normal té l'avantatge que Kotlin genera automàticament mètodes útils com equals, hashCode i toString.

```kotlin
data class Item (
    val nom: String,
    val desc: String,
    val img: Int,
    val categoria: Categoria
)
```

Per garantir que les categories siguin sempre vàlides i evitar errors tipogràfics, hem definit un enum class amb les tres categories possibles: COLLARS, GORROS i ULLERES. Això fa que sigui impossible assignar una categoria que no existeixi.

Totes les dades dels accessoris es guarden en un object anomenat DataSource. Utilitzar object en lloc de class implementa el patró Singleton, que garanteix que només existeix una única instància durant tota l'execució de l'aplicació. Això és ideal per a un repositori de dades centralitzat. La llista és de tipus MutableList perquè necessitem poder afegir o eliminar elements dinàmicament si fos necessari en el futur.

### Patró ViewHolder

El ViewHolder és un patró de disseny fonamental per millorar el rendiment del RecyclerView. El problema que resol és que findViewById és una operació computacionalment costosa, i sense ViewHolder es faria aquesta cerca cada vegada que es mostra un element. El ViewHolder emmagatzema les referències a les vistes una sola vegada quan es crea.

A més, quan l'usuari fa scroll i alguns elements surten de la pantalla, el RecyclerView no els destrueix sinó que els reutilitza per mostrar els nous elements que entren. Això redueix significativament l'ús de memòria i millora la fluïdesa del scroll.

El nostre ViewHolder rep la vista inflada i una funció lambda per gestionar els clics. Té un mètode bind que s'encarrega d'assignar les dades de cada Item als elements visuals corresponents:

```kotlin
fun bind (item: Item){
    ivImg.setImageResource(item.img)
    itemView.setOnClickListener { onItemClick(item) }
}
```

### Adapter com a pont

L'Adapter és el component que fa de pont entre les nostres dades i la interfície visual del RecyclerView. Una característica important del nostre adapter és que manté dues llistes diferents: itemsComplets que conté sempre tots els accessoris originals sense modificar, i itemsFiltrados que conté només els elements que s'han de mostrar després d'aplicar els filtres que l'usuari ha seleccionat.

L'adapter té tres mètodes principals que RecyclerView crida automàticament. getItemCount retorna el nombre d'elements que s'han de mostrar, i és molt important que utilitzi la llista filtrada i no la completa. onCreateViewHolder s'executa quan cal crear una nova vista, utilitzant el LayoutInflater per convertir el fitxer XML en un objecte View. onBindViewHolder s'executa cada vegada que un element es mostra a la pantalla, obtenint l'Item corresponent i cridant el mètode bind del ViewHolder.

### Sistema de filtres

El sistema de filtres és una funcionalitat important que permet a l'usuari veure només els accessoris d'una categoria específica. Hem implementat un mètode filtra que rep una categoria que pot ser null. Si és null significa que no volem cap filtre i per tant mostrem tots els elements. Si té un valor, utilitzem la funció filter de Kotlin per crear una nova llista que només conté els elements que coincideixen amb la categoria seleccionada.

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

Al fragment, la implementació dels filtres utilitza una lògica de toggle. Això significa que si l'usuari clica un botó de categoria que ja estava actiu, el filtre s'elimina i es mostren tots els accessoris de nou. Si clica un botó diferent, canvia directament al nou filtre. Aquesta funcionalitat es controla amb la variable ultimClicat que recorda quin filtre està actiu actualment.

Per mostrar els accessoris en format de graella en lloc de llista vertical, utilitzem GridLayoutManager amb 3 columnes. Això fa que tres accessoris es mostrin per fila i s'adapti automàticament a l'amplada de la pantalla del dispositiu.

---

## Splash Screen

La Splash Screen és la pantalla inicial que apareix quan l'usuari obre l'aplicació per primera vegada. Aquesta pantalla té diverses funcions importants: ofereix una aparença professional a l'aplicació, oculta el temps que triga l'aplicació a inicialitzar-se carregant tots els recursos necessaris, mostra el logotip i la identitat visual de l'aplicació, i proporciona una transició suau fins al contingut principal.

### Implementació pas a pas

Per implementar la Splash Screen, primer cal afegir la dependència corresponent al fitxer build.gradle. Aquesta llibreria proporciona totes les funcionalitats necessàries i és compatible amb versions antigues d'Android des de l'API 21.

Després cal crear un theme específic per a la Splash Screen al fitxer themes.xml. Aquest theme hereta de Theme.SplashScreen i defineix tres propietats clau. La primera és windowSplashScreenAnimatedIcon que especifica quina imatge o animació es mostrarà. La segona, i molt important, és postSplashScreenTheme que indica quin theme utilitzar un cop la Splash Screen desapareix. Sense aquesta propietat l'aplicació perdria tot l'estil visual. La tercera és windowSplashScreenAnimationDuration que controla quant de temps dura l'animació en mil·lisegons.

L'animació es crea amb objectAnimator, que és una eina molt potent d'Android per animar propietats dels objectes. En el nostre cas hem creat tres animacions que s'executen simultàniament gràcies a l'etiqueta set. Les dues primeres controlen l'escala horitzontal i vertical, fent que la imatge creixi des d'un 10% fins a un 60% de la seva mida original. La tercera controla l'opacitat, fent que la imatge aparegui gradualment des d'invisible fins a completament visible.

Al fitxer AndroidManifest.xml cal configurar l'activitat Inici perquè utilitzi el theme de la Splash Screen. L'atribut exported="true" és obligatori perquè permet que el sistema Android pugui llançar aquesta activitat. L'intent-filter amb MAIN i LAUNCHER fa que aquesta sigui la primera activitat que es mostra quan l'usuari obre l'aplicació.

Finalment, al codi de l'activitat Inici cal cridar installSplashScreen abans de super.onCreate. L'ordre és molt important perquè la Splash Screen s'ha de configurar abans que l'activitat es creï completament, altrament no funcionaria correctament.

---

## Menú de Navegació

El menú de navegació inferior és un component essencial de l'aplicació perquè permet als usuaris moure's fàcilment entre les diferents seccions principals. Hem optat per un BottomNavigationView perquè és més accessible amb una mà en dispositius grans i segueix les directrius de Material Design.

### Estructura del menú

El menú es defineix en un fitxer XML anomenat bottom_menu.xml que conté els diferents ítems. Cada item té un identificador únic per poder referenciar-lo al codi, una icona que es mostra visualment i un títol que apareix sota la icona.

Una característica important és que utilitzem selectors per a les icones. Un selector és un tipus especial de drawable que canvia automàticament segons l'estat de l'element. En el nostre cas, quan un item està seleccionat mostra una icona plena, i quan no està seleccionat mostra només el contorn. Aquest canvi és completament automàtic i no cal programar-lo.

El layout principal menu_main.xml està dividit en dues parts principals. A la part superior hi ha un FragmentContainerView que ocupa tot l'espai disponible entre el top de la pantalla i el menú inferior. Aquest contenidor és on es carreguen i es mostren els diferents fragments segons la secció que l'usuari ha seleccionat.

A la part inferior hi ha el BottomNavigationView que té una altura fixa de 80dp. Hem personalitzat el seu comportament amb diverses propietats. itemActiveIndicatorStyle amb valor null elimina la bombolla de fons que apareix per defecte darrere l'ítem seleccionat. itemRippleColor amb valor transparent elimina l'efecte d'onada que apareix quan es fa clic. Aquestes personalitzacions donen una aparença més neta i minimalista al menú.

### Gestió de la navegació

L'activitat MenuBottom.kt és la responsable de gestionar tota la lògica de navegació. Quan l'aplicació s'inicia, el mètode initComponents carrega el HomeFragment com a fragment per defecte. Això es fa mitjançant el supportFragmentManager que és el gestor de fragments de l'activitat.

El mètode initListeners configura un listener que s'executa cada vegada que l'usuari selecciona un item del menú. Utilitza una estructura when per determinar quin fragment correspon a cada item del menú. Si el fragment no és null, s'inicia una transacció de fragments que substitueix el contingut actual del FragmentContainerView pel nou fragment seleccionat.

### Avantatges dels Fragments

Un Fragment és un component reutilitzable que representa una porció de la interfície d'usuari. Utilitzar fragments en lloc d'activitats diferents té diversos avantatges importants. Primer, canviar entre fragments és molt més ràpid i eficient que canviar entre activitats perquè no cal crear una nova activitat sencera. Segon, redueix l'ús de memòria perquè només hi ha una activitat principal activa. Tercer, facilita compartir dades entre diferents seccions de l'aplicació. I finalment, proporciona una millor experiència d'usuari perquè el menú inferior es manté sempre visible i accessible.

A Sparkle's Task tenim tres fragments principals. HomeFragment mostra el calendari on l'usuari pot veure les dates importants i la llista de tasques pendents. PerfilFragment mostra el perfil de l'usuari amb les seves monedes Sparks acumulades i la botiga d'accessoris amb el sistema de filtres implementat amb RecyclerView. SettingsFragment permet modificar les preferències de l'usuari com el nom d'usuari, contrasenya i email, i també inclou un botó per tancar la sessió.

---

## Dialogs

Els Dialogs són finestres modals que apareixen per sobre de la interfície principal quan necessitem sol·licitar informació específica a l'usuari o mostrar missatges importants. A Sparkle's Task els utilitzem específicament per crear noves tasques i per modificar o eliminar tasques existents.

### DialogFragment vs Dialog tradicional

Hem optat per utilitzar DialogFragment en lloc del Dialog tradicional per diversos motius importants. Primer, DialogFragment gestiona automàticament el cicle de vida, això significa que s'adapta correctament als canvis de configuració com rotacions de pantalla sense perdre informació. Segon, s'integra perfectament amb el FragmentManager igual que qualsevol altre fragment. Tercer, permet personalitzar completament l'aparença del dialog carregant layouts XML personalitzats. I finalment, evita problemes comuns com memory leaks i crashes quan l'activitat canvia d'estat.

### Implementació dels dialogs

Tenim dos dialogs principals a l'aplicació. CreateTask s'utilitza per crear noves tasques i conté camps per introduir el nom de la tasca, els Sparks que s'obtindran en completar-la i la data límit. ActualitzaTasca s'utilitza per modificar tasques existents i té la mateixa estructura però afegeix un tercer botó per eliminar la tasca.

El mètode onCreateDialog és on es construeix el dialog. Primer es crea un AlertDialog.Builder que és el constructor estàndard de dialogs amb estil Material Design. Després s'utilitza el layoutInflater per convertir el fitxer XML del layout personalitzat en una vista. Un cop inflat el layout, s'obtenen les referències als botons i altres elements que necessitem controlar. Finalment s'assigna aquesta vista personalitzada al builder i es crea el dialog.

```kotlin
override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
        val builder = AlertDialog.Builder(it)
        val view = requireActivity().layoutInflater
            .inflate(R.layout.activity_afegir_tasca, null)
        builder.setView(view)
        builder.create()
    } ?: throw IllegalStateException("Activity cannot be null")
}
```

El mètode onStart s'executa just després que el dialog s'hagi creat i la finestra estigui disponible. Aquí és on configurem la mida del dialog perquè s'ajusti al contingut amb WRAP_CONTENT en lloc d'ocupar tota la pantalla. També fem el fons transparent perquè el nostre layout personalitzat amb corners arrodonits es vegi correctament. Sense aquesta configuració, hi hauria un fons gris rectangular per defecte que quedaria malament.

Per mostrar un dialog des d'un fragment, simplement es crea una nova instància i es crida el mètode show passant-li el parentFragmentManager. El segon paràmetre és un tag que serveix per identificar el dialog, útil per depuració.

---

## View Binding

View Binding és una característica molt útil que genera automàticament una classe de binding per cada layout XML de l'aplicació. Aquesta classe conté referències directes a totes les vistes que tenen un ID definit al layout. L'objectiu principal és eliminar la necessitat d'utilitzar findViewById i proporcionar més seguretat en accedir als elements de la interfície.

### Avantatges principals

El primer gran avantatge és la seguretat de tipus. Amb findViewById, si et confons i intentes accedir a una vista amb el tipus incorrecte, l'error només es detectarà quan l'aplicació s'executi i pot provocar un crash. Amb View Binding, aquests errors es detecten en temps de compilació, abans que l'aplicació s'executi.

El segon avantatge és la null safety. View Binding només genera referències per vistes que realment existeixen al layout. Si intentes accedir a una vista que no existeix, el codi ni tan sols compilarà. Això evita molts errors de NullPointerException.

El tercer avantatge és l'eficiència. Encara que findViewById no és extremadament lent, View Binding elimina completament aquestes crides repetitives, fent el codi una mica més eficient.

Finalment, redueix significativament la quantitat de codi repetitiu. En lloc d'haver de declarar variables i fer findViewById per cada element de la interfície, simplement accedim directament a través del binding.

### Ús a l'aplicació

A la nostra classe Inici hem implementat View Binding de la següent manera. Primer declarem una variable lateinit de tipus IniciBinding, que és la classe que s'ha generat automàticament a partir del layout inici.xml. Al mètode onCreate, utilitzem el mètode inflate del layoutInflater per crear la instància del binding. Després, en lloc de passar directament el layout a setContentView, passem binding.root que és la vista arrel del layout.

```kotlin
private lateinit var binding: IniciBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = IniciBinding.inflate(layoutInflater)
    setContentView(binding.root)
}
```

A partir d'aquest moment, podem accedir a qualsevol vista del layout directament a través del binding. Per exemple, en lloc d'utilitzar findViewById(R.id.etUser), simplement fem servir binding.etUser. Això fa el codi molt més net i llegible, especialment quan tens molts elements a la interfície.

---

## ViewModel

ViewModel és un dels components més importants de l'arquitectura moderna d'Android. Forma part del patró arquitectònic MVVM (Model-View-ViewModel) i el seu paper principal és gestionar totes les dades relacionades amb la interfície d'usuari de manera que sobrevisquin als canvis de configuració del dispositiu.

### Per què utilitzar ViewModel

El problema que resol ViewModel és molt comú en Android. Quan l'usuari gira el dispositiu, l'activitat es destrueix completament i es torna a crear amb la nova orientació. Sense ViewModel, totes les dades que l'usuari havia introduït es perdrien i hauria de tornar a començar. ViewModel sobreviu a aquests canvis de configuració, mantenint les dades intactes.

A més, ViewModel ajuda a separar les responsabilitats dins de l'aplicació. La lògica de negoci, les validacions i la gestió de dades estan totes dins del ViewModel, mentre que l'Activity o Fragment només s'encarrega de mostrar les dades i capturar les interaccions de l'usuari. Això fa que el codi sigui molt més organitzat i fàcil de mantenir.

També facilita enormement fer testing perquè es pot provar tota la lògica del ViewModel sense necessitat de crear la interfície d'usuari. Això fa els tests més ràpids i fiables.

### LiveData i observabilitat

ViewModel treballa conjuntament amb LiveData, que és un tipus especial de dades observable. LiveData notifica automàticament a la interfície quan les dades canvien, fent que la UI sigui reactiva. Això elimina la necessitat de refrescar manualment la pantalla cada vegada que alguna cosa canvia.

Al nostre IniciViewModel utilitzem un patró important: tenim variables privades de tipus MutableLiveData que poden ser modificades (per això tenen el prefix _ al nom), i variables públiques de tipus LiveData que són només lectura. Això garanteix que només el ViewModel pot modificar els valors, mentre que l'Activity només pot observar-los i llegir-los.

```kotlin
private val _email = MutableLiveData<String>("")
val email: LiveData<String> = _email
```

### Implementació a l'aplicació

Al nostre IniciViewModel gestionem tot el procés d'inici de sessió. Tenim LiveData per l'email, la contrasenya, els errors de validació, l'estat del botó d'inici i l'event d'èxit del login. Quan l'usuari escriu a un camp, es crida un mètode com onEmailChanged que actualitza el valor, valida el camp i actualitza l'estat del botó.

Les validacions són molt completes. Per l'email comprovem que no estigui buit, que tingui un format vàlid utilitzant Patterns.EMAIL_ADDRESS, i finalment que estigui registrat al sistema. Per la contrasenya comprovem que no estigui buida i que coincideixi amb la contrasenya registrada.

```kotlin
fun onEmailChanged(newEmail: String) {
    _email.value = newEmail
    validateEmail(newEmail)
    updateLoginButtonState()
}
```

A l'Activity utilitzem el mètode observe per escoltar els canvis. Quan emailError canvia, actualitzem el missatge d'error del camp. Quan isLoginButtonOn canvia, habilitem o deshabilitem el botó. Quan loginSuccesEvent indica èxit, mostrem un missatge i naveguem a la pantalla principal.

Aquest sistema reactiu fa que la interfície respongui automàticament a qualsevol canvi d'estat sense haver de gestionar manualment les actualitzacions, resultant en un codi molt més net i menys propens a errors.

---

## Resum de l'arquitectura

L'aplicació Sparkle's Task està construïda seguint les millors pràctiques i patrons de desenvolupament d'Android. Utilitza RecyclerView amb el patró Adapter i ViewHolder per mostrar llistes eficients amb funcionalitat de filtres. Implementa una Splash Screen professional que ofereix una bona primera impressió. El sistema de navegació amb Bottom Navigation i Fragments proporciona una experiència d'usuari fluida i eficient.

Per a la interacció amb l'usuari utilitzem DialogFragments que ofereixen finestres modals segures i ben integrades amb el cicle de vida. View Binding elimina codi repetitiu i proporciona accés segur a les vistes. Finalment, el patró ViewModel amb LiveData gestiona l'estat de manera reactiva seguint l'arquitectura MVVM.

Aquesta combinació de tecnologies i patrons crea una base sòlida, escalable i mantenible per a l'aplicació, facilitant futures ampliacions i millores.
