package cl.duoc.gameverse.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Este modelo representa una oferta individual que viene de la API de CheapShark.
 * Usamos @SerializedName por si el nombre en el JSON es diferente al de la variable,
 * aunque en este caso coinciden bastante.
 */
data class GameDealDto(
    // Título del juego
    @SerializedName("title")
    val title: String,
    // Precio de oferta actual
    @SerializedName("salePrice")
    val salePrice: String,
    // Precio normal sin descuento
    @SerializedName("normalPrice")
    val normalPrice: String,
    // ID interno de CheapShark
    @SerializedName("gameID")
    val gameID: String,
    // ID de Steam (crucial para obtener la imagen de alta calidad después)
    @SerializedName("steamAppID")
    val steamAppID: String?,
    // Imagen pequeña (miniatura) que provee la API por defecto
    @SerializedName("thumb")
    val thumb: String
)