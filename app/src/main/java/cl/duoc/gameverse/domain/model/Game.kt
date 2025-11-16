package cl.duoc.gameverse.domain.model
import androidx.annotation.DrawableRes

data class Game(
    val id: String,
    val nombre: String,
    val genero: String,
    @DrawableRes val imageResId: Int
)