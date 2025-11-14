package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.domain.model.Post
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumHome(
    navController: NavHostController,
    forumViewModel: ForumViewModel
) {
    val posts = forumViewModel.posts
    val favoritos = forumViewModel.favoritos

    var tituloNuevoPost by remember { mutableStateOf("") }
    var categoriaNueva by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Foro GameVerse") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (tituloNuevoPost.isNotBlank() && categoriaNueva.isNotBlank()) {
                        forumViewModel.agregarPost(
                            Post(
                                id = UUID.randomUUID().toString(),
                                titulo = tituloNuevoPost.trim(),
                                categoria = categoriaNueva.trim(),
                                autor = "UsuarioActual"
                            )
                        )
                        tituloNuevoPost = ""
                        categoriaNueva = ""
                    }
                }
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            OutlinedTextField(
                value = tituloNuevoPost,
                onValueChange = { tituloNuevoPost = it },
                label = { Text("Título") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = categoriaNueva,
                onValueChange = { categoriaNueva = it },
                label = { Text("Categoría") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(posts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(modifier = Modifier.weight(1f)) {
                                Text(post.titulo, style = MaterialTheme.typography.titleMedium)
                                Text("Categoría: ${post.categoria}", style = MaterialTheme.typography.bodySmall)
                                Text("Autor: ${post.autor}", style = MaterialTheme.typography.bodySmall)
                            }

                            Row {

                                Text(
                                    text = if (favoritos.contains(post.id)) "★" else "☆",
                                    color = Color.Yellow,
                                    modifier = Modifier
                                        .clickable { forumViewModel.toggleFavorito(post.id) }
                                        .padding(horizontal = 8.dp)
                                )

                                Text(
                                    text = "🗑",
                                    modifier = Modifier
                                        .clickable { forumViewModel.eliminarPost(post.id) }
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
