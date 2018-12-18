package com.skyver.trybase.presentation

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.skyver.trybase.R
import com.skyver.trybase.presentation.platform.BaseFragment
import timber.log.Timber.d
import timber.log.Timber.e


/**
 * Fragment used to show how to navigate to another destination
 */
class HomeFragment : BaseFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startAuthenticationIfNeeded()

        //authenticator.logOutUser()// todo delete stay for testing
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set NavOptions
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        view.findViewById<Button>(R.id.navigate_destination_button)?.setOnClickListener {


            //showProgress()
            //notify(R.string.my)
            //notifyWithAction(R.string.my, R.string.my){}

//            e("CRASH!!!")
//            Crashlytics.log(Log.DEBUG, "tag", "message 3 THIS!!")
//            Crashlytics.logException(Exception("my Exception 3 THIS!!"))

            //Crashlytics.log("message")
            //Crashlytics.getInstance().crash() // Force a crash

            findNavController().navigate(R.id.flow_step_one_dest, null, options)
        }

        // Update the OnClickListener to navigate using an action
        view.findViewById<Button>(R.id.navigate_action_button)?.setOnClickListener {

            //(
            //Navigation.createNavigateOnClickListener(R.id.next_action, null)
            //)

            val action = HomeFragmentDirections.nextAction()
            action.setFlowStepNumber(1)
            findNavController().navigate(action)
        }

    }

    private fun startAuthenticationIfNeeded() {
        //check if user logged in if not go for authentication
        if (!authenticator.isLogedIn()) {
            findNavController().navigate(
                R.id.authFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.home_dest, true).build() //prevents return here on back button press
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
