package mz.co.zonal.viewmodel

import androidx.lifecycle.ViewModel
import mz.co.zonal.service.model.User
import mz.co.zonal.service.repository.UserRepository
import mz.co.zonal.utils.APIException
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.utils.NoInternetException
import mz.co.zonal.view.callback.AuthListener

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    var user = User()
    var authListener: AuthListener? = null
    var phoneNumber: String? = null

    fun onLoginButtonClick(){
        authListener?.onStarted()
        if(user.email.isNullOrEmpty() || user.password.isNullOrEmpty()){
            authListener?.onFailure("Credenciais incorectas")
            return
        }

        Coroutine.main {
            try {
                val  response = repository.login(user)
                response.let {
                    authListener?.onSuccess(response)
                    repository.saveUser(response)
                    return@main
                }

            }catch (e: APIException){
                authListener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                authListener?.onFailure(e.message!!)
            }
        }
    }

    fun userLogged() = repository.userInfo()

    fun onUpdateButtonClick(){


    }

    fun onSignUpButtonClicked(){

        authListener?.onStarted()

        Coroutine.main {
            try {
                user.phoneNumber = phoneNumber!!.toInt()
                val  response = repository.signUp(user)
                response.let {
                    authListener?.onSuccess(response)
                    repository.saveUser(response)
                    return@main
                }

            }catch (e: APIException){
                authListener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                authListener?.onFailure(e.message!!)
            }
        }

    }
}