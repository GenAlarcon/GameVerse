package cl.duoc.gameverse.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.gameverse.domain.model.Usuario
import cl.duoc.gameverse.navigation.AppRoutes
import cl.duoc.gameverse.ui.viewmodel.UserViewModel
import cl.duoc.gameverse.ui.viewmodel.ValidacionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    navController: NavController,
    userViewModel: UserViewModel,
    validacionesViewModel: ValidacionesViewModel = viewModel()
) {
    val contexto = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }

    val loginError = userViewModel.loginError
    val usuarioLogueado = userViewModel.usuarioActual.value

    LaunchedEffect(usuarioLogueado) {
        if (usuarioLogueado != null) {
            Toast.makeText(contexto, "¡Bienvenido ${usuarioLogueado.nombre}! 🎮", Toast.LENGTH_LONG).show()

            // al registrarse, dirige al home
            navController.navigate(AppRoutes.FORUM_HOME) {
                popUpTo(AppRoutes.LOGIN) { inclusive = true }
            }
        }
    }

    LaunchedEffect(loginError) {
        if (loginError != null) {
            Toast.makeText(contexto, loginError, Toast.LENGTH_LONG).show()
            userViewModel.limpiarError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))


        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorNombre != null
        )
        if (errorNombre != null) Text(errorNombre!!, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(10.dp))

        // CORREO
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it.trim() },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = errorCorreo != null
        )
        if (errorCorreo != null) Text(errorCorreo!!, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(10.dp))

        // CONTRASEÑA
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it.trim() },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorContrasena != null
        )
        if (errorContrasena != null) Text(errorContrasena!!, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(10.dp))

        // CONFIRMAR
        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it.trim() },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorConfirmar != null
        )
        if (errorConfirmar != null) Text(errorConfirmar!!, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val nombreFinal = nombre.trim()
                val correoFinal = correo.trim()
                val passFinal = contrasena.trim()
                val confFinal = confirmar.trim()

                val resultado = validacionesViewModel.validarRegistro(
                    nombreFinal, correoFinal, passFinal, confFinal
                )

                errorNombre = resultado.errorNombre
                errorCorreo = resultado.errorCorreo
                errorContrasena = resultado.errorContrasena
                errorConfirmar = resultado.errorConfirmar

                if (resultado.ok) {
                    userViewModel.registrarUsuario(
                        Usuario(nombre = nombreFinal, correo = correoFinal, contrasena = passFinal)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate(AppRoutes.LOGIN) { popUpTo("registro") { inclusive = true } }
            }
        )
    }
}