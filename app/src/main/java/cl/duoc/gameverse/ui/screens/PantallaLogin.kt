package cl.duoc.gameverse.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cl.duoc.gameverse.R
import cl.duoc.gameverse.navigation.AppRoutes
import cl.duoc.gameverse.ui.viewmodel.UserViewModel
import cl.duoc.gameverse.ui.viewmodel.ValidacionesViewModel

@Composable
fun PantallaLogin(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val contexto = LocalContext.current
    val validacionesViewModel: ValidacionesViewModel = viewModel()
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val loginError = userViewModel.loginError
    val usuarioLogueado = userViewModel.usuarioActual.value

    LaunchedEffect(usuarioLogueado) {
        if (usuarioLogueado != null) {
            Toast.makeText(
                contexto,
                "¡Bienvenido ${usuarioLogueado.nombre}! 🎮",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(loginError) {
        if (loginError != null) {
            Toast.makeText(contexto, loginError, Toast.LENGTH_SHORT).show()
            userViewModel.limpiarError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icono2),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo o Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val validacion = validacionesViewModel.validarLoginCampos(correo, contrasena)

                if (validacion.ok) {
                    userViewModel.validarLogin(correo, contrasena)
                } else {
                    Toast.makeText(contexto, validacion.error, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(AppRoutes.REGISTRO) }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}