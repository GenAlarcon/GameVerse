package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.domain.model.Post
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel
import cl.duoc.gameverse.navigation.NavegacionBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumNovedad(
    navController: NavHostController,
    forumViewModel: ForumViewModel
) {
    // Filtrar solo posts de la categoría "Novedades"
    val posts: List<Post> = forumViewModel.getPostsByCategory("Novedades")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novedades") },
                // Botón de volver al stack anterior
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavegacionBar(navController) // Mantener la barra inferior
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
                        .padding(vertical = 10.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCCF3E1)),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // TÍTULO
                        Text(
                            text = post.titulo,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // DESCRIPCIÓN (contenido)
                        Text(
                            text = post.contenido,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // FILA CATEGORÍA - AUTOR - ESTRELLA
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎮 ${post.categoria ?: "Sin categoría"}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "👤 ${post.autor}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            // ESTRELLA FAVORITO
                            Text(
                                text = if (forumViewModel.favoritos.contains(post.id)) "★" else "☆",
                                color = Color.Yellow,
                                fontSize = 26.sp,
                                modifier = Modifier
                                    .clickable { forumViewModel.toggleFavorito(post.id) }
                            )
                        }
                    }
                }
            }

            // Si no hay posts, puedes mostrar un ítem con mensaje
            if (posts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay posts en Novedades aún 😢")
                    }
                }
            }
        }
    }
}
