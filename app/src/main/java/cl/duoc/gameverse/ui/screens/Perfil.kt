package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.duoc.gameverse.navigation.NavegacionBar
import androidx.compose.ui.res.painterResource
import cl.duoc.gameverse.R
import cl.duoc.gameverse.ui.viewmodel.UserViewModel

@Composable
fun PerfilScreen(navController: NavHostController, userViewModel: UserViewModel) {

    val usuario = userViewModel.usuarioActual.value

    Scaffold(
        bottomBar = { NavegacionBar(navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Avatar
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del usuario o Invitado
            Text(
                text = usuario?.nombre ?: "Invitado",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sobre mí
            Text(
                text = if (usuario != null)
                    "Correo: ${usuario.correo}"
                else
                    "Inicia sesión para ver tu información."
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar card SOLO si hay usuario
            if (usuario != null) {

                Text("Jugando ahora:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Image(
                            painter = painterResource(id = R.drawable.little_nightmares_3),
                            contentDescription = "Juego actual",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Little Nightmares 3")
                        Text("Progreso: 45%")

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(progress = 0.45f)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN CERRAR SESIÓN
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

