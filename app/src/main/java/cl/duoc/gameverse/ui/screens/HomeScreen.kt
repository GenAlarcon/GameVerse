package cl.duoc.gameverse.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.duoc.gameverse.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import cl.duoc.gameverse.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    onForumAction: () -> Unit
) {

    val usuario = userViewModel.usuarioActual.value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.icono2),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = if (usuario != null)
                    "Bienvenido ${usuario.nombre}"
                else
                    "Bienvenido a GameVerse",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Si NO está logueado mostrar botones de login
            if (usuario == null) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onPrimaryAction,
                        modifier = Modifier.weight(1f)
                    ) { Text("Iniciar Sesión") }

                    OutlinedButton(
                        onClick = onSecondaryAction,
                        modifier = Modifier.weight(1f)
                    ) { Text("Registrarse") }
                }
            } else {
                // Si está logueado mostrar botón Cerrar Sesión
                Button(
                    onClick = { userViewModel.cerrarSesion() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Cerrar sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón ir a inicio siempre visible
            Button(
                onClick = onForumAction,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir a inicio")
            }
        }
    }
}
