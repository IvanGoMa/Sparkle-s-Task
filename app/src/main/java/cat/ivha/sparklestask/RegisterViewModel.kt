package cat.ivha.sparklestask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterViewModel: ViewModel() {
    val username = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordValidation = MutableLiveData("")
    val email = MutableLiveData("")
    val date = MutableLiveData(Calendar.getInstance())
    val correct = false

    fun checkUsername(): Boolean{
        return (username.toString().length < 5)
    }

    fun checkPassword(): Boolean{
        val pwd = password.toString()
        var minus = false
        var mayus = false
        var digit = false
        var symbol = false

        for (char in pwd){
            if (char.isDigit()) digit = true
            else if (char.isUpperCase()) mayus = true
            else if (char.isLowerCase()) minus = true
            else symbol = true
        }

        return ((pwd.length > 7) and minus and mayus and digit and symbol)
    }
    fun checkPasswordValidation(): Boolean{
        return passwordValidation == password
    }
    fun checkEmail():Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
    }
    fun checkDate():Boolean{
        val toDate = Calendar.getInstance()
        val birthDate = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        birthDate.time = df.parse(date.toString())!!

        if (toDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR) > 18) {
            return true
        }

        if (toDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR) == 18) {

            if (toDate.get(Calendar.MONTH) > birthDate.get(Calendar.MONTH)) {
                return true
            }

            if (toDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && birthDate.get(Calendar.DAY_OF_MONTH) <= toDate.get(
                    Calendar.DAY_OF_MONTH
                )
            ) {
                return true
            }

        }
        return false
    }

}