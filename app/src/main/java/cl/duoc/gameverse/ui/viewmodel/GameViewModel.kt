package cl.duoc.gameverse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.gameverse.data.Repository
import cl.duoc.gameverse.domain.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class GameCatalogUiState(
    val juegos: List<Game> = emptyList(),
    val isLoading: Boolean = false

)

class GameViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameCatalogUiState())
    val uiState: StateFlow<GameCatalogUiState> = _uiState.asStateFlow()

    init {
        cargarCatalogo()
    }


    private fun cargarCatalogo() {
        viewModelScope.launch {
            _uiState.value = GameCatalogUiState(isLoading = true)
            val catalogo = repository.getJuegosDelCatalogo()
            _uiState.value = GameCatalogUiState(juegos = catalogo)
        }
    }
}