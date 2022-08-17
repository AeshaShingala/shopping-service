package com.simformsolutions.shop.repository;

import com.simformsolutions.shop.dto.CartProductDetails;
import com.simformsolutions.shop.dto.PurchaseDetails;
import com.simformsolutions.shop.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @Query(value = "SELECT p.name as name," +
            "p.price as price, " +
            "cp.quantity as quantity " +
            "FROM product p " +
            "JOIN cart_product cp ON cp.product_id = p.product_id " +
            "WHERE cp.cart_id=?"
            , nativeQuery = true)
    List<PurchaseDetails> purchaseDetails(int cartId);

    @Query(value = "SELECT p.name as name, " +
            "p.price as price , " +
            "p.image as image, " +
            "p.product_id as productId, " +
            "cp.size as size, " +
            "cp.cart_product_id as cartProductId, " +
            "cp.colour as colour, " +
            "cp.quantity as quantity, " +
            "pu.purchase_id as purchaseId " +
            "FROM purchase pu " +
            "JOIN cart_product cp ON pu.purchase_id = cp.purchase_id " +
            "JOIN product p ON cp.product_id = p.product_id " +
            "WHERE pu.purchase_id = ?", nativeQuery = true)
    List<CartProductDetails> allPurchaseProducts(int purchaseId);

    Optional<Purchase> findByInvoiceName(String invoiceName);
}