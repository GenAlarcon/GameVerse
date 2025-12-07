package cl.duoc.gameverse.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.duoc.gameverse.R
import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.domain.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Game::class, Usuario::class], version = 3, exportSchema = false)
abstract class GameRoomDB : RoomDatabase() {

    abstract fun gameDao(): GameDao
    abstract fun usuarioDao(): UsuarioDao

    private class GameDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context // Necesitamos el contexto para crear la URI
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

            // Función de ayuda para convertir R.drawable.id (Int) -> "android.resource://..." (String)
            fun getUri(resId: Int): String {
                return "android.resource://${context.packageName}/$resId"
            }

            // Usamos getUri(...) para pasar Strings, no Ints
            val misJuegos = listOf(
                Game("1", "Elden Ring", "RPG de Acción", getUri(R.drawable.eldenring)),
                Game("2", "Cyberpunk 2077", "RPG de Acción", getUri(R.drawable.cyberpunk)),
                Game("3", "Stardew Valley", "Simulación", getUri(R.drawable.stardewvalley)),
                Game("4", "Valorant", "Shooter Táctico", getUri(R.drawable.valorant)),
                Game("5", "The Witcher 3", "RPG", getUri(R.drawable.thewitcher))
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
                    "game_database"
                )
                    // Pasamos 'context' al callback
                    .addCallback(GameDatabaseCallback(scope, context))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}