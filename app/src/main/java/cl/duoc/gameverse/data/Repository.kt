package cl.duoc.gameverse.data

import cl.duoc.gameverse.domain.model.Game
import kotlinx.coroutines.flow.Flow

class Repository(private val gameDao: GameDao) {
    val juegos: Flow<List<Game>> = gameDao.getAlphabetizedGames()
    suspend fun insert(game: Game) {
        gameDao.insert(game)
    }
}