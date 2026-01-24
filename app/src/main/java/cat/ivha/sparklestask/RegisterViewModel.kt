package cat.ivha.sparklestask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterViewModel: ViewModel() {
    private val _username = MutableLiveData("")
    var username: LiveData<String> = _username
    private val _password = MutableLiveData("")
    var password: LiveData<String> = _password
    private val _passwordValidation = MutableLiveData("")
    var passwordValidation: LiveData<String> = _passwordValidation
    private val _email = MutableLiveData("")
    var email: LiveData<String> = _email
    private val _date = MutableLiveData("")
    var date: LiveData<String> = _date
    private val _correct = false





    // Funció que comprova tots els camps
    fun checkAll() = checkPassword() && checkUsername() && checkEmail() && checkPasswordValidation() && checkDate()

    // Funció per a poder passar les dades de l'activity al viewModel
    fun getData(usernameInput: String, emailInput: String, passwordInput: String, passwordValidationInput: String, dateInput:String){
        _username.value = usernameInput
        _email.value = emailInput
        _password.value = passwordInput
        _passwordValidation.value = passwordValidationInput
        _date.value = dateInput

    }

    fun checkUsername(): Boolean{
        return (username.value.toString().length > 5)
    }

    fun checkPassword(): Boolean{
        val pwd = password.value.toString()
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

        return ((pwd.length > 7) && minus && mayus && digit && symbol)
    }
    fun checkPasswordValidation(): Boolean{
        return passwordValidation.value == password.value
    }
    fun checkEmail():Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()
    }
    fun checkDate():Boolean{
        val toDate = Calendar.getInstance()
        val birthDate = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        birthDate.time = df.parse(date.value.toString())!!

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