package com.example.fakestore.products

import androidx.room.Entity
import androidx.room.PrimaryKey

/// Local Product Table for Room Database
@Entity("table_products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String
)

/// Use to transfer the data from app to cloud and vice versa.
data class ProductDTO(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String
)

/// Convert Cloud Product Data to Local Database Table
fun ProductDTO.toEntity(): ProductEntity {
    return ProductEntity(
        id,
        title,
        description,
        price,
        image
    )
}
