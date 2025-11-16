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
import cl.duoc.gameverse.navigation.NavegacionBar
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumHome(
    navController: NavHostController,
    forumViewModel: ForumViewModel
) {
    val categorias = listOf("Juegos", "Novedades", "Proximamente", "Tendencia")

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

            // Rectángulos verticales
            categorias.forEach { categoria ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Navegar a la pantalla de foros de la categoría
                            //navController.navigate("foros/$categoria")
                        },
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
                            text = categoria,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
        }
    }
}