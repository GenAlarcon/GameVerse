package cl.duoc.gameverse.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.gameverse.domain.model.Usuario

class ValidacionesViewModel : ViewModel() {

    // VALIDACIONES REGISTRO

    data class ResultadoValidacion(
        val ok: Boolean,
        val errorNombre: String? = null,
        val errorCorreo: String? = null,
        val errorContrasena: String? = null,
        val errorConfirmar: String? = null
    )

    fun validarRegistro(
        nombre: String,
        correo: String,
        contrasena: String,
        confirmar: String,
        usuariosRegistrados: List<Usuario>
    ): ResultadoValidacion {

        var errorNombre: String? = null
        var errorCorreo: String? = null
        var errorContrasena: String? = null
        var errorConfirmar: String? = null
        var valido = true

        // Validación nombre
        if (nombre.length < 3) {
            errorNombre = "Debe tener al menos 3 letras"
            valido = false
        } else if (usuariosRegistrados.any { it.nombre.equals(nombre, ignoreCase = true) }) {
            errorNombre = "El nombre ya está registrado"
            valido = false
        }

        // Validación correo
        val dominios = listOf("@gmail.com", "@hotmail.es", "@outlook.com", "@yahoo.com")
        if (!dominios.any { correo.endsWith(it) }) {
            errorCorreo = "Correo no válido (usa Gmail, Hotmail, Outlook o Yahoo)"
            valido = false
        }

        // Validación contraseña
        if (contrasena.length !in 4..10) {
            errorContrasena = "Debe tener entre 4 y 10 caracteres"
            valido = false
        }

        // Confirmación contraseña
        if (contrasena != confirmar) {
            errorConfirmar = "Las contraseñas no coinciden"
            valido = false
        }

        return ResultadoValidacion(
            ok = valido,
            errorNombre = errorNombre,
            errorCorreo = errorCorreo,
            errorContrasena = errorContrasena,
            errorConfirmar = errorConfirmar
        )
    }

    // VALIDACIONES LOGIN
    data class ResultadoValidacionLogin(
        val ok: Boolean,
        val error: String? = null
    )

    fun validarLoginCampos(
        identificador: String,
        contrasena: String
    ): ResultadoValidacionLogin {

        if (identificador.isBlank()) {
            return ResultadoValidacionLogin(
                ok = false,
                error = "Debes ingresar tu usuario o correo"
            )
        }

        if (contrasena.isBlank()) {
            return ResultadoValidacionLogin(
                ok = false,
                error = "Debes ingresar tu contraseña"
            )
        }

        if (contrasena.length < 4) {
            return ResultadoValidacionLogin(
                ok = false,
                error = "La contraseña es demasiado corta"
            )
        }

        // Validar formato básico si intenta usar correo
        if ("@" in identificador && !identificador.contains(".")) {
            return ResultadoValidacionLogin(
                ok = false,
                error = "Formato de correo inválido"
            )
        }

        return ResultadoValidacionLogin(ok = true)
    }
}
