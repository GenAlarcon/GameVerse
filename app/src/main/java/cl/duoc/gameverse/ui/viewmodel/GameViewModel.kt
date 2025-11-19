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

data class GameCatalogUiState(
    val juegos: List<Game> = emptyList(),
    val isLoading: Boolean = false
)

class GameViewModel(private val repository: Repository) : ViewModel() {
    val uiState: StateFlow<GameCatalogUiState> = repository.juegos
        .map { listaJuegos ->
            GameCatalogUiState(juegos = listaJuegos, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameCatalogUiState(isLoading = true)
        )

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