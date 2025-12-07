package cl.duoc.gameverse.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.cheapshark.com/api/1.0/"
    private const val BASE_URL_USERS = "http://10.0.2.2:8082/"
    private const val BASE_URL_GAMES = "http://10.0.2.2:8081/"

    val api: GameApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameApiService::class.java)
    }


    // Cliente para Usuarios
    val apiUsers: GameApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_USERS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameApiService::class.java)
    }

    // Cliente para Juegos
    val apiGames: GameApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_GAMES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameApiService::class.java)
    }
}