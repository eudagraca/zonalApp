package mz.co.zonal.view.ui.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_user.view.*
import mz.co.zonal.R
import mz.co.zonal.databinding.FragmentUserBinding
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.UserViewModelFactory
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.viewmodel.UserViewModel
import org.kodein.di.android.x.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class UserFragment : Fragment(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory : UserViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FragmentUserBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user, container, false)

        val userViewModel = ViewModelProviders.of(
            this, factory)
            .get(UserViewModel::class.java)

        binding.user = userViewModel
        binding.lifecycleOwner = this

        userViewModel.userLogged().observe(this, Observer {
            binding.root.tv_userName.text = it.fullName
        })

        binding.root.mcv_selling_product.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_userFragment_to_ownProductsActivity2)
            }
        }

        binding.root.mcv_account_info.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_userFragment_to_accountInfoActivity2)
            }
        }
        return binding.root
    }

    override fun onStarted() {
    }

    override fun onSuccess(user: User) {
    }

    override fun onFailure(message: String) {
    }
}
