package com.example.fakestore.products

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * from table_products")
    fun watchAll(): Flow<List<ProductEntity>>

    @Query("SELECT * from table_products")
    suspend fun readAll(): List<ProductEntity>

    @Query("SELECT * from table_products WHERE id =:id")
    suspend fun read(id: Int): ProductEntity?

    @Delete
    suspend fun delete(productEntity: ProductEntity)

    @Query("DELETE from table_products")
    suspend fun deleteAll()
}