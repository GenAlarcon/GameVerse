package cl.duoc.gameverse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.gameverse.domain.model.Post

class ForumViewModel : ViewModel() {

    // Lista reactiva de posts
    // Se utiliza mutableStateOf para que Compose detecte cambios automáticamente
    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    // Favoritos: ids de posts
    // Set de strings para almacenar los IDs de los posts marcados como favoritos
    var favoritos by mutableStateOf<Set<String>>(emptySet())
        private set

    init {
        posts = listOf(
            Post("1", "Nuevo juego lanzado", "Novedades", "Admin"),
            Post("2", "Evento especial este fin de semana", "Proximamente", "Admin"),
            Post("3", "Mejores tips de juego", "Juegos", "Gamer1"),
            Post("4", "Top tendencias de la semana", "Tendencia", "Gamer2"),
            Post("5", "Actualización del parche 1.2", "Novedades", "Admin"),
            Post("6", "Próximo torneo online", "Proximamente", "Juan")
        )
    }

    // Función para agregar un nuevo post
    fun agregarPost(post: Post) {
        posts = posts + post
    }

    // Función para eliminar un post por su ID
    // También se elimina de la lista de favoritos si estaba marcado
    fun eliminarPost(postId: String) {
        posts = posts.filter { it.id != postId }
        favoritos = favoritos - postId
    }

    // Función para marcar o desmarcar un post como favorito
    fun toggleFavorito(postId: String) {
        favoritos = if (favoritos.contains(postId)) {
            favoritos - postId
        } else {
            favoritos + postId
        }
    }

    // Función para obtener posts filtrados por categoría
    // Devuelve solo los posts cuya categoría coincida con el parámetro
    fun getPostsByCategory(categoria: String): List<Post> {
        return posts.filter { it.categoria == categoria }
    }
}
