package cl.duoc.gameverse.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.duoc.gameverse.R
import cl.duoc.gameverse.domain.model.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class GameRoomDB : RoomDatabase() {

    abstract fun gameDao(): GameDao

    private class GameDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.gameDao())
                }
            }
        }
        suspend fun populateDatabase(gameDao: GameDao) {
            gameDao.deleteAll()
            val misJuegos = listOf(
                Game("1", "Elden Ring", "RPG de Acción", R.drawable.eldenring),
                Game("2", "Cyberpunk 2077", "RPG de Acción", R.drawable.cyberpunk),
                Game("3", "Stardew Valley", "Simulación", R.drawable.stardewvalley),
                Game("4", "Valorant", "Shooter Táctico", R.drawable.valorant),
                Game("5", "The Witcher 3", "RPG", R.drawable.thewitcher)
            )

            gameDao.insertAll(misJuegos)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: GameRoomDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GameRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameRoomDB::class.java,
                    "game_database" // Nombre del archivo guardado en el celular
                )
                    .addCallback(GameDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}