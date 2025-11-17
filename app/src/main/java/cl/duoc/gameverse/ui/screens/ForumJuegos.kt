package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.navigation.NavegacionBar
import cl.duoc.gameverse.ui.viewmodel.GameViewModel
import cl.duoc.gameverse.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumJuegosScreen(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    userViewModel: UserViewModel
) {

    val uiState by gameViewModel.uiState.collectAsState()
    var juegoSeleccionado by remember { mutableStateOf<Game?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Juegos") },
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                CatalogoJuegosGrid(
                    juegos = uiState.juegos,
                    onGameClick = { game ->
                        juegoSeleccionado = game
                    }
                )
            }
        }
    }

    if (juegoSeleccionado != null) {
        GameDetailDialog(
            game = juegoSeleccionado!!,
            onDismiss = {
                juegoSeleccionado = null
            },
            onAddAdventure = {
                userViewModel.agregarAventura(juegoSeleccionado!!)
                juegoSeleccionado = null
                scope.launch {
                    snackbarHostState.showSnackbar("¡Agregado a Próximas Aventuras!")
                }
            }
        )
    }
}

@Composable
fun CatalogoJuegosGrid(juegos: List<Game>, onGameClick: (Game) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(juegos) { juego ->
            GameCard(game = juego, onClick = { onGameClick(juego) })
        }
    }
}

@Composable
fun GameCard(game: Game, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB2E2C8))
    ) {
        Column {
            Image(
                painter = painterResource(id = game.imageResId),
                contentDescription = "Portada de ${game.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = game.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = game.genero,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun GameDetailDialog(
    game: Game,
    onDismiss: () -> Unit,
    onAddAdventure: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(game.nombre) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = game.imageResId),
                    contentDescription = game.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )
                Text("Género: ${game.genero}", style = MaterialTheme.typography.bodyLarge)

            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        confirmButton = {
            Button(onClick = onAddAdventure) {
                Text("Agregar Aventura")
            }
        }
    )
}