package mz.co.zonal.view.ui.user


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user.view.*
import mz.co.zonal.R
import mz.co.zonal.databinding.FragmentUserBinding
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.Utils
import mz.co.zonal.utils.converters.FilePart
import mz.co.zonal.utils.loadImage
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.view.others.Dialog
import mz.co.zonal.view.ui.ContainerScreen
import mz.co.zonal.viewmodel.UserViewModel
import org.jetbrains.anko.support.v4.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class UserFragment : Fragment(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()
    private lateinit var userViewModel: UserViewModel
    private val session: Session by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user, container, false
        )

        userViewModel = ViewModelProvider(this, factory)
            .get(UserViewModel::class.java)

        //Lock drawer
        (activity as ContainerScreen?)?.lockDrawer()

        binding.user = userViewModel
        binding.lifecycleOwner = this


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

        binding.root.mcv_selled_product.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_userFragment_to_soldProductsActivity)
            }
        }

        binding.root.mcv_conversations.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_userFragment_to_chatListActivity)
            }
        }

        binding.root.iv_profileImage.setOnClickListener {
            openGallery()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Utils.closeApp(requireContext())
        }

        return binding.root
    }

    override fun onStarted() {
    }

    override fun onSuccess(u: Any) {
        if (u != null) {
            toast("Foto de perfil actualizada")
        }
    }

    override fun onFailure(message: String) {
    }

    private fun openGallery() {
        Dialog.alertDialog(
            {
                if (it) {
                    selectImage()
                }
            },
            requireContext(),
            "Abrir a galeria para seleccionar uma imagem",
            "Informação",
            "abrir"
        ).show()
    }

    private fun selectImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
            return
        }

        val picker = KwikPicker.Builder(
                requireContext(),
                imageProvider = { imageView, uri ->
                    Glide.with(requireActivity())
                        .load(uri)
                        .into(imageView)
                },

                onImageSelectedListener = { imageUri ->

                    if (imageUri.toString().isNotEmpty()) {
                        val image = FilePart.prepareFilePart(
                            "picPath", imageUri
                        )!!

                        userViewModel.userLiveData.observe(viewLifecycleOwner, Observer {
                            it as User
                            if (it.picPath != null) {
                                loadImage(
                                    requireView().rootView.iv_profileImage,
                                    userViewModel.imageProfile(session.id!!)
                                )

                            }
                        })

                        val userID = FilePart.createPartFromString(
                            session.id.toString()
                        )!!
                        userViewModel.setUserImage(userID, image)
                    }

                },
                peekHeight = Utils.getScreenHeight(),
                showTitle = false,
                selectMaxCount = 1,
                showCamera = true,
                showGallery = true,
                title = "Imagens",
                completeButtonText = "Certo",
                emptySelectionText = "Nenhuma imagem seleccionada"
            )
            .create(requireContext())
        picker.show(childFragmentManager)
    }
}
