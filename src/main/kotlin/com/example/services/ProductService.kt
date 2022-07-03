package com.example.services

import com.example.dao.product.ProductDAO

interface ProductService {
    fun getProducts(): String
}

class ProductServiceImpl(val productDAO: ProductDAO) : ProductService {
    override fun getProducts(): String {
        return productDAO.test()
    }
}
