package com.simformsolutions.shop.repository;

import com.simformsolutions.shop.dto.PurchaseProductDetails;
import com.simformsolutions.shop.entity.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Integer> {

    @Query(value = "SELECT p.name, " +
            "p.price, " +
            "p.image, " +
            "p.product_id," +
            "pp.size, " +
            "pp.colour, " +
            "pp.quantity, " +
            "c.cart_id " +
            "FROM cart c " +
            "JOIN purchase_product pp ON c.cart_id = pp.cart_id " +
            "JOIN product p ON pp.product_id = p.product_id " +
            "WHERE c.cart_id = ?", nativeQuery = true)
    List<PurchaseProductDetails> productsInCart(int cartId);
}
