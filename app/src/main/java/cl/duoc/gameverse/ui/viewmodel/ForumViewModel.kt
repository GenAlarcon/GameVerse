package cl.duoc.gameverse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.gameverse.domain.model.Post

class ForumViewModel : ViewModel() {

    // Lista reactiva de posts
    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    // Favoritos: ids de posts
    var favoritos by mutableStateOf<Set<String>>(emptySet())
        private set

    init {
        // Datos de ejemplo
        posts = listOf(
            Post("1", "Novedades de GameVerse", "Noticias", "Gamer1"),
            Post("2", "Cómo mejorar en el juego X", "Preguntas", "Juan"),
            Post("3", "Evento especial este fin de semana", "Eventos", "Gamer1")
        )
    }

    // Agregar post
    fun agregarPost(post: Post) {
        posts = posts + post
    }

    // Eliminar post
    fun eliminarPost(postId: String) {
        posts = posts.filter { it.id != postId }
        favoritos = favoritos - postId
    }

    // Favoritos
    fun toggleFavorito(postId: String) {
        favoritos = if (favoritos.contains(postId)) {
            favoritos - postId
        } else {
            favoritos + postId
        }
    }
}
