package cl.duoc.gameverse.domain.model

data class Post(
    val id: String,
    val titulo: String,
    val contenido: String,
    val categoria: String?,
    val autor: String
)
