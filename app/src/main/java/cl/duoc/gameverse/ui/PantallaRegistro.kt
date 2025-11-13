package cl.duoc.gameverse.ui

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
import cl.duoc.gameverse.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(navController: NavController, usuarios: MutableList<Usuario>) {

    // Campos del formulario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    // Variables de error
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }

    val contexto = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text("Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        // Campo: Nombre de usuario
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it.trim(); errorNombre = null },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorNombre != null
        )
        if (errorNombre != null) Text(errorNombre!!, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(10.dp))

        // Campo: Correo electrónico
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it.trim(); errorCorreo = null },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = errorCorreo != null
        )
        if (errorCorreo != null) Text(errorCorreo!!, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(10.dp))

        // Campo: Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it; errorContrasena = null },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorContrasena != null
        )
        if (errorContrasena != null) Text(errorContrasena!!, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(10.dp))

        // Campo: Confirmar contraseña
        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it; errorConfirmar = null },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorConfirmar != null
        )
        if (errorConfirmar != null) Text(errorConfirmar!!, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(20.dp))

        // Botón de registro
        Button(
            onClick = {
                var valido = true

                // Validaciones
                if (nombre.length < 3) {
                    errorNombre = "Debe tener al menos 3 letras"
                    valido = false
                } else if (usuarios.any { it.nombre.equals(nombre, ignoreCase = true) }) {
                    errorNombre = "El nombre ya está registrado"
                    valido = false
                }

                val dominios = listOf("@gmail.com", "@hotmail.es", "@outlook.com", "@yahoo.com")
                if (!dominios.any { correo.endsWith(it) }) {
                    errorCorreo = "Correo no válido (usa Gmail, Hotmail, Outlook o Yahoo)"
                    valido = false
                }

                if (contrasena.length !in 4..10) {
                    errorContrasena = "Debe tener entre 4 y 10 caracteres"
                    valido = false
                }

                if (contrasena != confirmar) {
                    errorConfirmar = "Las contraseñas no coinciden"
                    valido = false
                }

                // Si todo es válido, agregar usuario y volver al login
                if (valido) {
                    usuarios.add(Usuario(nombre, correo, contrasena))
                    nombre = ""
                    correo = ""
                    contrasena = ""
                    confirmar = ""
                    Toast.makeText(contexto, "Registro exitoso 🎮", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Enlace para ir al login
        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate("login") {
                    popUpTo("registro") { inclusive = true }
                }
            }
        )
    }
}
