package com.simformsolutions.shop.repository;


import com.simformsolutions.shop.dto.CartProductDetails;
import com.simformsolutions.shop.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {

    @Query(value = "SELECT p.name as name, " +
            "p.price as price , " +
            "p.image as image, " +
            "p.product_id as productId," +
            "cp.size as size, " +
            "cp.cart_product_id as cartProductId, " +
            "cp.colour as colour, " +
            "cp.quantity as quantity, " +
            "c.cart_id as cartId " +
            "FROM cart c " +
            "JOIN cart_product cp ON c.cart_id = cp.cart_id " +
            "JOIN product p ON cp.product_id = p.product_id " +
            "WHERE c.cart_id = ?", nativeQuery = true)
    List<CartProductDetails> allProductsInCart(int cartId);

    @Query(value = "SELECT * FROM cart_product p JOIN cart c " +
            "ON c.cart_id=p.cart_id " +
            "WHERE p.product_id=?1 AND p.colour=?2 AND p.size=?3 AND c.cart_id=?4", nativeQuery = true)
    Optional<CartProduct> findProductInCart(int productId, String colour, String size, int cartId);
}
