package cl.duoc.gameverse.domain.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//tablas en la bbdd
@Entity(tableName = "usuario_tabla")
data class Usuario(
    @ColumnInfo (name = "nombre")
    val nombre: String,
    @PrimaryKey
    val correo: String,
    @ColumnInfo(name = "contrasena")
    @SerializedName("password")
    val contrasena: String
)