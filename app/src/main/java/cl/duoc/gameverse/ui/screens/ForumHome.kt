package cl.duoc.gameverse.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image // Icono
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.domain.model.Post
import cl.duoc.gameverse.navigation.AppRoutes
import cl.duoc.gameverse.navigation.NavegacionBar
import cl.duoc.gameverse.ui.viewmodel.ForumViewModel
import cl.duoc.gameverse.ui.viewmodel.UserViewModel
import coil.compose.AsyncImage // Necesario para ver la foto
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumHome(
    navController: NavHostController,
    forumViewModel: ForumViewModel,
    userViewModel: UserViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val authorName = userViewModel.usuarioActual.value?.nombre ?: "Invitado"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GameVerse") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, "Crear Post")
                    }
                }
            )
        },
        bottomBar = { NavegacionBar(navController) }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Categorías
            item {
                Text("Categorías", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp, bottom = 24.dp))
                val categorias = listOf(
                    "Novedades" to AppRoutes.FORUM_NOVEDADES,
                    "Juegos" to AppRoutes.FORUM_JUEGOS,
                    "Próximamente" to AppRoutes.FORUM_PROXIMAMENTE,
                    "Ofertas" to AppRoutes.FORUM_OFERTAS
                )
                Column(Modifier.fillMaxWidth()) {
                    categorias.chunked(2).forEach { fila ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            fila.forEach { (nombre, ruta) ->
                                Card(
                                    modifier = Modifier.weight(1f).height(130.dp).clickable { navController.navigate(ruta) },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB2E2C8)),
                                    elevation = CardDefaults.cardElevation(6.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(text = nombre, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // publicaciones
            item {
                Text("Mis publicaciones", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
            }

            val misPosts = forumViewModel.getPostsByAuthor(authorName)

            if (misPosts.isEmpty()) {
                item { Text("Aún no has creado ninguna publicación.", modifier = Modifier.padding(bottom = 16.dp)) }
            } else {
                items(items = misPosts, key = { it.id }) { post ->
                    PostItemCard(post = post, forumViewModel = forumViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showDialog) {
        PostCreationDialog(
            onDismiss = { showDialog = false },
            onPublish = { titulo, contenido, imageUri ->
                val newPost = Post(
                    id = UUID.randomUUID().toString(),
                    titulo = titulo,
                    contenido = contenido,
                    imageUri = imageUri,
                    categoria = null,
                    autor = authorName
                )
                forumViewModel.agregarPost(newPost)
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemCard(post: Post, forumViewModel: ForumViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Muestra la imagen si existe
            if (post.imageUri != null) {
                AsyncImage(
                    model = post.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            ListItem(
                overlineContent = { Text("Autor: ${post.autor}") },
                headlineContent = { Text(post.titulo, style = MaterialTheme.typography.titleMedium) },
                supportingContent = {
                    Text(post.contenido, maxLines = 3, overflow = TextOverflow.Ellipsis)
                },
                trailingContent = {
                    IconToggleButton(
                        checked = forumViewModel.favoritos.contains(post.id),
                        onCheckedChange = { forumViewModel.toggleFavorito(post.id) }
                    ) {
                        Icon(
                            if (forumViewModel.favoritos.contains(post.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito"
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PostCreationDialog(
    onDismiss: () -> Unit,
    onPublish: (titulo: String, contenido: String, imageUri: String?) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // abrir galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear publicación") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 500.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text("Contenido") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    maxLines = 5
                )

                // Botón para abrir Galería
                Button(
                    onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (selectedImageUri == null) "Agregar Foto" else "Cambiar Foto")
                }

                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Vista previa",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(top = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        confirmButton = {
            Button(
                onClick = { onPublish(titulo, contenido, selectedImageUri?.toString()) },
                enabled = titulo.isNotBlank() && contenido.isNotBlank()
            ) {
                Text("Publicar")
            }
        }
    )
}