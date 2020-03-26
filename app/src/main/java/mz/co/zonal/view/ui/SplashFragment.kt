package mz.co.zonal.view.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import mz.co.zonal.R
import mz.co.zonal.service.model.User
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.viewmodel.UserViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SplashFragment : Fragment(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory : UserViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userViewModel = ViewModelProvider(
            this, factory
        ).get(UserViewModel::class.java)
        userViewModel.authListener = this


        Handler().postDelayed({
            context?.let {
                userViewModel.userLogged.observe(viewLifecycleOwner, Observer {

                    if (it != null) {
                        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                })
            }
        }, 2500)
    }

    override fun onStarted() {

    }

    override fun onSuccess(user: Any) {
    }

    override fun onFailure(message: String) {
    }
}
