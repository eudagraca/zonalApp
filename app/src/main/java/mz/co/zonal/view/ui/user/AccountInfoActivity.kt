package mz.co.zonal.view.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_account_info.*
import mz.co.zonal.R
import mz.co.zonal.databinding.ActivityAccountInfoBinding
import mz.co.zonal.service.db.ZonalDatabase
import mz.co.zonal.service.model.User
import mz.co.zonal.service.network.NetworkControl
import mz.co.zonal.service.repository.UserRepository
import mz.co.zonal.service.network.ZonalRestAPI
import mz.co.zonal.utils.Converters
import mz.co.zonal.utils.UserViewModelFactory
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.viewmodel.UserViewModel

class AccountInfoActivity : AppCompatActivity(), AuthListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        val binding: ActivityAccountInfoBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_account_info)
        binding.lifecycleOwner = this


        val database = ZonalDatabase(baseContext!!)
        val networkControl = NetworkControl(baseContext!!)
        val endPoint = ZonalRestAPI.invoke(networkControl)
        val repository = UserRepository(endPoint, database)
        val converters = Converters()

        val userViewModel = ViewModelProviders.of(this,
            UserViewModelFactory(repository)
        ).get(UserViewModel::class.java)
        userViewModel.authListener = this

        userViewModel.userLogged().observe(this, Observer {
            til_name_edit.editText!!.setText(it.fullName)
            til_email_edit.editText!!.setText(it.email)
            til_phone_edit.editText!!.setText(it!!.phoneNumber.toString())
            tv_create_at.text = converters.toSimpleString(it.createAt!!)
        })

        toolbar_edit.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setEnableForm(view: View){
        tie_name.isEnabled = true
        tie_phone.isEnabled = true
        signIn.visibility = View.VISIBLE
    }

    override fun onStarted() {

    }

    override fun onSuccess(user: User) {
    }

    override fun onFailure(message: String) {
    }
}
