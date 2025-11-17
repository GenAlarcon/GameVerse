package cl.duoc.gameverse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.gameverse.domain.model.Post

class ForumViewModel : ViewModel() {


    var posts by mutableStateOf<List<Post>>(emptyList())
        private set
    var favoritos by mutableStateOf<Set<String>>(emptySet())
        private set

    init {
        posts = listOf(
            Post("1", "Nuevo juego lanzado", "¡Acaba de salir 'Cyberpunk 2'!", "Novedades", "Admin"),
            Post("2", "Evento especial", "No se pierdan el evento de doble XP este finde.", "Proximamente", "Admin"),
            Post("3", "Mejores tips", "Mi tip es: no corras y siempre guarda la partida.", "Juegos", "Gamer1"),
            Post("4", "Top tendencias", "El juego más visto esta semana es Elden Ring, otra vez.", "Tendencia", "Gamer2"),
            Post("5", "Actualización 1.2", "El parche 1.2 arregla bugs de físicas en el juego.", "Novedades", "Admin"),
            Post("6", "Torneo online", "¡Inscripciones abiertas para el torneo de Valorant!", "Proximamente", "Juan")
        )
    }

    fun agregarPost(post: Post) {
        posts = posts + post
    }


    fun eliminarPost(postId: String) {
        posts = posts.filter { it.id != postId }
        favoritos = favoritos - postId
    }

    fun toggleFavorito(postId: String) {
        favoritos = if (favoritos.contains(postId)) {
            favoritos - postId
        } else {
            favoritos + postId
        }
    }
    fun getPostsByCategory(categoria: String): List<Post> {
        return posts.filter { it.categoria == categoria }
    }
    fun getPostsByAuthor(authorName: String): List<Post> {
        return posts.filter {
            it.autor.equals(authorName, ignoreCase = true) &&
                    it.categoria == null
        }
    }
}
