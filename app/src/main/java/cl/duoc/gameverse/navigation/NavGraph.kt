package cl.duoc.gameverse.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.duoc.gameverse.ui.screens.HomeScreen
import cl.duoc.gameverse.ui.screens.PantallaLogin
import cl.duoc.gameverse.ui.screens.PantallaRegistro
import cl.duoc.gameverse.ui.screens.ForumHome
import cl.duoc.gameverse.ui.viewmodel.UserViewModel
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel
import cl.duoc.gameverse.domain.model.Usuario

@Composable
fun AppNavHost(navController: NavHostController) {

    // ⭐ ViewModel global de usuarios: persiste entre pantallas
    val userViewModel: UserViewModel = viewModel()

    // ⭐ Cargar usuarios de prueba sólo una vez
    if (userViewModel.usuarios.isEmpty()) {
        userViewModel.registrarUsuario(Usuario("Gamer1", "gamer1@gmail.com", "1234"))
        userViewModel.registrarUsuario(Usuario("Juan", "juan@gmail.com", "abcd"))
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable(AppRoutes.HOME) {
            HomeScreen(
                onPrimaryAction = { navController.navigate(AppRoutes.LOGIN) },
                onSecondaryAction = { navController.navigate(AppRoutes.REGISTRO) },
                onForumAction = { navController.navigate(AppRoutes.FORUM_HOME) }
            )
        }

        composable(AppRoutes.LOGIN) {
            PantallaLogin(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable(AppRoutes.REGISTRO) {
            PantallaRegistro(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable(AppRoutes.FORUM_HOME) {
            val forumViewModel: ForumViewModel = viewModel()
            ForumHome(
                navController = navController,
                forumViewModel = forumViewModel
            )
        }
    }
}
