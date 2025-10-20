package com.example.fakestore.products

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun watchAll(): Flow<List<ProductEntity>>

    suspend fun getAll(): List<ProductEntity>

    suspend fun getProduct(id: Int): ProductEntity?
}

/// Product repository Implementation
class ProductRepositoryImpl(
    val service: ProductService,
    val dao: ProductDao,
) : ProductRepository {

    override fun watchAll(): Flow<List<ProductEntity>> {
        return dao.watchAll()
    }

    override suspend fun getAll(): List<ProductEntity> {
        val entities = mutableListOf<ProductEntity>()
        try {
            /// step 1: fetch all Product from Api Service
            val products = service.getProducts()
            for (item in products) {
                val entity = item.toEntity()
                entities.add(entity)
            }
            
            /// clean this table
            dao.deleteAll()

            /// step 2: Store all product in the database
            dao.insertAll(entities)
        } catch (e: Exception) {
            throw e
        }
        return entities
    }

    override suspend fun getProduct(id: Int): ProductEntity? {
        return dao.read(id)
    }
}