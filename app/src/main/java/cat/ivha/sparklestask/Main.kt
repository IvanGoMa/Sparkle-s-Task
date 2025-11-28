package cat.ivha.sparklestask

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menu : BottomNavigationView = findViewById  (R.id.nvMenu)
        menu.setOnItemSelectedListener{
            R.id.home_fragment -> HomeFragment()
        }
    }

}