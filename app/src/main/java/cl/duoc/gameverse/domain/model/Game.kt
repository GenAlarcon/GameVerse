package cl.duoc.gameverse.domain.model
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_table")
data class Game(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val nombre: String,

    @ColumnInfo(name = "genre")
    val genero: String,

    @ColumnInfo(name = "image_url")
    val imagen: String
)