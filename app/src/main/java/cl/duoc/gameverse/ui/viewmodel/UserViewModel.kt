package cl.duoc.gameverse.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import cl.duoc.gameverse.domain.model.Usuario

class UserViewModel : ViewModel() {

    // Lista de usuarios almacenada en memoria
    var usuarios = mutableStateListOf<Usuario>()
        private set

    //  Usuario actualmente logueado
    var usuarioActual = mutableStateOf<Usuario?>(null)
        private set

    // Registrar un nuevo usuario
    fun registrarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }

    // Validar login
    fun validarLogin(identificador: String, contrasena: String): Usuario? {
        val usuario = usuarios.find {
            (it.nombre.equals(identificador, ignoreCase = true) ||
                    it.correo.equals(identificador, ignoreCase = true)) &&
                    it.contrasena == contrasena
        }

        // Si el login es correcto, guardamos al usuario
        if (usuario != null) {
            usuarioActual.value = usuario
        }

        return usuario
    }
    fun cerrarSesion() {
        usuarioActual.value = null
    }
}
