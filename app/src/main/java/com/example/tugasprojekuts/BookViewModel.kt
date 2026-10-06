package com.example.tugasprojekuts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasprojekuts.data.Book
import com.example.tugasprojekuts.data.BookRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Loading : UiState
    data class Success(val books: List<Book>, val query: String) : UiState
    data class Error(val query: String) : UiState
}

class BookViewModel : ViewModel() {
    private val repository = BookRepository()
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        search("indonesia")
    }

    fun search(text: String) {
        val query = text.trim()
        searchJob?.cancel()
        if (query.isEmpty()) {
            _uiState.value = UiState.Success(emptyList(), query)
            return
        }
        searchJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _uiState.value = UiState.Success(repository.search(query), query)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _uiState.value = UiState.Error(query)
            }
        }
    }
}
