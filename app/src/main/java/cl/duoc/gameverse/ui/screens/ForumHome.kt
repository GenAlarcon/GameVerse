package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.navigation.AppRoutes
import cl.duoc.gameverse.navigation.NavegacionBar
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumHome(
    navController: NavHostController,
    forumViewModel: ForumViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GameVerse") },
                actions = {
                    IconButton(onClick = { /* Ir a mensajes */ }) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = "Mensajes"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavegacionBar(navController)
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título de la sección
            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Lista de categorías y sus rutas fijas
            val categorias = listOf(
                "Novedades" to AppRoutes.FORUM_NOVEDADES,
                "Juegos" to AppRoutes.FORUM_JUEGOS,
                "Proximamente" to AppRoutes.FORUM_PROXIMAMENTE,
                "Tendencia" to AppRoutes.FORUM_TENDENCIA
            )

            categorias.forEach { (nombre, ruta) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate(ruta) }, // Navegación fija
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB2E2C8)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
