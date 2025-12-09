package cat.ivha.sparklestask

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

lateinit var menu_nav : BottomNavigationView
class MenuBottom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.menu_main)

        initComponents()
        initListeners()
    }

    private fun initComponents(){
        menu_nav = findViewById(R.id.menu_bottom)
    }

    private fun initListeners(){
        menu_nav.setOnItemSelectedListener { item ->
            val selectedFragment : Fragment? = when (item.itemId) {
                R.id.iHome -> HomeFragment()
                R.id.iPerfil -> PerfilFragment()
                R.id.iSetting -> SettingFragment()
            }
        }
    }
}