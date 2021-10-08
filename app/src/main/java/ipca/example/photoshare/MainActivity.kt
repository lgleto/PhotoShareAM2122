package ipca.example.photoshare

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ipca.example.photoshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var notificationReceiver : NotificationReceiver? = null

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            intent?.extras?.getString(MyFirebaseMessagingService.NOTIFICATION_MESSAGE)?.let {
                alertNotificatio(this@MainActivity, it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notificationReceiver = NotificationReceiver()
        this.registerReceiver(notificationReceiver, IntentFilter(MyFirebaseMessagingService.BROADCAST_NEW_NOTIFICATION))
    }

    override fun onPause() {
        super.onPause()
        notificationReceiver?.let {
            this.unregisterReceiver(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_photo
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}