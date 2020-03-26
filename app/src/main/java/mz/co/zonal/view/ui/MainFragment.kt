package mz.co.zonal.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.view.*
import mz.co.zonal.R
import mz.co.zonal.utils.BottomNavigationViewBehavior
import mz.co.zonal.utils.Utils

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_main) as NavHostFragment

        view.bottomNavigationView.setupWithNavController(
            navController = navHostFragment.navController
        )

        Snackbar.make(view.root_main, "Active o GPS", Snackbar.LENGTH_SHORT).show()
        val layoutParams =
            view.bottomNavigationView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationViewBehavior()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Utils.closeApp(requireContext())
        }
        return view
    }
}
