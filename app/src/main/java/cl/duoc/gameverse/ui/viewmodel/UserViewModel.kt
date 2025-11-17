package cl.duoc.gameverse.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import cl.duoc.gameverse.domain.model.Usuario
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.domain.model.JuegoUsuario

class UserViewModel : ViewModel() {

    var usuarios = mutableStateListOf<Usuario>()
        private set
    var usuarioActual = mutableStateOf<Usuario?>(null)
        private set
    var proximasAventuras by mutableStateOf<List<JuegoUsuario>>(emptyList())
        private set
    var juegoEnCurso by mutableStateOf<JuegoUsuario?>(null)
        private set
    fun registrarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }
    fun validarLogin(identificador: String, contrasena: String): Usuario? {
        val usuario = usuarios.find {
            (it.nombre.equals(identificador, ignoreCase = true) ||
                    it.correo.equals(identificador, ignoreCase = true)) &&
                    it.contrasena == contrasena
        }
        if (usuario != null) {
            usuarioActual.value = usuario
        }

        return usuario
    }
    fun cerrarSesion() {
        usuarioActual.value = null
        // Reseteamos las listas de juegos al cerrar sesión
        juegoEnCurso = null
        proximasAventuras = emptyList()
    }
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
}
