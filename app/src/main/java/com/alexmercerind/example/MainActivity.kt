package com.alexmercerind.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexmercerind.movingletters.FadeAnimatedText
import com.alexmercerind.movingletters.JumpAnimatedText
import com.alexmercerind.movingletters.ScaleInAnimatedText
import com.alexmercerind.movingletters.ScaleOutAnimatedText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(resources.getColor(R.color.transparent)))
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Destinations.Home.value
                ) {
                    composable(Destinations.Home.value) {
                        HomeScreen(
                            navController = navController,
                            effects = mapOf(
                                Destinations.Effect1 to (Color(0xFFFFFFFF) to Color(0xFF9CA4B5)),
                                Destinations.Effect2 to (Color(0xFFFFFFFF) to Color(0xFFE7C3B9)),
                                Destinations.Effect3 to (Color(0xFFFFFFFF) to Color(0xFF234A54)),
                                Destinations.Effect4 to (Color(0xFFFFFFFF) to Color(0xFFC1605D))
                            )
                        )
                    }
                    composable(Destinations.Effect1.value) {
                        AnimatedTextScreen(
                            name = "ScaleInAnimatedText",
                            content = { state, text ->
                                ScaleInAnimatedText(
                                    state = state,
                                    text = text,
                                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                                    animateOnMount = false
                                )
                            },
                            contentColor = Color(0xFFFFFFFF),
                            containerColor = Color(0xFF9CA4B5)
                        )
                    }
                    composable(Destinations.Effect2.value) {
                        AnimatedTextScreen(
                            name = "ScaleOutAnimatedText",
                            content = { state, text ->
                                ScaleOutAnimatedText(
                                    state = state,
                                    text = text,
                                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                                    animateOnMount = false
                                )
                            },
                            contentColor = Color(0xFFFFFFFF),
                            containerColor = Color(0xFFE7C3B9)
                        )
                    }
                    composable(Destinations.Effect3.value) {
                        AnimatedTextScreen(
                            name = "FadeAnimatedText",
                            content = { state, text ->
                                FadeAnimatedText(
                                    state = state,
                                    text = text,
                                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                                    animateOnMount = false
                                )
                            },
                            contentColor = Color(0xFFFFFFFF),
                            containerColor = Color(0xFF234A54)
                        )
                    }
                    composable(Destinations.Effect4.value) {
                        AnimatedTextScreen(
                            name = "JumpAnimatedText",
                            content = { state, text ->
                                 JumpAnimatedText(
                                    state = state,
                                    text = text,
                                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                                    animateOnMount = false
                                )
                            },
                            contentColor = Color(0xFFFFFFFF),
                            containerColor = Color(0xFFC1605D)
                        )
                    }
                }
            }
        }
    }
}
