package com.example.fakestore.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<ProductEntity>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}

class ProductViewModel(val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadProducts()
            watchProducts()
        }
    }

    /// Fetch Products from Api & Store into Database
    private suspend fun loadProducts() {
        try {
            _uiState.value = ProductUiState.Loading
            /// Fetch Products from Api and Store them in the Database
            repository.getAll()
        } catch (e: Exception) {
            _uiState.value = ProductUiState.Error("Failed to fetch products: ${e.message}")
        }
    }

    /// Observe the Products
    private suspend fun watchProducts() {
        try {
            repository.watchAll().collectLatest {
                _uiState.value = ProductUiState.Success(it)
            }
        } catch (e: Exception) {
            _uiState.value = ProductUiState.Error("Failed to watch products: ${e.message}")
        }
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(repository) as T
    }
}