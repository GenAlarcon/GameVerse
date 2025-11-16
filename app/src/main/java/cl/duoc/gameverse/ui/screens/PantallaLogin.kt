package cl.duoc.gameverse.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.gameverse.navigation.AppRoutes
import cl.duoc.gameverse.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(
    navController: NavController,
    userViewModel: UserViewModel
) {

    var identificador by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val contexto = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = identificador,
            onValueChange = {
                identificador = it.trim()
                error = null
            },
            label = { Text("Usuario o correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                error = null
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                // Usamos el ViewModel para validar el login
                val usuarioEncontrado = userViewModel.validarLogin(
                    identificador,
                    contrasena
                )

                if (usuarioEncontrado != null) {

                    Toast.makeText(
                        contexto,
                        "¡Bienvenido ${usuarioEncontrado.nombre}! 🎮",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navegar al Home
                    navController.navigate(AppRoutes.FORUM_HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }

                } else {
                    error = "Usuario o contraseña incorrectos"
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                Toast.makeText(
                    contexto,
                    "Google Sign-In aún no implementado",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Iniciar sesión con Google")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate("registro")
            }
        )
    }
}
