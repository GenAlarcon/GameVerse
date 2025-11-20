package cl.duoc.gameverse.domain.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario_tabla")
data class Usuario(
    @ColumnInfo (name = "nombre")
    val nombre: String,
    @PrimaryKey
    val correo: String,
    @ColumnInfo (name = "contrasena")
    val contrasena: String
)