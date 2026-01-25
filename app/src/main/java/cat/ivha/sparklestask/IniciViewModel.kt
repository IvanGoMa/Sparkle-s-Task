package cat.ivha.sparklestask

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IniciViewModel : ViewModel() {

    private val usuari = UsuariRegistrat(
        email = "usuario@example.com",
        password = "1234"
    )


    private val _email = MutableLiveData<String>("")
    private val _passw = MutableLiveData<String>("")
    private val _emailError = MutableLiveData<String?>()
    private val _passwError = MutableLiveData<String?>()
    private val _isLoginButtonOn = MutableLiveData<Boolean>(false)
    private val _loginSuccesEvent = MutableLiveData<Boolean>()


    val email: LiveData<String> = _email
    val passw: LiveData<String> = _passw
    val emailError: LiveData<String?> = _emailError
    val passwError: LiveData<String?> = _passwError
    val isLoginButtonOn: LiveData<Boolean> = _isLoginButtonOn
    val loginSuccesEvent: LiveData<Boolean> = _loginSuccesEvent

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        validateEmail(newEmail)
        updateLoginButtonState()
    }

    fun onPasswChanged(newPassw: String) {
        _passw.value = newPassw
        validatePassw(newPassw)
        updateLoginButtonState()
    }
    fun onLoginClick() {
        val currentEmail = _email.value ?: ""
        val currentPassw = _passw.value ?: ""

        if (validateEmail(currentEmail) && validatePassw(currentPassw)) {
            if (validateCredencials(currentEmail, currentPassw)) {
                procesLogin(currentEmail, currentPassw)
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                _emailError.value = "L'email no pot estar buit"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _emailError.value = "Format d'email invàlid"
                false
            }
            else -> {
                _emailError.value = null
                true
            }
        }
    }

    private fun validatePassw(passw: String): Boolean {
        return when {
            passw.isEmpty() -> {
                _passwError.value = "Vigila! T'oblides la contrasenya"
                false
            }
            else -> {
                _passwError.value = null
                true
            }
        }
    }

    private fun validateCredencials(email: String, passw: String): Boolean {
        if (email != usuari.email) {
            _emailError.value = "Aquest email no està registrat"
            return false
        }

        if (passw != usuari.password) {
            _passwError.value = "Contrasenya incorrecta"
            return false
        }

        _emailError.value = null
        _passwError.value = null
        return true
    }

    private fun updateLoginButtonState() {
        val emailValid = _emailError.value == null && !_email.value.isNullOrEmpty()
        val passwValid = _passwError.value == null && !_passw.value.isNullOrEmpty()

        _isLoginButtonOn.value = emailValid && passwValid
    }

    private fun procesLogin(email: String, passw: String) {
        _loginSuccesEvent.value = true
    }

    fun onLoginSuccessEventHandled() {
        _loginSuccesEvent.value = false
    }
}