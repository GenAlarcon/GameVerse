package cl.duoc.gameverse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.gameverse.GameVerseApplication
import cl.duoc.gameverse.data.Repository
import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.domain.model.JuegoUsuario
import cl.duoc.gameverse.domain.model.Usuario
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {

    // --- ESTADO DE USUARIO (Persistente en BD) ---
    var usuarioActual = mutableStateOf<Usuario?>(null)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    // --- ESTADO DE JUEGOS (En memoria por sesión) ---
    // (Estas listas se reinician al cerrar la app)
    var proximasAventuras by mutableStateOf<List<JuegoUsuario>>(emptyList())
        private set

    var juegoEnCurso by mutableStateOf<JuegoUsuario?>(null)
        private set


    // ----------------------------------------------------------------
    // FUNCIONES DE BASE DE DATOS (Login y Registro)
    // ----------------------------------------------------------------

    fun registrarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                // 1. Intentamos registrar en la Nube y Local
                repository.registrarUsuario(usuario)

                // 2. Si no hubo error, actualizamos el estado para entrar
                usuarioActual.value = usuario
                loginError = null
            } catch (e: Exception) {
                // Capturamos el error que lanza el repositorio (ej. "Error API: 409 Conflict")
                val mensaje = e.message ?: "Error desconocido"
                if (mensaje.contains("409") || mensaje.contains("existe")) {
                    loginError = "El usuario ya existe."
                } else if (mensaje.contains("Failed to connect")) {
                    loginError = "No se pudo conectar al servidor."
                } else {
                    loginError = "Error al registrar: $mensaje"
                }
            }
        }
    }

    fun validarLogin(identificador: String, contrasena: String) {
        viewModelScope.launch {
            // Limpiamos errores previos
            loginError = null

            try {
                // El repositorio se encarga de probar en la Nube y luego en Local
                val usuarioEncontrado = repository.loginUsuario(identificador, contrasena)

                if (usuarioEncontrado != null) {
                    usuarioActual.value = usuarioEncontrado
                    loginError = null
                } else {
                    // Si retorna null, es credenciales inválidas
                    loginError = "Credenciales incorrectas."
                }
            } catch (e: Exception) {
                val mensaje = e.message ?: ""
                if (mensaje.contains("401")) {
                    loginError = "Credenciales incorrectas."
                } else if (mensaje.contains("Failed to connect")) {
                    // Si falló la nube, el repo debió haber intentado local.
                    // Si llegamos aquí, es que ambos fallaron o algo grave pasó.
                    loginError = "Error de conexión y usuario no encontrado localmente."
                } else {
                    loginError = "Error de inicio de sesión: $mensaje"
                }
            }
        }
    }

    fun cerrarSesion() {
        usuarioActual.value = null
        // Limpiamos datos de sesión
        juegoEnCurso = null
        proximasAventuras = emptyList()
        loginError = null
    }

    fun limpiarError() {
        loginError = null
    }


    // ----------------------------------------------------------------
    // FUNCIONES DE JUEGOS (Lógica de listas en memoria)
    // ----------------------------------------------------------------

    fun agregarAventura(game: Game) {
        val yaEstaEnAventuras = proximasAventuras.find { it.game.id == game.id } != null
        val yaEstaEnCurso = juegoEnCurso?.game?.id == game.id

        if (!yaEstaEnAventuras && !yaEstaEnCurso) {
            val nuevoJuegoUsuario = JuegoUsuario(game = game, progreso = 0.0f)
            proximasAventuras = proximasAventuras + nuevoJuegoUsuario
        }
    }

    fun moverJuegoA_EnCurso(juego: JuegoUsuario) {
        val juegoQueEstabaEnCurso = juegoEnCurso

        juegoEnCurso = juego

        proximasAventuras = proximasAventuras.filter { it.game.id != juego.game.id }

        if (juegoQueEstabaEnCurso != null) {
            proximasAventuras = proximasAventuras + juegoQueEstabaEnCurso
        }
    }

    fun actualizarProgresoEnCurso(nuevoProgreso: Float) {
        juegoEnCurso?.let {
            juegoEnCurso = it.copy(progreso = nuevoProgreso)
        }
    }

    // ----------------------------------------------------------------
    // FACTORY
    // ----------------------------------------------------------------
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameVerseApplication)
                val repository = application.repository
                UserViewModel(repository)
            }
        }
    }
}