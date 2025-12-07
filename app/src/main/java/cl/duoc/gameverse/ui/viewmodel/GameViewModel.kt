package cl.duoc.gameverse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.gameverse.GameVerseApplication
import cl.duoc.gameverse.data.Repository
import cl.duoc.gameverse.domain.model.Game
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GameCatalogUiState(
    val juegos: List<Game> = emptyList(),
    val isLoading: Boolean = false
)

class GameViewModel(private val repository: Repository) : ViewModel() {

    // 1. FLUJO DE DATOS (ROOM -> UI)
    // Nos suscribimos a la base de datos local. Si algo cambia ahí, la UI se entera.
    val uiState: StateFlow<GameCatalogUiState> = repository.juegos
        .map { listaJuegos ->
            // Si llegan datos de la BD, actualizamos la lista y quitamos la carga
            GameCatalogUiState(juegos = listaJuegos, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameCatalogUiState(isLoading = true) // Carga inicial
        )

    // 2. SINCRONIZACIÓN (API -> ROOM)
    // Al abrir la pantalla, pedimos datos frescos al servidor
    init {
        cargarJuegos()
    }

    private fun cargarJuegos() {
        viewModelScope.launch {
            // Esta función conecta con el puerto 8081, baja el JSON
            // y lo guarda en la base de datos local (Room).
            repository.cargarJuegosDesdeApi()
        }
    }

    // 3. FÁBRICA (Inyección de Dependencias)
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameVerseApplication)
                val repository = application.repository
                GameViewModel(repository)
            }
        }
    }
}