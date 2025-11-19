package cl.duoc.gameverse.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duoc.gameverse.domain.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM juegos ORDER BY name ASC")
    fun getAlphabetizedGames(): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)

    @Query("DELETE FROM juegos")
    suspend fun deleteAll()
}