package cl.duoc.gameverse.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.duoc.gameverse.ui.screens.*
import cl.duoc.gameverse.ui.viewmodel.UserViewModel
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel
import cl.duoc.gameverse.domain.model.Usuario
import cl.duoc.gameverse.ui.viewmodel.GameViewModel
import cl.duoc.gameverse.ui.screens.ForumJuegosScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
    val forumViewModel: ForumViewModel = viewModel()
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModel.Factory)

    val usuarioActual = userViewModel.usuarioActual.value
    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) {
            val currentRoute = navController.currentDestination?.route
            if (currentRoute == AppRoutes.LOGIN || currentRoute == AppRoutes.REGISTRO) {
                navController.navigate(AppRoutes.HOME) {
                    popUpTo(AppRoutes.HOME) { inclusive = true }
                }
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {

        composable(AppRoutes.HOME) {
            HomeScreen(
                userViewModel = userViewModel,
                onPrimaryAction = { navController.navigate(AppRoutes.LOGIN) },
                onSecondaryAction = { navController.navigate(AppRoutes.REGISTRO) },
                onForumAction = { navController.navigate(AppRoutes.FORUM_HOME) }
            )
        }

        composable(AppRoutes.LOGIN) {
            PantallaLogin(navController, userViewModel)
        }

        composable(AppRoutes.REGISTRO) {
            PantallaRegistro(navController, userViewModel)
        }

        composable(AppRoutes.FORUM_HOME) {
            ForumHome(forumViewModel = forumViewModel, navController = navController, userViewModel = userViewModel)
        }

        composable(AppRoutes.PERFIL) {
            PerfilScreen(navController, userViewModel)
        }

        composable(AppRoutes.FORUM_NOVEDADES) {
            ForumNovedad(navController = navController, forumViewModel = forumViewModel)
        }
        composable(AppRoutes.FORUM_JUEGOS) {
            ForumJuegosScreen(navController = navController, gameViewModel = gameViewModel, userViewModel = userViewModel)
        }

        composable(AppRoutes.FORUM_PROXIMAMENTE) {
            ForumProximo(navController = navController)
        }

        composable(AppRoutes.FORUM_TENDENCIA) {
            ForumTendencia(navController = navController)
        }

    }
}