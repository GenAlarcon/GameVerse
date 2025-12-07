package cl.duoc.gameverse.network

import cl.duoc.gameverse.domain.model.GameDealDto
import cl.duoc.gameverse.domain.model.GameBackendDto
import retrofit2.Response
import cl.duoc.gameverse.domain.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GameApiService {

    // Obtenemos ofertas de la API de CheapShark
    // Endpoint: https://www.cheapshark.com/api/1.0/deals
    // Documentación: https://apidocs.cheapshark.com/

    @GET("deals")
    suspend fun getDeals(
        // storeID=1 corresponde a Steam
        @Query("storeID") storeId: String = "1",

        // Filtramos juegos con precio menor o igual a 50
        @Query("upperPrice") upperPrice: Int = 50,

        // Cantidad de resultados por página
        @Query("pageSize") pageSize: Int = 20
    ): Response<List<GameDealDto>>


    // 2. MICROSERVICIO DE USUARIOS (Puerto 8082)
    // Base URL: http://10.0.2.2:8082/
    @POST("api/usuarios/registro")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>
    @POST("api/usuarios/login")
    suspend fun loginUsuario(@Body usuario: Usuario): Response<Usuario>
    // 3. MICROSERVICIO DE JUEGOS (Puerto 8081)
    // Base URL: http://10.0.2.2:8081/
    @GET("api/juegos")
    suspend fun getGames(): Response<List<GameBackendDto>>
}