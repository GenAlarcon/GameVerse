package cl.duoc.gameverse.data

import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

class Repository(private val gameDao: GameDao,
                 private val usuarioDao: UsuarioDao) {

    val juegos: Flow<List<Game>> = gameDao.getAlphabetizedGames()
    suspend fun insert(game: Game) {
        gameDao.insert(game)
    }

    suspend fun registrarUsuario(usuario: Usuario) {
        usuarioDao.registrar(usuario)
    }
    suspend fun loginUsuario(identificador: String, contrasena: String): Usuario? {
        return usuarioDao.login(identificador, contrasena)
    }
}