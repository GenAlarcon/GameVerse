package cl.duoc.gameverse.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duoc.gameverse.domain.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    // Consulta a la tabla correcta "game_table"
    @Query("SELECT * FROM game_table ORDER BY name ASC")
    fun getAlphabetizedGames(): Flow<List<Game>>

    // Inserta un solo juego
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    // Inserta una lista de juegos
    // Room verifica que 'Game' sea una @Entity válida.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)

    // Borra todo
    @Query("DELETE FROM game_table")
    suspend fun deleteAll()
}