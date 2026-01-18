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

### Tecnologies utilitzades

- **Llenguatge**: Kotlin
- **Plataforma**: Android
- **IDE**: Android Studio
- **Components**: Fragments, RecyclerView, BottomNavigationView

### Arquitectura de l'aplicació

#### Menú de navegació

El menú de **Sparkle's Task** està ubicat a la part inferior de la pantalla (BottomNavigationView) i permet navegar entre les diferents seccions de l'aplicació mitjançant fragments.

**Implementació:**

El layout principal està dividit en dues parts:

1. **FragmentContainerView**: Contenidor amb un ID únic que permet canviar dinàmicament entre els diferents fragments
2. **BottomNavigationView**: Menú inferior configurat amb les opcions de navegació

**Codi del layout principal:**

```xml
    
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
        app:layout_constraintBottom_toBottomOf="parent"
        app:menu="@menu/bottom_menu" />
        
</androidx.constraintlayout.widget.ConstraintLayout>
```

**Funcionalitat:**

Quan l'usuari selecciona una opció del menú inferior, l'aplicació carrega el fragment corresponent al `FragmentContainerView`, substituint el contingut anterior. Això permet una navegació fluida entre les diferents seccions sense necessitat de canviar d'activitat.

#### Fragments principals

L'aplicació està organitzada en diferents fragments, cadascun amb funcionalitats específiques:

- **HomeFragment**: Pantalla principal amb les tasques actives
- **PerfilFragment**: Gestió del perfil i botiga d'accessoris
- **SettingsFragment**: Configuració de l'aplicació

#### Sistema de visualització (RecyclerView)

Per mostrar llistes de tasques i accessoris, s'utilitza **RecyclerView** amb **LinearLayoutManager** per la organització linear de les tasques i amb **GridLayoutManager** pel's accesoris, que permet organitzar els elements en forma de graella.

**Característiques:**
- Disposició en graella de 3 columnes
- Disseny responsive amb aspect ratio 1:1, que permet l'adaptació dels elements als diferents tamanys dels dispositius dels usuaris
- Sistema de filtres per categories dels accesoris

---

