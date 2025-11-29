package org.techcult.scaleos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.techcult.scaleos.core.presentation.ScaleOsApp


class MainActivity : ComponentActivity() {

    var isLoading: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val splashScreen= installSplashScreen()
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            isLoading
        }
        setContent {
            ScaleOsApp(onLoading = {
                isLoading = false
            })
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    ScaleOsApp()
}