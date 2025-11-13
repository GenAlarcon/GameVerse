package cl.duoc.gameverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.gameverse.ui.theme.GameVerseTheme
import cl.duoc.gameverse.ui.screens.HomeScreen
import cl.duoc.gameverse.ui.screens.PantallaLogin
import cl.duoc.gameverse.ui.screens.PantallaRegistro
import cl.duoc.gameverse.domain.model.Usuario

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Lista de usuarios simulada en memoria
        val usuarios = mutableStateListOf<Usuario>()

        setContent {
            GameVerseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                onPrimaryAction = { navController.navigate("login") },
                                onSecondaryAction = { navController.navigate("registro") }
                            )
                        }
                        composable("login") {
                            PantallaLogin(navController, usuarios)
                        }
                        composable("registro") {
                            PantallaRegistro(navController, usuarios)
                        }
                    }
                }
            }
        }
    }
}
