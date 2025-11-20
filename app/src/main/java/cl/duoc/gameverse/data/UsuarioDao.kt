package cl.duoc.gameverse.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duoc.gameverse.domain.model.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrar(usuario: Usuario)

    @Query("SELECT * FROM usuario_tabla WHERE (correo = :identificador OR nombre = :identificador) AND contrasena = :contrasena LIMIT 1")
    suspend fun login(identificador: String, contrasena: String): Usuario?

    @Query("SELECT * FROM usuario_tabla WHERE correo = :correo")
    suspend fun getUsuarioPorCorreo(correo: String): Usuario?
}