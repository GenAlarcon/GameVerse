package cl.duoc.gameverse.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Message


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
            NavigationBar(containerColor = Color(0xFF00A86B)) { // verde
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    selected = false,
                    onClick = { /* Navegar Home */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    selected = false,
                    onClick = { /* Navegar búsqueda */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Agregar") },
                    selected = false,
                    onClick = { /* Agregar post */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Progreso") },
                    selected = false,
                    onClick = { /* Ir a progreso */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    selected = false,
                    onClick = { /* Ir a perfil */ }
                )
            }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // Inputs para nuevo post
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

            // Lista de posts estilo Reddit
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(posts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFB2E2C8) // Verde pastel clarito
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(post.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("Categoría: ${post.categoria}", style = MaterialTheme.typography.bodySmall)
                            Text("Autor: ${post.autor}", style = MaterialTheme.typography.bodySmall)
                            Row {
                                Text(
                                    text = if (favoritos.contains(post.id)) "★" else "☆",
                                    color = Color.Yellow,
                                    modifier = Modifier
                                        .clickable { forumViewModel.toggleFavorito(post.id) }
                                        .padding(end = 8.dp)
                                )
                                Text(
                                    text = "🗑",
                                    modifier = Modifier
                                        .clickable { forumViewModel.eliminarPost(post.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}