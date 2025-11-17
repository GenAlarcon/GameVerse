package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.gameverse.navigation.NavegacionBar
import cl.duoc.gameverse.ui.viewmodel.ForumProximosViewModel
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumProximo(
    navController: NavHostController,
    viewModel: ForumProximosViewModel = viewModel()
) {

    // Obtener lista de eventos próximos
    val posts by viewModel.proximosForos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Próximamente") },
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
                .padding(12.dp)
        ) {
            items(posts) { post ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE6F4EA)
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        // Imagen banner grande
                        Image(
                            painter = painterResource(id = post.imagen),
                            contentDescription = "Banner evento",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Título
                        Text(
                            text = post.titulo,
                            style = MaterialTheme.typography.titleLarge
                        )

                        // Descripción / contenido
                        Text(
                            text = post.contenido,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        // Fecha estimada
                        post.fechaEstimado?.let {
                            Text(
                                text = "Fecha estimada: $it",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        // Estado
                        post.estado?.let {
                            Text(
                                text = "Estado: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF1A5D1A),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
