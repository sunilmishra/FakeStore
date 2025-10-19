package com.example.fakestore.products

import retrofit2.http.GET

interface ProductService {

    @GET("/products")
    suspend fun getProducts(): List<ProductDTO>
}