package cl.duoc.gameverse.domain.model

import com.google.gson.annotations.SerializedName

data class GameBackendDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("genero")
    val genero: String,

    @SerializedName("imagenUrl")
    val imagenUrl: String?,

    @SerializedName("precio")
    val precio: Double
)