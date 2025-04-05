package com.example.todolist.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.repo.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val _quote = MutableStateFlow("Carregando frase do dia...")
    val quote = _quote.asStateFlow()

    private val repository = QuoteRepository(application)

    private fun fetchQuote() {
        viewModelScope.launch {
            repository.getQuote(
                callback = { result ->
                    _quote.value = result
                },
                onError = { error ->
                    _quote.value = error
                }
            )
        }
    }

    init {
        fetchQuote()
    }
}