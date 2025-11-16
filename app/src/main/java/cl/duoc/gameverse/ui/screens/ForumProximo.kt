package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel
import cl.duoc.gameverse.navigation.NavegacionBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumProximo(
    navController: NavHostController,
    forumViewModel: ForumViewModel
) {
    // Filtramos solo los posts de la categoría "Proximamente"
    val posts = forumViewModel.getPostsByCategory("Proximamente")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Próximamente") },

                // FLECHA DE RETROCESO
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavegacionBar(navController)
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB2E2C8)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            text = post.titulo,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Autor: ${post.autor}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (forumViewModel.favoritos.contains(post.id)) "★" else "☆",
                                color = Color.Yellow,
                                modifier = Modifier
                                    .clickable { forumViewModel.toggleFavorito(post.id) }
                                    .padding(end = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
