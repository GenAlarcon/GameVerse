package cl.duoc.gameverse

import android.app.Application
import cl.duoc.gameverse.data.GameRoomDB
import cl.duoc.gameverse.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GameVerseApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { GameRoomDB.getDatabase(this, applicationScope) }
    val repository by lazy { Repository(database.gameDao(), database.usuarioDao()) }
}