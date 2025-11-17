package cl.duoc.gameverse.ui.viewmodel

import cl.duoc.gameverse.domain.model.Forum

import androidx.lifecycle.ViewModel
import cl.duoc.gameverse.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForumProximosViewModel : ViewModel() {

    private val _proximosForos = MutableStateFlow<List<Forum>>(emptyList())
    val proximosForos: StateFlow<List<Forum>> = _proximosForos.asStateFlow()

    init {
        cargarForosProximos()
    }

    private fun cargarForosProximos() {
        _proximosForos.value = listOf(
            Forum(
                id = "001",
                titulo = "SOBRE LA VERSION 6.3",
                contenido = "Columbina tiene sinergia con los personajes de Nod-Krai que llegaron antes de la versión 6.3, pero también funcionará muy bien con la Adeptus Yegua Blanca.\n" +
                        "Ambas llegarán en la 6.3.",
                categoria = "Juego",
                autor = "GameVerse Tester",
                imagen = R.drawable.banner_gi,
                comentarios = 14,
                fechaEstimado = "Diciembre 2025",
                estado = "En Desarrollo ⭐"
            ),
            Forum(
                id = "002",
                titulo = "LA BETA DE PETIT PLANET A CERRADO",
                contenido = "Petit Planet, es un juego de simulación de vida y exploración de planetas, puede recordar a juegos como Animal Crossing por ejemplo.",
                categoria = "Beta",
                autor = "GameVerse Official",
                imagen = R.drawable.banner_pp,
                comentarios = 48,
                fechaEstimado = "Noviembre 2025",
                estado = "Beta Cerrada 🔒"
            ),
            Forum(
                id = "003",
                titulo = "Versión 2.4 «Al borde del abismo» de Zenless Zone Zero",
                contenido = "El guionista de Zenless Zone Zero confirma que algunos de los antiguos Void Hunters siguen vivos.",
                categoria = "Evento",
                autor = "GameVerse Official",
                imagen = R.drawable.banner_zzz,
                comentarios = 6,
                fechaEstimado = "Noviembre 2025",
                estado = "Confirmado ✅"
            ),
            Forum(
                id = "004",
                titulo = "¡Todo sobre Squid Craft 4!",
                contenido = "El próximo 19 de noviembre dará comienzo una de las temporadas más esperadas en las series de creadores de contenido.",
                categoria = "Evento",
                autor = "GameVerse Official",
                imagen = R.drawable.banner_minecraft,
                comentarios = 66,
                fechaEstimado = "Noviembre 2025",
                estado = "Confirmado ✅"
            )
        )
    }
}
