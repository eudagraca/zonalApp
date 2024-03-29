package mz.co.zonal.view.ui.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_up.*
import mz.co.zonal.R
import mz.co.zonal.databinding.FragmentSignUpBinding
import mz.co.zonal.service.model.User
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.view.others.Message
import mz.co.zonal.view.others.snackMessage
import mz.co.zonal.view.ui.ContainerScreen
import mz.co.zonal.viewmodel.UserViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSignUpBinding? = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up, container, false
        )
        val userViewModel = ViewModelProvider(
            this, factory
        ).get(UserViewModel::class.java)

        //Lock drawer
        (activity as ContainerScreen?)?.lockDrawer()


        binding!!.user = userViewModel
        binding.lifecycleOwner = this
        userViewModel.authListener = this

        binding.tvToLogin.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }


        return binding.root
    }


    override fun onStarted() {
        root_sign_up.snackMessage("Started")
//        dialog!!.show()
    }

    override fun onSuccess(u: Any) {
        val user = u as User
//            val session = Session(context!!)
//            session.setLogIn(true, user.fullName, user.token
//            , user.email, user.phoneNumber, user.id, user.createAt.toString())


        root_sign_up.snackMessage("${user.fullName}")

        context.let {
            findNavController().navigate(R.id.action_signUpFragment_to_mainFragment)
        }

    }

    override fun onFailure(message: String) {
//        dialog!!.dialog().hide()
        Message.messageError(requireContext(), message)
    }


}
