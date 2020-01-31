package mz.co.zonal.view.callback

interface UpdateListener {
    fun onStart()
    fun onSuccess()
    fun onFailure(message: String)
}