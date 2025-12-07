package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.R
import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.domain.model.JuegoUsuario
import cl.duoc.gameverse.navigation.NavegacionBar
import cl.duoc.gameverse.ui.viewmodel.UserViewModel
import coil.compose.AsyncImage

private val Game.imageResId: Int
    get() {
        TODO()
    }

@Composable
fun PerfilScreen(navController: NavHostController, userViewModel: UserViewModel) {

    val usuario = userViewModel.usuarioActual.value

    Scaffold(
        bottomBar = { NavegacionBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            item {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = usuario?.nombre ?: "Invitado",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (usuario != null)
                        "Correo: ${usuario.correo}"
                    else
                        "Inicia sesión para ver tu información."
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (usuario != null) {

                item {
                    Text(
                        "Jugando ahora:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth() // Alinea a la izquierda
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                val juegoEnCurso = userViewModel.juegoEnCurso

                item {
                    if (juegoEnCurso != null) {
                        JugandoAhoraCard(
                            juegoUsuario = juegoEnCurso,
                            onProgresoChange = { nuevoProgreso ->
                                userViewModel.actualizarProgresoEnCurso(nuevoProgreso)
                            }
                        )
                    } else {
                        Text(
                            "No tienes ningún juego marcado como 'Jugando ahora'.",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        "Mis Próximas Aventuras",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth() // Alinea a la izquierda
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                val aventuras = userViewModel.proximasAventuras

                if (aventuras.isEmpty()) {
                    item {
                        Text(
                            "Tu lista de próximas aventuras está vacía.",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    items(aventuras, key = { juegoUsuario -> juegoUsuario.game.id } ) { juegoUsuario ->
                        AventuraItem(
                            juegoUsuario = juegoUsuario,
                            onMoverJuego = {
                                userViewModel.moverJuegoA_EnCurso(juegoUsuario)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre juegos
                    }
                }
                // --- BOTÓN CERRAR SESIÓN ---
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            userViewModel.cerrarSesion()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}

@Composable
fun JugandoAhoraCard(
    juegoUsuario: JuegoUsuario,
    onProgresoChange: (Float) -> Unit
) {
    val game = juegoUsuario.game
    val progreso = juegoUsuario.progreso

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB2E2C8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = game.imageResId),
                contentDescription = game.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(game.nombre, style = MaterialTheme.typography.titleLarge)
            Text(game.genero, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Progreso: ${(progreso * 100).toInt()}%")
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth()
            )
            Slider(
                value = progreso,
                onValueChange = onProgresoChange, // Llama a la función del ViewModel
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AventuraItem(
    juegoUsuario: JuegoUsuario,
    onMoverJuego: () -> Unit // Función para moverlo a "Jugando ahora"
) {
    val game = juegoUsuario.game

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB2E2C8)) // Color verde
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = game.imagen, // Usamos la URL o URI
                contentDescription = game.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(3f / 4f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Columna para el texto y el botón
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(game.nombre, style = MaterialTheme.typography.titleLarge)
                Text(game.genero, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para mover el juego
                Button(onClick = onMoverJuego) {
                    Text("Jugar ahora")
                }
            }
        }
    }
}
