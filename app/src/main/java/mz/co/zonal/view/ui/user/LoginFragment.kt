package mz.co.zonal.view.ui.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import mz.co.zonal.R
import mz.co.zonal.databinding.FragmentLoginBinding
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.UserViewModelFactory
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.view.others.Dialog
import mz.co.zonal.view.others.Message
import mz.co.zonal.view.others.snackMessage
import mz.co.zonal.viewmodel.UserViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), AuthListener, KodeinAware {
    private var myView: View? = null
    private var dialog: Dialog? = null

    override val kodein by kodein()
    private val factory : UserViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentLoginBinding? = DataBindingUtil.inflate(inflater,
            R.layout.fragment_login, container, false)
        val userViewModel = ViewModelProviders.of(this,
            factory).get(UserViewModel::class.java)

        binding!!.userViewModel = userViewModel
        binding.lifecycleOwner = this
        userViewModel.authListener = this

        binding.tvToSignUp.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
            Toast.makeText(context!!, "hiii", Toast.LENGTH_SHORT).show()

        }
        binding.tvForgotPass.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        }
        myView = binding.root

        return myView
    }

    override fun onStarted() {
        root_login.snackMessage("Started")
//        dialog!!.show()
    }

    override fun onSuccess(user: User) {

//            val session = Session(context!!)
//            session.setLogIn(true, user.fullName, user.token
//            , user.email, user.phoneNumber, user.id, user.createAt.toString())


        root_login.snackMessage("${user.fullName}")


            context.let {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }

    }

    override fun onFailure(message: String){
//        dialog!!.dialog().hide()
        Message.messageError(context!!, message)
    }


}
