package com.skyver.trybase.presentation


import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.skyver.trybase.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasAndroidInjector {
    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up Action Bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //check if Google Play Services available
        if (checkPlayServices()) {
            // Start application
            setComponents()
        } else {
            e("MainActivity GoogleApiAvailability = NOT AVAILABLE")
        }
    }

    private fun setComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        setupActionBar(navController)

        setupNavigationMenu(navController)

        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }

            d("Navigated to $dest")
        }

        listenForDeepLink()
    }

    private fun listenForDeepLink() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link

                    e("listenForDeepLink() addOnSuccessListener!! deepLink: $deepLink")
                    val param = deepLink?.getQueryParameter("ff")
                    e("listenForDeepLink() addOnSuccessListener!! GET PARAMETER param: $param")
                    // For example, open the linked
                    // activity_main_content, or apply promotional credit to the user's
                    // account.
                }

                e("listenForDeepLink() addOnSuccessListener!!!!!!!!! listenForDeepLink: $deepLink")

            }
            .addOnFailureListener(this) { e -> e("getDynamicLink:onFailure $e") }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        // Use NavigationUI to set up Bottom Nav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    private fun setupNavigationMenu(navController: NavController) {
        //Use NavigationUI to set up a Navigation View
        // In split screen mode, you can drag this presentation out from the left
        // This does NOT modify the actionbar
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

    }

    private fun setupActionBar(navController: NavController) {
        // Have NavigationUI handle what your ActionBar displays
        // This allows NavigationUI to decide what label to show in the action bar
        // And, since we are passing in drawerLayout, it will also determine whether to
        // show the up arrow or drawer menu icon
        drawerLayout = findViewById(R.id.drawer_layout)
        setupActionBarWithNavController(navController, drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)

        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)

    }

    // Have NavigationUI handle up behavior in the ActionBar
    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        //return drawerLayout.navigateUp(findNavController(R.id.my_nav_host_fragment))

        return findNavController(R.id.my_nav_host_fragment).navigateUp(drawerLayout)
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                val dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                dialog.setOnDismissListener { closeApp() }
                dialog.show()
            } else {
                Timber.i("This device is not supported.")
                closeApp()
            }
            return false
        }
        return true
    }

    private fun closeApp() = finish()

    companion object {
        const val PLAY_SERVICES_RESOLUTION_REQUEST = 4794
    }

}
