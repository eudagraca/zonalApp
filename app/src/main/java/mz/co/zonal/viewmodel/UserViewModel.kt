package mz.co.zonal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.model.User
import mz.co.zonal.service.repository.UserRepository
import mz.co.zonal.utils.APIException
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.utils.NoInternetException
import mz.co.zonal.view.callback.AuthListener
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(private val repository: UserRepository, private val session: Session) : ViewModel() {

    var user = User()
    var authListener: AuthListener? = null
    var phoneNumber: String? = null
    private var _userLiveData = MutableLiveData<Any>()
    val userLiveData: LiveData<Any>
        get() = _userLiveData

    private lateinit var job: Job


    fun onLoginButtonClick() {
        authListener?.onStarted()
        if (user.email.isNullOrEmpty() || user.password.isNullOrEmpty()) {
            authListener?.onFailure("Credenciais incorectas")
            return
        }

        Coroutine.main {
            try {
                val response = repository.login(user)
                response.let {
                    authListener?.onSuccess(response)
                    session.setLogIn(true, response.id)
                    repository.saveUser(response)
                    return@main
                }
            } catch (e: APIException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    }

    val userLogged = repository.userInfo()

    fun onUpdateButtonClick() {
    }

    fun setCoordinators(user: User){
        authListener?.onStarted()
        Coroutine.main {
            try {
                val response = repository.setCoordinators(user)
                response.let {
                    authListener?.onSuccess(response)
                    repository.saveUser(response)
                    return@main
                }
            }catch (e: APIException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    }
    fun imageProfile(userId: Long) = repository.image(userId)

    fun onSignUpButtonClicked() {
        authListener?.onStarted()
        Coroutine.main {
            try {
                user.phoneNumber = phoneNumber!!.toInt()
                val response = repository.signUp(user)
                response.let {

                    session.setLogIn(true, response.id)
                    authListener?.onSuccess(response)
                    repository.saveUser(response)
                    return@main
                }
            } catch (e: APIException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    fun setUserImage(userID: RequestBody, image: MultipartBody.Part) {
        job = Coroutine.ioThreadMain(
            { repository.setUserImage(userID, image) },
            { _userLiveData.value = it as User }
        )
    }

    fun setToken(token: String?) {
        job = Coroutine.ioThreadMain(
            { repository.setToken(token) },
            { _userLiveData.value = it as User }
        )
    }
}