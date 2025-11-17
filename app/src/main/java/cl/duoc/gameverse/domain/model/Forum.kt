package cl.duoc.gameverse.domain.model

data class Forum(
    val id: String,
    val titulo: String,
    val contenido: String,
    val categoria: String?,
    val autor: String,
    val imagen: Int,
    val comentarios: Int,
    val fechaEstimado: String? = null,
    val estado: String? = null
)
