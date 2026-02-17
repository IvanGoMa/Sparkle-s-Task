package cat.ivha.sparklestask

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterViewModel: ViewModel() {

    private val _registerSucces = MutableLiveData<Boolean>()
    val registerSucces: LiveData<Boolean> = _registerSucces
    private val _username = MutableLiveData("")
    private val _usernameError = MutableLiveData<String?>()
    var username: LiveData<String> = _username
    var usernameError: LiveData<String?> = _usernameError
    private val _password = MutableLiveData("")
    private val _passwordError = MutableLiveData<String?>()
    var password: LiveData<String> = _password
    var passwordError: LiveData<String?> = _passwordError
    private val _passwordValidation = MutableLiveData("")
    private val _passwordValidationError = MutableLiveData<String?>()
    var passwordValidation: LiveData<String> = _passwordValidation
    var passwordValidationError: LiveData<String?> = _passwordValidationError
    private val _email = MutableLiveData("")
    private val _emailError = MutableLiveData<String?>()
    var email: LiveData<String> = _email
    var emailError: LiveData<String?> = _emailError
    private val _date = MutableLiveData("")
    private val _dateError = MutableLiveData<String?>()
    var date: LiveData<String> = _date
    var dateError: LiveData<String?> = _dateError
    private val _correct = false


    fun onUserChanged(user:String){
        _username.value = user
        if (!checkUsername()) _usernameError.value = "El nom d'usuari ha de tenir més de 5 caràcters"
        else _usernameError.value = null
    }

    fun onEmailChanged(email:String){
        _email.value = email
        if (!checkEmail()) _emailError.value = "Email invàlid"
        else _emailError.value = null
    }

    fun onPsswChanged(pssw:String){
        _password.value = pssw
        if (!checkPassword()) _passwordError.value = "La contrasenya ha de contenir minúscules, majúscules, nombres, símbols i tenir més de 7 caràcters"
        else _passwordError.value = null
    }

    fun onPsswXChanged(psswX:String){
        _passwordValidation.value = psswX
        if (!checkPasswordValidation()) _passwordValidationError.value = "La contrasenya no és igual als dos camps"
        else _passwordValidationError.value = null
    }

    fun onDateChanged(date:String){
        _date.value = date
        if (!checkDate()) _dateError.value = "Introdueix una data de naixement correcta"
        else _dateError.value = null
    }

    fun onRegisterClick(){
        if (checkAll()){
            _registerSucces.value = true
        }
    }




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
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try{
            birthDate.time = df.parse(date.value.toString())!!
        } catch (e: Exception){
            return false
        }


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

    fun onRegisterEventHandled() {
        _registerSucces.value = false
    }

}