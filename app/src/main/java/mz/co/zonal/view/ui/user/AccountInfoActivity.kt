package mz.co.zonal.view.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_account_info.*
import mz.co.zonal.R
import mz.co.zonal.databinding.ActivityAccountInfoBinding
import mz.co.zonal.service.model.User
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.viewmodel.UserViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AccountInfoActivity : AppCompatActivity(), AuthListener , KodeinAware {

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAccountInfoBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_account_info)

        val userViewModel = ViewModelProvider(this,
            factory
        ).get(UserViewModel::class.java)

        binding.user = userViewModel
        binding.lifecycleOwner = this
        userViewModel.authListener = this

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

    override fun onSuccess(user: Any) {
    }

    override fun onFailure(message: String) {
    }
}
