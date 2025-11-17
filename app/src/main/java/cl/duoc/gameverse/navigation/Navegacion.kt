package cl.duoc.gameverse.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Forum

@Composable
fun NavegacionBar(navController: NavController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        // HOME
        NavigationBarItem(
            selected = currentRoute == AppRoutes.HOME,
            onClick = {
                navController.navigate(AppRoutes.HOME) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.ArrowCircleLeft, contentDescription = "Home") }
        )

        // FORO
        NavigationBarItem(
            selected = currentRoute == AppRoutes.FORUM_HOME,
            onClick = {
                navController.navigate(AppRoutes.FORUM_HOME) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Foro") }
        )

        // PERFIL
        NavigationBarItem(
            selected = currentRoute == AppRoutes.PERFIL,
            onClick = {
                navController.navigate(AppRoutes.PERFIL) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
        )
    }
}
