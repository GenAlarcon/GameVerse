package cl.duoc.gameverse.domain.model
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "juegos")
data class Game(
    @PrimaryKey
    val id: String,
    @ColumnInfo (name = "name")
    val nombre: String,
    @ColumnInfo ("genero" )
    val genero: String,
    @ColumnInfo(name = "imagen")
    @DrawableRes val imageResId: Int
)