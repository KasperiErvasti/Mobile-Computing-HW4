package com.example.mobilecomputinghw4

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecomputinghw4.sensor.NotificationHelper
import com.example.mobilecomputinghw4.sensor.SensorActivity
import kotlinx.serialization.Serializable
import java.io.File


class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var mLight: Sensor? = null
    private val notificationHelper = NotificationHelper(this)
    private var denyLuxNotification = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        notificationHelper.createNotificationChannel()

        setContent {
            val context = LocalContext.current
            val usernameFile = File(context.filesDir, "username")

            if (!usernameFile.exists()) {
                usernameFile.writeBytes("Hullu".toByteArray())
            }

            AppNavigation(modifier = Modifier.systemBarsPadding(), notificationHelper)

        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.d("MY_TAG", "accuracy")
//        return
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]

            if (lux > 10000) {
                if (!denyLuxNotification) {
                    notificationHelper.createNotification("OMG BIG LUX", "LUX IS OVER 10000. WOW.")
                }
                denyLuxNotification = true
            } else {
                denyLuxNotification = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mLight?.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}

@Serializable
object Chat

@Serializable
object Profile


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    notificationHelper: NotificationHelper,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Chat
    ) {
        composable<Chat> {
            ChatScreen(
                onNavigateToProfile = {
                    navController.navigate(route = Profile)
                }
            )
        }
        composable<Profile> {
            ProfileScreen(
                onNavigateToChat = {
                    navController.navigate(route = Chat) {
                        popUpTo(Chat) {
                            inclusive = true
                        }
                    }
                },
                notificationHelper
            )

        }
    }
}
