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

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            val categorias = listOf(
                "Novedades" to AppRoutes.FORUM_NOVEDADES,
                "Juegos" to AppRoutes.FORUM_JUEGOS,
                "Próximamente" to AppRoutes.FORUM_PROXIMAMENTE,
                "Tendencia" to AppRoutes.FORUM_TENDENCIA
            )

            // GRID DE 2x2
            Column(Modifier.fillMaxWidth()) {

                categorias.chunked(2).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        fila.forEach { (nombre, ruta) ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(130.dp)
                                    .clickable { navController.navigate(ruta) },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFB2E2C8) // verde pastel
                                ),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
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

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
