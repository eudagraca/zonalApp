package mz.co.zonal.view.callback

import mz.co.zonal.service.model.User


interface AuthListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message: String)
}