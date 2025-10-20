package com.example.fakestore.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    repository: ProductRepository,
    onProductClick: (Int) -> Unit,
) {
    /// Remember the factory and ViewModel so it's only created once
    val factory = remember {
        ProductViewModelFactory(repository)
    }
    val viewModel: ProductViewModel = viewModel(factory = factory)
    val productUiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Products", fontWeight = FontWeight.SemiBold)
                },
            )
        },
    ) { innerPadding ->
        when (productUiState) {
            is ProductUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductUiState.Error -> {
                val message = (productUiState as ProductUiState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(message)
                }
            }

            is ProductUiState.Success -> {
                val products = (productUiState as ProductUiState.Success).products
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    items(products) { product ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    println("---- Product : $product")
                                    onProductClick(product.id)
                                },
                            shape = RoundedCornerShape(4.dp)

                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    product.title,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Box(modifier = Modifier.size(8.dp))
                                Text(
                                    "Price: $${product.price}",
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                Box(modifier = Modifier.size(8.dp))
                                Text(
                                    product.description,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}