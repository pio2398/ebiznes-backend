package com.example.services

import com.example.cart.dto.Cart
import com.example.cart.dto.CartContent
import com.example.exceptions.InvalidInputException
import com.example.models.UserCarts
import com.example.products.dto.ProductDetails
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

interface CartService {
    @Throws(InvalidInputException::class)
    fun addProductToCart(user_id: Int?, product_id: Int?);

    @Throws(InvalidInputException::class)
    fun removeProductFromCart(user_id: Int?, product_id: Int?);

    @Throws(InvalidInputException::class)
    fun getCart(user_id: Int?): Cart;
}

class CartServiceImpl(
    private val userService: UserService,
    private val productService: ProductService,
    private val databaseFactory: DatabaseFactory
) : CartService {
    @Throws(InvalidInputException::class)
    override fun addProductToCart(user_id: Int?, product_id: Int?) {
        if (user_id == null) {
            throw InvalidInputException("Not valid user_id.")
        }
        if (product_id == null) {
            throw InvalidInputException("Not valid product_id.")
        }

        val user = userService.userById(user_id) ?: throw InvalidInputException("Not valid user_id.")
        val product = productService.getProduct(product_id) ?: throw InvalidInputException("Not valid product_id.")

        transaction(databaseFactory.dataBase) {
            UserCarts.insert {
                it[UserCarts.user] = user.id
                it[UserCarts.product] = product.category_id
            }
        }
    }

    override fun removeProductFromCart(user_id: Int?, product_id: Int?) {
        if (user_id == null) {
            throw InvalidInputException("Not valid user_id.")
        }
        if (product_id == null) {
            throw InvalidInputException("Not valid product_id.")
        }
        transaction(databaseFactory.dataBase) {
            UserCarts.deleteWhere {
                (UserCarts.user eq user_id) and (UserCarts.product eq product_id)
            }

        }
    }

    override fun getCart(user_id: Int?): Cart {
        println(user_id)
        if (user_id == null) {
            throw InvalidInputException("Not valid user_id.")
        }
        val user = userService.userById(user_id) ?: throw InvalidInputException("Not valid user_id.")
        var totalPrice = 0
        var cartContent = mutableListOf<CartContent>()
        transaction(databaseFactory.dataBase) {
            for (product in user.cart) {
                cartContent.add(
                    CartContent(product.id.value, ProductDetails(product), 1, product.price)
                )
                totalPrice += product.price
            }
        }

        return Cart(user_id, totalPrice, cartContent);
    }

}