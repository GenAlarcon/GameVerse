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

    var usuarioActual = mutableStateOf<Usuario?>(null)
        private set
    var loginError by mutableStateOf<String?>(null)
        private set
    var proximasAventuras by mutableStateOf<List<JuegoUsuario>>(emptyList())
        private set
    var juegoEnCurso by mutableStateOf<JuegoUsuario?>(null)
        private set

    fun registrarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                repository.registrarUsuario(usuario)
                usuarioActual.value = usuario
                loginError = null
            } catch (e: Exception) {
                loginError = "El usuario ya existe."
            }
        }
    }

    fun validarLogin(identificador: String, contrasena: String) {
        viewModelScope.launch {
            val usuarioEncontrado = repository.loginUsuario(identificador, contrasena)
            if (usuarioEncontrado != null) {
                usuarioActual.value = usuarioEncontrado
                loginError = null
            } else {
                loginError = "Credenciales incorrectas."
            }
        }
    }

    fun cerrarSesion() {
        usuarioActual.value = null
        juegoEnCurso = null
        proximasAventuras = emptyList()
        loginError = null
    }

    fun limpiarError() {
        loginError = null
    }

    fun agregarAventura(game: Game) {
        // Verificamos que no esté ya en la lista ni en curso
        val yaEstaEnAventuras = proximasAventuras.find { it.game.id == game.id } != null
        val yaEstaEnCurso = juegoEnCurso?.game?.id == game.id

        if (!yaEstaEnAventuras && !yaEstaEnCurso) {
            // Creamos la relación con progreso 0
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
