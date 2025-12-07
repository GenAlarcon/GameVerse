package cl.duoc.gameverse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.gameverse.GameVerseApplication
import cl.duoc.gameverse.data.Repository
import cl.duoc.gameverse.domain.model.GameDealDto
import kotlinx.coroutines.launch

class DealsViewModel(private val repository: Repository) : ViewModel() {

    // Lista de ofertas que se mostrará en la UI
    var ofertas by mutableStateOf<List<GameDealDto>>(emptyList())
        private set

    // Estado de carga para mostrar el círculo giratorio
    var isLoading by mutableStateOf(false)
        private set

    init {
        cargarOfertas()
    }

    private fun cargarOfertas() {
        viewModelScope.launch {
            isLoading = true
            // Llamamos al repositorio (que llama a la API)
            val listaOfertas = repository.obtenerOfertasDeApi()
            ofertas = listaOfertas
            isLoading = false
        }
    }

    // Factory para inyectar el repositorio
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameVerseApplication)
                DealsViewModel(application.repository)
            }
        }
    }
}