package cl.duoc.gameverse.data

import android.util.Log
import cl.duoc.gameverse.domain.model.GameBackendDto
import cl.duoc.gameverse.domain.model.GameDealDto
import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.domain.model.Usuario
import cl.duoc.gameverse.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

class Repository(
    private val gameDao: GameDao,
    private val usuarioDao: UsuarioDao
) {

    // --- 1. JUEGOS (Microservicio 8081 + Room) ---
    val juegos: Flow<List<Game>> = gameDao.getAlphabetizedGames()

    suspend fun insert(game: Game) {
        gameDao.insert(game)
    }

    suspend fun cargarJuegosDesdeApi() {
        try {
            Log.d("REPO", "Conectando a microservicio 8081...")
            // 1. Pedimos los datos "crudos" a la API (Son GameBackendDto)
            val response = RetrofitClient.apiGames.getGames()

            if (response.isSuccessful) {
                // Obtenemos la lista (puede ser nula, así que usamos emptyList)
                val listaBackend: List<GameBackendDto> = response.body() ?: emptyList()

                if (listaBackend.isNotEmpty()) {

                    val juegosMapeados = listaBackend.map { dto ->
                        Game(
                            id = dto.id.toString(),      // Convertimos Long a String
                            nombre = dto.titulo,         // titulo -> nombre
                            genero = dto.genero,         // genero -> genero
                            imagen = dto.imagenUrl ?: "" // imagenUrl -> imagen (manejamos nulos)
                        )
                    }

                    // 2. Ahora sí guardamos la lista de 'Game' (que es lo que el DAO acepta)
                    gameDao.deleteAll()
                    gameDao.insertAll(juegosMapeados)

                    Log.d("REPO", "¡Catálogo actualizado con ${juegosMapeados.size} juegos!")
                }
            } else {
                Log.e("REPO", "Error Backend: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("REPO", "Fallo conexión Backend: ${e.message}")
        }
    }

    // --- 2. USUARIOS (Microservicio 8082) ---
    suspend fun registrarUsuario(usuario: Usuario) {
        try {
            val response = RetrofitClient.apiUsers.registrarUsuario(usuario)
            if (response.isSuccessful) {
                usuarioDao.registrar(usuario)
            } else {
                throw Exception("Error API: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun loginUsuario(identificador: String, contrasena: String): Usuario? {
        return try {
            val loginRequest = Usuario(nombre = "", correo = identificador, contrasena = contrasena)
            val response = RetrofitClient.apiUsers.loginUsuario(loginRequest)

            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    try { usuarioDao.registrar(user) } catch(_: Exception){}
                    return user
                }
            }
            null
        } catch (e: Exception) {
            usuarioDao.login(identificador, contrasena)
        }
    }

    // --- 3. OFERTAS EXTERNAS ---
    suspend fun obtenerOfertasDeApi(): List<GameDealDto> {
        return try {
            val response = RetrofitClient.api.getDeals()
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}